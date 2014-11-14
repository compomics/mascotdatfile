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

import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 26-feb-2006
 * Time: 16:52:10
 */

/**
 * This Class creates a map with all the peptide hits in 2 dimensions. The first level holds the results of the queries
 * in a hashmap (Key: Querynumber  Value:Vector with PeptideHits). The second dimension holds a Vector with the
 * corresponding peptide hits of the query.
 */
public class QueryToPeptideMap implements QueryToPeptideMapInf {
    // Class specific log4j logger for QueryToPeptideMap instances.
    private static Logger logger = Logger.getLogger(QueryToPeptideMap.class);
// ------------------------------ FIELDS ------------------------------

    /**
     * a 2-dimensional HashMap. The first dimension holds the results of the queries in a hashmap. The second dimension
     * holds a vector with the corresponding peptide hits of the query.
     */
    private HashMap iPeptideMap = null;
    /**
     * The average threshold values of the mascot search.
     */
    private double[] iAverageQueryThresholds = null;
    /**
     * The number of queries in the mascot search.
     */
    private int iNumberOfQueries = 0;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructor for creating a new QueryToPeptideMap.
     *
     * @param aPeptidesSection HashMap m with the Section peptides.
     * @param aNumberOfQueries Number of getQueryList done.
     * @param aProteinMap      ProteinMap is a structured version of the ProteinSection.
     * @param aMod             ModificationList to create a different Modification[] for each PeptideHit.
     * @param aThreshold       2-Dimensional int[] with treshold values. int[i][j] with i+1 the Querynumber and j=0
     *                         homology and j=1 identity score treshold.
     */
    public QueryToPeptideMap(HashMap aPeptidesSection, ProteinMap aProteinMap, int aNumberOfQueries, ModificationList aMod, double[][] aThreshold) {
        iNumberOfQueries = aNumberOfQueries;
        iPeptideMap = generatePeptideMap(aPeptidesSection, aProteinMap, aNumberOfQueries, aMod, aThreshold);
    }

    /**
     * Constructor for creating a new QueryToPeptideMap.
     *
     * @param aPeptidesSection    HashMap m with the Section peptides.
     * @param aETSPeptidesSection HashMap m with the Section et_peptides.
     * @param aNumberOfQueries    Number of getQueryList done.
     * @param aProteinMap         ProteinMap is a structured version of the ProteinSection.
     * @param aModificationList   ModificationList to create a different Modification[] for each PeptideHit.
     * @param aThresholds         2-Dimensional int[] with treshold values. int[i][j] with i+1 the Querynumber and j=0
     *                            homology and j=1 identity score treshold.
     */

    public QueryToPeptideMap(final HashMap aPeptidesSection, final HashMap aETSPeptidesSection, final ProteinMap aProteinMap, final int aNumberOfQueries, final ModificationList aModificationList, final double[][] aThresholds) {
        iNumberOfQueries = aNumberOfQueries;
        iPeptideMap = generatePeptideMap(aPeptidesSection, aETSPeptidesSection, aProteinMap, aNumberOfQueries, aModificationList, aThresholds);
    }

