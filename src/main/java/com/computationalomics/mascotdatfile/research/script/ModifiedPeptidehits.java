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

import com.computationalomics.mascotdatfile.util.interfaces.Modification;
import com.computationalomics.mascotdatfile.util.mascot.*;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <b>Created by IntelliJ IDEA.</b> User: Kenni Date: 4-jul-2006 Time: 14:47:29
 * <p/>
 * <br>This Class can read a datfile and will search for one or more modifications (args).</br>
 */
public class ModifiedPeptidehits {
    public static void main(String[] args) {
        if (args.length != 6 && args.length != 2) {
            printUsage();
        }

        // Read the params.
        String lDatfilePathAndFilename = args[0];

        // If option -m is triggered, print all the possible modifications to the printstream and system.exit(0);
        if (args[1].equals("-m")) {
            printPossibleModifications(lDatfilePathAndFilename);

            // if option -csv is triggered, tokenize the modifications and print all the peptidehits containing all the mods into a csv file.
        } else if (args[1].equals("-csv")) {
            String lTargetPath = args[2];
            String lTargetFilename = args[3];
            String lMods = args[4];
            double lConfidence = Double.parseDouble(args[5]);
            BufferedWriter bw = null;
            File lTargetPathFile = null;
            File lTarget = null;
            ArrayList lModsArray = new ArrayList();

            // Tokenize multiple mods into String[] (if there are more then one.)
            if (lMods.indexOf('|') == -1) {
                lModsArray.add(lMods);
            } else {
                StringTokenizer st = new StringTokenizer(lMods, "|");
                while (st.countTokens() > 0) {
                    lModsArray.add(st.nextToken());
                }
            }
            try {
                // The script will write the results into the target file. First make sure the file and directories are accessible.
                lTargetPathFile = new File(lTargetPath);
                lTargetPathFile.mkdirs();
                lTarget = new File(lTargetPathFile, lTargetFilename);
                lTarget.createNewFile();
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lTarget)));

                // Print the headings of the CSV file
                printCSVheaders(bw, lDatfilePathAndFilename);

                // Create a new MascotDatfile instance.
                MascotDatfile mdf = new MascotDatfile(lDatfilePathAndFilename);
                QueryToPeptideMap lQuery2P = mdf.getQueryToPeptideMap();
                for (int i = 0; i < lQuery2P.getNumberOfQueries(); i++) {

                    //Get the best peptideHit of Query (i+1)
                    PeptideHit lPeptidehit = (PeptideHit) lQuery2P.getPeptideHitOfOneQuery(i + 1);

                    //If peptideHit is null, no further information is written to the target file, else the script continues.
                    if (lPeptidehit != null) {
                        if (lPeptidehit.scoresAboveIdentityThreshold(0.05)) {
                            if (containsAllModifications(lPeptidehit, lModsArray)) {
                                printPeptideHitToFile(lPeptidehit, bw, i, lConfidence);
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException npe){
                    npe.printStackTrace();
                }
            }
        }
    }

    /**
     * This method checks if the PeptideHit contains all the modifications inside the aModsArray.
     *
     * @param aPeptideHit        - Peptidehit whose mods have to be checked.
     * @param aModsArray - Modifications that must be on the identification to set te boolean to true.
     * @return boolean - true(false) if the peptidehit does('nt) contain all the modifications.
     */
    private static boolean containsAllModifications(PeptideHit aPeptideHit, ArrayList aModsArray) {
        // This boolean will be true in the end, if all modifications are on the Peptidehit.
        boolean containsAllMods = true;

        // Get the modification list of this peptidehit.
        Modification[] lPHmods = aPeptideHit.getModifications();

        // this method will return true if all the modifications are set to true in the end.
        boolean[] boolModsArray = new boolean[aModsArray.size()];
        for (int i = 0; i < boolModsArray.length; i++) {
            boolModsArray[i] = false;
        }

        for (int j = 0; j < lPHmods.length; j++) {
            Modification lMod = lPHmods[j];
            if (lMod != null) {
                String lShortType = lMod.getShortType();
                for (int k = 0; k < aModsArray.size(); k++) {
                    if (lShortType.equals(aModsArray.get(k))) {
                        // This boolean[k] has to be set on true each time one of the modifications of aModsArray is matched.
                        boolModsArray[k] = true;
                    }
                }
            }
        }
        // If all the modifications are on the peptidehit, the booleans should should all have been set to true.
        for (int i = 0; i < boolModsArray.length; i++) {
            boolean aBoolean = boolModsArray[i];
            if (boolModsArray[i] == false) {
                containsAllMods = false;
                break;
            }
        }
        return containsAllMods;
    }

