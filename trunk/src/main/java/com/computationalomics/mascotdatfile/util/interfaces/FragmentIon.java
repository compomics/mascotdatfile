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

package com.computationalomics.mascotdatfile.util.interfaces;

import com.computationalomics.mascotdatfile.util.mascot.Peak;
import com.computationalomics.util.gui.interfaces.SpectrumAnnotation;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 10-mrt-2006
 * Time: 14:49:39
 */

/**
 * This interface is implemented by different fragmentions.
 */
public interface FragmentIon extends SpectrumAnnotation {
    /**
     * This variable is a hard coded identifier for a B-ion.
     */
    public static final int B_ION = 1;
    /**
     * This variable is a hard coded identifier for a B++-ion.
     * This is a double charged b-ion.
     */
    public static final int B_DOUBLE_ION = 2;
    /**
     * This variable is a hard coded identifier for a B-H2O-ion.
     * This is the b-ion with a loss of H2O.
     */
    public static final int B_H2O_ION = 3;
    /**
     * This variable is a hard coded identifier for a double charged B-H2O-ion.
     * This is the double charged b-ion with a loss of H2O.
     */
    public static final int B_H2O_DOUBLE_ION = 4;
    /**
     * This variable is a hard coded identifier for a B-NH3-ion.
     * This is the b-ion with a loss of NH3.
     */
    public static final int B_NH3_ION = 5;
    /**
     * This variable is a hard coded identifier for a double charged B-NH3-ion.
     * This is the double charged b-ion with a loss of NH3.
     */
    public static final int B_NH3_DOUBLE_ION = 6;

    /**
     * This variable is a hard coded identifier for a Y-ion.
     */
    public static final int Y_ION = 7;
    /**
     * This variable is a hard coded identifier for a Y++-ion.
     * This is a double charged y-ion.
     */
    public static final int Y_DOUBLE_ION = 8;
    /**
     * This variable is a hard coded identifier for a Y-H2O-ion.
     * This is the y-ion with a loss of H2O.
     */
    public static final int Y_H2O_ION = 9;
    /**
     * This variable is a hard coded identifier for a double charged Y-H2O-ion.
     * This is the double charged y-ion with a loss of H2O.
     */
    public static final int Y_H2O_DOUBLE_ION = 10;
    /**
     * This variable is a hard coded identifier for a Y-NH3-ion.
     * This is the y-ion with a loss of NH3.
     */
    public static final int Y_NH3_ION = 11;
    /**
     * This variable is a hard coded identifier for a double charged Y-NH3-ion.
     * This is the double charged y-ion with a loss of NH3.
     */
    public static final int Y_NH3_DOUBLE_ION = 12;

    /**
     * This variable is a hard coded identifier for a A-ion.
     * Aion is the b-ion minus CO.
     */
    public static final int A_ION = 13;
    /**
     * This variable is a hard coded identifier for a A++-ion.
     * This is a double charged a-ion.
     */
    public static final int A_DOUBLE_ION = 14;
    /**
     * This variable is a hard coded identifier for a A-H2O-ion.
     * This is the a-ion with a loss of H2O.
     */
    public static final int A_H2O_ION = 15;
    /**
     * This variable is a hard coded identifier for a double charged Y-H2O-ion.
     * This is the double charged a-ion with a loss of H2O.
     */
    public static final int A_H2O_DOUBLE_ION = 16;
    /**
     * This variable is a hard coded identifier for a A-NH3-ion.
     * This is the a-ion with a loss of NH3.
     */
    public static final int A_NH3_ION = 17;
    /**
     * This variable is a hard coded identifier for a double charged Y-NH3-ion.
     * This is the double charged a-ion with a loss of NH3.
     */
    public static final int A_NH3_DOUBLE_ION = 18;

    /**
     * This variable is a hard coded identifier for a X-ion.
     * Xion is the y-ion plus 'CO', minus 'H2'.
     */
    public static final int X_ION = 19;
    /**
     * This variable is a hard coded identifier for a X++-ion.
     * This is a double charged x-ion.
     */
    public static final int X_DOUBLE_ION = 20;

    /**
     * This variable is a hard coded identifier for a C-ion.
     * Cion is the b-ion plus 'NH3'.
     */
    public static final int C_ION = 21;
    /**
     * This variable is a hard coded identifier for a C++-ion.
     * This is a double charged c-ion.
     */
    public static final int C_DOUBLE_ION = 22;

    /**
     * This variable is a hard coded identifier for a Z-ion.
     * Zion is the y-ion minus 'NH3'.
     */
    public static final int Z_ION = 23;
    /**
     * This variable is a hard coded identifier for a Z++-ion.
     * This is a double charged z-ion.
     */
    public static final int Z_DOUBLE_ION = 24;

    /**
     * This variable is a hard coded identifier for a Z-ion.
     * Zion is the y-ion minus 'NH3'.
     */
    public static final int ZH_ION = 25;
    /**
     * This variable is a hard coded identifier for a Z++-ion.
     * This is a double charged z-ion.
     */
    public static final int ZH_DOUBLE_ION = 26;

    /**
     * This variable is a hard coded identifier for a Z-ion.
     * Zion is the y-ion minus 'NH3'.
     */
    public static final int ZHH_ION = 27;
    /**
     * This variable is a hard coded identifier for a Z++-ion.
     * This is a double charged z-ion.
     */
    public static final int ZHH_DOUBLE_ION = 28;