    /**
     * This method creates the PeptideMap
     *
     * @param aPeptidesSection    HashMap m with the Section peptides.
     * @param aETSPeptidesSection HashMap m with the Section et_peptides.
     * @param aNumberOfQueries    Number of getQueryList done.
     * @param aProteinMap         ProteinMap is a structured version of the ProteinSection.
     * @param aModificationList   ModificationList to create a different Modification[] for each PeptideHit.
     * @return HashMap          a 2dimensional hashmap (String,Vector(PeptideHits)). the first key is q1 and it goes
     *         untill qx where x=aNumberOfQueries.
     */
    private HashMap generatePeptideMap(final HashMap aPeptidesSection, final HashMap aETSPeptidesSection, final ProteinMap aProteinMap, final int aNumberOfQueries, final ModificationList aModificationList, final double[][] aThresholds) {
        // 1. Generate the HashMap on the first level(Key: query number(ex:q1) - Value: Vector of PeptideHit instances.)
        HashMap lFirstDimension = new HashMap(aNumberOfQueries);
        if (aPeptidesSection != null && aETSPeptidesSection != null) {
            for (int i = 1; i <= aNumberOfQueries; i++) {
                // 2.Create a Vector on the second level(ex: Vector.get(0) returns the first PeptideHit of the requested query.)
                Vector lSecondDimension = new Vector();

                //2.a) Counter for the number of PeptideHits
                int lCount = 1;

                //2.b) Get homology and identity score of this Query to pass to the creation of a peptidehit.
                //     lThreshold[0] is the homology threshold, lThreshold[1] is the identity threshold.
                double[] lThreshold = getQueryThresholdValues(i, aThresholds, aNumberOfQueries);

                //2.c) As long as there are PeptideHits returning, generate new PeptideHit Instances.
                while ((aPeptidesSection.get("q" + i + "_p" + lCount) != null)) {
                    // put a new Key(String: ex 'p1') in the lSecondDimension HashMap, it is
                    // corresponding with an instance of PeptideHit by calling its static method.
                    // The String for that static method is created with the 'i' and 'lCount' looping variables.

                    // The NON-error tolerant hit:
                    String lNonETS_PeptideHitString = (String) aPeptidesSection.get("q" + i + "_p" + lCount);
                    PeptideHit lNonETS_PeptideHit = null;
                    // If not empty, create a peptidehit.
                    if (!lNonETS_PeptideHitString.equals("-1")) {
                        lNonETS_PeptideHit = PeptideHit.parsePeptideHit(lNonETS_PeptideHitString, aProteinMap, aModificationList, lThreshold);
                    }

                    String lETSPeptideHitString = (String) aETSPeptidesSection.get("q" + i + "_p" + lCount);
                    PeptideHit lETS_PeptideHit = null;
                    if (lETSPeptideHitString != null && !lETSPeptideHitString.equals("-1")) {
                        String lETSPeptideHitMods = (String) aETSPeptidesSection.get("q" + i + "_p" + lCount + "_et_mods");
                        lETS_PeptideHit = PeptideHit.parsePeptideHit(lETSPeptideHitString, lETSPeptideHitMods, aProteinMap, aModificationList, lThreshold);
                    }

                    if (lNonETS_PeptideHit != null || lETS_PeptideHit != null) {
                        // There is at least one 'winning' PeptideHit to store.
                        PeptideHit lStrongestPeptideHit = null;

                        if (lETS_PeptideHit == null) {
                            // The ETS peptidehit is non-existing, the Non-ETS peptidehit wins.
                            lStrongestPeptideHit = lNonETS_PeptideHit;
                        } else if (lNonETS_PeptideHit == null) {
                            // The Non-ETS peptidehit is non-existing, the ETS peptidehit wins.
                            lStrongestPeptideHit = lETS_PeptideHit;
                        } else {
                            // Both the ETS and Non-ETS peptidehit exist.
                            // The highest ionscore wins!
                            if (lETS_PeptideHit.getIonsScore() >= lNonETS_PeptideHit.getIonsScore()) {
                                lStrongestPeptideHit = lETS_PeptideHit;
                                //System.out.println("Constructed ETS peptidehit " + lStrongestPeptideHit.getModifiedSequence());
                            } else {
                                lStrongestPeptideHit = lNonETS_PeptideHit;
                            }
                        }

                        // Persist into the hashmap.
                        lSecondDimension.add(lStrongestPeptideHit);

                        // Update the PeptideHit in the ProteinMap.
                        lStrongestPeptideHit = (PeptideHit) lSecondDimension.get(lCount - 1);
                        int lNumberOfProteinHits = lStrongestPeptideHit.getProteinHits().size();
                        for (int k = 0; k < lNumberOfProteinHits; k++) {
                            ProteinHit lProteinHit = (ProteinHit) lStrongestPeptideHit.getProteinHits().get(k);
                            aProteinMap.addProteinSource(lProteinHit.getAccession(), i, lCount);
                        }
                    }

                    lCount++;
                }
                // If no PeptideHits were added to the Vector, it makes no sense to have a vector with 0 PeptideHits.
                // Set SecondDimension to null.
                if (lSecondDimension.size() == 0) {
                    lSecondDimension = null;
                }

                // 3. Put a new Key(String: ex 'q1') the lFirstDimension HashMap, it is
                //    corresponding with a SecondDimension Vector containing PeptideHit values.
                lFirstDimension.put("q" + (i), lSecondDimension);
            }
        }
        return lFirstDimension;
    }


