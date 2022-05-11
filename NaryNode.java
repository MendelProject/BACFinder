/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.common;

import java.util.List;

/**
 * Nary node interface
 */
public interface NaryNode extends Jsonable, Markable, Rangeable{
    /**
     *
     * @return - node's children arrey
     */
    List<NaryNode> getChildren();
}
