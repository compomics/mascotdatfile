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

package com.compomics.mascotdatfile.research.tool.spectrumviewer.spectrumviewer_model;

import com.compomics.mascotdatfile.util.mascot.MascotDatfile;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.PeptideHitAnnotation;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.util.gui.spectrum.SpectrumPanel;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 27-okt-2006
 * Time: 16:27:23
 */
public class DatfileTreePanel extends JPanel {
    private MascotDatfile iMascotDatfile = null;

    private JPanel jpanMain = null;
    private JSplitPane splt1 = null;
    private JPanel jpanSpectrum = null;
    private JPanel jpanTree = null;
    private SpectrumPanel iSpectrumPanel;

    private JTree treeDatfile = null;
    private DatfileTreeModel iDatfileTreeModel = null;

    /**
     * Constructor to create a DatfileTreePanel.
     * This class extends JPanel, it contains a splitpane around a MascotDatfile.
     * At one side, there will be a tree with all the identifications,
     * at the other side an annotated SpectrumPanel instance of the identification is loaded on selection.
     * @param aMascotDatfile MascotDatfile instance whereby the tree is build.
     */
    public DatfileTreePanel(MascotDatfile aMascotDatfile) {
        iMascotDatfile = aMascotDatfile;
        setDefaultFilterSettingsOnTreeModel();
        construct();
    }

    /**
     * Construct the main panel.
     */
    private void construct(){
        this.removeAll();
        jpanMain = new JPanel(new BorderLayout());
        construct_jpanSpectrum();
        construct_jpanTree();

        splt1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jpanTree, jpanSpectrum);
        
