package com.compomics.mascotdatfile.util.gui;

import com.compomics.mascotdatfile.util.interfaces.FragmentIon;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: 19-okt-2007
 * Time: 11:03:42
 */

/**
 * Class description:
 * ------------------
 * This class was developed to display fragmentation information on the modified sequence as inspired by X!Tandem.
 */
public class SequenceFragmentationPanel extends JPanel {

    /**
     * Elementary data for composing the Panel.
     */
    private String[] iSequenceComponents;
    private Vector iFragmentIons;

    /**
     * Double array on b-ions for the sequence components.
     * If '0', no corresponding ions were given for the component.
     * Otherwise, a double between [0:1] is stored in the array that is relative with the intensity of the most intense fragmention.
     */
    private double[] bIons;

    /**
     * Double array on y-ions for the sequence components.
     * If '0', no corresponding ions were given for the component.
     * Otherwise, a double between [0:1] is stored in the array that is relative with the intensity of the most intense fragmention.
     */
    private double[] yIons;

    // GUI parameters
    private Font iBaseFont = new Font("Monospaced", Font.PLAIN, 14);
    private final double iMaxBarHeight = 40;
    private final int iBarWidth = 3;
    private final int iHorizontalSpace = 3;
    private final int iXStart = 10;

    /**
     * This boolean decides whether to markup the modified sequence in red for y-ion coverage and underline for b-ion coverage.
     */
    private boolean iBoolHighlightSequence = false;

    /**
     * This boolean holds whether or not the given sequence is a modified sequence or a normal peptide sequence.
     * Normal: KENNY
     * Modified: NH2-K<Ace>ENNY-COOH
     */
    private boolean isModifiedSequence;

    /**
     * Creates a non-modal dialog without a title with the
     * specified <code>Frame</code> as its owner.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p/>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param aModifiedSequence String with the Modified Sequence of an peptide identification.
     * @param aFragmentIons     Vector with Fragmentation objects.
     * @throws java.awt.HeadlessException if GraphicsEnvironment.isHeadless()
     *                                    returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     * @deprecated constructor with boolean on modified sequence is in favor of this constructor that always considers the sequence as a modified sequence.
     */
    public SequenceFragmentationPanel(String aModifiedSequence, Vector aFragmentIons) throws HeadlessException {
        super();
        isModifiedSequence = true;
        iSequenceComponents = parseSequenceIntoComponents(aModifiedSequence);
        iFragmentIons = aFragmentIons;
        this.normalizeMatchedIons();
        this.setMinimumSize(new Dimension(estimateWidth(), estimateHeight()));
    }

    /**
     * Creates a non-modal dialog without a title with the
     * specified <code>Frame</code> as its owner.  If <code>owner</code>
     * is <code>null</code>, a shared, hidden frame will be set as the
     * owner of the dialog.
     * <p/>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @param aSequence            String with the Modified Sequence of an peptide identification.
     * @param aFragmentIons        Vector with Fragmentation objects.
     * @param boolModifiedSequence boolean describing the sequence. This constructor can be used to enter a ModifiedSequence or a normal sequence.
     * @throws java.awt.HeadlessException if GraphicsEnvironment.isHeadless()
     *                                    returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see javax.swing.JComponent#getDefaultLocale
     */
    public SequenceFragmentationPanel(String aSequence, Vector aFragmentIons, boolean boolModifiedSequence) throws HeadlessException {
        super();
        isModifiedSequence = boolModifiedSequence;
        iSequenceComponents = parseSequenceIntoComponents(aSequence);
        iFragmentIons = aFragmentIons;
        this.normalizeMatchedIons();
        this.setPreferredSize(new Dimension(estimateWidth(), estimateHeight()));
    }

