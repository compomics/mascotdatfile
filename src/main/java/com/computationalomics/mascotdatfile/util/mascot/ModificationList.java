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

package com.computationalomics.mascotdatfile.util.mascot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 26-feb-2006
 * Time: 13:55:30
 * This class holds 2 vectors,
 * 1 with fixed modifications
 * 1 with variable modifications.
 */
public class ModificationList  implements Serializable {
    /**
     * This Vector holds the different Fixed Modificatins used in this Mascot search.
     */
    private Vector iFixedModifications = new Vector(0, 1);
    /**
     * This Vector holds the different Variable Modificatins used in this Mascot search.
     */
    private Vector iVariableModifications = new Vector(0, 1);

    /**
     * This Constructor recieves the modifications in String Format and will transform them into different objects.
     *
     * @param aFModStringArrayList FixedModifications ArrayList
     * @param aVModStringArrayList VariableModifications ArrayList
     */
    public ModificationList(ArrayList aFModStringArrayList, ArrayList aVModStringArrayList) {
        generateVModVector(aVModStringArrayList);
        generateFModVector(aFModStringArrayList);
    }

    /**
     * This Constructor recieves the modifications in String Format and will transform them into different objects.
     *
     * @param aFModStringArrayList FixedModifications ArrayList
     * @param aFixedModifications_ParameterSection
     *                             String with the fixed modifications as read from the parameters-section. They will not contain mass data!! This is only functional to create a complete modifiedSequence in older datfiles.
     */
    public ModificationList(ArrayList aFModStringArrayList, ArrayList aVModStringArrayList, String aFixedModifications_ParameterSection) {
        generateVModVector(aVModStringArrayList);
        if (aFixedModifications_ParameterSection != null) {
            generateFModVector(aFixedModifications_ParameterSection);
        }
    }

    /**
     * Create a Vector containing all the variable modification instances.
     */
    private void generateVModVector(ArrayList aVModStringArrayList) {
        StringTokenizer st = null;
        String lStringV = null;
        // Run every element in the aVModStringArrayList trough a for loop.
        // Create a VariableModification instance at the end of each loop.
        // Put the instance into the vector.
        for (int i = 0; i < aVModStringArrayList.size(); i++) {
            // Save the value of the StringArrayList in this variable and refresh each time it gets trough the loop.
            lStringV = (String) aVModStringArrayList.get(i);
            st = new StringTokenizer(lStringV, ",");
            VariableModification lV = null;

            // 1. First element in the String is the mass.
            double lMass = Double.parseDouble(st.nextToken());

            // 2. Second element in the String is the type AND location.
            String lTypeAndLocation = st.nextToken().trim();
            // 2.a)
            //   Use this token as the key to get the shortend notation of the modification.
            String lShortType = getShortType(lTypeAndLocation);

            // 2.b)
            //   throw lTypeAndLocation to parseVariableModName method and
            //   get a String[] in return containing the type @[0] and the location @[1]
            String[] lNameArray = parseVariableModName(lTypeAndLocation);
            String lType = lNameArray[0].trim();
            String lLocation = lNameArray[1];
            if(lLocation != null) {
                lLocation = lLocation.trim();
            }

            // 3. Third element in the StringTokenizer is the (possible) Neutralloss.
            double lNeutralLoss = Double.parseDouble(st.nextToken());

            // 4. The ModificationID runs parrallel with the for-loop.
            int lModificationID = i + 1;

            // 5. Finally create a new Variable modification instance.
            lV = new VariableModification(lType, lShortType, lMass, lNeutralLoss, lLocation, lModificationID);

            // 6. add the instance to this this class its VariableModification Vector.
            iVariableModifications.add(lV);
        }
    }

    /**
     * Create a Vector containing all the variable modification instances.
     */
    private void generateFModVector(ArrayList aFModStringArrayList) {
        StringTokenizer st = null;
        String lStringF = null;
        // Run every element in the aFModStringArrayList trough a for loop.
        // Create a FixedModification instance at the end of each loop.
        // Put the instance into the vector.
        for (int i = 0; i < aFModStringArrayList.size(); i++) {
            // Save the value of the StringArrayList in this variable and refresh each time it gets trough the loop.
            lStringF = (String) aFModStringArrayList.get(i);
            st = new StringTokenizer(lStringF, ",");
            FixedModification lF = null;

            // 1. First element in the StringTokenizer is the mass.
            double lMass = Double.parseDouble(st.nextToken());

            // 2. Second element in the StringTokenizer is the type and location.
            String lTypeAndLocation = st.nextToken().trim();
            //    We only need the type because the location is allready in the next token.
            String lType = parseFixedModName(lTypeAndLocation);
            //    The type is also used to get the ShortType notation.
            String lShortType = getShortType(lTypeAndLocation);

            // 3. Third element in the StringTokenizer is the location.
            String lLocation = st.nextToken();

            // 4. The ModificationID runs parrallel with the for-loop.
            int lModificationID = i + 1;

            // 5. Finally create a new Fixed modification instance.
            lF = new FixedModification(lType, lShortType, lMass, lLocation, lModificationID);

            // 6. add the instance to this this class its VariableModification Vector.
            iFixedModifications.add(lF);
        }
    }

