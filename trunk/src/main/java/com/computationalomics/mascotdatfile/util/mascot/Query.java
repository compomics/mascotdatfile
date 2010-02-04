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

package com.computationalomics.mascotdatfile.util.mascot;

import com.computationalomics.mascotdatfile.util.interfaces.Spectrum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 23-feb-2006
 * Time: 9:03:50
 */
public class Query implements Spectrum, Serializable {

    /**
     * The title of the MS/MS spectrum.
     */
    private String iTitle = null;
    /**
     * The retention time String of the MS/MS spectrum.
     */
    private String iRetentionTimeInSeconds = null;
    /**
     * The precursor m/z.
     */
    private double iPrecursorMZ = 0.0;
    /**
     * The precursor charge.
     */
    private String iCharge = null;
    /**
     * The double with the precursor mass.
     */
    private double iPrecursorMass = 0.0;
    /**
     * The precursor intensity.
     */
    private double iPrecursorIntensity = 0.0;
    /**
     * The the lowest mass in the spectrum.
     */
    private double iMinMZ = 0;
    /**
     * The highest mass in the spectrum.
     */
    private double iMaxMZ = 0;
    /**
     * The lowest intensity in the spectrum.
     */
    private double iMinIntensity = 0;
    /**
     * The highist intensity in the spectrum.
     */
    private double iMaxIntensity = 0;
    /**
     * The number of peaks in the spectrum.
     */
    private int iNumberOfPeaks = 0;
    /**
     * Number of used. Mascot note: 'obsolete' =>  This is a backwards compatibility feature of mascot.
     * This will be -1 by default.
     */
    private int iNumberUsed1 = -1;
    /**
     * The Peak array that includes all the peaks found in the massspectrum.
     */
    private Peak[] iPeakList = null;
    /**
     * Identifies the query in the mascot search by its number.
     */
    private int iQueryNumber = 0;

    /**
     * The scan number(s) from the Query.
     */
    private String iScans = null;


    /**
     * 090110
     * This boolean indicates whether or not the Query filenames must be transformed.
     * When Mascot Distiller performs the searches out of the raw data - there is no control on the filename.
     * If set to true, this boolean will transform the distiller filename into a shorter sensible filename
     * as used in ms_lims.
     */
    private static boolean iDistillerFilenameConversion = false;

    /**
     * A static reference to the Parameter section, only needed when Distiller processing is used.
     */
    private Parameters iParameters = null;

    /**
     * Constructor that takes a Mascot datfile Query Section as input and parses all the data into an instance of Query and its variables.
     *
     * @param aQueryMap This is the HashMap that contains data about the Mascot datfile Query Section.
     */
    public Query(HashMap aQueryMap, double aPrecursorMZ, String aCharge, double aPrecursorMass, double aPrecursorIntensity, int aQueryNumber) {
        parseQueryHashMap(aQueryMap, aQueryNumber);
        iPrecursorMZ = aPrecursorMZ;
        iCharge = aCharge;
        iPrecursorMass = aPrecursorMass;
        iPrecursorIntensity = aPrecursorIntensity;
    }

    /**
     * Constructor that takes a Mascot datfile Query Section as input and parses all the data into an instance of Query and its variables.
     *
     * @param aQueryMap This is the HashMap that contains data about the Mascot datfile Query Section.
     */
    public Query(HashMap aQueryMap, double aPrecursorMZ, String aCharge, double aPrecursorMass, double aPrecursorIntensity, int aQueryNumber, Parameters aParameters) {

        iParameters = aParameters;


        parseQueryHashMap(aQueryMap, aQueryNumber);
        iPrecursorMZ = aPrecursorMZ;
        iCharge = aCharge;
        iPrecursorMass = aPrecursorMass;
        iPrecursorIntensity = aPrecursorIntensity;

    }

