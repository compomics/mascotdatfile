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

/**
 * Created by IntelliJ IDEA.
 * User: Lennart
 * Date: 18-mei-2006
 * Time: 20:38:39
 */
package com.compomics.mascotdatfile.research.script;

import com.compomics.mascotdatfile.research.util.DatfileLocation;
import com.compomics.mascotdatfile.util.interfaces.FragmentIon;
import com.compomics.mascotdatfile.util.mascot.*;
import com.compomics.util.gui.spectrum.SpectrumPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;
/*
 * CVS information:
 *
 * $Revision: 1.5 $
 * $Date: 2008/07/23 08:55:58 $
 */

/**
 * This class scores individual amino acids based on the presence of flanking b (underlined)
 * and y-ions (red).
 *
 * @author Lennart Martens
 * @version $Id: AminoAcidScoring.java,v 1.5 2008/07/23 08:55:58 kenny Exp $
 */
public class AminoAcidScoring {

    public static void main(String[] args) {
        if(args == null || args.length != 4) {
            printUsage();
        }
        // Check tne input file.
        File testExistence = new File(args[0]);
        if(!testExistence.exists()) {
            printError("The datfile you specified ('" + args[0] + "') does not exist!");
        }
        // Check the querynumber.
        int queryNumber = -1;
        try {
            queryNumber = Integer.parseInt(args[1]);
            if(queryNumber < 0) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException nfe) {
            printError("The querynumber you specified ('" + args[1] + "') is not a positive, whole number!");
        }
        // Check the peptidehitnumber.
        int peptideHitNumber = -1;
        try {
            peptideHitNumber = Integer.parseInt(args[2]);
            if(peptideHitNumber < 0) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException nfe) {
            printError("The peptidehit number you specified ('" + args[2] + "') is not a positive, whole number!");
        }

        // Check the peptidehitnumber.
        int scoretype = -1;
        try {
            scoretype = Integer.parseInt(args[3]);
            if(scoretype < 0 || scoretype > 1) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException nfe) {
            printError("The scoringtype you specified ('" + args[3] + "') is not known to me.\nRun this program without parameters to find out about valid options for this parameter.!");
        }

// *********************** PART THAT ACTUALLY DOES SOMETHING *********************** \\
        // Alright, everything checks out. Now do the work!
        DatfileLocation dfl = new DatfileLocation(DatfileLocation.HARDDISK, new String[]{args[0]});
        try {
            MascotDatfile mdf = dfl.getDatfile();
            // Get the query.
            Query query = (Query)mdf.getQueryList().get(queryNumber-1);
            // Get the peptidehit.
            QueryToPeptideMap qtpm = mdf.getQueryToPeptideMap();
            PeptideHit ph = qtpm.getPeptideHitOfOneQuery(queryNumber,peptideHitNumber);
            if(ph == null){
                printError("Peptidehit " + peptideHitNumber + " from Query " + queryNumber + " does not exist!!");
            }
            // Get paptidehitannotation object.
            PeptideHitAnnotation pha = ph.getPeptideHitAnnotation(mdf.getMasses(),mdf.getParametersSection());
            // Match Mascot ions.
            Vector ions = null;
            switch(scoretype) {
                case 0:
                    ions = pha.getMatchedIonsByMascot(query.getPeakList(), ph.getPeaksUsedFromIons1());
                    break;
                case 1:
                    ions = pha.getFusedMatchedIons(query.getPeakList(), ph.getPeaksUsedFromIons1(), query.getMaxIntensity(), 0.1);
                    break;
            }
            // Peptide sequence + length.
            String sequence = ph.getSequence();
            int length = sequence.length();
            // Create Y and B boolean arrays.
            boolean[] yIons = new boolean[length];
            boolean[] bIons = new boolean[length];
            // Fill out arrays.
            for (int i = 0; i < ions.size(); i++) {
                FragmentIon lFragmentIon = (FragmentIon)ions.elementAt(i);
                switch(lFragmentIon.getID()) {
                    case FragmentIon.Y_ION:
                        yIons[lFragmentIon.getNumber()-1] = true;
                        break;
                    case FragmentIon.B_ION:
                        bIons[lFragmentIon.getNumber()-1] = true;
                        break;
                    default:
                        System.err.println(" * Ion found by Mascot that is disregarded: " + lFragmentIon.getLabel());
                }
            }
            // Now simply add formatting.
            String[] modifiedAA = parseModifiedStringIntoComponents(ph.getModifiedSequence());
            StringBuffer formattedSequence = new StringBuffer("<html>");
            // Cycle the amino acids (using b-ions indexing here).
            for (int i = 0; i < bIons.length; i++) {
                boolean italic = false;
                boolean bold = false;
                // First and last one only have 50% coverage anyway
                if(i == 0) {
                    if(bIons[i]) {
                        italic = true;
                    }
                    if(yIons[yIons.length-(i+1)] && yIons[yIons.length-(i+2)]) {
                        bold = true;
                    }
                } else if(i == (length-1)) {
                    if(bIons[i] && bIons[i-1]) {
                        italic = true;
                    }
                    if(yIons[yIons.length-(i+1)]) {
                        bold = true;
                    }
                } else {
                    // Aha, two ions needed here.
                    if(bIons[i] && bIons[i-1]) {
                        italic = true;
                    }
                    if(yIons[yIons.length-(i+1)] && yIons[yIons.length-(i+2)]) {
                        bold = true;
                    }
                }
                // Actually add the next char.
                formattedSequence.append(
                        (italic?"<u>":"") +
                        (bold?"<font color=\"red\">":"") +
                        modifiedAA[i].replaceAll("<","&lt;").replaceAll(">","&gt;") +
                        (italic?"</u>":"") +
                        (bold?"</font>":"")
                    );
            }
            // Finalize HTML'ized label text.
            formattedSequence.append("</html>");

            // Create label and set text.
            JLabel label = new JLabel(formattedSequence.toString());
// *********************** END OF PART THAT ACTUALLY DOES SOMETHING *********************** \\
            SpectrumPanel sp1 = new SpectrumPanel(query.getMZArray(),query.getIntensityArray(), query.getPrecursorMZ(), query.getChargeString(), query.getTitle());
            sp1.setAnnotations(ions);
            JFrame app1 = new JFrame("Spectrum for " + query.getTitle() + " - " + ph.getModifiedSequence() + " (Mascot ions)" + " - "+ "Score: " + ph.getIonsScore());
            app1.addWindowListener(new WindowAdapter() {
                /**
                 * Invoked when a window is in the process of being closed.
                 * The close operation can be overridden at this point.
                 */
                public void windowClosing(WindowEvent e) {
                    e.getWindow().dispose();
                    System.exit(0);
                }
            });
            JPanel jpanLabel = new JPanel();
            jpanLabel.add(label);
            app1.getContentPane().add(jpanLabel, BorderLayout.NORTH);
            app1.getContentPane().add(sp1, BorderLayout.CENTER);
            app1.setLocation(100, 100);
            app1.setSize(1200, 300);
            app1.setVisible(true);


        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static String[] parseModifiedStringIntoComponents(String aModSeq) {
        Vector parts = new Vector();
        String temp = aModSeq;
        String part = temp.substring(0, temp.indexOf("-")+1).trim();
        int start = temp.indexOf("-")+1;
        temp = temp.substring(start).trim();
        int endIndex = 1;
        if(temp.charAt(endIndex) == '<') {
            endIndex++;
            while(temp.charAt(endIndex) != '>') {
                endIndex++;
            }
            endIndex++;
        }
        part += temp.substring(0,endIndex);
        temp = temp.substring(endIndex);
        parts.add(part);
        while(temp.length() > 0) {
            start = 0;
            endIndex = 1;
            if(temp.charAt(start+endIndex) == '<') {
                endIndex++;
                while(temp.charAt(start + endIndex) != '>') {
                    endIndex++;
                }
                endIndex++;
            } else if(temp.charAt(start+endIndex) == '-') {
                endIndex = temp.length();
            }
            part = temp.substring(0,endIndex);
            temp = temp.substring(endIndex);
            parts.add(part);
        }
        String[] result = new String[parts.size()];
        parts.toArray(result);
        return result;
    }

    private static void printUsage() {
        printError("Usage:\tAminoAcidScoring <path and filename from datfile> <querynumber> <peptidehitnumber> <scoringtype>\n\n\tWhere scoringtype can be:\n\t\t\t0 for Mascot scoring\n\t\t\t1 for Fused scoring\n\nExample:\n\tAminoAcidScoring C:\\mascot\\datfiles\\F010345.dat 598 3 1\nWill load the Third PeptideHit from Query 598 of F010345.dat with fused(1) annotations into the AminoAcideScoring Panel.");
    }

    private static void printError(String aMsg) {
        System.err.println("\n\n" + aMsg + "\n\n");
        System.exit(1);
    }
}
