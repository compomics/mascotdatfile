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
package com.compomics.mascotdatfile.util.mascot.index;

import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.ModificationList;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.ProteinHit;
import com.compomics.mascotdatfile.util.mascot.ProteinMap;
import com.compomics.mascotdatfile.util.mascot.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 26-feb-2006 Time: 16:52:10
 */
/**
 * This Class creates a map with all the peptide hits in 2 dimensions. The first
 * level holds the results of the queries in a hashmap (Key: Querynumber
 * Value:Vector with PeptideHits). The second dimension holds a Vector with the
 * corresponding peptide hits of the query.
 */
public class QueryToPeptideMap_Index implements QueryToPeptideMapInf {
    // Class specific log4j logger for QueryToPeptideMap_Index instances.

    private static Logger logger = Logger.getLogger(QueryToPeptideMap_Index.class);

    /*
     * The number of queries in the mascot search.
     */
    protected Controller iController = null;
    protected ProteinMap iProteinMap = null;
    protected ModificationList iModificationList = null;
    private String lQMatchString;
    private String lQPlugholeString;

    /**
     * Constructor for creating a new QueryToPeptideMap.
     *
     * @param aController the controller
     * @param aProteinMap ProteinMap is a structured version of the
     * ProteinSection.
     * @param aModificationList ModificationList to create a different
     * Modification[] for each PeptideHit.
     */
    public QueryToPeptideMap_Index(Controller aController, ProteinMap aProteinMap, ModificationList aModificationList) {

        iController = aController;
        iModificationList = aModificationList;
        iProteinMap = aProteinMap;

    }

    /**
     * This method returns a PeptideHit of the QueryNumber that is given by two
     * parameters.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will
     * return the <i>aPeptideHitsNumber</i>
     * PeptideHit of Query 45.
     * @param aPeptideHitNumber The PeptideHit in the requested Query. The best
     * peptideHit is peptidehit 1.
     * @return PeptideHit returns the requested PeptideHit of the requested
     * QueryNumber. <br>Returns PeptideHit = null if PeptideHit is null.
     */
    public PeptideHit getPeptideHitOfOneQuery(int aQueryNumber, int aPeptideHitNumber) {

        //2.b) Get homology and identity score of this Query to pass to the creation of a peptidehit.
        //     lThreshold[0] is the homology threshold, lThreshold[1] is the identity threshold.
        lQMatchString = iController.readSummary(aQueryNumber, SummaryIndex.QMATCH);
        double lQmatch = Double.parseDouble(lQMatchString);

        lQPlugholeString = iController.readSummary(aQueryNumber, SummaryIndex.QPLUGHOLE);
        double lQplughole = Double.parseDouble(lQPlugholeString);

        //2.c) As long as there are PeptideHits returning, generate new PeptideHit Instances.

        List<String> peptideBlock = iController.readPeptideHitBlock(aQueryNumber, aPeptideHitNumber);
        if (!peptideBlock.isEmpty()) {
            String s = peptideBlock.get(0).substring(peptideBlock.get(0).indexOf('=') + 1);
            String substitution = null;
            for (String blockPart : peptideBlock) {
                if (blockPart.contains("subst")) {
                    substitution = blockPart.substring(blockPart.indexOf('=') + 1);
                }
            }
            return PeptideHit.parsePeptideHit(s, iProteinMap, iModificationList, new double[]{lQplughole, lQmatch}, substitution);
        } else {
            return null;
        }
    }

    /**
     * Method The requested query in a Vector with PeptideHit instances.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will
     * return all the PeptideHits of Query 45.
     * @return Vector Vector containing the PeptideHits of the requested query,
     * <br>'null' if the query number was not found or no identifications were
     * made from the query.
     */
    public List<PeptideHit> getAllPeptideHits(int aQueryNumber) {
        List<PeptideHit> peptideHits = new ArrayList<PeptideHit>();
        int iNumberOfPeptideHits = getNumberOfPeptideHits(aQueryNumber);

        for (int i = 0; i < iNumberOfPeptideHits; i++) {
            PeptideHit lPeptideHit = getPeptideHitOfOneQuery(aQueryNumber, (i + 1));
            if (lPeptideHit != null) {
                peptideHits.add(lPeptideHit);
            }
        }
        return peptideHits;
    }

    /**
     * Method The number of PeptideHits in the requested querynumber.
     *
     * @param aQueryNumber int The querynumber you want to get the amount of
     * PeptideHits from.
     * @return size int The number of peptidehits in the query, or 0 if the
     * querynumber does not exist. <b>Remark:</b> If there are no peptidehits in
     * the query(ex: 'q1_p1 = -1'), a PeptideHit with value = null is created.
     * So if the querynumber exists it will always have at least 1 PeptideHit.
     * This will cause a NullPointerException if you try to acces such a
     * null_PeptideHit.
     */
    public int getNumberOfPeptideHits(int aQueryNumber) {
        return iController.getNumberOfPeptideHits(aQueryNumber);
    }

    public int getNumberOfQueries() {
        return iController.getNumberOfQueries();
    }

    /**
     * This method returns the best PeptideHit of each Query in a Vector.
     * Vector.get(5) returns the best PeptideHit of Query 6 (5+1).
     *
     * @return Vector with all the best PeptideHits next to their QueryNumber.
     * Vector[0] contains info of Query 1. Vector[999] contains info of Query
     * 1000.
     */
    public List<PeptideHit> getBestPeptideHits() {
        return getPeptideHits(1);
    }

