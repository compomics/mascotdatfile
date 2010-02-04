package com.computationalomics.mascotdatfile.research.script;

import com.computationalomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.computationalomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.computationalomics.mascotdatfile.util.mascot.PeptideHit;
import com.computationalomics.mascotdatfile.util.mascot.ProteinHit;
import com.computationalomics.mascotdatfile.util.mascot.ProteinID;
import com.computationalomics.mascotdatfile.util.mascot.Query;
import com.computationalomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.computationalomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 17-mrt-2008
 * Time: 16:42:01
 * To change this template use File | Settings | File Templates.
 */
public class SimpleParser {
	private File iOutput = null;
	private String[] iInput = null;
	private char iSeparator;
	private BufferedWriter writer = null;

	public static void main(String[] args) {
		new SimpleParser();
	}

	public SimpleParser(){
		try {

			// Create an input File array with n files.
			iInput = new String[1];
			// Fill the Array.
			iInput[0] = "/Users/kenny/Proteomics/0812/0812_mascotdatfile_bug/F011830.dat";

			// Define an output file.
			iOutput = new File("/tmp/export.dat");
			// Create a buffered output.
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(iOutput)));
			// Define the separator
			iSeparator = ';';

			// Ready to go!
			MascotDatfileInf iMascotDatfile = null;

			for (int i = 0; i < iInput.length; i++) {
				System.gc();
				System.out.println("Processing " + iInput[i]);
				// Create a new MascotDatfile instance for each filename in the Input array.
				iMascotDatfile = MascotDatfileFactory.create(iInput[i], MascotDatfileType.MEMORY);

				// Fetch the QueryToPeptideMap. This indexes all queries.
				// From 1 to n number of spectra in the corresponding datfile.

				QueryToPeptideMapInf lQueryToPeptideMap = iMascotDatfile.getQueryToPeptideMap();
				// Also explore other methods on the QueryToPeptideMap!!!

				ArrayList list = null;

				// This Vector retrieves the best PeptideHit for each Query.
				// The Vector is zero based.
				// ex: Vector[0] contains the peptidehit of Query 1, etc.
				Vector lBestPeptideHits = lQueryToPeptideMap.getAllPeptideHitsAboveIdentityThreshold();

				// Iterate over all ProteinIDs
				Iterator iter = iMascotDatfile.getProteinMap().getProteinIDIterator();
				ProteinID lProteinID = null;
				while(iter.hasNext()){
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
					PeptideHit lPeptideHit = (PeptideHit) lBestPeptideHits.elementAt(j);
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
						ArrayList lProteins  = lPeptideHit.getProteinHits();

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
							list.add(((Query)iMascotDatfile.getQueryList().get(j)).getFilename());
							// 2.
							list.add(lPeptideHit.getModifiedSequence());
							// 3.
							list.add(lPeptideHit.getIonsScore());
							// 4.
							list.add( lPeptideHit.calculateIdentityThreshold(0.05));

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
