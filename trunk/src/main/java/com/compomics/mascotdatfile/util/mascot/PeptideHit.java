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
import com.compomics.mascotdatfile.util.interfaces.Modification;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This Class contains all the parsed data from a datfile peptidehit
 */
public class PeptideHit implements Serializable {
    // Class specific log4j logger for PeptideHit instances.
    private static Logger logger = Logger.getLogger(PeptideHit.class);
    /**
     * a soft type for the number of missed cleavages
     */
    public static final Integer MISSED_CLEAVAGES = new Integer(1);
    /**
     * a soft type for the peptide mass
     */
    public static final Integer PEPTIDE_MASS = new Integer(2);
    /**
     * a soft type for the theoretical-experimental mass error
     */
    public static final Integer DELTA_MASS = new Integer(3);
    /**
     * a soft type for the number of ions that where matched
     */
    public static final Integer NUMBER_OF_IONS_MATCHED = new Integer(4);
    /**
     * a soft type for the peptide's sequence
     */
    public static final Integer SEQUENCE = new Integer(5);
    /**
     * a soft type for the peptide's modified sequence
     */
    public static final Integer MODIFIED_SEQUENCE = new Integer(6);
    /**
     * a soft type for the number of peaks that where used for the identification
     */
    public static final Integer PEAKS_USED_FROM_IONS = new Integer(7);
    /**
     * a soft type for the identification's ionsscore
     */
    public static final Integer IONS_SCORE = new Integer(8);
    /**
     * a soft type for the threshold that must be surpassed by the ionsscore to be a confident identification
     */
    public static final Integer THRESHOLD = new Integer(9);
    /**
     * This String presents the peptide part of the peptidehitString
     */
    private String iPeptideHit_pep = null;
    /**
     * This String presents the protein part of the peptidehitString
     */
    private String iPeptideHit_prot = null;
    /**
     * This int presents the total of missed cleavages
     */
    private int iMissedCleavages = 0;
    /**
     * This double presents the peptide mass
     */
    private double iPeptideMr = 0;
    /**
     * This double presents the mass error (Expected - Observed)
     */
    private double iDeltaMass = 0;
    /**
     * This int presents the number of ions that where matched
     */
    private int iNumberOfIonsMatched = 0;
    /**
     * This String presents the peptide sequence
     */
    private String iSequence = null;
    /**
     * This int presents the peaks used from ions1
     */
    private int iPeaksUsedFromIons1 = 0;
    /**
     * This String presents the String with variable modifications
     */
    private int[] iVariableModificationsArray = null;
    /**
     * This String holds the Ionscore
     */
    private double iIonsScore = 0;
    /**
     * This int[] holds the Ion series that have been found
     */
    private int[] iIonSeriesFound = null;
    /**
     * This int presents the peaks used from ions2
     */
    private int iPeaksUsedFromIons2 = 0;
    /**
     * This int presents the peaks used from ions3
     */
    private int iPeaksUsedFromIons3 = 0;
    /**
     * The ArrayList is filled with ProteinHit instances. Each containing information about the (possible) origin(s) of
     * this PeptideHit instances.
     */
    private ArrayList iProteinHits = new ArrayList(1);
    /**
     * The array holds Fixed And Variable Modifications of the peptidehit. If the array value is null; there is no
     * modification at that residue. The array is build as follows: N-term[0] * aminoacids[1]-[n-1] * C-term[n].
     */
    private Modification[] iModifications = null;
    /**
     * The modified sequence notation of the peptidehit.
     */
    private String iModifiedSequence = null;
    /**
     * The modified sequence in different components according the primary sequence (5 components for peptide KENNY).
     */
    private String[] iModifiedSequenceComponents = null;
    /**
     * The identity treshold, when the peptideHitScore is above this treshold it will tagged as an identification.
     */
    private double iQueryIdentityThreshold = 0;
    /**
     * The homology treshold.
     */
    private double iHomologyThreshold = 0;
    /**
     * This instance of PeptideHitAnnotation calculates the b- and y-ion series of the PeptideHit. The instance is a
     * lazy cache.
     */
    private PeptideHitAnnotation iPha = null;

    /**
     * This boolean indicates whether this peptidehit origined from an errortolerant search.
     */
    private boolean boolErrorTolerantHit = false;


    /**
     * This Constructor sets up the object that contains all the datfile peptide section data in multiple variables.
     * This private constructor is being called by a static variable in this class.
     *
     * @param aPeptideHit       is a String that comes right from the .dat file
     * @param aModificationList is an instance of ModificationList containing all the possible Modifications of this
     *                          Mascot Search.
     * @param aThreshold        contains two doubles - qmatch and qplughole, source for the identity and homology
     *                          threshold.
     */
    private PeptideHit(String aPeptideHit, ProteinMap aProteinMap, ModificationList aModificationList, double[] aThreshold) {
    this(aPeptideHit,aProteinMap,aModificationList,aThreshold,null);
    }

