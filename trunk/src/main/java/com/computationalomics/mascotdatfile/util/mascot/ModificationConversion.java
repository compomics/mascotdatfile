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
package com.computationalomics.mascotdatfile.util.mascot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Singleton Class, only one instance of this Class is made.
 * The Class holds itself and a Static HashMap.
 * You can always get the hashmap by the static getConversionMap() method.
 * <p/>
 * Parses modificationConversion.txt (must be in classpath! 'src/conf/modificationConversion.txt')
 * Key:     fullname    Acetyl_heavy (N-term)
 * Value:   shortname   AcD3
 */
public class ModificationConversion {
    private static HashMap iConversionMap;

    static {
        iConversionMap = new HashMap();
        try {
            // Conversion file must be in classpath!!!
            BufferedReader lBuf = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("modificationConversion.txt")));

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

                if(lValue.indexOf(lIllegalChar) != -1){
                    illegalShortName(lKey, lValue, lIllegalChar);
                }
                lIllegalChar = '#';
                if(lValue.indexOf(lIllegalChar) != -1){
                    illegalShortName(lKey, lValue, lIllegalChar);
                }

                iConversionMap.put(lKey, lValue);
            }
            lBuf.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load file for modification conversion!\n" +
                "The file 'modificationConversion.txt' should be present in your classpath!");
        }
    }

    private static void illegalShortName(String aKey, String aValue, char aIllegalCharacter) throws RuntimeException{
        throw new RuntimeException("Illegal character ' " + aIllegalCharacter + "' used in \"" + aValue + "\" for Mascot modification \"" + aKey + "\".\nPlease remove all illegal charaters ('" + aIllegalCharacter + "') in the short names from ModificationCoverion.txt");
    }

    /**
     * Do not instantiate ModificationConversion.
     */
    private ModificationConversion() {
    }

    /**
     * Getter for property 'conversionMap'.
     *
     * @return Value for property 'conversionMap'.
     */
    public static HashMap getConversionMap() {
        return iConversionMap;
    }

    /**
     * This method returns the short-type notation of a modification name as found in the conversion map.
     *
     * @param aType Long name of the modification.
     * @return String Short name of the modification.
     */
    public static String getShortType(String aType) {
        String result = (String) iConversionMap.get(aType);
        if (result == null) {
            result = "#" + aType + "#";
        }
        return result;
    }
}
