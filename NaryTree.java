/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.common;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaryTree<T extends NaryNode>  implements Jsonable{

    T root;

    /**
     *
     * @param root - root node
     */
    public NaryTree(T root) {
        this.root = root;
    }

    /**
     *
     * @return - root node
     */
    public T getRoot() {
        return root;
    }

    /**
     *
     * @return - string
     */
    @Override
    public String toString() {
        List<NaryPath> allPaths = getAllPaths();
        String str = StringUtils.EMPTY;
        for(NaryPath path:allPaths){
            str += path + "\n";
        }
        return str;
    }

    /**
     *
     * @return - all paths for the tree
     */
    public List<NaryPath> getAllPaths(){
        List<NaryPath> allPaths = new ArrayList<>();
        if (root != null) {
            NaryPath path = new NaryPath<NaryNode>();
            buildPath(root, path, allPaths);
        }
        allPaths.removeIf(p->p.isMarked());
        Collections.sort(allPaths,new NaryPath.PathComparator());
        return allPaths;
    }

    /**
     *
     * @param node - start node
     * @param path - path to build
     * @param allPaths - all paths so far
     */
    private void buildPath(NaryNode node, NaryPath path, List<NaryPath> allPaths){
        path.nodes.add(node);

        if (node.getChildren().isEmpty()  || node.isMarked()){
            NaryPath np = new NaryPath(path);
            if(StringUtils.isNotEmpty(np.toJson()) && !allPaths.contains(np)) {
                allPaths.add(np);
            }
            path.removeLast();
            return;
        }
        for (NaryNode child:node.getChildren()){
            buildPath(child,path,allPaths);
        }
        path.removeLast();
    }

    /**
     *
     * @return - json string
     */
    public String toJson(){
        List<NaryPath> allPaths = getAllPaths();

        String json = "[";
        for (NaryPath path : allPaths){
            json += path.toJson() + ",";
        }
        if(json.endsWith(",")){
            json = json.substring(0, json.lastIndexOf(","));

        }
        json += "]";
        return json;
    }

}