    private PeptideHit(String aPeptideHit, ProteinMap aProteinMap, ModificationList aModificationList, double[] aThreshold,String substitutions) {
            splitPeptideHit(aPeptideHit);
            parseDatFilePeptideString();
            if (substitutions != null){
                substitutePeptides(substitutions);
            }
            parseDatFileProteinString();
            parseThresholds(aThreshold);
            parsePeptideHitModifications(aModificationList);

    }

    private void substitutePeptides(String substitutions) {

        String[] substitutionArray = substitutions.split(",");
        StringBuilder replaceAminoAcids = new StringBuilder(iSequence);
        //TODO in case of more than one variable amino acid being implemented --> while (i =0;i<substitutionArray.size;i+3) {        replaceAminoAcids.setCharAt(Integer.parseInt(substitutionArray[i]) - 1,substitutionArray[i+2].charAt(0));}
        replaceAminoAcids.setCharAt(Integer.parseInt(substitutionArray[0]) - 1,substitutionArray[2].charAt(0));
        iSequence = replaceAminoAcids.toString();
    }

    /**
     * This is a static method that takes a peptideHit String and returns an PeptideHit object with all the parsed
     * data. Returns a PeptideHit object that contains all the parsed data of the aPeptideHit String. Can be "null" if no
     * peptide match found.
     *
     * @param aETSPeptideHitString is a String with the unparsed data of the peptidehit.
     * @param aETSPeptideHitMods   is a String with the Error Tolerant Search modification in case.
     * @param aProteinMap          is a structured version of the ProteinSection.
     * @param aModificationList    is an instance of ModificationList containing all the possible Modifications of this
     *                             Mascot Search.
     * @param aThreshold           contains two doubles - qmatch and qplughole, source for the identity and homology
     *                             threshold.
     */
    public PeptideHit(final String aETSPeptideHitString, final String aETSPeptideHitMods, final ProteinMap aProteinMap, final ModificationList aModificationList, final double[] aThreshold) {
        splitPeptideHit(aETSPeptideHitString);
        parseDatFilePeptideString();
        parseDatFileProteinString();
        parseThresholds(aThreshold);
        parsePeptideHitModifications(aModificationList, aETSPeptideHitMods);
        setErrorTolerantHit(true);
    }

    /**
     * This is a static method that takes a peptideHit String and returns an PeptideHit object with all the parsed
     * data.
     *
     * @param aPeptideHit       is a String with the unparsed data of the peptidehit.
     * @param aProteinMap       is a structured version of the ProteinSection.
     * @param aModificationList is an instance of ModificationList containing all the possible Modifications of this
     *                          Mascot Search.
     * @param aThreshold        contains two doubles - qmatch and qplughole, source for the identity and homology
     *                          threshold.
     * @return an PeptideHit object that contains all the parsed data of the aPeptideHit String can be "null" if no
     *         peptide match found
     */
    public static PeptideHit parsePeptideHit(String aPeptideHit, ProteinMap aProteinMap, ModificationList aModificationList, double[] aThreshold) {
        PeptideHit ph = null;
        if (!aPeptideHit.equals("-1")) {
            ph = new PeptideHit(aPeptideHit, aProteinMap, aModificationList, aThreshold);
        }
        return ph;
    }

    public static PeptideHit parsePeptideHit(String aPeptideHit, ProteinMap aProteinMap, ModificationList aModificationList, double[] aThreshold,String substitution) {
        PeptideHit ph = null;
        if (!aPeptideHit.equals("-1")) {
            ph = new PeptideHit(aPeptideHit, aProteinMap, aModificationList, aThreshold,substitution);
        }
        return ph;
    }

    /**
     * This is a static method that takes a peptideHit String and returns an PeptideHit object with all the parsed
     * data.
     *
     * @param aETSPeptideHitString is a String with the unparsed data of the peptidehit.
     * @param aETSPeptideHitMods   is a String with the Error Tolerant Search modification in case.
     * @param aProteinMap          is a structured version of the ProteinSection.
     * @param aModificationList    is an instance of ModificationList containing all the possible Modifications of this
     *                             Mascot Search.
     * @param aThreshold           contains two doubles - qmatch and qplughole, source for the identity and homology
     *                             threshold.
     * @return an PeptideHit object that contains all the parsed data of the aPeptideHit String can be "null" if no
     *         peptide match found
     */
    public static PeptideHit parsePeptideHit(final String aETSPeptideHitString, final String aETSPeptideHitMods, final ProteinMap aProteinMap, final ModificationList aModificationList, final double[] aThreshold) {
        PeptideHit ph = null;
        if (!aETSPeptideHitString.equals("-1")) {
            ph = new PeptideHit(aETSPeptideHitString, aETSPeptideHitMods, aProteinMap, aModificationList, aThreshold);
        }
        return ph;
    }

