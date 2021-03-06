package com.compomics.mascotdatfile.util.io;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.interfaces.Modification;
import com.compomics.mascotdatfile.util.interfaces.QueryToPeptideMapInf;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;
import com.compomics.mascotdatfile.util.mascot.factory.MascotDatfileFactory;
import com.compomics.util.Util;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.Advocate;
import com.compomics.util.experiment.identification.matches.ModificationMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.io.identifications.IdfileReader;
import com.compomics.util.experiment.massspectrometry.Charge;
import com.compomics.util.experiment.massspectrometry.Spectrum;
import com.compomics.util.experiment.personalization.ExperimentObject;
import com.compomics.mascotdatfile.util.mascot.Query;
import com.compomics.util.experiment.biology.AminoAcidSequence;
import com.compomics.util.experiment.identification.identification_parameters.SearchParameters;
import com.compomics.util.experiment.identification.spectrum_assumptions.PeptideAssumption;
import com.compomics.util.preferences.SequenceMatchingPreferences;
import com.compomics.util.waiting.WaitingHandler;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 * This reader will import identifications from a Mascot dat file.
 * <p>
 * @author Marc Vaudel
 */
public class MascotIdfileReader extends ExperimentObject implements IdfileReader {

    /**
     * The inspected file.
     */
    private File inspectedFile;
    /**
     * Instance of the mascotdatfile parser.
     */
    private MascotDatfileInf iMascotDatfile;

    /**
     * Default constructor for the service loading mechanism.
     */
    public MascotIdfileReader() {

    }

    /**
     * Constructor for the MascotIdfilereader. Using the memory option for the
     * parser if the file is bigger than 1 GB.
     *
     * @param aFile a file to read
     */
    public MascotIdfileReader(File aFile) {
        this(aFile, aFile.length() > 1073741824);
    }

