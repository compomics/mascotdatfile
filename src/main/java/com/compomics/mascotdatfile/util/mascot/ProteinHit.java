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

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.StringTokenizer; /**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 28-feb-2006
 * Time: 14:03:52
 */

/**
 * An instance of this Class contains information about a ProteinHit (=origin of the Peptide). <b>NOTE:</b>The protein
 * description can be requested on the top-level MascotDatfile _ getProteinDiscritptionByAccessionNumber.
 */
public class ProteinHit implements Serializable {
    // Class specific log4j logger for ProteinHit instances.
    private static Logger logger = Logger.getLogger(ProteinHit.class);

    public ProteinHit(String aPeptideHit_prot) {
        parsePeptideHit_protString(aPeptideHit_prot);

    }

    /**
     * The identification of a protein hit. It is the accession number of the protein from the DB.
     */
    private String iAccession = null;
    /**
     * This String presents the frame number, if mascot has been used to process genomic data.
     */
    private int iFrameNumber = 0;
    /**
     * This String presents the START position of the PeptideHit in the database
     */
    private int iStart = 0;
    /**
     * This String presents the STOP position of the PeptideHit in the database
     */
    private int iStop = 0;
    /**
     * This String presents the multiplicity of the Peptidehit (REDUNDANT)
     */
    private int iMultiplicity = 0;

    /**
     * Method Parse the PeptideHit_protString into separate insctance variables of 'this' object.
     *
     * @param aProt String with proteinHit info. (ex: ["Q16363 (1694-1723)":0:1:30:1])
     */
    private void parsePeptideHit_protString(String aProt) {

        // 1. Extract the accession in between quotes.
        int lQuote1 = aProt.indexOf('"');
        int lQuote2 = aProt.indexOf('"', lQuote1 + 1);

        if (lQuote1 < 0 || lQuote2 < 0) {
            throw new IllegalArgumentException("ProteinHit accession not found. The protein             instance could not be created. " + aProt);
        }

        iAccession = aProt.substring(lQuote1 + 1, lQuote2);

        // 2.Strip the rest of the String and splice in different Tokens.
        aProt = aProt.substring(lQuote2 + 2);
        StringTokenizer st = new StringTokenizer(aProt, ":");

        if (st.countTokens() != 4) {
            throw new IllegalArgumentException("Illegal amount of tokens(" + st.countTokens() + ") to create the ProteinHit instance. There must be 5 tokens. " + aProt);
        }

        // 3.OK! now there are 5 tokens, read them out.
        // 3.a) FrameNumber.
        iFrameNumber = Integer.parseInt(st.nextToken());
        // 3.b) Start
        iStart = Integer.parseInt(st.nextToken());
        // 3.c) Stop
        iStop = Integer.parseInt(st.nextToken());
        // 3.d) Multiplicity
        iMultiplicity = Integer.parseInt(st.nextToken());
    }

    /**
     * Returns the identification of a protein hit. It is the accession number of the protein from the DB.
     *
     * @return the identification of a protein hit. It is the accession number of the protein from the DB.
     */
    public String getAccession() {
        return iAccession;
    }

    /**
     * Sets the identification of a protein hit. It is the accession number of the protein from the DB.
     *
     * @param aAccession the identification of a protein hit. It is the accession number of the protein from the DB.
     */
    public void setAccession(String aAccession) {
        iAccession = aAccession;
    }

    /**
     * Returns this String presents the frame number, if mascot has been used to process genomic data.
     *
     * @return this String presents the frame number, if mascot has been used to process genomic data.
     */
    public int getFrameNumber() {
        return iFrameNumber;
    }

    /**
     * Sets this String presents the frame number, if mascot has been used to process genomic data.
     *
     * @param aFrameNumber this String presents the frame number, if mascot has been used to process genomic data.
     */
    public void setFrameNumber(int aFrameNumber) {
        iFrameNumber = aFrameNumber;
    }

