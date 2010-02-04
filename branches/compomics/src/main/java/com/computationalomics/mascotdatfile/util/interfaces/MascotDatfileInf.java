package com.compomics.mascotdatfile.util.interfaces;

import com.compomics.mascotdatfile.util.mascot.*;
import com.compomics.mascotdatfile.util.mascot.iterator.QueryEnumerator;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 10-jul-2008 Time: 17:17:56 To change this template use File | Settings |
 * File Templates.
 */
public interface MascotDatfileInf {
    /**
     * Returns the header section of a Mascot result file as a Header object.
     *
     * @return Header instance.
     */
    public Header getHeaderSection();

    /**
     * Returns the masses section of a Mascot result file as a Masses object.
     *
     * @return Masses instance.
     */
    public Masses getMasses();

    /**
     * Returns the distinct peptide modifications set to active in this Mascot result file.
     *
     * @return ModificationList instance.
     */
    public ModificationList getModificationList();

    /**
     * Returns the number of queries (searched MS/MS spectra) that are stored in the Mascot result file.
     *
     * @return integer value
     */
    int getNumberOfQueries();

    /**
     * Returns a mapping from the Query to its PeptideHits as a QueryToPeptideMap implementation.
     *
     * @return QueryToPeptideMap implementation
     */
    public QueryToPeptideMapInf getQueryToPeptideMap();

    /**
     * Returns a mapping from the Query to its decoy Peptidehits as a QueryToPeptideMap implementation.
     *
     * @return QueryToPeptideMap implementation. Returns null if the decoy search (Mascot 2.2) was not performed.
     */
    public QueryToPeptideMapInf getDecoyQueryToPeptideMap();

    /**
     * Please use the getQuery method to receive the Queries one by one.
     * Returns all the Queries in a vector.
     *
     * @return Vector including all searched MS/MS spectra as Query instances.
     * @deprecated New methods replace the memory consuming vector. We advise to use either <code>getQuery()</code>
     *             to retrieve a single Query or <code>getQueryEnumerator()</code> to enumerate all Queries.
     */
    public Vector getQueryList();

    /**
     * Returns a single MS/MS spectrum as a Query instance.
     *
     * @param aQueryNumber of the Query that must be returned. '1'-based as in '1' will return Query 1.
     * @return a single Query instance of the given query number.
     */
    public Query getQuery(int aQueryNumber);

    /**
     * Returns a Enumeration implemenation that can pass all Queries of the MascotDatfile one by one.
     *
     * @return QueryEnumerator instance.
     */
    public QueryEnumerator getQueryEnumerator();

    /**
     * Returns the parameter section of the Mascot result file as a Parameter instance.
     *
     * @return Parameter instance.
     */
    public Parameters getParametersSection();

    /**
     * Returns a mapping from the Peptide sequence to the queries wherefrom that a peptide was suggested.
     *
     * @return a PeptideToQueryMap instance.
     */
    public PeptideToQueryMap getPeptideToQueryMap();

    /**
     * Returns a map of all Proteins that were suggested by linking suggested peptides in this Mascot result file.
     * <br>Note, the ProteinMap is not automatically indexed when
     *
     * @return a ProteinMap instance.
     */
    public ProteinMap getProteinMap();

    /**
     * Returns a map of all Proteins that were suggested by linking suggested decoy peptides in this Mascot result file.
     *
     * @return a ProteinMap instance.
     */
    public ProteinMap getDecoyProteinMap();

    /**
     * Returns a map from the MS/MS spectrum filenames to their query number in the Mascot result file.
     *
     * @return HashMap with MS/MS spectra filenames as keys, querynumbers as values.
     */
    public HashMap getSpectrumFilenameToQuerynumberMap();

    /**
     * Returns the name of the file (if initially given).
     *
     * @return filename
     */
    public String getFileName();

    /**
     * Sets the filename of the Mascot result file.
     *
     * @param aFileName name of the file.
     */
    public void setFileName(String aFileName);

    /**
     * Assures that all streams are broken and all temporary files are removed.
     */
    public void finish();

    /**
     * Returns the Quantitation section of a Mascot Results file.
     *
     * @return The Quantitation section.
     */
    public Quantitation getQuantitation();
}
