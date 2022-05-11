/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.objects;
import org.apache.commons.lang3.StringUtils;
import org.nyumc.isg.lims.common.NaryNode;

import java.util.*;

/**
 * BACNode clase as derived class of NaryNode
 */
public class BACNode implements NaryNode {
    static class NodeComparator implements Comparator<NaryNode> {
        public int compare(NaryNode node1, NaryNode node2) {
            return Integer.compare(node1.getChildren().size(), node2.getChildren().size());
        }
    }
    private boolean mark;
    private final BAC bac;
    private final List<NaryNode> children;

    /**
     * Default constructor
     */
    BACNode() {
        bac = null;
        children = new ArrayList<>();
    }

    /**
     * Constructor
     * @param bac object
     */
    BACNode(BAC bac) {
        this.bac = bac;
        children = new ArrayList<>();
    }

    /**
     *
     * @param other bac node object
     * @return true, if bac objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if (this.bac != null && this.getClass().equals(other.getClass())){
            BACNode otherNode = (BACNode)other;
            return this.bac.equals(otherNode.bac) && this.getChildren().equals(otherNode.getChildren());
        }
        return false;
    }

    /**
     *
     * @return embedded bac object
     */
    public BAC getBac() {
        return bac;
    }

    /**
     *
     * @return array of children bac nodes
     */
    public List<NaryNode> getChildren() {
        return children;
    }

    /**
     *
     * @return jsin string representation of the node
     */
    public String toJson(){
        if (bac != null)
            return bac.toJson();
        return StringUtils.EMPTY;
    }

    /**
     *
     * @return string representation of the node
     */
    @Override
    public String toString() {
        if (bac != null)
            return "BACNode{" + "bac=" + bac + '}';
        return StringUtils.EMPTY;
    }

    /**
     * marks node as either traversed or not
     * @param value
     */
    public void mark(boolean value){
        mark = value;
    }

    /**
     *
     * @return true if the node marked as traversed, false otherwise
     */
    public boolean isMarked(){
        return mark;
    }

    /**
     *
     * @return embedded bac object start coordinate
     */
    public int getStart(){
        if(bac != null)
            return bac.getStart();
        else
            return -1;
    }

    /**
     *
     * @return embedded bac object end coordinate
     */
    public int getEnd(){
        if(bac != null)
            return bac.getEnd();
        else
            return -1;
    }
}
