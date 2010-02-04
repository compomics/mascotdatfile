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

package com.computationalomics.mascotdatfile.research.script;

import com.computationalomics.mascotdatfile.util.interfaces.FragmentIon;
import com.computationalomics.mascotdatfile.util.mascot.*;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;
/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 6-apr-2006
 * Time: 15:43:11
 */

/**
 * 'The Research goals of this script are:'
 *  Print all the peptidehits above a parametrical threshold into a CSV-file.
 */

public class SequenceCoverage {
    public static void main(String[] args) {
        if(args == null || args.length != 4){
            printUsage();
        }
        // Read the params.
        String lDatfilePathAndFilename = args[0];
        String lTargetPath = args[1];
        String lTargetFilename = args[2];
        double lIDentityThreshold = Double.parseDouble(args[3]);
        BufferedWriter bw = null;

        try {
            // The script will write the results into the target file. First make sure the file and directories are accessible.
            File lTargetPathFile = new File(lTargetPath);
            lTargetPathFile.mkdirs();
            File lTarget = new File(lTargetPathFile, lTargetFilename);
            lTarget.createNewFile();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lTarget)));
            printCSVheaders(bw, lDatfilePathAndFilename);

            // Create a new MascotDatfile instance from the args.
            MascotDatfile mdf = new MascotDatfile(lDatfilePathAndFilename);
            QueryToPeptideMap lQuery2P = mdf.getQueryToPeptideMap();
            Vector lQueries = mdf.getQueryList();
            for(int i = 0; i < lQuery2P.getNumberOfQueries(); i++) {

                //Get all the best peptideHit of Query (i+1)
                PeptideHit ph = (PeptideHit) lQuery2P.getPeptideHitOfOneQuery(i + 1);

                //If no peptideHit is null, no further information is written to the target file, else the script continues.
                if(ph != null) {
                     if(ph.scoresAboveIdentityThreshold(lIDentityThreshold)){
                        PeptideHitAnnotation lPha = ph.getPeptideHitAnnotation(mdf.getMasses(), mdf.getParametersSection());
                        Query q = (Query)lQueries.get(i);

                        // 1.a) Get a vector with Fragmentions that were matched by the fused matching method(see javadoc).
                        Vector lFM = lPha.getFusedMatchedIons(q.getPeakList(), ph.getPeaksUsedFromIons1(), q.getMaxIntensity(), 0.1);
                        // 1.b) Analyse the fused matches against the sequence to calculate sequence coverage of this peptidehit.
                        int[] lFusedCoverage  = getCoverage(lFM,ph.getSequence().length());

                        // 2.a) Get a vector with Fragmentions that were matched by the Mascot matching method(see javadoc)
                        lFM = lPha.getMatchedIonsByMascot(q.getPeakList(), ph.getPeaksUsedFromIons1());
                        // 2.b) Analyse the mascot matches against the sequence to calculate sequence coverage of this peptidehit.
                        int[] lMascotCoverage  = getCoverage(lFM,ph.getSequence().length());

                        bw.write(
                                "Query _ "  + (i + 1) + ";" +
                                ph.getModifiedSequence() + ";" +
                                ph.getSequence() + ";" +
                                ph.getSequence().length() + ";" +
                                ph.getIonsScore() + ";" +
                                ph.calculateIdentityThreshold(lIDentityThreshold) + ";" +
                                lMascotCoverage[0] + ";" +
                                lMascotCoverage[1] + ";" +
                                lMascotCoverage[2] + ";" +
                                lFusedCoverage[0] + ";" +
                                lFusedCoverage[1] + ";" +
                                lFusedCoverage[2] + ";");
                        bw.newLine();
                        bw.flush();
                    }
                }
            }
            bw.flush();
            bw.close();

        } catch(FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch(IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * This method calculates ioncoverage of a PeptideSequence and matched fragmentions.
     * @param aFM - A Vector with fragmentions that were matched.
     * @param aLength - The Peptide's length
     * @return int[] with the distinct number fragmentions that covered the sequence. <br />
     *              <b>[0]</b> number of b-ions covering the peptide's sequence.<br />
     *              <b>[1]</b> number of y-ions covering the peptide's sequence.<br />
     *              <b>[2]</b> number of b- and -ions covering the peptide's sequence.
     */
    private static int[] getCoverage(Vector aFM,int aLength){
        /**
         * This int array will return the distinct number of fragmentions.
         * [0] - b
         * [1] - y
         * [2] - all
         */
        int[] lCoverage = new int[3];
        int[] lB = new int[aLength-1];
        int[] lY = new int[aLength-1];
        int[] lAll = new int[aLength];
        Iterator iterator = aFM.iterator();
        while(iterator.hasNext()) {
            FragmentIon fm = (FragmentIon) iterator.next();
            int lNumber = fm.getNumber()-1;

            if(0 < fm.getID() && fm.getID()< 7){
            //b-ion and its associated ions.
                if(lB[lNumber] == 0){
                    lB[lNumber] = 1;
                }
                if(lAll[lNumber] == 0){
                    lAll[lNumber] = 1;
                }
            }else if(12 < fm.getID() && fm.getID()< 19){
            //a-ion and its associated ions.(b-derivated!)
                if(lB[lNumber] == 0){
                    lB[lNumber] = 1;
                }
                if(lAll[lNumber] == 0){
                    lAll[lNumber] = 1;
                }
            }else if(6 < fm.getID() && fm.getID()< 13){
            //y-ion and its associated ions.
                if(lY[lNumber] == 0){
                    lY[lNumber] = 1;
                }
                if(lAll[aLength-(lNumber+1)] == 0){
                    lAll[aLength-(lNumber+1)] = 1;
                }
            }else if(20 < fm.getID() && fm.getID()< 23){
            //c and c++
                if(lB[lNumber] == 0){
                    lB[lNumber] = 1;
                }
                if(lAll[lNumber] == 0){
                    lAll[lNumber] = 1;
                }
            }else if(18 < fm.getID() && fm.getID()< 21){
             //x and x++
                if(lY[lNumber] == 0){
                    lY[lNumber] = 1;
                }
                if(lAll[aLength-(lNumber+1)] == 0){
                    lAll[aLength-(lNumber+1)] = 1;
                }
            }else if(22 < fm.getID() && fm.getID()< 25){
             //z and z++
                if(lY[lNumber] == 0){
                    lY[lNumber] = 1;
                }
                if(lAll[aLength-(lNumber+1)] == 0){
                    lAll[aLength-(lNumber+1)] = 1;
                }
            }

        }
        lCoverage[0] = getDistinctNumber(lB);
        lCoverage[1] = getDistinctNumber(lY);
        lCoverage[2] = getDistinctNumber(lAll);
        return lCoverage;

    }

    private static int getDistinctNumber(int[] aCoverage){
        int lCount = 0;
        for(int i = 0; i < aCoverage.length; i++) {
            if(aCoverage[i] == 1){
                lCount++;
            }
        }
        return lCount;
    }

    /**
     * This method prints the usage to the errorstream.
     */
    private static void printUsage(){
        String lMessage = "\"Usage:\tSequenceCoverage <1. Path and filename from datfile> " +
                "<2. Target path> <3. Target filename> <4. Confidence (alpha)> \"\n" +
                "Example:\n\t\tSequenceCoverage C:\\mascot\\datfiles\\F010345.dat C:\\target F010345.csv 0.05\n" +
                "Will run the SequenCoverage script on F010345.dat and write the peptide identifications scoring above " +
                "their 95% (1-0.05) identitythreshold into the F010345.csv file in the C:\\target directory.";
        printError(lMessage);
    }

    /**
     * This method prints error messages to the errorstream.
     */
    private static void printError(String aMessage){
        System.err.println(aMessage);
        System.exit(0);
    }

    /**
     * This method writes information about the csv values that will be printed.
     */
    private static void printCSVheaders(BufferedWriter bw, String aDatfilePathAndFilename) throws IOException{
        bw.write("SequenceCoverage.java analysis of " + aDatfilePathAndFilename + ".");
        bw.newLine();
        bw.write("Query_ID;ModifiedPeptideSequence;PeptideSequence;PeptideLength;PeptideScore;PeptideThreshold;MASCOT_b-covering ions;MASCOT_y-covering ions;MASCOT_all-covering ions;FUSED_b-covering ions;FUSED_y-covering ions;FUSED_all-covering ions");
        bw.newLine();
        bw.flush();
    }
}
