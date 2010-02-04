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
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import junit.TestCaseLM;
import junit.framework.Assert;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 2-mrt-2006
 * Time: 14:42:31
 */

/**
 * This class implements the test scenario for the PeptideToQueryMap class.
 */
public class TestDecoyPeptideToQueryMap extends TestCaseLM {


    public TestDecoyPeptideToQueryMap() {
        super("Testscenario for the peptidehit Class. ");
    }

    public void testReadDecoyPeptideHit() {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F001599.dat"));
        QueryToPeptideMapInf lQueryToPeptideMap = lMascotDatfile.getDecoyQueryToPeptideMap();
        //QueryToPeptideMap lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(1, 10);
        Assert.assertEquals(15.6, lPeptideHit.getIonsScore(), 0.0);
    }

}