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

package com.compomics.mascotdatfile.util.mascot.index;

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import junit.TestCaseLM;
import junit.framework.Assert;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: kenny Date: 12-okt-2006 Time: 14:11:46
 */
public class TestMascotDatfile extends TestCaseLM {
    // Class specific log4j logger for TestMascotDatfile instances.
    private static Logger logger = Logger.getLogger(TestMascotDatfile.class);

    public TestMascotDatfile() {
        super("Testscenario for parts of the MascotDatfile class.");
    }

    public void testMascotDatfile() {
        String lDatfileLocation = "C:\thisCannotBeACorrectFile.dat";
        try {
            new MascotDatfile_Index(lDatfileLocation);
            fail("File " + lDatfileLocation + " cannot create a valid MascotDatfileInstance and error must have been thrown!");
        } catch (IllegalArgumentException iae) {
            //System.err.prinln("");
        }
    }

    public void testSpectrumfilenameToQuerynumberMap() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F009911.dat"));
        //2. Get the the masses object to run the tests on.
        HashMap m = lMascotDatfile.getSpectrumFilenameToQuerynumberMap();
        //3.Test some QueryNumbers.
        Assert.assertEquals(new Integer(734), (Integer) (m.get("CapLC15371.200.2.3.mgf")));
        Assert.assertEquals(new Integer(1), (Integer) (m.get("CapLC15371.426.2.1.mgf")));
        Assert.assertEquals(new Integer(817), (Integer) (m.get("CapLC15373.305.2.4.mgf")));

    }

    public void testGetProteinDescriptionbyAccessionNumber() {

        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F009911.dat"));

        //2. Test protein discription.
        String lAccession = "O14770 (450-477)";
        String lDescription = lMascotDatfile.getProteinMap().getProteinDescription(lAccession);
        Assert.assertEquals("(*CE*) MEIS2_HUMAN Homeobox protein Meis2 (Meis1-related protein 1).", lDescription);


        lDescription = lMascotDatfile.getProteinMap().getProteinDescription("This accession does not exist.");
        Assert.assertEquals("No Description.", lDescription);
    }

    public void testGetFileName() {
        String lFilename = "F009911.dat";
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath(lFilename));
        Assert.assertEquals(lFilename, lMascotDatfile.getFileName());
    }
}
