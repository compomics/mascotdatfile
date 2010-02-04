package com.compomics.mascotdatfile.util.mascot;

import junit.TestCaseLM;
import junit.framework.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 17-jan-2007
 * Time: 16:36:23
 */

/**
 * TestClass description:
 * ------------------
 * This TestClass was developed to test the ProteinMap.
 */
public class TestProteinMap extends TestCaseLM {
    ProteinMap iProteinMap = null;


    /**
     * Constructor.
     */
    public TestProteinMap() {
        super("Testscenario TestProteinMap. ");
        MascotDatfile iMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        iProteinMap = iMascotDatfile.getProteinMap();
    }


    /**
     * Test the proteinDescription getter.
     */
    public void testGetProteinDescription() {
        Assert.assertEquals("(*CE*) ZMY15_HUMAN Zinc finger MYND domain-containing protein 15.", iProteinMap.getProteinDescription("Q9H091 (256-262)"));
        Assert.assertNotNull(iProteinMap.getProteinDescription("Q92576 (732-737)"));
        Assert.assertEquals("(*CE*) HD_HUMAN Huntingtin (Huntington disease protein) (HD protein).", iProteinMap.getProteinDescription("P42858 (1265-1270)"));

    }

    /**
     * Test the proteinID getter.
     */
    public void testGetProteinID() {
        ProteinID lProteinID = iProteinMap.getProteinID("Q9H091 (256-262)");
        Assert.assertEquals("Q9H091 (256-262)", lProteinID.getAccession());
        Assert.assertEquals(724.39, lProteinID.getMass(), 0.0);
    }
}