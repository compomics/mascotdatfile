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

import com.compomics.mascotdatfile.util.exception.MascotDatfileException;
import com.compomics.mascotdatfile.util.interfaces.FragmentIon;
import com.compomics.mascotdatfile.util.interfaces.Modification;
import com.compomics.mascotdatfile.util.mascot.fragmentions.FragmentIonImpl;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 10-mrt-2006
 * Time: 10:40:03
 */

/**
 * This Class calculates the fragmentIon masses. The masses are read directly from the datfile from Mascot.
 */
public class PeptideHitAnnotation implements Serializable {
    // Class specific log4j logger for PeptideHitAnnotation instances.
    private static Logger logger = Logger.getLogger(PeptideHitAnnotation.class);
// ------------------------------ FIELDS ------------------------------

    /**
     * This Vector holds the significant theoretical fragmentions that are returned by the
     * getSignificantTheoreticalFragmentions() method.<br>It is declared as an instance variable to do a lazy cache.
     */
    Vector iSignificantTheoreticalFragmentions = null;

    /**
     * This Vector holds the significant theoretical fragmentions that are returned by the
     * getNonSignificantTheoreticalFragementions() method.<br>It is declared as an instance variable to do a lazy
     * cache.
     */
    Vector iNonSignificantTheoreticalFragmentions = null;

    /**
     * This variable will hold the sequence of the PeptideHit.
     */
    private String iSequence = null;

    /**
     * This variable is the smalles index of a S|T|D|E residue in the sequence. These residues can loose H2O. It is used
     * as a lazy cache for the B_H2O_ions calculation.
     */
    private int iH2OStartB = -1;

    /**
     * This variable is the smalles index of a S|T|D|E residue in the sequence. These residues can loose H2O. It is used
     * as a lazy cache for the B_NH3_ions calculation.
     */
    private int iH2OStartY = -1;

    /**
     * This variable is the smalles index of a K|R|Q|N residue in the sequence. These residues can loose NH3. It is used
     * as a lazy cache for the Y_H2O_ions calculation.
     */
    private int iNH3StartB = -1;

    /**
     * This variable is the smalles index of a K|R|Q|N residue in the sequence. These residues can loose NH3. It is used
     * as a lazy cache for the Y_NH3_ions calcuation.
     */
    private int iNH3StartY = -1;

    /**
     * This is an int[] wherein the significant fragmention types are encoded.
     */
    private int[] iIonSeriesFound = null;

    /**
     * This is the default value for the intensity threshold for the getMatchedIonsAboveIntensityThreshold
     * method.<br>Default value equals 10%.
     */
    private double iIntensityPercentage = 0.1;

    /**
     * The array holds Fixed And Variable Modifications of iSequence. If the array value is null; there is no
     * modification at that residue. The array is build as follows: N-term[0] * aminoacids[1]-[n-1] * C-term[n].
     */
    private Modification[] iMods = null;

    /**
     * This FragmentIonImpl[] contains the y-ions.
     */
    private FragmentIonImpl[] iYions = null;

    /**
     * This FragmentIonImpl[] contains the b-ions.
     */
    private FragmentIonImpl[] iBions = null;

    /**
     * Double variable holding the tolerated mass difference when the theoretical ions are mathced next to a
     * massspectrum. <br>This variable comes from a parameters instance. It will be used further on for the
     * ionmatching.
     */
    private double iFragmentMassErrorMargin = 0.0;

    /**
     * Double variable holding the tolerated mass difference when the theoretical ions are mathced next to a
     * massspectrum. <br>This variable comes from a parameters instance. It will be used further on for the
     * ionmatching.
     */
    private double iPeptideMassErrorMargin = 0.0;
    /**
     * This double holds the mass of the precursor.
     */
    private double iPrecursorMZ = 0.0;
    /**
     * This variable iPrecursorCharge holds the charge of the precursor ion.
     */
    private String iPrecursorCharge = null;

    /**
     * This Peak[] is used for caching of the SequenceCoverage Methods.
     */
    private Peak[] iPeaks = null;

    /**
     * This int[] contains coverage info on the Mascot Matched ions of this PeptideHitAnnotation against Peak[] iPeaks
     */
    private int[] iMascotCoverage;

    /**
     * This int[] contains coverage info on the Fused Matched ions of this PeptideHitAnnotation against Peak[] iPeaks
     */
    private int[] iFusedCoverage;
    private int[] iIonSeriesRules;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * This constructor creates an instance of the PeptideHitAnnotation class. When the constructor is called, it will
     * generate an array with theoretical b- and y- ions. Other methods that calculate a- and c- ions, x- and z-ions are
     * included.
     *
     * @param aSequence       PeptideHit's sequence.
     * @param aMods           Modifications on the sequence, each Modification includes different variables such as the
     *                        mass.
     * @param aM              A masses object that holds all the masses values of the AA, Hydrogen, H2O, ..
     * @param aP              A parameter object that holds all the parameter data of the datfile. We need the tolerance
     *                        data in this object.
     * @param aIonSeriesFound A int[] with the wherein the significant fragmention types are encoded.
     */
    public PeptideHitAnnotation(String aSequence, Modification[] aMods, Masses aM, Parameters aP, int[] aIonSeriesFound) {
        iSequence = aSequence;
        iYions = new FragmentIonImpl[iSequence.length() - 1];
        iBions = new FragmentIonImpl[iSequence.length() - 1];
        iMods = aMods;
        iIonSeriesFound = aIonSeriesFound;
        iPeptideMassErrorMargin = Double.parseDouble(aP.getTOL());
        iFragmentMassErrorMargin = Double.parseDouble(aP.getITOL());
        parseFragmentationRules(aP);
        calculateBYions(aM);
    }

    private void parseFragmentationRules(final Parameters aP) {
        // These are the rules for annotating fragmentation spectra on the web page as well as the
        // ion table. We will convert these integers in order to match them to ionseries indices.
        ArrayList<Integer> lIonSeriesRules = new ArrayList<Integer>();
        int[] lRules = aP.getRules();
        for (int i = 0; i < lRules.length; i++) {
            int lRule = lRules[i];
            switch (lRule) {
                case 5: // a-ion
                    lIonSeriesRules.add(0);
                    lIonSeriesRules.add(2);
                    break;

                case 8: // b-ion
                    lIonSeriesRules.add(3);
                    lIonSeriesRules.add(5);
                    break;

                case 11: // c-ion
                    lIonSeriesRules.add(9);
                    lIonSeriesRules.add(10);
                    break;

                case 12: // x-ion
                    lIonSeriesRules.add(11);
                    lIonSeriesRules.add(12);
                    break;

                case 13: // y-ion
                    lIonSeriesRules.add(6);
                    lIonSeriesRules.add(8);
                    break;

                case 16: // z-ion
                    lIonSeriesRules.add(13);
                    lIonSeriesRules.add(14);
                    break;

                case 21: // zH-ion
                    lIonSeriesRules.add(15);
                    lIonSeriesRules.add(16);
                    break;

                case 25: // zHH-ion
                    lIonSeriesRules.add(17);
                    lIonSeriesRules.add(18);
                    break;

                default:
                    // Do nothing with the other rules!
                    break;
            }
        }

        iIonSeriesRules = new int[lIonSeriesRules.size()];
        for (int i = 0; i < lIonSeriesRules.size(); i++) {
            iIonSeriesRules[i] = lIonSeriesRules.get(i);
        }
    }

    /**
     * This method calculates the theoretical instance b-ions and y-ions of the PeptideHit. iBions[0] is the mass of b1
     * and iYions[0] is the mass of y1
     */
    private void calculateBYions(Masses aM) {
        double[] lPeptideUnitMasses = calculatePeptideUnitMasses(aM);
        double lHydrogen = aM.getMass("Hydrogen");
        double lCtermMass = lHydrogen + aM.getMass("Oxygen");

        int length = iSequence.length();

        // b and y ions are done here.
        for (int i = 1; i < length; i++) {
            double bMass = 0.0;
            double yMass = 0.0;

            // Count all the PeptideUnits of the fragmentIon. Finally add 1 extra hydrogen on the N-terminal AA if there is no fixed N-terminal modification. A B-ion has no new components at its C-terminal end.
            for (int j = 0; j < i; j++) {
                bMass += lPeptideUnitMasses[j];
            }
            bMass = bMass + lHydrogen;

            iBions[i - 1] = new FragmentIonImpl(bMass, iFragmentMassErrorMargin, FragmentIon.B_ION, i, "b");

            // Count all the PeptideUnits of the fragmentIon. Finally add  1 extra hydroxyl on the C-terminal AA. A Y-ion also has 2 extra Hydrogens on its N-terminal end (NH in peptide bond, NH3+ when its free).
            for (int j = 0; j < i; j++) {
                yMass += lPeptideUnitMasses[(length - 1) - j];
            }
            yMass = yMass + lCtermMass + (2 * lHydrogen);
            iYions[i - 1] = new FragmentIonImpl(yMass, iFragmentMassErrorMargin, FragmentIon.Y_ION, i, "y");
        }
    }

    /**
     * This method will calculate all the ion series masses for the peptide.
     *
     * @return double[]     This double[] contains the mass of each unit of the sequence. If the first element of the
     *         PeptideHit is A, the first element of the array will be the mass of A. If the (ex) thirth AA of of the
     *         PeptideHit is Acetylated K, the thirth element in the array will be the mass of (K + Acetylation).     *
     */
    private double[] calculatePeptideUnitMasses(Masses aM) {
        int length = iSequence.length();
        double[] lPeptideUnitMasses = new double[length];
        for (int i = 0; i < length; i++) {
            // This double will hold the mass of the peptideUnit.
            double lUnitMass = 0.0;

            // Check if there is a N-Terminal modification, if there is one, count the mass of the N-terminal modification to the mass of the first aminoacid.
            if (i == 0) {
                if (iMods[0] != null) {
                    lUnitMass += iMods[0].getMass();
                }
            }
            // Check if there is a C-Terminal modification, if there is one, count the mass of the C-terminal modification to the mass of the last aminoacid.
            if (i == length - 1) {
                if (iMods[i + 2] != null) {
                    lUnitMass += iMods[i + 2].getMass();
                }
            }
            // For aa i, count its mass to the UnitMass.
            lUnitMass = lUnitMass + aM.getMass(iSequence.charAt(i));
            // If there is a modification on aa i, count the mass at the UnitMass.
            // if the modification is fixed , don't add the mass because Mascot allready included an equivalent modified amino acid mass.
            if (iMods[i + 1] != null && !iMods[i + 1].isFixed()) {
                lUnitMass += iMods[i + 1].getMass();
            }
            lPeptideUnitMasses[i] = lUnitMass;
        }
        return lPeptideUnitMasses;
    }