    /**
     * This method splits the aPeptideHit into a string iPeptideHit_pep with peptide data and a String iPeptideHit_prot
     * with protein data.
     *
     * @param aPeptideHit
     */
    private void splitPeptideHit(String aPeptideHit) {
        StringTokenizer st = new StringTokenizer(aPeptideHit, ";");
        if (st.countTokens() != 2) {
            System.out.println("Warning, more than two ';' semicolons used in the peptidehit String!!\t" + aPeptideHit);
        }
        int lSplitIndex = aPeptideHit.indexOf(';');
        //OK, we know we have 2 tokens, read them now.
        iPeptideHit_pep = aPeptideHit.substring(0, lSplitIndex);
        iPeptideHit_prot = aPeptideHit.substring(lSplitIndex + 1);
    }

    /**
     * This method parses the String iPeptideHit_pep into peptide related variables of this object.
     */
    private void parseDatFilePeptideString() {
        StringTokenizer st = new StringTokenizer(iPeptideHit_pep, ",");
        if (st.countTokens() != 11) {
            throw new MascotDatfileException("Wrong String with input data to set variable values (found " +
                    st.countTokens() + " tokens instead of expected 11).");
        }
        // OK, now we know we have 11 tokens, read them all.
        setMissedCleavages(Integer.parseInt(st.nextToken()));
        setPeptideMr(Double.parseDouble(st.nextToken()));
        setDeltaMass(Double.parseDouble(st.nextToken()));
        setNumberOfIonsMatched(Integer.parseInt(st.nextToken()));
        setSequence(st.nextToken());
        setPeaksUsedFromIons1(Integer.parseInt(st.nextToken()));
        setVariableModificationsArray(st.nextToken());
        setIonsScore(Double.parseDouble(st.nextToken()));
        parseIonSeries(st.nextToken());
        setPeaksUsedFromIons2(Integer.parseInt(st.nextToken()));
        setPeaksUsedFromIons3(Integer.parseInt(st.nextToken()));
    }

    private void parseThresholds(double[] aThreshold) {
        iHomologyThreshold = aThreshold[0];
        iQueryIdentityThreshold = aThreshold[1];
    }

    /**
     * This method parses the String iPeptideHit_prot into protein related variable of this object.
     * Multiple proteinHits separated by -"- or -,- characters are split into Tokens.
     * New ProteinHit instances are then created and added into iProteinHits ArrayList.
     */
    private void parseDatFileProteinString() {
        if (iPeptideHit_prot.indexOf(",") == -1) {
            iProteinHits.add(new ProteinHit(iPeptideHit_prot));
        } else {
            // Multiple parent proteins can be separted by -,- characters or -"- charachters,
            // the split method thereby cuts the proteinhits String into distinct parent protein entities.
            String[] proArr = iPeptideHit_prot.split(",\"");
            for (int i = 0; i < proArr.length; i++) {
                if (i == 0) {
                    iProteinHits.add(new ProteinHit(proArr[i]));
                } else {
                    iProteinHits.add(new ProteinHit("\"" + proArr[i]));
                }
            }
        }
    }

    /**
     * This method returns the amount of missed cleavages
     *
     * @return the amount of missed cleavages
     */
    public int getMissedCleavages() {
        return iMissedCleavages;
    }

    /**
     * This method sets the missed cleavages.
     *
     * @param aMissedCleavages int with the new value of missed cleavages
     */
    public void setMissedCleavages(int aMissedCleavages) {
        iMissedCleavages = aMissedCleavages;
    }

    /**
     * This method gets the calculated peptide mass.
     *
     * @return double peptide mass
     */
    public double getPeptideMr() {
        return iPeptideMr;
    }

    /**
     * This method set's the peptide mass
     *
     * @param aPeptideMr
     */
    public void setPeptideMr(double aPeptideMr) {
        iPeptideMr = aPeptideMr;
    }

    /**
     * This method gets the experimental-theoretical peptide mass deviation.
     *
     * @return double iDeltaMass
     */
    public double getDeltaMass() {
        return iDeltaMass;
    }

    /**
     * This method sets the delta mass
     *
     * @param aDeltaMass
     */
    public void setDeltaMass(double aDeltaMass) {
        iDeltaMass = aDeltaMass;
    }

