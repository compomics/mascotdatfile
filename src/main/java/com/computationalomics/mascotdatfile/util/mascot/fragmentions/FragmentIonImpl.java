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

package com.computationalomics.mascotdatfile.util.mascot.fragmentions;

import com.computationalomics.mascotdatfile.util.interfaces.FragmentIon;
import com.computationalomics.mascotdatfile.util.mascot.Peak;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 10-mrt-2006
 * Time: 15:08:48
 */
public class FragmentIonImpl implements FragmentIon, Cloneable, Serializable {
    /**
     * This double holds the mass of fragmention.
     */
    private double iMZ = 0.0;
    /**
     * This variable represents the fragmentions intensity if it is a match.
     */
    private double iIntensity = 0.0;
    /**
     * The int Number of the fragmention. (Ex: a b3 fragmention will have int iNumber = 3)
     */
    private int iNumber = 0;
    /**
     * This ID represents an identification for this ion.
     * The FragmentIon implementation has these int-values hard coded. <br>The FragmentIonImpl constructor gets a hard coded int. The type of this fragmention will be verified by the getId() method.
     */
    private int iID = 0;
    /**
     * the type of the fragmention. (Ex: a b3 fragmention will have String iType = "b";
     */
    private String iType = null;
    /**
     * This double holds the mass error between the theoretical fragmention and the matched peakmass.
     * This variable can only be used when this fragmention has been matched by one of the method of this class.
     */
    private double iTheoreticalExperimantalMassError = 0.0;
    /**
     * The Color that will be used for this fragmention for annotation on the spectrumpannel.
     */
    private Color iColor = null;
    /**
     * This int value is a measurement of the importance of the fragmentions. It is originally from the ionseries.
     * <ul>
     * <li><b>FragmentIon.NOT_Sign_NOT_Scoring </b> - Not Significant, Not Scoring.
     * <li><b>FragmentIon.Sign_NOT_Scoring </b> - Significant, Not Scoring.
     * <li><b>FragmentIon.Sign_AND_Scoring  </b> - Significant And Scoring.
     * <i>default is FragmentIon.NOT_Sign_NOT_Scoring. </i>
     */
    private int iImportance = FragmentIon.NOT_Sign_NOT_Scoring;
    /**
     * This double is the default error margin at wich this fragmention should be matched in the mass spectrum.
     * <br>this Value is necessairy for the spectrumpannel. When this fragmention must be shown on the spectrumpannel, it MUST have a error margin for itself.
     * <br>the other methods that can be accessed on a fragmention have a parametrical error margin so. These do not interfere, because they work in opposite directions.
     */
    private double iErrorMargin = 0.0;
    /**
     * This boolean stores a previous match state.
     */
    private boolean boolMatch = false;

    /**
     * Simple Constructer setting the instance variables.
     *
     * @param aMZ          This double holds the mass of the fragmention.
     * @param aErrorMargin This double holds the error margin wich will be used in the spectrumpannel.
     * @param aID          A static integer from the FragmentIon interface that serves as an identifier for the type of fragmention.
     * @param aNumber      The int Number of the fragmention. (Ex: a b3 fragmention will have int iNumber = 3)
     * @param aType        The type of the fragmention. (Ex: a b3 fragmention will have String iType = "a"
     */
    public FragmentIonImpl(double aMZ, double aErrorMargin, int aID, int aNumber, String aType) {
        iMZ = aMZ;
        iErrorMargin = aErrorMargin;
        iID = aID;
        iNumber = aNumber;
        iType = aType;
        setDefaultColor();
    }

    /**
     * Simple Constructer setting the instance variables.
     *
     * @param aMZ          This double holds the mass of the fragmention.
     * @param aIntensity   This double holds the intensity of the fragmention.
     * @param aErrorMargin This double holds the error margin wich will be used in the spectrumpannel.
     * @param aID          A static integer from the FragmentIon interface that serves as an identifier for the type of fragmention.
     * @param aNumber      The int Number of the fragmention. (Ex: a b3 fragmention will have int iNumber = 3)
     * @param aType        The type of the fragmention. (Ex: a b3 fragmention will have String iType = "a"
     */
    public FragmentIonImpl(double aMZ, double aIntensity, double aErrorMargin, int aID, int aNumber, String aType) {
        this(aMZ, aErrorMargin, aID, aNumber, aType);
        iIntensity = aIntensity;
    }

