package com.compomics.mascotdatfile.util.mascot;

import org.apache.log4j.Logger;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 12-jan-2007
 * Time: 15:58:31
 */

/**
 * This Class represents one entry in the ProteinSection of a raw datfile along with the sources where peptides of this
 * Protein were found.
 */
public class ProteinID {
    // Class specific log4j logger for ProteinID instances.
    private static Logger logger = Logger.getLogger(ProteinID.class);

    /**
     * Protein mass.
     */
    private double iMass;
    /**
     * Protein accession.
     */
    private String iAccession;
    /**
     * Protein description.
     */
    private String iDescription;
    /**
     * Vector with int[] describing the protein sources. </br> Example: <i></br> If PeptideHit "KENNYHELSER" was found
     * in the second Hit of Query 567 and the first hit of Query 568; then this Vector's will contain two int[], wherein
     * [0] is the QueryNumber and [1] is the HitNumber</br> Vector.get(0)=[567][2]</br> Vector.get(1)=[568][1]</i>
     */
    private Vector iSources = null;

    public ProteinID(String aAccession, double aMass, String aDescription) {
        iMass = aMass;
        iAccession = aAccession;
        iDescription = aDescription;
    }

    /**
     * This method adds a source to the Vector with int[] describing the protein sources. Query and peptidehits are
     * 1-based! Query 1 returns the first query. <br> Example: <i><br> If PeptideHit "KENNYHELSER" was found in the
     * second Hit of Query 567 and the first hit of Query 568; then this Vector's will contain two int[], wherein [0] is
     * the QueryNumber and [1] is the HitNumber<br> Vector.get(0)=[567][2]<br> Vector.get(1)=[568][1]</i>
     *
     * @param aQueryNumber      Querynumber wherein the ProteinHit was found.
     * @param aPeptidehitNumber PeptideHit number in the Query.
     */
    public void addSource(int aQueryNumber, int aPeptidehitNumber) {
        if (iSources == null) {
            iSources = new Vector();
        }
        iSources.add(new int[]{aQueryNumber, aPeptidehitNumber});
    }

    /**
     * Getter for property 'accession'.
     *
     * @return Value for property 'accession'.
     */
    public String getAccession() {
        return iAccession;
    }

    /**
     * Getter for property 'description'.
     *
     * @return Value for property 'description'.
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     * Getter for property 'mass'.
     *
     * @return Value for property 'mass'.
     */
    public double getMass() {
        return iMass;
    }

    /**
     * Getter for property 'queryNumbers'.
     *
     * @return Value for property 'queryNumbers'.
     */
    public int[] getQueryNumbers() {
        int[] lQueryNumbers = new int[iSources.size()];
        Enumeration lEnum = iSources.elements();
        int lCount = 0;
        while (lEnum.hasMoreElements()) {
            lQueryNumbers[lCount] = ((int[]) lEnum.nextElement())[0];
            lCount++;
        }
        return lQueryNumbers;
    }

    /**
     * Getter for property 'queryNumbersAndPeptideHits'. Querynumber in first dimension, PeptideHit in second
     * dimension.
     *
     * @return Value for property 'queryNumbersAndPeptideHits'.
     */
    public int[][] getQueryNumbersAndPeptideHits() {
        int[][] lQueryNumbersAndPeptideHits = new int[iSources.size()][2];
        Enumeration lEnumeration = iSources.elements();
        int lCount = 0;
        while (lEnumeration.hasMoreElements()) {
            // 1) Querynumber in first dimension, PeptideHit in second dimension.
            lQueryNumbersAndPeptideHits[lCount] = ((int[]) lEnumeration.nextElement());
            lCount++;
        }
        return lQueryNumbersAndPeptideHits;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "ProteinID{" +
                "iMass=" + iMass +
                ", iAccession='" + iAccession + '\'' +
                ", iDescription='" + iDescription + '\'' +
                ", iSources=" + iSources +
                '}';
    }
}
