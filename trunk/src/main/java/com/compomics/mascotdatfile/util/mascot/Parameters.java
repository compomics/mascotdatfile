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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 23-feb-2006
 * Time: 14:32:13
 */

/**
 * This class contains all the parsed data from the parameters section of the datfile.
 */
public class Parameters implements Serializable {
    // Class specific log4j logger for Parameters instances.
    private static Logger logger = Logger.getLogger(Parameters.class);

    /**
     * The licence information in a String.
     */
    private String iLicense = null;
    /**
     * The data from a parameters-key. Value 'null' most of the time.
     */
    private String iMP = null;
    /**
     * The data from a parameters-key. Value 'null' most of the time.
     */
    private String iNM = null;
    /**
     * The the search title text.
     */
    private String iCom = null;
    /**
     * a ion weighting, value is often null.
     */
    private String iIATOL = null;
    /**
     * a++ ion weighting, value is often null.
     */
    private String iIA2TOL = null;
    /**
     * a* ion weighting, value is often null.
     */
    private String iIASTOL = null;
    /**
     * b ion weighting, value is often null.
     */
    private String iIBTOL = null;
    /**
     * b++ ion weighting, value is often null.
     */
    private String iIB2TOL = null;
    /**
     * b* ion weighting, value is often null.
     */
    private String iIBSTOL = null;
    /**
     * y ion weighting, value is often null.
     */
    private String iIYTOL = null;
    /**
     * y++ ion weighting, value is often null.
     */
    private String iIY2TOL = null;
    /**
     * y* ion weighting, value is often null.
     */
    private String iIYSTOL = null;
    /**
     * Protein Mass , value is often null.
     */
    private String iSEG = null;
    /**
     * Protein Mass Tol, value is often null.
     */
    private String iSEGT = null;
    /**
     * Units for SEGT, value is often null.
     */
    private String iSEGTU = null;
    /**
     * Tolerance data, value is often null.
     */
    private String iLTOL = null;
    /**
     * The Peptide Mass Tolerance parameter whereby the datfile was created.
     */
    private String iTOL = null;
    /**
     * The Peptide Mass Tolerance Unit to express LTOL.
     */
    private String iTOLU = null;
    /**
     * The fragment mass tolerance.
     */
    private String iITH = null;
    /**
     * The Fragment Mass Tolerance parameter whereby the datfile was created.
     */
    private String iITOL = null;
    /**
     * The Fragment Mass Tolerance Unit to express iITOL.
     */
    private String iITOLU = null;
    /**
     * The data about 'Partials Factor' (Mascot manual)
     */
    private String iPFA = null;
    /**
     * The (primary) database that was used for the Mascot search. See also 
     * iSecondaryDatabases.
     */
    private String iDatabase = null;
    /**
     * The secondary databases that was used for the Mascot search. Empty if 
     * only a single (primary) database was used.
     */
    private ArrayList<String> iSecondaryDatabases = null;
    /**
     * The FIXED modifications (String).
     */
    private String iFixedModifications = null;
    /**
     * Data about isotopes.
     */
    private String iMass = null;
    /**
     * The cleavage info whereby the peptides were cleaved. (ex: trypsin)
     */
    private String iCleavage = null;
    /**
     * The path&filename of the input file used for the mascot search.
     */
    private String iFile = null;
    /**
     * lookup
     */
    private String iPeak = null;
    /**
     * lookup
     */
    private String iQue = null;
    private String iTwo = null;
    /**
     * Parameter about the search type.
     */
    private String iSearch = null;
    /**
     * The username who submitted the Mascot search.
     */
    private String iUserName = null;
    /**
     * The e-mail of <iUserName>.
     */
    private String iUserEmail = null;
    /**
     * The possible charges on ions.
     */
    private String iCharge = null;
    private String iIntermediate = null;
    /**
     * The report setting.
     */
    private String iReport = null;
    private String iOverview = null;
    /**
     * The raw file type whereto mascot prints its results.
     */
    private String iFormat = null;
    /**
     * The version of <iFormat>.
     */
    private String iFormVersion = null;
    private String iFrag = null;
    /**
     * The variable modifications (String).
     */
    private String iVariableModifications = null;

    /**
     * The participating users.
     */
    private String iUser00 = null;
    private String iUser01 = null;
    private String iUser02 = null;
    private String iUser03 = null;
    private String iUser04 = null;
    private String iUser05 = null;
    private String iUser06 = null;
    private String iUser07 = null;
    private String iUser08 = null;
    private String iUser09 = null;
    private String iUser10 = null;
    private String iUser11 = null;
    private String iUser12 = null;
    private String iPrecursor = null;

