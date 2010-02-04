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

import junit.TestCaseLM;
import junit.framework.Assert;

import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 2-mrt-2006
 * Time: 14:42:31
 */

/**
 * This class implements the test scenario for the PeptideToQueryMap class.
 */
public class TestPeptideToQueryMap extends TestCaseLM {

    
    public TestPeptideToQueryMap() {
        super("Testscenario for the peptidehit Class. ");
    }

    public void testReadQuery() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F010062.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideToQueryMap lPeptideToQueryMap = lMascotDatfile.getPeptideToQueryMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(555, 1);
        Query lQuery = (Query) lPeptideToQueryMap.getQueriesByModifiedSequence(lPeptideHit.getModifiedSequence()).get(1);
        Assert.assertEquals(555, lQuery.getQueryNumber());
        Assert.assertEquals(54.76, lQuery.getMaxIntensity(), 0.0);
        Assert.assertEquals(2247, lPeptideToQueryMap.size());
    }


    public void testLastQuery() {
        //These tests are grouped together because they all load the same datfile.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F004071.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideToQueryMap lPeptideToQueryMap = lMascotDatfile.getPeptideToQueryMap();
        PeptideHit lPeptideHit = null;

        //test LastQuery
        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(1000, 1);
        Query lQuery = (Query) lPeptideToQueryMap.getQueriesByModifiedSequence(lPeptideHit.getModifiedSequence()).get(0);
        Assert.assertEquals("AebersSAX051008_1.6.1post_9097_130.mgf", lQuery.getTitle());

        //test FirstQuery
        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(1, 1);
        lQuery = (Query) lMascotDatfile.getQueryList().get(0);
        Assert.assertEquals("Aebers_SAX051008_1.6.1mox_9087_212.mgf", lQuery.getTitle());

        //test BestPeptideHits
        Vector lBestQueriesVec = lQueryToPeptideMap.getPeptideHits(1);
        lPeptideHit = (PeptideHit) lBestQueriesVec.get(1000 - 1);
        Assert.assertEquals("SIQQLVTYVFPIAEVVLKEEQQRR", lPeptideHit.getSequence());
        lPeptideHit = (PeptideHit) lBestQueriesVec.get(1 - 1);
        Assert.assertNull(lPeptideHit);

        //test getPeptideHits
        lBestQueriesVec = lQueryToPeptideMap.getPeptideHits(6);
        lPeptideHit = (PeptideHit) lBestQueriesVec.get(1000 - 1);
        Assert.assertEquals("KLNVSYPATGCQKLFEVVDEHKLR", lPeptideHit.getSequence());
        lPeptideHit = (PeptideHit) lBestQueriesVec.get(70 - 1);
        Assert.assertEquals("ASVGSSNAATTSSTTSAPR", lPeptideHit.getSequence());
    }
}
