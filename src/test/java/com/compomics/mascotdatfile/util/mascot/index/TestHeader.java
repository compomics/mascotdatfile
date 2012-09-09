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

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.Header;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.util.junit.TestCaseLM;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 24-feb-2006 Time: 14:13:50
 */
public class TestHeader extends TestCase {
    // Class specific log4j logger for TestHeader instances.
    private static Logger logger = Logger.getLogger(TestHeader.class);

    public TestHeader() {
        super("Testscenario for header class.");
    }

    public void testReadHeader() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lDfid = new MascotDatfile_Index(TestCaseLM.getFullFilePath("F009911.dat"));
        //2. Get the the header object to run the tests on.
        Header h = lDfid.getHeaderSection();
        //3.Test ALL instance variables.
        Assert.assertEquals(7718999, h.getSequences());
        Assert.assertEquals(7718999, h.getSequences_after_tax());
        Assert.assertEquals(142534249, h.getResidues());
        Assert.assertEquals(1963, h.getExecutionTime());
        Assert.assertEquals(1140512555, h.getDate());
        Assert.assertEquals("10:02:35", h.getTime());
        Assert.assertEquals(817, h.getQueries());
        Assert.assertEquals(50, h.getMaxHits());
        Assert.assertEquals("2.1.02", h.getVersion());
        Assert.assertEquals("SP_human_truncAll_NR_20060207.fas", h.getRelease());
        Assert.assertEquals("114051059101", h.getTaskID());
        Assert.assertEquals("Taxonomy 'Homo sapiens (human)' ignored. No taxonomy indexes for this database", h.getWarnings().get(0));
        //No further variables or functions in the Header instance. If this works, this object works fine.
    }
}
