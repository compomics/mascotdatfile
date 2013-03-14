/*
 * Created by IntelliJ IDEA.
 * User: Lennart
 * Date: 29-jul-02
 * Time: 16:16:31
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.compomics.mascotdatfile.util.mascot.parser;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/*
 * CVS information:
 *
 * $Revision: 1.5 $
 * $Date: 2009/03/19 13:10:16 $
 */

/**
 * This class provides a parser for parsing a raw results file of Mascot.
 */
public class MascotRawParser {
    // Class specific log4j logger for MascotRawParser instances.
    private static Logger logger = Logger.getLogger(MascotRawParser.class);

    private static String iSeparatorA = "=";
    private static String iSeparatorB = ":";

    /**
     * This HashMap will hold each section in the Mascot raw results file as key-value pairs, where the key is the name
     * of the section and the value a HashMap with all the entries in that section as key-value pairs.
     */
    private HashMap allSections = new HashMap(400);

    /**
     * This variable will hold the number of queries contained in the results after the first call to
     * 'getNumberOfQueries()'. <br /> <b>Please note</b> that you should never access this variable directly since it is
     * lazily cached!!!
     */
    private int numQueries = -1;

    /**
     * This method allows the caller to add a section to this wrapper.
     *
     * @param aKey     String with the name of the section.
     * @param aSection HashMap with the contents of the section as key-value pairs.
     */
    public void addSection(String aKey, HashMap aSection) {
        allSections.put(aKey, aSection);
    }

    /**
     * This method allows the caller to retrieve a section as a HashMap by providing the name of that section.
     *
     * @param aKey String with the name for the section.
     * @return HashMap with the contents of the requested section as key-value pairs, or 'null' if the section has not
     *         been found.
     */
    public HashMap getSection(String aKey) {
        return (HashMap) allSections.get(aKey);
    }

    /**
     * This method returns a String representation of the data wrapped. <br /> <b>Please note</b> that this is not the
     * same as the original format of the file from which the data came!!!
     *
     * @return String   with the String representation of this wrapper.
     */
    public String toString() {
        StringBuffer lsb = new StringBuffer("Hi, I'm currently holding the following sections:\n");
        Iterator i = allSections.keySet().iterator();
        while (i.hasNext()) {
            lsb.append("Section : '" + i.next() + "'.\n");
        }
        return lsb.toString();
    }

    /**
     * This method reports on the number of queries contained in this wrapper.
     *
     * @return int with the number of queries.
     */
    public int getNumberOfQueries() {
        if (this.numQueries < 0) {
            this.numQueries = Integer.parseInt((String) ((HashMap) allSections.get("header")).get("queries"));
        }
        return this.numQueries;
    }


    /**
     * This method is useful for parsing a result file stored locally.
     *
     * @param aFile File pointing to the raw Mascot result file.
     * @throws java.io.IOException if reading the file fails.
     */
    public MascotRawParser(File aFile) throws IOException {
        BufferedReader lbr = new BufferedReader(new InputStreamReader(new FileInputStream(aFile)));
        this.parseFromReader(lbr);
        lbr.close();
    }

