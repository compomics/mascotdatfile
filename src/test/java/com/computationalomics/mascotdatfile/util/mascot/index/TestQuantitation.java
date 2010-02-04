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

package com.computationalomics.mascotdatfile.util.mascot.index;

import com.computationalomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.computationalomics.mascotdatfile.util.mascot.Peak;
import com.computationalomics.mascotdatfile.util.mascot.Quantitation;
import com.computationalomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.computationalomics.mascotdatfile.util.mascot.enumeration.Mass;
import com.computationalomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;
import com.computationalomics.mascotdatfile.util.mascot.quantitation.Component;
import com.computationalomics.mascotdatfile.util.mascot.quantitation.Ratio;
import junit.TestCaseLM;
import junit.framework.Assert;

import java.util.ArrayList;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 24-feb-2006
 * Time: 16:00:02
 */

/**
 * This class implements the test scenario for the Masses class.
 */
public class TestQuantitation extends TestCaseLM {
    public TestQuantitation() {
        super("Testscenario for quantitation class.");
    }

    /**
     * This method tests all the getters.
     */
    public void testReadMasses() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lMascotDatfile = MascotDatfileFactory.create(getFullFilePath("F000001.dat"), MascotDatfileType.INDEX);
        Quantitation lQuantitation = lMascotDatfile.getQuantitation();
        Ratio[] lRatios = lQuantitation.getRatios();
        Component[] lComponents = lQuantitation.getComponents();

        // Verify the Number of Components and Ratios.
        Assert.assertEquals(lComponents.length, 4);
        Assert.assertEquals(lRatios.length, 3);

        // Verify the content of the Ratios.
        Assert.assertEquals(lRatios[0].getName(), "115 x 2.0/114");
        Assert.assertEquals(lRatios[1].getName(), "116/114 x 1.5");
        Assert.assertEquals(lRatios[2].getName(), "117/114");

        // Verify the content of the Components.
        Assert.assertEquals(lComponents[0].getName(), "114");
        Assert.assertEquals(lComponents[0].getAverage(), 114.17347);
        Assert.assertEquals(lComponents[0].getMonoisotopic(), 114.11123);

        Assert.assertEquals(lComponents[3].getName(), "117");
        Assert.assertEquals(lComponents[3].getAverage(), 117.15219);
        Assert.assertEquals(lComponents[3].getMonoisotopic(), 117.11497);

        // Verify the Ratio calculation.
        ArrayList<Peak> lPeaks = new ArrayList<Peak>();
        Peak lPeak = new Peak(114.0, 10);
        lPeaks.add(lPeak);

        lPeak = new Peak(115.0, 10);
        lPeaks.add(lPeak);

        lPeak = new Peak(116.0, 10);
        lPeaks.add(lPeak);

        Peak[] lPeakArray = new Peak[lPeaks.size()];
        lPeaks.toArray(lPeakArray);

        Assert.assertEquals(lRatios[0].calculate(lPeakArray, 0.2, Mass.MONOISOTOPIC), 2.0);
        Assert.assertEquals(lRatios[1].calculate(lPeakArray, 0.2, Mass.AVERAGE), 0.6667);
        Assert.assertEquals(lRatios[2].calculate(lPeakArray, 0.2, Mass.MONOISOTOPIC), Ratio.NUMERATOR_NOT_FOUND);

        Assert.assertEquals(lRatios[0].calculate(lPeakArray, 0.01, Mass.AVERAGE), Ratio.NUMERATOR_AND_DENOMINATOR_NOT_FOUND);

        Assert.assertEquals(lQuantitation.getQuantitationType(), "iTRAQ 4plex");
        Assert.assertEquals(lQuantitation.getMethodAttribute("protein_ratio_type"), "weighted");

        lMascotDatfile.finish();
    }
}