    /**
     * This method parses the Query HashMap (directly passed by the constructor) into variables of this Query object.
     */
    private void parseQueryHashMap(HashMap aQueryMap, int aQueryNumber) {
        //2.If there is spectrum information, parse it.
        if (aQueryMap.containsKey("Ions1")) {
            //a.get lowest mass in the spectrum.
            iMinMZ = Double.parseDouble((String) aQueryMap.get("mass_min"));
            //b.get highest mass in the spectrum.
            iMaxMZ = Double.parseDouble((String) aQueryMap.get("mass_max"));
            //c.get minimum peak with the smallest intensity in the spectrum.
            iMinIntensity = Double.parseDouble((String) aQueryMap.get("int_min"));
            //d.get maximum peak with the smallest intensity in the spectrum.
            iMaxIntensity = Double.parseDouble((String) aQueryMap.get("int_max"));
            //e.get number of peaks found in the spectrum.
            iNumberOfPeaks = Integer.parseInt((String) aQueryMap.get("num_vals"));
            //f.get number of Ions1 used.
            iNumberUsed1 = Integer.parseInt((String) aQueryMap.get("num_used1"));
            //g.get the mass peaks in a PeakArray.
            iPeakList = getPeakArray(aQueryMap);
        }
        //9.initiate aQueryNumber
        iQueryNumber = aQueryNumber;

        // 10. If MascotDistiller is used, get the scannumbers.
        if (aQueryMap.containsKey("scans")) {
            iScans = (String) aQueryMap.get("scans");
        }

        // If available, get the 'charge' string.
        if (aQueryMap.containsKey("charge")) {
            iCharge = (String) aQueryMap.get("charge");
        }

        //1.get the filename
        if (aQueryMap.containsKey("title")) {
            iTitle = parseTitle((String) aQueryMap.get("title"));
        } else {
            iTitle = "No title (Query " + aQueryNumber + ").";
        }

        // If available, get the 'rtinseconds' (retention time in seconds) string.
        if (aQueryMap.containsKey("rtinseconds")) {
            iRetentionTimeInSeconds = (String) aQueryMap.get("rtinseconds");
        }


    }

    /**
     * This method gets the unparsed Title value out of the Query Hashmap, parses the String and returns a readable String.
     *
     * @return String   Readable filename
     */
    private String parseTitle(String aTitle) {
        // 1.Get the unparsed filename .
        // 2.Get trough the while loops and reformat out the '%2x' into a readable filename.
        // 3.Return the parsed filename.

        String tempTitle = aTitle;
        int percentLoc = -1;
        while ((percentLoc = tempTitle.indexOf("%20")) >= 0) {
            tempTitle = tempTitle.substring(0, percentLoc) + " " + tempTitle.substring(percentLoc + 3, tempTitle.length());
        }
        int dotLoc = -1;
        while ((dotLoc = tempTitle.toLowerCase().indexOf("%2e")) >= 0) {
            tempTitle = tempTitle.substring(0, dotLoc) + "." + tempTitle.substring(dotLoc + 3, tempTitle.length());
        }
        int minusLoc = -1;
        while ((minusLoc = tempTitle.toLowerCase().indexOf("%2d")) >= 0) {
            tempTitle = tempTitle.substring(0, minusLoc) + "-" + tempTitle.substring(minusLoc + 3, tempTitle.length());
        }
        int plusLoc = -1;
        while ((plusLoc = tempTitle.toLowerCase().indexOf("%2b")) >= 0) {
            tempTitle = tempTitle.substring(0, plusLoc) + "+" + tempTitle.substring(plusLoc + 3, tempTitle.length());
        }
        int commaLoc = -1;
        while ((commaLoc = tempTitle.toLowerCase().indexOf("%2c")) >= 0) {
            tempTitle = tempTitle.substring(0, commaLoc) + "," + tempTitle.substring(commaLoc + 3, tempTitle.length());
        }
        int loconLoc = -1;
        while ((loconLoc = tempTitle.toLowerCase().indexOf("%3a")) >= 0) {
            tempTitle = tempTitle.substring(0, loconLoc) + ":" + tempTitle.substring(loconLoc + 3, tempTitle.length());
        }
        int backslashloc = -1;
        while ((backslashloc = tempTitle.toLowerCase().indexOf("%5c")) >= 0) {
            tempTitle = tempTitle.substring(0, backslashloc) + "\\" + tempTitle.substring(backslashloc + 3, tempTitle.length());
        }
        int leftBracketLoc = -1;
        while ((leftBracketLoc = tempTitle.toLowerCase().indexOf("%5b")) >= 0) {
            tempTitle = tempTitle.substring(0, leftBracketLoc) + "[" + tempTitle.substring(leftBracketLoc + 3, tempTitle.length());
        }
        int rightBracketLoc = -1;
        while ((rightBracketLoc = tempTitle.toLowerCase().indexOf("%5d")) >= 0) {
            tempTitle = tempTitle.substring(0, rightBracketLoc) + "]" + tempTitle.substring(rightBracketLoc + 3, tempTitle.length());
        }
        int leftRoundBracketLoc = -1;
        while ((leftRoundBracketLoc = tempTitle.toLowerCase().indexOf("%28")) >= 0) {
            tempTitle = tempTitle.substring(0, leftRoundBracketLoc) + "(" + tempTitle.substring(leftRoundBracketLoc + 3, tempTitle.length());
        }
        int rightRoundBracketLoc = -1;
        while ((rightRoundBracketLoc = tempTitle.toLowerCase().indexOf("%29")) >= 0) {
            tempTitle = tempTitle.substring(0, rightRoundBracketLoc) + ")" + tempTitle.substring(rightRoundBracketLoc + 3, tempTitle.length());
        }
        int equalSignLoc = -1;
        while ((equalSignLoc = tempTitle.toLowerCase().indexOf("%3d")) >= 0) {
            tempTitle = tempTitle.substring(0, equalSignLoc) + "=" + tempTitle.substring(equalSignLoc + 3, tempTitle.length());
        }

        // If distiller filename conversion is required, do so.
        if (iDistillerFilenameConversion) {
            tempTitle = processMGFTitleToFilename(tempTitle);
        }

        //now set parsed filename of the query to the instance variable iFilename.
        iTitle = tempTitle;

        return iTitle;
    }