    /**
     * This constructor creates an instance of the PeptideHitAnnotation class. When the constructor is called, it will
     * generate an array with theoretical b- and y- ions. Other methods that calculate a- and c- ions, x- and z-ions are
     * included.
     *
     * @param aSequence        PeptideHit's sequence.
     * @param aMods            Modifications on the sequence, each Modification includes different variables such as the
     *                         mass.
     * @param aM               A masses object that holds all the masses values of the AA, Hydrogen, H2O, ..
     * @param aP               A parameter object that holds all the parameter data of the datfile. We need the
     *                         tolerance data in this object.
     * @param aIonSeriesFound  A int[] with the wherein the significant fragmention types are encoded.
     * @param aPrecursorMZ     A double representing the precursorMZ of the Query wherefrom this peptidehit was
     *                         created.
     * @param aPrecursorCharge A int representing the charge of the precursor ion.
     */
    public PeptideHitAnnotation(String aSequence, Modification[] aMods, Masses aM, Parameters aP, int[] aIonSeriesFound, double aPrecursorMZ, String aPrecursorCharge) {
        this(aSequence, aMods, aM, aP, aIonSeriesFound);
        iPrecursorMZ = aPrecursorMZ;
        iPrecursorCharge = aPrecursorCharge;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Returns this FragmentIonImpl[] contains the b-ions.
     *
     * @return this FragmentIonImpl[] contains the b-ions.
     */
    public FragmentIonImpl[] getBions() {
        return iBions;
    }

    /**
     * This method calculates the start position of a possible H2O loss for the B-ion series. The returning variable is
     * used for the H2O-loss fragmentions calculation.
     *
     * @return int     This variable holds the smallest index of S|T|E|D residue. These aminoacids can loose their H2O.
     */
    private int getH2OStartB() {
        if (iH2OStartB == -1) {
            int lStart = iSequence.length() - 1;
            if (iSequence.indexOf('S') != -1 && iSequence.indexOf('S') < lStart) {
                lStart = iSequence.indexOf('S');
            }
            if (iSequence.indexOf('T') != -1 && iSequence.indexOf('T') < lStart) {
                lStart = iSequence.indexOf('T');
            }
            if (iSequence.indexOf('D') != -1 && iSequence.indexOf('D') < lStart) {
                lStart = iSequence.indexOf('D');
            }
            if (iSequence.indexOf('E') != -1 && iSequence.indexOf('E') < lStart) {
                lStart = iSequence.indexOf('E');
            }
            iH2OStartB = lStart;
        }
        return iH2OStartB;
    }

    /**
     * This method calculates the start position of a possible H2O loss for the Y-ion series. The returning variable is
     * used for the H2O-loss fragmentions calculation.
     *
     * @return int     This variable holds the smallest index of R|K|N|Q residue. These aminoacids can loose their H2O.
     */
    private int getH2OStartY() {
        if (iH2OStartY == -1) {
            int lStart = 0;
            if (iSequence.lastIndexOf('S') > lStart) {
                lStart = iSequence.indexOf('S');
            }
            if (iSequence.lastIndexOf('T') > lStart) {
                lStart = iSequence.indexOf('T');
            }
            if (iSequence.lastIndexOf('D') > lStart) {
                lStart = iSequence.indexOf('D');
            }
            if (iSequence.lastIndexOf('E') > lStart) {
                lStart = iSequence.indexOf('E');
            }
            iH2OStartY = lStart;
        }
        return iH2OStartY;
    }

    /**
     * This method filters the Peaks that have allready been matched out of the peaklist.
     * This is necessairy when the Fused matches want to go over the Peaklist that where allready
     */

    /**
     * Returns this is the default value for the intensity threshold for the getMatchedIonsAboveIntensityThreshold
     * method.<br>Default value equals 10%.
     *
     * @return this is the default value for the intensity threshold for the getMatchedIonsAboveIntensityThreshold
     *         method.<br>Default value equals 10%.
     */
    public double getIntensityPercentage() {
        return iIntensityPercentage;
    }

    /**
     * Sets this is the default value for the intensity threshold for the getMatchedIonsAboveIntensityThreshold
     * method.<br>Default value equals 10%.
     *
     * @param aIntensityPercentage this is the default value for the intensity threshold for the
     *                             getMatchedIonsAboveIntensityThreshold method.<br>Default value equals 10%.
     */
    public void setIntensityPercentage(double aIntensityPercentage) {
        iIntensityPercentage = aIntensityPercentage;
    }

    /**
     * This method calculates the start position of a possible NH3 loss for the B-ion series. The returning variable is
     * used for the NH3-loss fragmentions calculation.
     *
     * @return int     This variable holds the smallest index of R|K|N|Q residue. These aminoacids can loose their NH3.
     */
    private int getNH3StartB() {
        if (iNH3StartB == -1) {
            int lStart = iSequence.length() - 1;
            if (iSequence.indexOf('R') != -1 && iSequence.indexOf('R') < lStart) {
                lStart = iSequence.indexOf('R');
            }
            if (iSequence.indexOf('K') != -1 && iSequence.indexOf('K') < lStart) {
                lStart = iSequence.indexOf('K');
            }
            if (iSequence.indexOf('N') != -1 && iSequence.indexOf('N') < lStart) {
                lStart = iSequence.indexOf('N');
            }
            if (iSequence.indexOf('Q') != -1 && iSequence.indexOf('Q') < lStart) {
                lStart = iSequence.indexOf('Q');
            }
            iNH3StartB = lStart;
        }
        return iNH3StartB;
    }

    /**
     * This method calculates the start position of a possible NH3 loss for the Y-ion series. The returning variable is
     * used for the NH3-loss fragmentions calculation.
     *
     * @return int     This variable holds the smallest index of R|K|N|Q residue. These aminoacids can loose their NH3.
     */
    private int getNH3StartY() {
        if (iNH3StartY == -1) {
            int lStart = 0;
            if (iSequence.lastIndexOf('R') > lStart) {
                lStart = iSequence.lastIndexOf('R');
            }
            if (iSequence.lastIndexOf('K') > lStart) {
                lStart = iSequence.lastIndexOf('K');
            }
            if (iSequence.lastIndexOf('N') > lStart) {
                lStart = iSequence.lastIndexOf('N');
            }
            if (iSequence.lastIndexOf('Q') > lStart) {
                lStart = iSequence.lastIndexOf('Q');
            }
            iNH3StartY = lStart;
        }
        return iNH3StartY;
    }

    /**
     * Returns this FragmentIonImpl[] contains the y-ions.
     *
     * @return this FragmentIonImpl[] contains the y-ions.
     */
    public FragmentIonImpl[] getYions() {
        return iYions;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * @param aPeaks               Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was
     *                             created.
     * @param aNumberOfPeaksUsed   This variable holds the number of ions that mascot used to check witch of the
     *                             theoretical ions matched in the spectrum. In practice, anly the first
     *                             <aNumberOfPeaksUsed> Peaks from <aPeaks> will be used to do the check. This parameter
     *                             is returned by the getPeaksUsedFromIons1() method in PeptideHit.
     * @param aMaxIntensity        double with the max intensity of the spectrum.
     * @param aIntensityPercentage This double is a percent (ex: 0.10) , The relative intensityThreshold will then be
     *                             (aMaxIntensity*aIntensityPercentage), only matches that are above this threshold will
     *                             be added to the Vector.* @param aMassError           This is the mass error to check
     *                             if this theoretical fragment ion was found in the spectrum.
     * @return int[] with the distinct number fragmentions that covered the sequence. <br /> <b>[0]</b> number of b-ions
     *         covering the peptide's sequence.<br /> <b>[1]</b> number of y-ions covering the peptide's sequence.<br />
     *         <b>[2]</b> number of b- and -ions covering the peptide's sequence.
     */
    public int[] getFusedIonCoverage(Peak[] aPeaks, int aNumberOfPeaksUsed, double aMaxIntensity, double aIntensityPercentage) {
        // Cache
        boolean work = false;
        if ((iPeaks == null) || (iFusedCoverage == null)) {
            work = true;
        } else if (iPeaks != aPeaks) {
            work = true;
        }

        if (work) {
            iPeaks = aPeaks;
            Vector lFragmentIons = getFusedMatchedIons(iPeaks, aNumberOfPeaksUsed, aMaxIntensity, aIntensityPercentage);
            iFusedCoverage = calculateCoverage(lFragmentIons);
        }

        return iFusedCoverage;
    }

    /**
     * This method returns a Vector with FragmentIon instances.<br> All the FragmentIons are matched by 2 submethods.
     * <ul><li>MatchedIonsByMascot<br>First, check the Peaks that mascot used to check the matching fragmentions.
     * <li>MatchedIonsAboveIntensityThreshold<br>Second, check all the Peaks in the spectrum for matches. The matching
     * Peak must have a intensity above a parametrical intensityThreshold.
     *
     * @param aPeaks               Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was
     *                             created.
     * @param aNumberOfIonsUsed    This variable holds the number of ions that mascot used to check witch of the
     *                             theoretical ions matched in the spectrum.<br></br> In practice, only the first
     *                             <aNumberOfIonsUsed> Peaks from <aPeaks> will be used to do the check. This parameter
     *                             is returned by the getPeaksUsedFromIons1() method in PeptideHit.
     * @param aMaxIntensity        double with the max intensity of the spectrum.
     * @param aIntensityPercentage This double is a percent (ex: 0.10) , The relative intensityThreshold will then be
     *                             (aMaxIntensity*aIntensityPercentage), only matches that are above this threshold will
     *                             be added to the Vector.* @param aMassError           This is the mass error to check
     *                             if this theoretical fragment ion was found in the spectrum.
     * @return Vector with FragmentIons that were matched by Mascot used Peaks and by my aboveIntensityThreshold
     *         method.
     */
    public Vector getFusedMatchedIons(Peak[] aPeaks, int aNumberOfIonsUsed, double aMaxIntensity, double aIntensityPercentage) {
        resetPreviousMatching();

        // 1. We will put all the matches in this lFusesMatches.
        Vector lFusedMatches = new Vector();

        // 2. First check all the peaks used by mascot, the most intense of different parts of the spectrum.
        lFusedMatches.addAll(getMatchedIonsByMascot(aPeaks, aNumberOfIonsUsed));

        // 3. The check all the significant ions to a reduced peak list.
        //Peak[] lReducedPeaks = reducePeaks(aPeaks, lFusedMatches);
        lFusedMatches.addAll(getSignificantMatchedIonsAboveIntensityThreshold(aPeaks, aMaxIntensity, aIntensityPercentage));

        // 4. Give the Peak[] to the getGeneralMatchedIonsAboveIntensityThreshold() method to get extra matches and add them into the fused vector.
        lFusedMatches.addAll(getNonSignificantMatchedIonsAboveIntensityThreshold(aPeaks, aMaxIntensity, aIntensityPercentage));

        // 5. Check if any imoniumIons or the precursor matches in the spectrum.
        lFusedMatches.addAll(getMatchedIons(getPrecursorAndImmoniumIons(), aPeaks));

        // 6. Return the lFusedMatches Vector!
        return lFusedMatches;
    }

    /**
     * This method resets FragmentIon matching information such as the error or the matching status.
     */
    private void resetPreviousMatching() {
        // First reset all the significant ions,
        if (iSignificantTheoreticalFragmentions != null) {
            for (int i = 0; i < iSignificantTheoreticalFragmentions.size(); i++) {
                ((FragmentIonImpl) iSignificantTheoreticalFragmentions.elementAt(i)).resetMatchingValues();
            }
        }

        // Then reset all the non-significant ions.
        if (iNonSignificantTheoreticalFragmentions != null) {
            for (int i = 0; i < iNonSignificantTheoreticalFragmentions.size(); i++) {
                ((FragmentIonImpl) iNonSignificantTheoreticalFragmentions.elementAt(i)).resetMatchingValues();
            }
        }
    }

    /**
     * This method will return a Vector with FragmentIon Instances that were matched int the massspectrum by Mascot.
     *
     * @param aPeaks             Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was
     *                           created.
     * @param aNumberOfPeaksUsed This variable holds the number of ions that mascot used to check witch of the
     *                           theoretical ions matched in the spectrum. In practice, anly the first
     *                           <aNumberOfPeaksUsed> Peaks from <aPeaks> will be used to do the check. This parameter
     *                           is returned by the getPeaksUsedFromIons1() method in PeptideHit.
     * @return Vector  Returns a vector with FragmentIon instances that had a match in the Peak[].
     */
    public Vector getMatchedIonsByMascot(Peak[] aPeaks, int aNumberOfPeaksUsed) {
        resetPreviousMatching();
        Peak[] lPeaksUsedByMascot = null;
        // 190306
        // a bug resulted because aNumberOfPeaksUsed > aPeaks.length
        // This resulted in an arrayOutOfBoundsException, an if clause is catching the bug now.
        if (aPeaks.length > aNumberOfPeaksUsed) {
            lPeaksUsedByMascot = new Peak[aNumberOfPeaksUsed];
            for (int i = 0; i < lPeaksUsedByMascot.length; i++) {
                lPeaksUsedByMascot[i] = aPeaks[i];
            }
        } else {
            lPeaksUsedByMascot = aPeaks;
        }
        if (iSignificantTheoreticalFragmentions == null) {
            iSignificantTheoreticalFragmentions = getSignificantTheoreticalFragmentions();
        }
        return getMatchedIons(iSignificantTheoreticalFragmentions, lPeaksUsedByMascot);
    }

    /**
     * This method looks for matches between the FragmentIon[] and the Peak[], wich are both parameters.<br> This method
     * will return a Vector with FragmentIons that where matched in the spectrum.<br> Every FragmentIon in this vector
     * has a boolean with value = true.<br>
     *
     * @param fmv    FragmentIon Vector with Theoretical fragmentions.
     * @param aPeaks Peak[] with representing the massspectrum.
     * @return Vector with fragmentions that were matched in the spectrum.
     */
    private Vector getMatchedIons(Vector fmv, Peak[] aPeaks) {
        Vector lMatchedIons = new Vector();
        for (Iterator iter = fmv.iterator(); iter.hasNext();) {
            FragmentIon fi = (FragmentIon) iter.next();
            if (!fi.hasBeenMatched() &&
                    fi.isMatch(aPeaks, iFragmentMassErrorMargin)) {
                lMatchedIons.add(fi);
            }
        }
        return lMatchedIons;
    }

    /**
     * This method clears all the peaks that have allready been matched by the Fragmentions Vector.
     *
     * @param aPeaks        The original PeakList.
     * @param aFragmentions The fragmentions that have allready been matched, peaks corresponding to their mass will be
     *                      set to zero so they wont be matched anymore.
     * @return Peak[]            The reduced PeakList.
     */
    public Peak[] reducePeaks(Peak[] aPeaks, Vector aFragmentions) {
        // First get all the masses of the fragmentions and their error so you know wich peaks were matched.
        Vector lFragmentionsMasses = new Vector();
        for (int i = 0; i < aFragmentions.size(); i++) {
            FragmentIon fm = (FragmentIon) aFragmentions.get(i);
            // re-calculate the peak mass
            double lPeak = fm.getMZ() + fm.getTheoreticalExperimantalMassError();
            // if the peak was allready matched, you cannot add it twice (this happens if one peak is matched by two fragmentions)
            boolean lIsNotMatchedYet = true;
            if (lFragmentionsMasses.size() != 0) {
                for (int j = 0; j < lFragmentionsMasses.size(); j++) {
                    double lDouble = ((Double) lFragmentionsMasses.elementAt(j)).doubleValue();
                    if (lPeak == lDouble) {
                        lIsNotMatchedYet = false;
                    }
                }
            }
            if (lIsNotMatchedYet) {
                lFragmentionsMasses.add(new Double(lPeak));
            }
        }
        // Second copy aPeaks into lReducedPeaks if the mass of the Peak in aPeaks does not correspond to any value in the FragmentIonsMassas vector.
        Peak[] lReducedPeaks = new Peak[aPeaks.length - lFragmentionsMasses.size()];
        int lCountReducedPeaks = 0;
        boolean lPeakHasBeenMatched;
        for (int i = 0; i < aPeaks.length; i++) {
            Peak lPeak = new Peak(aPeaks[i].getMZ(), aPeaks[i].getIntensity());
            lPeakHasBeenMatched = false;
            for (int j = 0; j < lFragmentionsMasses.size(); j++) {
                double lTemp = ((Double) lFragmentionsMasses.get(j)).doubleValue();
                if (lTemp == lPeak.getMZ()) {
                    lPeakHasBeenMatched = true;
                    break;
                }
            }
            if (!lPeakHasBeenMatched) {
                lReducedPeaks[lCountReducedPeaks] = lPeak;
                lCountReducedPeaks++;
            }
        }
        return lReducedPeaks;
    }

    /**
     * This method will return a Vector with Significant FragmentIon Instances that were matched in the mass spectrum
     * AND where above a parametrical intensity threshold.
     *
     * @param aPeaks               Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was
     *                             created.
     * @param aMaxIntensity        double with the max intensity of the spectrum.
     * @param aIntensityPercentage This double is a percent (ex: 0.10) , The relative intensityThreshold will then be
     *                             (aMaxIntensity*aIntensityPercentage), only matches that are above this threshold will
     *                             be added to the Vector.
     * @return Vector Returns a vector with the significant FragmentIon instances that had a match above the intensity
     *         threshold in the Peak[].
     */
    public Vector getSignificantMatchedIonsAboveIntensityThreshold(Peak[] aPeaks, double aMaxIntensity, double aIntensityPercentage) {
        //get the significant theoretical fragmentions and pass them to the getMatchedIons method with intensity parameters.
        if (iSignificantTheoreticalFragmentions == null) {
            iSignificantTheoreticalFragmentions = getSignificantTheoreticalFragmentions();
        }
        return getMatchedIons(iSignificantTheoreticalFragmentions, aPeaks, aMaxIntensity, aIntensityPercentage);
    }

    /**
     * This method returns all significant fragmentions. Based on the ionseries int[]. It serves as input for the
     * different matching methods.
     *
     * @return Vector with all the significant theoretical fragmentions that should be checked for a match in the
     *         spectrum.
     */
    public Vector getSignificantTheoreticalFragmentions() {
        Vector lAllSignificantIons = new Vector();
        for (int i = 0; i < iIonSeriesFound.length; i++) {
            if (iIonSeriesFound[i] != 0) {
                FragmentIonImpl[] fma = getFragmentIons(i);
                for (int j = 0; j < fma.length; j++) {
                    //Set the importance factor in the fragmention.
                    if (iIonSeriesFound[i] == FragmentIon.Sign_NOT_Scoring) {
                        fma[j].setImportance(FragmentIon.Sign_NOT_Scoring);
                    }
                    if (iIonSeriesFound[i] == FragmentIon.Sign_AND_Scoring) {
                        fma[j].setImportance(FragmentIon.Sign_AND_Scoring);
                    }
                    lAllSignificantIons.add(fma[j]);
                }
            }
        }
        return lAllSignificantIons;
    }

    /**
     * This method returns an array with all possible B neutral loss ions.
     */
    public FragmentIonImpl[] getBNeutralLossIons() {
        Vector<FragmentIonImpl> lNeutralLossIons = new Vector<FragmentIonImpl>();
        // Iterate all modifications that could possibly yield a neutral loss ion.
        for (int i = 0; i < iMods.length; i++) {
            Modification lMod = iMods[i];
            if (lMod != null && lMod instanceof VariableModification) {
                double lNeutralLossMass = ((VariableModification) lMod).getNeutralLoss();
                if (lNeutralLossMass > 0) {
                    // Ok, this modification is a possible contributor of neutrol loss fragment ions.
                    // First, get all b-ions and clone plus change the name and mass
                    try {

                        for (int j = i - 1; j < iBions.length; j++) {
                            // Iterate b-ions from the modification index.
                            // KENNYPRR
                            // e.g.: modification index 5 equals starting from b5, and b-ion5 is located at the b-ion array index 4.
                            FragmentIonImpl lBion = iBions[j].clone();
                            lBion.setMZ(lBion.getMZ() - lNeutralLossMass);
                            lBion.setType(lBion.getType() + "-" + lMod.getShortType());
                            lNeutralLossIons.add(lBion);
                        }
                    } catch (CloneNotSupportedException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        if (lNeutralLossIons.size() > 0) {
            return lNeutralLossIons.toArray(new FragmentIonImpl[lNeutralLossIons.size()]);
        } else {
            return null;
        }
    }

    /**
     * This method returns an array with all possible Y neutral loss ions.
     */
    public FragmentIonImpl[] getYNeutralLossIons() {
        Vector<FragmentIonImpl> lNeutralLossIons = new Vector<FragmentIonImpl>();
        // Iterate all modifications that could possibly yield a neutral loss ion.
        for (int i = 0; i < iMods.length; i++) {
            Modification lMod = iMods[i];
            if (lMod != null && lMod instanceof VariableModification) {
                double lNeutralLossMass = ((VariableModification) lMod).getNeutralLoss();
                if (lNeutralLossMass > 0) {
                    // Ok, this modification is a possible contributor of neutrol loss fragment ions.
                    // First, get all b-ions and clone plus change the name and mass
                    try {

                        for (int j = iMods.length - i - 1; j < iYions.length; j++) {
                            // KENNYPRR
                            // e.g.: modification index 5 equals starting from y3, and y-ion3 is located at the y-ion array index 2.
                            FragmentIonImpl lYion = iYions[j].clone();
                            lYion.setMZ(lYion.getMZ() - lNeutralLossMass);
                            lYion.setType(lYion.getType() + "-" + lMod.getShortType());
                            lNeutralLossIons.add(lYion);
                        }
                    } catch (CloneNotSupportedException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }

        if (lNeutralLossIons.size() > 0) {
            return lNeutralLossIons.toArray(new FragmentIonImpl[lNeutralLossIons.size()]);
        } else {
            return null;
        }
    }


    /**
     * This method returns a FragmentIonImpl[] with fragmentions. The fragmention type is made up by a switch()
     *
     * @param aIonSeriesIndex
     * @return FragmentIonImpl[]   Returns a FragmentIonImpl[] with fragmentions of the requested type. (by int
     *         aIonSeries read from the iIonSeries int[].
     */
    private FragmentIonImpl[] getFragmentIons(int aIonSeriesIndex) {
        FragmentIonImpl[] lFragmentIons = null;
        switch (aIonSeriesIndex) {

            /**   Mascot 2.2
             0  a
             1  place holder
             2  a++

             3  b
             4  place holder
             5  b++

             6  y
             7  place holder
             8  y++

             9  c
             10  c++
             11  x
             12  x++
             13  z
             14  z++
             15  z+H
             16  z+H++
             17  z+2H
             18  z+2H++

             0 0 0 0 0 0 0 0 0 0 1  0  0  2  0  0  2  0  1
             0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18
             */

            case 0:
                lFragmentIons = getASeries();
                break;

            case 1:
                throw new MascotDatfileException("Ionseries index = 1; this is a placeholder!");

            case 2:
                lFragmentIons = getADoubleSeries();
                break;

            case 3:
                lFragmentIons = getBSeries();
                break;

            case 4:
                throw new MascotDatfileException("Ionseries index = 4; this is a placeholder!");

            case 5:
                lFragmentIons = getBDoubleSeries();
                break;

            case 6:
                lFragmentIons = getYSeries();
                break;

            case 7:
                throw new MascotDatfileException("Ionseries index = 7; this is a placeholder!");

            case 8:
                lFragmentIons = getYDoubleSeries();
                break;

            case 9:
                lFragmentIons = getCions();
                break;

            case 10:
                lFragmentIons = getCDoubleions();
                break;

            case 11:
                lFragmentIons = getXions();
                break;

            case 12:
                lFragmentIons = getXDoubleions();
                break;

            case 13:
                lFragmentIons = getZions();
                break;

            case 14:
                lFragmentIons = getZDoubleions();
                break;

            case 15:
                lFragmentIons = getZHions();
                break;

            case 16:
                lFragmentIons = getZHDoubleions();
                break;

            case 17:
                lFragmentIons = getZHHions();
                break;

            case 18:
                lFragmentIons = getZHHDoubleions();
                break;

            default:
                throw new MascotDatfileException("Ionseries index '" + aIonSeriesIndex + "' not handled by MascotDatfile!!");
        }
        return lFragmentIons;
    }

    /**
     * This method returns the A-ions and its associated ions. (A-ions, A-H2O-ions and A-NH3-ions)
     *
     * @return FragmentIonImpl[]  Returns a FragmentIonImpl[] with the A-ions, A-H2O-ions and A-NH3-ions, if Mascot used
     *         a-ions according the IonSeries, all these ions should be checked.
     */
    private FragmentIonImpl[] getASeries() {
        FragmentIonImpl[] lAions = getAions();
        FragmentIonImpl[] lAH2O = getAH2Oions();
        FragmentIonImpl[] lANH3 = getANH3ions();
        int lASeriesLength = lAions.length;
        if (lAH2O != null) {
            lASeriesLength = lASeriesLength + lAH2O.length;
        }
        if (lANH3 != null) {
            lASeriesLength = lASeriesLength + lANH3.length;
        }
        FragmentIonImpl[] lASeries = new FragmentIonImpl[lASeriesLength];
        System.arraycopy(lAions, 0, lASeries, 0, lAions.length);
        int lASeriesIndex = lAions.length;
        if (lANH3 != null) {
            System.arraycopy(lANH3, 0, lASeries, lASeriesIndex, lANH3.length);
            lASeriesIndex = lASeriesIndex + lANH3.length;
        }
        if (lAH2O != null) {
            System.arraycopy(lAH2O, 0, lASeries, (lASeriesIndex), lAH2O.length);
            lASeriesIndex = lASeriesIndex + lAH2O.length;
        }
        return lASeries;
    }

    /**
     * This method will report on all the a-ions minus 'H2O' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the a-ions minus 'H2O', ordered from low to high mass.
     */
    public FragmentIonImpl[] getAH2Oions() {
        int lStart = getH2OStartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the a-ions minus H2O start by a4. lStart equals 3. So only iAions.length=7, 7-3 = 4
        FragmentIonImpl[] lAH2Oions = null;
        if (lStart != iBions.length) {
            FragmentIonImpl[] lAions = getAions();
            lAH2Oions = new FragmentIonImpl[iBions.length - lStart];
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // a-ion minus 'H2O'
                lAH2Oions[lCount] = new FragmentIonImpl((lAions[i].getMZ() - 18.010565), iFragmentMassErrorMargin, FragmentIon.A_H2O_ION, (i + 1), "a-H2O");
                lCount++;
            }
        }
        return lAH2Oions;
    }

    /**
     * This method will report on all the a-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the a-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getANH3ions() {
        int lStart = getNH3StartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the a-ions minus H2O start by a4. lStart equals 3. So only iAions.length=7, 7-3 = 4
        FragmentIonImpl[] lANH3ions = null;
        if (lStart != iBions.length) {
            lANH3ions = new FragmentIonImpl[iBions.length - lStart];
            FragmentIonImpl[] lAions = getAions();
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // a-ion minus 'NH3'
                lANH3ions[lCount] = new FragmentIonImpl((lAions[i].getMZ() - 17.026549), iFragmentMassErrorMargin, FragmentIon.A_NH3_ION, (i + 1), "a-NH3");
                lCount++;
            }
        }
        return lANH3ions;
    }

    /**
     * This method returns the double charged A-ions and its associated ions. (A++-ions, A++-H2O-ions and A++-NH3-ions)
     *
     * @return FragmentIonImpl[]  Returns a FragmentIonImpl[] with the A++-ions, A++-H2O-ions and A++-NH3-ions, if
     *         Mascot used a++-ions according the IonSeries, all these ions should be checked.
     */
    private FragmentIonImpl[] getADoubleSeries() {
        FragmentIonImpl[] lADouble = getADoubleions();
        FragmentIonImpl[] lADoubleH2O = getADoubleH2Oions();
        FragmentIonImpl[] lADoubleNH3 = getADoubleNH3ions();
        int lADoubleSeriesLength = lADouble.length;
        if (lADoubleH2O != null) {
            lADoubleSeriesLength = lADoubleSeriesLength + lADoubleH2O.length;
        }
        if (lADoubleNH3 != null) {
            lADoubleSeriesLength = lADoubleSeriesLength + lADoubleNH3.length;
        }
        FragmentIonImpl[] lADoubleSeries = new FragmentIonImpl[lADoubleSeriesLength];
        System.arraycopy(lADouble, 0, lADoubleSeries, 0, lADouble.length);
        int lADoubleSeriesIndex = lADouble.length;
        if (lADoubleNH3 != null) {
            System.arraycopy(lADoubleNH3, 0, lADoubleSeries, lADoubleSeriesIndex, lADoubleNH3.length);
            lADoubleSeriesIndex = lADoubleSeriesIndex + lADoubleNH3.length;
        }
        if (lADoubleH2O != null) {
            System.arraycopy(lADoubleH2O, 0, lADoubleSeries, (lADoubleSeriesIndex), lADoubleH2O.length);
            lADoubleSeriesIndex = lADoubleSeriesIndex + lADoubleH2O.length;
        }
        return lADoubleSeries;
    }

    /**
     * This method will report on all the double charged b-ions plus 'CO' minus 'H2', for this given PeptideHit.
     *
     * @return Aion[] with the double charged b-ions plus 'CO', minus 'H2', ordered from low to high mass.
     */
    public FragmentIonImpl[] getADoubleions() {
        FragmentIonImpl[] lAions = getAions();
        FragmentIonImpl[] lADoubleIons = new FragmentIonImpl[lAions.length];
        for (int i = 0; i < lAions.length; i++) {
            // b-ion minus 'H2O' plus H divided by 2.
            lADoubleIons[i] = new FragmentIonImpl(((lAions[i].getMZ() + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.A_DOUBLE_ION, (i + 1), "a++");
        }
        return lADoubleIons;
    }

    /**
     * This method will report on all the b-ions plus 'CO' minus 'H2', for this given PeptideHit.
     *
     * @return Aion[] with the b-ions plus 'CO', minus 'H2', ordered from low to high mass.
     */
    public FragmentIonImpl[] getAions() {
        FragmentIonImpl[] lAions = new FragmentIonImpl[iBions.length];
        for (int i = 0; i < iBions.length; i++) {
            // b-ion minus 'CO' plus 'H2'
            lAions[i] = new FragmentIonImpl((iBions[i].getMZ() - 27.994915), iFragmentMassErrorMargin, FragmentIon.A_ION, (i + 1), "a");
        }
        return lAions;
    }

    /**
     * This method will report on all the double charged a-ions minus 'H2O' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged a-ions minus 'H2O', ordered from low to high mass.
     */
    public FragmentIonImpl[] getADoubleH2Oions() {
        int lStart = getH2OStartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the a-ions minus H2O start by a4. lStart equals 3. So only iAions.length=7, 7-3 = 4
        FragmentIonImpl[] lADoubleH2Oions = null;
        if (lStart != iBions.length) {
            lADoubleH2Oions = new FragmentIonImpl[iBions.length - lStart];
            FragmentIonImpl[] lAions = getAions();
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // double chared a-ion minus 'H2O'
                lADoubleH2Oions[lCount] = new FragmentIonImpl(((lAions[i].getMZ() - 18.010565 + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.A_H2O_DOUBLE_ION, (i + 1), "a++-H2O");
                lCount++;
            }
        }
        return lADoubleH2Oions;
    }

    /**
     * This method will report on all the double charged a-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged a-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getADoubleNH3ions() {
        int lStart = getNH3StartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the a-ions minus H2O start by a4. lStart equals 3. So only iAions.length=7, 7-3 = 4
        FragmentIonImpl[] lADoubleNH3ions = null;
        if (lStart != iBions.length) {
            lADoubleNH3ions = new FragmentIonImpl[iBions.length - lStart];
            FragmentIonImpl[] lAions = getAions();
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // double chared a-ion minus 'NH3'
                lADoubleNH3ions[lCount] = new FragmentIonImpl(((lAions[i].getMZ() - 17.026549 + 1) / 2), iFragmentMassErrorMargin, FragmentIon.A_NH3_DOUBLE_ION, (i + 1), "a++-NH3");
                lCount++;
            }
        }
        return lADoubleNH3ions;
    }

    /**
     * This method returns the B-ions and its associated ions. (B-ions, B-H2O-ions and B-NH3-ions)
     *
     * @return Vector  Returns a FragmentIon[] with the B-ions, B-H2O-ions and B-NH3-ions, if Mascot used b-ions
     *         according the IonSeries, all these ions should be checked.
     */
    private FragmentIonImpl[] getBSeries() {
        FragmentIonImpl[] lBH2O = getBH2Oions();
        FragmentIonImpl[] lBNH3 = getBNH3ions();
        FragmentIonImpl[] lBNL = getBNeutralLossIons();

        int lBSeriesLength = iBions.length;
        if (lBH2O != null) {
            lBSeriesLength = lBSeriesLength + lBH2O.length;
        }
        if (lBNH3 != null) {
            lBSeriesLength = lBSeriesLength + lBNH3.length;
        }
        if (lBNL!= null) {
            lBSeriesLength = lBSeriesLength + lBNL.length;
        }

        FragmentIonImpl[] lBSeries = new FragmentIonImpl[lBSeriesLength];
        System.arraycopy(iBions, 0, lBSeries, 0, iBions.length);

        int lBSeriesIndex = iBions.length;
        if (lBNH3 != null) {
            System.arraycopy(lBNH3, 0, lBSeries, lBSeriesIndex, lBNH3.length);
            lBSeriesIndex = lBSeriesIndex + lBNH3.length;
        }
        if (lBH2O != null) {
            System.arraycopy(lBH2O, 0, lBSeries, (lBSeriesIndex), lBH2O.length);
            lBSeriesIndex = lBSeriesIndex + lBH2O.length;
        }
        if (lBNL!= null) {
            System.arraycopy(lBNL, 0, lBSeries, (lBSeriesIndex), lBNL.length);
            lBSeriesIndex = lBSeriesIndex + lBNL.length;
        }
        return lBSeries;
    }

    /**
     * This method will report on all the b-ions minus 'H2O' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the b-ions minus 'H2O', ordered from low to high mass.
     */
    public FragmentIonImpl[] getBH2Oions() {
        int lStart = getH2OStartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the b-ions minus H2O start by b4. lStart equals 3. So only iBions.length=7, 7-3 = 4
        FragmentIonImpl[] lBH2Oions = null;
        if (lStart != iBions.length) {
            lBH2Oions = new FragmentIonImpl[iBions.length - lStart];
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // b-ion minus 'H2O'
                lBH2Oions[lCount] = new FragmentIonImpl((iBions[i].getMZ() - 18.010565), iFragmentMassErrorMargin, FragmentIon.B_H2O_ION, (i + 1), "b-H2O");
                lCount++;
            }
        }
        return lBH2Oions;
    }

    /**
     * This method will report on all the b-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the b-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getBNH3ions() {
        int lStart = getNH3StartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the b-ions minus H2O start by b4. lStart equals 3. So only iBions.length=7, 7-3 = 4
        FragmentIonImpl[] lBNH3ions = null;
        if (lStart != iBions.length) {
            lBNH3ions = new FragmentIonImpl[iBions.length - lStart];
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // b-ion minus 'NH3'
                lBNH3ions[lCount] = new FragmentIonImpl((iBions[i].getMZ() - 17.026549), iFragmentMassErrorMargin, FragmentIon.B_NH3_ION, (i + 1), "b-NH3");
                lCount++;
            }
        }
        return lBNH3ions;
    }

    /**
     * This method returns the double charged B-ions and its associated ions. (B++-ions, B++-H2O-ions and B++-NH3-ions)
     *
     * @return Vector  Returns a vector with the B++-ions, B++-H2O-ions and B++-NH3-ions, if Mascot used b++-ions
     *         according the IonSeries, all these ions should be checked.
     */
    private FragmentIonImpl[] getBDoubleSeries() {
        FragmentIonImpl[] lBDouble = getBDoubleions();
        FragmentIonImpl[] lBDoubleH2O = getBDoubleH2Oions();
        FragmentIonImpl[] lBDoubleNH3 = getBDoubleNH3ions();
        int lBDoubleSeriesLength = lBDouble.length;
        if (lBDoubleH2O != null) {
            lBDoubleSeriesLength = lBDoubleSeriesLength + lBDoubleH2O.length;
        }
        if (lBDoubleNH3 != null) {
            lBDoubleSeriesLength = lBDoubleSeriesLength + lBDoubleNH3.length;
        }
        FragmentIonImpl[] lBDoubleSeries = new FragmentIonImpl[lBDoubleSeriesLength];
        System.arraycopy(lBDouble, 0, lBDoubleSeries, 0, lBDouble.length);
        int lBDoubleSeriesIndex = lBDouble.length;
        if (lBDoubleNH3 != null) {
            System.arraycopy(lBDoubleNH3, 0, lBDoubleSeries, lBDoubleSeriesIndex, lBDoubleNH3.length);
            lBDoubleSeriesIndex = lBDoubleSeriesIndex + lBDoubleNH3.length;
        }
        if (lBDoubleH2O != null) {
            System.arraycopy(lBDoubleH2O, 0, lBDoubleSeries, (lBDoubleSeriesIndex), lBDoubleH2O.length);
            lBDoubleSeriesIndex = lBDoubleSeriesIndex + lBDoubleH2O.length;
        }
        return lBDoubleSeries;
    }

    /**
     * This method will report on all double charged b-ions for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged b-ions, ordered from low to high mass.
     */
    public FragmentIonImpl[] getBDoubleions() {
        FragmentIonImpl[] lBDoubleIons = new FragmentIonImpl[iBions.length];
        for (int i = 0; i < iBions.length; i++) {
            // b-ion plus 'H' over a double charge
            lBDoubleIons[i] = new FragmentIonImpl(((iBions[i].getMZ() + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.B_DOUBLE_ION, (i + 1), "b++");
        }
        return lBDoubleIons;
    }

    /**
     * This method will report on all the double charged b-ions minus 'H2O' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged b-ions minus 'H2O', ordered from low to high mass.
     */
    public FragmentIonImpl[] getBDoubleH2Oions() {
        int lStart = getH2OStartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the b-ions minus H2O start by b4. lStart equals 3. So only iBions.length=7, 7-3 = 4
        FragmentIonImpl[] lBDoubleH2Oions = null;
        if (lStart != iBions.length) {
            lBDoubleH2Oions = new FragmentIonImpl[iBions.length - lStart];
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // double charged b-ion minus 'H2O'
                lBDoubleH2Oions[lCount] = new FragmentIonImpl(((iBions[i].getMZ() - 18.010565 + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.B_H2O_DOUBLE_ION, (i + 1), "b++-H2O");
                lCount++;
            }
        }
        return lBDoubleH2Oions;
    }

    /**
     * This method will report on all the double charged b-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged b-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getBDoubleNH3ions() {
        int lStart = getNH3StartB();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the b-ions minus H2O start by b4. lStart equals 3. So only iBions.length=7, 7-3 = 4
        FragmentIonImpl[] lBDoubleNH3ions = null;
        if (lStart != iBions.length) {
            lBDoubleNH3ions = new FragmentIonImpl[iBions.length - lStart];
            int lCount = 0;
            for (int i = lStart; i < iBions.length; i++) {
                // double charged b-ion minus 'NH3'
                lBDoubleNH3ions[lCount] = new FragmentIonImpl(((iBions[i].getMZ() - 17.026549 + 1) / 2), iFragmentMassErrorMargin, FragmentIon.B_NH3_DOUBLE_ION, (i + 1), "b++-NH3");
                lCount++;
            }
        }
        return lBDoubleNH3ions;
    }

    /**
     * This method returns the Y-ions and its associated ions. (Y-ions, Y-H2O-ions and Y-NH3-ions)
     *
     * @return FragmentIonImpl[]  Returns a FragmentIonImpl[] with the Y-ions, Y-H2O-ions and Y-NH3-ions, if Mascot used
     *         y-ions according the IonSeries, all these ions should be checked.
     */
    private FragmentIonImpl[] getYSeries() {
        FragmentIonImpl[] lYH2O = getYH2Oions();
        FragmentIonImpl[] lYNH3 = getYNH3ions();
        FragmentIonImpl[] lYNL = getYNeutralLossIons();
        int lYSeriesLength = iYions.length;
        if (lYH2O != null) {
            lYSeriesLength = lYSeriesLength + lYH2O.length;
        }
        if (lYNH3 != null) {
            lYSeriesLength = lYSeriesLength + lYNH3.length;
        }
        if (lYNL != null) {
            lYSeriesLength = lYSeriesLength + lYNL.length;
        }

        FragmentIonImpl[] lYSeries = new FragmentIonImpl[lYSeriesLength];
        System.arraycopy(iYions, 0, lYSeries, 0, iYions.length);
        int lYSeriesIndex = iYions.length;

        if (lYNH3 != null) {
            System.arraycopy(lYNH3, 0, lYSeries, lYSeriesIndex, lYNH3.length);
            lYSeriesIndex = lYSeriesIndex + lYNH3.length;
        }

        if (lYH2O != null) {
            System.arraycopy(lYH2O, 0, lYSeries, (lYSeriesIndex), lYH2O.length);
            lYSeriesIndex = lYSeriesIndex + lYH2O.length;
        }

        if (lYNL!= null) {
            System.arraycopy(lYNL, 0, lYSeries, (lYSeriesIndex), lYNL.length);
            lYSeriesIndex = lYSeriesIndex + lYNL.length;
        }

        return lYSeries;
    }

    /**
     * This method will report on all the y-ions minus 'H2O' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the y-ions minus 'H2O', ordered from low to high mass.
     */
    public FragmentIonImpl[] getYH2Oions() {
        int lStart = getH2OStartY();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the y-ions minus H2O start by y5. lStart equals 3. So iYions.length=7, 7-(5-1) = 3 is the length of the FragmentIonImpl[].
        FragmentIonImpl[] lYH2Oions = null;
        if (lStart != 0) {
            lYH2Oions = new FragmentIonImpl[lStart];
            int lCount = 0;
            for (int i = (iYions.length - lStart); i < iYions.length; i++) {
                // y-ion minus 'H2O'
                lYH2Oions[lCount] = new FragmentIonImpl((iYions[i].getMZ() - 18.010565), iFragmentMassErrorMargin, FragmentIon.Y_H2O_ION, (i + 1), "y-H2O");
                lCount++;
            }
        }
        return lYH2Oions;
    }

    /**
     * This method will report on all the y-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the y-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getYNH3ions() {
        int lStart = getNH3StartY();
        //if a peptide KLNEGGRT must be checked for loss of H2O the y-ions minus H2O start by y5. lStart equals 3. So iYions.length=7, 7-(5-1) = 3 is the length of the FragmentIonImpl[].
        FragmentIonImpl[] lYNH3ions = null;
        if (lStart != 0) {
            lYNH3ions = new FragmentIonImpl[lStart];
            int lCount = 0;
            for (int i = (iYions.length - lStart); i < iYions.length; i++) {
                // y-ion minus 'NH3'
                lYNH3ions[lCount] = new FragmentIonImpl((iYions[i].getMZ() - 17.026549), iFragmentMassErrorMargin, FragmentIon.Y_NH3_ION, (i + 1), "y-NH3");
                lCount++;
            }
        }
        return lYNH3ions;
    }

    /**
     * This method returns the double charged Y-ions and its associated ions. (Y++-ions, Y++-H2O-ions and Y++-NH3-ions)
     *
     * @return FragmentIonImpl[]  Returns a FragmentIonImpl[] with the Y++-ions, Y++-H2O-ions and Y++-NH3-ions, if
     *         Mascot used y++-ions according the IonSeries, all these ions should be checked.
     */
    private FragmentIonImpl[] getYDoubleSeries() {
        FragmentIonImpl[] lYDouble = getYDoubleions();
        FragmentIonImpl[] lYDoubleH2O = getYDoubleH2Oions();
        FragmentIonImpl[] lYDoubleNH3 = getYDoubleNH3ions();
        int lYDoubleSeriesLength = lYDouble.length;
        if (lYDoubleH2O != null) {
            lYDoubleSeriesLength = lYDoubleSeriesLength + lYDoubleH2O.length;
        }
        if (lYDoubleNH3 != null) {
            lYDoubleSeriesLength = lYDoubleSeriesLength + lYDoubleNH3.length;
        }
        FragmentIonImpl[] lYDoubleSeries = new FragmentIonImpl[lYDoubleSeriesLength];
        System.arraycopy(lYDouble, 0, lYDoubleSeries, 0, lYDouble.length);
        int lYDoubleSeriesIndex = lYDouble.length;
        if (lYDoubleNH3 != null) {
            System.arraycopy(lYDoubleNH3, 0, lYDoubleSeries, lYDoubleSeriesIndex, lYDoubleNH3.length);
            lYDoubleSeriesIndex = lYDoubleSeriesIndex + lYDoubleNH3.length;
        }
        if (lYDoubleH2O != null) {
            System.arraycopy(lYDoubleH2O, 0, lYDoubleSeries, (lYDoubleSeriesIndex), lYDoubleH2O.length);
            lYDoubleSeriesIndex = lYDoubleSeriesIndex + lYDoubleH2O.length;
        }
        return lYDoubleSeries;
    }

    /**
     * This method will report on all double charged y-ions for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged y-ions, ordered from low to high mass.
     */
    public FragmentIonImpl[] getYDoubleions() {
        FragmentIonImpl[] lY2ions = new FragmentIonImpl[iYions.length];
        for (int i = 0; i < iYions.length; i++) {
            // y-ion plus 'H' over a double charge
            lY2ions[i] = new FragmentIonImpl(((iYions[i].getMZ() + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.Y_DOUBLE_ION, (i + 1), "y++");
        }
        return lY2ions;
    }

    /**
     * This method will report on all the double charged y-ions minus 'H2O' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged y-ions minus 'H2O', ordered from low to high mass.
     */
    public FragmentIonImpl[] getYDoubleH2Oions() {
        int lStart = getH2OStartY();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the y-ions minus H2O start by y5. lStart equals 3. So iYions.length=7, 7-(5-1) = 3 is the length of the FragmentIonImpl[].
        FragmentIonImpl[] lYDoubleH2Oions = null;
        if (lStart != 0) {
            lYDoubleH2Oions = new FragmentIonImpl[lStart];
            int lCount = 0;
            for (int i = (iYions.length - lStart); i < iYions.length; i++) {
                // double charged y-ion minus 'H2O'
                lYDoubleH2Oions[lCount] = new FragmentIonImpl(((iYions[i].getMZ() - 18.010565 + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.Y_H2O_DOUBLE_ION, (i + 1), "y++-H2O");
                lCount++;
            }
        }
        return lYDoubleH2Oions;
    }

    /**
     * This method will report on all the double charged y-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged y-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getYDoubleNH3ions() {
        int lStart = getNH3StartY();
        //if a peptide KLNEGGRT must be checked for loss of H2O; the y-ions minus H2O start by y5. lStart equals 3. So iYions.length=7, 7-(5-1) = 3 is the length of the FragmentIonImpl[].
        FragmentIonImpl[] lYDoubleNH3ions = null;
        if (lStart != 0) {
            lYDoubleNH3ions = new FragmentIonImpl[lStart];
            int lCount = 0;
            for (int i = (iYions.length - lStart); i < iYions.length; i++) {
                // double charged y-ion minus 'NH3'
                lYDoubleNH3ions[lCount] = new FragmentIonImpl(((iYions[i].getMZ() - 17.026549 + 1) / 2), iFragmentMassErrorMargin, FragmentIon.Y_NH3_DOUBLE_ION, (i + 1), "y++-NH3");
                lCount++;
            }
        }
        return lYDoubleNH3ions;
    }

    /**
     * This method will report on all the double charged b-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged b-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getCDoubleions() {
        FragmentIonImpl[] lCions = getCions();
        FragmentIonImpl[] lCDoubleIons = new FragmentIonImpl[lCions.length];
        for (int i = 0; i < lCions.length; i++) {
            // b-ion minus 'H2O' plus H divided by 2.
            lCDoubleIons[i] = new FragmentIonImpl(((lCions[i].getMZ() + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.C_DOUBLE_ION, (i + 1), "c++");
        }
        return lCDoubleIons;
    }

    /**
     * This method will report on all the b-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the b-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getCions() {
        FragmentIonImpl[] lCions = new FragmentIonImpl[iBions.length];
        for (int i = 0; i < iBions.length; i++) {
            // b-ion minus 'NH3'
            lCions[i] = new FragmentIonImpl((iBions[i].getMZ() + 17.026549), iFragmentMassErrorMargin, FragmentIon.C_ION, (i + 1), "c");
        }
        return lCions;
    }

    /**
     * This method will report on all the double charged b-ions plus 'CO' minus 'H2', for this given PeptideHit.
     *
     * @return Xion[] with the double charged y-ions plus 'CO', minus 'H2', ordered from low to high mass.
     */
    public FragmentIonImpl[] getXDoubleions() {
        FragmentIonImpl[] lXions = getXions();
        FragmentIonImpl[] lXDoubleIons = new FragmentIonImpl[lXions.length];
        for (int i = 0; i < lXions.length; i++) {
            // y-ion minus 'H2O' plus H divided by 2.
            lXDoubleIons[i] = new FragmentIonImpl(((lXions[i].getMZ() + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.X_DOUBLE_ION, (i + 1), "x++");
        }
        return lXDoubleIons;
    }

    /**
     * This method will report on all the y-ions minus 'H2O' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the y-ions plus 'CO', minus 'H2', ordered from low to high mass.
     */
    public FragmentIonImpl[] getXions() {
        FragmentIonImpl[] lXions = new FragmentIonImpl[iYions.length];
        for (int i = 0; i < iYions.length; i++) {
            // y-ion minus 'CO' plus '2H'
            lXions[i] = new FragmentIonImpl((iYions[i].getMZ() + 25.979265), iFragmentMassErrorMargin, FragmentIon.X_ION, (i + 1), "x");
        }
        return lXions;
    }

    /**
     * This method will report on all the double charged y-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the double charged y-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getZDoubleions() {
        FragmentIonImpl[] lZions = getZions();
        FragmentIonImpl[] lZDoubleIons = new FragmentIonImpl[lZions.length];
        for (int i = 0; i < lZions.length; i++) {
            // y-ion minus 'H2O' plus H divided by 2.
            lZDoubleIons[i] = new FragmentIonImpl(((lZions[i].getMZ() + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.Z_DOUBLE_ION, (i + 1), "z++");
        }
        return lZDoubleIons;
    }

    /**
     * This method will report on all the y-ions minus 'NH3' for the given PeptideHit.
     *
     * @return FragmentIonImpl[] with the y-ions minus 'NH3', ordered from low to high mass.
     */
    public FragmentIonImpl[] getZions() {
        FragmentIonImpl[] lZions = new FragmentIonImpl[iYions.length];
        for (int i = 0; i < iYions.length; i++) {
            lZions[i] = new FragmentIonImpl((iYions[i].getMZ() - 17.026549), iFragmentMassErrorMargin, FragmentIon.Z_ION, (i + 1), "z");
        }
        return lZions;
    }

    public FragmentIonImpl[] getZHions() {
        FragmentIonImpl[] lZHions = new FragmentIonImpl[iYions.length];
        for (int i = 0; i < iYions.length; i++) {
            lZHions[i] = new FragmentIonImpl((iYions[i].getMZ() - 17.026549 + 1.007825), iFragmentMassErrorMargin, FragmentIon.ZH_ION, (i + 1), "zH");
        }
        return lZHions;
    }

    public FragmentIonImpl[] getZHDoubleions() {
        FragmentIonImpl[] lZHions = getZions();
        FragmentIonImpl[] lZDoubleIons = new FragmentIonImpl[lZHions.length];
        for (int i = 0; i < lZHions.length; i++) {
            lZDoubleIons[i] = new FragmentIonImpl(((lZHions[i].getMZ() + 1.007825 + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.ZH_DOUBLE_ION, (i + 1), "zH++");
        }
        return lZDoubleIons;
    }

    public FragmentIonImpl[] getZHHions() {
        FragmentIonImpl[] lZHHions = new FragmentIonImpl[iYions.length];
        for (int i = 0; i < iYions.length; i++) {
            // y-ion minus 'NH3'
            lZHHions[i] = new FragmentIonImpl((iYions[i].getMZ() - 17.026549 + 1.007825 + 1.007825), iFragmentMassErrorMargin, FragmentIon.ZHH_ION, (i + 1), "zHH");
        }
        return lZHHions;
    }

    public FragmentIonImpl[] getZHHDoubleions() {
        FragmentIonImpl[] lZHions = getZions();
        FragmentIonImpl[] lZDoubleIons = new FragmentIonImpl[lZHions.length];
        for (int i = 0; i < lZHions.length; i++) {
            // y-ion minus 'H2O' plus H divided by 2.
            lZDoubleIons[i] = new FragmentIonImpl(((lZHions[i].getMZ() + 1.007825 + 1.007825 + 1.007825) / 2), iFragmentMassErrorMargin, FragmentIon.ZHH_DOUBLE_ION, (i + 1), "zHH++");
        }
        return lZDoubleIons;
    }

    /**
     * This method looks for matches between the FragmentIon[] and the Peak[], <b>The matched peak must be above the
     * intensity threshold(parameters)</b>.<br> This method will return a Vector with FragmentIons that where matched in
     * the spectrum.<br> Every FragmentIon in this vector has a boolean with value = true.<br>
     *
     * @param fmv    FragmentIon Vector with Theoretical fragmentions.
     * @param aPeaks Peak[] with representing the massspectrum.
     * @return Vector with fragmentions that were matched in the spectrum.
     */
    private Vector getMatchedIons(Vector fmv, Peak[] aPeaks, double aMaxIntensity, double aIntensityPercentage) {
        Vector lMatchedIons = new Vector();
        for (Iterator iter = fmv.iterator(); iter.hasNext();) {
            FragmentIon fi = (FragmentIon) iter.next();
            if (!fi.hasBeenMatched() &&
                    fi.isMatchAboveIntensityThreshold(aPeaks, aMaxIntensity, aIntensityPercentage, iFragmentMassErrorMargin)) {
                lMatchedIons.add(fi);
            }
        }
        return lMatchedIons;
    }

    /**
     * This method will return a Vector with Non Significant FragmentIon Instances that were matched in the mass
     * spectrum AND where above a parametrical intensity threshold.
     *
     * @param aPeaks               Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was
     *                             created.
     * @param aMaxIntensity        double with the max intensity of the spectrum.
     * @param aIntensityPercentage This double is a percent (ex: 0.10) , The relative intensityThreshold will then be
     *                             (aMaxIntensity*aIntensityPercentage), only matches that are above this threshold will
     *                             be added to the Vector.
     * @return Vector Returns a vector with non significant FragmentIon instances that had a match above the intensity
     *         threshold in the Peak[].
     */
    public Vector getNonSignificantMatchedIonsAboveIntensityThreshold(Peak[] aPeaks, double aMaxIntensity, double aIntensityPercentage) {
        //get the significant theoretical fragmentions and pass them to the getMatchedIons method with intensity parameters.
        if (iNonSignificantTheoreticalFragmentions == null) {
            iNonSignificantTheoreticalFragmentions = getNonSignificantTheoreticalFragmentions();
        }
        return getMatchedIons(iNonSignificantTheoreticalFragmentions, aPeaks, aMaxIntensity, iIntensityPercentage);
    }

    /**
     * This method returns all non-significant fragmentions based on the ionseries int[], these are all the fragmentions
     * with importance equal to 0. It serves as input for the different matching methods.
     *
     * @return Vector with all the non-significant theoretical fragmentions.
     */
    public Vector getNonSignificantTheoreticalFragmentions() {
        Vector lAllNonSignificantIons = new Vector();
        // By this we will only get non-significant b, b2+, y and y2+ fragmentions.
        for (int i = 0; i < iIonSeriesRules.length; i++) {
            if (iIonSeriesFound[iIonSeriesRules[i]] == 0) {
                FragmentIonImpl[] fma = getFragmentIons(iIonSeriesRules[i]);
                for (int j = 0; j < fma.length; j++) {
                    // Set the importance factor in the fragmention.
                    fma[j].setImportance(FragmentIon.NOT_Sign_NOT_Scoring);
                    // Add to the Vector with non-significant fragmentions.
                    lAllNonSignificantIons.add(fma[j]);
                }
            }
        }
        return lAllNonSignificantIons;
    }

    /**
     * This method returns a FragmentIonImp[] with the precursor as a fragmention and the immoniumIons.
     */
    public Vector getPrecursorAndImmoniumIons() {
        Vector lPrecAndImmoIons = new Vector();
        if (iPrecursorMZ != 0.0) {
            lPrecAndImmoIons.add(new FragmentIonImpl(iPrecursorMZ, iFragmentMassErrorMargin, FragmentIon.PRECURSOR, 0, "Prec " + iPrecursorCharge));
            lPrecAndImmoIons.add(new FragmentIonImpl(iPrecursorMZ - ((18.010565) / (Double.parseDouble(iPrecursorCharge.substring(0, 1)))), iFragmentMassErrorMargin, FragmentIon.PRECURSOR_LOSS, 0, "Prec-H2O " + iPrecursorCharge));
            lPrecAndImmoIons.add(new FragmentIonImpl(iPrecursorMZ - ((17.026549) / (Double.parseDouble(iPrecursorCharge.substring(0, 1)))), iFragmentMassErrorMargin, FragmentIon.PRECURSOR_LOSS, 0, "Prec-NH3 " + iPrecursorCharge));
        }

        if (iSequence.indexOf('A') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(44, 0.5, FragmentIon.IMMONIUM, 0, "iA"));
        }
        if (iSequence.indexOf('R') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(129, 0.5, FragmentIon.IMMONIUM, 0, "iR"));
        }
        if (iSequence.indexOf('N') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(87, 0.5, FragmentIon.IMMONIUM, 0, "iN"));
        }
        if (iSequence.indexOf('D') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(88, 0.5, FragmentIon.IMMONIUM, 0, "iD"));
        }
        if (iSequence.indexOf('C') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(76, 0.5, FragmentIon.IMMONIUM, 0, "iC"));
        }
        if (iSequence.indexOf('E') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(102, 0.5, FragmentIon.IMMONIUM, 0, "iE"));
        }
        if (iSequence.indexOf('Q') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(101, 0.5, FragmentIon.IMMONIUM, 0, "iQ"));
        }
        if (iSequence.indexOf('G') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(30, 0.5, FragmentIon.IMMONIUM, 0, "iG"));
        }
        if (iSequence.indexOf('H') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(110, 0.5, FragmentIon.IMMONIUM, 0, "iH"));
        }
        if (iSequence.indexOf('I') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(86, 0.5, FragmentIon.IMMONIUM, 0, "iI"));
        }
        if (iSequence.indexOf('L') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(86, 0.5, FragmentIon.IMMONIUM, 0, "iL"));
        }
        if (iSequence.indexOf('K') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(101, 0.5, FragmentIon.IMMONIUM, 0, "iK"));
        }
        if (iSequence.indexOf('M') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(104, 0.5, FragmentIon.IMMONIUM, 0, "iM"));
        }
        if (iSequence.indexOf('F') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(120, 0.5, FragmentIon.IMMONIUM, 0, "iF"));
        }
        if (iSequence.indexOf('P') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(70, 0.5, FragmentIon.IMMONIUM, 0, "iP"));
        }
        if (iSequence.indexOf('S') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(60, 0.5, FragmentIon.IMMONIUM, 0, "iS"));
        }
        if (iSequence.indexOf('T') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(74, 0.5, FragmentIon.IMMONIUM, 0, "iT"));
        }
        if (iSequence.indexOf('W') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(159, 0.5, FragmentIon.IMMONIUM, 0, "iW"));
        }
        if (iSequence.indexOf('Y') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(136, 0.5, FragmentIon.IMMONIUM, 0, "iY"));
        }
        if (iSequence.indexOf('V') != -1) {
            lPrecAndImmoIons.add(new FragmentIonImpl(72, 0.5, FragmentIon.IMMONIUM, 0, "iV"));
        }

        return lPrecAndImmoIons;
    }


    /**
     * This method returns all theoretical fragmentions:<br><i>  b | b++ | b(NH3) | b(NH3)++ | b(H2O) | b(H2O)++ | y |
     * y++ | y(NH3) | y(NH3)++ | y(H2O) | y(H2O)++</i> <br> It serves as input for the different matching methods.
     *
     * @return Vector with all the theoretical fragmentions that should be checked for a match in the spectrum.
     */
    public Vector getAllTheoreticalFragmentions() {
        return getTheoreticalFragmentions(iIonSeriesRules);
    }

    /**
     * This method returns all significant fragmentions that were requested in the int array. <br> It serves as input
     * for the different matching methods.
     *
     * @param aIonTypes int[] wherein the ions you want in return can be retrieved. use the static integers on the
     *                  FragmentIon interface!<ul> <li>0  - a Series <li>1  place holder <li>2  - a++ Series
     *                  <p/>
     *                  <li>3  - b Series <li>4  place holder <li>5  - b++ Series
     *                  <p/>
     *                  <li>6  - y Series <li>7  place holder <li>8  - y++ Series
     *                  <p/>
     *                  <li>9  c <li>10 c++ <li>11 x <li>12 x++ <li>13 z <li>14 z++</ul>
     * @return Vector with all the theoretical fragmentions that should be checked for a match in the spectrum.
     */
    public Vector getTheoreticalFragmentions(int[] aIonTypes) {
        Vector lAllIons = new Vector();
        for (int i = 0; i < aIonTypes.length; i++) {
            int index = aIonTypes[i];
            FragmentIonImpl[] fma = getFragmentIons(index);
            for (int j = 0; j < fma.length; j++) {
                lAllIons.add(fma[j]);
            }
        }
        return lAllIons;
    }

    /**
     * This method calculates ioncoverage of a PeptideSequence and matched fragmentions.
     *
     * @param aPeaks             Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was
     *                           created.
     * @param aNumberOfPeaksUsed This variable holds the number of ions that mascot used to check witch of the
     *                           theoretical ions matched in the spectrum. In practice, anly the first
     *                           <aNumberOfPeaksUsed> Peaks from <aPeaks> will be used to do the check. This parameter
     *                           is returned by the getPeaksUsedFromIons1() method in PeptideHit.
     * @return int[] with the distinct number fragmentions that covered the sequence. <br /> <b>[0]</b> number of b-ions
     *         covering the peptide's sequence.<br /> <b>[1]</b> number of y-ions covering the peptide's sequence.<br />
     *         <b>[2]</b> number of b- and -ions covering the peptide's sequence.
     */
    public int[] getMascotIonCoverage(Peak[] aPeaks, int aNumberOfPeaksUsed) {
        boolean work = false;
        if ((iPeaks == null) || (iMascotCoverage == null)) {
            work = true;
        } else if (iPeaks != aPeaks) {
            work = true;
        }

        if (work) {
            iPeaks = aPeaks;
            Vector lFragmentIons = getMatchedIonsByMascot(iPeaks, aNumberOfPeaksUsed);
            iMascotCoverage = calculateCoverage(lFragmentIons);
        }

        return iMascotCoverage;
    }

    /**
     * This method calculates ioncoverage of a PeptideSequence and matched fragmentions.
     *
     * @param aFragmentIons - A Vector with fragmentions that were matched.
     * @return int[] with the distinct number fragmentions that covered the sequence. <br /> <b>[0]</b> number of b-ions
     *         covering the peptide's sequence.<br /> <b>[1]</b> number of y-ions covering the peptide's sequence.<br />
     *         <b>[2]</b> number of b- and -ions covering the peptide's sequence.
     */
    private int[] calculateCoverage(Vector aFragmentIons) {
        /**
         * This int array will return the distinct number of fragmentions.
         * [0] - b
         * [1] - y
         * [2] - all
         */
        int lLength = iSequence.length();

        int[] lCoverage = new int[3];
        int[] lB = new int[lLength - 1];
        int[] lY = new int[lLength - 1];
        int[] lAll = new int[lLength];
        Iterator iterator = aFragmentIons.iterator();
        while (iterator.hasNext()) {
            FragmentIon fm = (FragmentIon) iterator.next();
            int lNumber = fm.getNumber() - 1;

            if (0 < fm.getID() && fm.getID() < 7) {
                //b-ion and its associated ions.
                if (lB[lNumber] == 0) {
                    lB[lNumber] = 1;
                }
                if (lAll[lNumber] == 0) {
                    lAll[lNumber] = 1;
                }
            } else if (12 < fm.getID() && fm.getID() < 19) {
                //a-ion and its associated ions.(b-derivated!)
                if (lB[lNumber] == 0) {
                    lB[lNumber] = 1;
                }
                if (lAll[lNumber] == 0) {
                    lAll[lNumber] = 1;
                }
            } else if (6 < fm.getID() && fm.getID() < 13) {
                //y-ion and its associated ions.
                if (lY[lNumber] == 0) {
                    lY[lNumber] = 1;
                }
                if (lAll[lLength - (lNumber + 1)] == 0) {
                    lAll[lLength - (lNumber + 1)] = 1;
                }
            } else if (20 < fm.getID() && fm.getID() < 23) {
                //c and c++
                if (lB[lNumber] == 0) {
                    lB[lNumber] = 1;
                }
                if (lAll[lNumber] == 0) {
                    lAll[lNumber] = 1;
                }
            } else if (18 < fm.getID() && fm.getID() < 21) {
                //x and x++
                if (lY[lNumber] == 0) {
                    lY[lNumber] = 1;
                }
                if (lAll[lLength - (lNumber + 1)] == 0) {
                    lAll[lLength - (lNumber + 1)] = 1;
                }
            } else if (22 < fm.getID() && fm.getID() < 25) {
                //z and z++
                if (lY[lNumber] == 0) {
                    lY[lNumber] = 1;
                }
                if (lAll[lLength - (lNumber + 1)] == 0) {
                    lAll[lLength - (lNumber + 1)] = 1;
                }
            }
        }
        lCoverage[0] = sumIntArray(lB);
        lCoverage[1] = sumIntArray(lY);
        lCoverage[2] = sumIntArray(lAll);
        return lCoverage;
    }

    private int sumIntArray(int[] aCoverage) {
        int lCount = 0;
        for (int i = 0; i < aCoverage.length; i++) {
            lCount = lCount + aCoverage[i];
        }
        return lCount;
    }

    /**
     * Returns a Vector with only b or y ions that where found in the PeakList.<br>
     *
     * @param aPeaks Peak[] containing all the peak masses of the Query wherefrom this PeptideHit was created.
     * @return Vector with b or y Fragmentions that where found in the PeakList.
     */
    public Vector getMatchedBYions(Peak[] aPeaks) {
        resetPreviousMatching();
        // Put the instance B and Y ions in a Vector to pass to the match method.
        Vector lBYions = new Vector();
        for (int i = 0; i < iBions.length; i++) {
            FragmentIonImpl lBion = iBions[i];
            lBion.setImportance(2);
            lBYions.add(lBion);
        }
        for (int i = 0; i < iYions.length; i++) {
            FragmentIonImpl lYion = iYions[i];
            lYion.setImportance(2);
            lBYions.add(lYion);
        }
        return getMatchedIons(lBYions, aPeaks);
    }

// --------------------------- main() method ---------------------------

    // Test && demo ---------------------------------------------------------------------------------------------//

    public static void main(String[] args) {
        MascotDatfile mdf = new MascotDatfile("/Users/kenny/Proteomics/Projects/1002/0210_mascotdatfile_mod_error/F046001.dat");
        QueryToPeptideMap lQuery2P = mdf.getQueryToPeptideMap();
        PeptideHit ph = lQuery2P.getPeptideHitOfOneQuery(218, 1);
        PeptideHitAnnotation lPha = new PeptideHitAnnotation(ph.getSequence(), ph.getModifications(), mdf.getMasses(), mdf.getParametersSection(), ph.getIonSeriesFound());
        lPha.printIonSeries();
        PeptideToQueryMap lPeptide2Q = mdf.getPeptideToQueryMap();
        Vector Queries = (Vector) lPeptide2Q.getQueriesByModifiedSequence(ph.getModifiedSequence());
        for (Iterator lIterator = Queries.iterator(); lIterator.hasNext();) {
            Query q = (Query) lIterator.next();
            System.out.println("First: check for matches by intensityThreshold.");
            Vector lIntensityMatchedIons = lPha.getSignificantMatchedIonsAboveIntensityThreshold(q.getPeakList(), q.getMaxIntensity(), 0.1);
            for (int i = 0; i < lIntensityMatchedIons.size(); i++) {
                FragmentIon fm = (FragmentIon) lIntensityMatchedIons.get(i);
                System.out.println(fm.getType() + fm.getNumber() + " was matched in the mass spectrum with a mass error of " + fm.getTheoreticalExperimantalMassError());
            }
            System.out.println("Second: check for Mascot Matches.");
            Vector lMascotMatchedIons = lPha.getMatchedIonsByMascot(q.getPeakList(), ph.getPeaksUsedFromIons1());
            for (int i = 0; i < lMascotMatchedIons.size(); i++) {
                FragmentIon fm = (FragmentIon) lMascotMatchedIons.get(i);
                System.out.println(fm.getType() + fm.getNumber() + " was matched in the mass spectrum with a mass error of " + fm.getTheoreticalExperimantalMassError());
            }
            System.out.println("Finally: Get the fused matches!");
            Vector lFusedMatchedIons = lPha.getFusedMatchedIons(q.getPeakList(), ph.getPeaksUsedFromIons1(), q.getMaxIntensity(), 0.1);
            for (int i = 0; i < lFusedMatchedIons.size(); i++) {
                FragmentIon fm = (FragmentIon) lFusedMatchedIons.get(i);
                System.out.println(fm.getType() + fm.getNumber() + " was matched in the mass spectrum with a mass error of " + fm.getTheoreticalExperimantalMassError());
            }
        }
    }

    /**
     * This method prints the theoretical b- and -y ion series to the standard output.
     */
    public void printIonSeries() {
        System.out.println("The theoretical b- and y-ion series of :" + iSequence + "\n\n");
        for (int i = 0; i < iYions.length; i++) {
            System.out.println(iBions[i].getLabel() + "\t" + iBions[i].getMZ() + "\t||\t" + iYions[i].getLabel() + "\t" + iYions[i].getMZ());
        }
    }
}
