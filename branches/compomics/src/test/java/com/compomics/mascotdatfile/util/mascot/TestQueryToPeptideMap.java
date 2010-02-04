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
 * * This class implements the test scenario for the PeptideHit Class.
 */
public class TestQueryToPeptideMap extends TestCaseLM {
    public TestQueryToPeptideMap() {
        super("This is the test scenario for a QueryToPeptideMap instance.");
    }

    public void testReadQuery() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F010062.dat"));
        QueryToPeptideMap Query2P = lMDF.getQueryToPeptideMap();
        PeptideHit ph = Query2P.getPeptideHitOfOneQuery(1, 1);
        Assert.assertNull(ph);
        ph = Query2P.getPeptideHitOfOneQuery(555, 1);
        Assert.assertEquals("GAPAPPPPAQPR", ph.getSequence());
    }

    public void testReadVectorWithPeptideHits() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F010062.dat"));
        QueryToPeptideMap Query2P = lMDF.getQueryToPeptideMap();
        Vector vph = Query2P.getAllPeptideHits(1);
        Assert.assertNull(vph);
        vph = Query2P.getAllPeptideHits(555);
        PeptideHit lPh = (PeptideHit) vph.firstElement();
        Assert.assertEquals("GAPAPPPPAQPR", lPh.getSequence());
    }

    public void testGetPeptideHitsAboveIdentityThreshold() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F010062.dat"));
        QueryToPeptideMap Query2P = lMDF.getQueryToPeptideMap();
        Vector lBestPeptideHitsOfOneQuery = Query2P.getPeptideHitsAboveIdentityThreshold(555);
        Assert.assertEquals(0, lBestPeptideHitsOfOneQuery.size());

        Vector lBestPeptideHits = Query2P.getAllPeptideHitsAboveIdentityThreshold(0.05);
        Assert.assertEquals(45, lBestPeptideHits.size());
        PeptideHit ph = (PeptideHit) lBestPeptideHits.get(0);
        Assert.assertEquals("VAIKR", ph.getSequence());
        ph = (PeptideHit) lBestPeptideHits.get(44);
        Assert.assertEquals("MMGHRPVLVLSQNTKR", ph.getSequence());


        lMDF = new MascotDatfile(getFullFilePath("F010983.dat"));
        Query2P = lMDF.getQueryToPeptideMap();
        lBestPeptideHitsOfOneQuery = Query2P.getPeptideHitsAboveIdentityThreshold(174);
        ph = (PeptideHit) lBestPeptideHitsOfOneQuery.get(0);
        Assert.assertEquals(2, lBestPeptideHitsOfOneQuery.size());
        Assert.assertEquals("METVQLR", ph.getSequence());
        ph = (PeptideHit) lBestPeptideHitsOfOneQuery.get(1);
        Assert.assertEquals("METLNLR", ph.getSequence());

        lBestPeptideHitsOfOneQuery = Query2P.getPeptideHitsAboveIdentityThreshold(1);
        Assert.assertEquals(0, lBestPeptideHitsOfOneQuery.size());
    }

    public void testGetIdentifiedQueries() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F010062.dat"));
        QueryToPeptideMap Query2P = lMDF.getQueryToPeptideMap();
        Vector lIdentifiedQueries = Query2P.getIdentifiedQueries(0.05, lMDF.getQueryList());
        Assert.assertEquals(45, lIdentifiedQueries.size());
        lIdentifiedQueries = Query2P.getIdentifiedQueries(0.10, lMDF.getQueryList());
        Assert.assertEquals(50, lIdentifiedQueries.size());
    }

    public void testReadDatfileWithoutPeptidehits() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F244227.dat"));
        QueryToPeptideMap Query2P = lMDF.getQueryToPeptideMap();
        Assert.assertEquals(5, Query2P.getNumberOfQueries());
        Assert.assertTrue(Query2P.getPeptideMap().isEmpty());
    }
}
