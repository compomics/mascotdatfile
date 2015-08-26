package com.compomics.mascotdatfile.util.io;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Niklaas Colaert
 * Date: 9-dec-2008
 * Time: 16:48:03
 */

/**
 * This class extends the XmlElement. This class parses the xml element and can hold XmlElement attribute information,
 * title, value and children of the XmlElements
 */
public class XmlElementExtension {
    // Class specific log4j logger for XmlElementExtension instances.
    private static Logger logger = Logger.getLogger(XmlElementExtension.class);

    /**
     * Value of this XmlElementExtension
     */
    public String iValue;
    /**
     * Vector with the children of this XmlElementExtension
     */
    public Vector<XmlElementExtension> iChildren = new Vector<XmlElementExtension>();
    /**
     * HashMap with the attributes of this XmlElementExtension
     */
    public HashMap iAttributes = new HashMap();
    /**
     * Title of this XmlElementExtension
     */
    public String iTitle;
    /**
     * IndexElementExtension of this XmlElementExtension
     */
    public IndexElementExtension iIndexElementExtension;
    /**
     * boolean that states if this XmlElementExtension is a child of another XmlElementExtension
     */
    private boolean iChild;

    private boolean iParsed;

    private String iXmlString;

    /**
     * The constructor
     *
     * @param aXmlString             String with the whole xml tag
     * @param aIndexElementExtension The IndexElementExtensio of the XmlElementExtension
     * @param aChild                 Boolean that states if this XmlElementExtension is a child of another
     *                               XmlElementExtension
     */
    public XmlElementExtension(String aXmlString, IndexElementExtension aIndexElementExtension, boolean aChild) {
        this.iIndexElementExtension = aIndexElementExtension;
        this.iChild = aChild;
        if (aChild) {
            iParsed = false;
            iXmlString = aXmlString;
        } else {
            this.parse(aXmlString);
            iParsed = true;
        }
    }


    /**
     * This method will parse the xml string.
     *
     * @param lXmlString the XML string
     */
    public void parse(String lXmlString) {
        //split the xml tag in different lines
        StringTokenizer lTok = new StringTokenizer(lXmlString, "\n");

        int lLineCounter = 0;
        int lLineNumber = lTok.countTokens();
        String lChildrenString = "";
        String lChildrenStopper = "";
        boolean lChildDetected = false;

        while (lTok.hasMoreElements()) {
            //read line by line
            String lLine = (String) lTok.nextElement();
            lLine = lLine.trim();
            if (lLineCounter == 0) {
                //first line, get the title and the attributes
                boolean lTitleSetting = true;
                boolean lInAttributeKey = false;
                boolean lInAttributeValue = false;
                iTitle = "";
                String key = "";
                String value = "";
                for (int i = 1; i < lLine.length(); i++) {
                    if (lTitleSetting) {
                        //create the title
                        if (lLine.charAt(i) == ' ' || lLine.charAt(i) == '>') {
                            //end of the title
                            lTitleSetting = false;
                            lInAttributeKey = true;
                        } else {
                            //it's not yet the title end, append the title with one char
                            iTitle = iTitle + lLine.charAt(i);
                        }
                    } else {
                        //title is set, look for attributes
                        if (lInAttributeKey && lLine.charAt(i) != ' ') {
                            if (lLine.charAt(i) == '=') {
                                //do nothing
                            } else if (lLine.charAt(i) == '\"') {
                                //end of this attribute
                                lInAttributeKey = false;
                                lInAttributeValue = true;
                            } else {
                                //it's not yet the end of the attribute, append the attribute with one char
                                key = key + lLine.charAt(i);
                            }
                        } else if (lInAttributeValue) {
                            //attribute value is set
                            if (lLine.charAt(i) != '\"') {
                                value = value + lLine.charAt(i);
                            } else {
                                //add it to the attribute HashMap
                                lInAttributeValue = false;
                                lInAttributeKey = true;
                                iAttributes.put(key, value);
                                key = "";
                                value = "";
                            }
                        }
                    }
                }
            } else if (lLineCounter + 1 == lLineNumber) {
                //last line closing xml tag
                // do nothing
            } else if (lLine.startsWith("<")) {
                //This is not the first line but we fine a "<" at the start => child detected
                if (lChildDetected) {
                    //we have a child of more than one line
                    if (lLine.indexOf(lChildrenStopper) > -1) {
                        //stop child
                        iChildren.add(new XmlElementExtension(lChildrenString + "\r\n     " + lLine, iIndexElementExtension, true));
                        lChildDetected = false;
                    } else {
                        //end of the child not yet detected , append the lChildrenString
                        lChildrenString = lChildrenString + "\r\n     " + lLine;
                    }

                } else {
                    int lStart = lLine.indexOf("<") + 1;
                    int lEnd = lLine.indexOf(" ", lStart);
                    if (lEnd == -1) {
                        // No attributes within, use the '>' from the opening tag to extract the child name.
                        lEnd = lLine.indexOf(">", lStart);

                    }
                    String lChildName = lLine.substring(lStart, lEnd);

                    if (lLine.endsWith("/>") || lLine.endsWith("/" + lChildName + ">")) {
                        // a child without childs is detected
                        // add it to this XmlElementExtension
                        iChildren.add(new XmlElementExtension(lLine, iIndexElementExtension, true));
                    } else {
                        // a child with childs (different lines) is detected
                        lChildDetected = true;
                        lChildrenString = lLine;
                        //find where this lines stops
                        lChildrenStopper = "</";
                        lChildrenStopper = lChildrenStopper + lChildName;
                    }
                }

            } else {
                //line is the value of this xml tag
                iValue = lLine;
            }
            lLineCounter = lLineCounter + 1;
        }
    }