    /**
     * This method prints the usage to the standard printstream.
     */
    private static void printUsage() {
        String lMessage = "\"Usage:\tModifiedPeptideHits " +
                "<1. Path and filename from datfile> " +
                "<2. option> " +
                "<3. option related parameters>" +
                "\"\n\n" +
                "Options:" +
                "\n\t-m" +
                "\n\t---> Print all availlable modifications in the specified datfile.\n" +
                "\n\t-cvs <3a. Target path> <3b. Target File> <3c. modification1(|modification2|modification3)(short)> <3d. Confidence(alpha)>" +
                "\n\t---> Write all the peptidehits (confidence > 1-alpha) containing all the modification(s) in a csv file." +
                "\n\n" +
                "Example: \n\tModifiedPeptidehits C:\\mascot\\datfiles\\F010345.dat -csv C:\\target F013345_ace_modified.csv Ace 0.10";
        printMessage(lMessage);
        System.exit(0);
    }

    /**
     * This method will read the parametrical datfile, parse the possible modifications inside therein and print them into the errorstream.
     *
     * @param aDatfilePathAndFilename - String targetting datfile source.
     */
    private static void printPossibleModifications(String aDatfilePathAndFilename) {
        StringBuffer sb = new StringBuffer();
        MascotDatfile lMDF = new MascotDatfile(aDatfilePathAndFilename);
        ModificationList lModsList = lMDF.getModificationList();
        Vector lMods = null;

        // 1. print the fixed modifications.
        sb.append("**Fixed Modifications**");
        lMods = lModsList.getFixedModifications();
        for (int i = 0; i < lMods.size(); i++) {
            FixedModification fmod = (FixedModification) lMods.elementAt(i);
            sb.append("\n\t" + (i + 1) + ".  " + fmod.getShortType() + " (short) \t" + fmod.getType() + " " + fmod.getLocation() + " (full)");
        }

        sb.append("\n\n");

        //2. print the variable modifications.
        sb.append("**Variable Modifications**");
        lMods = lModsList.getVariableModifications();
        for (int i = 0; i < lMods.size(); i++) {
            VariableModification fmod = (VariableModification) lMods.elementAt(i);
            sb.append("\n\t" + (i + 1) + ".  " + fmod.getShortType() + " (short) \t" + fmod.getType() + " " + fmod.getLocation() + " (full)");
        }

        printMessage(sb.toString());
        System.exit(0);
    }

    /**
     * This method writes information about the csv values that will be printed.
     *
     * @param bw                      - BufferedWriter to the filestream
     * @param aDatfilePathAndFilename - datfile source
     * @throws IOException
     */
    private static void printCSVheaders(BufferedWriter bw, String aDatfilePathAndFilename) throws IOException {
        bw.write("ModifiedPeptidehits.java analysis of " + aDatfilePathAndFilename + ".");
        bw.newLine();
        bw.write("Query_ID;ModifiedPeptideSequence;PeptideSequence;PeptideLength;PeptideScore;PeptideThreshold");
        bw.newLine();
        bw.flush();
    }

    private static void printPeptideHitToFile(PeptideHit aPeptideHit, BufferedWriter bw, int aQueryNumber, double aConfidence) throws IOException {
        bw.write(
                "Query _ " + (aQueryNumber + 1) + ";" +
                        aPeptideHit.getModifiedSequence() + ";" +
                        aPeptideHit.getSequence() + ";" +
                        aPeptideHit.getSequence().length() + ";" +
                        aPeptideHit.getIonsScore() + ";" +
                        aPeptideHit.calculateIdentityThreshold(aConfidence));
        bw.newLine();
        bw.flush();
    }

    /**
     * This method prints messages to the standard outputstream.
     *
     * @param aMessage - the message that has to be written to the standard outputstream.
     */
    private static void printMessage(String aMessage) {
        System.out.println(aMessage);
    }

    /**
     * This method prints error messages to the errorstream.
     *
     * @param aError - the error that has to be written to the standard outputstream.
     */
    private static void printError(String aError) {
        System.err.println(aError);
        System.exit(0);
    }
}
