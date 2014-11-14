package com.compomics.mascotdatfile.util.mascot.quantitation;

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.mascot.Peak;
import com.compomics.mascotdatfile.util.mascot.enumeration.Mass;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Mar 16, 2009 Time: 5:35:40 PM To change this template use File | Settings
 * | File Templates.
 */
public class Ratio {
    // Class specific log4j logger for Ratio instances.
    private static Logger logger = Logger.getLogger(Ratio.class);

    public static double NUMERATOR_NOT_FOUND = -1.0;
    public static double DENOMINATOR_NOT_FOUND = -2.0;
    public static double NUMERATOR_AND_DENOMINATOR_NOT_FOUND = -3.0;

    private Component iNumerator;
    private Component iDenominator;

    private double iNumeratorCoefficient = 1.0;
    private double iDenominatorCoefficient = 1.0;

    public Ratio(final Component aNumeratorComponent, final Component aDenominatorComponent) {
        this(aNumeratorComponent, aDenominatorComponent, 1.0, 1.0);
    }

    public Ratio(final Component aNumeratorComponent, final Component aDenominatorComponent, final Double aNumeratorCoeficient, final Double aDenominatorCoeficient) {
        iNumerator = aNumeratorComponent;
        iNumeratorCoefficient = aNumeratorCoeficient;
        iDenominator = aDenominatorComponent;
        iDenominatorCoefficient = aDenominatorCoeficient;

    }

    public String getName() {
        return iNumerator.getName() +
                ((iNumeratorCoefficient != 1.0) ? " x " + iNumeratorCoefficient : "") +
                "/" +
                iDenominator.getName() +
                ((iDenominatorCoefficient != 1.0) ? " x " + iDenominatorCoefficient : "");

    }


    @Override
    public String toString() {
        return getName();
    }

    /**
     * Calculates the Ratio for the Components within the given peaks from.
     *
     * @param aPeaks
     * @return The calculated ratio. If these were set, the intensities will be weighted by their coefficient. If one of
     *         the Peaks was not found, the returning is negative. <ul> <li>-1 means the numerator peak is missing</li>
     *         <li>-2 means the denominator peak is missing</li> <li>-3 means both the numerator and the denominator
     *         peak are missing</li> </ul>
     */
    public double calculate(Peak[] aPeaks, double aIonTolerance, Mass aMassType) {
        double lResult = 0;
        // Local variables.
        double lNumeratorMass = 0.0;
        double lDenominatorMass = 0.0;
        Peak lNumeratorPeak = null;
        Peak lDenominatorPeak = null;

        // Localize the mass to find.
        if (aMassType == Mass.AVERAGE) {
            lNumeratorMass = iNumerator.getAverage();
            lDenominatorMass = iDenominator.getAverage();
        } else if (aMassType == Mass.MONOISOTOPIC) {
            lNumeratorMass = iNumerator.getMonoisotopic();
            lDenominatorMass = iDenominator.getMonoisotopic();
        }

        // Find the corresponding peaks.
        for (int i = 0; i < aPeaks.length; i++) {
            Peak lPeak = aPeaks[i];

            // Numerator match?
            if (Math.abs(lPeak.getMZ() - lNumeratorMass) < aIonTolerance) {
                lNumeratorPeak = lPeak;
            }

            // Denominator match?
            if (Math.abs(lPeak.getMZ() - lDenominatorMass) < aIonTolerance) {
                lDenominatorPeak = lPeak;
            }

            if (lDenominatorPeak != null & lNumeratorPeak != null) {
                break;
            }
        }

        // Ok, finished iterating the Peaks.
        if (lNumeratorPeak == null) {
            // Subtract '-1' from the result if the Numerator peak is missing.
            lResult = lResult + NUMERATOR_NOT_FOUND;
        }
        if (lDenominatorPeak == null) {
            // Subtract '-2' from the result if the Denominator is missing.
            lResult = lResult + DENOMINATOR_NOT_FOUND;
        }

        // If one of the peaks was missing, the ratio cannot be calculated. Therefore, the result double should still be ')'.
        if (lResult == 0) {
            // Calculate the Ratio!
            lResult = (lNumeratorPeak.getIntensity() * iNumeratorCoefficient) / (lDenominatorPeak.getIntensity() * iDenominatorCoefficient);

            // Round up to 4 decimals.
            BigDecimal lBigDecimal = new BigDecimal(lResult);
            lBigDecimal = lBigDecimal.setScale(4, BigDecimal.ROUND_UP);

            lResult = lBigDecimal.doubleValue();
        }

        return lResult;
    }


}