    /**
     * This method creates the PeptideMap
     *
     * @param m                HashMap with the peptideSection
     * @param aProteinMap      ProteinMap is a structured version of the ProteinSection.
     * @param aNumberOfQueries Number of getQueryList done in the Mascot search.
     * @param aMod             Modificationlist to create a different Modification[] for each PeptideHit.
     * @return HashMap          a 2dimensional hashmap (String,Vector(PeptideHits)). the first key is q1 and it goes
     *         untill qx where x=aNumberOfQueries.
     */
    private HashMap generatePeptideMap(HashMap m, ProteinMap aProteinMap, int aNumberOfQueries, ModificationList aMod, double[][] aThreshold) {
        // 1. Generate the HashMap on the first level(Key: query number(ex:q1) - Value: Vector of PeptideHit instances.)
        HashMap lFirstDimension = new HashMap(aNumberOfQueries);
        if (m != null) {
            for (int i = 1; i <= aNumberOfQueries; i++) {
                // 2.Create a Vector on the second level(ex: Vector.get(0) returns the first PeptideHit of the requested query.)
                Vector lSecondDimension = new Vector();
                Pattern findMoreThanOneVariable = Pattern.compile(".*X.*X.*");
                //2.a) Counter for the number of PeptideHits
                int lCount = 1;
                int lCountBackwards = 1;
                //2.b) Get homology and identity score of this Query to pass to the creation of a peptidehit.
                //     lThreshold[0] is the homology threshold, lThreshold[1] is the identity threshold.
                double[] lThreshold = getQueryThresholdValues(i, aThreshold, aNumberOfQueries);
                //2.c) As long as there are PeptideHits returning, generate new PeptideHit Instances.

                while ((m.get("q" + i + "_p" + lCount) != null) && !(m.get("q" + i + "_p" + lCount).equals("-1"))) {
                    // put a new Key(String: ex 'p1') in the lSecondDimension HashMap, it is
                    // corresponding with an instance of PeptideHit by calling its static method.
                    // The String for that static method is created with the 'i' and 'lCount' looping variables.
                    if ((m.get("q" + i + "_p" + lCount+"_subst"))!= null){
                        lSecondDimension.add(PeptideHit.parsePeptideHit((String) m.get("q" + i + "_p" + lCount), aProteinMap, aMod, lThreshold,(String)m.get("q" + i + "_p" + lCount+"_subst")));
                    } else  {
                    lSecondDimension.add(PeptideHit.parsePeptideHit((String) m.get("q" + i + "_p" + lCount), aProteinMap, aMod, lThreshold));
                    }
                    // Update the PeptideHit in the ProteinMap.
                        PeptideHit lPeptideHit = (PeptideHit) lSecondDimension.get(lCount - lCountBackwards);
                    int lNumberOfProteinHits = lPeptideHit.getProteinHits().size();
                    for (int k = 0; k < lNumberOfProteinHits; k++) {
                        ProteinHit lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(k);
                        aProteinMap.addProteinSource(lProteinHit.getAccession(), i, lCount);
                    }
                    lCount++;
                }
                // If no PeptideHits were added to the Vector, it makes no sense to have a vector with 0 PeptideHits.
                // Set SecondDimension to null.
                if (lSecondDimension.size() == 0) {
                    lSecondDimension = null;
                }

                // 3. Put a new Key(String: ex 'q1') the lFirstDimension HashMap, it is
                //    corresponding with a SecondDimension Vector containing PeptideHit values.
                lFirstDimension.put("q" + (i), lSecondDimension);
            }
        }
        return lFirstDimension;
    }

