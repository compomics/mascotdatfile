/*
 * Created by IntelliJ IDEA.
 * User: Lennart
 * Date: 12-mrt-03
 * Time: 16:28:23
 */
package com.computationalomics.mascotdatfile.util.mascot.parser;

import junit.TestCaseLM;
import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * CVS information:
 *
 * $Revision: 1.1 $
 * $Date: 2007/10/22 09:38:53 $
 */

/**
 * This class implements the test scenario for the MascotRawParser.
 *
 * @author Lennart Martens
 * @see com.computationalomics.mascotdatfile.util.mascot.parser.MascotRawParser
 */
public class TestMascotRawParser extends TestCaseLM {

    public TestMascotRawParser() {
        this("Test scenario for the MascotRawParser class.");
    }

    public TestMascotRawParser(String aName) {
        super(aName);
    }

    /**
     * This method test the parsing of a mascot raw file
     * in its constituent parts.
     */
    public void testParsing() {
        // First parse from String.
        try {
            BufferedReader br = new BufferedReader(new FileReader(super.getFullFilePath("F001326.dat")));
            String line = null;
            StringBuffer all = new StringBuffer();
            while((line = br.readLine()) != null) {
                all.append(line + "\n");
            }
            br.close();
            MascotRawParser mrp = new MascotRawParser(all.toString());
            Assert.assertEquals(869, mrp.getNumberOfQueries());
        } catch(IOException ioe) {
            fail("IOExcpetion during test of MascotRawParser: " + ioe.getMessage() + "!");
        }

        // Then parse from reader.
        try {
            BufferedReader br = new BufferedReader(new FileReader(super.getFullFilePath("F001326.dat")));
            MascotRawParser mrp = new MascotRawParser(br);
            br.close();
            Assert.assertEquals(869, mrp.getNumberOfQueries());
        } catch(IOException ioe) {
            fail("IOExcpetion during test of MascotRawParser: " + ioe.getMessage() + "!");
        }

        // Finally parse from file.
        try {

            MascotRawParser mrp = new MascotRawParser(new File(super.getFullFilePath("F001326.dat")));
            Assert.assertEquals(869, mrp.getNumberOfQueries());
        } catch(IOException ioe) {
            fail("IOExcpetion during test of MascotRawParser: " + ioe.getMessage() + "!");
        }

        // Now try to elicit an IllegalArgumentException by passing a non-datfile.
        try {
            MascotRawParser mrp = new MascotRawParser(new File(super.getFullFilePath("Threshold_test_F009911_conf95.txt")));
            fail("No IllegalArgumentException thrown when attempting to parse a non-datfile!");
        } catch(IOException ioe) {
            fail("IOException during test of MascotRawParser: " + ioe.getMessage() + "!");
        } catch(IllegalArgumentException iae) {
            // This is what we expected.
        }
    }
}
