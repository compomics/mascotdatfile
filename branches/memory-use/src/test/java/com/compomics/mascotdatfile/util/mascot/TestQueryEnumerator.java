package com.compomics.mascotdatfile.util.mascot;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;
import com.compomics.mascotdatfile.util.mascot.iterator.QueryEnumerator;
import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 14-jul-2008 Time: 15:28:11 To change this template use File | Settings |
 * File Templates.
 */
public class TestQueryEnumerator extends TestCase {
    // Class specific log4j logger for TestQueryEnumerator instances.
    private static Logger logger = Logger.getLogger(TestQueryEnumerator.class);

    public TestQueryEnumerator(String s) {
        super(s);
    }

    public void testReadMemory() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lDfid = MascotDatfileFactory.create(TestCaseLM.getFullFilePath("F016486.dat"), MascotDatfileType.MEMORY);
        QueryEnumerator e = lDfid.getQueryEnumerator();

        int lCounter = 0;

        while (e.hasMoreElements()) {
            if (e.nextElement() instanceof Query) {
                lCounter++;
            }
        }
        // There are 248 Queries in the Mascot result file.
        Assert.assertEquals(lCounter, 248);

    }
}