    /**
     * Get the value for the xml element
     *
     * @return String with the value for the xml element
     */
    public String getValue() {
        if (!iParsed) {
            this.parse(iXmlString);
            iParsed = true;
            iXmlString = null;
        }
        return iValue;
    }

    /**
     * Get all the children of the XmlElementExtension
     *
     * @return the children of the XmlElementExtension
     */
    public Vector<XmlElementExtension> getChildren() {
        if (!iParsed) {
            this.parse(iXmlString);
            iParsed = true;
            iXmlString = null;
        }
        return iChildren;
    }

    /**
     * This method gives the value for an attribute
     *
     * @param aKey Key for the attribute
     * @return String with the value for the given key
     */
    public String getAttribute(String aKey) {
        if (!iParsed) {
            this.parse(iXmlString);
            iParsed = true;
            iXmlString = null;
        }
        return (String) iAttributes.get(aKey);
    }

    /**
     * This method gives an XmlElementExtension that is a child with a specific title
     *
     * @param aTitle Title of the child (first/second/third) If the title of this child is "first", the children of this
     *               child will be asked with title "second/third"
     * @return Vector with the asked children
     */
    public Vector<XmlElementExtension> getChildByTitle(String aTitle) {
        if (!iParsed) {
            this.parse(iXmlString);
            iParsed = true;
            iXmlString = null;
        }
        String lTitleToFind;
        boolean lFindGrandChildren;
        if (!aTitle.contains("/")) {
            lTitleToFind = aTitle;
            lFindGrandChildren = false;
        } else {
            lTitleToFind = aTitle.substring(0, aTitle.indexOf("/"));
            lFindGrandChildren = true;
        }
        Vector<XmlElementExtension> lChilds = new Vector<XmlElementExtension>();
        for (int i = 0; i < iChildren.size(); i++) {
            if (iChildren.get(i).getTitle().equalsIgnoreCase(lTitleToFind)) {
                if (lFindGrandChildren) {
                    //look at the childrens children
                    Vector<XmlElementExtension> lGrandChildren = iChildren.get(i).getChildByTitle(aTitle.substring(aTitle.indexOf("/") + 1));
                    for (int j = 0; j < lGrandChildren.size(); j++) {
                        lChilds.add(lGrandChildren.get(j));
                    }
                } else {
                    lChilds.add(iChildren.get(i));
                }
            }
        }
        return lChilds;
    }

    /**
     * This string gives the title of this XmlElementExtension
     *
     * @return String Title
     */
    public String getTitle() {
        if (!iParsed) {
            this.parse(iXmlString);
            iParsed = true;
            iXmlString = null;
        }
        return iTitle;
    }

    /**
     * This getter gives the IndexElementExtension for this XmlElementExtension
     *
     * @return IndexElementExtension
     */
    public IndexElementExtension getIndexElement() {
        return iIndexElementExtension;
    }

    /**
     * This getter gives the Keys of the Attribues
     *
     * @return the Keys of the Attribues
     */
    public HashMap getAttributes() {
        return iAttributes;
    }
}