        this.setLayout(new BorderLayout());
        this.add(splt1, BorderLayout.CENTER);
        this.validate();
    }

    /**
     * Construct the spectrumpanel.
     */
    private void construct_jpanSpectrum(){
        jpanSpectrum = new JPanel(new BorderLayout());
        jpanSpectrum.add(new JLabel("Click a PeptideHit in the Tree structure."), BorderLayout.CENTER);
        jpanSpectrum.validate();
    }

    /**
     * Construct the tree structure of the MascotDatfile.
     */
    private void construct_jpanTree(){
        // Create the JTree and
        treeDatfile = new JTree(iDatfileTreeModel);
        treeDatfile.setCellRenderer(new DatfileTreeCellRenderer());
        treeDatfile.addTreeSelectionListener(new TreeSelectionListener(){
            /** {@inheritDoc} */
            public void valueChanged(TreeSelectionEvent e) {
                        TreePath path = treeDatfile.getSelectionPath();
                        if(path != null) {
                            Object lTemp = path.getLastPathComponent();
                            if(lTemp instanceof PeptideHit) {
                                PeptideHit lPeptideHit = (PeptideHit) lTemp;
                                Query lQuery = (Query)path.getParentPath().getLastPathComponent();
                                jpanSpectrum.removeAll();
                                iSpectrumPanel = new SpectrumPanel(lQuery.getMZArray(), lQuery.getIntensityArray(), lQuery.getPrecursorMZ(), lQuery.getChargeString(), lQuery.getTitle());
                                PeptideHitAnnotation pha = lPeptideHit.getPeptideHitAnnotation(iMascotDatfile.getMasses(), iMascotDatfile.getParametersSection(), lQuery.getPrecursorMZ(), lQuery.getChargeString());

                                // Minimal peak intensity for fused ions is hard coded at 5%!!!
                                // ** FUSED **
                                //Vector annotations = pha.getFusedMatchedIons(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1(), lQuery.getMaxIntensity(), 0.05);

                                // ** MASCOT **
                                Vector annotations = pha.getMatchedIonsByMascot(lQuery.getPeakList(), lPeptideHit.getPeaksUsedFromIons1());


                                // ** B & Y IONS **
                                //Vector annotations = pha.getMatchedBYions(lQuery.getPeakList());


                                iSpectrumPanel.setAnnotations(annotations);
                                //lSpecPanel.setFont(jpanSpectrum.getFont().deriveFont(15.0F));
                                
                                JPanel jpanPeptideHit = new JPanel();
                                BoxLayout lBoxLayout = new BoxLayout(jpanPeptideHit, BoxLayout.LINE_AXIS);
                                String lFilterSettingThresholdString = iDatfileTreeModel.getFilterSettingThresholdString();
                                double lFilterSettingThreshold = iDatfileTreeModel.getFilterSettingThreshold();
                                MathContext lMathContext = new MathContext(3);
                                BigDecimal lThreshold = new BigDecimal(lPeptideHit.calculateIdentityThreshold(lFilterSettingThreshold), lMathContext);

                                jpanPeptideHit.add(Box.createHorizontalStrut(10));
                                jpanPeptideHit.add(new JLabel("Modified Sequence: " + lPeptideHit.getModifiedSequence() + "     "));
                                jpanPeptideHit.add(Box.createHorizontalStrut(50));
                                jpanPeptideHit.add(new JLabel("Score vs " + lFilterSettingThresholdString +" Threshold: " + lPeptideHit.getIonsScore() + " \\ " + lThreshold.doubleValue() + "    "));
                                jpanPeptideHit.add(Box.createHorizontalStrut(10));




                                jpanSpectrum.add(iSpectrumPanel, BorderLayout.CENTER);
                                jpanSpectrum.add(jpanPeptideHit, BorderLayout.SOUTH);

                                jpanSpectrum.validate();
                                jpanSpectrum.repaint();
                            }
                        }
                    }
        });
        treeDatfile.setExpandsSelectedPaths(true);
        treeDatfile.validate();
        jpanTree = new JPanel(new BorderLayout());
        jpanTree.add(new JScrollPane(treeDatfile), BorderLayout.CENTER);
        jpanTree.validate();
    }

    /**
     * This method changes the selection path of the tree.
     * @param aInput String may contain two sources.
     * <ol type='1'><li>QueryNumber - '35' will select Query35.</li>
     *              <li>SpectrumFilename - Title value of the query.</li>
     * </ol>
     */
    public void select_jpanTree_node(String aInput){

        // Check if the input is a Querynumber(Integer) or SpectrumFile(String)
        boolean lIsQuerynumber = true;
        try {
            Integer.parseInt(aInput);
        } catch(NumberFormatException e){
            lIsQuerynumber = false;
        }
        // 1. The input was an int (try as Querynumber).
        if(lIsQuerynumber){
            int lQueryNumber = Integer.parseInt(aInput);
            TreePath tpQuery = treeDatfile.getNextMatch("Query " + lQueryNumber, 1, Position.Bias.Forward);
            TreePath tpPeptideHit = null;
            if(tpQuery == null){
                JOptionPane.showMessageDialog(this, "Query " + lQueryNumber + " was not found in the tree.");
            }else {
                Object lPeptideHit = treeDatfile.getModel().getChild(tpQuery.getLastPathComponent(),0);
                tpPeptideHit = tpQuery.pathByAddingChild(lPeptideHit);
                treeDatfile.expandPath(tpQuery);
                treeDatfile.setSelectionPath(tpPeptideHit);
                treeDatfile.getTreeSelectionListeners()[0].valueChanged(new TreeSelectionEvent(
                        treeDatfile, new TreePath[]{tpQuery,tpPeptideHit}, new boolean[]{true, false}, tpQuery, tpPeptideHit
                ));
            }
        }

        // 2. The input was a String (try as SpectrumFilename).
        else{
            String lSpectrumFilename = aInput;
            if(iMascotDatfile.getSpectrumFilenameToQuerynumberMap().get(lSpectrumFilename) == null){
                JOptionPane.showMessageDialog(this, "Spectrumfilename " + lSpectrumFilename +
                        " has no corresponding Querynumber in the datfile.");
            }else{
                Object o = iMascotDatfile.getSpectrumFilenameToQuerynumberMap().get(lSpectrumFilename);
                if(o instanceof Integer){
                    int lQueryNumber = ((Integer)(o)).intValue();
                    TreePath tpQuery = treeDatfile.getNextMatch("Query " + lQueryNumber, 1, Position.Bias.Forward);
                    TreePath tpPeptideHit = null;
                    if(tpQuery == null){
                        JOptionPane.showMessageDialog(this, "Spectrum" + lSpectrumFilename + " was not found in the tree.");
                    }else {
                        Object lPeptideHit = treeDatfile.getModel().getChild(tpQuery.getLastPathComponent(),0);
                        tpPeptideHit = tpQuery.pathByAddingChild(lPeptideHit);
                        treeDatfile.expandPath(tpQuery);
                        treeDatfile.setSelectionPath(tpPeptideHit);
                        treeDatfile.getTreeSelectionListeners()[0].valueChanged(new TreeSelectionEvent(
                                treeDatfile, new TreePath[]{tpQuery,tpPeptideHit}, new boolean[]{true, false}, tpQuery, tpPeptideHit
                        ));
                    }
                }
            }
        }
    }

    /**
     * Sets the filter settings to the DatfileTreeModel and a new JTree is constructed.
     * @param aConfidence
     */
    public void setFilterSettingsOnTreeModel(double aConfidence){
        int lConfidencePercentage = (new Double((1-aConfidence)*100)).intValue();
        DatfileTreeModel lTempDatfileTreeModel = new DatfileTreeModel(iMascotDatfile, "MascotDatfile @" + lConfidencePercentage + "%");
        lTempDatfileTreeModel.setFilterSettingThreshold(aConfidence);
        iDatfileTreeModel = lTempDatfileTreeModel;
        construct();
    }

    /**
     * Sets the default filter settings to the DatfileTreeModel, used in the constructor.
     */
    public void setDefaultFilterSettingsOnTreeModel(){
        iDatfileTreeModel = new DatfileTreeModel(iMascotDatfile, "MascotDatfile");
    }


    /**
     * This method rescales the X-axis while notifying the observers.
     *
     * @param aMinMass  double with the new minimum mass to display.
     * @param aMaxMass  double with the new maximum mass to display.
     */
    public void rescale(double aMinMass, double aMaxMass){
        iSpectrumPanel.rescale(aMinMass, aMaxMass);
        jpanSpectrum.repaint();
    }


    /**
     * Returns the JPanel with the spectrum.
     * @return the JPanel with the spectrum.
     */
    public JPanel getSpectrumPanel() {
        return jpanSpectrum;
    }
}

