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

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 1-mrt-2006
 * Time: 9:10:10
 */

/**
 * Contains a HashMap in the PeptideHit to direction. Keys     ModifiedSequences. Vector   Query instances.
 */
public class PeptideToQueryMap {
    // Class specific log4j logger for PeptideToQueryMap instances.
    private static Logger logger = Logger.getLogger(PeptideToQueryMap.class);
    /**
     * The HashMap in the PeptideHit to Query direction. Keys     ModifiedSequences. Vector   Query instances.
     */
    private HashMap iPeptideToQueryMap = null;

    /**
     * Constructor Create the iPeptideToQueryMap, starting from the iQueryToPeptideMap
     *
     * @param aQueryToPeptideMap Hashmap with key=Querynumber that has a value=vector with peptidehits.
     * @param aQueryList the query list
     */
    public PeptideToQueryMap(QueryToPeptideMapInf aQueryToPeptideMap, Vector aQueryList) {
        // 1.Initiate iPeptideToQueryMap.
        iPeptideToQueryMap = new HashMap(300, 100);

        // 2.Loop trough the Queries.
        for (int i = 1; i <= aQueryList.size(); i++) {
            // 3. Each query Key holds a Vector with PeptideHits, now loop trough these PeptideHits.
            List lPeptideHits = aQueryToPeptideMap.getAllPeptideHits(i);
            if (lPeptideHits == null) {
                continue;
            } else if (lPeptideHits.isEmpty()) {
                continue;
            }
            for (int j = 0; j < lPeptideHits.size(); j++) {
                PeptideHit lTempPH = (PeptideHit) lPeptideHits.get(j);
                String lTempPHModifiedSequence = lTempPH.getModifiedSequence();
                Vector lQueries = null;
                // 3.b) If there is no Key with the getModifiedSequence(),
                //      add a key with the ModifiedSequence of the PeptideHit with Value = new Vector.
                //      now add the Query to the Vector.
                if (!iPeptideToQueryMap.containsKey(lTempPHModifiedSequence)) {
                    iPeptideToQueryMap.put(lTempPHModifiedSequence, new Vector());
                    lQueries = (Vector) iPeptideToQueryMap.get(lTempPHModifiedSequence);
                    lQueries.add(aQueryList.get(i - 1));
                } else {
                    //There is a key with the modifiedSequence in iPeptideToQueryMap containing a Vector.
                    //Add the Query to the Vector.
                    lQueries = (Vector) iPeptideToQueryMap.get(lTempPHModifiedSequence);
                    lQueries.add(aQueryList.get(i - 1));
                }
            }
        }
    }

    public Vector getQueriesByModifiedSequence(String aModifiedSequence) {
        return (Vector) iPeptideToQueryMap.get(aModifiedSequence);
    }

    /**
     * Returns the number of unique modified sequences in this map.
     *
     * @return int with the number of unique modified sequences in this map.
     */
    public int size() {
        return iPeptideToQueryMap.size();
    }
}