    /**
     * This method returns the PeptideHits of one number of each query in a
     * Vector.
     *
     * @param aPeptideHitNumber The PeptideHit in the requested Query. The best
     * peptideHit is peptidehit 1.
     * @return Vector with PeptideHit (aPeptideHitNumber) of every query.
     * Vector[0] contains info of Query 1. Vector[999] contains info of Query
     * 1000.
     */
    public List<PeptideHit> getPeptideHits(int aPeptideHitNumber) {
        int numberOfQueriesInMascotDatFile = iController.getNumberOfQueries();
        List<PeptideHit> lBestPeptideHits = new ArrayList<PeptideHit>(numberOfQueriesInMascotDatFile);
        for (int i = 1; i <= numberOfQueriesInMascotDatFile; i++) {
            lBestPeptideHits.add(getPeptideHitOfOneQuery(i, aPeptideHitNumber));
        }
        return lBestPeptideHits;
    }

    /**
     * This method returns the best PeptideHit(P1) of the QueryNumber that is
     * given by a parameter.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will
     * return the best PeptideHit of Query 45.
     * @return PeptideHit returns the best PeptideHit of the requested
     * QueryNumber. throws IndexOutOfBounds if there are no peptide hits for the
     * given query
     */
    public PeptideHit getPeptideHitOfOneQuery(int aQueryNumber) throws IndexOutOfBoundsException {
        return getAllPeptideHits(aQueryNumber).get(0);
    }

    /**
     * This method calculates all the peptidehits that are above the default
     * (alpha=0.05) identity threshold.
     *
     * @return Vector vector with all the PeptideHit instances above identity
     * threshold.
     */
    public List<PeptideHit> getAllPeptideHitsAboveIdentityThreshold() {
        return this.getAllPeptideHitsAboveIdentityThreshold(0.05);
    }

    /**
     * This method calculates all the peptidehits that are above the default
     * identity threshold.
     *
     * @param aConfidence alpha confidence of the returning peptidehits.
     * @return Vector vector with all the PeptideHit instances above identity
     * threshold.
     */
    public List<PeptideHit> getAllPeptideHitsAboveIdentityThreshold(double aConfidence) {
        List<PeptideHit> lAllBestPeptideHits = getBestPeptideHits();
        //test fails, if this changes, change code duplication further down
        for (Iterator<PeptideHit> itr = lAllBestPeptideHits.iterator(); itr.hasNext();) {
            PeptideHit peptideHit = itr.next();
            if (peptideHit != null) {
                if (!peptideHit.scoresAboveIdentityThreshold(aConfidence)) {
                    itr.remove();
                }
            } else {
                itr.remove();
            }
        }
        return lAllBestPeptideHits;
    }

    /**
     * This method returns all the peptidehits above 95% threshold from one
     * specified query.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will
     * return all the PeptideHits of Query 45 above their 95% identity
     * threshold.
     * @return Vector Vector with peptidehits above threshold.
     */
    public List<PeptideHit> getPeptideHitsAboveIdentityThreshold(int aQueryNumber) {
        return getPeptideHitsAboveIdentityThreshold(aQueryNumber, 0.05);
    }

    /**
     * This method returns all the peptidehits above a parametrical threshold
     * from one specified query.
     *
     * @param aQueryNumber Requested query by number! <br>45 as parameter will
     * return all the PeptideHits of Query 45.
     * @param aConfidenceInterval The confidence the identification has to
     * match, this value equals alpha. So for a 90% confidence alpha is 0.10.
     * @return Vector Vector with peptidehits above threshold.
     */
    public List<PeptideHit> getPeptideHitsAboveIdentityThreshold(int aQueryNumber, double aConfidenceInterval) {
        List<PeptideHit> lPeptideHits = getAllPeptideHits(aQueryNumber);
        if (lPeptideHits != null) {
            for (Iterator<PeptideHit> itr = lPeptideHits.iterator(); itr.hasNext();) {
                PeptideHit peptideHit = itr.next();
                if (peptideHit != null) {
                    if (!peptideHit.scoresAboveIdentityThreshold(aConfidenceInterval)) {
                        itr.remove();
                    }
                } else {
                    itr.remove();
                }
            }
            return lPeptideHits;
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    /**
     * This method returns a Vector with Query's that lead to a identification
     * above a certain confidence (alpha).
     *
     * @param aConfidence double confidence alpha above wich an identification
     * has to score.
     * @param aCompleteQueryList Vector with all queries.
     * @return Vector queries with an identification above a certain confidence
     * (alpha).
     */
    public List getIdentifiedQueries(double aConfidence, List<Query> aCompleteQueryList) {
        // All the identified Queries will be stored here.
        List<Query> lIdentifiedQueries = new ArrayList<Query>();
        // The temporary peptidehits will be stored here, if the size is bigger then 0, the Query will be stored.
        // Mind that the getPeptideHitsAboveIdentityThreshold() method takes the querynumber as input (45 leads to query45)
        // And that the QueryList is a Vector starting from zero! (45 leads to query44)
        List lTempPeptideHits;
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
     * A single iteration over all PeptideHits will index the ProteinMap
     * correctly.
     */
    public void buildProteinMap() {
        int lQueryIndex;
        int lPeptideIndex;
        int numberOfQueriesInDatFile = iController.getNumberOfQueries();

        for (int i = 0; i < numberOfQueriesInDatFile; i++) {
            lQueryIndex = i + 1;
            List<PeptideHit> peptideHitsForQuery = getAllPeptideHits(lQueryIndex);
            // Update the PeptideHit in the ProteinMap.
            for (int j = 0; j < peptideHitsForQuery.size(); j++) {
                lPeptideIndex = j + 1;
                PeptideHit lPeptideHit = peptideHitsForQuery.get(j);
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
