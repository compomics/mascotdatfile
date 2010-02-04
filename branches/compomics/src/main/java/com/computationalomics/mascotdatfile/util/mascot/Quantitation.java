package com.compomics.mascotdatfile.util.mascot;

import com.compomics.mascotdatfile.util.io.IndexElementExtension;
import com.compomics.mascotdatfile.util.io.XmlElementExtension;
import com.compomics.mascotdatfile.util.mascot.quantitation.Component;
import com.compomics.mascotdatfile.util.mascot.quantitation.Ratio;
import psidev.psi.tools.xxindex.StandardXpathAccess;
import psidev.psi.tools.xxindex.index.IndexElement;
import psidev.psi.tools.xxindex.index.StandardXpathIndex;
import psidev.psi.tools.xxindex.index.XmlXpathIndexer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Mar 16, 2009
 * Time: 4:04:03 PM
 * This class represents the Quantiation section of a MascotDatfile.
 */
public class Quantitation {
    private Component[] iComponents;
    private Ratio[] iRatios;
    private HashMap iMethod = new HashMap();
    private String iQuantitationType;


    /**
     * Construct a new Quantitation object.
     *
     * @param s The .xml content of the quantitation section.
     */
    public Quantitation(final String s) {
        try {
            File lFile = File.createTempFile("quant." + System.currentTimeMillis(), ".dat");
            FileOutputStream fos = new FileOutputStream(lFile);
            fos.write(s.getBytes());
            fos.flush();
            fos.close();
            StandardXpathAccess sxa = new StandardXpathAccess(lFile);

            InputStream is = new ByteArrayInputStream(s.getBytes());
            StandardXpathIndex sxi = XmlXpathIndexer.buildIndex(is);

            // 1.Get the components from the xml file.
            Vector<XmlElementExtension> lElementExtensions = getXmlElements(sxi, sxa, "/quantitation/method/component");
            iComponents = new Component[lElementExtensions.size()];
            for (int i = 0; i < lElementExtensions.size(); i++) {
                XmlElementExtension lComponentExtension = lElementExtensions.elementAt(i);
                String lName = lComponentExtension.getAttribute("name");
                double lAverage;
                double lMonoIsotopic;

                List<XmlElementExtension> lMoverZ = lComponentExtension.getChildByTitle("moverz");
                XmlElementExtension lMoverZExtension = lMoverZ.get(0);
                lAverage = Double.parseDouble(lMoverZExtension.getAttribute("average"));
                lMonoIsotopic = Double.parseDouble(lMoverZExtension.getAttribute("monoisotopic"));
                iComponents[i] = new Component(lName, lAverage, lMonoIsotopic);
            }

            // 2.Get the ratios from the xml file.
            lElementExtensions = getXmlElements(sxi, sxa, "/quantitation/method/report_ratio");
            iRatios = new Ratio[lElementExtensions.size()];
            for (int i = 0; i < lElementExtensions.size(); i++) {
                XmlElementExtension lRatioExtension = lElementExtensions.elementAt(i);

                // Create some local variables.
                String lNumeratorName = null;
                Double lNumeratorCoeficient = 1.0;
                Component lNumeratorComponent = null;
                String lDenominatorName = null;
                Double lDenominatorCoeficient = 1.0;
                Component lDenominatorComponent = null;

                // Now get the numerator of the Ratio.
                Vector<XmlElementExtension> lNumeratorExtension = lRatioExtension.getChildByTitle("numerator_component");
                XmlElementExtension lChild = lNumeratorExtension.elementAt(0);
                lNumeratorName = lChild.getAttribute("name");
                lNumeratorCoeficient = Double.parseDouble(lChild.getAttribute("coefficient"));

                // Now get the denominator of the Ratio.
                Vector<XmlElementExtension> lDenominatorExtension = lRatioExtension.getChildByTitle("denominator_component");
                lChild = lDenominatorExtension.elementAt(0);
                lDenominatorName = lChild.getAttribute("name");
                lDenominatorCoeficient = Double.parseDouble(lChild.getAttribute("coefficient"));

                for (int j = 0; j < iComponents.length; j++) {
                    Component lComponent = iComponents[j];

                    if (lComponent.getName().equals(lNumeratorName)) {
                        lNumeratorComponent = lComponent;
                    } else if (lComponent.getName().equals(lDenominatorName)) {
                        lDenominatorComponent = lComponent;
                    }
                }

                iRatios[i] = new Ratio(lNumeratorComponent, lDenominatorComponent, lNumeratorCoeficient, lDenominatorCoeficient);
            }

            // 3. Fill the method hashmap.
            lElementExtensions = getXmlElements(sxi, sxa, "/quantitation/method");
            XmlElementExtension method = lElementExtensions.get(0);
            iMethod = method.getAttributes();
            iQuantitationType = (String) iMethod.get("name");


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * Returns the Components used for this quantitation method.
     *
     * @return Component[]
     */
    public Component[] getComponents() {
        return iComponents;
    }

    /**
     * Returns the Ratio's used for this quantitation method.
     * A Ratio consists of two Components which it can match in a given Spectrum.
     *
     * @return Ratio[] found in the Mascot result file.
     */
    public Ratio[] getRatios() {
        return iRatios;
    }

    /**
     * Returns the QuantitationType name
     *
     * @return String
     */
    public String getQuantitationType() {
        return iQuantitationType;
    }

    /**
     * This method will get the XmlElementExtension for a specific path from the xml file.
     *
     * @param lIndex   StandardXpathIndex
     * @param lAccess  StandardXpathAccess
     * @param lXmlPath String with the xml tag name
     * @return Vector<XmlElementExtension> with the XmlElementExtensions
     * @throws IOException error reading the xml file.
     */
    public Vector<XmlElementExtension> getXmlElements(StandardXpathIndex lIndex, StandardXpathAccess lAccess, String lXmlPath) throws IOException {
        List<IndexElement> lIndexElements = lIndex.getElements(lXmlPath);
        Vector<IndexElementExtension> lRatioIndexedElements = new Vector<IndexElementExtension>();
        for (IndexElement element : lIndexElements) {
            lRatioIndexedElements.add(new IndexElementExtension(element, lXmlPath, lAccess));
        }
        //get all the information from the file
        Vector<XmlElementExtension> lElements = new Vector<XmlElementExtension>();
        for (int i = 0; i < lRatioIndexedElements.size(); i++) {
            Vector<XmlElementExtension> tse = lRatioIndexedElements.get(i).getExtendedXmlElement();
            for (int x = 0; x < tse.size(); x++) {
                lElements.add(tse.get(x));
            }
        }
        return lElements;
    }

    /**
     * Returns the Attribute value of the requested Attribute.
     *
     * @param aAttributeName
     * @return String value of the requested Attribute. Can be null if the attribute was not found.
     */
    public String getMethodAttribute(String aAttributeName) {
        return (String) iMethod.get(aAttributeName);
    }
}
