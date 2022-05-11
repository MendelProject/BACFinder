/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.common;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NaryPath<T extends NaryNode>  implements Jsonable, Markable, Rangeable{
    /**
     * Comparator class
     */
    static class PathComparator implements Comparator<NaryPath> {
        public int compare(NaryPath path1, NaryPath path2) {
            int range1 = path1.getEnd() - path1.getStart();
            int range2 = path2.getEnd() - path2.getStart();
            int numNodes1 = path1.getNodes().size();
            int numNodes2 = path2.getNodes().size();
            if (numNodes1 == numNodes2)
                return range1 - range2;
            else return numNodes1 - numNodes2;
        }
    }

    /**
     * default constructor
     */
    List<T> nodes;
    NaryPath() {
        nodes = new ArrayList<>();
    }

    /**
     *
     * @param other - other path
     */
    NaryPath(NaryPath other){
        this.nodes = new ArrayList<>();
        this.nodes.addAll(other.nodes);
    }

    /**
     *
     * @param other - ither path
     * @return 1, 0, -1
     */
    @Override
    public boolean equals(Object other){
        if (this.getClass().equals(other.getClass())){
            NaryPath otherPath = (NaryPath) other;
            int range1 = this.getEnd() - this.getStart();
            int range2 = otherPath.getEnd() - otherPath.getStart();
            int numNodes1 = this.getNodes().size();
            int numNodes2 = otherPath.getNodes().size();
            return range1 == range2 && numNodes1 == numNodes2;
        }
        return false;
    }

    /**
     * removes last node
     */
    void removeLast(){
        nodes.remove(nodes.size() -1);
    }

    /**
     *
     * @return - list of nodes
     */
    public List<T> getNodes(){return nodes;}
    @Override
    public String toString() {
        String str = StringUtils.EMPTY;
        for(T node:nodes){
            str += node;
        }
        return str;
    }

    /**
     *
     * @return - json string
     */
    public String toJson(){
        String json =  "[";
        for (T node: nodes){
            String value = node.toJson();
            if (StringUtils.isNotEmpty(value)) {
                json += value;
                json += ",";
            }
        }
        if (StringUtils.isNotEmpty(json)){
            json = json.substring(0,json.lastIndexOf(","));
        }
        json += "]";
        return json;
    }

    /**
     *
     * @return - true, if marked
     */
    public boolean isMarked(){
        for(T node:nodes){
            if (node.isMarked())
                return true;
        }
        return false;
    }

    /**
     * Compute total lengths of a path
     * @return total length of a path
     */
    public int getTotalLength(){
        return getEnd() - getStart();
    }

    /**
     *
     * @return length of the longest BAC in the path
     */
    public int getBACMaxLen(){
        int len = 0;
        for (NaryNode node : nodes){
            if (len < node.getEnd() - node.getStart())
                len = node.getEnd() - node.getStart();
        }
        return len;
    }

    /**
     *
     * @return start coordinate of the first node
     */
    public int getStart(){
        return nodes.get(1).getStart();
    }

    /**
     *
     * @return - end coordinate of the last node
     */
    public int getEnd(){
        return nodes.get(nodes.size() - 1).getEnd();
    }

    /**
     * noop
     * @param b boolean flag
     */
    public void mark(boolean b){}
}
