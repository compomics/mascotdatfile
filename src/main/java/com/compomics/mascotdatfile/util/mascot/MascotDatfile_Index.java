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
import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.index.Controller;
import com.compomics.mascotdatfile.util.mascot.index.DecoyQueryToPeptideMap_Index;
import com.compomics.mascotdatfile.util.mascot.index.QueryToPeptideMap_Index;
import com.compomics.mascotdatfile.util.mascot.index.SummaryIndex;
import com.compomics.mascotdatfile.util.mascot.iterator.QueryEnumerator;

import java.io.BufferedReader;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 21-feb-2006 Time: 16:22:19 This was done by Kenny
 */
public class MascotDatfile_Index implements MascotDatfileInf {
    // Class specific log4j logger for MascotDatfile_Index instances.
    private static Logger logger = Logger.getLogger(MascotDatfile_Index.class);

    /**
     * Controller of the file indexing and reading.
     */
    private Controller iController = null;

    /**
     * Private variable iHeader is a (lazy) instance of Header.
     */
    private Header iHeader = null;

    /**
     * Private variable iMasses is a (lazy) instance of Masses.
     */
    private Masses iMasses = null;

    /**
     * Private variable iModificationList is a (lazy) instance of ModificationList.
     */
    private ModificationList iModificationList = null;

    /**
     * Private variable iParameters is a (lazy) instance of Parameters.
     */
    private Parameters iParameters = null;

    /**
     * Private variable iPeptideToQueryMap is a (lazy) instance of PeptideToQueryMap.
     */
    private PeptideToQueryMap iPeptideToQueryMap = null;


    /**
     * Private variable iProteinMap is a (lazy) instance of ProteinMap.
     */
    private ProteinMap iProteinMap = null;

    /**
     * Private variable iProteinMap is a (lazy) instance of ProteinMap.
     */
    private ProteinMap iDecoyProteinMap = null;

    /**
     * Private variable iQueryToPeptideMap is a (lazy) instance of QueryToPeptideMap.
     */
    private QueryToPeptideMap_Index iQueryToPeptideMap = null;

    /**
     * Private variable iQueryToPeptideMap is a (lazy) instance of QueryToPeptideMap. This Instance holds the decoy
     * hits.
     */
    private DecoyQueryToPeptideMap_Index iDecoyQueryToPeptideMap = null;


    /**
     * Private variable iQueryList is a (lazy) Vector with Query instances.
     */
    private Vector iQueryList = null;

    /**
     * Private variable iQuerynumberToSpectrumfilename is a (lazy) HashMap mapping the Querynumbers of this datfile to
     * the spectrumfilenames.
     */
    private HashMap iQuerynumberToSpectrumfilename = null;

    /**
     * The Quantitation containing object.
     */
    private Quantitation iQuantitation;

    /**
     * Private variable iThreshold holds the threshold values in 2 dimensions. iThreshold[i][j] with i+1 the Querynumber
     * and j=0 homology (QPLUGHOLE) and j=1 identity (QMATCH) threshold of Query i.
     */
    private double[][] iThreshold = null;

    /**
     * Private variable iThreshold holds the threshold values in 2 dimensions. This concerns the thresholds related to
     * the Decoy search. iThreshold[i][j] with i+1 the Querynumber and j=0 homology (QPLUGHOLE) and j=1 identity
     * (QMATCH) threshold of Query i.
     */
    private double[][] iDecoyThreshold = null;


    /**
     * The filename of the Mascot dat file.
     */
    private String iFileName = null;


