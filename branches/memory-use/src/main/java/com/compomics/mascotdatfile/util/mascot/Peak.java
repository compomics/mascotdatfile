/*
 * Copyright (C) 2006 - Helsens Kenny and Martens Lennart
 * 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"),
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied.
 * 
 * See the License for the specific language governing permissions 
 * and limitations under the License.
 * 
 * 
 * 
 * Contact: 
 * kenny.helsens@ugent.be 
 * lennart.martens@ebi.ac.uk
 */

package com.compomics.mascotdatfile.util.mascot;

import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * This inner class holds the data for just one peak in the spectrum.
 */
public class Peak implements Serializable {
    // Class specific log4j logger for Peak instances.
    private static Logger logger = Logger.getLogger(Peak.class);
    /**
     * The mass of the peak.
     */
    private double iMass = 0;
    /**
     * The intensity of the peak.
     */
    private double iIntensity = 0;

    /**
     * Constructor that makes a peak.
     *
     * @param aMass      The mass of the peak.
     * @param aIntensity The intensity of the peak.
     */
    public Peak(double aMass, double aIntensity) {
        iMass = aMass;
        iIntensity = aIntensity;
    }

    /**
     * This method returns a String that will be printed when a Peak instance is called into a print();
     *
     * @return String with peak data
     */
    public String toString() {
        return "mass||" + iMass + "\t\tintensity||" + iIntensity;

    }

    /**
     * Returns the mass of the peak.
     *
     * @return the mass of the peak.
     */
    public double getMZ() {
        return iMass;
    }

    /**
     * Sets the mass of the peak.
     *
     * @param aMass the mass of the peak.
     */
    public void setMass(double aMass) {
        iMass = aMass;
    }

    /**
     * Returns the intensity of the peak.
     *
     * @return the intensity of the peak.
     */
    public double getIntensity() {
        return iIntensity;
    }

    /**
     * Sets the intensity of the peak.
     *
     * @param aIntensity the intensity of the peak.
     */
    public void setIntensity(double aIntensity) {
        iIntensity = aIntensity;
    }
}
