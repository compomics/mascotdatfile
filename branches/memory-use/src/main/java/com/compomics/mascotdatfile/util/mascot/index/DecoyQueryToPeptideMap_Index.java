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

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.ModificationList;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.ProteinMap;
import java.util.Vector;

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
public class DecoyQueryToPeptideMap_Index extends QueryToPeptideMap_Index implements QueryToPeptideMapInf {
    // Class specific log4j logger for DecoyQueryToPeptideMap_Index instances.
    private static Logger logger = Logger.getLogger(DecoyQueryToPeptideMap_Index.class);

    /**
     * Constructor for creating a new QueryToPeptideMap.
     *
     * @param aProteinMap       ProteinMap is a structured version of the ProteinSection.
     * @param aModificationList ModificationList to create a different Modification[] for each PeptideHit.
     */
    public DecoyQueryToPeptideMap_Index(Controller aController, ProteinMap aProteinMap, ModificationList aModificationList) {
        super(aController, aProteinMap, aModificationList);
    }

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
        PeptideHit lPeptideHit = null;

        int lCount = 0;

        //2.b) Get homology and identity score of this Query to pass to the creation of a peptidehit.
        //     lThreshold[0] is the homology threshold, lThreshold[1] is the identity threshold.
        double lQmatch = Double.parseDouble(iController.readDecoySummary(aQueryNumber, SummaryIndex.QMATCH));
        double lQplughole = Double.parseDouble(iController.readDecoySummary(aQueryNumber, SummaryIndex.QPLUGHOLE));

        //2.c) As long as there are PeptideHits returning, generate new PeptideHit Instances.
        String s = iController.readDecoyPeptideHit(aQueryNumber, aPeptideHitNumber);

        if (s != null) {
            lPeptideHit = PeptideHit.parsePeptideHit(s, iProteinMap, iModificationList, new double[]{lQplughole, lQmatch});
        }

        return lPeptideHit;
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
        return iController.getNumberOfDecoyPeptideHits(aQueryNumber);
    }


}
