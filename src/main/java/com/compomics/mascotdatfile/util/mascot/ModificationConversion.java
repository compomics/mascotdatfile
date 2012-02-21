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

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 2-mrt-2006
 * Time: 10:43:47
 */
package com.compomics.mascotdatfile.util.mascot;

import com.compomics.mascotdatfile.util.exception.MascotDatfileException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Singleton Class, only one instance of this Class is made. The Class holds itself and a Static HashMap. You can always
 * get the hashmap by the static getConversionMap() method.
 * <p/>
 * Parses modificationConversion.txt (must be in classpath! 'src/conf/modificationConversion.txt') Key:     fullname
 * Acetyl_heavy (N-term) Value:   shortname   AcD3
 */
public class ModificationConversion {
    // Class specific log4j logger for ModificationConversion instances.
    private static Logger logger = Logger.getLogger(ModificationConversion.class);
// ------------------------------ FIELDS ------------------------------

    private static ModificationConversion singleton = null;
    private HashMap iConversionMap;

// -------------------------- STATIC METHODS --------------------------

    /**
     * This method returns the short-type notation of a modification name as found in the conversion map.
     *
     * @param aType Long name of the modification.
     * @return String Short name of the modification.
     */
    public static String getShortType(String aType) {
        String result = (String) getInstance().getConversionMap().get(aType);
        if (result == null) {
            result = "#" + aType + "#";
        }
        return result;
    }

    /**
     * Get the singleton instance of the modificationConversion instance.
     *
     * @return
     */
    public static ModificationConversion getInstance() {
        if (singleton == null) {
            singleton = new ModificationConversion();
        }
        return singleton;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Do not instantiate ModificationConversion.
     */
    private ModificationConversion() {
        // empty constructor.
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Getter for property 'conversionMap'.
     *
     * @return Value for property 'conversionMap'.
     */
    public HashMap getConversionMap() {
        if (iConversionMap == null) {
            initModificationConversionMap();
        }
        return iConversionMap;
    }

    /**
     * Initiate the modificationconversion.txt file.
     */
    private void initModificationConversionMap() {
        iConversionMap = new HashMap();
        try {
            BufferedReader lBuf = null;

            // First, try to find the modificationconversion file in the "resources" jar launcher folder.
            String path = "" + this.getClass().getProtectionDomain().getCodeSource().getLocation();
            path = path.substring(5, path.lastIndexOf("/"));
            if (path.endsWith("/lib")) {
                path = path.substring(0, path.length() - 4);
            }
            path = path + "/resources/modificationConversion.txt";
            path = path.replace("%20", " ");

            File lFile = new File(path);
            if (lFile.exists()) {
                lBuf = new BufferedReader(new InputStreamReader(new FileInputStream(lFile)));
                logger.debug("Using modificationConversion from disk (/resources/modificationConversion.txt)");
            } else {
                // Second, if not found - try to find the file in the classpath.
            	InputStream isRessource = ClassLoader.getSystemResourceAsStream("modificationConversion.txt");
                //InputStreamReader lReader = new InputStreamReader(ClassLoader.getSystemResourceAsStream("modificationConversion.txt"));

                if (isRessource == null) {
                   // lReader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("modificationConversion.txt"));
                	 isRessource = this.getClass().getClassLoader().getResourceAsStream("modificationConversion.txt");
                }

                lBuf = new BufferedReader(new InputStreamReader(isRessource));
            }


            if (lBuf == null) {
                // BufferedReader has not been initialized, quit!
                throw new FileNotFoundException("Unable to load modifiactionConversion.txt file from the /resources/ directory or from the classpath!!");
            } else {
                String line = null;
                while ((line = lBuf.readLine()) != null) {
                    // Skip comments and empty lines.
                    if (line.trim().startsWith("#") || line.trim().equals("")) {
                        continue;
                    }
                    StringTokenizer lst = new StringTokenizer(line, "=");
                    String lKey = lst.nextToken().trim();
                    String lValue = lst.nextToken().trim();

                    // Check for illegal characters in the short name.
                    char lIllegalChar = '-';

                    if (lValue.indexOf(lIllegalChar) != -1) {
                        illegalShortName(lKey, lValue, lIllegalChar);
                    }
                    lIllegalChar = '#';
                    if (lValue.indexOf(lIllegalChar) != -1) {
                        illegalShortName(lKey, lValue, lIllegalChar);
                    }

                    iConversionMap.put(lKey, lValue);
                }
                lBuf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MascotDatfileException("Unable to load file for modification conversion!\n" +
                    "The file 'modificationConversion.txt' should be present in your classpath!");
        }
    }

    /**
     * Set the ConversionMap for key-value pairs for long and short peptide modification names.
     * e.g.
     * Key:Acetyl (K)
     * Value:Ace
     *
     * Note that the long peptide modification name should correspond to the modifcation name used on the Mascot server!!
     * @param iConversionMap HashMap with key-value pairs for long and short peptide modification names.
     */
    public void setModificationConversionMap(HashMap iConversionMap) {
        this.iConversionMap = iConversionMap;
    }

    private static void illegalShortName(String aKey, String aValue, char aIllegalCharacter) throws RuntimeException {
        throw new MascotDatfileException("Illegal character ' " + aIllegalCharacter + "' used in \"" + aValue + "\" for Mascot modification \"" + aKey + "\".\nPlease remove all illegal charaters ('" + aIllegalCharacter + "') in the short names from ModificationCoverion.txt");
    }
}

