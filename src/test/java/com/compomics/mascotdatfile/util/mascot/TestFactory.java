package com.compomics.mascotdatfile.util.mascot;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;
import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 14-jul-2008 Time: 15:28:11 To change this template use File | Settings |
 * File Templates.
 */
public class TestFactory extends TestCase {
    // Class specific log4j logger for TestFactory instances.
    private static Logger logger = Logger.getLogger(TestFactory.class);

    public TestFactory(String s) {
        super(s);
    }

    public void testReadMemory() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lDfid = MascotDatfileFactory.create(TestCaseLM.getFullFilePath("F009911.dat"), MascotDatfileType.MEMORY);


        Assert.assertTrue(lDfid instanceof MascotDatfile);
        //No further variables or functions in the Header instance. If this works, this object works fine.
    }

    public void testReadIndex() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lDfid = MascotDatfileFactory.create(TestCaseLM.getFullFilePath("F009911.dat"), MascotDatfileType.INDEX);
        //2. Get the the header object to run the tests on.
        Assert.assertTrue(lDfid instanceof MascotDatfile_Index);

    }
}
