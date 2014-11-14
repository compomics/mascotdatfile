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

package com.compomics.mascotdatfile.util.mascot;

import com.compomics.mascotdatfile.util.exception.MascotDatfileException;
import com.compomics.mascotdatfile.util.interfaces.Modification;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 26-feb-2006
 * Time: 14:01:38
 */

/**
 * Instances of this class represent 1 fixed modifications that will be on every peptideHit while doing the Mascot
 * search.
 */
public class FixedModification implements Modification, Serializable {
    // Class specific log4j logger for FixedModification instances.
    private static Logger logger = Logger.getLogger(FixedModification.class);
    /**
     * The standard name of the modification, read from the '.dta' file
     */
    private String iType = null;
    /**
     * The mass of the modification.
     */
    private double iMass = 0;

    /**
     * This boolean indicates whether the mass was supplied during construction.
     */
    private boolean iHasMass = false;

    /**
     * The one-lettercode of the aminoacids with Fixed ModificationList. Multi aminoacid's are possible. (ex: QN * Dam)
     * The "QN" String will be converted to a char[] if necissairy in the PeptideHit instance.
     */
    private String iLocation = null;
    /**
     * The int ID of the modification as used in the peptideHits_VariableModificationsString
     */
    private int iModificationID = 0;
    /**
     * The short end notation for a modifcication.
     */
    private String iShortType = null;


    /**
     * Contructor Generate a fixed modification containing different parameters of the modification.
     *
     * @param aType           standard name by mascot
     * @param aMass           mass
     * @param aLocation       location
     * @param aModificationID modification integer ID like it used in a PeptideHit ModificationSequence String.
     */
    public FixedModification(String aType, String aShortType, double aMass, String aLocation, int aModificationID) {
        iType = aType;
        iMass = aMass;
        iHasMass = true;
        iLocation = aLocation;
        iModificationID = aModificationID;
        iShortType = aShortType;
    }

    /**
     * Contructor Generate a fixed modification containing different parameters of the modification except for the mass!
     * This constructor needs to be called when parsing old datfiles, they dont contain the mass of the fixed
     * modifications.
     *
     * @param aType           standard name by mascot
     * @param aLocation       location
     * @param aModificationID modification integer ID like it used in a PeptideHit ModificationSequence String.
     * @param aShortType
     */
    public FixedModification(String aType, String aShortType, String aLocation, int aModificationID) {
        iType = aType;
        iShortType = aShortType;
        iLocation = aLocation;
        iModificationID = aModificationID;

        // Mass is not supplied!!
        iHasMass = false;
    }

    /**
     * Returns the standard name of the modification, read from the '.dta' file
     *
     * @return the standard name of the modification, read from the '.dta' file
     */
    public String getType() {
        return iType;
    }



    /**
     * Returns the mass of the modification.
     *
     * @return the mass of the modification by the (old) datfile.
     * @throws MascotDatfileException
     */
    public double getMass() {
        if (isValidMass() == false) {
            // If the mass was set to -2, then the mass of the fixed modification was not supplied by the (old) datfile.
            throw new MascotDatfileException("You cannot acces the mass as of this fixed modifcation (" + iType + "). It was not supplied in the mascot datfile.");
        }
        return iMass;
    }


    /**
     * Returns the one-lettercode of the aminoacids with Fixed ModificationList.
     *
     * @return the one-lettercode of the aminoacids with Fixed ModificationList.
     */
    public String getLocation() {
        return iLocation;
    }



    /**
     * Returns the int ID of the modification as used in the peptideHits_VariableModificationsString
     *
     * @return the int ID of the modification as used in the peptideHits_VariableModificationsString
     */
    public int getModificationID() {
        return iModificationID;
    }


    /**
     * Method to identify the object type.
     *
     * @return boolean      true, this is a FixedModification object.
     */
    public boolean isFixed() {
        return true;
    }

    /**
     * Returns the short end notation for a modifcication.
     *
     * @return the short end notation for a modifcication.
     */
    public String getShortType() {
        return iShortType;
    }


    /**
     * Returns whether this FixedModification instance had a Mass value supplied during construction.
     * @return True if the FixedModification has a mass. False if else.
     */
    public boolean isValidMass() {
        return iHasMass;
    }
}