    /**
     * Set the errortolerant search origin for this peptidehit.
     *
     * @param aErrorTolerantHit
     */
    public void setErrorTolerantHit(final boolean aErrorTolerantHit) {
        boolErrorTolerantHit = aErrorTolerantHit;
    }

    /**
     * This method gets the number of matched ions
     *
     * @return int number of matched ions
     */
    public int getNumberOfIonsMatched() {
        return iNumberOfIonsMatched;
    }

    /**
     * This method sets the number of matched ions
     *
     * @param aNumberOfIonsMatched
     */
    public void setNumberOfIonsMatched(int aNumberOfIonsMatched) {
        iNumberOfIonsMatched = aNumberOfIonsMatched;
    }

    /**
     * This method gets the sequence of the peptide
     *
     * @return String with the sequence of the peptidehit
     */
    public String getSequence() {
        return iSequence;
    }

    /**
     * This method sets the peptide sequence
     *
     * @param aSequence
     */
    public void setSequence(String aSequence) {
        iSequence = aSequence;
    }

    /**
     * This method gets an int with the total number of used peaks from ions1
     *
     * @return int with the total number of used peaks from ions1
     */
    public int getPeaksUsedFromIons1() {
        return iPeaksUsedFromIons1;
    }

    /**
     * This method sets the total number of peaks that were used from ions1
     *
     * @param aPeaksUsedFromIons1
     */
    public void setPeaksUsedFromIons1(int aPeaksUsedFromIons1) {
        iPeaksUsedFromIons1 = aPeaksUsedFromIons1;
    }


    /*
    * Returns the ETS search origin of this peptidehit.
    */

    public boolean isErrorTolerantHit() {
        return boolErrorTolerantHit;
    }

    /**
     * This method gets a coded String with modifications on the sequence
     *
     * @return int[] with modification (coded)
     */
    public int[] getVariableModificationsArray() {
        return iVariableModificationsArray;
    }

    /**
     * This method sets the modifications on the peptidehit by a coded String
     *
     * @param aVariableModificationsArray
     */
    public void setVariableModificationsArray(int[] aVariableModificationsArray) {
        iVariableModificationsArray = aVariableModificationsArray;
    }

    /**
     * method parses the coded variablemodificationString into an int[]
     *
     * @param aVariableModificationString
     */
    public void setVariableModificationsArray(String aVariableModificationString) {
        //initialise the int[]
        iVariableModificationsArray = new int[aVariableModificationString.length()];
        //for loop to fill the int[]
        for (int i = 0; i < aVariableModificationString.length(); i++) {
            char c = aVariableModificationString.charAt(i);
            if (c != 'X') {
                iVariableModificationsArray[i] = Integer.parseInt("" + c, 16);
            } else {
                iVariableModificationsArray[i] = c;
            }
        }
    }

    /**
     * This gets a double with the ionscore of the peptidehit
     *
     * @return int with ionsscore
     */
    public double getIonsScore() {
        return iIonsScore;
    }

    /**
     * This sets the ionsscore of the peptidehit
     *
     * @param aIonsScore
     */
    public void setIonsScore(double aIonsScore) {
        iIonsScore = aIonsScore;
    }

    /**
     * This gets a coded String with found ionseries
     *
     * @return String with found ionseries
     */
    public int[] getIonSeriesFound() {
        return iIonSeriesFound;
    }

    /**
     * This sets a the ionseries
     *
     * @param aIonSeriesFound
     */
    public void setIonSeriesFound(int[] aIonSeriesFound) {
        iIonSeriesFound = aIonSeriesFound;
    }

    /**
     * This gets the peaks used from ions2
     *
     * @return int with the amount of peaks used from ions2
     */
    public int getPeaksUsedFromIons2() {
        return iPeaksUsedFromIons2;
    }

    /**
     * This sets how many peaks there were used from ions2
     *
     * @param aPeaksUsedFromIons2
     */
    public void setPeaksUsedFromIons2(int aPeaksUsedFromIons2) {
        iPeaksUsedFromIons2 = aPeaksUsedFromIons2;
    }

    /**
     * This gets the peaks used from ions3
     *
     * @return int with the amount of peaks used from ions3
     */
    public int getPeaksUsedFromIons3() {
        return iPeaksUsedFromIons3;
    }

    /**
     * This sets how many peaks there were used from ions3
     *
     * @param aPeaksUsedFromIons3
     */
    public void setPeaksUsedFromIons3(int aPeaksUsedFromIons3) {
        iPeaksUsedFromIons3 = aPeaksUsedFromIons3;
    }

    /**
     * Returns the ArrayList is filled with ProteinHit instances. Each containing information about the (possible)
     * origin(s) of this PeptideHit instances.
     *
     * @return the ArrayList is filled with ProteinHit instances. Each containing information about the (possible)
     *         origin(s) of this PeptideHit instances.
     */
    public ArrayList getProteinHits() {
        return iProteinHits;
    }