    /**
     * Method puts the Qmatch and Plughole values for the Query in a double array. If the values are = 0, the average
     * Qmatch and Plughole values are put in the array.
     *
     * @param aQueryNumber     The thresholds of this query.
     * @param aThreshold       All the thresholds, to calculate the average if needed.
     * @param aNumberOfQueries The total number of queries to calculate the average.
     * @return double[]        QueryThresholds
     */
    private double[] getQueryThresholdValues(int aQueryNumber, double[][] aThreshold, int aNumberOfQueries) {
        //2 local variables to hold the identity and homology Query related thresholds.
        double lIdentityThreshold = 0;
        double lHomologyThreshold = 0;
        // if identity threshold smaller then 4, the threshold will be unacceptable low. So dont create the threshold with
        // this low idenit threshold, create it with AverageQueryThreshold!

        if (aThreshold[aQueryNumber - 1][1] < 4) {
            // if the average is not calculated yet, do it now. Also set the homology with the average value.
            if (iAverageQueryThresholds == null) {
                // initiate the double array.
                iAverageQueryThresholds = new double[2];
                // create 2 local variables.
                double lSumOfHomology = 0;
                double lSumOfIdentity = 0;
                // count all the values of every query.
                // counter to sum all the "0" thresholds, these should not be used for the average!
                int count = 0;
                for (int i = 0; i < aNumberOfQueries; i++) {
                    if (aThreshold[i][0] == 0 || aThreshold[i][1] == 0) {
                        count++;
                        continue;
                    }
                    lSumOfHomology += aThreshold[i][0];
                    lSumOfIdentity += aThreshold[i][1];
                }
                iAverageQueryThresholds[0] = (lSumOfHomology / (aNumberOfQueries - count));
                iAverageQueryThresholds[1] = (lSumOfIdentity / (aNumberOfQueries - count));
            }
            lHomologyThreshold = iAverageQueryThresholds[0];
            lIdentityThreshold = iAverageQueryThresholds[1];
        } else {
            lHomologyThreshold = aThreshold[aQueryNumber - 1][0];
            lIdentityThreshold = aThreshold[aQueryNumber - 1][1];
        }
        //Return the double array with the fitting
        return new double[]{lHomologyThreshold, lIdentityThreshold};
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Returns the number of queries in the mascot search.
     *
     * @return the number of queries in the mascot search.
     */
    public int getNumberOfQueries() {
        return iNumberOfQueries;
    }

    /**
     * Returns a 2-dimensional HashMap. The first dimension holds the results of the queries in a hashmap. The second
     * dimension holds a hashmap with the corresponding peptide hits of the query. If there are no peptidehits from a
     * query, a <b>nullpointer</b> is inside the HashMap!!
     *
     * @return a 2-dimensional HashMap.
     */
    public HashMap getPeptideMap() {
        return iPeptideMap;
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface QueryToPeptideMapInf ---------------------


    /**
     * This method returns a PeptideHit of the QueryNumber that is given by two parameters.
     *
     * @param aQueryNumber      Requested query by number! <br>45 as parameter will return the <i>aPeptideHitsNumber</i>
     *                          PeptideHit of Query 45.
     * @param aPeptideHitNumber The PeptideHit in the requested Query. The best peptideHit is peptidehit 1.
     * @return PeptideHit       returns the requested PeptideHit of the requested QueryNumber. <br>Returns PeptideHit =
     *         null if PeptideHit is null.
     */
    public PeptideHit getPeptideHitOfOneQuery(int aQueryNumber, int aPeptideHitNumber) {
        PeptideHit ph = null;
        Vector lPeptideHits = getAllPeptideHits(aQueryNumber);
        if (lPeptideHits != null) {
            if (aPeptideHitNumber - 1 < lPeptideHits.size()) {
                if (lPeptideHits.get(aPeptideHitNumber - 1) != null) {
                    ph = (PeptideHit) lPeptideHits.get(aPeptideHitNumber - 1);
                }
            }
        }
        return ph;
    }

    /**
     * Method The requested query in a Vector with PeptideHit instances.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will return all the PeptideHits of Query 45.
     * @return Vector       Vector containing the PeptideHits of the requested query, <br>'null' if the query number was
     *         not found or no identifications were made from the query.
     */
    public Vector getAllPeptideHits(int aQueryNumber) {
        return (Vector) iPeptideMap.get("q" + aQueryNumber);
    }

    /**
     * Method The number of PeptideHits in the requested querynumber.
     *
     * @param aQueryNumber int       The querynumber you want to get the amount of PeptideHits from.
     * @return size             int       The number of peptidehits in the query, or 0 if the querynumber does not
     *         exist. <b>Remark:</b> If there are no peptidehits in the query(ex: 'q1_p1 = -1'), a PeptideHit with value
     *         = null is created. So if the querynumber exists it will always have at least 1 PeptideHit. This will
     *         cause a NullPointerException if you try to acces such a null_PeptideHit.
     */
    public int getNumberOfPeptideHits(int aQueryNumber) {
        Vector v = (Vector) iPeptideMap.get("q" + aQueryNumber);
        int size = 0;
        if (v != null) {
            size = v.size();
        }
        return size;
    }

    /**
     * This method returns the best PeptideHit of each Query in a Vector. Vector.get(5) returns the best PeptideHit of
     * Query 6 (5+1).
     *
     * @return Vector with all the best PeptideHits next to their QueryNumber. Vector[0] contains info of Query 1.
     *         Vector[999] contains info of Query 1000.
     */
    public Vector getBestPeptideHits() {
        return getPeptideHits(1);
    }

    /**
     * This method returns the PeptideHits of one number of each query in a Vector.
     *
     * @param aPeptideHitNumber The PeptideHit in the requested Query. The best peptideHit is peptidehit 1.
     * @return Vector with PeptideHit (aPeptideHitNumber) of every query. Vector[0] contains info of Query 1. Vector[999]
     *         contains info of Query 1000.
     */
    public Vector getPeptideHits(int aPeptideHitNumber) {
        Vector lBestPeptideHits = new Vector(iNumberOfQueries);
        for (int i = 1; i <= iNumberOfQueries; i++) {
            lBestPeptideHits.add(getPeptideHitOfOneQuery(i, aPeptideHitNumber));
        }
        return lBestPeptideHits;
    }

    /**
     * This method returns the best PeptideHit(P1) of the QueryNumber that is given by a parameter.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will return the best PeptideHit of Query 45.
     * @return PeptideHit      returns the best PeptideHit of the requested QueryNumber. returns PeptideHit = null if
     *         PeptideHit is null.
     */
    public PeptideHit getPeptideHitOfOneQuery(int aQueryNumber) {
        PeptideHit ph = null;
        Vector lPeptideHits = getAllPeptideHits(aQueryNumber);
        if (lPeptideHits != null) {
            ph = (PeptideHit) lPeptideHits.get(0);
        }
        return ph;
    }

    /**
     * This method calculates all the peptidehits that are above the default (alpha=0.05) identity threshold.
     *
     * @return Vector  vector with all the PeptideHit instances above identity threshold.
     */
    public Vector getAllPeptideHitsAboveIdentityThreshold() {
        return this.getAllPeptideHitsAboveIdentityThreshold(0.05);
    }

    /**
     * This method calculates all the peptidehits that are above the default identity threshold.
     *
     * @param aConfidence alpha confidence of the returning peptidehits.
     * @return Vector  vector with all the PeptideHit instances above identity threshold.
     */
    public Vector getAllPeptideHitsAboveIdentityThreshold(double aConfidence) {
        Vector lPeptideHitsAboveThreshold = new Vector();
        Vector lAllBestPeptideHits = getBestPeptideHits();
        for (int i = 0; i < lAllBestPeptideHits.size(); i++) {
            PeptideHit temp = (PeptideHit) lAllBestPeptideHits.get(i);
            if (temp != null) {
                if (temp.scoresAboveIdentityThreshold(aConfidence)) {
                    lPeptideHitsAboveThreshold.add(temp);
                }
            }
        }
        return lPeptideHitsAboveThreshold;
    }

    /**
     * This method returns all the peptidehits above 95% threshold from one specified query.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will return all the PeptideHits of Query 45
     *                     above their 95% identity threshold.
     * @return Vector       Vector with peptidehits above threshold.
     */
    public Vector getPeptideHitsAboveIdentityThreshold(int aQueryNumber) {
        return getPeptideHitsAboveIdentityThreshold(aQueryNumber, 0.05);
    }

    /**
     * This method returns all the peptidehits above a parametrical threshold from one specified query.
     *
     * @param aQueryNumber        Requested query by number! <br>45 as parameter will return all the PeptideHits of
     *                            Query 45.
     * @param aConfidenceInterval The confidence the identification has to match, this value equals alpha. So for a 90%
     *                            confidence alpha is 0.10.
     * @return Vector       Vector with peptidehits above threshold.
     */
    public Vector getPeptideHitsAboveIdentityThreshold(int aQueryNumber, double aConfidenceInterval) {
        Vector lPeptideHits = getAllPeptideHits(aQueryNumber);
        Vector lResult = new Vector();
        if (lPeptideHits != null) {
            for (int i = 0; i < lPeptideHits.size(); i++) {
                PeptideHit lPeptideHit = (PeptideHit) lPeptideHits.elementAt(i);
                if (lPeptideHit.scoresAboveIdentityThreshold(aConfidenceInterval)) {
                    lResult.add(lPeptideHit);
                }
            }
        }
        return lResult;
    }

    /**
     * This method returns a Vector with Query's that lead to a identification above a certain confidence (alpha).
     *
     * @param aConfidence        double confidence alpha above wich an identification has to score.
     * @param aCompleteQueryList Vector with <b>the complete querylist of the datfile</b>.
     * @return Vector queries with an identification above a certain confidence (alpha).
     */
    public List getIdentifiedQueries(double aConfidence, List<Query> aCompleteQueryList) {
        // All the identified Queries will be stored here.
        List lIdentifiedQueries = new ArrayList();
        // The temporary peptidehits will be stored here, if the size is bigger then 0, the Query will be stored.
        // Mind that the getPeptideHitsAboveIdentityThreshold() method takes the querynumber as input (45 leads to query45)
        // And that the QueryList is a Vector starting from zero! (45 leads to query44)
        List lTempPeptideHits = new ArrayList();
        for (int i = 0; i < aCompleteQueryList.size(); i++) {
            int lQueryNumber = i + 1;
            lTempPeptideHits = getPeptideHitsAboveIdentityThreshold(lQueryNumber, aConfidence);
            if (lTempPeptideHits.size() > 0) {
                lIdentifiedQueries.add(aCompleteQueryList.get(i));
            }
        }
        return lIdentifiedQueries;
    }

    public void buildProteinMap() {
        // Is build upon construction of the proteinmap, do nothing!
    }
}

