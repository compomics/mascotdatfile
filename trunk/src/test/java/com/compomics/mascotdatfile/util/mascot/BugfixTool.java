package com.compomics.mascotdatfile.util.mascot;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;
import com.compomics.mascotdatfile.util.mascot.iterator.QueryEnumerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * Created by IntelliJ IDEA. User: kenny Date: Mar 31, 2010 Time: 1:37:36 PM
 * <p/>
 * This class
 */
public class BugfixTool {


    /**
     * Created by IntelliJ IDEA. User: kenny Date: Mar 9, 2010 Time: 11:24:26 AM
     * <p/>
     * This class
     */


    private static JTextArea txtMessage;
    private static JPanel jpanContent;


    public static void main(String[] args) {

        JFrame frame = new JFrame();
        jpanContent = new JPanel();

        txtMessage = new JTextArea("Please select a Mascot results file");
        txtMessage.setMinimumSize(new Dimension(300, 200));

        final JFileChooser chooser = new JFileChooser(System.getProperties().get("user.home").toString());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setVisible(true);


        JButton btnBrowse = new JButton("Browse MascotDatfile..");
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                int lResult = chooser.showOpenDialog(null);
                if (lResult == JFileChooser.APPROVE_OPTION) {
                    File lFile = chooser.getSelectedFile();
                    testMascotDatfile(lFile);
                }
            }
        });

        jpanContent.setLayout(new BorderLayout());
        jpanContent.add(btnBrowse, BorderLayout.NORTH);
        jpanContent.add(new JScrollPane(txtMessage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        frame.setContentPane(jpanContent);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(640, 480);
    }

    private static void testMascotDatfile(File aFile) {

        try {
            // First issue, corrupt datfile due to Fat32 file system of creators.
            //MascotDatfileInf lMascotDatfileInf = MascotDatfileFactory.create("/Users/kenny/Temp/F046749.dat", MascotDatfileType.INDEX);

            // Second issue, failing indexing strategy.
            MascotDatfileInf lMascotDatfileInf = MascotDatfileFactory.create(aFile.getCanonicalPath(), MascotDatfileType.INDEX);

            QueryToPeptideMapInf qtpmap = lMascotDatfileInf.getQueryToPeptideMap();

            int lNumberOfQueries = lMascotDatfileInf.getNumberOfQueries();
            int lNumberOfQueries2 = qtpmap.getNumberOfQueries();

            int lQueryNumber = 20000;
            PeptideHit lPeptideHit = qtpmap.getPeptideHitOfOneQuery(lQueryNumber);

            if (lPeptideHit != null) {
                print("Sequence of Peptidehit " + lQueryNumber + " equals " + lPeptideHit.getSequence());
            } else {
                print("Peptidehit " + lQueryNumber + " was not identified!!");
            }

            assert lNumberOfQueries == lNumberOfQueries2;


            print("The datfile contains " + lNumberOfQueries + " distinct queries.");
            print("Iterating all queries ..");

            QueryEnumerator queries = lMascotDatfileInf.getQueryEnumerator();
            Query lQuery = null;
            while (queries.hasMoreElements()) {
                lQuery = queries.nextElement();
                int num = lQuery.getQueryNumber();
                qtpmap.getPeptideHitOfOneQuery(num);
            }
            print("The last query is " + lQuery.getFilename());

            print("Exiting...");
        } catch (Throwable t) {
            print(t.getMessage());
            StackTraceElement[] lTraceElements = t.getStackTrace();
            for (int i = 0; i < lTraceElements.length; i++) {
                StackTraceElement lTraceElement = lTraceElements[i];
                print(lTraceElement.toString());
            }
        }

    }

    private static void print(final String aT) {
        String oldText = txtMessage.getText();
        String newText = oldText + "\n" + aT;
        txtMessage.setText(newText);
        jpanContent.validate();
        jpanContent.updateUI();
    }
}


