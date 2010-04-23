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

package com.compomics.mascotdatfile.util.mascot;

import org.apache.log4j.Logger;

import junit.TestCaseLM;
import junit.framework.Assert;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 24-feb-2006
 * Time: 16:00:02
 */

/**
 * This class implements the test scenario for the Masses class.
 */
public class TestMasses extends TestCaseLM {
    // Class specific log4j logger for TestMasses instances.
    private static Logger logger = Logger.getLogger(TestMasses.class);

    public TestMasses() {
        super("Testscenario for masses class.");
    }

    /**
     * This method tests all the getters.
     */
    public void testReadMasses() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        //2. Get the the masses object to run the tests on.
        Masses m = lMascotDatfile.getMasses();
        //3.Test ALL instance variables.
        Assert.assertEquals(71.037110, m.getMass("A"), 0.0);
        Assert.assertEquals(114.534930, m.getMass("B"), 0.0);
        Assert.assertEquals(160.030649, m.getMass("C"), 0.0);
        Assert.assertEquals(115.026940, m.getMass("D"), 0.0);
        Assert.assertEquals(129.042590, m.getMass("E"), 0.0);
        Assert.assertEquals(147.068410, m.getMass("F"), 0.0);
        Assert.assertEquals(57.021460, m.getMass("G"), 0.0);
        Assert.assertEquals(137.058910, m.getMass("H"), 0.0);
        Assert.assertEquals(113.084060, m.getMass("I"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("J"), 0.0);
        Assert.assertEquals(173.124358, m.getMass("K"), 0.0);
        Assert.assertEquals(113.084060, m.getMass("L"), 0.0);
        Assert.assertEquals(131.040490, m.getMass("M"), 0.0);
        Assert.assertEquals(114.042930, m.getMass("N"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("O"), 0.0);
        Assert.assertEquals(97.052760, m.getMass("P"), 0.0);
        Assert.assertEquals(128.058580, m.getMass("Q"), 0.0);
        Assert.assertEquals(162.187600, m.getMass("R"), 0.0);
        Assert.assertEquals(87.032030, m.getMass("S"), 0.0);
        Assert.assertEquals(101.047680, m.getMass("T"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("U"), 0.0);
        Assert.assertEquals(99.068410, m.getMass("V"), 0.0);
        Assert.assertEquals(186.079310, m.getMass("W"), 0.0);
        Assert.assertEquals(111.000000, m.getMass("X"), 0.0);
        Assert.assertEquals(163.063330, m.getMass("Y"), 0.0);
        Assert.assertEquals(128.550590, m.getMass("Z"), 0.0);
        Assert.assertEquals(1.007825, m.getMass("Hydrogen"), 0.0);
        Assert.assertEquals(12.000000, m.getMass("Carbon"), 0.0);
        Assert.assertEquals(14.003070, m.getMass("Nitrogen"), 0.0);
        Assert.assertEquals(15.994910, m.getMass("Oxygen"), 0.0);
        Assert.assertEquals(0.000549, m.getMass("Electron"), 0.0);
        Assert.assertEquals(17.002735, m.getMass("C_term"), 0.0);
        Assert.assertEquals(1.007825, m.getMass("N_term"), 0.0);
    }

    /**
     * This method tests the ModificationStrings.
     */
    public void testReadModifications() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfile lMascotDatfile = new MascotDatfile(super.getFullFilePath("F010062.dat"));
        //2. Get the the masses object to run the tests on.
        Masses m = lMascotDatfile.getMasses();
        //3.Test the modification Strings.

        try {
            m.getFixedModifications().get(2);
            fail("no indexOutOfBoundsException is thrown, FixedModification ArrayList is to big.");
        } catch (IndexOutOfBoundsException iobe) {
            //No error printing. If exception is'nt caught, there will be a failure with a message.
        }
        Assert.assertEquals("57.021459, Carbamidomethyl (C),C", m.getFixedModifications().get(1));

        if ("57.021459, Carbamidomethyl (C),C".equals(m.getFixedModifications().get(0))) {
            fail("FixedModifications ArrayList is not good.");
        }
        Assert.assertEquals("0.984009,Deamidation (NQ),0.000000", m.getVariableModifications().get(2));
        if ("0.984009,Deamidation (NQ),0.000000".equals(m.getVariableModifications().get(1))) {
            fail("VariableModifications ArrayList is not good.");
        }
    }

}
