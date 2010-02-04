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

import com.compomics.mascotdatfile.research.util.DatfileLocation;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile;
import com.compomics.util.gui.JLabelAndComponentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <b>Created by IntelliJ IDEA.</b> User: Kenni Date: 9-jul-2006 Time: 13:27:41
 * <p/>
 * <br>This Class is a JDialog to retrieve Spectrum information from a URL</br>
 */
public class Spectrumviewer_URL_JDialog extends JDialog {

    private DataBridge iTarget = null;
    private String iPropsFile = null;

    private JTextField txtServer = null;
    private JTextField txtDate = null;
    private JTextField txtDatfile = null;

    private JButton btnShow = null;
    private JButton btnCancel = null;


    public Spectrumviewer_URL_JDialog(JFrame aParent, DataBridge aTarget, String aTitle, String aPropsFile) throws HeadlessException {
        super(aParent, aTitle, true);
        this.iTarget = aTarget;
        this.iPropsFile = aPropsFile;
        this.showSpectrumviewer_URL_JDialog();
    }

    /**
     * This method actually shows the Spectrumviewer_URL_JDialog.
     * It takes care of the GUI related stuff.
     */
    private void showSpectrumviewer_URL_JDialog() {
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
    txtServer = new JTextField(25);
    txtServer.addKeyListener(new KeyAdapter() {
        /**
         * Invoked when a key has been typed.
         * This event occurs when a key press is followed by a key release.
         */
        public void keyTyped(KeyEvent e) {
            if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                txtDate.requestFocus();
            } else {
                super.keyTyped(e);
            }
        }
    });
    txtDate = new JTextField(25);
    txtDate.addKeyListener(new KeyAdapter() {
        /**
         * Invoked when a key has been typed.
         * This event occurs when a key press is followed by a key release.
         */
        public void keyTyped(KeyEvent e) {
            if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                txtDatfile.requestFocus();
            } else {
                super.keyTyped(e);
            }
        }
    });
    txtDatfile = new JTextField(25);
    txtDatfile.addKeyListener(new KeyAdapter() {
        /**
         * Invoked when a key has been typed.
         * This event occurs when a key press is followed by a key release.
         */
        public void keyTyped(KeyEvent e) {
            if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                txtDatfile.requestFocus();
            } else {
                super.keyTyped(e);
            }
        }
    });
        JLabelAndComponentPanel jpanTop = new JLabelAndComponentPanel(new JLabel[] {new JLabel("Mascot Server"), new JLabel("Date"), new JLabel("Datfile")},
                                                            new JTextField[]{txtServer, txtDate, txtDatfile});
        jpanTop.setBorder(BorderFactory.createTitledBorder("Datfile source"));

        btnShow = new JButton("Show");
        btnShow.setMnemonic(KeyEvent.VK_S);
        btnShow.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                btnShowTriggered();
            }
        });
        btnShow.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been typed.
             * This event occurs when a key press is followed by a key release.
             */
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    btnShowTriggered();
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
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    btnCancelTriggered();
                }
            }
        });

        JPanel jpanButtons = new JPanel();
        jpanButtons.setLayout(new BoxLayout(jpanButtons, BoxLayout.X_AXIS));

        jpanButtons.add(Box.createHorizontalGlue());
        jpanButtons.add(btnShow);
        jpanButtons.add(Box.createRigidArea(new Dimension(15, btnShow.getHeight())));
        jpanButtons.add(btnCancel);
        jpanButtons.add(Box.createRigidArea(new Dimension(10, btnShow.getHeight())));

        JPanel jpanTotal = new JPanel();
        jpanTotal.setLayout(new BoxLayout(jpanTotal, BoxLayout.Y_AXIS));
        jpanTotal.add(jpanTop);
        jpanTotal.add(Box.createRigidArea(new Dimension(jpanTop.getWidth(), 10)));
        jpanTotal.add(jpanButtons);

        this.getContentPane().add(jpanTotal, BorderLayout.CENTER);
    }

    /**
     * This method is called when the user attempts to connect.
     */
    private void btnShowTriggered() {
        String lServer = txtServer.getText().trim();
        if( lServer.charAt(lServer.length()-1) != '/'){
            lServer = lServer + '/';
        }
        String lDate = txtDate.getText().trim();
        String lDatfile = txtDatfile.getText().trim();

        if(lServer.equals("")) {
            JOptionPane.showMessageDialog(this, "Datfile source server needs to be specified!!", "No server specified!", JOptionPane.ERROR_MESSAGE);
            txtServer.requestFocus();
            return;
        }

        if(lDate.equals("")) {
            JOptionPane.showMessageDialog(this, "Creation date of datfile needs to be specified!", "No date specified!", JOptionPane.ERROR_MESSAGE);
            txtDate.requestFocus();
            return;
        }

        String errorString = null;
        MascotDatfile lMdf = null;
        try {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            DatfileLocation dfl = new DatfileLocation(DatfileLocation.URL, new String[]{lServer, lDate, lDatfile});
            lMdf = dfl.getDatfile();
        } catch(ClassNotFoundException cnfe) {
            errorString = "URL class was not found! (" + cnfe.getMessage() + ")";
        } catch(IllegalAccessException iae) {
            errorString = "(" + iae.getMessage() + ")";
        } catch(InstantiationException ie) {
            errorString = "Could not create instance of the MascotDatfile class! (" + ie.getMessage() + ")";
        } catch(SQLException sqle) {
            errorString = "(" + sqle.getMessage() + ")";
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if(errorString != null) {
            JOptionPane.showMessageDialog(this, new String[]{"Unable to load " + lDatfile + "from '" + lServer + ". " + errorString, "\n"}, "Unable to get the datfile!", JOptionPane.ERROR_MESSAGE);
        } else {
            this.iTarget.passMascotDatfile(lMdf, lDatfile);
            super.pack();
            this.setVisible(false);
            this.dispose();
        }
    }

    /**
     * This method is called when the user presses cancel.
     */
    private void btnCancelTriggered() {
        this.setVisible(false);
        this.dispose();
    }

    /**
     * This method attempts to load connection parameters from
     * a properties file in the classpath.
     * If this file is not found, nothing happens.
     * If it is found, the parameters found will be filled out.
     */
    private void tryToLoadParams() {
        if(iPropsFile != null) {
            try {
                Properties p = new Properties();
                InputStream is = ClassLoader.getSystemResourceAsStream(iPropsFile);
                if(is == null) {
                    is = this.getClass().getClassLoader().getResourceAsStream(iPropsFile);
                    if(is == null) {
                        // Leave it at that.
                        return;
                    }
                    System.out.println("local classloader.");
                }
                p.load(is);

                String lServer = p.getProperty("URL_SERVER");
                if(lServer != null) {
                    this.txtServer.setText(lServer.trim());
                }
                String lDate = p.getProperty("URL_DATE");
                if(lDate != null) {
                    this.txtDate.setText(lDate.trim());
                }
                String lDatfile = p.getProperty("DATFILE");
                if(lDatfile != null){
                    this.txtDatfile.setText(lDatfile.trim());
                }
                is.close();
            } catch(Exception e) {
                // Do nothing.
                e.printStackTrace();
            }
        }
    }
}
