package com.computationalomics.mascotdatfile.util.mascot.enumeration;

/**
 * Enumaration for the distinct MascotDatfile types.
 */
public enum MascotDatfileType {
    /*
     * Types a MascotDatfile implementation that works completely in memory.
     * As a rough measure, a 10-fold of the file size will be required as virtual memory.
     * ex: A 100MB Mascot result file requires 1000 MB virtual memory.
     *
     * If memory is no issue, use this type as it is most fast.
     */
    MEMORY("In-memory parsing"),
    /**
     * Types a MascotDatfile implementation that is fully indexed.
     * As a rough measure, half of the file size will be required as virtual memory.
     * ex: A 100MB Mascot result file requires 50MB virtual memory.
     *
     * Even though the indexing is far slower, if memory is an issue, use this type of MascotDatfile.
     */
    INDEX("Index-based parsing");

    /**
     * A name for the Enum.
     */
    private String iName;

    /**
     * Constructor for the enum.
     * @param aName String name for the enum.
     */
    MascotDatfileType(String aName) {
        iName = aName;
    }

    /**
     * Returns a description for the enum.
     * @return String describing the enum.
     */
    public String toString() {
        return iName;
    }
}
