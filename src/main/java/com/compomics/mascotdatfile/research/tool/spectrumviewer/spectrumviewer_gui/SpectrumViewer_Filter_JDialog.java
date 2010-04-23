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

package com.compomics.mascotdatfile.research.tool.spectrumviewer.spectrumviewer_gui;

import org.apache.log4j.Logger;

import com.compomics.util.gui.JLabelAndComponentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA. User: kenny Date: 30-okt-2006 Time: 16:50:49
 */
public class SpectrumViewer_Filter_JDialog extends JDialog {
    // Class specific log4j logger for SpectrumViewer_Filter_JDialog instances.
    private static Logger logger = Logger.getLogger(SpectrumViewer_Filter_JDialog.class);

    DataBridge iTarget = null;
    String iPropsFile = null;

    private JTextField txtThreshold = null;

    private JButton btnApply = null;
    private JButton btnCancel = null;


    /**
     * Basic constructor
     *
     * @param aOwner     Owner Frame of this Dialog.
     * @param aTarget    Databridge to pass the filter settings.
     * @param aTitle     Title of the dialog.
     * @param aPropsFile Properties file path for default settings.
     * @throws java.awt.HeadlessException
     */
    public SpectrumViewer_Filter_JDialog(Frame aOwner, DataBridge aTarget, String aTitle, String aPropsFile) throws HeadlessException {
        super(aOwner, aTitle, true);
        this.iTarget = aTarget;
        this.iPropsFile = aPropsFile;
        this.show_Filter_JDialog();
    }

    private void show_Filter_JDialog() {
        this.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             */
            public void windowClosing(WindowEvent e) {
                btnCancelTriggered();
            }
        });
        this.constructScreen();
        this.tryToLoadParams();

        this.setLocation(100, 100);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * This method will initialize and lay-out all components.
     */
    private void constructScreen() {
        btnApply = new JButton("Apply");
        btnApply.setMnemonic(KeyEvent.VK_A);
        btnApply.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                btnApplyTriggered();
            }
        });
        btnApply.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             */
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    btnApply.requestFocus();
                } else {
                    super.keyTyped(e);
                }
            }
        });

        btnCancel = new JButton("Cancel");
        btnCancel.setMnemonic(KeyEvent.VK_C);
        btnCancel.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                btnCancelTriggered();
            }
        });
        btnCancel.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             */
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    btnCancel.requestFocus();
                } else {
                    super.keyTyped(e);
                }
            }
        });

        txtThreshold = new JTextField(25);
        txtThreshold.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             */
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    txtThreshold.requestFocus();
                } else {
                    super.keyTyped(e);
                }
            }
        });
        JLabelAndComponentPanel jpanCenter = new JLabelAndComponentPanel(new JLabel[]{new JLabel("Identity threshold (0.05 means 95% confidence).")},
                new JTextField[]{txtThreshold});
        jpanCenter.setToolTipText("Set an identitythreshold to filter the tree so it only contains PeptideHits scoring " +
                "above the entered threshold. If you want the tree to list PeptideHits scoring above 95% Confidence," +
                " then the input value alpha must be 0.05.\n(Confidence = 1 - alpha)");
        jpanCenter.setBorder(BorderFactory.createTitledBorder("SpectrumViewer Filter Settings."));

        JPanel jpanButtons = new JPanel();
        jpanButtons.setLayout(new BoxLayout(jpanButtons, BoxLayout.X_AXIS));

        jpanButtons.add(Box.createHorizontalGlue());
        jpanButtons.add(btnApply);
        jpanButtons.add(Box.createRigidArea(new Dimension(15, btnApply.getHeight())));
        jpanButtons.add(btnCancel);
        jpanButtons.add(Box.createRigidArea(new Dimension(10, btnCancel.getHeight())));

        JPanel jpanMain = new JPanel();
        jpanMain.setLayout(new BoxLayout(jpanMain, BoxLayout.Y_AXIS));
        jpanMain.add(Box.createVerticalStrut(10));
        jpanMain.add(jpanCenter);
        jpanMain.add(Box.createVerticalGlue());
        jpanMain.add(jpanButtons);
        jpanMain.add(Box.createVerticalStrut(10));

        this.getContentPane().add(jpanMain, BorderLayout.CENTER);
    }

    /**
     * Gather and pass the settings by the DataBridge.
     */
    public void btnApplyTriggered() {
        double lIdentityThreshold = Double.parseDouble(txtThreshold.getText());
        iTarget.passFilterSettings(lIdentityThreshold);
        super.pack();
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Cancel without saving any settings.
     */
    public void btnCancelTriggered() {
        this.setVisible(false);
        this.dispose();
    }

    /**
     * This method attempts to load connection parameters from a properties file in the classpath. If this file is not
     * found, nothing happens. If it is found, the parameters found will be filled out.
     */
    private void tryToLoadParams() {
        if (iPropsFile != null) {
            try {
                Properties p = new Properties();
                InputStream is = ClassLoader.getSystemResourceAsStream(iPropsFile);
                if (is == null) {
                    is = this.getClass().getClassLoader().getResourceAsStream(iPropsFile);
                    if (is == null) {
                        // Leave it at that.
                        return;
                    }
                    System.out.println("local classloader.");
                }
                p.load(is);

                String lDatfile = p.getProperty("FILTER_THRESHOLD");
                if (lDatfile != null) {
                    this.txtThreshold.setText(lDatfile.trim());
                }
                is.close();
            } catch (Exception e) {
                // Do nothing.
                e.printStackTrace();
            }
        }
    }
}