    /**
     * This method returns all the MZ values of the peaks in a double[].
     *
     * @return double[]    Peak mass.
     */
    public double[] getMZArray() {
        double[] lMZArray = new double[iPeakList.length];
        for (int i = 0; i < iPeakList.length; i++) {
            lMZArray[i] = iPeakList[i].getMZ();
        }
        return lMZArray;
    }

    /**
     * Returns the Scan number if found in the Query.
     *
     * @return String with scan numbers.
     */
    public String getScans() {
        return iScans;
    }

    /**
     * This method returns all the Intensity values of the peaks in a double[].
     *
     * @return double[]    Peak intensity.
     */
    public double[] getIntensityArray() {
        double[] lIntensityArray = new double[iPeakList.length];
        for (int i = 0; i < iPeakList.length; i++) {
            lIntensityArray[i] = iPeakList[i].getIntensity();
        }
        return lIntensityArray;
    }

    /**
     * Returns the title of the MS/MS spectrum.
     *
     * @return the title of the MS/MS spectrum.
     */
    public String getTitle() {
        return iTitle;
    }

    /**
     * Sets the title of the MS/MS spectrum.
     *
     * @param aTitle the title of the MS/MS spectrum.
     */
    public void setTitle(String aTitle) {
        iTitle = aTitle;
    }

    /**
     * Returns the retention time string for the spectrum.
     *
     * @return String with the retention time string, can
     *         be 'null' if no retention time was specified.
     */
    public String getRetentionTimeInSeconds() {
        return iRetentionTimeInSeconds;
    }

    public void setRetentionTimeInSeconds(String iRetentionTimeInSeconds) {
        this.iRetentionTimeInSeconds = iRetentionTimeInSeconds;
    }

    /**
     * Returns the precursor charge.
     *
     * @return the precursor charge.
     */
    public String getChargeString() {
        return iCharge;
    }