    /**
     * Sets the ArrayList is filled with ProteinHit instances. Each containing information about the (possible)
     * origin(s) of this PeptideHit instances.
     *
     * @param aProteinHits the ArrayList is filled with ProteinHit instances. Each containing information about the
     *                     (possible) origin(s) of this PeptideHit instances.
     */
    public void setProteinHits(ArrayList aProteinHits) {
        iProteinHits = aProteinHits;
    }

    /**
     * This method returns the number of (parametrical specified) aminoacids this PeptideHit has.
     *
     * @param aAminoAcid The aminoAcid that we want to count.
     * @return returns an int with the number of aminoacids found in the sequence.
     */
    public int getNumberOfAminoAcid(char aAminoAcid) {
        int lCount = 0;
        for (int i = 0; i < iSequence.length(); i++) {
            if (iSequence.charAt(i) == aAminoAcid) {
                lCount++;
            }
        }
        return lCount;
    }


    /**
     * Method Parse the MascotDatfile_Modifications instance into relevant modifications of this peptidehit into the
     * Modification[] iModifications. Process: 1.Put all the fixed mods in the Modification[], 2.Put all the variable
     * mods in the Modification[], this is done by using this instance's VariableModificationsString. (   if a fixed and
     * variable mod are both possible (ex: Fixed K-acetylation & Variable K-biotinylation, 1.The Fixed is set on every K
     * 2.The variable overwrites the fixed if the VariableModificationsString would say that the K has the variable mod!
     * )
     *
     * @param aMod    ModificationList instance with all fixed and variable modifications
     * @param aETSMod ErrorTolerantModification implementation on index 'X'.
     */
    public void parsePeptideHitModifications(ModificationList aMod, String aETSMod) {
        // Do the standard modification parsing.
        parsePeptideHitModifications(aMod);

        // Plus parse the errortolerant modification.
        for (int i = 0; i < iVariableModificationsArray.length; i++) {
            if (iVariableModificationsArray[i] == 'X') {

                // ETSPeptideHitMods example:
                // 13.031634,0.000000,Methylamine (T)
                double lMass = -1.0;
                double lNeutralLoss = -1.0;
                String lType = null;
                String lShortType = null;
                String lLocation = null;

                StringTokenizer st = new StringTokenizer(aETSMod, ",");
                int lCounter = -1;
                while (st.hasMoreTokens()) {
                    lCounter++;
                    String token = st.nextToken();
                    if (lCounter == 0) {
                        // Modification mass.
                        lMass = Double.parseDouble(token);
                    } else if (lCounter == 1) {
                        // Neutral loss.
                        lNeutralLoss = Double.parseDouble(token);
                    } else if (lCounter == 2) {
                        // The name
                        int indexStart = 0;
                        int indexStop = token.indexOf('(') - 1; // +1 for the leading space.
                        lType = token.substring(indexStart, indexStop);

                        // The short name.
                        lShortType = (String) ModificationConversion.getInstance().getConversionMap().get(token);
                        if (lShortType == null) {
                            lShortType = "ETS_" + lType;
                        } else {
                            lShortType = "**" + lShortType;
                        }

                        lType = "ETS_" + lType;

                        // The residue
                        indexStart = token.indexOf('(') + 1;
                        indexStop = token.indexOf(')');
                        lLocation = token.substring(indexStart, indexStop);
                    }
                }


                iModifications[i] = new ErrorTolerantModification(lType, lShortType, lMass, lNeutralLoss, lLocation, 'X');
            }
        }
    }

