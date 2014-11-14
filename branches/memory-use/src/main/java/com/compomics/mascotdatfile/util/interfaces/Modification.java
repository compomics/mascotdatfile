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

package com.compomics.mascotdatfile.util.interfaces;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 28-feb-2006
 * Time: 10:53:23
 */

/**
 * Interface.<br>
 * Implemented by FixedModification.<br>
 * (Instances of this class represent 1 fixed modifications that will be on every peptideHit while doing the Mascot search.)<br><br>
 * Implemented by VariableModification.<br>
 * (Instances of this class represent 1 variable modification that can be used to create a peptideHit during the Mascot search.)<br><br>
 *
 * @see <ul>
 *      <li>getType()
 *      The standard name of the modification, read from the '.dta' file
 *      <li>getMZ()
 *      The mass of the modification.
 *      <li>getLocation()
 *      The location of the ModificationList(wich residue will be modified).
 *      <li>getModificationID()
 *      The int ID of the modification as used in the peptideHits_*ModificationsString.
 *      </ul>
 */
public interface Modification {
    /**
     * The standard name of the modification, read from the '.dta' file
     * @return Type
     */
    String getType();

    /**
     * The mass of the modification.
     */
    double getMass();

    /**
     * The location of the ModificationList(wich residue will be modified).
     */
    String getLocation();

    /**
     * The int ID of the modification as used in the peptideHits_*ModificationsString
     */
    int getModificationID();

    /**
     * method to test the object type.
     *
     * @return boolean      true = Fixed modification.
     *         false = Variable modification.
     */
    boolean isFixed();

    /**
     * Method
     *
     * @return String   The short end notation for a modifcication.
     */
    String getShortType();


    /**
     * Returns whether this FixedModification instance had a Mass value supplied during construction.
     * @return True if the FixedModification has a mass. False if else.
     */
    boolean isValidMass();
}