    /**
     * Constructs MascotDatfile instance from a String containing to an existing path and filename.
     *
     * @param aDatFile where to parse your data from
     */
    public MascotDatfile_Index(String aDatFile) {
        File inputFile = new File(aDatFile);
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("raw Mascot datfile from " + aDatFile + " does not exist.");
        }
        iController = new Controller(inputFile);
        setFileName(new File(aDatFile).getName());
    }

    /**
     * Constructs MascotDatFile instance with a BufferedReader wheretrough the datfile is buffered.
     *
     * @param aReader The bufferedReader containing the datfile.
     */
    public MascotDatfile_Index(BufferedReader aReader) {
        this(aReader, "NA");
    }

    /**
     * Constructs MascotDatFile instance with a BufferedReader wheretrough the datfile is buffered.
     *
     * @param aReader   The bufferedReader containing the datfile.
     * @param aFileName String with the filename.
     */
    public MascotDatfile_Index(BufferedReader aReader, String aFileName) {
        iController = new Controller(aReader);
        setFileName(aFileName);
    }


    /**
     * This method creates a new Header instance. 1.Submit the header HashMap in the constructor. 2.Receive the parsed
     * data into a Header instance.
     *
     * @return Header   instance of Header with all the parsed data of the header section of the datfile.
     */
    public Header getHeaderSection() {
        if (iHeader == null) {
            iHeader = new Header(iController.readSectionAsHashMap("header"));
        }
        return iHeader;
    }

    /**
     * This method creates a new Masses instance. 1.Submit the masses HashMap in the constructor. 2.Receive the parsed
     * data into an Masses instance.
     *
     * @return Masses   instance of Masses with all the parsed data of the masses section of the datfile.
     */
    public Masses getMasses() {
        if (iMasses == null) {
            iMasses = new Masses(iController.readSectionAsHashMap("masses"));
        }
        return iMasses;
    }

    /**
     * This method creates a new Masses instance. 1.Submit the masses HashMap in the constructor. 2.Receive the parsed
     * data into an Masses instance.
     *
     * @return Masses   instance of Masses with all the parsed data of the masses section of the datfile.
     */
    public Quantitation getQuantitation() {
        if (iQuantitation == null) {
            iQuantitation = new Quantitation(iController.readSection("quantitation"));
        }
        return iQuantitation;
    }

    /**
     * This method creates a new ModificationList instance. 1.Get the Fixed and Variable ModificationList ArrayList from
     * the Masses object. 2.Create a new ModificationList instance owning: a)Vector with VariableModification instances.
     * b)Vector with FixedModification instances.
     *
     * @return ModificationList     holding 2 vectors with modification instances.
     */
    public ModificationList getModificationList() {
        if (iModificationList == null) {
            Masses m = getMasses();
            if (m.getFixedModifications().size() > 0) {
                iModificationList = new ModificationList(m.getFixedModifications(), m.getVariableModifications());
            } else {
                Parameters p = getParametersSection();
                iModificationList = new ModificationList(m.getFixedModifications(), m.getVariableModifications(), p.getFixedModifications());
            }
        }
        return iModificationList;
    }

    /**
     * The number of queries done in the mascot search (a parameter of a Header instance).
     *
     * @return int     number of queries done.
     */
    public int getNumberOfQueries() {
        return getHeaderSection().getQueries();
    }

    /**
     * This Class creates a map with all the peptide hits in 2 dimensions. The first level holds the results of the
     * queries in a hashmap (Key: Querynumber  Value:Vector with PeptideHits). The second dimension holds a Vector with
     * the corresponding peptide hits of the query.
     *
     * @return QueryToPeptideMap instance with the queries and peptidehits of this MascotDatfile.
     */
    public QueryToPeptideMap_Index getQueryToPeptideMap() {
        if (iQueryToPeptideMap == null) {
            iQueryToPeptideMap = new QueryToPeptideMap_Index(iController, getProteinMap(), getModificationList());
        }
        return iQueryToPeptideMap;
    }

    /**
     * {@inheritDoc}
     */ /*
     * <b>This is based on the decoy peptides section!!</b>
     * This Class creates a map with all the peptide hits in 2 dimensions.
     * The first level holds the results of the queries in a hashmap (Key: Querynumber  Value:Vector with PeptideHits).
     * The second dimension holds a Vector with the corresponding peptide hits of the query.
     *
     * @return QueryToPeptideMap instance with the queries and peptidehits of this MascotDatfile.
     * <b>Can return null if no decoy search was done.</b>
     */
    public QueryToPeptideMap_Index getDecoyQueryToPeptideMap() {
        return getDecoyQueryToPeptideMap(true);
    }
    
    /**
     * {@inheritDoc}
     */ /*
     * <b>This is based on the decoy peptides section!!</b>
     * This Class creates a map with all the peptide hits in 2 dimensions.
     * The first level holds the results of the queries in a hashmap (Key: Querynumber  Value:Vector with PeptideHits).
     * The second dimension holds a Vector with the corresponding peptide hits of the query.
     *
     * @return QueryToPeptideMap instance with the queries and peptidehits of this MascotDatfile.
     * <b>Can return null if no decoy search was done.</b>
     */
    public QueryToPeptideMap_Index getDecoyQueryToPeptideMap(boolean showErrorMessage) {
        if (iDecoyQueryToPeptideMap == null) {
            iDecoyQueryToPeptideMap = new DecoyQueryToPeptideMap_Index(iController, getDecoyProteinMap(), getModificationList());
        }
        return iDecoyQueryToPeptideMap;
    }


    /**
     * @return Vector with the corresponding Query instances. Query n is located at <Vector>.get(n-1)
     * @deprecated This method is depracated as it uses a lot of memory. It is better advised the getNumberOfQueries
     * method and retrieve the queries one by one with the getQuery() method.
     * <p/>
     * This method parses all the datfile Query sections into a Vector with Query instances containing all the original
     * data.
     */
    public Vector getQueryList() {
        if (iQueryList == null) {
            int lNumQueries = iController.getNumberOfQueries();
            iQueryList = new Vector(lNumQueries);
            for (int i = 1; i < lNumQueries + 1; i++) {
                iQueryList.add(getQuery(i));
            }
        }
        return iQueryList;
    }

    /**
     * Returns a new Iterator for all Queries in this MascotDatfile.
     * The Iterator on this Indexed MascotDatfile will read the Query instances one after another from the file.
     * Every call to this method returns a new Iterator.
     * @return Iterator to iterate over all Queries in this Datfile.
     */
    public Iterator getQueryIterator() {
        return new Iterator() {
            private int lCounter = 0;

            public boolean hasNext() {
                return lCounter < getNumberOfQueries();
            }

            public Query next() {
                lCounter = lCounter + 1;
                return getQuery(lCounter);
            }

            public void remove() {
                // Not implemented.
            }
        };
    }

    /**
     * Returns Query n as the nth MS/MS spectrum in this MascotDatfile instance.
     *
     * @param aQueryNumber 1 returns Query 1.
     * @return Query instance for the specified QueryNumber.
     */
    public Query getQuery(int aQueryNumber) {
        Query q = null;

        HashMap lQuerySection = iController.readSectionAsHashMap("query" + aQueryNumber);
        // We need to get some things (precursor-related) from the summary section...
        // Precursor mass.
        double lPrecursorMass = Double.parseDouble((String) iController.readSummary(aQueryNumber, SummaryIndex.QMASS));
        // Precursor m/z && Charge.
        String lMzAndChargeString = iController.readSummary(aQueryNumber, SummaryIndex.QEXP);
        StringTokenizer st = new StringTokenizer(lMzAndChargeString, ",");
        double lPrecursorMZ = Double.parseDouble(st.nextToken());
        String lCharge = st.nextToken();
        if (st.hasMoreTokens()) {
            throw new MascotDatfileException("There are tokens left unused!");
        }
        // Precursor intensity.
        double lPrecursorIntensity = -1;
        //bugfix 090506 -- Some datfiles dont have the qintensityX field in their summary section and this resulted in a null pointer.

        if (SummaryIndex.getInstance().getIntensity_index() != -1) {
            lPrecursorIntensity = Double.parseDouble((String) iController.readSummary(aQueryNumber, SummaryIndex.QINTENSITY));
        }

        return q = new Query(lQuerySection, lPrecursorMZ, lCharge, lPrecursorMass, lPrecursorIntensity, aQueryNumber, getParametersSection());
    }

    /**
     * @inheritDoc
     */
    public QueryEnumerator getQueryEnumerator() {
        return new QueryEnumerator(this);  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * This method creates a new Parameters instance. 1.Submit the paramaters HashMap in the constructor. 2.Receive the
     * parsed data into an Parameters instance.
     *
     * @return Parameters   instance of Parameters with all the parsed data of the parameters section of the datfile.
     */
    public Parameters getParametersSection() {
        if (iParameters == null) {
            iParameters = new Parameters(iController.readSectionAsHashMap("parameters"));
        }
        return iParameters;
    }

    /**
     * The method gets the PeptideToQueryMap of the datfile.
     *
     * @return PeptideToQueryMap
     */
    public PeptideToQueryMap getPeptideToQueryMap() {
        if (iPeptideToQueryMap == null) {
            iPeptideToQueryMap = new PeptideToQueryMap(getQueryToPeptideMap(), this.getQueryList());
        }
        return iPeptideToQueryMap;
    }

    /**
     * This method gets the ProteinMap. All the proteins from the protein section are included. The proteinID's include
     * a 2D array with the queries and peptidehits wherein they were found.
     *
     * @return ProteinMap
     */
    public ProteinMap getProteinMap() {
        if (iProteinMap == null) {
            HashMap m = iController.readSectionAsHashMap("proteins");
            if (m != null) {
                iProteinMap = new ProteinMap(m);
            }
        }
        return iProteinMap;
    }

    /**
     * This method gets the ProteinMap. All the proteins from the protein section are included. The proteinID's include
     * a 2D array with the queries and peptidehits wherein they were found.
     *
     * @return ProteinMap
     */

    public ProteinMap getDecoyProteinMap() {
        if (iDecoyProteinMap == null) {
            HashMap m = iController.readSectionAsHashMap("decoy_proteins");
            if (m != null) {
                iDecoyProteinMap = new ProteinMap(m);
            }
        }
        return iDecoyProteinMap;
    }

    /**
     * This method returns a HashMap mapping the Querynumbers to the SpectrumFilenames in the datfile.
     *
     * @return HashMap - key:Spectrumfilename(title) value:Querynumber
     */
    public HashMap getSpectrumFilenameToQuerynumberMap() {
        if (iQuerynumberToSpectrumfilename == null) {
            iQuerynumberToSpectrumfilename = new HashMap();
            //    Vector lQueryList = this.getQueryList();
            for (int i = 0; i < this.getNumberOfQueries(); i++) {
                Query lQuery = (Query) getQuery(i + 1);
                int lQueryNumber = lQuery.getQueryNumber();
                String lQueryTitle = lQuery.getTitle();
                iQuerynumberToSpectrumfilename.put(lQueryTitle, new Integer(lQueryNumber));
            }
        }
        return iQuerynumberToSpectrumfilename;
    }

    public String getFileName() {
        if (iFileName == null) {
            return "NA";
        } else {
            return iFileName;
        }
    }

    /**
     * Sets the name of this Mascot dat file as used in the filesystem.
     *
     * @param aFileName String with the filename of the Datfile.
     */
    public void setFileName(String aFileName) {
        iFileName = aFileName;
    }

    /**
     * Call to close this indexed MascotDatfile must be finished. The controller may have build a temporary file that
     * must be deleted.
     */
    public void finish() {
        iController.close();
    }
}
