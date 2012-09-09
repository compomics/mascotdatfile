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

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.PeptideToQueryMap;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

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
public class TestPeptideToQueryMap extends TestCase {
    // Class specific log4j logger for TestPeptideToQueryMap instances.
    private static Logger logger = Logger.getLogger(TestPeptideToQueryMap.class);


    public TestPeptideToQueryMap() {
        super("Testscenario for the peptidehit Class. ");
    }

    public void testReadQuery() {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(TestCaseLM.getFullFilePath("F010062.dat"));
        QueryToPeptideMapInf lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideToQueryMap lPeptideToQueryMap = lMascotDatfile.getPeptideToQueryMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(555, 1);
        Query lQuery = (Query) lPeptideToQueryMap.getQueriesByModifiedSequence(lPeptideHit.getModifiedSequence()).get(1);
        Assert.assertEquals(555, lQuery.getQueryNumber());
        Assert.assertEquals(54.76, lQuery.getMaxIntensity(), 0.0);
        Assert.assertEquals(2247, lPeptideToQueryMap.size());
    }


    public void testLastQuery() {
        //These tests are grouped together because they all load the same datfile.
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(TestCaseLM.getFullFilePath("F004071.dat"));
        QueryToPeptideMapInf lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideToQueryMap lPeptideToQueryMap = lMascotDatfile.getPeptideToQueryMap();
        PeptideHit lPeptideHit = null;

        //test LastQuery
        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(1000, 1);
        Query lQuery = (Query) lPeptideToQueryMap.getQueriesByModifiedSequence(lPeptideHit.getModifiedSequence()).get(0);
        Assert.assertEquals("AebersSAX051008_1.6.1post_9097_130.mgf", lQuery.getTitle());

        //test FirstQuery
        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(1, 1);
        lQuery = (Query) lMascotDatfile.getQuery(1);
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