    /**
     * Method Parse the MascotDatfile_Modifications instance into relevant modifications of this peptidehit into the
     * Modification[] iModifications. Process: 1.Put all the fixed mods in the Modification[], 2.Put all the variable
     * mods in the Modification[], this is done by using this instance's VariableModificationsString. (   if a fixed and
     * variable mod are both possible (ex: Fixed K-acetylation & Variable K-biotinylation, 1.The Fixed is set on every K
     * 2.The variable overwrites the fixed if the VariableModificationsString would say that the K has the variable mod!
     * )
     *
     * @param aMod ModificationList instance with all fixed and variable modifications
     */
    public void parsePeptideHitModifications(ModificationList aMod) {
        //initiate Modification[] iModifications.
        iModifications = new Modification[iSequence.length() + 2];
        // These temporary Modification instances will be reused in the loops instead of using generics.
        FixedModification lTempF = null;
        VariableModification lTempV = null;

        //1.Parse the fixed mod's into the array.
        //1.a)Check N-terminal fixed modification.
        for (int i = 0; i < aMod.getFixedModifications().size(); i++) {
            // reset lTempF
            lTempF = null;
            lTempF = (FixedModification) aMod.getFixedModifications().get(i);
            //Select N-terminal mods.
            // 060705 Fixed a bug with N-terminal fixed modifications, their location is parsed by an '_' character, not with a '-' like in the
            if (lTempF.getLocation().startsWith("N_term")) {
                //Aselective N-terminal modification.
                if (lTempF.getLocation().length() == 6) {
                    //Assign N-terminal modification at iModifications[N-term]
                    iModifications[0] = lTempF;

                    //Selective N-terminal modification => get specificity.
                } else {
                    //The aminoacid specificity is the last character of the getLocation();
                    int index = lTempF.getLocation().length();
                    //The specificity of this modification is saved in char c.
                    char c = lTempF.getLocation().charAt(index - 1);
                    //If the N-terminal aminoacid is equal to char c, then assign the modification in the Modifications[0] as the N-terminal mod.
                    if (iSequence.charAt(0) == c) {
                        iModifications[0] = lTempF;
                    }
                }
            }
        }
        //1.b)Check C-terminal fixed modification.
        for (int i = 0; i < aMod.getFixedModifications().size(); i++) {
            // reset lTempF
            lTempF = null;
            lTempF = (FixedModification) aMod.getFixedModifications().get(i);
            //Select C-terminal mods.
            if (lTempF.getLocation().startsWith("C_term")) {
                ///Aselective C-terminal modification.
                if (lTempF.getLocation().length() == 6) {
                    //Assign the aselective mod to Modifications[C-term]
                    iModifications[iSequence.length() + 1] = lTempF;
                    //Selective C-terminal modification =>get specificity.
                } else {
                    //The aminoacid specificity is the last character of the getLocation();
                    int index = lTempF.getLocation().length();
                    //The specificity of this modification is saved in char c.
                    char c = lTempF.getLocation().charAt(index - 1);
                    //If the C-terminal aminoacid is equal to char c, then assign the modification in the Modifications[(iSequence.length)-1] as the C-terminal mod.
                }
            }
        }
        //1.c)Check internal modifications.
        //A_for-loop over the iSequence chars, representing a loop over all the aminoacids and check if they will have a fixed modification.
        for (int i = 0; i < iSequence.length(); i++) {
            //Local variable to hold the aminoacid we will check if it has a fixed modification.
            char lAminoAcid = iSequence.charAt(i);

            //B_Loop trough the Variable modifications.
            for (int j = 0; j < aMod.getFixedModifications().size(); j++) {
                // reset lTempF
                lTempF = null;
                lTempF = (FixedModification) aMod.getFixedModifications().get(j);
                // If the fixed mod is not C_term or N_term!!
                if (lTempF.getLocation().indexOf('_') == -1) {
                    //Create a char[] for possible aminoacids to be modified (ex: QN - Dam =>'char[] lModLocation = new char['Q','N'];')
                    char[] lModLocation = lTempF.getLocation().toCharArray();
                    //C_Loop trough the char[].
                    for (int k = 0; k < lModLocation.length; k++) {
                        if (lModLocation[k] == lAminoAcid) {
                            iModifications[i + 1] = lTempF;
                        }
                    }
                }
            }
        }
        //2.//Parse the fixed mod's into the array and overwrite where necessairy.
        for (int i = 0; i < (iSequence.length() + 2); i++) {
            //check if there is a modification at index i in the 'VariableModificationsString".
            if (iVariableModificationsArray[i] != 0) {
                // Assign the variable modification trough this method() of the ModificationList instance.
                iModifications[i] = aMod.getVariableModificationByModificationID(iVariableModificationsArray[i]);
            }
        }
    }


    /**
     * Returns the array holds Fixed And Variable Modifications of the peptidehit. If the array value is null; there is
     * no modification at that residue. The array is build as follows: N-term[0] * aminoacids[1]-[n-1] * C-term[n].
     *
     * @return the array holds Fixed And Variable Modifications of the peptidehit. If the array value is null; there is
     *         no modification at that residue.
     */
    public Modification[] getModifications() {
        return iModifications;
    }

    /**
     * Returns the modified sequence notation of the peptidehit.
     *
     * @return the modified sequence notation of the peptidehit.
     */
    public String getModifiedSequence() {
        if (iModifiedSequence == null) {
            if (iModifiedSequenceComponents == null) {
                // Call & create components if not done before.
                getModifiedSequenceComponents();
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < iModifiedSequenceComponents.length; i++) {
                String lModifiedSequenceComponent = iModifiedSequenceComponents[i];
                sb.append(lModifiedSequenceComponent);
            }
            iModifiedSequence = sb.toString();
        }
        return iModifiedSequence;
    }

