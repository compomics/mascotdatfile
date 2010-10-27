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
import com.compomics.mascotdatfile.util.mascot.iterator.QueryEnumerator;
import com.compomics.mascotdatfile.util.mascot.parser.MascotRawParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 21-feb-2006 Time: 16:22:19 This was done by Kenny
 */
public class MascotDatfile implements MascotDatfileInf {
    // Class specific log4j logger for MascotDatfile instances.
    private static Logger logger = Logger.getLogger(MascotDatfile.class);

    /**
     * Private variable iHeader is a (lazy) instance of Header.
     */
    private Header iHeader = null;

    /**
     * Private variable iMasses is a (lazy) instance of Masses.
     */
    private Masses iMasses = null;

    /**
     * Private variable iMRP is an instance of MascotRawParser. Elementary datfile parsing object.
     */
    private MascotRawParser iMRP = null;

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
     * The Quantitation containing object.
     */
    private Quantitation iQuantitation;

    /**
     * Private variable iQueryToPeptideMap is a (lazy) instance of QueryToPeptideMap.
     */
    private QueryToPeptideMap iQueryToPeptideMap = null;

    /**
     * Private variable iQueryToPeptideMap is a (lazy) instance of QueryToPeptideMap. This Instance holds the decoy
     * hits.
     */
    private QueryToPeptideMap iDecoyQueryToPeptideMap = null;

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
    public MascotDatfile(String aDatFile) {
        try {
            File inputFile = new File(aDatFile);
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("raw Mascot datfile from " + aDatFile + " does not exist.");
            }
            iMRP = new MascotRawParser(inputFile);
            setFileName(new File(aDatFile).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs MascotDatFile instance with a BufferedReader wheretrough the datfile is buffered.
     *
     * @param aReader The bufferedReader containing the datfile.
     */
    public MascotDatfile(BufferedReader aReader) {
        try {
            iMRP = new MascotRawParser(aReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs MascotDatFile instance with a BufferedReader wheretrough the datfile is buffered.
     *
     * @param aReader   The bufferedReader containing the datfile.
     * @param aFileName String with the filename.
     */
    public MascotDatfile(BufferedReader aReader, String aFileName) {
        try {
            iMRP = new MascotRawParser(aReader);
            setFileName(aFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method creates a new Header instance. 1.Submit the header HashMap in the constructor. 2.Receive the parsed
     * data into a Header instance.
     *
     * @return Header   instance of Header with all the parsed data of the header section of the datfile.
     */
    public Header getHeaderSection() {
        if (iHeader == null) {
            iHeader = new Header(iMRP.getSection("header"));
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
            iMasses = new Masses(iMRP.getSection("masses"));
        }
        return iMasses;
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
    public QueryToPeptideMap getQueryToPeptideMap() {
        if (iQueryToPeptideMap == null) {
            if (!isErrorTolerantSearch()) {
                // No ets, only parse the peptides.
                iQueryToPeptideMap = new QueryToPeptideMap(iMRP.getSection("peptides"), getProteinMap(), getNumberOfQueries(), getModificationList(), getThresholds());
            } else {
                iQueryToPeptideMap = new QueryToPeptideMap(iMRP.getSection("peptides"), iMRP.getSection("et_peptides"), getProteinMap(), getNumberOfQueries(), getModificationList(), getThresholds());
            }
        }
        return iQueryToPeptideMap;
    }

    /**
     * <b>This is based on the decoy peptides section!!</b> This Class creates a map with all the peptide hits in 2
     * dimensions. The first level holds the results of the queries in a hashmap (Key: Querynumber  Value:Vector with
     * PeptideHits). The second dimension holds a Vector with the corresponding peptide hits of the query.
     *
     * @return QueryToPeptideMap instance with the queries and peptidehits of this MascotDatfile. <b>Can return null if
     *         no decoy search was done.</b>
     */
    public QueryToPeptideMap getDecoyQueryToPeptideMap() {
        if (iDecoyQueryToPeptideMap == null) {
            HashMap lHashMap = iMRP.getSection("decoy_peptides");
            if (lHashMap != null) {
                iDecoyQueryToPeptideMap = new QueryToPeptideMap(lHashMap, getProteinMap(), getNumberOfQueries(), getModificationList(), getThresholds());
            } else {
                System.err.println("No Decoy section found!!");
            }
        }
        return iDecoyQueryToPeptideMap;
    }


    /**
     * This method parses all the datfile Query sections into a Vector with Query instances containing all the original
     * data.
     *
     * @return Vector with the corresponding Query instances. Query n is located at <Vector>.get(n-1)
     */
    public Vector getQueryList() {
        if (iQueryList == null) {
            int lNumQueries = iMRP.getNumberOfQueries();
            iQueryList = new Vector(lNumQueries);
            for (int i = 1; i < lNumQueries + 1; i++) {
                HashMap lQuerySection = iMRP.getSection("query" + i);
                // We need to get some things (precursor-related) from the summary section...
                HashMap lSummarySection = iMRP.getSection("summary");
                // Precursor mass.
                double lPrecursorMass = Double.parseDouble((String) lSummarySection.get("qmass" + i));
                // Precursor m/z && Charge.
                String lMzAndChargeString = (String) lSummarySection.get("qexp" + i);
                StringTokenizer st = new StringTokenizer(lMzAndChargeString, ",");
                double lPrecursorMZ = Double.parseDouble(st.nextToken());
                String lCharge = st.nextToken();
                if (st.hasMoreTokens()) {
                    throw new MascotDatfileException("There are tokens left unused!");
                }
                // Precursor intensity.
                double lPrecursorIntensity = -1;
                //bugfix 090506 -- Some datfiles dont have the qintensityX field in their summary section and this resulted in a null pointer.
                if (lSummarySection.get("qintensity" + i) != null) {
                    lPrecursorIntensity = Double.parseDouble((String) lSummarySection.get("qintensity" + i));
                }
                iQueryList.add(new Query(lQuerySection, lPrecursorMZ, lCharge, lPrecursorMass, lPrecursorIntensity, i, getParametersSection()));
            }
        }
        return iQueryList;
    }

    /**
     * Returns an Iterator implementation for all Query instances captured inside the MascotDatfile.
     * @return Iterator instance
     */
    public Iterator getQueryIterator() {
        return getQueryList().iterator();
    }

    public Query getQuery(final int aQueryNumber) {
        return (Query) getQueryList().get(aQueryNumber - 1);  //To change body of implemented methods use File | Settings | File Templates.
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
            iParameters = new Parameters(iMRP.getSection("parameters"));
        }
        return iParameters;
    }

    /**
     * Initiate the isErrorTolerantSearch boolean
     */
    private boolean isErrorTolerantSearch() {
        // Get the ets value.
        String lErrorTolerantSearch = getParametersSection().getErrorTolerant();

        if (lErrorTolerantSearch != null) {
            // If 1, ets was active.
            if (lErrorTolerantSearch.equals("1")) {
                return true;
            } else if (lErrorTolerantSearch.equals("0")) {
                return false;
            } else {
                throw new MascotDatfileException(
                        "Unexpected value ' " + iParameters.getErrorTolerant() +
                                "' for error tolerant search parameter!!");
            }
        }
        return false;
    }

    /**
     * The method gets the PeptideToQueryMap of the datfile.
     *
     * @return PeptideToQueryMap
     */
    public PeptideToQueryMap getPeptideToQueryMap() {
        if (iPeptideToQueryMap == null) {
            iPeptideToQueryMap = new PeptideToQueryMap(getQueryToPeptideMap(), getQueryList());
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
            iProteinMap = new ProteinMap(iMRP.getSection("proteins"));
        }
        return iProteinMap;
    }

    public ProteinMap getDecoyProteinMap() {
        return getProteinMap();
    }

    /**
     * This method returns a HashMap mapping the Querynumbers to the SpectrumFilenames in the datfile.
     *
     * @return HashMap - key:Spectrumfilename(title) value:Querynumber
     */
    public HashMap getSpectrumFilenameToQuerynumberMap() {
        if (iQuerynumberToSpectrumfilename == null) {
            iQuerynumberToSpectrumfilename = new HashMap();
            Vector lQueryList = this.getQueryList();
            for (int i = 0; i < lQueryList.size(); i++) {
                Query lQuery = (Query) lQueryList.elementAt(i);
                int lQueryNumber = lQuery.getQueryNumber();
                String lQueryTitle = lQuery.getTitle();
                iQuerynumberToSpectrumfilename.put(lQueryTitle, new Integer(lQueryNumber));
            }
        }
        return iQuerynumberToSpectrumfilename;
    }

    /**
     * method that creates iThreshold.<br> It creates the private variable iThreshold that is holding the threshold
     * values in 2 dimensions.<br> iThreshold[i][j] with i+1 the Querynumber and j=0 homology and j=1 identity score
     * threshold of Query i.
     *
     * @return Double[][] ex:          iThreshold[5] holds the thresholds of query 6. iThreshold[5][0] holds the
     *         homology threshold of query 6. iThreshold[5][0] holds the identity threshold of query 6.
     */
    public double[][] getThresholds() {
        if (iThreshold == null) {
            iThreshold = new double[getNumberOfQueries()][2];
            HashMap lSummary = iMRP.getSection("summary");
            for (int i = 0; i < getNumberOfQueries(); i++) {
                iThreshold[i][0] = Double.parseDouble((String) lSummary.get("qplughole" + (i + 1)));
                iThreshold[i][1] = Double.parseDouble((String) lSummary.get("qmatch" + (i + 1)));
            }
        }
        return iThreshold;
    }


    /**
     * method that creates iThreshold.<br> It creates the private variable iThreshold that is holding the threshold
     * values in 2 dimensions.<br> iThreshold[i][j] with i+1 the Querynumber and j=0 homology and j=1 identity score
     * threshold of Query i.
     *
     * @return Double[][] ex:          iThreshold[5] holds the thresholds of query 6. iThreshold[5][0] holds the
     *         homology threshold of query 6. iThreshold[5][0] holds the identity threshold of query 6.
     */
    public double[][] getDecoyThresholds() {
        if (iDecoyThreshold == null) {
            iDecoyThreshold = new double[getNumberOfQueries()][2];
            HashMap lSummary = iMRP.getSection("decoy_summary");
            // If no decoy searches were performed, there's no decoy summary section and null will be returned!
            if (lSummary == null) {
                for (int i = 0; i < getNumberOfQueries(); i++) {
                    iDecoyThreshold[i][0] = Double.parseDouble((String) lSummary.get("qplughole" + (i + 1)));
                    iDecoyThreshold[i][1] = Double.parseDouble((String) lSummary.get("qmatch" + (i + 1)));
                }
            }
        }
        return iDecoyThreshold;
    }

    /**
     * Returns the name of the filesystem of this Mascot dat file.
     *
     * @return String with the filename.
     */
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

    public void finish() {
        iMRP.clear();
    }

    /**
     * @{inheritDoc}
     */
    public Quantitation getQuantitation() {

        if (iQuantitation == null) {
            HashMap hm = iMRP.getSection("quantitation");
            String lQuantitationContent = (String) hm.get("XML");
            iQuantitation = new Quantitation(lQuantitationContent);
        }
        return iQuantitation;
    }

}
