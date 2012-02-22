package com.compomics.mascotdatfile.research.script;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 17-mrt-2008 Time: 16:42:01 To change this template use File | Settings |
 * File Templates.
 */
public class PeptideHitParser {
    // Class specific log4j logger for SimpleParser instances.
    private static Logger logger = Logger.getLogger(PeptideHitParser.class);
    private File iOutput = null;
    private char iSeparator;
    private BufferedWriter writer = null;

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
        } else {
            new PeptideHitParser(args);
        }
    }

    public PeptideHitParser(String[] lArguments) {
        try {

            Double lAlpha = Double.parseDouble(lArguments[0]);

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

                File lInput = new File(lFileName);
                System.gc();
                System.out.println("Processing " + lInput);

                int lPeptideHitCounter = 0;
                int lQueryHitCounter = 0;
                // Create a new MascotDatfile instance for each filename in the Input array.
                iMascotDatfile = MascotDatfileFactory.create(lFileName, MascotDatfileType.INDEX);

                // Fetch the QueryToPeptideMap. This indexes all queries.
                // From 1 to n number of spectra in the corresponding datfile.

                int lNumberOfQueries = iMascotDatfile.getHeaderSection().getQueries();
                QueryToPeptideMapInf lQueryToPeptideMap = iMascotDatfile.getQueryToPeptideMap();
                // Also explore other methods on the QueryToPeptideMap!!!

                ArrayList list = null;

                // This Vector retrieves the best PeptideHit for each Query.
                // The Vector is zero based.
                // ex: Vector[0] contains the peptidehit of Query 1, etc.

                // Iterate over all PeptideHits.
                for (int j = 1; j <= lNumberOfQueries; j++) {
                    Query lQuery = iMascotDatfile.getQuery(j);
                    Vector lPeptideHits = lQueryToPeptideMap.getPeptideHitsAboveIdentityThreshold(j, lAlpha);

                    // CSV output array.
                    int lNumberOfPeptideHits = lPeptideHits.size();
                    if(lNumberOfPeptideHits > 0){
                        lQueryHitCounter++;
                    }

                    for (int k = 0; k < lNumberOfPeptideHits; k++) {
                        PeptideHit lPeptideHit = (PeptideHit) lPeptideHits.get(k);

                        if (lPeptideHit != null) {

                            list = new ArrayList();

                            // <MascotDatfile>
                            list.add(lInput.getName());

                            // <query number>
                            list.add(j);

                            // <spectrum title>
                            list.add(lQuery.getFilename());

                            // <charge state>
                            list.add(lQuery.getChargeString());

                            // <peptide>
                            list.add(lPeptideHit.getSequence());

                            // <peptide+PTM>
                            list.add(lPeptideHit.getModifiedSequence());

                            // <ionscore>
                            list.add(lPeptideHit.getIonsScore());

                            // <rank>
                            int lRank = k +1;
                            list.add(lRank);

                            writeOutput(list);
                            lPeptideHitCounter++;
                        }
                    }
                }
                iMascotDatfile.finish();

                lPeptideHitCounter++;
                System.out.println(String.format("Successfully parsed %d PSMs from %d (%d) Queries above alpha %f", lPeptideHitCounter, lQueryHitCounter, lNumberOfQueries, lAlpha));
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            printUsage();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println(String.format("Finished PeptideHitParser job"));
            }

        }

    }

    private static void printUsage() {
        System.out.println("SimpleParser arguments:\t <alpha> <output> <input 1> [<input 2> <input 3> ... <input n>]");
        System.out.println("\n\n");
        System.out.println("Arguments:");
        System.out.println("<alpha> \t alpha=0.05 reports peptide hits above 95% probability threshold");
        System.out.println("<output> \t output file");
        System.out.println("<input> \t one or more MascotDatfile input files");
        System.out.println("\n\n");
        System.out.println("Output structure:\n<MascotDatfile> <query number> <spectrum title> <charge state> <peptide> <peptide+PTM> <ionscore> <rank>");
    }


    private void writeOutput(final ArrayList aList) throws IOException {
        for (int i = 0; i < aList.size(); i++) {
            Object o = aList.get(i);
            writer.write(o.toString());
            writer.write(iSeparator);
        }
        writer.newLine();
        writer.flush();
    }


}
