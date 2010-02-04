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

import com.compomics.mascotdatfile.util.mascot.ModificationConversion;
import junit.TestCaseLM;
import junit.framework.Assert;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 3-mrt-2003
 * Time: 14:42:50
 */

/**
 * This class implements the test scenario for the TestModificationConversion Class.
 */
public class TestModificationConversion extends TestCaseLM {

    /**
     * Constructor.
     */
    public TestModificationConversion() {
        super("Testscenario for ModificationConversion class.");
    }

    /**
     * Test the modificationCoversion file.
     */
    public void testGetShortType() {
        Assert.assertEquals("CmmOx", ModificationConversion.getShortType("Carbamidomethyloxide (C)"));
        Assert.assertEquals("Ysb", ModificationConversion.getShortType("Tyr-SB (Y)"));
        Assert.assertEquals("S18O", ModificationConversion.getShortType("O18Ser (S)"));
        Assert.assertEquals("CO1", ModificationConversion.getShortType("Lys-CO1 (K)"));
        Assert.assertEquals("#Test-Modification-Not-In-File#", ModificationConversion.getShortType("Test-Modification-Not-In-File"));
        Assert.assertEquals(ModificationConversion.getShortType("Methyl ester (C-term)"), ModificationConversion.getShortType("Methyl ester (DE)"));
    }
}