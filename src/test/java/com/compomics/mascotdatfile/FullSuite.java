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

package com.compomics.mascotdatfile;

import com.compomics.mascotdatfile.util.mascot.*;
import com.compomics.mascotdatfile.util.mascot.TestHeader;
import com.compomics.mascotdatfile.util.mascot.TestMascotDatfile;
import com.compomics.mascotdatfile.util.mascot.TestMasses;
import com.compomics.mascotdatfile.util.mascot.TestModification;
import com.compomics.mascotdatfile.util.mascot.TestModificationConversion;
import com.compomics.mascotdatfile.util.mascot.TestParameters;
import com.compomics.mascotdatfile.util.mascot.TestPeptideHit;
import com.compomics.mascotdatfile.util.mascot.TestPeptideHitAnnotation;
import com.compomics.mascotdatfile.util.mascot.TestPeptideToQueryMap;
import com.compomics.mascotdatfile.util.mascot.TestProteinHit;
import com.compomics.mascotdatfile.util.mascot.TestProteinID;
import com.compomics.mascotdatfile.util.mascot.TestProteinMap;
import com.compomics.mascotdatfile.util.mascot.TestQuantitation;
import com.compomics.mascotdatfile.util.mascot.TestQuery;
import com.compomics.mascotdatfile.util.mascot.TestQueryToPeptideMap;
import com.compomics.mascotdatfile.util.mascot.TestThreshold;
import com.compomics.mascotdatfile.util.mascot.index.*;
import com.compomics.mascotdatfile.util.mascot.parser.TestMascotRawParser;
import org.apache.log4j.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 24-feb-2006 Time: 15:03:49 This Suite tests all the components of the
 * MascotDatfile library.
 */
public class FullSuite extends TestCase {
    // Class specific log4j logger for FullSuite instances.
    private static Logger logger = Logger.getLogger(FullSuite.class);

    public FullSuite() {
        this("Full suite of test for mascotdatfile project.");
    }

    public FullSuite(String aName) {
        super(aName);
    }

    public static Test suite() {
        TestSuite ts = new TestSuite();

        // The memory MascotDatfile TestClasses wich must be run.
        ts.addTest(new TestSuite(TestHeader.class));
        ts.addTest(new TestSuite(TestMascotDatfile.class));
        ts.addTest(new TestSuite(TestMasses.class));
        ts.addTest(new TestSuite(TestModification.class));
        ts.addTest(new TestSuite(TestModificationConversion.class));
        ts.addTest(new TestSuite(TestParameters.class));
        ts.addTest(new TestSuite(TestPeptideHit.class));
        ts.addTest(new TestSuite(TestPeptideHitAnnotation.class));
        ts.addTest(new TestSuite(TestPeptideToQueryMap.class));
        ts.addTest(new TestSuite(TestProteinHit.class));
        ts.addTest(new TestSuite(TestProteinID.class));
        ts.addTest(new TestSuite(TestProteinMap.class));
        ts.addTest(new TestSuite(TestQuery.class));
        ts.addTest(new TestSuite(TestQueryToPeptideMap.class));
        ts.addTest(new TestSuite(TestThreshold.class));

        ts.addTest(new TestSuite(TestQuantitation.class));

        ts.addTest(new TestSuite(TestMascotRawParser.class));
        ts.addTest(new TestSuite(TestFactory.class));

        // The indexed MascotDatfile TestClasses that must be run.
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestHeader.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestMascotDatfile.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestMasses.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestModification.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestModificationConversion.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestParameters.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestPeptideHit.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestPeptideHitAnnotation.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestPeptideToQueryMap.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestProteinHit.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestProteinID.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestProteinMap.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestQuery.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestQueryToPeptideMap.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestThreshold.class));
        ts.addTest(new TestSuite(TestDecoyPeptideToQueryMap.class));
        ts.addTest(new TestSuite(com.compomics.mascotdatfile.util.mascot.index.TestQuantitation.class));

        ts.addTest(new TestSuite(TestController.class));

        return ts;
    }
}