    /**
     * The parameter for taxonomy information of the mascot search.
     */
    private String iTaxonomy = null;
    private String iAccession = null;
    private String iReportType = null;
    private String iSubcluster = null;
    private String iICAT = null;
    /**
     * The massspectrometer that made the spectra of this search.
     */
    private String iInstrument = null;
    private String iErrorTolerant = null;
    private String iFrames = null;
    private String iCutout = null;
    private String iQuantiation = null;
    /**
     * The ID of the user.
     */
    private String iUserID = null;
    /**
     * For an MS/MS Ions Search, choose the description which best matches the type of instrument used to acquire the
     * data. This setting determines which fragment ion series will be used for scoring, according to the following
     * table.
     */
    private int[] iRules = null;

    /**
     * This variable stores the 'Distiller' origin status. This boolean is used in order to parse the filename of the
     * MS/MS spectra appropriately.
     */
    private boolean iDistillerMultiFile = false;

    /**
     * This variable stores the raw filenames from a multifile Distiller project.
     */
    private String[] iDistillerMultiFileNames = null;
    /**
     * The Distiller options, if any where found!
     */
    private HashMap iDistillerOptions = null;
    /**
     * The Distiller processing boolean reports on whether or not this data has been processed & searched by Mascot
     * Distiller.
     */
    private boolean iDistillerProcessing = false;

    /**
     * The Proteome Discoverer option to the remote file.
     */
    private String iProteomeDiscovererFileTime = null;

    /**
     * The Proteome Discoverer option to the remote file path.
     */
    private String iProteomeDiscovererFilePath = null;

    /**
     * The Proteome Discoverer option to the remote file size.
     */
    private String iProteomeDiscovererFileSize = null;

    /**
     * This method parses a String with numbers presenting the rules that define the instrument type.
     *
     * @param aRules String with rule-numbers
     * @return int[]        int[] with rule numbers
     */
    private int[] parseRules(String aRules) {
        //1.splice the rules into different tokens.
        StringTokenizer st = new StringTokenizer(aRules, ",");
        //2.make a int[] with the size == <st.countTokens()>
        int[] lRules = new int[st.countTokens()];
        int lCount = 0;
        //3.Fill the int[]
        while (st.hasMoreTokens()) {
            lRules[lCount] = Integer.parseInt(st.nextToken());
            lCount++;
        }
        return lRules;
    }