    /**
     * Getter for property 'precursorMZ'.
     *
     * @return Value for property 'precursorMZ'.
     */
    public double getPrecursorMZ() {
        return iPrecursorMZ;
    }

    /**
     * Getter for property 'precursorMass'.
     *
     * @return Value for property 'precursorMass'.
     */
    public double getPrecursorMass() {
        return iPrecursorMass;
    }

    /**
     * Getter for property 'precursorIntensity'.
     *
     * @return Value for property 'precursorIntensity'.
     */
    public double getPrecursorIntensity() {
        return iPrecursorIntensity;
    }

    /**
     * Sets the precursor charge.
     *
     * @param aCharge the precursor charge.
     */
    public void setChargeString(String aCharge) {
        iCharge = aCharge;
    }

    /**
     * This static swithch will transform the Distiller based "long" filename into a "shorter" functional name for ms_lims.
     */
    public static void setDistillerFilenameProcessing(boolean status) {
        iDistillerFilenameConversion = status;
    }

    /**
     * Returns the the lowest mass in the spectrum.
     *
     * @return the the lowest mass in the spectrum.
     */
    public double getMinMZ() {
        return iMinMZ;
    }

    /**
     * Sets the the lowest mass in the spectrum.
     *
     * @param aMinMZ the the lowest mass in the spectrum.
     */
    public void setMinMZ(double aMinMZ) {
        iMinMZ = aMinMZ;
    }

    /**
     * Returns the highest mass in the spectrum.
     *
     * @return the highest mass in the spectrum.
     */
    public double getMaxMZ() {
        return iMaxMZ;
    }

    /**
     * Sets the highest mass in the spectrum.
     *
     * @param aMaxMZ the highest mass in the spectrum.
     */
    public void setMaxMZ(double aMaxMZ) {
        iMaxMZ = aMaxMZ;
    }

    /**
     * Returns the lowest intensity in the spectrum.
     *
     * @return the lowest intensity in the spectrum.
     */
    public double getMinIntensity() {
        return iMinIntensity;
    }

    /**
     * Sets the lowest intensity in the spectrum.
     *
     * @param aMinIntensity the lowest intensity in the spectrum.
     */
    public void setMinIntensity(double aMinIntensity) {
        iMinIntensity = aMinIntensity;
    }

    /**
     * Returns the highist intensity in the spectrum.
     *
     * @return the highist intensity in the spectrum.
     */
    public double getMaxIntensity() {
        return iMaxIntensity;
    }

    /**
     * Sets the highist intensity in the spectrum.
     *
     * @param aMaxIntensity the highist intensity in the spectrum.
     */
    public void setMaxIntensity(double aMaxIntensity) {
        iMaxIntensity = aMaxIntensity;
    }

    /**
     * Returns the number of peaks in the spectrum.
     *
     * @return the number of peaks in the spectrum.
     */
    public int getNumberOfPeaks() {
        return iNumberOfPeaks;
    }

    /**
     * Sets the number of peaks in the spectrum.
     *
     * @param aNumberOfPeaks the number of peaks in the spectrum.
     */
    public void setNumberOfPeaks(int aNumberOfPeaks) {
        iNumberOfPeaks = aNumberOfPeaks;
    }

    /**
     * Returns number of used. Mascot note: 'obsolete' =>  This is a backwards compatibility feature of mascot.
     * This will be -1 by default.
     *
     * @return number of used. Mascot note: 'obsolete' =>  This is a backwards compatibility feature of mascot.
     */
    public int getNumberUsed1() {
        return iNumberUsed1;
    }

    /**
     * Sets number of used. Mascot note: 'obsolete' =>  This is a backwards compatibility feature of mascot.
     * This will be -1 by default.
     *
     * @param aNumberUsed1 number of used. Mascot note: 'obsolete' =>  This is a backwards compatibility feature of mascot.
     */
    public void setNumberUsed1(int aNumberUsed1) {
        iNumberUsed1 = aNumberUsed1;
    }

