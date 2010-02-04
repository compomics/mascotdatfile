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

package com.computationalomics.mascotdatfile.util.mascot.index;

import com.computationalomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.computationalomics.mascotdatfile.util.mascot.ModificationList;
import com.computationalomics.mascotdatfile.util.mascot.PeptideHit;
import com.computationalomics.mascotdatfile.util.mascot.ProteinHit;
import com.computationalomics.mascotdatfile.util.mascot.ProteinMap;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 26-feb-2006
 * Time: 16:52:10
 */

/**
 * This Class creates a map with all the peptide hits in 2 dimensions.
 * The first level holds the results of the queries in a hashmap (Key: Querynumber  Value:Vector with PeptideHits).
 * The second dimension holds a Vector with the corresponding peptide hits of the query.
 */
public class QueryToPeptideMap_Index implements QueryToPeptideMapInf {

    /*
     * The number of queries in the mascot search.
     */
    protected int iNumberOfQueries = 0;

    protected Controller iController = null;

    protected ProteinMap iProteinMap = null;

    protected ModificationList iModificationList = null;
    private String lQMatchString;
    private String lQPlugholeString;

    /**
     * Constructor for creating a new QueryToPeptideMap.
     *
     * @param aProteinMap      ProteinMap is a structured version of the ProteinSection.
     * @param aModificationList             ModificationList to create a different Modification[] for each PeptideHit.
     */
    public QueryToPeptideMap_Index(Controller aController, ProteinMap aProteinMap, ModificationList aModificationList) {

        iController = aController;
        iNumberOfQueries = aController.getNumberOfQueries();
        iModificationList = aModificationList;
        iProteinMap = aProteinMap;
    }

    /**
     * This method returns a PeptideHit of the QueryNumber that is given by two parameters.
     *
     * @param aQueryNumber      Requested query by number! <br>45 as parameter will return the <i>aPeptideHitsNumber</i> PeptideHit of Query 45.
     * @param aPeptideHitNumber The PeptideHit in the requested Query. The best peptideHit is peptidehit 1.
     * @return PeptideHit       returns the requested PeptideHit of the requested QueryNumber. <br>Returns PeptideHit = null if PeptideHit is null.
     */
    public PeptideHit getPeptideHitOfOneQuery(int aQueryNumber, int aPeptideHitNumber) {
        PeptideHit lPeptideHit = null;

        int lCount = 0;

        //2.b) Get homology and identity score of this Query to pass to the creation of a peptidehit.
        //     lThreshold[0] is the homology threshold, lThreshold[1] is the identity threshold.
        lQMatchString = iController.readSummary(aQueryNumber, SummaryIndex.QMATCH);
        double lQmatch = Double.parseDouble(lQMatchString);

        lQPlugholeString = iController.readSummary(aQueryNumber, SummaryIndex.QPLUGHOLE);
        double lQplughole = Double.parseDouble(lQPlugholeString);

                //2.c) As long as there are PeptideHits returning, generate new PeptideHit Instances.
        String s = iController.readPeptideHit(aQueryNumber, aPeptideHitNumber);

        if (s != null) {
            lPeptideHit = PeptideHit.parsePeptideHit(s, iProteinMap, iModificationList, new double[]{lQplughole, lQmatch});
         }

        return lPeptideHit;
    }

    /**
     * Method
     * The requested query in a Vector with PeptideHit instances.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will return all the PeptideHits of Query 45.
     * @return Vector       Vector containing the PeptideHits of the requested query, <br>'null' if the query number
     *         was not found or no identifications were made from the query.
     */
    public Vector getAllPeptideHits(int aQueryNumber) {
        Vector v = null;
        int iNumberOfPeptideHits = getNumberOfPeptideHits(aQueryNumber);

        for (int i = 0; i < iNumberOfPeptideHits; i++) {
            PeptideHit lPeptideHit = getPeptideHitOfOneQuery(aQueryNumber, (i+1));
            if(lPeptideHit!= null){
                if (i == 0) {
                    v = new Vector();
                }
                v.add(lPeptideHit);
            }
        }
        return v;
    }