    /**
     * This variable is a hard coded identifier for the Precursor .
     */
    public static final int PRECURSOR = 29;
    /**
     * This variable is a hard coded identifier for the Precursor, with neutral losses.
     */
    public static final int PRECURSOR_LOSS = 30;
    /**
     * This variable is a hard coded identifier for an immoniumIon.
     */
    public static final int IMMONIUM = 31;

    /**
     * This variable is a hard coded identifier for a fragmention that was of no importance for the score calculation of the peptidehit and if its a match,
     * mascot judges it as a random match.
     */
    public static final int NOT_Sign_NOT_Scoring = 0;

    /**
     * This variable is a hard coded identifier for a fragmention that was significant in the ionseries but was of no importance for the score calculation of the peptidehit.
     */
    public static final int Sign_NOT_Scoring = 1;

    /**
     * This variable is a hard coded identifier for a fragmention that was significant in the ionseries AND was used for the score calculation of the peptidehit.
     */
    public static final int Sign_AND_Scoring = 2;

    /**
     * This method returns the M/Z of the feature to annotate.
     *
     * @return double with the M/Z.
     */
    public double getMZ();

    /**
     * Returns this variable represents the fragmentions intensity if it is a match.
     *
     * @return this variable represents the fragmentions intensity if it is a match.
     */
    public double getIntensity();

    /**
     * This method returns the soft type int ID of the fragmention. ex:an X-ion has an integer iID = 4.<br>These id's are static final integers fixed in this FragmentIon interface.
     * <br><b>B-ion : 1</B>
     * <br><b>B++-ion : 2</B>
     * <br><b>B-H2O-ion : 3</B>
     * <br><b>B++-H2O-ion : 4</B>
     * <br><b>B-NH3-ion : 5</B>
     * <br><b>B++-NH3-ion : 6</B>
     * <br><b>Y-ion : 7</B>
     * <br><b>Y++-ion : 8</B>
     * <br><b>Y-H2O-ion : 9</B>
     * <br><b>Y++-H2O-ion : 10</B>
     * <br><b>Y-NH3-ion : 11</B>
     * <br><b>Y++-NH3-ion : 12</B>
     * <br><b>A-ion : 13</B>
     * <br><b>A++-ion : 14</B>
     * <br><b>A-H2O-ion : 15</B>
     * <br><b>A++-H2O-ion : 16</B>
     * <br><b>A-NH3-ion : 17</B>
     * <br><b>A++-NH3-ion : 18</B>
     * <br><b>X-ion : 19</B>
     * <br><b>X++-ion : 20</B>
     * <br><b>C-ion : 21</B>
     * <br><b>C++-ion : 22</B>
     * <br><b>Z-ion : 23</B>
     * <br><b>Z++-ion : 24</B>
     *
     * @return int     ID with the ionnumber.
     */
    public int getID();

    /**
     * This method returns the type of the fragmention. (Ex: a b3 fragmention will have String iType = "b";
     *
     * @return String   Returns a String with the type of fragmention.
     */
    public String getType();

    /**
     * This method returns the color for the annotation.
     *
     * @return Color with the color for the annotation.
     */
    public Color getColor();

    /**
     * This method sets the color for the annotation.
     */
    public void setColor(Color aColor);

    /**
     * Returns the int Number of the fragmention. (Ex: a b3 fragmention will have Number = 3)
     *
     * @return the int Number of the fragmention. (Ex: a b3 fragmention will have Number = 3)
     */
    public int getNumber();

    /**
     * This method returns a boolean if this fragmention is double charged.
     *
     * @return boolean     returns true if this fragmention is double charged.
     */
    public boolean isDoubleCharged();

    /**
     * This method checks if the mass of this fragmention was found in the original spectrum with a given MassError. This method does not take any intensity parameters in count!<br>
     * If the mass was matched, <b>the instance boolean iMatch will be set to true</b>, if there is no match, the boolean will stay false.
     * This boolean will be returned in the end of this method!
     *
     * @param aPeaks     Peak[] containing all the peak masses from the mass spectrum that was used by the query that delivered this peptidehit.
     * @param aMassError This is the mass error to check if this theoretical fragment ion was found in the spectrum.
     * @return boolean      This boolean says if this theoretical FragmentIon wass found with a mass error iMassError in the spectrum.
     */
    public boolean isMatch(Peak[] aPeaks, double aMassError);

    /**
     * This method checks if the mass of this fragmention was found in the original spectrum with a given MassError.<br>
     * The Peak that is matched must have an intensity above a threshold that is based on the highest intensity of the spectrum.<br>
     * If the mass was matched, <b>the instance boolean iMatch will be set to true</b>, if there is no match, the boolean will stay false.
     * This boolean will be returned in the end of this method!
     *
     * @param aPeaks               Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was created.
     * @param aMaxIntensity        double with the max intensity of the spectrum.
     * @param aIntensityPercentage This double is a percent (ex: 0.10) , The relative intensityThreshold will then be (aMaxIntensity*aIntensityPercentage),
     *                             only matches that are above this threshold will be added to the Vector.
     * @param aMassError           This is the mass error to check if this theoretical fragment ion was found in the spectrum.
     * @return boolean      This boolean says if this theoretical FragmentIon wass found with a mass error iMassError in the spectrum.
     */
    public boolean isMatchAboveIntensityThreshold(Peak[] aPeaks, double aMaxIntensity, double aIntensityPercentage, double aMassError);

    /**
     * Returns this double holds the mass error between the theoretical fragmention and the matched peakmass.
     * <b>The fragmention must be a match in the mass spectrum before this method can be used!</b>
     *
     * @return this double holds the mass error between the theoretical fragmention and the matched peakmass.
     */
    public double getTheoreticalExperimantalMassError();

    boolean hasBeenMatched();
}
