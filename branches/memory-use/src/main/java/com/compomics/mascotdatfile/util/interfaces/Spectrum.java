package com.compomics.mascotdatfile.util.interfaces;

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.mascot.Peak;
/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 27-feb-2007
 * Time: 10:47:47
 */

/**
 * Interface description:
 * ------------------
 * This Interface was developed to be implemented by Objects that represent a spectrum.
 */
public interface Spectrum {

    /**
     * This method returns the Peaklist of the implementing object.
     *
     * @return Peak[] peaklist of the spectrum.
     */
    public Peak[] getPeakList();

    /**
     * This method returns a filename for the implementing object.
     *
     * @return String filename of the Spectrum.
     */
    public String getFilename();

    /**
     * Returns the precursor charge.
     *
     * @return the precursor charge.
     */
    public String getChargeString();

    /**
     * Returns the the lowest mass in the spectrum.
     *
     * @return the the lowest mass in the spectrum.
     */
    public double getMinMZ();

    /**
     * Returns the highest mass in the spectrum.
     *
     * @return the highest mass in the spectrum.
     */
    public double getMaxMZ();

    /**
     * Returns the lowest intensity in the spectrum.
     *
     * @return the lowest intensity in the spectrum.
     */
    public double getMinIntensity();

    /**
     * Returns the highist intensity in the spectrum.
     *
     * @return the highist intensity in the spectrum.
     */
    public double getMaxIntensity();

    /**
     * Getter for property 'precursorMZ'.
     *
     * @return Value for property 'precursorMZ'.
     */
    public double getPrecursorMZ();


}