    /**
     * Returns this String presents the START position of the PeptideHit in the database
     *
     * @return this String presents the START position of the PeptideHit in the database
     */
    public int getStart() {
        return iStart;
    }

    /**
     * Sets this String presents the START position of the PeptideHit in the database
     *
     * @param aStart this String presents the START position of the PeptideHit in the database
     */
    public void setStart(int aStart) {
        iStart = aStart;
    }

    /**
     * Returns this String presents the STOP position of the PeptideHit in the database
     *
     * @return this String presents the STOP position of the PeptideHit in the database
     */
    public int getStop() {
        return iStop;
    }

    /**
     * Sets this String presents the STOP position of the PeptideHit in the database
     *
     * @param aStop this String presents the STOP position of the PeptideHit in the database
     */
    public void setStop(int aStop) {
        iStop = aStop;
    }

    /**
     * Returns this String presents the multiplicity of the Peptidehit
     *
     * @return this String presents the multiplicity of the Peptidehit
     */
    public int getMultiplicity() {
        return iMultiplicity;

    }

    /**
     * Sets this String presents the multiplicity of the Peptidehit
     *
     * @param aMultiplicity this String presents the multiplicity of the Peptidehit
     */
    public void setMultiplicity(int aMultiplicity) {
        iMultiplicity = aMultiplicity;
    }

    /**
     * This method should only be used of the Protein accession comes from a PeptideCentric database as can be generated
     * with DBToolkit. (http://genesis.ugent.be/dbtoolkit/) These databases contain peptides, so being truncated
     * proteins, as fasta entries. Example: >sw|Q9P1Y5 (716-726)|K1543_HUMAN Protein KIAA1543. DMQRLTDQQQR
     * <p/>
     * Is a fasta entry in the truncated database. The Sequence is the Peptide from 716-726 in HUMAN Protein KIAA1543 -
     * Q9P1Y5. Mascot takes these as protein entries, and the protein hits will have a start site of 1 if this peptide
     * is identified. This method will return 716 as the correct start site.
     *
     * @return int Start position of the peptide in a protein when working with PeptideCentric databases.
     */
    public int getPeptideStartInProtein_PeptideCentricDatabase() {
        // "CG5625-PA (4-22)"
        String result = null;
        int lStart = iAccession.indexOf('(') + 1;
        int lStop = iAccession.lastIndexOf('-');
        // In case this method is called to a proteinhit thats not comming from a peptidecentric database, it will return the instance stop value.
        if (lStart == -1 || lStop == -1) {
            result = "" + iStart;
        } else {
            result = (iAccession.substring(lStart, lStop));
        }
        return Integer.parseInt(result);
    }


    /**
     * This method should only be used of the Protein accession comes from a PeptideCentric database as can be generated
     * with DBToolkit. (http://genesis.ugent.be/dbtoolkit/) These databases contain peptides, so being truncated
     * proteins, as fasta entries. Example: >sw|Q9P1Y5 (716-726)|K1543_HUMAN Protein KIAA1543. DMQRLTDQQQR
     * <p/>
     * Is a fasta entry in the truncated database. The Sequence is the Peptide from 716-726 in HUMAN Protein KIAA1543 -
     * Q9P1Y5. Mascot takes these as protein entries, and the protein hits will have a stop site of 10 if this peptide
     * is identified. This method will return 726 as the correct stop site.
     *
     * @return int Stop position of the peptide in a protein when working with PeptideCentric databases.
     */
    public int getPeptideStopInProtein_PeptideCentricDatabase() {
        // "P20591 (31-49)"
        String result = null;
        int lStart = iAccession.indexOf('-') + 1;
        int lStop = iAccession.indexOf(')');
        // In case this method is called to a proteinhit thats not comming from a peptidecentric database, it will return the instance stop value.
        if (lStart == -1 || lStop == -1) {
            result = "" + iStop;
        } else {
            result = (iAccession.substring(lStart, lStop));
        }
        return Integer.parseInt(result);
    }
}
