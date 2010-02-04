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

import junit.TestCaseLM;
import junit.framework.Assert;

import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 27-feb-2006
 * Time: 19:01:27
 */

/**
 * This class implements the test scenario for the class.
 */
public class TestModification extends TestCaseLM {
    public TestModification() {
        super("Testscenario for ModificationList class.");
    }

    /**
     * method
     * 'equal' tests on the instance variables of a FixedModification Vector of the ModificationList Class.
     */
    public void testReadFixedModification() {
        // 1. Create a MascotDatfile instance.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        // 2. Get the ModificationList Instance.
        ModificationList lModificationList = lMascotDatfile.getModificationList();

        Vector lFixedModificationsVec = lModificationList.getFixedModifications();
        FixedModification lFixedModification = null;

        lFixedModification = (FixedModification) lFixedModificationsVec.get(0);
        Assert.assertEquals(45.029398, lFixedModification.getMass(), 0.0);
        Assert.assertEquals("Acetyl_heavy", lFixedModification.getType());
        Assert.assertEquals("K", lFixedModification.getLocation());
        Assert.assertEquals(1, lFixedModification.getModificationID());
        Assert.assertEquals("AcD3K", lFixedModification.getShortType());

        lFixedModification = (FixedModification) lFixedModificationsVec.get(1);
        Assert.assertEquals(57.021459, lFixedModification.getMass(), 0.0);
        Assert.assertEquals("Carbamidomethyl", lFixedModification.getType());
        Assert.assertEquals("C", lFixedModification.getLocation());
        Assert.assertEquals("Cmm", lFixedModification.getShortType());

        lFixedModification = (FixedModification) lFixedModificationsVec.get(2);
        Assert.assertEquals(6.086490, lFixedModification.getMass(), 0.0);
        Assert.assertEquals("Arg 6xC(13)", lFixedModification.getType());
        Assert.assertEquals("R", lFixedModification.getLocation());
        Assert.assertEquals("C13", lFixedModification.getShortType());

        try {
            lFixedModification = (FixedModification) lFixedModificationsVec.get(3);
            Assert.assertEquals(lFixedModification.getModificationID(), 4);
            fail("iFMod FixedModification Vector is to big!");
        } catch (IndexOutOfBoundsException ioob) {
            //no stacktrace printing, we only need a message if the error isnt catched!
        }

    }

    /**
     * method
     * 'equal' tests on the instance variables of a VariableModification Vector of the ModificationList Class(F009911.Dat).
     */
    public void testReadVariableModification() {

        // 1. Create a MascotDatfile instance.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        // 2. Get the ModificationList Instance.
        ModificationList lModificationList = lMascotDatfile.getModificationList();

        Vector lVariableModificationsVec = lModificationList.getVariableModifications();
        VariableModification lVariableModification = null;

        //First modification_Acetyl
        lVariableModification = (VariableModification) lVariableModificationsVec.get(0);
        Assert.assertEquals(42.010559, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Acetyl", lVariableModification.getType());
        Assert.assertEquals("N-term", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(1, lVariableModification.getModificationID());
        Assert.assertEquals("Ace", lVariableModification.getShortType());

        //Second modification_Acetyl_heavy
        lVariableModification = (VariableModification) lVariableModificationsVec.get(1);
        Assert.assertEquals(45.029388, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Acetyl_heavy", lVariableModification.getType());
        Assert.assertEquals("N-term", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(2, lVariableModification.getModificationID());
        Assert.assertEquals("AcD3", lVariableModification.getShortType());

        //Thirth modification_Deamidation
        lVariableModification = (VariableModification) lVariableModificationsVec.get(2);
        Assert.assertEquals(0.984009, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Deamidation", lVariableModification.getType());
        Assert.assertEquals("NQ", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(3, lVariableModification.getModificationID());
        Assert.assertEquals("Dam", lVariableModification.getShortType());

        //Fourth modification_Oxidation
        lVariableModification = (VariableModification) lVariableModificationsVec.get(3);
        Assert.assertEquals(15.994904, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Oxidation", lVariableModification.getType());
        Assert.assertEquals("M", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(4, lVariableModification.getModificationID());
        Assert.assertEquals("Mox", lVariableModification.getShortType());

        //Fifth modification_Pyro-cmC
        lVariableModification = (VariableModification) lVariableModificationsVec.get(4);
        Assert.assertEquals(-17.026535, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Pyro-cmC", lVariableModification.getType());
        Assert.assertEquals("N-term camC", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(5, lVariableModification.getModificationID());
        Assert.assertEquals("Pyc", lVariableModification.getShortType());

        //Sixth modification_Pyro-glu
        lVariableModification = (VariableModification) lVariableModificationsVec.get(5);
        Assert.assertEquals(-17.026535, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Pyro-glu", lVariableModification.getType());
        Assert.assertEquals("N-term Q", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(6, lVariableModification.getModificationID());
        Assert.assertEquals("Pyr", lVariableModification.getShortType());

        //Seventh modification_Pro 5xC(13)
        lVariableModification = (VariableModification) lVariableModificationsVec.get(6);
        Assert.assertEquals(5.000000, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Pro 5xC(13)", lVariableModification.getType());
        Assert.assertEquals("P", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(7, lVariableModification.getModificationID());
        Assert.assertEquals("C13", lVariableModification.getShortType());

        Assert.assertFalse(lVariableModificationsVec.get(0).equals(lVariableModificationsVec.get(1)));


        lMascotDatfile = new MascotDatfile(getFullFilePath("F009154.dat"));
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(422);
        Assert.assertEquals(1, lPeptideHit.getVariableModificationsArray()[8]);




    }

    public void testModificationInterface() {
        // 1. Create a MascotDatfile instance.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        // 2. Get the ModificationList Instance.
        ModificationList lModificationList = lMascotDatfile.getModificationList();
    }

    public void testReadFixedModificationFromParameters() {
        // 1. Create a MascotDatfile instance.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F001326.dat"));
        // 2. Get the ModificationList Instance.
        ModificationList lModificationList = lMascotDatfile.getModificationList();
        Vector lFixedModificationsVec = lModificationList.getFixedModifications();
        FixedModification lFixedModification = (FixedModification) lFixedModificationsVec.get(0);
        Assert.assertEquals("Acetyl", lFixedModification.getType());
        Assert.assertEquals("K", lFixedModification.getLocation());
        Assert.assertEquals(1, lFixedModification.getModificationID());
        Assert.assertEquals("Ace", lFixedModification.getShortType());
        boolean lMustBeFalse = true;
        try {
            lFixedModification.getMass();
        } catch (IllegalAccessError ia) {
            lMustBeFalse = false;
        }
        Assert.assertFalse(lMustBeFalse);


    }

}

