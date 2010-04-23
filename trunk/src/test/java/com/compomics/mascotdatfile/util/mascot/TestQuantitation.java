package com.compomics.mascotdatfile.util.mascot;

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.enumeration.Mass;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;
import com.compomics.mascotdatfile.util.mascot.quantitation.Component;
import com.compomics.mascotdatfile.util.mascot.quantitation.Ratio;
import junit.TestCaseLM;
import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Mar 16, 2009 Time: 3:59:08 PM To change this template use File | Settings
 * | File Templates.
 */
public class TestQuantitation extends TestCaseLM {
    // Class specific log4j logger for TestQuantitation instances.
    private static Logger logger = Logger.getLogger(TestQuantitation.class);

    public TestQuantitation() {
        super("Testscenario for quantitation class.");
    }

    /**
     * This method tests all the getters.
     */
    public void testReadMasses() {
        //1. Create a new DatfileID instance of the first datfile.
        MascotDatfileInf lMascotDatfile = MascotDatfileFactory.create(getFullFilePath("F000001.dat"), MascotDatfileType.MEMORY);
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