    /**
     * Paints the SequenceFragmentationPanel.
     * Based on the given ModifiedSequence Components and Fragmentions, a visualisation (inspired by X!Tandem) is drawn on a Graphics object.
     * Next to every possible fragmentation site of the peptide a bar is drawn wether b or y ions were found originating from this fragmentation side.
     *
     * @param g the specified Graphics window
     * @see java.awt.Component#update(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        // Set the base font, monospaced!
        g2.setFont(iBaseFont);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Drawing offsets.
        int yLocation = new Double(iMaxBarHeight).intValue() + iXStart;
        int xLocation = iXStart;

        int lFontHeight = g2.getFontMetrics().getHeight();
        Double lMidStringHeight = yLocation - lFontHeight * 0.2;

        for (int i = 0; i < iSequenceComponents.length; i++) {
            // reset base color to black.
            g2.setColor(Color.black);

            /**
             * A. Draw the component.
             *  --------------------
             */

            int length = iSequenceComponents.length;

            if (iBoolHighlightSequence) {
                if (i == 0) {
                    // b-ion fragment of this component found?
                    if (bIons[i] != 0) {
                        g2.setColor(Color.black);
                        g2.drawLine(xLocation - iHorizontalSpace, (yLocation + 2), xLocation + g2.getFontMetrics().stringWidth(iSequenceComponents[i]) + iHorizontalSpace, (yLocation + 2));
                    }
                } else if (i == (length - 1)) {
                    // y-ions fragment of this component found?
                    if (yIons[yIons.length - (i)] != 0) {
                        g2.setColor(Color.red);
                    }
                } else {
                    // Aha, two ions needed here.
                    // b-ion fragment of this component found?
                    if (bIons[i] != 0 && (bIons[i - 1] != 0 || bIons[i + 1] != 0)) {
                        g2.setColor(Color.black);
                        g2.drawLine(xLocation - iHorizontalSpace, (yLocation + 2), xLocation + g2.getFontMetrics().stringWidth(iSequenceComponents[i]) + iHorizontalSpace, (yLocation + 2));
                    }
                    // y-ions fragment of this component found?
                    if (yIons[yIons.length - (i)] != 0 && (yIons[yIons.length - (i + 1)] != 0 || yIons[yIons.length - (i - 1)] != 0)) {
                        g2.setColor(Color.red);
                    }
                }
            }

            // Draw this component.
            g2.drawString(iSequenceComponents[i], xLocation, yLocation);

            // Move the XLocation forwards with the component's length and the horizontal spacer..
            xLocation = xLocation + g2.getFontMetrics().stringWidth(iSequenceComponents[i]) + iHorizontalSpace;

            /**
             * B. Draw the bars.
             *  --------------------
             */


            int lBarHeight = 0;
            // bIon Bar
            if (i <= bIons.length - 1) {
                if (bIons[i] != 0) {
                    lBarHeight = (new Double(bIons[i] * iMaxBarHeight).intValue());
                    if (lBarHeight < 5) {
                        lBarHeight = 7;
                    }
                    g2.setColor(Color.BLUE);
                    g2.fill(new Rectangle(xLocation, lMidStringHeight.intValue() + 1, iBarWidth, lBarHeight));
                }
            }

            // yIon Bar
            if (i <= yIons.length - 1) {
                if (yIons[yIons.length - (i + 1)] != 0) {
                    lBarHeight = (new Double(yIons[yIons.length - (i + 1)] * iMaxBarHeight).intValue());
                    if (lBarHeight < 5) {
                        lBarHeight = 7;
                    }
                    g2.setColor(Color.RED);
                    // y bar height and y-axis start are somewhat different for yIons.
                    int yBarStart = lMidStringHeight.intValue() - 1 - lBarHeight;

                    g2.fill(new Rectangle(xLocation, yBarStart, iBarWidth, lBarHeight));
                }
            }


