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

package com.compomics.mascotdatfile.research.script;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.research.util.DatfileLocation;
import com.compomics.mascotdatfile.util.mascot.*;
import com.compomics.util.gui.spectrum.SpectrumPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 5-apr-2006 Time: 16:48:21
 */
public class DrawSpectrum {
    // Class specific log4j logger for DrawSpectrum instances.
    private static Logger logger = Logger.getLogger(DrawSpectrum.class);

    /**
     * Main() method draws the spectrumpannel of the requested peptidehit.
     *
     * @param args [0] = source of the datfile ( 0 is hard disk, 1 is localDB, 2 is Muppet, 3 is URL) [1] =   1) if
     *             datfile is on hard disk; args[1] must be the path and filename. 2) if datfile comes from db, args[1]
     *             can be the datfileID or the filename. 3) if datfile comes from the internet, args[1] should be the
     *             filename. (F010834.dat) [2] = Querynumber. [3] =  nth PeptideHit resulting from Query. [4] = if
     *             connection to muppet03 database, this parameter should be the username. (ex: kenny) if datfile comes
     *             from the internet, this parameter should be the server. (ex: http://cavell.ugent.be/) [5] = if
     *             connection to muppet03 database, this parameter should be the password. (ex: 000) if datfile comes
     *             from the internet, this parameter should be the date. (ex: 20061130)
     */
    public static void main(String[] args) {
        DatfileLocation lDatfileLocation = null;
        MascotDatfileInf lMascotDatfile = null;
        int lQueryNumber = Integer.parseInt(args[2]);
        int lPeptideHitNumber = Integer.parseInt(args[3]);
        // Get the datfile from different sources, the DatfileLocation Class guides the process based on the args[0] parameter.
        int lDatfileLocationType = Integer.parseInt(args[0]);
        try {
            Label:
            if (lDatfileLocationType == DatfileLocation.HARDDISK) {
                {
                    lDatfileLocation = new DatfileLocation(DatfileLocation.HARDDISK, new String[]{args[1]});
                    lMascotDatfile = lDatfileLocation.getDatfile();
                    break Label;
                }
            } else if (lDatfileLocationType == DatfileLocation.URL) {
                {
                    lDatfileLocation = new DatfileLocation(DatfileLocation.URL, new String[]{args[4], args[5], args[1]});
                    lMascotDatfile = lDatfileLocation.getDatfile();
                    break Label;
                }
            }
            System.out.println("Datfile received.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  // print ${Exception} information.
        } catch (InstantiationException e) {
            e.printStackTrace();  // print ${Exception} information.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  // print ${Exception} information.
        } catch (SQLException e) {
            e.printStackTrace();  // print ${Exception} information.
        }

        QueryToPeptideMapInf QueryTP = lMascotDatfile.getQueryToPeptideMap();
        PeptideHit ph = QueryTP.getPeptideHitOfOneQuery(lQueryNumber, lPeptideHitNumber);
        Vector queries = lMascotDatfile.getQueryList();
        Query q = (Query) queries.get(lQueryNumber - 1);
        // Evoke an error if the requested peptidehit doesnt exist.
        if (ph == null) {
            throw new NullPointerException("The peptidehit you requested has evoked a nullpointerException. This means that there was no peptidehit in the requested query.");
        }
        ArrayList lProteinHits = ph.getProteinHits();
        ProteinHit ProteinHit = (ProteinHit) lProteinHits.get(0);
        System.out.println(
                "Showing spectrum from " + lDatfileLocation + " Datfile " + args[1] + ", Query " + lQueryNumber + ", Peptidehit " + lPeptideHitNumber +
                        ":\t" +
                        ph.getModifiedSequence() + "\n" +
                        lMascotDatfile.getProteinMap().getProteinDescription(ProteinHit.getAccession())
        );

        PeptideHitAnnotation pha = ph.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection(), q.getPrecursorMZ(), q.getChargeString());
        //mascot
        Vector fm = pha.getMatchedIonsByMascot(q.getPeakList(), ph.getPeaksUsedFromIons1());
        SpectrumPanel sp1 = new SpectrumPanel(q.getMZArray(), q.getIntensityArray(), q.getPrecursorMZ(), q.getChargeString(), q.getTitle());
        sp1.setAnnotations(fm);
        JFrame app1 = new JFrame("Spectrum for " + q.getTitle() + " - " + ph.getModifiedSequence() + " (Mascot ions)" + " - " + "Score: " + ph.getIonsScore());
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
        app1.getContentPane().add(sp1);
        app1.setLocation(100, 100);
        app1.setSize(1200, 300);
        app1.setVisible(true);

        //fused
        fm = pha.getFusedMatchedIons(q.getPeakList(), ph.getPeaksUsedFromIons1(), q.getMaxIntensity(), 0.1);
        SpectrumPanel sp2 = new SpectrumPanel(q.getMZArray(), q.getIntensityArray(), q.getPrecursorMZ(), q.getChargeString(), q.getTitle());
        sp2.setAnnotations(fm);
        JFrame app2 = new JFrame("Spectrum for " + q.getTitle() + " - " + ph.getModifiedSequence() + " (Fused)" + " - " + "Score: " + ph.getIonsScore());
        app2.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             */
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
            }
        });
        app2.getContentPane().add(sp2);
        app2.setLocation(110, 110);
        app2.setSize(1200, 300);
        app2.setVisible(true);

    }

    /**
     * This static method displays a masspectrum by the peaks from the Query and annotations in the Vector.
     *
     * @param aPeptideHit the peptide hit
     * @param aMascotDatfile MascotDatfile pointer wherefrom you can get anything you need for the visualization.
     */
    public static void drawSpectrum(PeptideHit aPeptideHit, MascotDatfile aMascotDatfile) {
        // Get the best Query behind this identification.
        Query lQuery = (Query) ((aMascotDatfile.getPeptideToQueryMap().getQueriesByModifiedSequence(aPeptideHit.getModifiedSequence())).get(0));
        // Get the peptidehitannotations.
        PeptideHitAnnotation lPeptideHitAnnotation = aPeptideHit.getPeptideHitAnnotation(aMascotDatfile.getMasses(), aMascotDatfile.getParametersSection(), lQuery.getPrecursorMZ(), lQuery.getChargeString());
        Vector lFusedFragmentionsVec = lPeptideHitAnnotation.getFusedMatchedIons(lQuery.getPeakList(), aPeptideHit.getPeaksUsedFromIons1(), lQuery.getMaxIntensity(), 0.10);
        // Build the massspectrum with annotations.
        SpectrumPanel sp1 = new SpectrumPanel(lQuery.getMZArray(), lQuery.getIntensityArray(), lQuery.getPrecursorMZ(), lQuery.getChargeString(), lQuery.getTitle());
        sp1.setAnnotations(lFusedFragmentionsVec);
        JFrame app1 = new JFrame("Spectrum for " + lQuery.getTitle() + " - " + aPeptideHit.getModifiedSequence() + " - " + "Score: " + aPeptideHit.getIonsScore());
        app1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        app1.getContentPane().add(sp1);
        app1.setLocation(100, 100);
        app1.setSize(1200, 300);
        app1.setVisible(true);
    }

    /**
     * This static method returns an annotated spectrumpanel by passing the MascotDatfile, the Querynumber and the
     * PeptidehitNumber.
     *
     * @param aMdf                 MascotDatfile instance
     * @param aQueryNumber         Querynumber
     * @param aPeptidehitNumber    Peptidehit
     * @param aIntensityPercentage IntensityPercentage for Fused matches calculation.
     * @return JPanel with the spectrum and the fused annotations of the identification.
     */
    public static JPanel getAnnotatedSpectrumPanel(MascotDatfile aMdf, int aQueryNumber, int aPeptidehitNumber, double aIntensityPercentage) {
        JPanel jpan1 = new JPanel(new BorderLayout());
        //jpan1.setLayout(new BoxLayout(jpan1, BoxLayout.Y_AXIS));
        PeptideHit lPeptideHit = aMdf.getQueryToPeptideMap().getPeptideHitOfOneQuery(aQueryNumber, aPeptidehitNumber);
        Query lQuery = (Query) ((aMdf.getQueryList().get(aQueryNumber - 1)));
        // Get the peptidehitannotations.
        if (lQuery == null) {
            try {
                throw new IllegalArgumentException("The requested query does not extist!");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                System.err.println("System.exit(1)");
                System.exit(1);
            }

        }
        PeptideHitAnnotation lPeptideHitAnnotation = lPeptideHit.getPeptideHitAnnotation(aMdf.getMasses(), aMdf.getParametersSection(), lQuery.getPrecursorMZ(), lQuery.getChargeString());
        Vector lFusedFragmentionsVec = lPeptideHitAnnotation.getFusedMatchedIons(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1(), lQuery.getMaxIntensity(), aIntensityPercentage);

        JPanel jpanPeptideHit = new JPanel(new BorderLayout());
        jpanPeptideHit.add(new JLabel("Modified Sequence: " + lPeptideHit.getModifiedSequence()), BorderLayout.EAST);
        jpanPeptideHit.add(new JLabel("Score vs Threshold: " + lPeptideHit.getIonsScore() + " \\ " + lPeptideHit.calculateIdentityThreshold()), BorderLayout.WEST);

        // Build the massspectrum with annotations.
        SpectrumPanel sp1 = new SpectrumPanel(lQuery.getMZArray(), lQuery.getIntensityArray(), lQuery.getPrecursorMZ(), lQuery.getChargeString(), lQuery.getTitle());
        sp1.setAnnotations(lFusedFragmentionsVec);


        jpan1.add(sp1, BorderLayout.CENTER);
        jpan1.add(jpanPeptideHit, BorderLayout.SOUTH);
        jpan1.validate();

        return jpan1;
    }

    /**
     * This static method returns an annotated spectrumpanel by passing the MascotDatfile, the Querynumber and the
     * PeptidehitNumber.
     *
     * @param aMdf              MascotDatfile instance
     * @param aQueryNumber      Querynumber
     * @param aPeptidehitNumber Peptidehit
     * @return JPanel with the spectrum and the fused annotations of the identification.
     */
    public static JPanel getAnnotatedSpectrumPanel(MascotDatfile aMdf, int aQueryNumber, int aPeptidehitNumber) {
        return getAnnotatedSpectrumPanel(aMdf, aQueryNumber, aPeptidehitNumber, 0.10);
    }

}