    /**
     * This method is useful for parsing a result file stored as a String in memory.
     *
     * @param aString String with the contents of the raw Mascot result file.
     */
    public MascotRawParser(String aString) {
        try {
            BufferedReader lbr = new BufferedReader(new StringReader(aString));
            this.parseFromReader(lbr);
            lbr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * This method is useful for parsing a result file from a Reader.
     *
     * @param abr BufferedReader, connected to the contents of the raw Mascot result file.
     * @throws java.io.IOException if reading the file fails.
     */
    public MascotRawParser(BufferedReader abr) throws IOException {
        this.parseFromReader(abr);
    }

    /**
     * This method is useful for parsing a result file from a Reader.
     *
     * @param abr BufferedReader, connected to the contents of the raw Mascot result file.
     * @throws java.io.IOException if reading the file fails.
     */
    private void parseFromReader(BufferedReader abr) throws IOException {
        // Parse!
        String line = null;
        if (abr != null) {
            // First line is to be ignored.
            line = abr.readLine();
            // Find the boundary.
            line = abr.readLine();
            while (line != null && line.indexOf("boundary") < 0) {
                line = abr.readLine();
            }
            // If the line is 'null' here, we read the entire datfile without encountering a
            // boundary.
            if (line == null) {
                throw new IllegalArgumentException("Did not find 'boundary' definition in the datfile!");
            }
            String boundary = this.getBoundary(line);
            Vector sections = new Vector(400, 20);
            // Find next boundary-demarcated section.

            // Cycle the stream.
            boolean lbInSection = false;
            InnerSectionWrapper isw = null;
            StringBuffer lsb = null;
            while ((line = abr.readLine()) != null) {

                if (line.indexOf(boundary) >= 0) {

                    if (lbInSection) {
                        isw.setContent(lsb.toString());
                        sections.addElement(isw);
                    }

                    // Check for endmarker.
                    if (line.endsWith(boundary + "--")) {
                        break;
                    }

                    lbInSection = true;
                    isw = new InnerSectionWrapper(getSectionName(abr.readLine()));
                    lsb = new StringBuffer();
                } else {
                    if (lbInSection) {
                        if (!line.trim().equals("")) {
                            lsb.append(line + "\n");
                        }
                    }
                }
            }

            // Read the whole thing, which is now stored in the Vector.
            // Parse each element in the Vector next.
            int liSize = sections.size();
            for (int i = 0; i < liSize; i++) {
                InnerSectionWrapper lsw = (InnerSectionWrapper) sections.elementAt(i);
                HashMap lhmToPut = null;
                // Index section is skipped. It has no meaning for us.
                if (!("index".equalsIgnoreCase(lsw.getName()))) {
                    lhmToPut = processSectionToHashMap(lsw.getContent());
                }
                //HashMap lhmToPut = parseSection(lsw.getName(), lsw.getContent());
                // We will refrain from putting junk into the HM.
                if (lhmToPut != null) {
                    this.addSection(lsw.getName(), lhmToPut);
                }
            }
        }
    }

    public void clear() {
        if (allSections != null) {
            allSections.clear();
        }
    }

    /**
     * This method parses the content of a section into key-value pairs and stores these in a HashMap.
     *
     * @param aContent the content of the section to be parsed.
     * @return HashMap with the contents of the section as key-value pairs.
     */
    private HashMap processSectionToHashMap(String aContent) {
        HashMap lhmResult = new HashMap();
        try {
            BufferedReader lbr = new BufferedReader(new StringReader(aContent));
            String line = null;
            boolean firstLine = true;
            while ((line = lbr.readLine()) != null) {
                // KEY=VALUE
                // If this section has XML content (such as the modification / quantitation sections as of Mascot 2.2)
                // The xml section is stored as a whole in the map for the key 'XML'.
                if (firstLine) {
                    if (line.startsWith("<?xml version")) {
                        // We match a xml section.
                        lhmResult.put("XML", aContent); // Now break the while loop.//
                        break;
                    }
                    firstLine = false;
                }


                // More, for header lines ('hx_text'; holding the protein description)
                // or Protein section names (starting with the accession, encircled by '"')
                // we need to make sure we have the full description line (it might
                // contain an '=', as IPI headers often do)!
                Object key = null;
                Object value = null;
                if ((line.startsWith("h") && line.indexOf("_text=") >= 0) || (line.startsWith("\"") && line.endsWith("\""))) {
                    key = line.substring(0, line.indexOf("=")).trim();
                    if (line.length() > line.indexOf("=") + 1) {
                        value = line.substring(line.indexOf("=") + 1).trim();
                    }
                } else {
                    // Normal key/value row.
                    // first option is '=' sign.
                    String sep = analyseSeparator(line);
                    if (sep != null) {
                        StringTokenizer lst = new StringTokenizer(line, sep);
                        key = lst.nextToken().trim();
                        value = null;
                        if (lst.hasMoreTokens()) {
                            value = lst.nextToken().trim();
                        }
                    }
                }
                lhmResult.put(key, value);
            }
            lbr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return lhmResult;
    }

    /**
     * This method parses a section definition line for the name of that section.
     *
     * @param sectionDefLine String with the section definition line.
     * @return String  with the section name.
     */
    private String getSectionName(String sectionDefLine) {
        return this.getProp(sectionDefLine, "name");
    }

    /**
     * This method returns the required separator for the line tokenizer.
     *
     * @param aLine
     * @return the required separator for the line tokenizer
     */
    private String analyseSeparator(String aLine) {
        if (aLine.indexOf(iSeparatorA) > 0) {
            return iSeparatorA;
        } else if (aLine.indexOf(iSeparatorB) > 0) {
            return iSeparatorB;
        }
        return null;
    }

    /**
     * This method parses the boundary definition line for the boundary String.
     *
     * @param boundaryDefLine String with the boundary definition line.
     * @return String with the boundary.
     */
    private String getBoundary(String boundaryDefLine) {
        String lookFor = "boundary";
        String found = this.getProp(boundaryDefLine, lookFor);
        return found;
    }

    /**
     * This method finds a property, associated by a name in the following context: <br /> NAME=VALUE
     *
     * @param line     String with the line on which the 'KEY=VALUE' pair is to be found.
     * @param propName String with the name of the KEY.
     * @return String  with the VALUE
     */
    private String getProp(String line, String propName) {
        propName += "=";
        int start = line.indexOf(propName);
        int offset = propName.length();
        //System.out.println("---" + line);
        String found = line.substring(start + offset).trim();
        // Trim away opening and closing '"'.
        if (found.startsWith("\"")) {
            found = found.substring(1);
        }
        if (found.endsWith("\"")) {
            found = found.substring(0, found.length() - 1);
        }
        return found.trim();
    }

    /**
     * A simple wrapper for a section, consisting of a name and associated content.
     */
    private class InnerSectionWrapper {
        /**
         * The name for the section.
         */
        private String name = null;
        /**
         * The content of the section.
         */
        private String content = null;

        /**
         * Empty constructor.
         */
        public InnerSectionWrapper() {
            this(null, null);
        }

        /**
         * Constructor which sets the name.
         *
         * @param aName String with a name for the section.
         */
        public InnerSectionWrapper(String aName) {
            this(aName, null);
        }

        /**
         * Constructor which sets the name and the content.
         *
         * @param aName    String with a name for the section.
         * @param aContent String with the content for the section.
         */
        public InnerSectionWrapper(String aName, String aContent) {
            this.name = aName;
            this.content = aContent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
