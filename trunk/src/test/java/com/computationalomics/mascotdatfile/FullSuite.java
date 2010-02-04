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

package com.computationalomics.mascotdatfile;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 24-feb-2006
 * Time: 15:03:49
 * This Suite tests all the components of the MascotDatfile library.
 */
public class FullSuite extends TestCase {
    public FullSuite() {
        this("Full suite of test for mascotdatfile project.");
    }

    public FullSuite(String aName) {
        super(aName);
    }

    public static Test suite() {
        TestSuite ts = new TestSuite();

        // The memory MascotDatfile TestClasses wich must be run.
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestHeader.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestMascotDatfile.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestMasses.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestModification.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestModificationConversion.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestParameters.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestPeptideHit.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestPeptideHitAnnotation.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestPeptideToQueryMap.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestProteinHit.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestProteinID.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestProteinMap.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestQuery.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestQueryToPeptideMap.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestThreshold.class));

        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestQuantitation.class));

        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.parser.TestMascotRawParser.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.TestFactory.class));

        // The indexed MascotDatfile TestClasses that must be run.
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestHeader.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestMascotDatfile.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestMasses.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestModification.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestModificationConversion.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestParameters.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestPeptideHit.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestPeptideHitAnnotation.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestPeptideToQueryMap.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestProteinHit.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestProteinID.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestProteinMap.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestQuery.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestQueryToPeptideMap.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestThreshold.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestDecoyPeptideToQueryMap.class));
        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestQuantitation.class));

        ts.addTest(new TestSuite(com.computationalomics.mascotdatfile.util.mascot.index.TestController.class));

        return ts;
    }
}