    /**
     * Returns the modified sequence of the peptidehit in a String[]. Example: The peptide Ace-K<AceD3>ENNYR-COOH will
     * return a String[] with [0]Ace-K<AceD3> [1]E [2]N [3]N [4]Y [5]R-COOH
     *
     * @return String[] with the modified sequence in different components according the primary sequence.
     */
    public String[] getModifiedSequenceComponents() {
        if (iModifiedSequenceComponents == null) {
            iModifiedSequenceComponents = new String[iSequence.length()];
            StringBuffer sb = new StringBuffer();
            //Check N-term modification.
            // Note that 'pyro-glu' and 'pyro-cmc' are NOT annotated at the N-terminus but rather at
            // the N-terminal Q or C residue.
            boolean pyro = false;
            if (iModifications[0] != null && iModifications[0].getShortType().indexOf("Py") != 0) {
                sb.append(iModifications[0].getShortType());
                if (iModifications[0].isFixed()) {
                    sb.append("*");
                }
            } else if (iModifications[0] != null && iModifications[0].getShortType().indexOf("Py") == 0) {
                pyro = true;
                sb.append("NH2");
            } else {
                sb.append("NH2");
            }
            sb.append("-");
            //Walk trough internal modifications.
            for (int i = 0; i < iSequence.length(); i++) {
                sb.append(iSequence.charAt(i));
                if (i == 0 && pyro) {
                    StringBuffer sbPyr = new StringBuffer("<" + iModifications[0].getShortType());
                    if (iModifications[0].isFixed()) {
                        sbPyr.append("*");
                    }
                    if (iModifications[i + 1] != null) {
                        sbPyr.append("," + iModifications[i + 1].getShortType());
                        if (iModifications[i + 1].isFixed()) {
                            sbPyr.append("*");
                        }
                    }
                    sbPyr.append(">");
                    sb.append(sbPyr);
                } else if (iModifications[i + 1] != null) {
                    sb.append("<" + iModifications[i + 1].getShortType());
                    if (iModifications[i + 1].isFixed()) {
                        sb.append("*");
                    }
                    sb.append(">");
                }
                // By end of the for loop, a new component is complete. Put it in the iModifiedSequenceComponents array.
                // All but the last component must be inserted here.
                if (i < iSequence.length() - 1) {
                    iModifiedSequenceComponents[i] = sb.toString();
                    // Reset the StringBuffer.
                    sb.delete(0, sb.length());
                }
            }
            // Check C-term modification.
            sb.append("-");
            if (iModifications[iSequence.length() + 1] != null) {
                sb.append(iModifications[iSequence.length() + 1].getShortType());
                if (iModifications[iSequence.length() + 1].isFixed()) {
                    sb.append("*");
                }
            } else {
                sb.append("COOH");
            }
            // Insert the last component
            iModifiedSequenceComponents[iModifiedSequenceComponents.length - 1] = sb.toString();
        }
        return iModifiedSequenceComponents;
    }

    /**
     * method       test if the PeptideHit's score is above the homology threshold.
     *
     * @return boolean     true if the this PeptideHit's score is above the homology threshold.
     */
    public boolean scoresAboveHomologyThreshold() {
        return (iIonsScore >= iHomologyThreshold);
    }

    /**
     * method       test if the PeptideHit's score is above the identity threshold. The threshold score is calculated
     * with the default Confidence interval with alpha = 0.05
     *
     * @return boolean     true if this PeptideHit's score is above the threshold threshold.
     */
    public boolean scoresAboveIdentityThreshold() {
        return scoresAboveIdentityThreshold(0.05);
    }

    /**
     * method       test if the PeptideHit's score is above the identity threshold. The threshold score is calculated
     * with the Confidence interval alpha as a parameter.
     *
     * @param aConfidenceInterval Confidence interval with alpha = aConfidenceInterval
     * @return boolean     true if this PeptideHit's score is above the threshold threshold.
     */
    public boolean scoresAboveIdentityThreshold(double aConfidenceInterval) {
        double lThreshold = calculateIdentityThreshold(aConfidenceInterval);
        return (iIonsScore >= lThreshold);
    }

    /**
     * method       test if the PeptideHit's score is above the changed identity threshold. The threshold score is
     * calculated with the default Confidence interval with alpha = 0.05
     *
     * @param aDelta delta is an int that can be positive or negative. It will be added to the IdentityThreshold to make
     *               it higher or lower.
     * @return boolean     true if this PeptideHit's score is above the threshold threshold.
     */
    public boolean scoresAboveIdentityThreshold(int aDelta) {
        return scoresAboveIdentityThreshold(0.05, aDelta);
    }

