package com.compomics.mascotdatfile.util.mascot.index;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.ProteinHit;
import com.compomics.mascotdatfile.util.mascot.ProteinID;
import junit.TestCaseLM;
import junit.framework.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 17-jan-2007
 * Time: 11:41:09
 */

/**
 * TestClass description:
 * ------------------
 * This TestClass was developed to test
 */
public class TestProteinID extends TestCaseLM {
    ProteinID iProteinID;

    public TestProteinID() {
        super("Testscenario TestProteinID. ");
        iProteinID = new ProteinID("testAccession", 1000.0, "testDescription");
    }


    public void testGetMass() {
        Assert.assertEquals(1000.0, iProteinID.getMass(), 0.0);
    }

    public void testGetAccession() {
        Assert.assertEquals("testAccession", iProteinID.getAccession());
    }

    public void testGetDescription() {
        Assert.assertEquals("testDescription", iProteinID.getDescription());
    }

    public void testGetQueryNumbers() {
        ProteinID lProteinID = new ProteinID("testAccession", 1000.0, "testDescription");
        // ex. The protein was identified in Query 50, PeptideHit 1.
        lProteinID.addSource(50, 1);
        lProteinID.addSource(55, 3);
        int[] lQueryNumbers = lProteinID.getQueryNumbers();
        Assert.assertEquals(50, lQueryNumbers[0]);
        Assert.assertEquals(55, lQueryNumbers[1]);
        Assert.assertEquals(2, lQueryNumbers.length);
    }

    public void testGetQueryNumbersAndPeptideHits() {
        ProteinID lProteinID = new ProteinID("testAccession", 1000.0, "testDescription");
        // ex. The protein was identified in Query 50-55-80 resp in PeptideHit 1-3-8.
        lProteinID.addSource(50, 1);
        lProteinID.addSource(55, 3);
        lProteinID.addSource(80, 8);

        int[][] lQueryNumbersAndPeptideHits = lProteinID.getQueryNumbersAndPeptideHits();
        Assert.assertEquals(50, lQueryNumbersAndPeptideHits[0][0]);
        Assert.assertEquals(1, lQueryNumbersAndPeptideHits[0][1]);
        Assert.assertEquals(80, lQueryNumbersAndPeptideHits[2][0]);
        Assert.assertEquals(8, lQueryNumbersAndPeptideHits[2][1]);
        Assert.assertEquals(3, lQueryNumbersAndPeptideHits.length);
    }

    public void testGetQueryAndPeptideHitsFromMascotDatfile() {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F009911.dat"));
        // Get a PeptideHit.
        // ew: q448_p1=0,1816.101181,-0.006109,5,FLKQLLQLKFEDR,10,100030000000000,21.37,00000020000000000,0,0;"Q8IYJ2 (120-132)":0:1:13:1
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(614, 1);
        lMascotDatfile.getQueryToPeptideMap().buildProteinMap();
        String lAccession = ((ProteinHit) lPeptideHit.getProteinHits().get(0)).getAccession();
        Assert.assertEquals("O60832 (2-18)", lAccession);
        ProteinID lProteinID = lMascotDatfile.getProteinMap().getProteinID(lAccession);
        int[] lQueryNumbers = lProteinID.getQueryNumbers();
        Assert.assertEquals(2, lQueryNumbers.length);
        Assert.assertEquals(615, lQueryNumbers[1]);

        lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(519, 1);
        lAccession = ((ProteinHit) lPeptideHit.getProteinHits().get(0)).getAccession();
        Assert.assertEquals("Q92794 (996-1012)", lAccession);
        lProteinID = lMascotDatfile.getProteinMap().getProteinID(lAccession);
        int[][] lQueryNumbersAndPeptideHits = lProteinID.getQueryNumbersAndPeptideHits();
        Assert.assertEquals(4, lQueryNumbersAndPeptideHits.length);
        Assert.assertEquals(519, lQueryNumbersAndPeptideHits[0][0]);
        Assert.assertEquals(519, lQueryNumbersAndPeptideHits[1][0]);
        Assert.assertEquals(519, lQueryNumbersAndPeptideHits[1][0]);
        Assert.assertEquals(519, lQueryNumbersAndPeptideHits[1][0]);
        Assert.assertEquals(1, lQueryNumbersAndPeptideHits[0][1]);
        Assert.assertEquals(5, lQueryNumbersAndPeptideHits[1][1]);
        Assert.assertEquals(6, lQueryNumbersAndPeptideHits[2][1]);
        Assert.assertEquals(7, lQueryNumbersAndPeptideHits[3][1]);
    }

}