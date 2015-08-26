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

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 24-feb-2006
 * Time: 12:47:30
 */

/**
 * This class contains all the parsed data from the 'header' section of the datfile.
 */
public class Header implements Serializable {
    // Class specific log4j logger for Header instances.
    private static Logger logger = Logger.getLogger(Header.class);
    /**
     * Number of sequences in the database.
     */
    private long iSequences = 0;
    /**
     * Number of sequences after taxonomy filter.
     */
    private long iSequences_after_tax = 0;
    /**
     * Number of residues in the DB.
     */
    private long iResidues = 0;
    /**
     * This is a ',' seperated list of values that represent a histogram of the complete protein score distribution.
     * Only meaningfull for a PMF search.
     */
    private String iDistribution = null;
    /**
     * Search time in seconds.
     */
    private long iExecutionTime = 0;
    /**
     * Date when the search was done.
     */
    private long iDate = 0;
    /**
     * Time that the search was requested.
     */
    private String iTime = null;
    /**
     * Number of queries done.
     */
    private int iQueries = 0;
    /**
     * Maximum number of hits that should be listed in the datfile.
     */
    private int iMaxHits = 0;
    /**
     * The Mascot version used to generate the dat file.
     */
    private String iVersion = null;
    /**
     * Filename of the actual database. (ex: SP_human_20060207.fasta)
     */
    private String iRelease = null;
    /**
     * Unique task identifier for searches submitted asynchronously.
     */
    private String iTaskID = null;
    /**
     * This is a String[] with warnings from Mascot.
     */
    private ArrayList iWarnings = null;

    public Header(HashMap h) {
        //parse all the key-values into instance variables.
        iSequences = Long.parseLong((String) h.get("sequences"));
        iSequences_after_tax = Long.parseLong((String) h.get("sequences_after_tax"));
        iResidues = Long.parseLong((String) h.get("residues"));
        iDistribution = (String) h.get("distribution");
        iExecutionTime = Long.parseLong((String) h.get("exec_time"));
        iDate = Long.parseLong((String) h.get("date"));
        iTime = (String) h.get("time");
        iQueries = Integer.parseInt((String) h.get("queries"));
        iMaxHits = Integer.parseInt((String) h.get("max_hits"));
        iVersion = (String) h.get("version");
        iRelease = (String) h.get("release");
        iTaskID = (String) h.get("taskid");
        iWarnings = getWarnings(h);
    }

    private ArrayList getWarnings(HashMap h) {
        ArrayList lWarnings = new ArrayList(1);
        int index = 0;
        while (h.get("Warn" + index) != null) {
            String s = (String) h.get("Warn" + index);
            lWarnings.add(s);
            index++;
        }
        return lWarnings;
    }

    /**
     * Number of sequences in the database.
     *
     * @return number of sequences in the database
     */
    public long getSequences() {
        return iSequences;
    }

    /**
     * Number of sequences in the database.
     *
     * @param aSequences the number of sequences in the database
     */
    public void setSequences(int aSequences) {
        iSequences = aSequences;
    }

    /**
     * Number of sequences after taxonomy filter.
     *
     * @return number of sequences after taxonomy filter
     */
    public long getSequences_after_tax() {
        return iSequences_after_tax;
    }

    /**
     * Number of sequences after taxonomy filter.
     *
     * @param aSequences_after_tax number of sequences after taxonomy filter
     */
    public void setSequences_after_tax(int aSequences_after_tax) {
        iSequences_after_tax = aSequences_after_tax;
    }

    /**
     * Number of residues in the DB.
     *
     * @return number of residues in the DB
     */
    public long getResidues() {
        return iResidues;
    }

    /**
     * Number of residues in the DB.
     *
     * @param aResidues number of residues in the DB
     */
    public void setResidues(int aResidues) {
        iResidues = aResidues;
    }

    /**
     * This is a ',' separated list of values that represent a histogram of the complete protein score distribution.
     * Only meaningful for a PMF search.
     *
     * @return list of values that represent a histogram of the complete protein score distribution
     */
    public String getDistribution() {
        return iDistribution;
    }

    /**
     * This is a ',' separated list of values that represent a histogram of the complete protein score distribution.
     * Only meaningful for a PMF search.
     *
     * @param aDistribution list of values that represent a histogram of the complete protein score distribution
     */
    public void setDistribution(String aDistribution) {
        iDistribution = aDistribution;
    }

    /**
     * Search time in seconds.
     *
     * @return search time in seconds
     */
    public long getExecutionTime() {
        return iExecutionTime;
    }

    /**
     * Search time in seconds.
     *
     * @param aExecutionTime search time in seconds
     */
    public void setExecutionTime(int aExecutionTime) {
        iExecutionTime = aExecutionTime;
    }

    /**
     * Date when the search was done.
     *
     * @return date when the search was done
     */
    public long getDate() {
        return iDate;
    }

    /**
     * Date when the search was done.
     *
     * @param aDate date when the search was done
     */
    public void setDate(int aDate) {
        iDate = aDate;
    }

    /**
     * Time that the search was requested.
     *
     * @return time that the search was requested
     */
    public String getTime() {
        return iTime;
    }

    /**
     * Time that the search was requested.
     *
     * @param aTime time that the search was requested
     */
    public void setTime(String aTime) {
        iTime = aTime;
    }

    /**
     * Number of queries done.
     *
     * @return number of queries done
     */
    public int getQueries() {
        return iQueries;
    }

    /**
     * Number of queries done.
     *
     * @param aQueries number of queries done
     */
    public void setQueries(int aQueries) {
        iQueries = aQueries;
    }

    /**
     * Maximum number of hits that should be listed in the datfile.
     *
     * @return maximum number of hits
     */
    public int getMaxHits() {
        return iMaxHits;
    }

    /**
     * Maximum number of hits that should be listed in the datfile.
     *
     * @param aMaxHits maximum number of hits
     */
    public void setMaxHits(int aMaxHits) {
        iMaxHits = aMaxHits;
    }

    /**
     * Returns the Mascot version.
     *
     * @return the Mascot version
     */
    public String getVersion() {
        return iVersion;
    }

    /**
     * Set the Mascot version.
     *
     * @param aVersion the Mascot version
     */
    public void setVersion(String aVersion) {
        iVersion = aVersion;
    }

    /**
     * Filename of the actual database. (ex: SP_human_20060207.fasta)
     *
     * @return filename of the actual database
     */
    public String getRelease() {
        return iRelease;
    }

    /**
     * Filename of the actual database. (ex: SP_human_20060207.fasta)
     *
     * @param aRelease filename of the actual database
     */
    public void setRelease(String aRelease) {
        iRelease = aRelease;
    }

    /**
     * Unique task identifier for searches submitted asynchronously.
     *
     * @return unique task identifier
     */
    public String getTaskID() {
        return iTaskID;
    }

    /**
     * Unique task identifier for searches submitted asynchronously.
     *
     * @param aTaskID unique task identifier
     */
    public void setTaskID(String aTaskID) {
        iTaskID = aTaskID;
    }

    /**
     * This is a String[] with warnings from Mascot.
     *
     * @return the warnings
     */
    public ArrayList getWarnings() {
        return iWarnings;
    }

    /**
     * This is a String[] with warnings from Mascot.
     *
     * @param aWarnings the warnings
     */
    public void setWarnings(ArrayList aWarnings) {
        iWarnings = aWarnings;
    }
}
