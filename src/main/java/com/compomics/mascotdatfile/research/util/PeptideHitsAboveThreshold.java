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

package com.compomics.mascotdatfile.research.util;

import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.mascot.MascotDatfile;
import com.compomics.mascotdatfile.util.mascot.PeptideHit;
import com.compomics.mascotdatfile.util.mascot.QueryToPeptideMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 4-mrt-2006 Time: 11:36:42
 */
public class PeptideHitsAboveThreshold {
    // Class specific log4j logger for PeptideHitsAboveThreshold instances.
    private static Logger logger = Logger.getLogger(PeptideHitsAboveThreshold.class);

    /**
     * Method Input is a QueryToPeptideMap, it calculates all the peptideHits of the Map that are above the Threshold.
     * All the aOutput is written to a Parameter File
     *
     * @param aQueryTP QueryToPeptideMap
     * @param aOutput  aOutput of the datastream
     */
    public static void calculateAllPeptideHitsAboveThreshold(QueryToPeptideMap aQueryTP, File aOutput) {

        int lNumberOfQueries = aQueryTP.getPeptideMap().size();
        int lPeptideHitsAboveIdentityCount = 0;
        int lPeptideHitsCount = 0;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(aOutput));
            bw.write("The following is a list of identified PeptideHits in a QueryToPeptideMap:\n Each Query that has PeptideHits is listed.\n");
            bw.newLine();
            for (int i = 1; i < lNumberOfQueries + 1; i++) {
                Vector lPeptideHits = aQueryTP.getAllPeptideHits(i);
                if (lPeptideHits.get(0) == null) {
                    continue;
                }
                bw.write("\nQuery " + i + " has identified " + lPeptideHits.size() + " PeptideHits:");
                lPeptideHitsCount += lPeptideHits.size();
                PeptideHit lPh = null;
                for (int j = 0; j < lPeptideHits.size(); j++) {
                    lPh = (PeptideHit) lPeptideHits.get(j);
                    if (lPh.scoresAboveIdentityThreshold(0.01)) {
                        bw.write("\n" + (j + 1) + ".\t" + lPh.getModifiedSequence());
                        lPeptideHitsAboveIdentityCount++;
                    }
                }
                bw.flush();
            }
            bw.newLine();
            bw.write("There were " + lPeptideHitsCount + " PeptideHits in " + lNumberOfQueries + " Queries.");
            bw.newLine();
            bw.write(lPeptideHitsAboveIdentityCount + " PeptideHits were above the threshold. (" + ((((double) lPeptideHitsAboveIdentityCount) / lPeptideHitsCount) * 100));
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use only for a quick and dirty run to get the peptidehits above threshold.
     *
     * @param args
     */
    public static void main(String[] args) {
        DatfileLocation dfl = null;
        MascotDatfile mdf = null;
        File file = new File("C:\\temp\\PeptideHitsAboveThreshold.txt");
        int lQueryNumber = Integer.parseInt(args[2]);
        int lPeptideHitNumber = Integer.parseInt(args[3]);
        // Get the datfile from different sources, the DatfileLocation Class guides the process based on the args[0] parameter.
        int lDatfileLocationType = Integer.parseInt(args[0]);
        try {
            Label:
            if (lDatfileLocationType == DatfileLocation.HARDDISK) {
                {
                    dfl = new DatfileLocation(DatfileLocation.HARDDISK, new String[]{args[1]});
                    mdf = dfl.getDatfile();
                    break Label;
                }
            } else if (lDatfileLocationType == DatfileLocation.URL) {
                {
                    dfl = new DatfileLocation(DatfileLocation.URL, new String[]{args[4], args[5], args[1]});
                    mdf = dfl.getDatfile();
                    break Label;
                }
            }
            System.out.println("Datfile received.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  // print ${Exception} information.
        } catch (InstantiationException e) {
            e.printStackTrace();  // print ${Exception} information.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  // print ${Exception} information.
        } catch (SQLException e) {
            e.printStackTrace();  // print ${Exception} information.
        }
        QueryToPeptideMap lQueryTP = mdf.getQueryToPeptideMap();
        calculateBestPeptideHitAboveThreshold(lQueryTP, file);

    }

    /**
     * Method Input is a QueryToPeptideMap, it calculates all the peptideHits of the Map that are above the Threshold.
     * All the aOutput is written to a Parameter File
     *
     * @param aQueryTP QueryToPeptideMap
     * @param aOutput  aOutput of the datastream
     */
    public static void calculateBestPeptideHitAboveThreshold(QueryToPeptideMap aQueryTP, File aOutput) {

        int lNumberOfQueries = aQueryTP.getPeptideMap().size();
        int lPeptideHitsAboveIdentityCount = 0;
        int lPeptideHitsCount = 0;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(aOutput));
            bw.write("The following is a list of identified PeptideHits in a QueryToPeptideMap:\n Each Query that has a PeptideHit above the threshold is listed followed by it's greatest scoring PeptideHit.");
            bw.newLine();
            PeptideHit lPh = null;
            for (int i = 1; i < lNumberOfQueries + 1; i++) {
                Vector lPeptideHits = aQueryTP.getAllPeptideHits(i);
                if (lPeptideHits.get(0) == null) {
                    continue;
                }
                lPh = (PeptideHit) lPeptideHits.get(0);
                if (lPh.scoresAboveIdentityThreshold(0.05)) {
                    bw.newLine();
                    bw.write(lPh.getModifiedSequence() + "," + i);
                    lPeptideHitsAboveIdentityCount++;
                }
                bw.flush();
            }
            bw.newLine();
            bw.write("There were " + lNumberOfQueries + " Queries.");
            bw.newLine();
            bw.write(lPeptideHitsAboveIdentityCount + " PeptideHits were above the threshold. (" + ((((double) lPeptideHitsAboveIdentityCount) / lPeptideHitsCount) * 100) + " percent of the Queries delivered a Peptide Identification.");
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