    /**
     * method       test if the PeptideHit's score is above the changed identity threshold. The threshold score is
     * calculated with the Confidence interval alpha as a parameter.
     *
     * @param aDelta              delta is an int that can be positive or negative. It will be added to the
     *                            IdentityThreshold to make it higher or lower.
     * @param aConfidenceInterval Confidence interval with alpha = aConfidenceInterval
     * @return boolean     true if this PeptideHit's score is above the threshold threshold.
     */
    public boolean scoresAboveIdentityThreshold(double aConfidenceInterval, int aDelta) {
        double lThreshold = calculateIdentityThreshold(aConfidenceInterval);
        return (iIonsScore >= (lThreshold + aDelta));
    }

    /**
     * method to get the expectance at default confidence alpha = 0.05. <br> Expectancy is the number of times you could
     * expect to get this score or better by chance.
     *
     * @return double Expectancy
     */
    public double getExpectancy() {
        return getExpectancy(0.05);
    }


    /**
     * Returns Homology threshold (QPlughole value from .dat file).
     *
     * @return homology threshold
     */
    public double getHomologyThreshold() {
        return iHomologyThreshold;
    }

    /**
     * method to get the expectance at confidence alpha = parameter. <br> Expectancy is the number of times you could
     * expect to get this score or better by chance.
     *
     * @param aConfidenceInterval the confidence interval
     * @return double Expectancy
     */
    public double getExpectancy(double aConfidenceInterval) {
        double lThreshold = calculateIdentityThreshold(aConfidenceInterval);
        return (aConfidenceInterval * Math.pow(10, ((lThreshold - iIonsScore) / 10)));
    }

    /**
     * method that calculates the IdentityThreshold at default confidence alpha = 0.05.<br>
     *
     * @return double      IdentityThreshold.
     */
    public double calculateIdentityThreshold() {
        return calculateIdentityThreshold(0.05);
    }

    /**
     * method that calculates the IdentityThreshold at confidence alpha = parameter.<br>
     *
     * @param aConfidenceInterval the confidence interval
     * @return double      IdentityThreshold.
     */
    public double calculateIdentityThreshold(double aConfidenceInterval) {
        return 10.0 * Math.log(iQueryIdentityThreshold / (aConfidenceInterval * 20.0)) / Math.log(10);
    }

    /**
     * This method method parse the String with the coded information of the ionseries type's into an int[].
     *
     * @param aIonSeries String with the ionSeries information.
     */
    private void parseIonSeries(String aIonSeries) {
        iIonSeriesFound = new int[aIonSeries.length()];
        for (int i = 0; i < aIonSeries.length(); i++) {
            iIonSeriesFound[i] = Character.getNumericValue(aIonSeries.charAt(i));
        }
    }

    /**
     * Returns an instance of PeptideHitAnnotation. It holds information about the ion series of this peptidehit.
     *
     * @param aMasses      Instance of the Masses Class to calculate the ion series.
     * @param aParameters  Instance of the Parameters Class to get the mass tolerance data.
     * @param aPrecursorMZ Double with the MZ value of the precursor of the spectrum wherefrom this peptidehit was
     *                     created.
     * aPrecursorCharge    String with the precursor charge.
     * @return PeptideHitAnnotation     returns an instance of PeptideHitAnnotation wherefrom you can get the ion series
     *         of this peptidehit.
     */
    public PeptideHitAnnotation getPeptideHitAnnotation(Masses aMasses, Parameters aParameters, double aPrecursorMZ, String aPrecursorCharge) {
        if (iPha == null) {
            iPha = new PeptideHitAnnotation(iSequence, iModifications, aMasses, aParameters, iIonSeriesFound, aPrecursorMZ, aPrecursorCharge);
        }
        return iPha;
    }

    /**
     * Returns an instance of PeptideHitAnnotation. It holds information about the ion series of this peptidehit.
     *
     * @param aMasses     Instance of the Masses Class to calculate the ion series.
     * @param aParameters Instance of the Parameters Class to get the mass tolerance data.
     * @return PeptideHitAnnotation     returns an instance of PeptideHitAnnotation wherefrom you can get the ion series
     *         of this peptidehit.
     */
    public PeptideHitAnnotation getPeptideHitAnnotation(Masses aMasses, Parameters aParameters) {
        if (iPha == null) {
            iPha = new PeptideHitAnnotation(iSequence, iModifications, aMasses, aParameters, iIonSeriesFound);
        }
        return iPha;
    }

    /**
     * This method returns a String with information about this peptidehit.
     *
     * @return String with information about this peptidehit.
     */
    public String toString() {
        return (getModifiedSequence() + " - " + iIonsScore);
    }
}