    /**
     * Constructor for the MascotIdilereader.
     *
     * @param aFile a file to read
     * @param index indicating whether the parsing shall be indexed or in memory
     */
    public MascotIdfileReader(File aFile, boolean index) {
        inspectedFile = aFile;
        try {
            if (index) {
                iMascotDatfile = MascotDatfileFactory.create(inspectedFile.getCanonicalPath(), MascotDatfileType.INDEX);
            } else {
                iMascotDatfile = MascotDatfileFactory.create(inspectedFile.getCanonicalPath(), MascotDatfileType.MEMORY);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Get the spectrum file name.
     *
     * @return the spectrum file name
     */
    public String getMgfFileName() {
        String temp = iMascotDatfile.getParametersSection().getFile();
        return Util.getFileName(temp);
    }

    /**
     * Getter for the file name.
     *
     * @return the file name
     */
    public String getFileName() {
        return iMascotDatfile.getFileName();
    }

    @Override
    public LinkedList<SpectrumMatch> getAllSpectrumMatches(WaitingHandler waitingHandler, SearchParameters searchParameters) 
            throws IOException, IllegalArgumentException, SQLException, ClassNotFoundException, InterruptedException, JAXBException {
        return getAllSpectrumMatches(waitingHandler, searchParameters, null, true);
    }

    @Override
    public LinkedList<SpectrumMatch> getAllSpectrumMatches(WaitingHandler waitingHandler, SearchParameters searchParameters, 
            SequenceMatchingPreferences sequenceMatchingPreferences, boolean expandAaCombinations) 
            throws IOException, IllegalArgumentException, SQLException, ClassNotFoundException, InterruptedException, JAXBException {

        LinkedList<SpectrumMatch> result = new LinkedList<SpectrumMatch>();

        String mgfFileName = getMgfFileName();

        QueryToPeptideMapInf lQueryToPeptideMap = iMascotDatfile.getQueryToPeptideMap();
        QueryToPeptideMapInf lDecoyQueryToPeptideMap = iMascotDatfile.getDecoyQueryToPeptideMap(false);

        int numberOfQueries = iMascotDatfile.getNumberOfQueries();

        if (waitingHandler != null) {
            waitingHandler.setMaxSecondaryProgressCounter(numberOfQueries);
        }

        for (int i = 1; i <= numberOfQueries; i++) {

            List<PeptideHit> mascotDecoyPeptideHits = null;
            try {
                mascotDecoyPeptideHits = lDecoyQueryToPeptideMap.getAllPeptideHits(i);
            } catch (Exception e) {
                // Looks like there is no decoy section
            }

            List<PeptideHit> mascotPeptideHits = lQueryToPeptideMap.getAllPeptideHits(i);

            // Get spectrum information
            PeptideHit testPeptide = null;

            if (mascotPeptideHits != null && !mascotPeptideHits.isEmpty()) {
                testPeptide = mascotPeptideHits.get(0);
            } else if (mascotDecoyPeptideHits != null && !mascotDecoyPeptideHits.isEmpty()) {
                testPeptide = mascotDecoyPeptideHits.get(0);
            }

            if (testPeptide != null) {
                Query tempQuery = iMascotDatfile.getQuery(i);

                String tempName = i + "";
                if (tempQuery.getTitle() != null
                        && !tempQuery.getTitle().startsWith("No title (Query ")) {
                    tempName = tempQuery.getTitle();
                }

                String spectrumId = fixMgfTitle(tempName);
                String measuredCharge = tempQuery.getChargeString();
                String sign = String.valueOf(measuredCharge.charAt(1));
                Charge charge;

                if (measuredCharge.equalsIgnoreCase("Mr")) { // @TODO: figure out what 'Mr' means...
                    charge = null;
                } else {
                    if (sign.compareTo("+") == 0) {
                        charge = new Charge(Charge.PLUS, Integer.valueOf(measuredCharge.substring(0, 1)));
                    } else {
                        charge = new Charge(Charge.MINUS, Integer.valueOf(measuredCharge.substring(0, 1)));
                    }
                }

                SpectrumMatch currentMatch = new SpectrumMatch(Spectrum.getSpectrumKey(mgfFileName, spectrumId));
                currentMatch.setSpectrumNumber(i); //@TODO: set the spectrum index instead
                HashMap<Double, ArrayList<PeptideHit>> hitMap = new HashMap<Double, ArrayList<PeptideHit>>();

                // Get all hits
                if (mascotPeptideHits != null) {
                    for (PeptideHit peptideHit : mascotPeptideHits) {
                        if (!hitMap.containsKey(peptideHit.getExpectancy())) {
                            hitMap.put(peptideHit.getExpectancy(), new ArrayList<PeptideHit>());
                        }
                        hitMap.get(peptideHit.getExpectancy()).add(peptideHit);
                    }
                }

                if (mascotDecoyPeptideHits != null) {
                    for (PeptideHit peptideHit : mascotDecoyPeptideHits) {
                        if (!hitMap.containsKey(peptideHit.getExpectancy())) {
                            hitMap.put(peptideHit.getExpectancy(), new ArrayList<PeptideHit>());
                        }
                        hitMap.get(peptideHit.getExpectancy()).add(peptideHit);
                    }
                }

                ArrayList<Double> eValues = new ArrayList<Double>(hitMap.keySet());
                Collections.sort(eValues);
                int rank = 1;

                for (Double eValue : eValues) {
                    for (PeptideHit peptideHit : hitMap.get(eValue)) {
                        PeptideAssumption peptideAssumption = getPeptideAssumption(peptideHit, charge, rank, sequenceMatchingPreferences);
                        if (expandAaCombinations && AminoAcidSequence.hasCombination(peptideAssumption.getPeptide().getSequence())) {
                            Peptide peptide = peptideAssumption.getPeptide();
                            ArrayList<ModificationMatch> modificationMatches = peptide.getModificationMatches();
                            for (StringBuilder expandedSequence : AminoAcidSequence.getCombinations(peptide.getSequence())) {
                                Peptide newPeptide = new Peptide(expandedSequence.toString(), new ArrayList<ModificationMatch>(modificationMatches.size()));
                                for (ModificationMatch modificationMatch : modificationMatches) {
                                    newPeptide.addModificationMatch(new ModificationMatch(modificationMatch.getTheoreticPtm(), modificationMatch.isVariable(), modificationMatch.getModificationSite()));
                                }
                                PeptideAssumption newAssumption = new PeptideAssumption(newPeptide, peptideAssumption.getRank(), peptideAssumption.getAdvocate(), peptideAssumption.getIdentificationCharge(), peptideAssumption.getScore(), peptideAssumption.getIdentificationFile());
                                currentMatch.addHit(Advocate.mascot.getIndex(), newAssumption, false);
                            }
                        } else {
                            currentMatch.addHit(Advocate.mascot.getIndex(), peptideAssumption, false);
                        }
                    }
                    rank += hitMap.get(eValue).size();
                }

                result.add(currentMatch);
            }

            if (waitingHandler != null) {
                if (waitingHandler.isRunCanceled()) {
                    break;
                }
                waitingHandler.setSecondaryProgressCounter(i);
            }
        }

        return result;
    }

    /**
     * Parses a peptide assumption out of a peptideHit (this is a separated
     * function because in the good old times I used to parse the target and
     * decoy sections separately. Now, for the sake of search engine
     * compatibility the decoy option should be disabled.)
     *
     * @param aPeptideHit the peptide hit to parse
     * @param charge the corresponding charge
     * @param rank the rank of the peptideHit
     * @param sequenceMatchingPreferences the sequence matching preferences to
     * use to fill the secondary maps
     *
     * @return a peptide assumption
     */
    private PeptideAssumption getPeptideAssumption(PeptideHit aPeptideHit, Charge charge, int rank, SequenceMatchingPreferences sequenceMatchingPreferences) {

        ArrayList<ModificationMatch> foundModifications = new ArrayList();
        String peptideSequence = aPeptideHit.getSequence();
        int peptideSequenceLength = peptideSequence.length();
        int numberOfModifications = aPeptideHit.getModifications().length;

        int modificationSite;

        for (int l = 0; l < numberOfModifications; l++) {

            if (l == 0) {
                modificationSite = 1;
            } else if (l > peptideSequenceLength) {
                modificationSite = peptideSequenceLength;
            } else {
                modificationSite = l;
            }

            Modification handledModification = aPeptideHit.getModifications()[l];

            if (handledModification != null) {
                // the modification is named mass@residue for later identification
                foundModifications.add(new ModificationMatch(handledModification.getMass() + "@"
                        + peptideSequence.charAt(modificationSite - 1),
                        !handledModification.isFixed(), modificationSite));
            }
        }

        Peptide peptide = new Peptide(peptideSequence, foundModifications);

        PeptideAssumption currentAssumption = new PeptideAssumption(peptide,
                rank, Advocate.mascot.getIndex(), charge, aPeptideHit.getExpectancy(), getFileName());
        currentAssumption.setRawScore(aPeptideHit.getIonsScore());

        return currentAssumption;
    }

    /**
     * Returns the fixed mgf title.
     *
     * @param spectrumTitle
     * @return the fixed mgf title
     */
    private String fixMgfTitle(String spectrumTitle) {

        // a special fix for mgf files with titles containing url encoding, e.g.: %3b instead of ;
        try {
            spectrumTitle = URLDecoder.decode(spectrumTitle, "utf-8"); // @TODO: only needed for mascot???
        } catch (UnsupportedEncodingException e) {
            System.out.println("An exception was thrown when trying to decode an mgf tile!");
            e.printStackTrace();
        }

        // a special fix for mgf files with titles containing \\ instead of \
        //spectrumTitle = spectrumTitle.replaceAll("\\\\\\\\", "\\\\");  // @TODO: only needed for OMSSA???
        return spectrumTitle;
    }

    @Override
    public void close() throws IOException {
        iMascotDatfile.finish();
        iMascotDatfile = null;
    }

    @Override
    public String getExtension() {
        return ".dat";
    }

    @Override
    public HashMap<String, ArrayList<String>> getSoftwareVersions() {
        HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
        ArrayList<String> versions = new ArrayList<String>();
        versions.add(iMascotDatfile.getHeaderSection().getVersion());
        result.put("Mascot", versions);
        return result;
    }

    @Override
    public boolean hasDeNovoTags() {
        return false;
    }
}
