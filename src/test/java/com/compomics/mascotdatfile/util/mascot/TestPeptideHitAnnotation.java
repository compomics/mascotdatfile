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

import com.compomics.mascotdatfile.util.interfaces.FragmentIon;
import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 15-mrt-2006
 * Time: 22:03:57
 */

/**
 * This class implements the test scenario for the class.
 */
public class TestPeptideHitAnnotation extends TestCase {
    // Class specific log4j logger for TestPeptideHitAnnotation instances.
    private static Logger logger = Logger.getLogger(TestPeptideHitAnnotation.class);

    public TestPeptideHitAnnotation() {
        super("This is the test scenario for a PeptideHitAnnotation instance.");
    }

    public void testIonSeries() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        // Request PeptideHit @Query 224, sequence: VAIKR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(223, 1);
        // There must be 4 b- and 4 y-ions!
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        Assert.assertEquals(14, (lPeptideHitAnnotation.getBions().length + lPeptideHitAnnotation.getYions().length));
        // b- and y-ion masses, checked by the mascot output!
        //
    }

    /**
     * This method will test the B and Y ion genereation in the constructor.
     */
    public void testBYIons() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //QQMENYPK
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(263, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        FragmentIon[] lFragmentIonsArray = null;
        //Get Bions.
        lFragmentIonsArray = lPeptideHitAnnotation.getBions();
        Assert.assertEquals(112.03987000000001, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(890.3354540000001, lFragmentIonsArray[6].getMZ(), 0.0);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        //Get Yions.
        lFragmentIonsArray = lPeptideHitAnnotation.getYions();
        Assert.assertEquals(147.11334499999998, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Y_ION, lFragmentIonsArray[1].getID(), 0.0);
        Assert.assertEquals(925.4089290000001, lFragmentIonsArray[6].getMZ(), 0.0);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
    }


    /**
     * This method will test the B and Y ion genereation in the constructor. The identifications that will be tested
     * carry a fixed Cterminal modification.
     */
    public void testBYIons2() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F016486.dat"));
        //PGVTVKDVNQQEFVR-O18*
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(158, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        FragmentIon[] lFragmentIonsArray = null;
        //Get Bions.
        lFragmentIonsArray = lPeptideHitAnnotation.getBions();
        Assert.assertEquals(114.091889, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(1479.7331139999997, lFragmentIonsArray[14].getMZ(), 0.0);
        try {
            lFragmentIonsArray[15].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        //Get Yions.
        lFragmentIonsArray = lPeptideHitAnnotation.getYions();
        Assert.assertEquals(179.12799199999998, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Y_ION, lFragmentIonsArray[1].getID(), 0.0);
        Assert.assertEquals(1544.769217, lFragmentIonsArray[14].getMZ(), 0.0);
        try {
            lFragmentIonsArray[15].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        String lModifiedSequence = lPeptideHit.getModifiedSequence();
        Assert.assertEquals("NH2-IVQSPDVIPADSEAGR-C18O*", lModifiedSequence);
    }

    /**
     * This method will test the B and Y ion genereation in the constructor. The identifications that will be tested
     * carry a fixed Cterminal modification.
     */
    public void testBYIons3() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F016528.dat"));
        //Ace*-PGVTVKDVNQQEFVR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(961, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        FragmentIon[] lFragmentIonsArray = null;

        //Get Yions.
        lFragmentIonsArray = lPeptideHitAnnotation.getYions();
        Assert.assertEquals(175.11950099999999, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Y_ION, lFragmentIonsArray[1].getID(), 0.0);
        Assert.assertEquals(2683.217138, lFragmentIonsArray[25].getMZ(), 0.0);
        try {
            lFragmentIonsArray[26].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }

        //Get Bions.
        lFragmentIonsArray = lPeptideHitAnnotation.getBions();
        Assert.assertEquals(158.045333, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(2666.14297, lFragmentIonsArray[25].getMZ(), 0.0);
        try {
            lFragmentIonsArray[26].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        String lModifiedSequence = lPeptideHit.getModifiedSequence();
        Assert.assertEquals("Ace*-DDDIAALVVDNGSGM<Mox*>C<Cmm*>K<AcD3K*>AGFAGDDAPR-COOH", lModifiedSequence);

    }


    /**
     * This method tests the calculation of the ions minus NH3.
     */
    public void testCZIons() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //QQMENYPK
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(263, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        //get Cions.
        FragmentIon[] lFragmentIonsArray = lPeptideHitAnnotation.getCions();
        Assert.assertEquals(129.066419, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("c", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.C_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(907.3620030000002, lFragmentIonsArray[6].getMZ(), 0.0);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        //get CDouble ions.
        lFragmentIonsArray = lPeptideHitAnnotation.getCDoubleions();
        Assert.assertEquals(FragmentIon.C_DOUBLE_ION, lFragmentIonsArray[0].getID());
        Assert.assertEquals(65.037122, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("c++", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        //get Zions.
        lFragmentIonsArray = lPeptideHitAnnotation.getZions();
        Assert.assertEquals(130.086796, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("z", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Z_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(227.139556, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(390.20288600000003, lFragmentIonsArray[2].getMZ(), 0.0);
        Assert.assertEquals(504.24581600000005, lFragmentIonsArray[3].getMZ(), 0.0);
        Assert.assertEquals(633.288406, lFragmentIonsArray[4].getMZ(), 0.0);
        Assert.assertEquals(780.3238, lFragmentIonsArray[5].getMZ(), 0.0);
        Assert.assertEquals(908.38238, lFragmentIonsArray[6].getMZ(), 0.0);
        //get ZDouble ions.
        lFragmentIonsArray = lPeptideHitAnnotation.getZDoubleions();
        Assert.assertEquals(FragmentIon.Z_DOUBLE_ION, lFragmentIonsArray[0].getID());
        Assert.assertEquals(65.5473105, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("z++", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
    }

    /**
     * This method tests the calculation of the ions minus NH3.
     */
    public void testCZIons2() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F024982.dat"));
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(1916, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        //get Cions.
        FragmentIon[] lFragmentIonsArray = lPeptideHitAnnotation.getZHHDoubleions();
        Assert.assertEquals(FragmentIon.ZHH_DOUBLE_ION, lFragmentIonsArray[0].getID());
        Assert.assertEquals("zHH++", lFragmentIonsArray[0].getType());
        Assert.assertEquals(83.5682765, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());

        Query q = lMascotDatfile.getQuery(1916);
        Peak[] lPeaks = q.getPeakList();


        Vector lFusedMatchedIons = lPeptideHitAnnotation.getFusedMatchedIons(lPeaks, lPeptideHit.getPeaksUsedFromIons1(), q.getMaxIntensity(), 0.1);

        Assert.assertEquals(15, lFusedMatchedIons.size());
    }

    /**
     * This method tests the calculation of the A- and X-ions.
     */
    public void testAXIons() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //QQMENYPK
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(263, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        //get Aions.
        FragmentIon[] lFragmentIonsArray = lPeptideHitAnnotation.getAions();
        Assert.assertEquals(84.04495500000002, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("a", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.A_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(862.3405390000001, lFragmentIonsArray[6].getMZ(), 0.0);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        //get double Aions.
        lFragmentIonsArray = lPeptideHitAnnotation.getADoubleions();
        Assert.assertEquals(42.526390000000006, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("a++", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.A_DOUBLE_ION, lFragmentIonsArray[1].getID());

        //get Xions.
        lFragmentIonsArray = lPeptideHitAnnotation.getXions();
        Assert.assertEquals(173.09260999999998, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("x", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.X_ION, lFragmentIonsArray[1].getID());
        Assert.assertFalse("isDoubleCharged method is not working correctly! this " + lFragmentIonsArray[0].getType() + "fragmention is not double charged but returns true on the isDoubleCharged() method.", lFragmentIonsArray[0].isDoubleCharged());

        //get double Xions.
        lFragmentIonsArray = lPeptideHitAnnotation.getXDoubleions();
        Assert.assertEquals(87.05021749999999, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("x++", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.X_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertTrue("isDoubleCharged method is not working correctly! this " + lFragmentIonsArray[0].getType() + "fragmention is double charged but returns false on the isDoubleCharged() method.", lFragmentIonsArray[0].isDoubleCharged());
    }

    /**
     * This method tests the calculation of the double charged ions.
     */
    public void testDoubleIons() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //QQMENYPK
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(263, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        FragmentIon[] lFragmentIonsArray = lPeptideHitAnnotation.getBDoubleions();
        Assert.assertEquals(56.5238475, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b++", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(194.07083450000005, lFragmentIonsArray[2].getMZ(), 0.0);
        Assert.assertEquals(258.59212950000006, lFragmentIonsArray[3].getMZ(), 0.0);
        Assert.assertEquals(315.61359450000003, lFragmentIonsArray[4].getMZ(), 0.0);
        Assert.assertEquals(397.14525950000007, lFragmentIonsArray[5].getMZ(), 0.0);
        Assert.assertEquals(445.6716395000001, lFragmentIonsArray[6].getMZ(), 0.0);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        lFragmentIonsArray = lPeptideHitAnnotation.getYDoubleions();
        Assert.assertEquals(74.06058499999999, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y++", lFragmentIonsArray[0].getType());
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Y_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(122.58696499999999, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(204.11863000000002, lFragmentIonsArray[2].getMZ(), 0.0);
        Assert.assertEquals(261.14009500000003, lFragmentIonsArray[3].getMZ(), 0.0);
        Assert.assertEquals(325.66139000000004, lFragmentIonsArray[4].getMZ(), 0.0);
        Assert.assertEquals(399.17908700000004, lFragmentIonsArray[5].getMZ(), 0.0);
        Assert.assertEquals(463.20837700000004, lFragmentIonsArray[6].getMZ(), 0.0);
    }

    /**
     * This method tests the calculation of the ions minus H2O.
     */
    public void testMinusH2OIons() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //QQMENYPK
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(263, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        //get Bions minus H2O.
        FragmentIon[] lFragmentIonsArray = lPeptideHitAnnotation.getBH2Oions();
        Assert.assertEquals(498.1658690000001, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b-H2O", lFragmentIonsArray[0].getType());
        Assert.assertEquals(612.208799, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(5, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_H2O_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(775.2721290000001, lFragmentIonsArray[2].getMZ(), 0.0);
        Assert.assertEquals(872.3248890000001, lFragmentIonsArray[3].getMZ(), 0.0);
        Assert.assertEquals(4, lFragmentIonsArray.length);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        //get double charged Bions minus H2O.
        lFragmentIonsArray = lPeptideHitAnnotation.getBDoubleH2Oions();
        Assert.assertEquals(249.58684700000006, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b++-H2O", lFragmentIonsArray[0].getType());
        Assert.assertEquals(5, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_H2O_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(436.66635700000006, lFragmentIonsArray[3].getMZ(), 0.0);
        Assert.assertEquals(4, lFragmentIonsArray.length);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }

        //get Yions minus H2O.
        lFragmentIonsArray = lPeptideHitAnnotation.getYH2Oions();
        Assert.assertEquals(632.30439, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y-H2O", lFragmentIonsArray[0].getType());
        Assert.assertEquals(779.339784, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(6, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Y_H2O_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(907.398364, lFragmentIonsArray[2].getMZ(), 0.0);
        Assert.assertEquals(3, lFragmentIonsArray.length);

        //get double charged Yions minus H2O.
        lFragmentIonsArray = lPeptideHitAnnotation.getYDoubleH2Oions();
        Assert.assertEquals(316.6561075, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y++-H2O", lFragmentIonsArray[0].getType());
        Assert.assertEquals(390.1738045, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(6, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Y_H2O_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(454.2030945, lFragmentIonsArray[2].getMZ(), 0.0);
        Assert.assertEquals(3, lFragmentIonsArray.length);

        //get Aions minus H2O.
        lFragmentIonsArray = lPeptideHitAnnotation.getAH2Oions();
        Assert.assertEquals(470.1709540000001, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("a-H2O", lFragmentIonsArray[0].getType());
        Assert.assertEquals(5, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.A_H2O_ION, lFragmentIonsArray[1].getID());

        //get Aions minus H2O.
        lFragmentIonsArray = lPeptideHitAnnotation.getADoubleH2Oions();
        Assert.assertEquals(235.58938950000007, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("a++-H2O", lFragmentIonsArray[0].getType());
        Assert.assertEquals(7, lFragmentIonsArray[3].getNumber());
        Assert.assertEquals(FragmentIon.A_H2O_DOUBLE_ION, lFragmentIonsArray[1].getID());

    }

    /**
     * This method tests the calculation of the ions minus NH3.
     */
    public void testMinusNH3Ions() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //METVQLR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(174, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        //get Bions minus NH3.
        FragmentIon[] lFragmentIonsArray = lPeptideHitAnnotation.getBNH3ions();
        Assert.assertEquals(630.2444889999999, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b-NH3", lFragmentIonsArray[0].getType());
        Assert.assertEquals(743.328549, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(6, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_NH3_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(2, lFragmentIonsArray.length);
        try {
            lFragmentIonsArray[2].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }
        //get double charged Bions minus NH3.
        lFragmentIonsArray = lPeptideHitAnnotation.getBDoubleNH3ions();
        Assert.assertEquals(315.62224449999997, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("b++-NH3", lFragmentIonsArray[0].getType());
        Assert.assertEquals(6, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.B_NH3_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(2, lFragmentIonsArray.length);
        try {
            lFragmentIonsArray[7].getMZ();
            fail("The array with double charged ions is bigger then it should be.");
        } catch (ArrayIndexOutOfBoundsException aiob) {
            //Do nothing because an error should be thrown!
        }

        //get Yions minus NH3.
        lFragmentIonsArray = lPeptideHitAnnotation.getYNH3ions();
        Assert.assertEquals(158.092946, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y-NH3", lFragmentIonsArray[0].getType());
        Assert.assertEquals(271.177006, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(2, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(FragmentIon.Y_NH3_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(728.394266, lFragmentIonsArray[5].getMZ(), 0.0);
        Assert.assertEquals(6, lFragmentIonsArray.length);

        //get double charged Yions minus NH3.
        lFragmentIonsArray = lPeptideHitAnnotation.getYDoubleNH3ions();
        Assert.assertEquals(79.546473, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("y++-NH3", lFragmentIonsArray[0].getType());
        Assert.assertEquals(136.088503, lFragmentIonsArray[1].getMZ(), 0.0);
        Assert.assertEquals(6, lFragmentIonsArray[5].getNumber());
        Assert.assertEquals(FragmentIon.Y_NH3_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(6, lFragmentIonsArray.length);

        //get Aions minus NH3.
        lFragmentIonsArray = lPeptideHitAnnotation.getANH3ions();
        Assert.assertEquals(602.2495739999999, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("a-NH3", lFragmentIonsArray[0].getType());
        Assert.assertEquals(6, lFragmentIonsArray[1].getNumber());
        Assert.assertEquals(17, lFragmentIonsArray[1].getID());
        Assert.assertEquals(FragmentIon.A_NH3_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(2, lFragmentIonsArray.length);

        //get Aions minus NH3.
        lFragmentIonsArray = lPeptideHitAnnotation.getADoubleNH3ions();
        Assert.assertEquals(301.62478699999997, lFragmentIonsArray[0].getMZ(), 0.0);
        Assert.assertEquals("a++-NH3", lFragmentIonsArray[0].getType());
        Assert.assertEquals(5, lFragmentIonsArray[0].getNumber());
        Assert.assertEquals(FragmentIon.A_NH3_DOUBLE_ION, lFragmentIonsArray[1].getID());
        Assert.assertEquals(2, lFragmentIonsArray.length);
    }

    public void testSignificantTheoreticalFragmentions() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //METVQLR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(174, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        Vector lSignificantFragmentionsVec = lPeptideHitAnnotation.getSignificantTheoreticalFragmentions();

        Assert.assertEquals(lSignificantFragmentionsVec.size(), 27);
        FragmentIon fm = null;
        //b1
        fm = (FragmentIon) lSignificantFragmentionsVec.get(0);
        Assert.assertEquals("b", fm.getType());
        Assert.assertEquals(190.053778, fm.getMZ(), 0.0);
        Assert.assertEquals(1, fm.getNumber());

        //b6
        fm = (FragmentIon) lSignificantFragmentionsVec.get(5);
        Assert.assertEquals("b", fm.getType());
        Assert.assertEquals(760.355098, fm.getMZ(), 0.0);
        Assert.assertEquals(6, fm.getNumber());

        //b5-NH3
        fm = (FragmentIon) lSignificantFragmentionsVec.get(6);
        Assert.assertEquals("b-NH3", fm.getType());
        Assert.assertEquals(630.2444889999999, fm.getMZ(), 0.0);

        //b2-H2O
        fm = (FragmentIon) lSignificantFragmentionsVec.get(8);
        Assert.assertEquals("b-H2O", fm.getType());
        Assert.assertEquals(301.085803, fm.getMZ(), 0.0);

        //b6-H2O
        fm = (FragmentIon) lSignificantFragmentionsVec.get(12);
        Assert.assertEquals("b-H2O", fm.getType());
        Assert.assertEquals(742.344533, fm.getMZ(), 0.0);
        Assert.assertEquals(6, fm.getNumber());
        Assert.assertEquals(3, fm.getID());

        //y1
        fm = (FragmentIon) lSignificantFragmentionsVec.get(13);
        Assert.assertEquals("y", fm.getType());
        Assert.assertEquals(175.119495, fm.getMZ(), 0.0);
        Assert.assertEquals(1, fm.getNumber());
        Assert.assertEquals(7, fm.getID());

        //y6
        fm = (FragmentIon) lSignificantFragmentionsVec.get(18);
        Assert.assertEquals("y", fm.getType());
        Assert.assertEquals(745.4208150000001, fm.getMZ(), 0.0);
        Assert.assertEquals(6, fm.getNumber());
        Assert.assertEquals(7, fm.getID());

        //y1-NH3
        fm = (FragmentIon) lSignificantFragmentionsVec.get(19);
        Assert.assertEquals("y-NH3", fm.getType());
        Assert.assertEquals(158.092946, fm.getMZ(), 0.0);
        Assert.assertEquals(1, fm.getNumber());
        Assert.assertEquals(11, fm.getID());

        //y6-NH3
        fm = (FragmentIon) lSignificantFragmentionsVec.get(24);
        Assert.assertEquals("y-NH3", fm.getType());
        Assert.assertEquals(728.394266, fm.getMZ(), 0.0);
        Assert.assertEquals(6, fm.getNumber());
        Assert.assertEquals(11, fm.getID());

        //y5-H2O
        fm = (FragmentIon) lSignificantFragmentionsVec.get(25);
        Assert.assertEquals("y-H2O", fm.getType());
        Assert.assertEquals(598.36766, fm.getMZ(), 0.0);
        Assert.assertEquals(5, fm.getNumber());
        Assert.assertEquals(9, fm.getID());

        //y6-H2O
        fm = (FragmentIon) lSignificantFragmentionsVec.get(26);
        Assert.assertEquals("y-H2O", fm.getType());
        Assert.assertEquals(727.41025, fm.getMZ(), 0.0);
        Assert.assertEquals(6, fm.getNumber());
        Assert.assertEquals(9, fm.getID());
    }

    public void testNonSignificantTheoreticalFragmentions() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //METVQLR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(174, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        Vector lNonSignificantFragmentionsVec = lPeptideHitAnnotation.getNonSignificantTheoreticalFragmentions();

        Assert.assertEquals(lNonSignificantFragmentionsVec.size(), 27);
        FragmentIon fm = null;

        //b1++
        fm = (FragmentIon) lNonSignificantFragmentionsVec.get(0);
        Assert.assertEquals("b++", fm.getType());
        Assert.assertEquals(95.5308015, fm.getMZ(), 0.0);
        Assert.assertEquals(1, fm.getNumber());
        Assert.assertEquals(2, fm.getID());

        //b3++-H2O
        fm = (FragmentIon) lNonSignificantFragmentionsVec.get(9);
        Assert.assertEquals("b++-H2O", fm.getType());
        Assert.assertEquals(201.57065400000002, fm.getMZ(), 0.0);
        Assert.assertEquals(3, fm.getNumber());
        Assert.assertEquals(4, fm.getID());

        //y6++-NH3
        fm = (FragmentIon) lNonSignificantFragmentionsVec.get(20);
        Assert.assertEquals("y++-NH3", fm.getType());
        Assert.assertEquals(136.088503, fm.getMZ(), 0.0);
        Assert.assertEquals(2, fm.getNumber());
        Assert.assertEquals(12, fm.getID());

        //y5-H2O
        fm = (FragmentIon) lNonSignificantFragmentionsVec.get(26);
        Assert.assertEquals("y++-H2O", fm.getType());
        Assert.assertEquals(364.2090375, fm.getMZ(), 0.0);
        Assert.assertEquals(6, fm.getNumber());
        Assert.assertEquals(10, fm.getID());
    }

    public void testAllTheoreticalFragmentions() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //METVQLR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(174, 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        Vector lFragmentionsVec = lPeptideHitAnnotation.getAllTheoreticalFragmentions();
        FragmentIon lFragmentIon = null;
        Assert.assertEquals(54, lFragmentionsVec.size());
        lFragmentIon = (FragmentIon) (lFragmentionsVec.get(0));
        Assert.assertEquals("b", lFragmentIon.getType());
        lFragmentIon = (FragmentIon) (lFragmentionsVec.get(53));
        Assert.assertEquals(364.2090375, lFragmentIon.getMZ(), 0.0);
    }

    public void testMascotMatchedIons() {
        MascotDatfile lMDF = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //HDLLVGAPLYMESR
        PeptideHit lPeptideHit = lMDF.getQueryToPeptideMap().getPeptideHitOfOneQuery(797, 1);
        Vector lQueries = lMDF.getQueryList();
        Query lQuery = (Query) lQueries.get(797 - 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMDF.getMasses(), lMDF.getParametersSection());
        Vector lMatchedFragmentionsByMascotVec = lPeptideHitAnnotation.getMatchedIonsByMascot(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1());
        FragmentIon lFragmentIon = null;
        Assert.assertEquals(19, lMatchedFragmentionsByMascotVec.size());
        lFragmentIon = (FragmentIon) (lMatchedFragmentionsByMascotVec.get(0));
        Assert.assertEquals(2, lFragmentIon.getNumber());
        lFragmentIon = (FragmentIon) (lMatchedFragmentionsByMascotVec.get(18));
        Assert.assertEquals(-0.007629000000179076, lFragmentIon.getTheoreticalExperimantalMassError(), 0.0);
    }

    public void testFusedMatchedIons() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010983.dat"));
        //HDLLVGAPLYMESR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(797, 1);
        Vector lQueries = lMascotDatfile.getQueryList();
        Query lQuery = (Query) lQueries.get(797 - 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection());
        Vector lMatchedFragmentionsByFusedVec = lPeptideHitAnnotation.getFusedMatchedIons(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1(), lQuery.getMaxIntensity(), 0.10);
        Assert.assertEquals(29, lMatchedFragmentionsByFusedVec.size());
        lMatchedFragmentionsByFusedVec = lPeptideHitAnnotation.getFusedMatchedIons(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1(), lQuery.getMaxIntensity(), 0.40);
        Assert.assertEquals(27, lMatchedFragmentionsByFusedVec.size());
        FragmentIon lFragmentIon = null;
        lFragmentIon = (FragmentIon) (lMatchedFragmentionsByFusedVec.get(0));
        Assert.assertEquals(253.093675, lFragmentIon.getMZ(), 0.0);
        lFragmentIon = (FragmentIon) (lMatchedFragmentionsByFusedVec.get(20));  //immonium!
        Assert.assertEquals(0, lFragmentIon.getNumber());
        lFragmentIon = (FragmentIon) (lMatchedFragmentionsByFusedVec.get(26));
        Assert.assertEquals("iV", lFragmentIon.getType());
    }

    public void testPrecursorNeutralLoss() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010062.dat"));
        //PKTISVR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(317, 1);
        Vector lQueries = lMascotDatfile.getQueryList();
        Query lQuery = (Query) lQueries.get(317 - 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection(), lQuery.getPrecursorMZ(), lQuery.getChargeString());
        Vector lMatchedFragmentionsByFusedVec = lPeptideHitAnnotation.getFusedMatchedIons(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1(), lQuery.getMaxIntensity(), 0.05);
        Assert.assertEquals(16, lMatchedFragmentionsByFusedVec.size());
        FragmentIon lFragmentIon = null;
        lFragmentIon = (FragmentIon) (lMatchedFragmentionsByFusedVec.get(13));
        Assert.assertEquals(436.8421175, lFragmentIon.getMZ(), 0.0);
        lFragmentIon = (FragmentIon) (lMatchedFragmentionsByFusedVec.get(14));  //immonium!
        Assert.assertEquals(0, lFragmentIon.getNumber());
        Assert.assertEquals("Prec-NH3 2+", lFragmentIon.getType());
    }

    public void testGetMatchedBYions() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010062.dat"));
        //PKTISVR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(317, 1);
        Vector lQueries = lMascotDatfile.getQueryList();
        Query lQuery = (Query) lQueries.get(317 - 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection(), lQuery.getPrecursorMZ(), lQuery.getChargeString());
        Assert.assertEquals(6, lPeptideHitAnnotation.getBions().length);
        Assert.assertEquals(6, lPeptideHitAnnotation.getYions().length);
        Vector lMatchedBYions = lPeptideHitAnnotation.getMatchedBYions(lQuery.getPeakList());
        Assert.assertEquals(9, lMatchedBYions.size());
        Assert.assertEquals(6, ((FragmentIon) (lMatchedBYions.get(8))).getNumber());
        Assert.assertEquals("b3", ((FragmentIon) (lMatchedBYions.get(2))).getLabel());


    }

    public void testGetMascotIonCoverage() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010062.dat"));
        //PKTISVR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(317, 1);
        Vector lQueries = lMascotDatfile.getQueryList();
        Query lQuery = (Query) lQueries.get(317 - 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection(), lQuery.getPrecursorMZ(), lQuery.getChargeString());
        int[] lCoverage = lPeptideHitAnnotation.getMascotIonCoverage(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1());
        Assert.assertEquals(2, lCoverage[0]);
        Assert.assertEquals(5, lCoverage[1]);
        Assert.assertEquals(6, lCoverage[2]);
    }

    public void testGetFusedIonCoverage() {
        MascotDatfile lMascotDatfile = new MascotDatfile(TestCaseLM.getFullFilePath("F010062.dat"));
        //PKTISVR
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(317, 1);
        Vector lQueries = lMascotDatfile.getQueryList();
        Query lQuery = (Query) lQueries.get(317 - 1);
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection(), lQuery.getPrecursorMZ(), lQuery.getChargeString());
        int[] lCoverage = lPeptideHitAnnotation.getFusedIonCoverage(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1(), lQuery.getMaxIntensity(), 0.10);
        Assert.assertEquals(3, lCoverage[0]);
        Assert.assertEquals(6, lCoverage[1]);
        Assert.assertEquals(7, lCoverage[2]);
    }
}
