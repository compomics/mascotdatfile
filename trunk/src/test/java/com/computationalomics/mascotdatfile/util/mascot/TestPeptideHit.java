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

package com.computationalomics.mascotdatfile.util.mascot;

import com.computationalomics.mascotdatfile.util.interfaces.FragmentIon;
import junit.TestCaseLM;
import junit.framework.Assert;

import java.util.ArrayList;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 28-feb-2006
 * Time: 9:13:57
 */

/**
 * This class implements the test scenario for the PeptideHit Class.
 */
public class TestPeptideHit extends TestCaseLM {
    public TestPeptideHit() {
        super("Testscenario for the peptidehit Class. ");
    }

    public void testReadPeak() {
        Peak lPeak = new Peak(15.55, 150.5);
        Assert.assertEquals(15.55, lPeak.getMZ(), 0.0);
        Assert.assertEquals(150.5, lPeak.getIntensity(), 0.0);
    }

    public void testReadPeptideHit() {

        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        //F009911  q447_p3
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(447, 3);

        //Test all the PeptideHit instance variables.
        Assert.assertEquals(0, lPeptideHit.getMissedCleavages());
        Assert.assertEquals(1802.063690, lPeptideHit.getPeptideMr(), 0.0);
        Assert.assertEquals(0.196582, lPeptideHit.getDeltaMass(), 0.0);
        Assert.assertEquals(6, lPeptideHit.getNumberOfIonsMatched());
        Assert.assertEquals("GKYQIHTGLQHSIIR", lPeptideHit.getSequence());
        Assert.assertEquals(56, lPeptideHit.getPeaksUsedFromIons1());
        Assert.assertEquals(3, lPeptideHit.getVariableModificationsArray()[10]);
        Assert.assertEquals(3.76, lPeptideHit.getIonsScore(), 0.0);
        //ion series found:     -- 00000020000000000 --
        Assert.assertEquals(2, lPeptideHit.getIonSeriesFound()[6]);
        Assert.assertEquals(0, lPeptideHit.getIonSeriesFound()[3]);
        Assert.assertEquals(17, lPeptideHit.getIonSeriesFound().length);
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons2());
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons3());

        ProteinHit lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(0);
        Assert.assertEquals("Q5FYB0 (132-146)", lProteinHit.getAccession());
        Assert.assertEquals(0, lProteinHit.getFrameNumber());
        Assert.assertEquals(1, lProteinHit.getStart());
        Assert.assertEquals(15, lProteinHit.getStop());
        Assert.assertEquals(1, lProteinHit.getMultiplicity());
    }

    public void testReadProteinHit() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        //F009911  q447_p3
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(447, 3);
        ProteinHit lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(0);
        Assert.assertEquals("Q5FYB0 (132-146)", lProteinHit.getAccession());
        Assert.assertEquals(0, lProteinHit.getFrameNumber());
        Assert.assertEquals(1, lProteinHit.getStart());
        Assert.assertEquals(15, lProteinHit.getStop());
        Assert.assertEquals(1, lProteinHit.getMultiplicity());
    }

    public void testProteinHitFromPeptideCentricDatabase() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F001326.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(447, 3);
        ProteinHit lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(0);
        Assert.assertEquals("IPI00029377.1 (77-85)", lProteinHit.getAccession());
        Assert.assertEquals(1, lProteinHit.getStart());
        Assert.assertEquals(77, lProteinHit.getPeptideStartInProtein_PeptideCentricDatabase());
        Assert.assertEquals(9, lProteinHit.getStop());
        Assert.assertEquals(85, lProteinHit.getPeptideStopInProtein_PeptideCentricDatabase());
    }

    public void testReadMultipleProteinHits() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F004071.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        //F004071  q917_p10
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(917, 10);
        ArrayList lProteinHitsArray = lPeptideHit.getProteinHits();
        //First proteinHit
        ProteinHit lProteinHit = (ProteinHit) lProteinHitsArray.get(0);
        Assert.assertEquals("CG32019-PE", lProteinHit.getAccession());
        Assert.assertEquals(0, lProteinHit.getFrameNumber());
        Assert.assertEquals(5879, lProteinHit.getStart());
        Assert.assertEquals(5892, lProteinHit.getStop());
        Assert.assertEquals(6, lProteinHit.getMultiplicity());

    }

    public void testModification() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        //F009911  q447_p5
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(447, 5);
        Assert.assertNull(lPeptideHit.getModifications()[0]);
        Assert.assertNull(lPeptideHit.getModifications()[1]);
        Assert.assertNull(lPeptideHit.getModifications()[2]);
        Assert.assertEquals("Pro 5xC(13)", lPeptideHit.getModifications()[3].getType());
        Assert.assertEquals(5.000000, lPeptideHit.getModifications()[3].getMass(), 0.0);
        Assert.assertNull(lPeptideHit.getModifications()[4]);
        Assert.assertEquals(7, lPeptideHit.getModifications()[5].getModificationID());
        Assert.assertEquals("P", lPeptideHit.getModifications()[5].getLocation());
        Assert.assertNull(lPeptideHit.getModifications()[6]);
        Assert.assertNotNull(lPeptideHit.getModifications()[7]);
        Assert.assertNull(lPeptideHit.getModifications()[8]);
        Assert.assertNotNull(lPeptideHit.getModifications()[9]);
        Assert.assertEquals(0.984009, lPeptideHit.getModifications()[10].getMass(), 0.0);
        Assert.assertEquals("Deamidation", lPeptideHit.getModifications()[10].getType());
        Assert.assertEquals("NQ", lPeptideHit.getModifications()[10].getLocation());
        Assert.assertEquals(3, lPeptideHit.getModifications()[10].getModificationID());
        Assert.assertNull(lPeptideHit.getModifications()[11]);
        Assert.assertNull(lPeptideHit.getModifications()[12]);
        Assert.assertNull(lPeptideHit.getModifications()[13]);
        Assert.assertNull(lPeptideHit.getModifications()[14]);
        Assert.assertNull(lPeptideHit.getModifications()[15]);
        Assert.assertNull(lPeptideHit.getModifications()[16]);
        Assert.assertEquals(6.086490, lPeptideHit.getModifications()[17].getMass(), 0.0);
        Assert.assertEquals("Arg 6xC(13)", lPeptideHit.getModifications()[17].getType());
        Assert.assertEquals("R", lPeptideHit.getModifications()[17].getLocation());

        //0 0 0 7 0 7 0 7 0 7 3  0  0  0  0  0  0  0//
        //0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17/
        //  Q T P G P L P A P Q  L  T  S  T  V  L  R//
    }

    public void testModifiedSequenceA() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        //F009911  q447_p5
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(447, 5);
        Assert.assertEquals("NH2-QTP<C13>GP<C13>LP<C13>AP<C13>Q<Dam>LTSTVLR<C13*>-COOH", lPeptideHit.getModifiedSequence());

        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(381, 6);
        Assert.assertEquals("AcD3-K<AcD3K*>VLR<C13*>LLITAWER<C13*>-COOH", lPeptideHit.getModifiedSequence());

        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(344, 4);
        Assert.assertEquals("AcD3-Q<Dam>Q<Dam>SP<C13>K<AcD3K*>PSVIK<AcD3K*>SR<C13*>-COOH", lPeptideHit.getModifiedSequence());
        Assert.assertFalse(lPeptideHit.getModifications()[0].isFixed());
    }

    public void testModifiedSequenceB() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F010062.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();


        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(256, 3);
        Assert.assertEquals("NH2-Q<Pyr,Dam>IM<Mox>ALR-COOH", lPeptideHit.getModifiedSequence());
        Assert.assertFalse(lPeptideHit.getModifications()[0].isFixed());

        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(574, 5);
        Assert.assertEquals("NH2-C<Pyc,Cmm*>LK<AcD3K*>PFEN<Dam>SR-COOH", lPeptideHit.getModifiedSequence());
        Assert.assertFalse(lPeptideHit.getModifications()[0].isFixed());
        Assert.assertTrue(lPeptideHit.getModifications()[1].isFixed());

        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(381, 6);
        Assert.assertEquals("NH2-PM<Mox>PPEARR-COOH", lPeptideHit.getModifiedSequence());

        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(276, 9);
        Assert.assertEquals("NH2-C<Cmm*>N<Dam>QLLR-COOH", lPeptideHit.getModifiedSequence());
        Assert.assertTrue(lPeptideHit.getModifications()[1].isFixed());

        /*q276_p9=0,803.395935,0.205313,4,CNQLLR,54,00300000,1.07,00010020000000000,0,0;"Q7Z7L7":0:387:392:1*/
    }

    public void testModifiedSequenceC() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F004071.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(319, 9);
        Assert.assertEquals("AKWHLGIR", lPeptideHit.getSequence());
        Assert.assertEquals("Car-AK<AcD3K*>WHLGIR-COOH", lPeptideHit.getModifiedSequence());
        Assert.assertFalse("Carbamylation is not fixed!!", lPeptideHit.getModifications()[0].isFixed());
        Assert.assertTrue("AcD3 is fixed!!", lPeptideHit.getModifications()[2].isFixed());

        /*q319_p9=0,1067.606750,-0.003302,4,AKWHLGIR,26,4000000000,8.85,00020010000000000,0,0;"CG3051-PA":0:458:465:2*/

    }

    // This is from an old datfile from the platelets projects. The fixed mods are read from the parameters this time!!
    public void testModifiedSequenceD() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F001326.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(497, 8);
        Assert.assertEquals("KDCGQDRR", lPeptideHit.getSequence());
        Assert.assertEquals("Ace-K<Ace*>DC<Cmm*>GQDRR-COOH", lPeptideHit.getModifiedSequence());

    }

    public void testThreshold() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F001326.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(497, 8);
        Assert.assertEquals(41.2067, lPeptideHit.calculateIdentityThreshold(), 0.01);
        Assert.assertEquals(38.1964, lPeptideHit.calculateIdentityThreshold(0.10), 0.01);
        Assert.assertEquals(31.2067, lPeptideHit.calculateIdentityThreshold(0.50), 0.01);
        Assert.assertEquals(30.7936, lPeptideHit.getHomologyThreshold(), 0.01);
    }

    public void testGetModifiedSequenceComponents() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F010062.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(256, 3);
        String[] lModifiedSequenceComponents = lPeptideHit.getModifiedSequenceComponents();
        Assert.assertEquals("NH2-Q<Pyr,Dam>", lModifiedSequenceComponents[0]);
        Assert.assertEquals("I", lModifiedSequenceComponents[1]);
        Assert.assertEquals("M<Mox>", lModifiedSequenceComponents[2]);
        Assert.assertEquals("A", lModifiedSequenceComponents[3]);
        Assert.assertEquals("L", lModifiedSequenceComponents[4]);
        Assert.assertEquals("R-COOH", lModifiedSequenceComponents[5]);
        Assert.assertEquals(6, lPeptideHit.getSequence().length());
        Assert.assertFalse(lPeptideHit.getModifications()[0].isFixed());
    }

    public void testErrorTolerantSearchPeptideHit() {
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F028476.dat"));
        QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(23, 1);
        Assert.assertEquals("TITLEVEPSDTIENVK", lPeptideHit.getSequence());
        Assert.assertEquals("NH2-TITLEVEPSDTIEN<**Dam>VK-COOH", lPeptideHit.getModifiedSequence());
        Assert.assertEquals(28.06, lPeptideHit.getIonsScore());
        Assert.assertEquals("ETS_Deamidated", lPeptideHit.getModifications()[14].getType());
        Assert.assertEquals("**Dam", lPeptideHit.getModifications()[14].getShortType());
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(
                lMascotDatfile.getMasses(),
                lMascotDatfile.getParametersSection()
        );
        // Get y4, directly after the ETS - deamidation modification.
        FragmentIon lFragmentIon = lPeptideHitAnnotation.getYions()[3];
        Assert.assertEquals(490.25130300000006, lFragmentIon.getMZ());


    }
}
