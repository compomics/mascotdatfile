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

import junit.TestCaseLM;
import junit.framework.Assert;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 1-mrt-2006
 * Time: 20:31:06
 */

/**
 * * This class implements the test scenario for the PeptideHit Class.
 */
public class TestQuery extends TestCaseLM {
    // Class specific log4j logger for TestQuery instances.
    private static Logger logger = Logger.getLogger(TestQuery.class);

    public TestQuery() {
        super("This is the test scenario for a Query instance.");
    }

    public void testReadQuery() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F010062.dat"));
        Vector lQueryList = lMDF.getQueryList();
        Assert.assertEquals("Query has wrong size." + lQueryList.size() + " instead of 1000.", 1000, lQueryList.size());
        //get query 55
        Query q = (Query) lQueryList.get(55 - 1);
        Assert.assertEquals("CapLC15412.431.2.1.mgf", q.getTitle());
        Assert.assertEquals("1+", q.getChargeString());
        Assert.assertEquals(95.099400, q.getMinMZ(), 0.0);
        Assert.assertEquals(344.692200, q.getMaxMZ(), 0.0);
        Assert.assertEquals(1.104, q.getMinIntensity(), 0.0);
        Assert.assertEquals(295.6, q.getMaxIntensity(), 0.0);
        Assert.assertEquals(41, q.getNumberOfPeaks());
        Assert.assertEquals(-1, q.getNumberUsed1());
        Assert.assertEquals(342.350124, q.getPrecursorMass(), 0.0);
        Assert.assertEquals(416.2654, q.getPrecursorIntensity(), 0.0);
        Assert.assertEquals(343.357400, q.getPrecursorMZ(), 0.0);

    }

    public void testReadQueryWithoutCharge() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F001343.dat"));
        Vector lQueryList = lMDF.getQueryList();
        Query q = (Query) lQueryList.get(55);
        Assert.assertEquals("1+", q.getChargeString());
    }

    public void testQueryWithoutPeaks() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F001343.dat"));
        Vector lQueryList = lMDF.getQueryList();
        Query q = (Query) lQueryList.get(766);
        Assert.assertNotNull(q);
        Assert.assertEquals("caplc138.001.2.2.mgf", q.getTitle());
        Assert.assertEquals("2+", q.getChargeString());
        Assert.assertEquals(1275.64933, q.getPrecursorMass(), 0.0);
        Assert.assertEquals(638.827, q.getPrecursorMZ(), 0.0);
        boolean lMustBeFalse = true;
        try {
            q.getMaxIntensity();
        } catch (NullPointerException ne) {
            lMustBeFalse = false;
        }
        Assert.assertTrue(lMustBeFalse);
    }

    public void testGetNumberOfBins() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F010062.dat"));
        Vector lQueryList = lMDF.getQueryList();
        //get query 1000
        Query q = (Query) lQueryList.get(1000 - 1);
        Assert.assertEquals(16, q.getNumberOfBins());
        q = (Query) lQueryList.get(1 - 1);
        Assert.assertEquals(3, q.getNumberOfBins());
    }

    //Datfile F001343.dat was modified for testing purposes. Query 894 has no title whereas Query 895 has no peaklist.

    public void testNoTitleOrNoPeaks() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F001343.dat"));
        Vector lQueryList = lMDF.getQueryList();
        //get query 894 wich has no title.
        Query q = (Query) lQueryList.get(894 - 1);
        Assert.assertEquals("No title (Query 894).", q.getTitle());
        Assert.assertEquals("3+", q.getChargeString());
        Assert.assertEquals(2045.834295, q.getPrecursorMass(), 0.0);
        Assert.assertEquals(147.4, q.getMaxIntensity(), 0.0);

        //get query 895 wich has no peaklist.
        // Ions1 is not a key.
        q = (Query) lQueryList.get(895 - 1);
        Assert.assertEquals("caplc191.065.2.3.mgf", q.getTitle());
        Assert.assertEquals("3+", q.getChargeString());
        Assert.assertEquals(2304.696195, q.getPrecursorMass(), 0.0);
        boolean lMustBeTrue = false;
        Assert.assertNull(q.getPeakList());

        //get query 896 wich has an empty Ions1 value.
        // Ions1 is a key, but has no value.
        q = (Query) lQueryList.get(893 - 1);
        Assert.assertNull(q.getPeakList());
    }

    //Datfile F001343.dat was modified for testing purposes. Query 894 has no title whereas Query 895 has no peaklist.

    public void testDistillerMultiFileTitleParsing() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F000002.dat"));
        Query.setDistillerFilenameProcessing(true);
        Vector lQueryList = lMDF.getQueryList();
        Query q = (Query) lQueryList.get(0);
        Assert.assertEquals("QstarE04508_3772_147506_1_2.mgf", q.getTitle());
    }

    //Datfile F001343.dat was modified for testing purposes. Query 894 has no title whereas Query 895 has no peaklist.

    public void testDistillerSingleFileTitleParsing() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F000003.dat"));
        Query.setDistillerFilenameProcessing(true);
        Vector lQueryList = lMDF.getQueryList();
        Query q = (Query) lQueryList.get(0);
        Assert.assertEquals("L446_Bart_081022A_Reverse_17_1491_2621_1_2.mgf", q.getTitle());
        Query.setDistillerFilenameProcessing(false);

    }

    public void testRetentionTimeInSeconds() {
        MascotDatfile lMDF = new MascotDatfile(getFullFilePath("F000003.dat"));
        Vector lQueryList = lMDF.getQueryList();
        Query q = (Query) lQueryList.get(1);
        Assert.assertEquals("1407.2138", q.getRetentionTimeInSeconds());

        lMDF = new MascotDatfile(getFullFilePath("F010983.dat"));
        lQueryList = lMDF.getQueryList();
        q = (Query) lQueryList.get(1);
        Assert.assertNull(q.getRetentionTimeInSeconds());
        Query.setDistillerFilenameProcessing(false);
    }

}