    /**
     * This constructor gets a HashMap p which contains all the data in the parameter section of a datfile.
     *
     * @param p
     */
    public Parameters(HashMap p) {
        //parse all the key-values into instance variables.
        iLicense = (String) p.get("LICENSE");
        iMP = (String) p.get("MP");
        iNM = (String) p.get("NM");
        iCom = (String) p.get("COM");
        iIATOL = (String) p.get("IATOL");
        iIA2TOL = (String) p.get("IA2TOL");
        iIASTOL = (String) p.get("IASTOL");
        iIBTOL = (String) p.get("IBTOL");
        iIB2TOL = (String) p.get("IB2TOL");
        iIBSTOL = (String) p.get("IBSTOL");
        iIYTOL = (String) p.get("IYTOL");
        iIY2TOL = (String) p.get("IY2TOL");
        iIYSTOL = (String) p.get("IYSTOL");
        iSEG = (String) p.get("SEG");
        iSEGT = (String) p.get("SEGT");
        iSEGTU = (String) p.get("SEGTU");
        iLTOL = (String) p.get("LTOL");
        iTOL = (String) p.get("TOL");
        iTOLU = (String) p.get("TOLU");
        iITH = (String) p.get("ITH");
        iITOL = (String) p.get("ITOL");
        iITOLU = (String) p.get("ITOLU");
        iPFA = (String) p.get("PFA");
        iDatabase = (String) p.get("DB");
        
        iSecondaryDatabases = new ArrayList<String>();

        for (int i = 2; i < 100; i++) { // note that this limits the number of secondary databases, but should be more than enough...
            if (p.containsKey("DB" + i)) {
                iSecondaryDatabases.add((String) p.get("DB" + i));
            }
        }

        iFixedModifications = (String) p.get("MODS");
        iMass = (String) p.get("MASS");
        iCleavage = (String) p.get("CLE");
        iFile = (String) p.get("FILE");
        iPeak = (String) p.get("PEAK");
        iQue = (String) p.get("QUE");
        iTwo = (String) p.get("TWO");
        iSearch = (String) p.get("SEARCH");
        iUserName = (String) p.get("USERNAME");
        iUserEmail = (String) p.get("USEREMAIL");
        iCharge = (String) p.get("CHARGE");
        iIntermediate = (String) p.get("INTERMEDIATE");
        iReport = (String) p.get("REPORT");
        iOverview = (String) p.get("OVERVIEW");
        iFormat = (String) p.get("FORMAT");
        iFormVersion = (String) p.get("FORMVER");
        iFrag = (String) p.get("FRAG");
        iVariableModifications = (String) p.get("IT_MODS");
        iUser00 = (String) p.get("USER00");
        iUser01 = (String) p.get("USER01");
        iUser02 = (String) p.get("USER02");
        iUser03 = (String) p.get("USER03");
        iUser04 = (String) p.get("USER04");
        iUser05 = (String) p.get("USER05");
        iUser06 = (String) p.get("USER06");
        iUser07 = (String) p.get("USER07");
        iUser08 = (String) p.get("USER08");
        iUser09 = (String) p.get("USER09");
        iUser10 = (String) p.get("USER10");
        iUser11 = (String) p.get("USER11");
        iUser12 = (String) p.get("USER12");
        iPrecursor = (String) p.get("PRECURSOR");
        iTaxonomy = (String) p.get("TAXONOMY");
        iAccession = (String) p.get("ACCESSION");
        iReportType = (String) p.get("REPTYPE");
        iSubcluster = (String) p.get("SUBCLUSTER");
        iICAT = (String) p.get("ICAT");
        iInstrument = (String) p.get("INSTRUMENT");
        iErrorTolerant = (String) p.get("ERRORTOLERANT");
        iFrames = (String) p.get("FRAMES");
        iCutout = (String) p.get("CUTOUT");
        iUserID = (String) p.get("USERID");
        iRules = parseRules((String) p.get("RULES"));
        iQuantiation = (String) p.get("QUANTITATION");

        // Proteome discoverer specific options.
        iProteomeDiscovererFileTime = (String) p.get("file time");
        iProteomeDiscovererFilePath = (String) p.get("file path");
        iProteomeDiscovererFileSize = (String) p.get("file size");

        // If Distiller multifile information is found, parse it.
        Object o = p.get("_DISTILLER_RAWFILE[0]");
        if (o != null) {
            iDistillerMultiFile = true;
            boolean running = true;
            int lCounter = 0;
            Vector<String> lRawFileNames = new Vector<String>();
            while (running) {
                String s = (String) p.get("_DISTILLER_RAWFILE[" + lCounter + "]");
                if (s == null) {
                    running = false;
                } else {
                    lRawFileNames.add(s);
                    lCounter++;
                }
            }
            iDistillerMultiFileNames = new String[lRawFileNames.size()];
            lRawFileNames.toArray(iDistillerMultiFileNames);
        }

        // Ok, now lets try to find any Distiller parameters.
        // These always start with an '_' underscore symbol.
        Iterator<String> iter = p.keySet().iterator();
        while (iter.hasNext()) {
            String lKey = iter.next();
            if (lKey.startsWith("_")) {
                if (iDistillerOptions == null) {
                    iDistillerOptions = new HashMap();
                    iDistillerProcessing = true;
                }
                iDistillerOptions.put(lKey, p.get(lKey));
            }
        }

        if (iFile != null) {
            // Second Distiller option, If the source file ends with .RAW - This means the RAW data must
            // have been processed before the search was performed. We assume Mascot Distiller was used in these cases then.
            if (iFile.toLowerCase().endsWith(".raw")) {
                iDistillerProcessing = true;
            }

            if (iFile.toLowerCase().endsWith(".raw.-1.mgf")) {
                iDistillerProcessing = true;
            }
        }

    }
    //next up, a huge list of getters and setters of all the parameter instance variables.

    /**
     * Returns the licence information in a String.
     *
     * @return the licence information in a String.
     */
    public String getLicense() {
        return iLicense;
    }

    /**
     * Sets the licence information in a String.
     *
     * @param aLicense the licence information in a String.
     */
    public void setLicense(String aLicense) {
        iLicense = aLicense;
    }

    /**
     * Returns the data from a parameters-key. Value 'null' most of the time.
     *
     * @return the data from a parameters-key. Value 'null' most of the time.
     */
    public String getMP() {
        return iMP;
    }

    /**
     * Sets this holds data from a parameters-key that value 'null' most of the time.
     *
     * @param aMP this holds data from a parameters-key that value 'null' most of the time.
     */
    public void setMP(String aMP) {
        iMP = aMP;
    }

    /**
     * Returns this holds data from a parameters-key that value 'null' most of the time.
     *
     * @return this holds data from a parameters-key that value 'null' most of the time.
     */
    public String getNM() {
        return iNM;
    }

    /**
     * Sets the data from a parameters-key. Value 'null' most of the time.
     *
     * @param aNM the data from a parameters-key. Value 'null' most of the time.
     */
    public void setNM(String aNM) {
        iNM = aNM;
    }

    /**
     * Returns the the search title text.
     *
     * @return the the search title text.
     */
    public String getCom() {
        return iCom;
    }

