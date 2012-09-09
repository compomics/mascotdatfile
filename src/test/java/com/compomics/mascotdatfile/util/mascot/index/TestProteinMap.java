package com.compomics.mascotdatfile.util.mascot.index;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.mascotdatfile.util.mascot.ProteinID;
import com.compomics.mascotdatfile.util.mascot.ProteinMap;
import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 17-jan-2007
 * Time: 16:36:23
 */

/**
 * TestClass description: ------------------ This TestClass was developed to test the ProteinMap.
 */
public class TestProteinMap extends TestCase {
    // Class specific log4j logger for TestProteinMap instances.
    private static Logger logger = Logger.getLogger(TestProteinMap.class);
    ProteinMap iProteinMap = null;


    /**
     * Constructor.
     */
    public TestProteinMap() {
        super("Testscenario TestProteinMap. ");
        MascotDatfileInf iMascotDatfile = new MascotDatfile_Index(TestCaseLM.getFullFilePath("F009911.dat"));
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