    /**
     * This class will parse al the ion peaks information out of the Query HashMap into a Peak[].
     *
     * @return Peak[] containing all the peaks of the spectrum.
     */
    private Peak[] getPeakArray(HashMap aQueryMap) {
        String lPeakList = (String) aQueryMap.get("Ions1");
        if (lPeakList != null) {
            Peak[] lPeakArray = new Peak[iNumberOfPeaks];
            int lCount = 0;
            //1.get peaklist in a String from the HashMap.
            //2.split all peaks by ',' with a StringTokenizer.
            StringTokenizer st = new StringTokenizer(lPeakList, ",");
            //3.make a new Peak instance from every token (separated by ',')
            while (st.hasMoreTokens()) {
                StringTokenizer tempSt = new StringTokenizer(st.nextToken(), ":");
                //Check if everything goes OK.
                if (tempSt.countTokens() != 2) {
                    throw new IllegalArgumentException("The tempSt should contain just 2 elements (mass:intensity) , now it contains " + tempSt.countTokens() + "elements. ");
                }
                //4.Create a new peak instance with the mass and intensity from tempSt and place it into lPeakArray.
                lPeakArray[lCount] = new Peak(Double.parseDouble(tempSt.nextToken()), Double.parseDouble(tempSt.nextToken()));
                lCount++;
            }
            return lPeakArray;
        } else {
            // Ions1 is empty! This occurs in the 'stripped' mascot result files within the
            // Mascot Distiller project files.
            return null;
        }
    }

    /**
     * Returns the Peak array that includes all the peaks found in the massspectrum.
     *
     * @return the Peak array that includes all the peaks found in the massspectrum.
     *         <p/>
     *         Note that 'null' can be returned if the Ions1 value is empty as in the Mascot Distiller project files.
     */
    public Peak[] getPeakList() {
        return iPeakList;
    }

    public String getFilename() {
        return iTitle;
    }

    /**
     * Sets the Peak array that includes all the peaks found in the massspectrum.
     *
     * @param aPeakList the Peak array that includes all the peaks found in the massspectrum.
     */
    public void setPeakList(Peak[] aPeakList) {
        iPeakList = aPeakList;
    }

    /**
     * Returns identifies the query in the mascot search by its number.
     *
     * @return identifies the query in the mascot search by its number.
     */
    public int getQueryNumber() {
        return iQueryNumber;
    }

    /**
     * Sets identifies the query in the mascot search by its number.
     *
     * @param aQueryNumber identifies the query in the mascot search by its number.
     */
    public void setQueryNumber(int aQueryNumber) {
        iQueryNumber = aQueryNumber;
    }

    /**
     * This method calculates the number of bins mascot has used for this massspectrum.
     * Mascot separates the massspectrum by a series of bins. <br>The first round, the peak with the highest intensity of each bin is used to check for ion matches.
     * <br>The second round, the peak with the second highest intensity of each bin is used to check for ion matches.
     * <br>etc..
     *
     * @return int     The number of bins mascot is using for this massspectrum.
     */
    public int getNumberOfBins() {
        int lCount = 1;
        int lTempInteger;
        for (int i = 0; i < iPeakList.length; i++) {
            if (iPeakList[i].getMZ() < iPeakList[i + 1].getMZ()) {
                lCount++;
            } else {
                break;
            }
        }
        return lCount;
    }


    /**
     * This method returns a filename for the .mgf file custom created by its 'TITLE' value.
     * Parameters from a static Parameters instance are also used.
     *
     * @param aTitle The 'TITLE' value from the .mgf file.
     * @return The filename as created for the given 'TITLE' by the MascotDistillerMergeFileReader.
     */
    private String processMGFTitleToFilename(final String aTitle) {
        boolean lMultiFile = iParameters.isDistillerMultiFile();
        String[] lMultiFileNames = iParameters.getDistillerMultiFileNames();
        return processMGFTitleToFilename(aTitle, lMultiFile, lMultiFileNames, iScans, iCharge);
    }

