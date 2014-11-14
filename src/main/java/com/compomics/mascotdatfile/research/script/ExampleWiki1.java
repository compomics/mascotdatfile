package com.compomics.mascotdatfile.research.script;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.ProteinHit;
import com.compomics.mascotdatfile.util.mascot.ProteinID;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This class is a
 */
public class ExampleWiki1 {
    public ExampleWiki1(String aFileName) {

        String file = aFileName;

        // Define the separator
        char separator = ',';

        // Ready to go!
        MascotDatfileInf iMascotDatfile = null;

        // log the status.
        System.out.println("Processing " + file);

        // Create a new MascotDatfile instance for each filename in the Input array.
        iMascotDatfile = MascotDatfileFactory.create(file, MascotDatfileType.MEMORY);

        // Fetch the QueryToPeptideMap. This indexes all queries.
        // From 1 to n number of spectra in the corresponding datfile.

        QueryToPeptideMapInf lQueryToPeptideMap = iMascotDatfile.getQueryToPeptideMap();
        // Also explore other methods on the QueryToPeptideMap!!!

        ArrayList list = null;

        // This Vector retrieves the best PeptideHit for each Query.
        // The Vector is zero based.
        // ex: Vector[0] contains the peptidehit of Query 1, etc.
        List<PeptideHit> lBestPeptideHits = lQueryToPeptideMap.getAllPeptideHitsAboveIdentityThreshold();

        // A - Iterate over all ProteinIDs
        Iterator iter = iMascotDatfile.getProteinMap().getProteinIDIterator();
        ProteinID lProteinID = null;
        while (iter.hasNext()) {
            String item = "";

            String lAccession = iter.next().toString();
            lProteinID = iMascotDatfile.getProteinMap().getProteinID(lAccession);

            // Collect information for current protein.
            item = "PROTEIN" + separator
                    + lAccession + separator
                    + lProteinID.getQueryNumbers().length + separator
                    + lProteinID.getDescription();

            // Print to system outputstream
            System.out.println(item);
        }

        Vector queryVector = iMascotDatfile.getQueryList();

        // B - Iterate over all PeptideHits.
        for (int j = 0; j < lBestPeptideHits.size(); j++) {
            PeptideHit lPeptideHit = (PeptideHit) lBestPeptideHits.get(j);
            // CSV output array.

            if (lPeptideHit != null) {
                // 1. MS/MS Spectrum filename.
                // 2. Modified PeptideSequence
                // 3. IonScore
                // 4. 95% Identity Threshold
                // 5. Number of ProteinHits
                // 6a. Protein i accession
                // 6b. Protein i description
                // etc. for n proteins.

                // As a Peptide can come from multiple proteins, it can have multiple proteinhits.
                ArrayList lProteins = lPeptideHit.getProteinHits();

                for (int k = 0; k < lProteins.size(); k++) {
                    list = new ArrayList();

                    ProteinHit lProteinHit = (ProteinHit) lProteins.get(k);
                    String lAccession = lProteinHit.getAccession();
                    // The protein description come from another part of the Mascot Result file.
                    // The ProteinMap also keeps track how many peptides refer to a Protein, mind that protein inference is not regarded at all!
                    // 6a.
                    list.add(lAccession);
                    list.add("PEPTIDE");

                    // 1.
                    list.add(((Query) queryVector.get(j)).getFilename());

                    // 2.
                    list.add(lPeptideHit.getModifiedSequence());

                    // 3.
                    list.add(lPeptideHit.getIonsScore());

                    // 4.
                    list.add(lPeptideHit.calculateIdentityThreshold(0.05));

                    String lResult = "";
                    for (Object item : list) {
                        lResult = lResult + item + separator;
                    }

                    System.out.println(lResult);

                }
            }
        }
        iMascotDatfile.finish();

    }

    public static void main(String[] args) {
        new ExampleWiki1("/Users/kennyhelsens/Java/mascotdatfile/src/test/resources/F004071.dat");
    }
}

