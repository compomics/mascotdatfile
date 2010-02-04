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
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 24-feb-2006
 * Time: 9:05:56
 */

/**
 * This class contains all the parsed data from the 'masses' section of the datfile.
 */
public class Masses implements Serializable {
    /**
     * This HashMap holds all the masses that are written in the datfile Masses section.
     */
    HashMap iMasses = new HashMap();
    /**
     * Used Fixed ModificationList in an ArrayList.
     */
    private ArrayList iFixedModifications = null;
    /**
     * Used Variable ModificationList in an ArrayList.
     */
    private ArrayList iVariableModifications = null;

    /**
     * This method parses all the fixed modifications into an array of ModificationList Strings.
     * The String array will be used later on to create Fixed ModificationList instances.
     *
     * @param m Hashmap         masses section
     * @return ArrayList    ArrayList with all the data from 1 fixed modification.
     */
    private ArrayList getFixedModifications(HashMap m) {
        //1. Create ArrayList with modifications
        ArrayList lFixedModifications = new ArrayList();

        //2.counter for the upcomming loop to count the amount of fixed modifications.
        int lCount = 1;

        //3.While there are more fixed modifications, put them in the array.
        //  First add the FixedMod Value, then add a ',' then add the FixedModResidues Value.
        //  Now each element of the ArrayList contains all the possible info about 1 fixed modification.
        while (m.get("FixedMod" + lCount) != null) {
            StringBuffer sb = new StringBuffer();
            sb.append((String) m.get("FixedMod" + lCount));
            sb.append(',');
            sb.append((String) m.get("FixedModResidues" + lCount));
            lFixedModifications.add(sb.toString());
            lCount++;
        }
        return lFixedModifications;
    }

    /**
     * This method parses all the variable modifications into an array of variable ModificationList Strings.
     * The String array will be used later on to create Variable ModificationList instances.
     *
     * @param m Hashmap         masses section
     * @return ArrayList    ArrayList with all the data from 1 variable modification.
     */
    private ArrayList getVariableModifications(HashMap m) {

        //1. Create ArrayList with modifications
        ArrayList lVariableModifications = new ArrayList();

        //2.counter for the upcomming loop to count the amount of fixed modifications.
        int lCount = 1;

        //3.While there are more variable modifications, put them in the array.
        //  First add the VariableMod Value, then add a ',' then add the NeutralLoss Value.
        //  Now each element of the ArrayList contains all the possible info about 1 variable modification.
        while (m.get("delta" + lCount) != null) {
            StringBuffer sb = new StringBuffer();
            sb.append((String) m.get("delta" + lCount));
            sb.append(',');
            sb.append((String) m.get("NeutralLoss" + lCount));
            lVariableModifications.add(sb.toString());
            lCount++;
        }
        return lVariableModifications;
    }

    /**
     * This constructor reads out all of the data that comes with the hashmap.
     * This hashmap actually coppied to a local HashMap that contains Double values for the masses now!
     *
     * @param m HashMap     Masses section of the datfile.
     */
    public Masses(HashMap m) {
        //parse all the key-values into instance variables.
		Iterator iter = m.keySet().iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			iMasses.put(o, m.get(o));
		}
        iFixedModifications = getFixedModifications(m);
        iVariableModifications = getVariableModifications(m);
    }

    /**
     * Used Fixed ModificationList in an array.
     *
     * @return ArrayList with the Fixed Modification instances.
     */
    public ArrayList getFixedModifications() {
        return iFixedModifications;
    }

    /**
     * Used Fixed ModificationList in an array.
     *
     * @param aFixedModifications ArrayList with the FixedModifications to be set.
     */
    public void setFixedModifications(ArrayList aFixedModifications) {
        iFixedModifications = aFixedModifications;
    }

    /**
     * Used Variable ModificationList in an array.
     *
     * @return ArrayList with the Variable Modification instances.
     */
    public ArrayList getVariableModifications() {
        return iVariableModifications;
    }

    /**
     * Used Variable ModificationList in an array.
     *
     * @param aVariableModifications ArrayList with the FixedModifications to be set.
     */
    public void setVariableModifications(ArrayList aVariableModifications) {
        iVariableModifications = aVariableModifications;
    }

    /**
     * This method returns the mass of the AA that is requested as a parameter.
     *
     * @param aa The requested AA in one letter code.
     * @return double  Returns the mass of the requested AA.
     */
    public double getMass(char aa) {
        String s = String.valueOf(aa);
        return getMass(s);
    }

    /**
     * This method returns the mass of the AA that is requested as a parameter.
     *
     * @param aa The requested AA in one letter code.
     * @return double  Returns the mass of the requested AA.
     */
    public double getMass(String aa) {
        if (!iMasses.containsKey(aa)) {
            throw new IllegalArgumentException(" The requested mass for " + aa + " is no key in the iMasses HashMap.");
        } else {
            return Double.parseDouble((String) iMasses.get(aa));
        }
    }

}
