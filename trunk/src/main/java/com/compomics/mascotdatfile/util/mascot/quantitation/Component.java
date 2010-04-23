package com.compomics.mascotdatfile.util.mascot.quantitation;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Mar 16, 2009 Time: 5:33:18 PM To change this template use File | Settings
 * | File Templates.
 */
public class Component {
    // Class specific log4j logger for Component instances.
    private static Logger logger = Logger.getLogger(Component.class);
    /**
     * The name for the component.
     */
    private String iName = "";

    private double iAverage = 0.0;

    private double iMonoisotopic = 0.0;

    public Component(final String aName, final double aAverage, final double aMonoisotopic) {
        iName = aName;
        iAverage = aAverage;
        iMonoisotopic = aMonoisotopic;
    }

    public String getName() {
        return iName;
    }

    public double getAverage() {
        return iAverage;
    }

    public double getMonoisotopic() {
        return iMonoisotopic;
    }

    @Override
    public String toString() {
        return getName();
    }
}