    /**
     * Method
     * The number of PeptideHits in the requested querynumber.
     *
     * @param aQueryNumber int       The querynumber you want to get the amount of PeptideHits from.
     * @return size             int       The number of peptidehits in the query, or 0 if the querynumber does not exist.
     *         <b>Remark:</b> If there are no peptidehits in the query(ex: 'q1_p1 = -1'), a PeptideHit with value = null is created. So if the querynumber exists it will always have at least 1 PeptideHit. This will cause a NullPointerException if you try to acces such a null_PeptideHit.
     */
    public int getNumberOfPeptideHits(int aQueryNumber) {
        return iController.getNumberOfPeptideHits(aQueryNumber);
    }



    /**
     * Returns the number of queries in the mascot search.
     *
     * @return the number of queries in the mascot search.
     */
    public int getNumberOfQueries() {
        return iNumberOfQueries;
    }

    /**
     * This method returns the best PeptideHit of each Query in a Vector.
     * Vector.get(5) returns the best PeptideHit of Query 6 (5+1).
     *
     * @return Vector with all the best PeptideHits next to their QueryNumber. Vector[0] contains info of Query 1. Vector[999] contains info of Query 1000.
     */
    public Vector getBestPeptideHits() {
        return getPeptideHits(1);
    }

    /**
     * This method returns the PeptideHits of one number of each query in a Vector.
     *
     * @param aPeptideHitNumber The PeptideHit in the requested Query. The best peptideHit is peptidehit 1.
     * @return Vector with PeptideHit<aPeptideHitNumber> of every query. Vector[0] contains info of Query 1. Vector[999] contains info of Query 1000.
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
     * @return PeptideHit      returns the best PeptideHit of the requested QueryNumber.
     *         returns PeptideHit = null if PeptideHit is null.
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
     * @param aQueryNumber Requested query by number! <br>45 as parameter will return all the PeptideHits of Query 45 above their 95% identity threshold.
     * @return Vector       Vector with peptidehits above threshold.
     */
    public Vector getPeptideHitsAboveIdentityThreshold(int aQueryNumber) {
        return getPeptideHitsAboveIdentityThreshold(aQueryNumber, 0.05);
    }

    /**
     * This method returns all the peptidehits above a parametrical threshold from one specified query.
     *
     * @param aQueryNumber        Requested query by number! <br>45 as parameter will return all the PeptideHits of Query 45.
     * @param aConfidenceInterval The confidence the identification has to match, this value equals alpha. So for a 90% confidence alpha is 0.10.
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
     * @param aCompleteQueryList Vector with all queries.
     * @return Vector queries with an identification above a certain confidence (alpha).
     */
    public Vector getIdentifiedQueries(double aConfidence, Vector aCompleteQueryList) {
        // All the identified Queries will be stored here.
        Vector lIdentifiedQueries = new Vector();
        // The temporary peptidehits will be stored here, if the size is bigger then 0, the Query will be stored.
        // Mind that the getPeptideHitsAboveIdentityThreshold() method takes the querynumber as input (45 leads to query45)
        // And that the QueryList is a Vector starting from zero! (45 leads to query44)
        Vector lTempPeptideHits = null;
        for (int i = 0; i < aCompleteQueryList.size(); i++) {
            int lQueryNumber = i + 1;
            lTempPeptideHits = getPeptideHitsAboveIdentityThreshold(lQueryNumber, aConfidence);
            if (lTempPeptideHits.size() > 0) {
                lIdentifiedQueries.add(aCompleteQueryList.get(i));
            }
        }
        return lIdentifiedQueries;
    }

    /**
     * A single iteration over all PeptideHits will index the ProteinMap correctly.
     */
    public void buildProteinMap(){
        int lQueryIndex;
        int lPeptideIndex;

        for (int i = 0; i < iNumberOfQueries; i++) {
            lQueryIndex = i+1;
            Vector v = getAllPeptideHits(lQueryIndex);
            // Update the PeptideHit in the ProteinMap.
            if (v != null) {
                for (int j = 0; j < v.size(); j++) {
                    lPeptideIndex = j + 1;
                    PeptideHit lPeptideHit = (PeptideHit) v.elementAt(j);
                    if (lPeptideHit != null) {
                        int lNumberOfProteinHits = lPeptideHit.getProteinHits().size();
                        for (int k = 0; k < lNumberOfProteinHits; k++) {
                            ProteinHit lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(k);
                            iProteinMap.addProteinSource(lProteinHit.getAccession(), lQueryIndex, lPeptideIndex);
                        }
                    }
                }
            }
        }
    }
}