    /**
     * Simple Constructer setting the instance variables.
     *
     * @param aMZ          This double holds the mass of the fragmention.
     * @param aIntensity   This double holds the intensity of the fragmention.
     * @param aErrorMargin This double holds the error margin wich will be used in the spectrumpannel.
     * @param aID          A static integer from the FragmentIon interface that serves as an identifier for the type of fragmention.
     * @param aNumber      The int Number of the fragmention. (Ex: a b3 fragmention will have int iNumber = 3)
     * @param aType        The type of the fragmention. (Ex: a b3 fragmention will have String iType = "a"
     * @param aColor       The Color that should be used to annotate this fragmention on the spectrumpannel.
     */
    public FragmentIonImpl(double aMZ, double aIntensity, double aErrorMargin, int aID, int aNumber, String aType, Color aColor) {
        this(aMZ, aErrorMargin, aID, aNumber, aType);
        iIntensity = aIntensity;
        iColor = aColor;
    }

    /**
     * Simple Constructer setting the instance variables.
     *
     * @param aMZ          This double holds the mass of the fragmention.
     * @param aErrorMargin This double holds the error margin wich will be used in the spectrumpannel.
     * @param aID          A static integer from the FragmentIon interface that serves as an identifier for the type of fragmention.
     * @param aNumber      The int Number of the fragmention. (Ex: a b3 fragmention will have int iNumber = 3)
     * @param aType        The type of the fragmention. (Ex: a b3 fragmention will have String iType = "a"
     * @param aColor       The Color that should be used to annotate this fragmention on the spectrumpannel.
     */
    public FragmentIonImpl(double aMZ, double aErrorMargin, int aID, int aNumber, String aType, Color aColor) {
        this(aMZ, aErrorMargin, aID, aNumber, aType);
        iColor = aColor;
    }


