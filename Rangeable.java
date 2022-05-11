/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.common;

/**
 * Rangeable interface
 */
public interface Rangeable {
    /**
     *
     * @return - start coordinate
     */
    int getStart();

    /**
     *
     * @return - end coordinate
     */
    int getEnd();
}
