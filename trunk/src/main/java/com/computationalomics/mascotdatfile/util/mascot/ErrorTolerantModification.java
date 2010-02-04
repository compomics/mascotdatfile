package com.computationalomics.mascotdatfile.util.mascot;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: kenny
 * Date: Jun 15, 2009
 * Time: 12:09:30 PM
 * <p/>
 * This class
 */
public class ErrorTolerantModification implements com.computationalomics.mascotdatfile.util.interfaces.Modification, Serializable {
    /**
     * The standard type of the variable modification.
     */
    private String iType = null;
    /**
     * The mass of the variable modification.
     */
    private double iMass = 0;
    /**
     * The (possible) neutral loss that comes along with the variable modification.
     */
    private double iNeutralLoss = 0;
    /**
     * The location of the variable modification
     */
    private String iLocation = null;
    /**
     * The int ID of the variable modification as used in the peptideHits_VariableModificationsString
     */
    private int iModificationID = 0;
    /**
     * The short end notation for a modifcication.
     */
    private String iShortType = null;

    /**
     * Contructor
     * Generate a fixed modification containing different parameters of the modification.
     *
     * @param aType           standard name by mascot
     * @param aMass           mass
     * @param aNeutralLoss    neutral loss
     * @param aLocation       location ( Can be 'null' if location unspecified )
     * @param aModificationID modification integer ID like it used in a PeptideHit ModificationSequence String.
     */
    public ErrorTolerantModification(String aType, String aShortType, double aMass, double aNeutralLoss, String aLocation, int aModificationID) {
        iType = aType;
        iMass = aMass;
        iNeutralLoss = aNeutralLoss;
        iLocation = aLocation;
        iModificationID = aModificationID;
        iShortType = aShortType;
    }

    /**
     * Returns the type of the modification, read from the '.dta' file
     *
     * @return the standard name of the modification, read from the '.dta' file
     */
    public String getType() {
        return iType;
    }

    /**
     * Sets the type of the modification, read from the '.dta' file
     *
     * @param aType the standard name of the modification, read from the '.dta' file
     */
    public void setType(String aType) {
        iType = aType;
    }

    /**
     * Returns the mass of the modification.
     *
     * @return the mass of the modification.
     */
    public double getMass() {
        return iMass;
    }

    /**
     * Sets the mass of the modification.
     *
     * @param aMass the mass of the modification.
     */
    public void setMass(double aMass) {
        iMass = aMass;
    }

    /**
     * Returns the (possible) neutral loss that comes along with the modification.
     *
     * @return the (possible) neutral loss that comes along with the modification.
     */
    public double getNeutralLoss() {
        return iNeutralLoss;
    }

    /**
     * Sets the (possible) neutral loss that comes along with the modification.
     *
     * @param aNeutralLoss the (possible) neutral loss that comes along with the modification.
     */
    public void setNeutralLoss(double aNeutralLoss) {
        iNeutralLoss = aNeutralLoss;
    }

    /**
     * Returns the one-lettercode of the aminoacids with Fixed ModificationList.
     * ( Can be 'null' if location unspecified )
     *
     * @return the one-lettercode of the aminoacids with Fixed ModificationList.
     */
    public String getLocation() {
        return iLocation;
    }

    /**
     * Sets the one-lettercode of the aminoacids with Fixed ModificationList.
     * ( Can be 'null' if location unspecified )
     *
     * @param aLocation the one-lettercode of the aminoacids with Fixed ModificationList.
     */
    public void setLocation(String aLocation) {
        iLocation = aLocation;
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
     * Sets the int ID of the modification as used in the peptideHits_VariableModificationsString
     *
     * @param aModificationID the int ID of the modification as used in the peptideHits_VariableModificationsString
     */
    public void setModificationID(int aModificationID) {
        iModificationID = aModificationID;
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
     * Sets the short end notation for a modifcication.
     *
     * @param aShortType the short end notation for a modifcication.
     */
    public void setShortType(String aShortType) {
        iShortType = aShortType;
    }

    /**
     * Method to identify the object type.
     *
     * @return boolean      false, this is a VariableModification object.
     */
    public boolean isFixed() {
        return false;
    }

}
