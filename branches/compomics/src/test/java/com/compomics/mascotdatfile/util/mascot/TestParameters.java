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

import java.util.HashMap;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 27-feb-2006
 * Time: 11:56:15
 */

/**
 * This class implements the test scenario for the Parameters Class.
 */
public class TestParameters extends TestCaseLM {
    public TestParameters() {
        super("Testscenario for masses class.");
    }

    /**
     * Test all the instance variables.
     */
    public void testReadParameters() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F009911.dat"));
        //2. Get the the Parameter object to run the tests on.
        Parameters lParameters = lMascotDatfile.getParametersSection();
        //3.Test ALL instance variables.
        Assert.assertEquals("Licensed to: Vlaams Interuniversitair Instituut voor Biotechnologie, (2 processors).", lParameters.getLicense());
        Assert.assertTrue(null == lParameters.getMP());
        Assert.assertTrue(null == lParameters.getNM());
        Assert.assertEquals("GranzymeB-SILAC-Diff-060221-C13 | (C:\\Program Files\\Matrix Science\\Mascot Daemon\\2.SP_SILAC_QTOF_C13_4.par), submitted from Daemon on PCJOSE", lParameters.getCom());
        Assert.assertTrue(null == lParameters.getIATOL());
        Assert.assertTrue(null == lParameters.getIA2TOL());
        Assert.assertTrue(null == lParameters.getIASTOL());
        Assert.assertTrue(null == lParameters.getIBTOL());
        Assert.assertTrue(null == lParameters.getIB2TOL());
        Assert.assertTrue(null == lParameters.getIBSTOL());
        Assert.assertTrue(null == lParameters.getIYTOL());
        Assert.assertTrue(null == lParameters.getIY2TOL());
        Assert.assertTrue(null == lParameters.getIYSTOL());
        Assert.assertTrue(null == lParameters.getSEG());
        Assert.assertTrue(null == lParameters.getSEGT());
        Assert.assertTrue(null == lParameters.getSEGTU());
        Assert.assertTrue(null == lParameters.getLTOL());
        Assert.assertEquals("0.3", lParameters.getTOL());
        Assert.assertEquals("Da", lParameters.getTOLU());
        Assert.assertTrue(null == lParameters.getITH());
        Assert.assertEquals("0.3", lParameters.getITOL());
        Assert.assertEquals("Da", lParameters.getITOLU());
        Assert.assertEquals("0", lParameters.getPFA());
        Assert.assertEquals("SP_human_trunc_all", lParameters.getDatabase());
        Assert.assertEquals("Acetyl_heavy (K),Carbamidomethyl (C),Arg 6xC(13) (R)", lParameters.getFixedModifications());
        Assert.assertEquals("Monoisotopic", lParameters.getMass());
        Assert.assertEquals("no_cleavage", lParameters.getCleavage());
        Assert.assertEquals("C:\\Petra\\185.Jurkat_Granzyme_substr_SILAC\\Mergefiles\\mergefile_21022006_084216296.txt", lParameters.getFile());
        Assert.assertTrue(null == lParameters.getPeak());
        Assert.assertTrue(null == lParameters.getQue());
        Assert.assertTrue(null == lParameters.getTwo());
        Assert.assertEquals("MIS", lParameters.getSearch());
        Assert.assertEquals("Petra", lParameters.getUserName());
        Assert.assertTrue(null == lParameters.getUserEmail());
        Assert.assertEquals("2+ and 3+", lParameters.getCharge());
        Assert.assertEquals("../data/20060221/F009907.dat", lParameters.getIntermediate());
        Assert.assertEquals("AUTO", lParameters.getReport());
        Assert.assertTrue(null == lParameters.getOverview());
        Assert.assertEquals("Mascot generic", lParameters.getFormat());
        Assert.assertEquals("1.01", lParameters.getFormVersion());
        Assert.assertTrue(null == lParameters.getFrag());
        Assert.assertEquals("Acetyl (N-term),Acetyl_heavy (N-term),Deamidation (NQ),Oxidation (M),Pyro-cmC (N-term camC),Pyro-glu (N-term Q),Pro 5xC(13) (P)", lParameters.getVariableModifications());
        Assert.assertTrue(null == lParameters.getUser01());
        Assert.assertTrue(null == lParameters.getUser02());
        Assert.assertTrue(null == lParameters.getUser03());
        Assert.assertTrue(null == lParameters.getUser04());
        Assert.assertTrue(null == lParameters.getUser05());
        Assert.assertTrue(null == lParameters.getUser06());
        Assert.assertTrue(null == lParameters.getUser07());
        Assert.assertTrue(null == lParameters.getUser08());
        Assert.assertTrue(null == lParameters.getUser09());
        Assert.assertTrue(null == lParameters.getUser10());
        Assert.assertTrue(null == lParameters.getUser11());
        Assert.assertTrue(null == lParameters.getUser12());
        Assert.assertTrue(null == lParameters.getPrecursor());
        Assert.assertEquals(". . . . . . . . . . . . . . . . Homo sapiens (human)", lParameters.getTaxonomy());
        Assert.assertTrue(null == lParameters.getAccession());
        Assert.assertEquals("Peptide", lParameters.getReportType());
        Assert.assertTrue(null == lParameters.getSubcluster());
        Assert.assertTrue(null == lParameters.getICAT());
        Assert.assertEquals("ESI-QUAD-TOF", lParameters.getInstrument());
        Assert.assertTrue(null == lParameters.getErrorTolerant());
        Assert.assertTrue(null == lParameters.getFrames());
        Assert.assertTrue(null == lParameters.getCutout());
        Assert.assertFalse(lParameters.isDistillerMultiFile());
        Assert.assertFalse(lParameters.isDistillerProcessing());
        Assert.assertNull(lParameters.getDistillerMultiFileNames());
        //Test int[] rules (Make a sum, if the sum is'nt 72, fail() will be called.
        int[] lRules = lParameters.getRules();
        int sum = 0;
        for (int i = 0; i < lRules.length; i++) {
            sum += lRules[i];
        }
        Assert.assertEquals("lRules int[] was not formed correctly.", 72, sum);
    }


    /**
     * Test all the instance variables.
     */
    public void testReadDistillerParameters() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfile lMascotDatfile = new MascotDatfile(getFullFilePath("F000002.dat"));
        //2. Get the the Parameter object to run the tests on.
        Parameters lParameters = lMascotDatfile.getParametersSection();
        //3.Test ALL instance variables.
        Assert.assertTrue(lParameters.isDistillerMultiFile());
        Assert.assertEquals("iTRAQ 4plex", lParameters.getQuantiation());

        String[] lDistillerMultiFilenames = lParameters.getDistillerMultiFileNames();
        Assert.assertEquals(9, lDistillerMultiFilenames.length);
        Assert.assertEquals("{1}C:\\Users\\mascot\\Desktop\\Julibu\\plt proteome\\wiff-rohdaten\\Met COFRADIC iTRAQ\\QstarE04494.wiff", lDistillerMultiFilenames[0]);
        Assert.assertEquals("{1}C:\\Users\\mascot\\Desktop\\Julibu\\plt proteome\\wiff-rohdaten\\Met COFRADIC iTRAQ\\QstarE04510.wiff", lDistillerMultiFilenames[8]);


        Assert.assertTrue(lParameters.isDistillerProcessing());
        HashMap hm = lParameters.getDistillerOptions();
        Assert.assertEquals("2.2.1.0", hm.get("_DISTILLER_MDRO_VERSION"));
    }


}