            // Move the XLocation forwards with the component's length and the horizontal spacer..
            xLocation = xLocation + iBarWidth + iHorizontalSpace;
        }
        this.setPreferredSize(new Dimension(xLocation, 200));

    }


    /**
     * This method can parse a modified sequence String into a String[] with different components.
     * Primitive analog to getModifiedSequenceComponents() on a peptidehit.
     *
     * @param aSequence String with the Modified sequence of a peptideHit.
     * @return the modified sequence of the peptidehit in a String[].
     *         Example:
     *         The peptide Ace-K<AceD3>ENNYR-COOH will return a String[] with
     *         [0]Ace-K<AceD3>
     *         [1]E
     *         [2]N
     *         [3]N
     *         [4]Y
     *         [5]R-COOH
     */
    private String[] parseSequenceIntoComponents(String aSequence) {
        String[] result;
        if (isModifiedSequence) {
            // Given sequence is a ModifiedSequence!
            Vector parts = new Vector();
            String temp = aSequence;
            int start = 0;
            if (temp.startsWith("#")) {
                int nterm = temp.indexOf("#", start + 1);
                start = temp.indexOf("-", nterm);
            } else {
                start = temp.indexOf("-");
            }
            start++;
            String part = temp.substring(0, start).trim();
            temp = temp.substring(start).trim();
            int endIndex = 1;
            if (temp.charAt(endIndex) == '<') {
                endIndex++;
                while (temp.charAt(endIndex) != '>') {
                    endIndex++;
                }
                endIndex++;
            }
            part += temp.substring(0, endIndex);
            temp = temp.substring(endIndex);
            parts.add(part);
            while (temp.length() > 0) {
                start = 0;
                endIndex = 1;
                if (temp.charAt(start + endIndex) == '<') {
                    endIndex++;
                    while (temp.charAt(start + endIndex) != '>') {
                        endIndex++;
                    }
                    endIndex++;
                }
                if (temp.charAt(start + endIndex) == '-') {
                    endIndex = temp.length();
                }
                part = temp.substring(0, endIndex);
                temp = temp.substring(endIndex);
                parts.add(part);
            }
            result = new String[parts.size()];
            parts.toArray(result);
        } else {
            // Given sequence is a flat sequence!
            result = new String[aSequence.length()];
            for (int i = 0; i < result.length; i++) {
                result[i] = Character.toString(aSequence.charAt(i));
            }
        }

        return result;
    }

    /**
     * Returns an estimation of the Width.
     *
     * @return
     */
    private int estimateWidth() {
        int lEstimateX = iXStart;

        for (int i = 0; i < iSequenceComponents.length; i++) {
            // Move X for a text component.
            lEstimateX = lEstimateX + this.getFontMetrics(iBaseFont).stringWidth(iSequenceComponents[i]) + iHorizontalSpace;
            // Move the XLocation forwards with the component's length and the horizontal spacer.
            lEstimateX = lEstimateX + iBarWidth + iHorizontalSpace;
        }

        lEstimateX = lEstimateX + iXStart;
        return lEstimateX;
    }

    /**
     * Returns an estimation of the height.
     *
     * @return
     */
    private int estimateHeight() {
        int lEstimateY = 0;
        lEstimateY += 2 * iXStart;
        lEstimateY += 1.8 * iMaxBarHeight;
        return lEstimateY;
    }

    /**
     * Build the normalized intensity indexes for the parts of the modified sequence that were covered by fragmentions.
     */
    private void normalizeMatchedIons() {

        int length = iSequenceComponents.length;
        // Create Y and B boolean arrays.
        bIons = new double[iSequenceComponents.length - 1];
        yIons = new double[iSequenceComponents.length - 1];
        // Dig up the most intense matched ion.
        double lMaxIntensity = 0.0;
        for (int i = 0; i < iFragmentIons.size(); i++) {
            FragmentIon lFragmentIon = (FragmentIon) iFragmentIons.elementAt(i);
            if (lMaxIntensity < lFragmentIon.getIntensity()) {
                lMaxIntensity = lFragmentIon.getIntensity();
            }
        }
        for (int i = 0; i < iFragmentIons.size(); i++) {
            FragmentIon lFragmentIon = (FragmentIon) iFragmentIons.elementAt(i);
            double lRatio = lFragmentIon.getIntensity() / lMaxIntensity;

            switch (lFragmentIon.getID()) {
                // singly charged Yion
                case FragmentIon.Y_ION:
                    // If array unit is not '0', another ion for this fragmentation site is allready found.
                    if (yIons[lFragmentIon.getNumber() - 1] != 0) {
                        // We want to save the most intense.
                        if (yIons[lFragmentIon.getNumber() - 1] > lRatio) {
                            // Reset lRatio to the most intense.
                            lRatio = yIons[lFragmentIon.getNumber() - 1];
                        }
                    }
                    yIons[lFragmentIon.getNumber() - 1] = lRatio;
                    break;
                // singly charged Bion
                case FragmentIon.B_ION:
                    if (bIons[lFragmentIon.getNumber() - 1] != 0) {
                        if (bIons[lFragmentIon.getNumber() - 1] > lRatio) {
                            lRatio = bIons[lFragmentIon.getNumber() - 1];
                        }
                    }
                    bIons[lFragmentIon.getNumber() - 1] = lRatio;
                    break;

                // double charged Yion
                case FragmentIon.Y_DOUBLE_ION:
                    if (yIons[lFragmentIon.getNumber() - 1] != 0) {
                        if (yIons[lFragmentIon.getNumber() - 1] > lRatio) {
                            lRatio = yIons[lFragmentIon.getNumber() - 1];
                        }
                    }
                    yIons[lFragmentIon.getNumber() - 1] = lRatio;
                    break;
                // double charged Bion
                case FragmentIon.B_DOUBLE_ION:
                    if (bIons[lFragmentIon.getNumber() - 1] != 0) {
                        if (bIons[lFragmentIon.getNumber() - 1] > lRatio) {
                            lRatio = bIons[lFragmentIon.getNumber() - 1];
                        }
                    }
                    bIons[lFragmentIon.getNumber() - 1] = lRatio;
                    break;
            }
        }
    }

    /**
     * Set the Sequence for the SequenceFragmentationPanel.
     *
     * @param lSequence            String with peptide sequence.
     * @param boolModifiedSequence Boolean whether lSequence is a Modified Sequence "NH2-K<Ace>ENNY-COOH" or a Flat Sequence "KENNY".
     */
    public void setSequence(String lSequence, boolean boolModifiedSequence) {
        isModifiedSequence = boolModifiedSequence;
        iSequenceComponents = parseSequenceIntoComponents(lSequence);
    }

    /**
     * Set the Vector with FragmentIons.
     * The double[] indexing b and y ion intensities will be recalculated.
     *
     * @param lFragmentions Vector
     */
    public void setFragmentions(Vector lFragmentions) {
        iFragmentIons = lFragmentions;
        normalizeMatchedIons();
    }

    /**
     * main for testing.
     *
     * @param args [0] MascotDatfile [1] PeptideHitNumber
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sequence Fragmentation display.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MascotDatfile lMascotDatfile = (MascotDatfile) MascotDatfileFactory.create(args[0], MascotDatfileType.MEMORY);
        int lPeptideHitNumber = Integer.parseInt(args[1]);
        PeptideHit lPeptideHit = lMascotDatfile.getQueryToPeptideMap().getPeptideHitOfOneQuery(lPeptideHitNumber);
        Vector lFragmentions = lPeptideHit.getPeptideHitAnnotation(lMascotDatfile.getMasses(), lMascotDatfile.getParametersSection())
                .getMatchedBYions(((Query) lMascotDatfile.getQueryList().get(lPeptideHitNumber - 1)).getPeakList());

        final SequenceFragmentationPanel jpanImage = new SequenceFragmentationPanel(lPeptideHit.getModifiedSequence(), lFragmentions, false);
        jpanImage.setBorder(BorderFactory.createEtchedBorder());

        frame.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             */
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                System.exit(0);
            }
        });

        JPanel jpanMain = new JPanel();
        jpanMain.setLayout(new BorderLayout());

        jpanMain.add(jpanImage, BorderLayout.CENTER);

        frame.getContentPane().add(jpanMain);
        frame.setLocation(100, 100);
        frame.setSize(jpanImage.getPreferredSize());
        frame.setVisible(true);
    }
}