    /**
     * Create a vector with fixed modifications, mind they will only contain the modification type and location!
     *
     * @param aFixedModifications_ParameterSection
     *
     */
    private void generateFModVector(String aFixedModifications_ParameterSection) {
        //Acetyl (K),Carbamidomethyl (C)
        FixedModification lF;
        ArrayList lFixedMods = new ArrayList();
        StringTokenizer st = new StringTokenizer(aFixedModifications_ParameterSection, ",");
        while (st.hasMoreTokens()) {
            lFixedMods.add(st.nextToken());
        }
        for (int i = 0; i < lFixedMods.size(); i++) {
            String lStringF = (String) lFixedMods.get(i);
            String lType = lStringF.substring(0, lStringF.indexOf(' '));
            String lLocation = lStringF.substring(lStringF.indexOf('(') + 1, lStringF.indexOf(')'));
            String lShortType = getShortType(lStringF);
            int lModificationID = i + 1;

            lF = new FixedModification(lType, lShortType, lLocation, lModificationID);
            iFixedModifications.add(lF);
        }
    }

    /**
     * Returns this Vector holds the different FixedModificatins used in this Mascot search.
     *
     * @return this Vector holds the different FixedModificatins used in this Mascot search.
     */
    public Vector getFixedModifications() {
        return iFixedModifications;
    }

    /**
     * Returns this Vector holds the different Variable Modificatins used in this Mascot search.
     *
     * @return this Vector holds the different Variable Modificatins used in this Mascot search.
     */
    public Vector getVariableModifications() {
        return iVariableModifications;
    }

    public VariableModification getVariableModificationByModificationID(int aModificationID) {
        int n = 0;
        VariableModification m = null;
        for (int i = 0; i < iVariableModifications.size(); i++) {
            m = (VariableModification) iVariableModifications.get(i);
            if (m.getModificationID() == aModificationID) {
                n = i;
                break;
            }
        }
        return m;
    }

    /**
     * method
     * Use the static TestModificationConversion instance HashMap.
     *
     * @param aKey String    Modification Type.
     * @return ShortType   String    Modification ShortType.
     */
    private String getShortType(String aKey) {
        return ModificationConversion.getShortType(aKey);
    }

    /**
     * Method
     * Parse the name string of a variable modidication into type of modifiction(ex: 'Acetyl') and the location(ex:'N-term')
     *
     * @param aName String with modificationType and modificationLocation.
     * @return String[] with the parsed modificationType and modificationLocation.
     *         input example: 'Pyro-cmC (N-term camC)'
     *         returns [0]='Pyro-cmC'  [1]='N-term camC'
     */
    private String[] parseVariableModName(String aName) {
        String[] lNameArray = new String[2];
        int lBeginIndex = aName.lastIndexOf('(');
        if(lBeginIndex > 0) {
            int lEndIndex = aName.lastIndexOf(')');
            lNameArray[0] = aName.substring(0, (lBeginIndex));
            lNameArray[1] = aName.substring(lBeginIndex + 1, lEndIndex);
        } else {
            lNameArray[0] = aName;
            lNameArray[1] = null;
        }
        return lNameArray;
    }

    /**
     * Method
     * Parse the name string of a fixed modidication into type of modifiction(ex: 'Acetyl_heavy').
     *
     * @param aName String with modificationType( and modificationLocation).
     * @return String with the parsed modificationType.
     *         input example: 'Arg 6xC(13) (R)'
     *         returns 'Arg 6xC(13)'
     */
    private String parseFixedModName(String aName) {
        aName = aName.trim();                               //Cut of the leading whitespace.
        int lEndIndex = aName.lastIndexOf('(') - 1;         //Find the last ')' bracket; minus 1 is the end of the Mod type.
		if (lEndIndex >= 0) {
			aName = aName.substring(0, lEndIndex);
		}
		return aName;
    }
}