    /**
     * Sets the the search title text.
     *
     * @param aCom the the search title text.
     */
    public void setCom(String aCom) {
        iCom = aCom;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIATOL() {
        return iIATOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIATOL tolerance data, value is often null.
     */
    public void setIATOL(String aIATOL) {
        iIATOL = aIATOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIA2TOL() {
        return iIA2TOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIA2TOL tolerance data, value is often null.
     */
    public void setIA2TOL(String aIA2TOL) {
        iIA2TOL = aIA2TOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIASTOL() {
        return iIASTOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIASTOL tolerance data, value is often null.
     */
    public void setIASTOL(String aIASTOL) {
        iIASTOL = aIASTOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIBTOL() {
        return iIBTOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIBTOL tolerance data, value is often null.
     */
    public void setIBTOL(String aIBTOL) {
        iIBTOL = aIBTOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIB2TOL() {
        return iIB2TOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIB2TOL tolerance data, value is often null.
     */
    public void setIB2TOL(String aIB2TOL) {
        iIB2TOL = aIB2TOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIBSTOL() {
        return iIBSTOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIBSTOL tolerance data, value is often null.
     */
    public void setIBSTOL(String aIBSTOL) {
        iIBSTOL = aIBSTOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIYTOL() {
        return iIYTOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIYTOL tolerance data, value is often null.
     */
    public void setIYTOL(String aIYTOL) {
        iIYTOL = aIYTOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIY2TOL() {
        return iIY2TOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIY2TOL tolerance data, value is often null.
     */
    public void setIY2TOL(String aIY2TOL) {
        iIY2TOL = aIY2TOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getIYSTOL() {
        return iIYSTOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aIYSTOL tolerance data, value is often null.
     */
    public void setIYSTOL(String aIYSTOL) {
        iIYSTOL = aIYSTOL;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getSEG() {
        return iSEG;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aSEG tolerance data, value is often null.
     */
    public void setSEG(String aSEG) {
        iSEG = aSEG;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getSEGT() {
        return iSEGT;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aSEGT tolerance data, value is often null.
     */
    public void setSEGT(String aSEGT) {
        iSEGT = aSEGT;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getSEGTU() {
        return iSEGTU;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aSEGTU tolerance data, value is often null.
     */
    public void setSEGTU(String aSEGTU) {
        iSEGTU = aSEGTU;
    }

    /**
     * Returns tolerance data, value is often null.
     *
     * @return tolerance data, value is often null.
     */
    public String getLTOL() {
        return iLTOL;
    }

    /**
     * Sets tolerance data, value is often null.
     *
     * @param aLTOL tolerance data, value is often null.
     */
    public void setLTOL(String aLTOL) {
        iLTOL = aLTOL;
    }

    /**
     * Returns the Peptide Mass Tolerance parameter whereby the datfile was created.
     *
     * @return the Peptide Mass Tolerance parameter whereby the datfile was created.
     */
    public String getTOL() {
        return iTOL;
    }

    /**
     * Sets the Peptide Mass Tolerance parameter whereby the datfile was created.
     *
     * @param aTOL the Peptide Mass Tolerance parameter whereby the datfile was created.
     */
    public void setTOL(String aTOL) {
        iTOL = aTOL;
    }

    /**
     * Returns the Peptide Mass Tolerance Unit to express LTOL.
     *
     * @return the Peptide Mass Tolerance Unit to express LTOL.
     */
    public String getTOLU() {
        return iTOLU;
    }

    /**
     * Sets the Peptide Mass Tolerance Unit to express LTOL.
     *
     * @param aTOLU the Peptide Mass Tolerance Unit to express LTOL.
     */
    public void setTOLU(String aTOLU) {
        iTOLU = aTOLU;
    }

    /**
     * Returns the fragment mass tolerance.
     *
     * @return the fragment mass tolerance.
     */
    public String getITH() {
        return iITH;
    }

    /**
     * Sets the fragment mass tolerance.
     *
     * @param aITH the fragment mass tolerance.
     */
    public void setITH(String aITH) {
        iITH = aITH;
    }

    /**
     * Returns the Fragment Mass Tolerance parameter whereby the datfile was created.
     *
     * @return the Fragment Mass Tolerance parameter whereby the datfile was created.
     */
    public String getITOL() {
        return iITOL;
    }

    /**
     * Sets the Fragment Mass Tolerance parameter whereby the datfile was created.
     *
     * @param aITOL the Fragment Mass Tolerance parameter whereby the datfile was created.
     */
    public void setITOL(String aITOL) {
        iITOL = aITOL;
    }

    /**
     * Returns the Fragment Mass Tolerance Unit to express iITOL.
     *
     * @return the Fragment Mass Tolerance Unit to express iITOL.
     */
    public String getITOLU() {
        return iITOLU;
    }

    /**
     * Sets the Fragment Mass Tolerance Unit to express iITOL.
     *
     * @param aITOLU the Fragment Mass Tolerance Unit to express iITOL.
     */
    public void setITOLU(String aITOLU) {
        iITOLU = aITOLU;
    }

    /**
     * Returns the data about 'Partials Factor' (Mascot manual)
     *
     * @return the data about 'Partials Factor' (Mascot manual)
     */
    public String getPFA() {
        return iPFA;
    }

    /**
     * Sets the data about 'Partials Factor' (Mascot manual)
     *
     * @param aPFA the data about 'Partials Factor' (Mascot manual)
     */
    public void setPFA(String aPFA) {
        iPFA = aPFA;
    }

    /**
     * Returns the database that was used for the Mascot search.
     *
     * @return the database that was used for the Mascot search.
     */
    public String getDatabase() {
        return iDatabase;
    }

    /**
     * Sets the database that was used for the Mascot search.
     *
     * @param aDatabase the database that was used for the Mascot search.
     */
    public void setDatabase(String aDatabase) {
        iDatabase = aDatabase;
    }
    
    /**
     * Returns the secondary databases that was used for the Mascot search. If 
     * empty list only a primary database was used.
     *
     * @return the secondary databases that was used for the Mascot search.
     */
    public ArrayList<String> getSecondaryDatabases() {
        return iSecondaryDatabases;
    }

    /**
     * Sets the secondary databases that was used for the Mascot search.
     *
     * @param aSecondaryDatabases the secondary database that was used for the Mascot search.
     */
    public void setSecondaryDatabases(ArrayList<String> aSecondaryDatabases) {
        iSecondaryDatabases = aSecondaryDatabases;
    }

    /**
     * Returns the FIXED modifications (String).
     *
     * @return the FIXED modifications (String).
     */
    public String getFixedModifications() {
        return iFixedModifications;
    }

    /**
     * Sets the FIXED modifications (String).
     *
     * @param aFixedModifications the FIXED modifications (String).
     */
    public void setFixedModifications(String aFixedModifications) {
        iFixedModifications = aFixedModifications;
    }

    /**
     * Returns data about isotopes.
     *
     * @return data about isotopes.
     */
    public String getMass() {
        return iMass;
    }

    /**
     * Sets data about isotopes.
     *
     * @param aMass data about isotopes.
     */
    public void setMass(String aMass) {
        iMass = aMass;
    }

    /**
     * Returns the cleavage info whereby the peptides were cleaved. (ex: trypsin)
     *
     * @return the cleavage info whereby the peptides were cleaved. (ex: trypsin)
     */
    public String getCleavage() {
        return iCleavage;
    }

    /**
     * Sets the cleavage info whereby the peptides were cleaved. (ex: trypsin)
     *
     * @param aCleavage the cleavage info whereby the peptides were cleaved. (ex: trypsin)
     */
    public void setCleavage(String aCleavage) {
        iCleavage = aCleavage;
    }

    /**
     * Returns the path&filename of the input file used for the mascot search.
     *
     * @return the path&filename of the input file used for the mascot search.
     */
    public String getFile() {
        return iFile;
    }

    /**
     * Sets the path&filename of the input file used for the mascot search.
     *
     * @param aFile the path&filename of the input file used for the mascot search.
     */
    public void setFile(String aFile) {
        iFile = aFile;
    }

    /**
     * Returns lookup
     *
     * @return lookup
     */
    public String getPeak() {
        return iPeak;
    }

    /**
     * Sets lookup
     *
     * @param aPeak lookup
     */
    public void setPeak(String aPeak) {
        iPeak = aPeak;
    }

    /**
     * Returns lookup
     *
     * @return lookup
     */
    public String getQue() {
        return iQue;
    }

    /**
     * Sets lookup
     *
     * @param aQue lookup
     */
    public void setQue(String aQue) {
        iQue = aQue;
    }

    /**
     * Getter for property 'two'.
     *
     * @return Value for property 'two'.
     */
    public String getTwo() {
        return iTwo;
    }

    /**
     * Setter for property 'two'.
     *
     * @param aTwo Value to set for property 'two'.
     */
    public void setTwo(String aTwo) {
        iTwo = aTwo;
    }

    /**
     * Returns parameter about the search type.
     *
     * @return parameter about the search type.
     */
    public String getSearch() {
        return iSearch;
    }

    /**
     * Sets parameter about the search type.
     *
     * @param aSearch parameter about the search type.
     */
    public void setSearch(String aSearch) {
        iSearch = aSearch;
    }

    /**
     * Returns the username who submitted the Mascot search.
     *
     * @return the username who submitted the Mascot search.
     */
    public String getUserName() {
        return iUserName;
    }

    /**
     * Sets the username who submitted the Mascot search.
     *
     * @param aUserName the username who submitted the Mascot search.
     */
    public void setUserName(String aUserName) {
        iUserName = aUserName;
    }

    /**
     * Returns the e-mail of <iUserName>.
     *
     * @return the e-mail of <iUserName>.
     */
    public String getUserEmail() {
        return iUserEmail;
    }

    /**
     * Sets the e-mail of <iUserName>.
     *
     * @param aUserEmail the e-mail of <iUserName>.
     */
    public void setUserEmail(String aUserEmail) {
        iUserEmail = aUserEmail;
    }

    /**
     * Returns the possible charges on ions.
     *
     * @return the possible charges on ions.
     */
    public String getCharge() {
        return iCharge;
    }

    /**
     * Sets the possible charges on ions.
     *
     * @param aCharge the possible charges on ions.
     */
    public void setCharge(String aCharge) {
        iCharge = aCharge;
    }

    /**
     * Getter for property 'intermediate'.
     *
     * @return Value for property 'intermediate'.
     */
    public String getIntermediate() {
        return iIntermediate;
    }

    /**
     * Setter for property 'intermediate'.
     *
     * @param aIntermediate Value to set for property 'intermediate'.
     */
    public void setIntermediate(String aIntermediate) {
        iIntermediate = aIntermediate;
    }

    /**
     * Returns the report setting.
     *
     * @return the report setting.
     */
    public String getReport() {
        return iReport;
    }

    /**
     * Sets the report setting.
     *
     * @param aReport the report setting.
     */
    public void setReport(String aReport) {
        iReport = aReport;
    }

    /**
     * Getter for property 'overview'.
     *
     * @return Value for property 'overview'.
     */
    public String getOverview() {
        return iOverview;
    }

    /**
     * Setter for property 'overview'.
     *
     * @param aOverview Value to set for property 'overview'.
     */
    public void setOverview(String aOverview) {
        iOverview = aOverview;
    }

    /**
     * Returns the raw filetype whereto mascot prints its results.
     *
     * @return the raw filetype whereto mascot prints its results.
     */
    public String getFormat() {
        return iFormat;
    }

    /**
     * Sets the raw filetype whereto mascot prints its results.
     *
     * @param aFormat the raw filetype whereto mascot prints its results.
     */
    public void setFormat(String aFormat) {
        iFormat = aFormat;
    }

    /**
     * Returns the version of the format.
     *
     * @return the version of the format.
     */
    public String getFormVersion() {
        return iFormVersion;
    }

    /**
     * Sets the version of the format.
     *
     * @param aFormVersion the version of the format.
     */
    public void setFormVersion(String aFormVersion) {
        iFormVersion = aFormVersion;
    }

    /**
     * Getter for property 'frag'.
     *
     * @return Value for property 'frag'.
     */
    public String getFrag() {
        return iFrag;
    }

    /**
     * Setter for property 'frag'.
     *
     * @param aFrag Value to set for property 'frag'.
     */
    public void setFrag(String aFrag) {
        iFrag = aFrag;
    }

    /**
     * Returns the variable modifications (String).
     *
     * @return the variable modifications (String).
     */
    public String getVariableModifications() {
        return iVariableModifications;
    }

    /**
     * Sets the variable modifications (String).
     *
     * @param aVariableModifications the variable modifications (String).
     */
    public void setVariableModifications(String aVariableModifications) {
        iVariableModifications = aVariableModifications;
    }

    /**
     * Returns the participating users.
     *
     * @return the participating users.
     */
    public String getUser00() {
        return iUser00;
    }

    /**
     * Sets the participating users.
     *
     * @param aUser00 the participating users.
     */
    public void setUser00(String aUser00) {
        iUser00 = aUser00;
    }

    /**
     * Getter for property 'user01'.
     *
     * @return Value for property 'user01'.
     */
    public String getUser01() {
        return iUser01;
    }

    /**
     * Setter for property 'user01'.
     *
     * @param aUser01 Value to set for property 'user01'.
     */
    public void setUser01(String aUser01) {
        iUser01 = aUser01;
    }

    /**
     * Getter for property 'user02'.
     *
     * @return Value for property 'user02'.
     */
    public String getUser02() {
        return iUser02;
    }

    /**
     * Setter for property 'user02'.
     *
     * @param aUser02 Value to set for property 'user02'.
     */
    public void setUser02(String aUser02) {
        iUser02 = aUser02;
    }

    /**
     * Getter for property 'user03'.
     *
     * @return Value for property 'user03'.
     */
    public String getUser03() {
        return iUser03;
    }

    /**
     * Setter for property 'user03'.
     *
     * @param aUser03 Value to set for property 'user03'.
     */
    public void setUser03(String aUser03) {
        iUser03 = aUser03;
    }

    /**
     * Getter for property 'user04'.
     *
     * @return Value for property 'user04'.
     */
    public String getUser04() {
        return iUser04;
    }

    /**
     * Setter for property 'user04'.
     *
     * @param aUser04 Value to set for property 'user04'.
     */
    public void setUser04(String aUser04) {
        iUser04 = aUser04;
    }

    /**
     * Getter for property 'user05'.
     *
     * @return Value for property 'user05'.
     */
    public String getUser05() {
        return iUser05;
    }

    /**
     * Setter for property 'user05'.
     *
     * @param aUser05 Value to set for property 'user05'.
     */
    public void setUser05(String aUser05) {
        iUser05 = aUser05;
    }

    /**
     * Getter for property 'user06'.
     *
     * @return Value for property 'user06'.
     */
    public String getUser06() {
        return iUser06;
    }

    /**
     * Setter for property 'user06'.
     *
     * @param aUser06 Value to set for property 'user06'.
     */
    public void setUser06(String aUser06) {
        iUser06 = aUser06;
    }

    /**
     * Getter for property 'user07'.
     *
     * @return Value for property 'user07'.
     */
    public String getUser07() {
        return iUser07;
    }

    /**
     * Setter for property 'user07'.
     *
     * @param aUser07 Value to set for property 'user07'.
     */
    public void setUser07(String aUser07) {
        iUser07 = aUser07;
    }

    /**
     * Getter for property 'user08'.
     *
     * @return Value for property 'user08'.
     */
    public String getUser08() {
        return iUser08;
    }

    /**
     * Setter for property 'user08'.
     *
     * @param aUser08 Value to set for property 'user08'.
     */
    public void setUser08(String aUser08) {
        iUser08 = aUser08;
    }

    /**
     * Getter for property 'user09'.
     *
     * @return Value for property 'user09'.
     */
    public String getUser09() {
        return iUser09;
    }

    /**
     * Setter for property 'user09'.
     *
     * @param aUser09 Value to set for property 'user09'.
     */
    public void setUser09(String aUser09) {
        iUser09 = aUser09;
    }

    /**
     * Getter for property 'user10'.
     *
     * @return Value for property 'user10'.
     */
    public String getUser10() {
        return iUser10;
    }

    /**
     * Setter for property 'user10'.
     *
     * @param aUser10 Value to set for property 'user10'.
     */
    public void setUser10(String aUser10) {
        iUser10 = aUser10;
    }

    /**
     * Getter for property 'user11'.
     *
     * @return Value for property 'user11'.
     */
    public String getUser11() {
        return iUser11;
    }

    /**
     * Setter for property 'user11'.
     *
     * @param aUser11 Value to set for property 'user11'.
     */
    public void setUser11(String aUser11) {
        iUser11 = aUser11;
    }

    /**
     * Getter for property 'user12'.
     *
     * @return Value for property 'user12'.
     */
    public String getUser12() {
        return iUser12;
    }

    /**
     * Setter for property 'user12'.
     *
     * @param aUser12 Value to set for property 'user12'.
     */
    public void setUser12(String aUser12) {
        iUser12 = aUser12;
    }

    /**
     * Getter for property 'precursor'.
     *
     * @return Value for property 'precursor'.
     */
    public String getPrecursor() {
        return iPrecursor;
    }

    /**
     * Setter for property 'precursor'.
     *
     * @param aPrecursor Value to set for property 'precursor'.
     */
    public void setPrecursor(String aPrecursor) {
        iPrecursor = aPrecursor;
    }

    /**
     * Returns the parameter for taxonomy information of the mascot search.
     *
     * @return the parameter for taxonomy information of the mascot search.
     */
    public String getTaxonomy() {
        return iTaxonomy;
    }

    /**
     * Sets the parameter for taxonomy information of the mascot search.
     *
     * @param aTaxonomy the parameter for taxonomy information of the mascot search.
     */
    public void setTaxonomy(String aTaxonomy) {
        iTaxonomy = aTaxonomy;
    }

    /**
     * Getter for property 'accession'.
     *
     * @return Value for property 'accession'.
     */
    public String getAccession() {
        return iAccession;
    }

    /**
     * Setter for property 'accession'.
     *
     * @param aAccession Value to set for property 'accession'.
     */
    public void setAccession(String aAccession) {
        iAccession = aAccession;
    }

    /**
     * Getter for property 'reportType'.
     *
     * @return Value for property 'reportType'.
     */
    public String getReportType() {
        return iReportType;
    }

    /**
     * Setter for property 'reportType'.
     *
     * @param aReportType Value to set for property 'reportType'.
     */
    public void setReportType(String aReportType) {
        iReportType = aReportType;
    }

    /**
     * Getter for property 'subcluster'.
     *
     * @return Value for property 'subcluster'.
     */
    public String getSubcluster() {
        return iSubcluster;
    }

    /**
     * Setter for property 'subcluster'.
     *
     * @param aSubcluster Value to set for property 'subcluster'.
     */
    public void setSubcluster(String aSubcluster) {
        iSubcluster = aSubcluster;
    }

    /**
     * Getter for property 'ICAT'.
     *
     * @return Value for property 'ICAT'.
     */
    public String getICAT() {
        return iICAT;
    }

    /**
     * Setter for property 'ICAT'.
     *
     * @param aICAT Value to set for property 'ICAT'.
     */
    public void setICAT(String aICAT) {
        iICAT = aICAT;
    }

    /**
     * Returns the massspectrometer that made the spectra of this search.
     *
     * @return the massspectrometer that made the spectra of this search.
     */
    public String getInstrument() {
        return iInstrument;
    }

    /**
     * Sets the massspectrometer that made the spectra of this search.
     *
     * @param aInstrument the massspectrometer that made the spectra of this search.
     */
    public void setInstrument(String aInstrument) {
        iInstrument = aInstrument;
    }

    /**
     * Getter for property 'errorTolerant'.
     *
     * @return Value for property 'errorTolerant'.
     */
    public String getErrorTolerant() {
        return iErrorTolerant;
    }

    /**
     * Setter for property 'errorTolerant'.
     *
     * @param aErrorTolerant Value to set for property 'errorTolerant'.
     */
    public void setErrorTolerant(String aErrorTolerant) {
        iErrorTolerant = aErrorTolerant;
    }

    /**
     * Getter for property 'frames'.
     *
     * @return Value for property 'frames'.
     */
    public String getFrames() {
        return iFrames;
    }

    /**
     * Setter for property 'frames'.
     *
     * @param aFrames Value to set for property 'frames'.
     */
    public void setFrames(String aFrames) {
        iFrames = aFrames;
    }

    /**
     * Getter for property 'cutout'.
     *
     * @return Value for property 'cutout'.
     */
    public String getCutout() {
        return iCutout;
    }

    /**
     * Setter for property 'cutout'.
     *
     * @param aCutout Value to set for property 'cutout'.
     */
    public void setCutout(String aCutout) {
        iCutout = aCutout;
    }

    /**
     * Returns the ID of the user.
     *
     * @return the ID of the user.
     */
    public String getUserID() {
        return iUserID;
    }

    /**
     * Sets the ID of the user.
     *
     * @param aUserID the ID of the user.
     */
    public void setUserID(String aUserID) {
        iUserID = aUserID;
    }

    /**
     * Returns the list with the rule numbers that define the instrument type in the configuration file
     * fragmentation_rules.
     *
     * @return the list with the rule numbers that define the instrument type in the configuration file
     *         fragmentation_rules.
     */
    public int[] getRules() {
        return iRules;
    }

    /**
     * Sets the list with the rule numbers that define the instrument type in the configuration file
     * fragmentation_rules.
     *
     * @param aRules the list with the rule numbers that define the instrument type in the configuration file
     *               fragmentation_rules.
     */
    public void setRules(int[] aRules) {
        iRules = aRules;
    }

    /**
     * Returns the status of the Distiller project as multi or single file.
     *
     * @return the status of the Distiller project as multi or single file
     */
    public boolean isDistillerMultiFile() {
        return iDistillerMultiFile;
    }

    /**
     * @return trur if Distiller processing.
     */
    public boolean isDistillerProcessing() {
        return iDistillerProcessing;

    }

    public HashMap getDistillerOptions() {
        return iDistillerOptions;
    }

    /**
     * Returns the different raw filenames within a single Distiller multifile project.
     *
     * @return the different raw filenames within a single Distiller multifile project
     */
    public String[] getDistillerMultiFileNames() {
        return iDistillerMultiFileNames;
    }

    /**
     * Returns the Quantitation type parameter.
     *
     * @return String Quantitation type.
     */
    public String getQuantiation() {
        return iQuantiation;
    }

    /**
     * Returns true if the datfile has been searched by proteome discoverer.
     * @return true if the datfile has been searched by proteome discoverer
     */
    public boolean isProteomeDiscoverer(){
        return !(iProteomeDiscovererFilePath == null && iProteomeDiscovererFileSize == null && iProteomeDiscovererFileTime == null);
    }

    /**
     * Returns the Time specified by Proteome Discoverer. Null if absent.
     * @return the Time specified by Proteome Discoverer. Null if absent
     */
    public String getProteomeDiscovererFileTime() {
        return iProteomeDiscovererFileTime;
    }

    /**
     *
     * Returns the File Path specified by Proteome Discoverer. Null if absent.
     * @return the File Path specified by Proteome Discoverer. Null if absent
     */
    public String getProteomeDiscovererFilePath() {
        return iProteomeDiscovererFilePath;
    }

    /**
     *
     * Returns the File size specified by Proteome Discoverer. Null if absent.
     * @return the File size specified by Proteome Discoverer. Null if absent
     */
    public String getProteomeDiscovererFileSize() {
        return iProteomeDiscovererFileSize;
    }
}
