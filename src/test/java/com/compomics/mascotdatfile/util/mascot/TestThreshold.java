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

import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 4-mrt-2006
 * Time: 8:59:27
 */

/**
 * This class implements the test scenario for the class.
 */
public class TestThreshold extends TestCase {
    // Class specific log4j logger for TestThreshold instances.
    private static Logger logger = Logger.getLogger(TestThreshold.class);

    public TestThreshold() {
        super("This is the test scenario for Threshold functionality of the PeptideHit Class.");
    }

    public void testCalculateThreshold() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010062.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        Assert.assertEquals(26.998377258672452, lQueryToPeptideMap.getPeptideHitOfOneQuery(866, 1).calculateIdentityThreshold(0.05), 0.0);
        Assert.assertEquals(30.008677215312265, lQueryToPeptideMap.getPeptideHitOfOneQuery(866, 1).calculateIdentityThreshold(0.025), 0.0);
        Assert.assertEquals(30.115704435972777, lQueryToPeptideMap.getPeptideHitOfOneQuery(384, 1).calculateIdentityThreshold(0.05), 0.0);
        Assert.assertEquals(28.662873390841945, lQueryToPeptideMap.getPeptideHitOfOneQuery(821, 1).calculateIdentityThreshold(0.05), 0.0);
        Assert.assertEquals(30.67814511161839, lQueryToPeptideMap.getPeptideHitOfOneQuery(904, 1).calculateIdentityThreshold(0.05), 0.0);
        Assert.assertEquals(29.79092900638326, lQueryToPeptideMap.getPeptideHitOfOneQuery(266, 1).calculateIdentityThreshold(0.05), 0.0);
        Assert.assertEquals(27.07570176097936, lQueryToPeptideMap.getPeptideHitOfOneQuery(903, 1).calculateIdentityThreshold(0.05), 0.0);
        Assert.assertTrue(lQueryToPeptideMap.getPeptideHitOfOneQuery(903, 1).calculateIdentityThreshold(0.05) < lQueryToPeptideMap.getPeptideHitOfOneQuery(903, 1).calculateIdentityThreshold(0.04));

    }

    public void testReadAndAnalyseF010062Conf95() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010062.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        try {
            File input = new File(TestCaseLM.getFullFilePath("Threshold_test_F010062_conf95.txt"));
            BufferedReader lBufferedReader = new BufferedReader(new FileReader(input));
            int lCount = 0;
            for (int i = 1; i < lQueryToPeptideMap.getNumberOfQueries() + 1; i++) {
                Vector lPeptideHitsVec = lQueryToPeptideMap.getAllPeptideHits(i);
                if (lPeptideHitsVec == null) {
                    continue;
                }
                PeptideHit lPeptideHit = (PeptideHit) lPeptideHitsVec.get(0);
                if (lPeptideHit.scoresAboveIdentityThreshold(0.05)) {
                    lCount++;
                    Assert.assertEquals(lBufferedReader.readLine(), lPeptideHit.getModifiedSequence() + "," + i);
                }
            }
            Assert.assertEquals(45, lCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testReadAndAnalyseF010062Conf98() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010062.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        try {
            File input = new File(TestCaseLM.getFullFilePath("Threshold_test_F010062_conf98.txt"));
            BufferedReader lBufferedReader = new BufferedReader(new FileReader(input));
            int lCount = 0;
            for (int i = 1; i < lQueryToPeptideMap.getNumberOfQueries() + 1; i++) {
                Vector lPeptideHitsVec = lQueryToPeptideMap.getAllPeptideHits(i);
                if (lPeptideHitsVec == null) {
                    continue;
                }
                PeptideHit lPeptideHit = (PeptideHit) lPeptideHitsVec.get(0);
                if (lPeptideHit.scoresAboveIdentityThreshold(0.02)) {
                    lCount++;
                    Assert.assertEquals(lBufferedReader.readLine(), lPeptideHit.getModifiedSequence() + "," + i);
                }
            }
            Assert.assertEquals(39, lCount);
            Assert.assertFalse(44 == lCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testReadAndAnalyseF009911Conf95() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F009911.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        try {
            File input = new File(TestCaseLM.getFullFilePath("Threshold_test_F009911_conf95.txt"));
            BufferedReader lBufferedReader = new BufferedReader(new FileReader(input));
            int lCount = 0;
            for (int i = 1; i < lQueryToPeptideMap.getNumberOfQueries() + 1; i++) {
                Vector lPeptideHitsVec = lQueryToPeptideMap.getAllPeptideHits(i);
                if (lPeptideHitsVec == null) {
                    continue;
                }
                PeptideHit lPeptideHit = (PeptideHit) lPeptideHitsVec.get(0);
                if (lPeptideHit.scoresAboveIdentityThreshold(0.05)) {
                    lCount++;
                    Assert.assertEquals(lBufferedReader.readLine(), lPeptideHit.getModifiedSequence() + "," + i);
                }
            }
            Assert.assertEquals(1, lCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
