package com.compomics.mascotdatfile.util.mascot.index;

import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 4-jul-2008 Time: 12:01:42 To change this template use File | Settings |
 * File Templates.
 */
public class TestController extends TestCase {
    // Class specific log4j logger for TestController instances.
    private static Logger logger = Logger.getLogger(TestController.class);
    Controller iController;

    public TestController() {
        super("Testscenario for Controller class.");
        iController = new Controller(TestCaseLM.getFullFilePath("F009911.dat"));
    }

    public void testReadPeptideLineIndex() {

        int lQueryNumber = 1;
        int lPeptideHitNumber = 1;
        String test = iController.readPeptideHit(lQueryNumber, lPeptideHitNumber);
        Assert.assertEquals("-1", test);

        lQueryNumber = 813;
        lPeptideHitNumber = 10;
        test = iController.readPeptideHit(lQueryNumber, lPeptideHitNumber);
        Assert.assertEquals("0,4004.196152,-0.202856,5,VTQLPNHVVNVVPAPSANSPVNGKLSVTKPVLQSTMR," +
                "16,000307300030070000000000000000000300400,3.99,00020000200000000,0,0" +
                ";\"P18850 (262-298)\":0:1:37:2", test);

        lQueryNumber = 817;
        lPeptideHitNumber = 1;
        test = iController.readPeptideHit(lQueryNumber, lPeptideHitNumber);
        Assert.assertEquals("-1", test);

        Assert.assertEquals(817, iController.getNumberOfQueries());


    }

}
