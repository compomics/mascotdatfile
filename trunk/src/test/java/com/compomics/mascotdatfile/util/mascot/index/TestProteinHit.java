package com.compomics.mascotdatfile.util.mascot.index;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.ProteinHit;
import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 17-jan-2007
 * Time: 17:03:42
 */

/**
 * TestClass description: ------------------ This TestClass was developed to test
 */
public class TestProteinHit extends TestCase {
    // Class specific log4j logger for TestProteinHit instances.
    private static Logger logger = Logger.getLogger(TestProteinHit.class);
    ProteinHit iProteinHit;

    public TestProteinHit() {
        super("Testscenario TestProteinHit. ");
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(TestCaseLM.getFullFilePath("F009911.dat"));
        // Get a PeptideHit.
        // ew: q448_p1=0,1816.101181,-0.006109,5,FLKQLLQLKFEDR,10,100030000000000,21.37,00000020000000000,0,0;"Q8IYJ2 (120-132)":0:1:13:1
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(448);
        iProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(0);
    }

    public void testGetAccession() {
        Assert.assertEquals("Q8IYJ2 (120-132)", iProteinHit.getAccession());
    }

    public void testGetFrameNumber() {
        Assert.assertEquals(0, iProteinHit.getFrameNumber());
    }

    public void testGetStart() {
        Assert.assertEquals(1, iProteinHit.getStart());
    }

    public void testGetStop() {
        Assert.assertEquals(13, iProteinHit.getStop());
    }

    public void testGetMultiplicity() {
        Assert.assertEquals(1, iProteinHit.getMultiplicity());
    }

    public void testGetPeptideStartInProtein_PeptideCentricDatabase() {
        Assert.assertEquals(120, iProteinHit.getPeptideStartInProtein_PeptideCentricDatabase());
    }

    public void testGetPeptideStopInProtein_PeptideCentricDatabase() {
        Assert.assertEquals(132, iProteinHit.getPeptideStopInProtein_PeptideCentricDatabase());
    }

    public void testIPIProteinAccession() {
        ProteinHit lProteinHit = new ProteinHit("\"IPI:IPI00025499.1\":0:275:280:1");
        Assert.assertEquals("IPI:IPI00025499.1", lProteinHit.getAccession());
    }
}
