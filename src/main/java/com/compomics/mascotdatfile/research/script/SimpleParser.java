package com.compomics.mascotdatfile.research.script;

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.ProteinHit;
import com.compomics.mascotdatfile.util.mascot.ProteinID;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 17-mrt-2008 Time: 16:42:01 To change this template use File | Settings |
 * File Templates.
 */
public class SimpleParser {
// Class specific log4j logger for SimpleParser instances.
private static Logger logger = Logger.getLogger(SimpleParser.class);
private File iOutput = null;
private char iSeparator;
private BufferedWriter writer = null;

    public static void main(String[] args) {
        if(args.length < 3){
            System.out.println("SimpleParser arguments:\n<alpha> <output> <input 1> [<input 2> <input 3> <input ...>]");
        }else{
            new SimpleParser(args);
        }
    }

    public SimpleParser(String[] lArguments) {
        try {
            double lAlpha = Double.parseDouble(lArguments[0]);

            ArrayList<String> lInputs = new ArrayList<String>();

            int i = 2;
            while (i < lArguments.length) {
                String lInput = lArguments[i];
                lInputs.add(lInput);
                i++;
            }

            // Define an output file.
            iOutput = new File(lArguments[1]);

            // Create a buffered output.
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(iOutput)));

            // Define the separator
            iSeparator = ';';

            // Ready to go!
            MascotDatfileInf iMascotDatfile = null;

            for (i = 0; i < lInputs.size(); i++) {

                String lFileName = lInputs.get(i);

                File iInput = new File(lFileName);
                System.gc();
                System.out.println("Processing " + iInput);
                // Create a new MascotDatfile instance for each filename in the Input array.
                iMascotDatfile = MascotDatfileFactory.create(lFileName, MascotDatfileType.MEMORY);

                // Fetch the QueryToPeptideMap. This indexes all queries.
                // From 1 to n number of spectra in the corresponding datfile.

                int lQueries = iMascotDatfile.getHeaderSection().getQueries();
                System.out.println(lQueries);

                QueryToPeptideMapInf lQueryToPeptideMap = iMascotDatfile.getQueryToPeptideMap();
                // Also explore other methods on the QueryToPeptideMap!!!

                ArrayList list = null;

                // This Vector retrieves the best PeptideHit for each Query.
                // The Vector is zero based.
                // ex: Vector[0] contains the peptidehit of Query 1, etc.
                List<PeptideHit> lBestPeptideHits = lQueryToPeptideMap.getAllPeptideHitsAboveIdentityThreshold(lAlpha);

                // Iterate over all ProteinIDs
                Iterator iter = iMascotDatfile.getProteinMap().getProteinIDIterator();
                ProteinID lProteinID = null;
                while (iter.hasNext()) {
                    list = new ArrayList();
                    String lAccession = iter.next().toString();
                    lProteinID = iMascotDatfile.getProteinMap().getProteinID(lAccession);

                    list.add(lAccession);
                    list.add("PROTEIN");
                    list.add(lProteinID.getQueryNumbers().length);
                    list.add(lProteinID.getDescription());

                    writeOutput(list);
                }

                // Iterate over all PeptideHits.
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
                            list.add(lFileName);
                            list.add(j);
                            list.add(k);

                            // 1.
                            list.add(((Query) iMascotDatfile.getQueryList().get(j)).getFilename());
                            // 2.
                            list.add(lPeptideHit.getModifiedSequence());
                            // 3.
                            list.add(lPeptideHit.getIonsScore());
                            // 4.
                            list.add(lPeptideHit.calculateIdentityThreshold(0.05));

                            writeOutput(list);
                        }
                    }
                }
                iMascotDatfile.finish();
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    private void writeOutput(final ArrayList aList) throws IOException {
        for (int i = 0; i < aList.size(); i++) {
            Object o = aList.get(i);
            writer.write(o.toString());
            writer.write(iSeparator);
            //System.out.println(o.toString());
            //System.out.println(iSeparator);
            //System.out.println("\n");
        }
        writer.newLine();
        writer.flush();
    }



}