    /**
     * Returns this double holds the mass of the fragmention.
     *
     * @return this double holds the mass of the fragmention
     */
    public double getMZ() {
        return iMZ;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Sets this double holds the mass of fragmention.
     *
     * @param aMZ this double holds the mass of fragmention.
     */
    public void setMZ(double aMZ) {
        iMZ = aMZ;
    }

    /**
     * Returns this variable represents the fragmentions intensity if it is a match.
     *
     * @return this variable represents the fragmentions intensity if it is a match.
     */
    public double getIntensity() {
        return iIntensity;
    }

    /**
     * Returns the int Number of the fragmention. (Ex: a b3 fragmention will have Number = 3)
     *
     * @return the int Number of the fragmention. (Ex: a b3 fragmention will have Number = 3)
     */
    public int getNumber() {
        return iNumber;
    }

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
     * <br><b>Precursor : 25</B>
     * <br><b>Immonium : 26</B>
     *
     * @return int     ID with the ionnumber.
     */
    public int getID() {
        return iID;
    }

    /**
     * Returns the type of the fragmention. (Ex: a b3 fragmention will have String iType = "b";
     *
     * @return the type of the fragmention. (Ex: a b3 fragmention will have String iType = "b";
     */
    public String getType() {
        return iType;
    }

    /**
     * Sets the type of the fragmention. (Ex: a b3 fragmention will have String iType = "b";
     *
     * @param aType the type of the fragmention. (Ex: a b3 fragmention will have String iType = "b";
     */
    public void setType(String aType) {
        iType = aType;
    }

    /**
     * Returns the Color that will be used for this fragmention for annotation on the spectrumpannel.
     *
     * @return the Color that will be used for this fragmention for annotation on the spectrumpannel.
     */
    public Color getColor() {
        return iColor;
    }

    /**
     * Sets the Color that will be used for this fragmention for annotation on the spectrumpannel.
     *
     * @param aColor the Color that will be used for this fragmention for annotation on the spectrumpannel.
     */
    public void setColor(Color aColor) {
        iColor = aColor;
    }

    /**
     * Returns this int value is a measurement of the importance of the fragmentions. It is originally from the ionseries.
     * <ul>
     * <li><b>FragmentIon.NOT_Sign_NOT_Scoring </b> - Not Significant, Not Scoring.
     * <li><b>FragmentIon.Sign_NOT_Scoring </b> - Significant, Not Scoring.
     * <li><b>FragmentIon.Sign_AND_Scoring  </b> - Significant And Scoring.
     * <i>default is FragmentIon.NOT_Sign_NOT_Scoring. </i>
     *
     * @return this int value is a measurement of the importance of the fragmentions. It is originally from the ionseries.
     */
    public int getImportance() {
        return iImportance;
    }

    /**
     * Sets this int value is a measurement of the importance of the fragmentions. It is originally from the ionseries.
     * <ul>
     * <li><b>FragmentIon.NOT_Sign_NOT_Scoring </b> - Not Significant, Not Scoring.
     * <li><b>FragmentIon.Sign_NOT_Scoring </b> - Significant, Not Scoring.
     * <li><b>FragmentIon.Sign_AND_Scoring  </b> - Significant And Scoring.
     * <i>default is FragmentIon.NOT_Sign_NOT_Scoring. </i>
     *
     * @param aImportance this int value is a measurement of the importance of the fragmentions. It is originally from the ionseries.
     */
    public void setImportance(int aImportance) {
        iImportance = aImportance;
    }

    /**
     * Returns this double is the default error margin at wich this fragmention should be matched in the mass spectrum.
     * <br>this Value is necessairy for the spectrumpannel. When this fragmention must be shown on the spectrumpannel, it MUST have a error margin for itself.
     * <br>the other methods that can be accessed on a fragmention have a parametrical error margin so. These do not interfere, because they work in opposite directions.
     *
     * @return this double is the default error margin at wich this fragmention should be matched in the mass spectrum.
     */
    public double getErrorMargin() {
        return iErrorMargin;
    }

    /**
     * This method returns the label of the fragmention. It is a combination of the Number and the Type.
     * ex: FragmentIon with iNumber=3 and iType=b will return b3 on this method call.
     *
     * @return String       Label of the framention (Number+Type).
     */
    public String getLabel() {
        StringBuffer label = new StringBuffer();
        // This label formation is no good for immonium ions(iID = 26) & precursor(iID = 25).
        if (iID < FragmentIon.PRECURSOR) {
            // Not Significant, Not Scoring has a '!' in front.
            if (iImportance == FragmentIon.NOT_Sign_NOT_Scoring) {
                label.append('&');
            }
            // Significant, Not Scoring has a '#' in front.
            else if (iImportance == FragmentIon.Sign_NOT_Scoring) {
                label.append('#');
            }
            // Simple labels get simple notation.
            // Complex (multi-character) ones get a '[x]' notation.
            if (getType().length() > 1) {
                label.append(getType().substring(0, 1) + "[" + getNumber() + "]" + getType().substring(1));
            } else {
                label.append(getType() + getNumber());
            }
        } else {
            label.append(iType);
        }
        return label.toString();
    }

    /**
     * Returns this double holds the mass error between the theoretical fragmention and the matched peakmass.
     * <b>The fragmention must be a match in the mass spectrum before this method can be used!</b>
     *
     * @return this double holds the mass error between the theoretical fragmention and the matched peakmass.
     */
    public double getTheoreticalExperimantalMassError() {
        return iTheoreticalExperimantalMassError;
    }

    /**
     * This method checks if the mass of this fragmention was found in the original spectrum with a given ErrorMargin. This method does not take any intensity parameters in count!<br>
     * If the mass was matched, a 'true' will be returned in the end of this method!
     * The mass Theoretical and experimental mass error is saved in the iTheoreticalExperimantalMassError double.
     *
     * @param aPeaks       Peak[] containing all the peak masses from the mass spectrum that was used by the query that delivered this peptidehit.
     * @param aErrorMargin This is the mass error to check if this theoretical fragment ion was found in the spectrum.
     * @return boolean      This boolean says if this theoretical FragmentIon wass found with a mass error aErrorMargin in the spectrum.
     */
    public boolean isMatch(Peak[] aPeaks, double aErrorMargin) {
        if (!boolMatch) {
            for (int i = 0; i < aPeaks.length; i++) {
                if (-aErrorMargin <= (aPeaks[i].getMZ() - iMZ) && (aPeaks[i].getMZ() - iMZ) <= aErrorMargin) {
                    iTheoreticalExperimantalMassError = aPeaks[i].getMZ() - iMZ;
                    boolMatch = true;
                    iIntensity = aPeaks[i].getIntensity();
                    break;
                }
            }
        }
        return boolMatch;
    }

    /**
     * This method checks if the mass of this fragmention was found in the original spectrum with a given ErrorMargin.<br>
     * The Peak that is matched must have an intensity above a threshold that is based on the highest intensity of the spectrum.<br>
     * If the mass was matched, <b>the instance boolean lMatch will be set to true</b>, if there is no match, the boolean will stay false.
     * This boolean will be returned in the end of this method!
     *
     * @param aPeaks               Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was created.
     * @param aMaxIntensity        double with the max intensity of the spectrum.
     * @param aIntensityPercentage This double is a percent (ex: 0.10) , The relative intensityThreshold will then be (aMaxIntensity*aIntensityPercentage),
     *                             only matches that are above this threshold will be added to the Vector.
     * @param aErrorMargin         This is the mass error to check if this theoretical fragment ion was found in the spectrum.
     * @return boolean      This boolean says if this theoretical FragmentIon wass found with a mass error iErrorMargin in the spectrum.
     */
    public boolean isMatchAboveIntensityThreshold(Peak[] aPeaks, double aMaxIntensity, double aIntensityPercentage, double aErrorMargin) {
        if (!boolMatch) { // If the ion has not been matched before,
            double lIntensityThreshold = aMaxIntensity * aIntensityPercentage;
            for (int i = 0; i < aPeaks.length; i++) {
                if (aPeaks[i].getIntensity() > lIntensityThreshold) {
                    if ((-aErrorMargin) < (aPeaks[i].getMZ() - iMZ) && (aPeaks[i].getMZ() - iMZ) < aErrorMargin) {
                        iTheoreticalExperimantalMassError = aPeaks[i].getMZ() - iMZ;
                        boolMatch = true;
                        iIntensity = aPeaks[i].getIntensity();
                        break;
                    }
                }
            }
        }
        return boolMatch;
    }

    /**
     * This method returns a boolean if this fragmention is double charged.
     *
     * @return boolean     returns true if this fragmention is double charged.
     */
    public boolean isDoubleCharged() {
        boolean bl = false;
        if (iType.indexOf('+') != -1) {
            bl = true;
        }
        return bl;
    }

    /**
     * This boolean returns whether or not this fragmention has been successfully matched before.
     *
     * @return Boolean with successfull match status.
     */
    public boolean hasBeenMatched() {
        return boolMatch;
    }

    /**
     * This method sets the default color according to the type of this fragmention.
     */
    public void setDefaultColor() {
        try {
            switch (iID) {
                case FragmentIon.A_ION:
                    //Turquoise
                    iColor = new Color(153, 0, 0);
                    break;

                case FragmentIon.A_DOUBLE_ION:
                    //dwarf green
                    iColor = new Color(0, 139, 0);
                    break;

                case FragmentIon.A_H2O_ION:
                    //Light purple-blue
                    iColor = new Color(171, 161, 255);
                    break;

                case FragmentIon.A_H2O_DOUBLE_ION:
                    //Light purple-blue
                    iColor = new Color(171, 161, 255);
                    break;

                case FragmentIon.A_NH3_ION:
                    //ugly purple pink
                    iColor = new Color(248, 151, 202);
                    break;

                case FragmentIon.A_NH3_DOUBLE_ION:
                    //ugly purple pink
                    iColor = new Color(248, 151, 202);
                    break;

                case FragmentIon.B_ION:
                    //Dark Blue
                    iColor = new Color(0, 0, 255);
                    break;

                case FragmentIon.B_DOUBLE_ION:
                    //Dark Blue
                    iColor = new Color(0, 0, 255);
                    break;

                case FragmentIon.B_H2O_ION:
                    //nice Blue
                    iColor = new Color(0, 125, 200);
                    break;

                case FragmentIon.B_H2O_DOUBLE_ION:
                    //nice Blue
                    iColor = new Color(0, 125, 200);
                    break;

                case FragmentIon.B_NH3_ION:
                    //Dwarf green
                    iColor = new Color(153, 0, 255);
                    break;

                case FragmentIon.B_NH3_DOUBLE_ION:
                    //another purple
                    iColor = new Color(153, 0, 255);
                    break;

                case FragmentIon.C_ION:
                    //another purple
                    iColor = new Color(188, 0, 255);
                    break;

                case FragmentIon.C_DOUBLE_ION:
                    //Purple blue
                    iColor = new Color(188, 0, 255);
                    break;

                case FragmentIon.X_ION:
                    //green
                    iColor = new Color(78, 200, 0);
                    break;

                case FragmentIon.X_DOUBLE_ION:
                    //green
                    iColor = new Color(78, 200, 0);
                    break;

                case FragmentIon.Y_ION:
                    //Black
                    iColor = new Color(0, 0, 0);
                    break;

                case FragmentIon.Y_DOUBLE_ION:
                    //Black
                    iColor = new Color(0, 0, 0);
                    break;

                case FragmentIon.Y_H2O_ION:
                    //Navy blue
                    iColor = new Color(0, 70, 135);
                    break;

                case FragmentIon.Y_H2O_DOUBLE_ION:
                    //Navy blue
                    iColor = new Color(0, 70, 135);
                    break;

                case FragmentIon.Y_NH3_ION:
                    //another purple
                    iColor = new Color(155, 0, 155);
                    break;

                case FragmentIon.Y_NH3_DOUBLE_ION:
                    //another purple
                    iColor = new Color(155, 0, 155);
                    break;

                case FragmentIon.Z_ION:
                    //Red
                    iColor = new Color(255, 140, 0);
                    break;

                case FragmentIon.Z_DOUBLE_ION:
                    //dark green
                    iColor = new Color(64, 179, 0);
                    break;

                case FragmentIon.ZH_ION:
                    //Red
                    iColor = new Color(255, 140, 0);
                    break;

                case FragmentIon.ZH_DOUBLE_ION:
                    //dark green
                    iColor = new Color(64, 179, 0);
                    break;
                case FragmentIon.ZHH_ION:
                    //Red
                    iColor = new Color(255, 140, 0);
                    break;

                case FragmentIon.ZHH_DOUBLE_ION:
                    //dark green
                    iColor = new Color(64, 179, 0);
                    break;

                case FragmentIon.PRECURSOR:
                    //red
                    iColor = Color.red;
                    break;

                case FragmentIon.PRECURSOR_LOSS:
                    //red
                    iColor = Color.red;
                    break;

                case FragmentIon.IMMONIUM:
                    //red
                    iColor = Color.gray;
                    break;

                default:
                    throw new IllegalAccessException("No correct Color could be chosen in the creation of the FragmentIonImpl instance.\nThis means that there was no correct assignement of the fragmention type!");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method should return a clone of this FragmentionImpl.
     *
     * @return Object     Clone of this FragmetionImpl.
     */
    public Object clone() throws CloneNotSupportedException {
        FragmentIonImpl fi = null;
        try {
            fi = (FragmentIonImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();  // print ${Exception} information.
        }
        return fi;
    }


    /**
     * Reset the mathcing values.
     */
    public void resetMatchingValues() {
        boolMatch = false;
        iTheoreticalExperimantalMassError = 0.0;
        iIntensity = 0.0;
    }
}
