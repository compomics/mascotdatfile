package com.compomics.mascotdatfile.util.io;

import org.apache.log4j.Logger;

import psidev.psi.tools.xxindex.StandardXpathAccess;
import psidev.psi.tools.xxindex.index.IndexElement;

import java.io.IOException;
import java.util.List;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 9-dec-2008
 * Time: 20:05:16
 */

/**
 * This class holds an IndexElement, the accessor for that IndexElement and the xml title tag linked to the accassor and
 * the IndexElement
 */
public class IndexElementExtension {
    // Class specific log4j logger for IndexElementExtension instances.
    private static Logger logger = Logger.getLogger(IndexElementExtension.class);

    /**
     * The IndexElement
     */
    private IndexElement iIndexElement;
    /**
     * The accessor
     */
    private StandardXpathAccess iAccessor;
    /**
     * The title for the IndexElement
     */
    private String iTitlePath;

    /**
     * Constructor
     *
     * @param aElement
     * @param aPath
     * @param aAccessor
     */
    public IndexElementExtension(IndexElement aElement, String aPath, StandardXpathAccess aAccessor) {
        this.iIndexElement = aElement;
        this.iTitlePath = aPath;
        this.iAccessor = aAccessor;
    }

    /**
     * Getter for the XmlElementExtensions linked to the accessor and IndexElement
     *
     * @return the XmlElementExtensions linked to the accessor and IndexElement
     * @throws java.io.IOException
     */
    public Vector<XmlElementExtension> getExtendedXmlElement() throws IOException {
        Vector<XmlElementExtension> lElements = new Vector<XmlElementExtension>();

        List<String> lSnippet = iAccessor.getXmlSnippets(iTitlePath, iIndexElement.getStart(), iIndexElement.getStop());
        for (int i = 0; i < lSnippet.size(); i++) {
            XmlElementExtension axt = new XmlElementExtension(lSnippet.get(i), this, false);
            lElements.add(axt);
        }
        return lElements;
    }
}