    /**
     * This method returns a filename for the .mgf file custom created by its 'TITLE' value.
     *
     * @param aTitle          The 'TITLE' value from the .mgf file.
     * @param aMutliFile      Boolean that indicates if we must find the filename in the multifilename vector.
     * @param aMultiFileNames Vector with different filenames for the raw files.
     * @param aScans          The 'SCANS' value from the .mgf file.
     * @param aCharge         The 'CHARGE' value from the .mgf file.
     * @return The filename as created for the given 'TITLE' by the MascotDistillerMergeFileReader.
     */
    public static String processMGFTitleToFilename(String aTitle, boolean aMutliFile, String[] aMultiFileNames, String aScans, String aCharge) {

        // aNumber is not used as this information is also inside the 'TITLE' field.
        // We prefer to use the counter from the 'TITLE' field as this returns identically in
        // the Mascot Result files.

        // Example:
        // a single scan
        // TITLE=704: Scan 1440 (rt=17.7728) [C:\XCalibur\data\data_linda\L59_Bart_Metox_080530A_forward_p2A01.RAW]
        //
        // b summed scan
        // TITLE=705: Sum of 2 scans in range 1441 (rt=17.7785) to 1651 (rt=19.1359) [C:\XCalibur\data\data_linda\L59_Bart_Metox_080530A_forward_p2A01.RAW]
        //
        // c multifile
        // TITLE=1: Sum of 4 scans in range rt=639.562 to rt=658.607 from file [0]
        // The "0" is an index reference to the filename in the multifilename vector
        // SCANS=687 or SCANS=678-687
        //
        // All posibilities to parse
        //
        // From .dat files on the mascot server
        // - single files
        // TITLE=704: Scan 1440 (rt=17.7728) [C:\XCalibur\data\data_linda\L59_Bart_Metox_080530A_forward_p2A01.RAW]
        // TITLE=705: Sum of 2 scans in range 1441 (rt=17.7785) to 1651 (rt=19.1359) [C:\XCalibur\data\data_linda\L59_Bart_Metox_080530A_forward_p2A01.RAW]
        // - multi files
        // TITLE=1: Sum of 4 scans in range rt=639.562 to rt=658.607 from file [0]
        //      We need scans and charge
        //          SCANS=687 or SCANS=678-687
        //          CHARGE=3+
        // TITLE=2: Scan rt=643.3 from file [0]
        //      We need scans and charge
        //          SCANS=4835
        //          CHARGE=3+
        //
        // From .dat files found in the .rov files
        //
        // From Mascot Distiller mergefiles
        // - multifile mergefiles Searched with mascot daemon
        // - multifile mergefiles Searched with mascot distiller
        // TITLE=8: Scan rt=128.78
        // TITLE=37: Sum of 3 scans in range rt=613.745 to rt=628.328
        //      No file link could be found here!!!!!!!!!
        // - singlefile mergefiles Searched with mascot daemon
        // TITLE=122: Scan 6042 (rt=1655.72, p=0, c=1510, e=1) [C:\Users\mascot\Desktop\Julibu\plt proteome\wiff-rohdaten\Czs COFRADIC iTRAQ\QstarE04564.wiff]
        // TITLE=278: Sum of 6 scans in range 6500 (rt=2246.73, p=0, c=1624, e=3) to 6520 (rt=2273.09, p=0, c=1629, e=3) [C:\Users\mascot\Desktop\Julibu\plt proteome\wiff-rohdaten\Czs COFRADIC iTRAQ\QstarE04564.wiff]
        //
        // From the .dat file stored in the .rov file
        //
        // title=122: Scan 6042 (rt=1655.72, p=0, c=1510, e=1)
        // title=278: Sum of 6 scans in range 6500 (rt=2246.73, p=0, c=1624, e=3) to 6520 (rt=2273.09, p=0, c=1629, e=3)
        //
        //

        String lLCRun = null;
        int lCompound = -1;
        int lBeginScan = -1;
        int lEndScan = -1;
        int lSumOfScans = 1;
        int lCharge = -1;

        // a) Parse the Lcrun
        if (aMutliFile) {
            //it's a multifile
            //get the index from "from file [index]"
            String lTemp = aTitle.substring(aTitle.lastIndexOf("[") + 1, aTitle.lastIndexOf("]"));
            if (lTemp.indexOf(",") > 0) {
                //merged spectrum from multiple files [1,7,8]
                lTemp = lTemp.substring(0, lTemp.indexOf(","));
            }
            int lIindex = Integer.valueOf(lTemp);
            String lFileName = aMultiFileNames[lIindex];
            int lStart = lFileName.lastIndexOf("\\") + 1;
            int lEnd = lFileName.lastIndexOf(".");
            lLCRun = lFileName.substring(lStart, lEnd);

        } else {
            //not a multifile, parse the lcrun from the title string
            if (aTitle.lastIndexOf('\\') == -1 || aTitle.lastIndexOf('.') == -1) {
                //lcrun cannot be parsed
                lLCRun = "lcrun";
            } else {
                lLCRun = aTitle.substring(aTitle.lastIndexOf('\\') + 1, aTitle.lastIndexOf('.'));
            }
        }

        if (aTitle.indexOf("TITLE=") == 0) {
            aTitle = aTitle.substring(6);
        }

        // b) Parse the compound number
        lCompound = Integer.valueOf(aTitle.substring(0, aTitle.indexOf(':')));

        // c) Find out the sum of scans
        if (aTitle.indexOf("Sum") >= 0) {
            // c1 Multiple scans from this spectrum!
            if (aMutliFile) {
                //in the multifile title the scans are not there, it has to be parsed from the aScans string
                lBeginScan = Integer.valueOf(aScans.substring(aScans.indexOf("=") + 1, aScans.indexOf("-")));
                lEndScan = Integer.valueOf(aScans.substring(aScans.indexOf("-") + 1));
            } else {
                if (aScans == null) {
                    lBeginScan = Integer.valueOf(aTitle.substring(aTitle.indexOf("range ") + 6, aTitle.indexOf(" (rt=")));
                    lEndScan = Integer.valueOf(aTitle.substring(aTitle.indexOf(") to ") + 5, aTitle.lastIndexOf(" (rt=")));
                } else {
                    lBeginScan = Integer.valueOf(aScans.substring(aScans.indexOf("=") + 1, aScans.indexOf("-")));
                    lEndScan = Integer.valueOf(aScans.substring(aScans.indexOf("-") + 1));
                }
            }
            lSumOfScans = Integer.valueOf(aTitle.substring(aTitle.indexOf("Sum of ") + 7, aTitle.lastIndexOf(" scans ")));
        } else {
            // c2 Single scan form this spectrum!
            if (aMutliFile) {
                //in the multifile title the scan is not there, it has to be parsed from the aScans string
                lBeginScan = Integer.valueOf(aScans.substring(aScans.indexOf("=") + 1));
            } else {
                if (aScans == null) {
                    lBeginScan = Integer.valueOf(aTitle.substring(aTitle.indexOf("Scan ") + 5, aTitle.indexOf(" (rt=")));
                } else {
                    lBeginScan = Integer.valueOf(aScans.substring(aScans.indexOf("=") + 1));
                }
            }
        }

        //d) parse the charge
        lCharge = Integer.valueOf(aCharge.substring(0, 1));

        String lResult = "";
        if (lSumOfScans == 1) {
            // Single scan
            lResult = lLCRun + "_" + lCompound + "_" + lBeginScan + "_" + lSumOfScans + "_" + lCharge + ".mgf";
        } else if (lSumOfScans > 1) {
            // Summed scan
            lResult = lLCRun + "_" + lCompound + "_" + lBeginScan + "." + lEndScan + "_" + lSumOfScans + "_" + lCharge + ".mgf";
        }
        return lResult;
    }

    /**
     * ToString method for the Query object.
     *
     * @return String Text representation of the object.
     */
    public String toString() {
        String s = null;
        if (iQueryNumber != 0) {
            s = "Query " + iQueryNumber + " from spectrum: " + iTitle;
        } else {
            s = "Invalid QueryNumber.";
        }
        return s;
    }
}
