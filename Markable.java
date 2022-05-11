/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.common;

/**
 * Markable interface
 */
public interface Markable {
    /**
     *
     * @return - true, if marked
     */
    boolean isMarked();

    /**
     *
     * @param value - mark flag
     */
    void mark(boolean value);
}
