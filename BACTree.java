/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.objects;

import org.nyumc.isg.lims.common.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BACTree  implements Jsonable{
    NaryTree<BACNode> tree;

    /**
     * Constructor
     * @param locus - locus of interest which needs to be covered by BAC (tree)
     * @throws LimsException - Lims Exception
     * @throws SQLException - SQL exception
     */
    public BACTree(LocusOfInterest locus) throws LimsException, SQLException{
        BACNode root = getBACNode(locus);
        tree = new NaryTree(root);
    }

    /**
     *  finds children for the given parent node, that cover [remaining] region
     * @param parent - parent node
     * @param connection - database connection
     * @param table - database table
     * @param chr - chromosome
     * @param start - start coordinate of the region
     * @param end - end coordinate of the region
     * @throws SQLException - SQL exception
     */
    private void findChildren(NaryNode parent, Connection connection, String table, String chr, int start, int end )
            throws SQLException {
        BACNode bacParent = (BACNode) parent;
        PreparedStatement ps;
        String sql;
        String groupByLibrarySql = "select substring(name,1,4) as library, end_loc " +
                " from lims." + table + " where chromosome = '" + chr +
                "'  and start_loc <= ? and end_loc >= ? group by library, end_loc order by library" ;

        if ( start != end && bacParent != null && bacParent.getBac() != null) {
            // 4) try to find bacs that for each library can complete the solution
            //                                    ------------------------
            //                     ---------------------------
            //      ---------------------
            //         |---------------------------------------------|
            String library = bacParent.getBac().getName().substring(0, 3);
            int bacParentStart = bacParent.getBac().getStart();
            int bacParentEnd = bacParent.getBac().getEnd();
            sql = "select * from lims." + table +
                    " where chromosome = '" + chr + "'  and start_loc <= ? and end_loc >= ? " ;
            sql += " and start_loc  > " + bacParentStart + " and end_loc > " + bacParentEnd;
            sql += " and name like '" + library + "%'";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, end);
        } else if (start == end && bacParent != null && bacParent.getBac() != null){
            // 3) try to find bacs that for each library cover most of the remaining region
            //                     ---------------------------
            //      ---------------------
            //         |---------------------------------------------|

            String library = bacParent.getBac().getName().substring(0, 3);
            int bacParentStart = bacParent.getBac().getStart();
            int bacParentEnd = bacParent.getBac().getEnd();
            sql = "select * from lims." + table + " A, (" + groupByLibrarySql + ") B" +
                    " where chromosome = '" + chr + "'  and A.start_loc <= ? and A.end_loc >= ? " ;
            sql += " and A.start_loc  > " + bacParentStart + " and A.end_loc > " + bacParentEnd;
            sql += " and A.name like '" + library + "%'";
            sql += " and A.end_loc =  B.end_loc";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, end);
            ps.setInt(3, start);
            ps.setInt(4, end);
        }
         else if (start == end) {
             // 2) try to find single bacs that for each library cover most of the region
             //    ---------------------------------- (RP11)
             //      ------------------------         (CH17)
             //         |---------------------------------------------|
            sql = "select distinct * from lims." + table + " A, (" + groupByLibrarySql + ") B" +
                    " where A.chromosome = '" + chr + "'  and A.start_loc <= ? and A.end_loc > ? " ;
            sql += " and A.end_loc =  B.end_loc";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, end);
            ps.setInt(3, start);
            ps.setInt(4, end);

        } else {
             // 1) try to find single bac solutions that cover the region completely
             //    ------------------------------------------------------- (RP11)
             //      ----------------------------------------------------------------------         (CH17)
             //         |---------------------------------------------|
            sql = "select distinct * from lims." + table +
                    " where chromosome = '" + chr + "'  and start_loc <= ? and end_loc >= ? " ;
            ps = connection.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, end);
        }

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            BAC bac = new BAC(rs);
            BACNode node = new BACNode(bac);
            if (parent != null && (parent.getChildren() != null) && !parent.getChildren().contains(node)) {
                parent.getChildren().add(new BACNode(bac));
            }
        }
    }

    /**
     * recursively populates BAC tree
     * @param solutionFound - indicates whether the solution has been found
     * @param parent - parent node
     * @param connection - database connection
     * @param table - database table (bac library) name
     * @param chr - chromosome
     * @param start - start coordinate
     * @param end - end coordinate
     * @throws SQLException - SQL exception
     */
    private void getBACs(Boolean solutionFound, NaryNode parent, Connection connection, String table, String chr,
                         int start, int end ) throws SQLException{
        if (solutionFound)
            return;
        findChildren(parent, connection, table,chr, start,end);
        solutionFound = !parent.getChildren().isEmpty();
        if (solutionFound || parent.isMarked())
            return;
        else{
            findChildren(parent, connection, table,chr, start,start);
            if (parent.getChildren().isEmpty()){
                parent.mark(true);
                return;
            }
            for(NaryNode node : parent.getChildren()) {
                int newStart = ((BACNode) node).getBac().getEnd();
                if (newStart <= start)
                    node.mark(true);
                if (!node.isMarked()) {
                    getBACs(solutionFound, node, connection, table,chr, newStart, end);
                }
            }
        }
    }

    /**
     *
     * @param locus - locus of interest
     * @return  - BAC node tat is a root of the tree covering the locus
     * @throws LimsException - Lims exception
     * @throws SQLException - SQL exception
     */
    private  BACNode getBACNode(LocusOfInterest locus) throws LimsException, SQLException {
        BACNode root = new BACNode();
        String table = "bac_" + locus.getSource(locus.getOrganism());
        Connection connection = DataSource.getInstance().getConnection();

        Boolean solutionFound = Boolean.FALSE;
        getBACs(solutionFound, root, connection, table, locus.getChromosome(),
                locus.getOpenPosition(), locus.getClosePosition());
        root.getChildren().sort(new BACNode.NodeComparator());
        return root;
    }

    /**
     *
     * @return list of BAC paths covering locus
     */
    public List<NaryPath> getBACPaths() {
        return tree.getAllPaths();
    }

    /**
     *
     * @return tree as json string
     */
    public String toJson(){
        return tree.toJson();
    }

}
