package com.computationalomics.mascotdatfile.util.mascot.index;


/**
 * Created by IntelliJ IDEA. User: Kenny Date: 7-jul-2008 Time: 14:45:00 To change this template use File | Settings |
 * File Templates.
 */
public class SummaryIndex {

    private static SummaryIndex singleton = null;
    private static SummaryIndex decoyInstance;

    public static int QMASS= 0;
    public static int QEXP = 1;
    public static int QMATCH = 2;
    public static int QPLUGHOLE = 3;

    public static int QINTENSITY = 4;

    private int iSummaryLineIndex;
    private int iNumberOfIndexes = 0;

    /**
     * Setter for property 'numberOfIndexes'.
     *
     * @param aNumberOfIndexes Value to set for property 'numberOfIndexes'.
     */
    public void setNumberOfIndexes(final int aNumberOfIndexes) {
        iNumberOfIndexes = aNumberOfIndexes;
    }

    private int mass_index = -1;

    private int exp_index = -1;

    private int match_index = -1;

    private int plughole_index = -1;

    private int intensity_index = -1;

    /** Do not instantiate SummaryIndex. */
    private SummaryIndex() {

    }

    /**
     * Getter for property 'instance'.
     *
     * @return Value for property 'instance'.
     */
    public static SummaryIndex getInstance(){
        if(singleton == null){
            singleton = new SummaryIndex();
        }
        return singleton;
    }

    /**
     * Getter for property 'decoyInstance'.
     *
     * @return Value for property 'decoyInstance'.
     */
    public static SummaryIndex getDecoyInstance() {
        if(decoyInstance == null){
            decoyInstance = new SummaryIndex();
        }
        return decoyInstance;
    }

    public int getSummaryLine(int aQueryNumber, int aType){
        int aTypeIndex = parseType(aType);
        if(aTypeIndex == -1){
            throw new IllegalAccessError("Illegal summary type requested: \'" + aType + "\'.");
        }
        return iSummaryLineIndex + ((aQueryNumber-1) * iNumberOfIndexes + parseType(aType));
    }

    private int parseType(int aType){
        if(aType == QMATCH){
            return match_index;
        }else if(aType == QPLUGHOLE){
            return plughole_index;
        }else if(aType == QEXP){
            return exp_index;
        }else if(aType == QMASS){
            return mass_index;
        }else if(aType == QINTENSITY){
            return intensity_index;
        }else{
            return -1;
        }
    }

    /**
     * Setter for property 'summaryLineIndex'.
     *
     * @param aSummaryLineIndex Value to set for property 'summaryLineIndex'.
     */
    public void setSummaryLineIndex(final int aSummaryLineIndex) {
        iSummaryLineIndex = aSummaryLineIndex;
    }

    /**
     * Getter for property 'index_type_count'.
     *
     * @return Value for property 'index_type_count'.
     */
    public int getIndex_type_count() {
        return iNumberOfIndexes;
    }

    /**
     * Getter for property 'mass_index'.
     *
     * @return Value for property 'mass_index'.
     */
    public int getMass_index() {
        return mass_index;
    }

    /**
     * Setter for property 'mass_index'.
     *
     * @param aMass_index Value to set for property 'mass_index'.
     */
    public void setMass_index(final int aMass_index) {
        mass_index = aMass_index;
    }

    /**
     * Getter for property 'exp_index'.
     *
     * @return Value for property 'exp_index'.
     */
    public int getExp_index() {
        return exp_index;
    }

    /**
     * Setter for property 'exp_index'.
     *
     * @param aExp_index Value to set for property 'exp_index'.
     */
    public void setExp_index(final int aExp_index) {
        exp_index = aExp_index;
    }

    /**
     * Getter for property 'match_index'.
     *
     * @return Value for property 'match_index'.
     */
    public int getMatch_index() {
        return match_index;
    }

    /**
     * Setter for property 'match_index'.
     *
     * @param aMatch_index Value to set for property 'match_index'.
     */
    public void setMatch_index(final int aMatch_index) {
        match_index = aMatch_index;
    }

    /**
     * Getter for property 'plughole_index'.
     *
     * @return Value for property 'plughole_index'.
     */
    public int getPlughole_index() {
        return plughole_index;
    }

    /**
     * Setter for property 'plughole_index'.
     *
     * @param aPlughole_index Value to set for property 'plughole_index'.
     */
    public void setPlughole_index(final int aPlughole_index) {
        plughole_index = aPlughole_index;
    }

    /**
     * Getter for property 'intensity_index'.
     *
     * @return Value for property 'intensity_index'.
     */
    public int getIntensity_index() {
        return intensity_index;
    }


    /**
     * Setter for property 'intensity_index'.
     *
     * @param aIntensity_index Value to set for property 'intensity_index'.
     */
    public void setIntensity_index(final int aIntensity_index) {
        intensity_index = aIntensity_index;
    }

    /** {@inheritDoc} */
    public String toString() {
        return "SummaryIndex{" +
               "iSummaryLineIndex=" + iSummaryLineIndex +
               ", mass_index=" + mass_index +
               ", exp_index=" + exp_index +
               ", match_index=" + match_index +
               ", plughole_index=" + plughole_index +
               ", intensity_index=" + intensity_index +
               '}';
    }
}
