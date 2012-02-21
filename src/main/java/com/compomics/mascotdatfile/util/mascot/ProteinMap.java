package com.compomics.mascotdatfile.util.mascot;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 12-jan-2007
 * Time: 14:02:21
 */

/**
 * This Class is a Map of all the proteins information in this MascotDatfile. Mind that this Class is using the
 * proteinSection from the raw datfile as a base. Knowing that not all the ProteinHit accessions encountered in the
 * PeptideHits are located in the proteins section of the datfile!
 */
public class ProteinMap {
    // Class specific log4j logger for ProteinMap instances.
    private static Logger logger = Logger.getLogger(ProteinMap.class);
    private int iNumberOfProteins;
    private HashMap iProteinMap = new HashMap();

    /**
     * Constructor of the ProteinMap. An instance that contains all the protein related information from a datfile.
     *
     * @param aProteinSection - HashMap Summary from the Mascot results file.
     */
    public ProteinMap(HashMap aProteinSection) {
        if (aProteinSection != null) {
            iNumberOfProteins = aProteinSection.size();
            buildProteinMap(aProteinSection);
        }
    }

    /**
     * Private method delegated by the Constructor. Build of this instance.
     *
     * @param aProteinSection HashMap with the proteinsection of a raw datfile.
     */
    private void buildProteinMap(HashMap aProteinSection) {
        iProteinMap = new HashMap(iNumberOfProteins);
        Iterator iter = aProteinSection.keySet().iterator();
        StringTokenizer st = null;
        ProteinID lProteinID = null;
        while (iter.hasNext()) {
            String lNext = (String) iter.next();
            if (lNext != null) {
                // 1. Get the next protein accession.
                String lAccession = lNext;
                // 2. Get the corresponding protein values.
                String lProteinValue = (String) aProteinSection.get(lAccession);
                // ex. 1030.68,"(*CE*) ACYP1_HUMAN Acylphosphatase, organ-common type isozyme (EC 3.6.1.7) (Acylphosphatephosphohydrolase) (Acylphosphatase, erythrocyte isozyme)."

                // 3. Strip the " chars from the accession.
                //int lAccessionStart = lAccession.indexOf("\"") + 1;
                //int lAccessionStop = lAccession.lastIndexOf("\"");

                // This is modified by Rui Wang to convert special cases
                Pattern pattern = Pattern.compile("[\"]?([^\"]+)[\"]?");
                Matcher m = pattern.matcher(lAccession);
                if (m.find()) {
                    lAccession = m.group(1);
                    //lAccession = lAccession.substring((lAccession.indexOf("\"") + 1), (lAccession.lastIndexOf("\"")));
                }

                double lMass = Double.parseDouble(lProteinValue.substring(0, lProteinValue.indexOf(",", 0)));
                String lDescription = lProteinValue.substring((lProteinValue.indexOf("\"") + 1), lProteinValue.length() - 1);
                lProteinID = new ProteinID(lAccession, lMass, lDescription);
                iProteinMap.put(lAccession, lProteinID);
            }
        }
    }


    /**
     * Public method accessed during the QueryToPeptideMap Generation. If a PeptideHit contains a ProteinHit with an
     * accession found in this ProteinMap, this method will be called and the corresponding ProteinID instance will be
     * updated with a QueryNumber & PeptideHitNumber wherein that ProteinID was found.
     *
     * @param aAccession        String identifier of the Protein.
     * @param aQueryNumber      QueryNumber wherein a PeptideHit was linked to this ProteinID.
     * @param aPeptideHitNumber PeptideHitNumber of the QueryNumber.
     */
    public void addProteinSource(String aAccession, int aQueryNumber, int aPeptideHitNumber) {
        ProteinID lProteinID;
        if (iProteinMap.get(aAccession) == null) {
            // If the accession for the protein hit was not in the protein Section,
            // then we create a new non-descriptive ProteinID.
            double lMass = -1;
            String lDescription = "no description";
            lProteinID = new ProteinID(aAccession, lMass, lDescription);
            iProteinMap.put(aAccession, lProteinID);
        }
        lProteinID = ((ProteinID) iProteinMap.get(aAccession));
        lProteinID.addSource(aQueryNumber, aPeptideHitNumber);
    }

    /**
     * This method returns a ProteinID instance corresponding to aAccession. Mind that the ProteinMap object is using
     * the proteinSection from the raw datfile as a base. Know that not all the ProteinHit accessions encountered in the
     * PeptideHits are located in the proteins section of the datfile! Therefor a ProteinID with aAccesion, mass:-1.0
     * and "No Description" is returned if the Accession is not found in the ProteinMap.
     *
     * @param aAccession String identifier of the ProteinHit.
     * @return ProteinID instance corresponding to param aAccession.
     */
    public ProteinID getProteinID(String aAccession) {
        ProteinID lProteinID = null;
        if (iProteinMap.get(aAccession) == null) {
            lProteinID = new ProteinID(aAccession, -1.0, "No Description.");
        } else {
            lProteinID = ((ProteinID) iProteinMap.get(aAccession));
        }
        return lProteinID;
    }

    /**
     * This method returns a description String corresponding to aAccession. Mind that the ProteinMap object is using
     * the proteinSection from the raw datfile as a base. Know that not all the ProteinHit accessions encountered in the
     * PeptideHits are located in the proteins section of the datfile! Therefor a "No Description." String is returned
     * if the Accession is not found in the ProteinMap.
     *
     * @param aAccession String identifier of the ProteinHit.
     * @return ProteinID instance corresponding to param aAccession.
     */
    public String getProteinDescription(String aAccession) {
        String result = null;
        if (iProteinMap.get(aAccession) != null) {
            result = ((ProteinID) iProteinMap.get(aAccession)).getDescription();
        } else {
            result = "No Description.";
        }
        return result;
    }

    /**
     * This method returns the number Protein entries there were in the Protein section of the raw datfile.
     *
     * @return int Number of Proteins in the Protein section.
     */
    public int getNumberOfProteins() {
        return iNumberOfProteins;
    }

    /**
     * Returns an iterator of the keys in the proteinmap.
     *
     * @return Iterator instance on the keyset of the ProteinMap HashMap. Each key can be used in the getProteinID
     *         method, this Iterator can as such be used to iterate over all ProteinID's in the MascotDatfile.
     */
    public Iterator getProteinIDIterator() {
        return iProteinMap.keySet().iterator();
    }
}
