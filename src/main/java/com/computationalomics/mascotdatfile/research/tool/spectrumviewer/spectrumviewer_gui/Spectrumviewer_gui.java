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

import com.compomics.mascotdatfile.research.tool.spectrumviewer.spectrumviewer_model.DatfileTreePanel;
import com.compomics.mascotdatfile.research.util.DatfileLocation;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;

/**
 * <b>Created by IntelliJ IDEA.</b> User: Kenni Date: 6-jul-2006 Time: 21:09:33
 * <p/>
 * <br>This Class has a main that will start a gui thread to show a spectrum with annotations.</br>
 */
public class Spectrumviewer_gui extends JFrame implements DataBridge {
    private JButton btnHardDisk = null;
    private JButton btnDatabase = null;
    private JButton btnUrl = null;

    private JPanel jpanMenuBar = null;
    private JPanel jpanDatfileTree = null;

    private DatfileTreePanel dtp = null;

    private JMenuBar mbar1 = null;
    private JMenu menuOpen = null;
    private JMenu menuDo = null;
    private JMenuItem menuItem1 = null;

    // Provided in the classpath!
    private String iPropertiesFile = "Spectrumviewer_gui.properties";


    /**
     * This constructer launches the gui thread by the constructScreen method.
     */
    public Spectrumviewer_gui(){
        super("Spectrumviewer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        constructScreen();
        this.validate();
        this.pack();
    }

    /**
     * This method contructs the JFrame, it is called in the constructor.
     */
    public void constructScreen(){

        // Create the menubar.
        mbar1 = new JMenuBar();

        // Create the "open" menu.
        menuOpen = new JMenu("Datfile");
        menuOpen.setMnemonic(KeyEvent.VK_D);

        // Create the "do" menu.
        menuDo = new JMenu("Tools");
        menuDo.setMnemonic(KeyEvent.VK_T);

        // Add the menu's to the bar.
        mbar1.add(menuOpen);
        mbar1.add(menuDo);


        // Create a group of items for the "Open Datfile" menu (Datfile dialog launchers).
        menuItem1 = new JMenuItem("URL Mascot Server",KeyEvent.VK_U);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menuItem1.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                launch_URL_JDialog();
            }
        });
        menuOpen.add(menuItem1);

        menuItem1 = new JMenuItem("Hard Disk",KeyEvent.VK_H);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
        menuItem1.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                launch_HDD_JDialog();
            }
        });
        menuOpen.add(menuItem1);

        // Create a group of items for the "Do" menu.
        // 1. "Filter" - Filters the tree node - queries by the confidence of its identifications.
        menuItem1 = new JMenuItem("Filter", KeyEvent.VK_F);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
        menuItem1.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                launch_Filter_JDialog();
            }
        });
        menuDo.add(menuItem1);

        // 2. "Select Query" - Select a treenode by its querynumber or spectrumfilename -
        menuItem1 = new JMenuItem("Select Query", KeyEvent.VK_S);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem1.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                if(dtp == null){
                    JOptionPane.showMessageDialog(jpanMenuBar, "First load a datfile by the Datfile menu.");
                }else{
                    String lInput = JOptionPane.showInputDialog(jpanMenuBar, "Insert the Querynumber or SpectrumFilename.", "Select Query", JOptionPane.QUESTION_MESSAGE).trim();
                    if(lInput != null){
                        dtp.select_jpanTree_node(lInput);
                    }
                }
            }
        });

        menuDo.add(menuItem1);

        // This function requires the ITEXT pdf library!!!
        //3. "Pdf output" - Select a outputfile to export the spectrumpanel to.
        menuItem1 = new JMenuItem("Pdf output", KeyEvent.VK_P);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuItem1.addActionListener(new ActionListener() {
            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                pdfOutputTriggered();
            }
        });
        menuDo.add(menuItem1);


        //Create a Panel for the menubar.
        jpanMenuBar = new JPanel();
        jpanMenuBar.setLayout(new BorderLayout(5,5));
        jpanMenuBar.add(mbar1);


        jpanDatfileTree = new JPanel(new BorderLayout(5,5));
        jpanDatfileTree.add(new JLabel("Select a datfile through the Datfile menu.", JLabel.CENTER));

        JPanel jpanMain = new JPanel(new BorderLayout());
        jpanMain.add(jpanMenuBar, BorderLayout.NORTH);
        jpanMain.add(jpanDatfileTree, BorderLayout.CENTER);
        this.getContentPane().add(jpanMain);
    }

    /**
     * This method is used by the dialog to set the DatfileTreePanel.
     * @param mdf MascotDatfile instance
     */
    public void passMascotDatfile(MascotDatfile mdf, String aFilename) {
        jpanDatfileTree.removeAll();

        // Create a DatfileTreePanel from the returning MascotDatfile.
        dtp = new DatfileTreePanel(mdf);

        // Add and refresh the jpanDatfileTree.
        jpanDatfileTree.add(dtp, BorderLayout.CENTER);
        jpanDatfileTree.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jpanDatfileTree.validate();
        this.setTitle("Spectrumviewer - MascotDatfile " + aFilename);
        this.pack();
    }

    /**
     * This method is used by the dialog to apply the user defined filter settings.
     *
     */
    public void passFilterSettings(double aIntensityThreshold){
        dtp.setFilterSettingsOnTreeModel(aIntensityThreshold);
        this.validate();
    }

    /**
     * This method is called whenever the user clicked the button to
     * export the spectrum to pdf.
     */
    private void pdfOutputTriggered() {
        if(dtp != null){

             // Looping boolean.
            boolean lbContinue = true;
            // Previous selected path.
            String previousPath = "/";
            // The file filter to use.
            FileFilter filter = new FileFilter() {
                public boolean accept(File f) {
                    boolean result = false;
                    if(f.isDirectory() || f.getName().endsWith(".pdf")) {
                        result = true;
                    }
                    return result;
                }

                public String getDescription() {
                    return "PDF file";
                }
            };
            while(lbContinue) {
                JFileChooser jfc = new JFileChooser(previousPath);
                jfc.setDialogTitle("Save spectrum panel as PDF file");
                jfc.setDialogType(JFileChooser.SAVE_DIALOG);
                jfc.setFileFilter(filter);
                int returnVal = jfc.showSaveDialog(this.getParent());
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = jfc.getSelectedFile();
                    // Append the file extension if it is not already there.
                    if(jfc.getFileFilter() == filter && !file.getName().toLowerCase().endsWith(".pdf")) {
                        file = new File(file.getAbsolutePath() + ".pdf");
                    }
                    // Check for existing file.
                    if(file.exists()) {
                        int reply = JOptionPane.showConfirmDialog(this.getParent(), new String[] {"File '" + file.getAbsolutePath() + "' exists.", "Do you wish to overwrite?"}, "File exists!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(reply != JOptionPane.YES_OPTION) {
                            previousPath = file.getParent();
                            continue;
                        }
                    }

                    // Output to PDF.
                    JPanel jpanSpectrum = dtp.getSpectrumPanel();
                    float lWidthFloat = (new Double(jpanSpectrum.getSize().getWidth())).floatValue()+20;
                    float lHeightFloat = (new Double(jpanSpectrum.getSize().getHeight())).floatValue();
                    int lWidthInt = (new Double(jpanSpectrum.getSize().getWidth())).intValue();
                    int lHeightInt = (new Double(jpanSpectrum.getSize().getHeight())).intValue();

                    Document document = new Document(new com.lowagie.text.Rectangle(lWidthInt, lHeightInt));

                    try {
                        PdfWriter writer;
                        writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                        document.open();
                        PdfContentByte cb = writer.getDirectContent();
                        PdfTemplate tp = cb.createTemplate(lWidthFloat, lHeightFloat);
                        Graphics2D g2;
                        g2 = tp.createGraphicsShapes(lWidthFloat, lHeightFloat);
                        jpanSpectrum.print(g2);
                        g2.dispose();
                        cb.addTemplate(tp, 0, 0);
                        writer.flush();
                        JOptionPane.showMessageDialog(this, "Data successfully written to '" + file + "'!", "Output completed!", JOptionPane.INFORMATION_MESSAGE);
                        document.close();
                    } catch (DocumentException e) {
                        JOptionPane.showMessageDialog(this, new String[]{"Unable to create PDF file!", e.getMessage()}, "Unable to create PDF file!", JOptionPane.WARNING_MESSAGE);
                    } catch (FileNotFoundException e) {
                        // Should never occur.
                        JOptionPane.showMessageDialog(this, new String[]{"Unable to write file!", e.getMessage()}, "Unable to write file!", JOptionPane.WARNING_MESSAGE);
                    }

                    lbContinue = false;
                } else {
                    lbContinue = false;
                }
            }
        }else{
            JOptionPane.showMessageDialog(this, "You need to load a datfile and SpectrumPanel first!", "No datfile loaded yet!", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void launch_URL_JDialog(){
        JDialog d = new Spectrumviewer_URL_JDialog(this, this, "Spectrumviewer URL dialog", iPropertiesFile);
    }
    
    private void launch_HDD_JDialog(){
        File lDatfileFile = null;
        boolean lbContinue = true;
        while(lbContinue) {
            JFileChooser lJFileChooser = new JFileChooser("/");
            int lReturnVal = lJFileChooser.showOpenDialog(this);
            if (lReturnVal == JFileChooser.APPROVE_OPTION) {
                lDatfileFile = lJFileChooser.getSelectedFile();
                if(lDatfileFile == null || !lDatfileFile.exists()) {
                    JOptionPane.showMessageDialog(this, "You need to specify an existing datfile!", "Datfile not found!", JOptionPane.WARNING_MESSAGE);
                } else {
                    lbContinue = false;
                }
            } else if(lReturnVal == JFileChooser.CANCEL_OPTION) {
                return;
            }
        }

        String errorString = null;
        MascotDatfile lMdf = null;
        try {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            DatfileLocation dfl = new DatfileLocation(DatfileLocation.HARDDISK, lDatfileFile.getPath());
            lMdf = dfl.getDatfile();
        } catch(ClassNotFoundException cnfe) {
            errorString = "HD class was not found! (" + cnfe.getMessage() + ")";
        } catch(IllegalAccessException iae) {
            errorString = "(" + iae.getMessage() + ")";
        } catch(InstantiationException ie) {
            errorString = "Could not create instance of the MascotDatfile class! (" + ie.getMessage() + ")";
        } catch(SQLException sqle) {
            errorString = "(" + sqle.getMessage() + ")";
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if(errorString != null) {
            JOptionPane.showMessageDialog(this, new String[]{"Unable to load " + lDatfileFile.getPath() + ". " + errorString, "\n"}, "Unable to get the datfile!", JOptionPane.ERROR_MESSAGE);
        } else {
            this.passMascotDatfile(lMdf, lDatfileFile.getPath());
        }
    }


    private void launch_Filter_JDialog(){
        JDialog d = new SpectrumViewer_Filter_JDialog(this, this, "Spectrumviewer Filter Settings", iPropertiesFile);
    }



    /**
     * This main method will be called to launch this tool.
     * @param args - no params necessary
     */
    public static void main(String[] args) {
        JFrame frame = new Spectrumviewer_gui();
        frame.setSize(750,100);
        frame.setLocation(50,50);
        frame.setVisible(true);
    }

}
