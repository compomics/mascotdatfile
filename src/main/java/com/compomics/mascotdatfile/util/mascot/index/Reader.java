package com.compomics.mascotdatfile.util.mascot.index;

import org.apache.log4j.Logger;

import javax.tools.JavaCompiler;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 25-jun-2008 Time: 12:23:42 To change this template use File | Settings |
 * File Templates.
 */
public class Reader {
    // Class specific log4j logger for Reader instances.
    private static Logger logger = Logger.getLogger(Reader.class);

    private int iByteLengthStatus = -1;
    /**
     * The file that will be read.
     */
    private File iFile = null;

    /**
     * A reference to the controlling Mediator.
     */
    private Controller iController = null;

    /**
     * This directory will specifically hold temporary Mascot result files.
     */
    private static File iTempFolder = null;

    /**
     * The name for the temporary file.
     */
    private String lTempFileName = "Reader";

    /**
     * The Reader's RandomAccesFile.
     */
    private RandomAccessFile raf = null;

    private TreeSet<Integer> iLineNumbersToInclude = new TreeSet<Integer>();
    /**
     * Instance variables for the algorthm. Two long counters for lines an bytes of the file, a boolean watching for
     * boundaries in the file and a byte[] indexing the bytes that are used for a new line.
     */

    private Integer iLineCount = 0;
    private Long iByteCount = 0l;
    private long iSectionEndByte = -1l;
    private long iSectionStartByte = -1l;
    private String iSectionName = "";
    private boolean isIndexingLines = false;

    private byte[] iLineSeparator;
    private boolean iTempFileNeeded = false;
    private OutputStream fos = null;

    /**
     * Construct a Reader for a Mascot results file that is located at the local filesystem.
     *
     * @param aFileName   String to the Mascot result file.
     * @param aController Controller instance steering the Reader.
     */
    public Reader(String aFileName, Controller aController) {
        this(new File(aFileName), aController);
    }

    /**
     * Construct a Reader for a Mascot results file that is located at the local filesystem.
     *
     * @param aFile       File handle to the Mascot result file.
     * @param aController Controller instance steering the reader.
     */
    public Reader(final File aFile, Controller aController) {
        iController = aController;
        iFile = aFile;
        try {
            construct(new BufferedReader(new InputStreamReader(new FileInputStream(iFile))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct a Reader for a Mascot results file that is streamed by a buffered reader.
     *
     * @param aBufferedReader BufferedReader streaming the Mascot result file.
     * @param aController     Controller instance steering the Reader.
     */
    public Reader(BufferedReader aBufferedReader, Controller aController) {
        iController = aController;
        iTempFileNeeded = true;
        construct(aBufferedReader);
    }

    /**
     * This private method is used by the constructor. First, the Mascot result file is indexed by bytes upon a single
     * reading. Second, a random acces file is created using this index.
     *
     * @param aBufferedReader BufferedReader to the Mascot result file.
     */
    private void construct(BufferedReader aBufferedReader) {

        try {

            // 1. If the file is not local, we index it local to create the Random Access File.
            //          Therefore, a FileOutputStream writes every read byte into a temp file.
            if (iTempFileNeeded) {
                iFile = File.createTempFile(lTempFileName + System.currentTimeMillis(), ".tmp", getTempDirectory());
                iFile.deleteOnExit();
                fos = new BufferedOutputStream(new FileOutputStream(iFile));
            }

            // 2. Then we must find out how many bytes are used for a line feed.
            // As this differes in MacOSX, Windows and UNIX systems, this must be exact identical
            // to get the byte indexing right.
            // For each byte that is read, the byte counter increases and the byte is optionally written
            // into the fileoutputstream.
            //checking happens until we hit the end of the line containing the mascot header to make sure the correct byte is used for the rest of the file
            //from there a check happens if the newline is windows (\r\n) linux or OSX (\n) or old apples (\r)
            Integer lCharacter = -1;
            ArrayList testarray = new ArrayList<String>();
            ArrayList<Integer> lNewLineCharacters = new ArrayList<Integer>();
            boolean newlinefound = false;
            while ((lCharacter = aBufferedReader.read()) != -1) {
                while (!newlinefound) {
                    lCharacter = aBufferedReader.read();
                    iByteCount++;
                    if (iTempFileNeeded) {
                        fos.write(lCharacter);
                    }
                    Object temp = ((char) lCharacter.byteValue());
                    testarray.add(temp);
                    if (lCharacter == '\n') {
                        String tweedetestarray = testarray.toString();
                        tweedetestarray = tweedetestarray.replaceAll(",", "");
                        if (tweedetestarray.contains("M a s c o t")) {
                            lNewLineCharacters.add(lCharacter);
                            newlinefound = true;
                        } else {
                            testarray.clear();
                        }
                    } else if (lCharacter == '\r') {
                        String tweedetestarray = testarray.toString();
                        tweedetestarray = tweedetestarray.replaceAll(",", "");
                        if (tweedetestarray.contains("M a s c o t")) {
                            lCharacter = aBufferedReader.read();
                            iByteCount++;
                            if (iTempFileNeeded) {
                                fos.write(lCharacter);
                            }
                            if (lCharacter == '\n') {
                                lNewLineCharacters.add(13);
                                lNewLineCharacters.add(10);
                                newlinefound = true;
                            } else {
                                lNewLineCharacters.add(13);
                            }
                        } else {
                            testarray.clear();
                        }
                    }
                }
                     // OK, now we are at the end of the line -  we now how
                     // lines are separated in the file.
                // Now make this functional and break the byte reading as to proceed to line reading.
                iLineSeparator = new byte[lNewLineCharacters.size()];
                for (int i = 0; i < lNewLineCharacters.size(); i++) {
                    byte b = lNewLineCharacters.get(i).byteValue();
                    iLineSeparator[i] = b;
                }
                iLineCount++;
                // No use to index the first line..?
                // iController.addLineIndex(iLineCount, 0l);
                break;
            }
            // 3. LineIndex reading is needed as to make the indexing feasable.
            // Now the buffered reader continues reading line by line.

            String lLine = "";
            boolean lSectionFinished = false;

            while ((lLine = aBufferedReader.readLine()) != null) {
                lineReadFromBufferedReader(lLine);
                //b) index section lines!
                if (lLine.startsWith("--") || lSectionFinished) {
                    if (lSectionFinished) {
                        lSectionFinished = false;
                    } else {
                        processSectionBoundary(aBufferedReader, lLine);
                    }
                    if (iSectionName.equals("peptides")) {
                        // Commence the peptide sections, this must be indexed in more detail.
                        processPeptideSection(aBufferedReader);
                        // The method above breaks when another boundary is reached,
                        // therefore the BufferedReader is passed to a standard processSectionBoundary.
                        lSectionFinished = true;

                    } else if (iSectionName.equals("summary")) {
                        // Commence the summary section, this must be indexed in more detail.
                        initSummaryIndex(aBufferedReader, SummaryIndex.getInstance());
                    } else if (iSectionName.equals("decoy_peptides")) {
                        // Commence the peptide sections, this must be indexed in more detail.
                        processDecoyPeptideSection(aBufferedReader);
                        // The method above breaks when another boundary is reached,
                        // therefore the BufferedReader is passed to a standard processSectionBoundary.
                        lSectionFinished = true;
                    } else if (iSectionName.equals("decoy_summary")) {
                        // Commence the summary section, this must be indexed in more detail.
                        initSummaryIndex(aBufferedReader, SummaryIndex.getDecoyInstance());
                    }

                }
            }

            // Close the BufferedReader and the FileOutputStream.
            aBufferedReader.close();
            if (iTempFileNeeded) {
                fos.flush();
                fos.close();
            }
            // As from now, the Mascot Result File will be accesed by a Random Acces File.
            raf = new RandomAccessFile(iFile, "r");

        } catch (IOException e) {
            // Delete the temporary file when an error occurs!
            if (iTempFileNeeded) {
                if (iFile.exists()) {
                    iFile.delete();
                }
            }
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Return a tmp direct
     *
     * @return
     */
    private static File getTempDirectory() {
        if (iTempFolder == null) {
            try {
                File lTempFile = File.createTempFile("anchor", "tmp");
                iTempFolder = new File(lTempFile.getParent(), "mascotdatfile_raf");
                iTempFolder.mkdir();
                lTempFile.delete();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return iTempFolder;
    }


    private void processSectionBoundary(BufferedReader aBufferedReader, String aCurrentLine) throws IOException {
        //iSectionEndByte = iByteCount - (iLineSeparator.length*2) - aCurrentLine.getBytes().length;
        iSectionEndByte = iByteCount - (iLineSeparator.length * 2) - getStringLengthInBytes(aCurrentLine);

        // boundary flag, parse the next line!
        //reset to false.
        String lLine = "";
        if ((lLine = aBufferedReader.readLine()) != null) {
            lineReadFromBufferedReader(lLine);

            // b) get the section name.

            long[] lSectionIndex = new long[2];
            lSectionIndex[0] = iSectionStartByte;
            lSectionIndex[1] = iSectionEndByte;
            if (iSectionStartByte != -1) {
                iController.addSectionIndex(iSectionName, new ByteOffset(iSectionStartByte, iSectionEndByte));
            }

            iSectionName = parseSectionName(lLine);

            isIndexingLines = isLineIndexedSection(iSectionName);

            // The first bytes of this section start here.
            iSectionStartByte = iByteCount + iLineSeparator.length;
        }


    }

    private void initSummaryIndex(BufferedReader aBufferedReader, SummaryIndex aSummaryIndex) throws IOException {
        int lTypeIndex = -1;
        String lLine;
        while ((lLine = aBufferedReader.readLine()) != null) {
            lineReadFromBufferedReader(lLine);

            // first line is blanc.
            if (!lLine.equals("")) {
                lLine = lLine.substring(0, lLine.indexOf('='));
                if (lLine.endsWith("1")) {
                    lTypeIndex++;
                } else {
                    aSummaryIndex.setNumberOfIndexes(lTypeIndex + 1);
                    aSummaryIndex.setSummaryLineIndex(iLineCount - (lTypeIndex + 1));
                    break;
                }

                if (lLine.startsWith("qmass")) {
                    aSummaryIndex.setMass_index(lTypeIndex);
                } else if (lLine.startsWith("qexp")) {
                    aSummaryIndex.setExp_index(lTypeIndex);
                } else if (lLine.startsWith("qmatch")) {
                    aSummaryIndex.setMatch_index(lTypeIndex);
                } else if (lLine.startsWith("qplughole")) {
                    aSummaryIndex.setPlughole_index(lTypeIndex);
                } else if (lLine.startsWith("qintensity")) {
                    aSummaryIndex.setIntensity_index(lTypeIndex);
                }

                if (lLine.startsWith("--")) {
                    break;
                }
            }
        }

    }

    private void processPeptideSection(BufferedReader aBufferedReader) throws IOException {
        String lLine;
        ArrayList<Integer> lLineNumbers = new ArrayList<Integer>();
        int lOldQuery = 1;
        int lQuery = 1;
        int lPeptide = 1;

        while ((lLine = aBufferedReader.readLine()) != null) {
            lineReadFromBufferedReader(lLine);
            //b) index section lines!

            // first line is blanc.
            if (!lLine.equals("")) {
                lOldQuery = lQuery;

                if (lLine.startsWith("--")) {
                    Integer[] aLines = new Integer[lLineNumbers.size()];
                    iController.addPeptideLineIndex(lLineNumbers.toArray(aLines));
                    processSectionBoundary(aBufferedReader, lLine);
                    break;
                }

                int lEqualSign = lLine.indexOf('=');
                int lSeparator = lLine.indexOf('p');
                int lUnderscore = lLine.indexOf('_', lSeparator);

                // In this condition, there is a secundary line with peptide bounds.
                if (!(lSeparator < lUnderscore && lUnderscore < lEqualSign)) {

                    lQuery = Integer.parseInt(lLine.substring(1, lSeparator - 1));

                    if (lQuery != lOldQuery) {
                        Integer[] aLines = new Integer[lLineNumbers.size()];
                        iController.addPeptideLineIndex(lLineNumbers.toArray(aLines));
                        lLineNumbers = new ArrayList<Integer>();
                    }
                    lLineNumbers.add(new Integer(iLineCount));
                }
            }
        }
    }

    private void processDecoyPeptideSection(final BufferedReader aBufferedReader) throws IOException {
        String lLine;
        ArrayList<Integer> lLineNumbers = new ArrayList<Integer>();
        int lOldQuery = 1;
        int lQuery = 1;
        int lPeptide = 1;

        while ((lLine = aBufferedReader.readLine()) != null) {
            lineReadFromBufferedReader(lLine);
            //b) index section lines!

            // first line is blanc.
            if (!lLine.equals("")) {
                lOldQuery = lQuery;

                if (lLine.startsWith("--")) {
                    Integer[] aLines = new Integer[lLineNumbers.size()];
                    iController.addDecoyPeptideLineIndex(lLineNumbers.toArray(aLines));
                    processSectionBoundary(aBufferedReader, lLine);
                    break;
                }

                int lEqualSign = lLine.indexOf('=');
                int lSeparator = lLine.indexOf('p');
                int lUnderscore = lLine.indexOf('_', lSeparator);

                // In this condition, there is a secundary line with peptide bounds.
                if (!(lSeparator < lUnderscore && lUnderscore < lEqualSign)) {

                    lQuery = Integer.parseInt(lLine.substring(1, lSeparator - 1));

                    if (lQuery != lOldQuery) {
                        Integer[] aLines = new Integer[lLineNumbers.size()];
                        iController.addDecoyPeptideLineIndex(lLineNumbers.toArray(aLines));
                        lLineNumbers = new ArrayList<Integer>();
                    }
                    lLineNumbers.add(new Integer(iLineCount));
                }
            }
        }


    }

    /**
     * Performs a common process when another line is read from the buffer. This concerns increasing the line and byte
     * counters as well as writing the line into the fos.
     *
     * @param lLine String with the last read line.
     * @throws java.io.IOException
     */
    private void lineReadFromBufferedReader(String lLine) throws IOException {

        // Get the line in terms of bytes.

        // If the Mascot result file comes from a stream, then the line must be written into the fos.
        if (iTempFileNeeded) {
            // @TODO Check if buffered writer is not better.
            byte[] lBytes = lLine.getBytes();
            fos.write(lBytes);
            fos.write(iLineSeparator);
            fos.flush();
        }
        // Increase the linecount.
        iLineCount++;

        // Index this line, if needed for this section.
        if (isIndexingLines) {
            iController.addLineIndex(iLineCount, iByteCount);
        }

        // Increase the bytecount.

        iByteCount = iByteCount + getStringLengthInBytes(lLine) + iLineSeparator.length;
    }

    /**
     * Parses the name of the section from the section header line that comes just after the boundary line.
     *
     * @param lLine String is the section header.
     * @return String naming the section.
     */
    private String parseSectionName(String lLine) {
        if (lLine.equals("")) {
            return lLine;
        } else {
            int startIndex;
            int endIndex = lLine.length() - 1;
            startIndex = lLine.indexOf("name=") + 6;

            return lLine.substring(startIndex, endIndex);
        }
    }

    /**
     * Reads a single line from the indexed file by giving the byte position.
     *
     * @param aBytePosition long positions the Random Acces File to reed a line.
     * @return String line from the indexed file starting at position lPosition
     */
    public String readLine(long aBytePosition) {
        String lResult = "";
        try {
            raf.seek(aBytePosition);
            lResult = raf.readLine();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return lResult;
    }

    /**
     * Reads a part from the indexed file by giving the byte position and the length of the byte[] that must be read.
     *
     * @param aBytePositionStart long offset seeks in the file.
     * @param aLength            int number of bytes that must be read.
     * @return String that starts from the offset for aLength bytes.
     */
    public String readInterval(long aBytePositionStart, int aLength) {
        byte[] lReadBytes = new byte[aLength];
        String lResult = null;
        try {
            raf.seek(aBytePositionStart);
            raf.read(lReadBytes);
            lResult = new String(lReadBytes);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lResult;
    }

    /**
     * Close the RandomAccessFile and the temporary file, if any.
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        if (raf != null) {
            raf.close();
        }
        if (iTempFileNeeded) {
            if (iFile.exists()) {
                iFile.delete();
            }
        }
    }

    /**
     * Calling this method will remove all former tmp files created by various Reader instances.
     */
    public static void cleanOldFiles() {
        File lTempDirectory = getTempDirectory();
        File[] lFiles = lTempDirectory.listFiles();
        for (File lFile : lFiles) {
            lFile.delete();
        }
    }

    private boolean isLineIndexedSection(String aSectionName) {
        // Only do line indexing for the peptide section,
        // memory saving!
        if (aSectionName.equals("peptides")
                || aSectionName.equals("summary")
                || aSectionName.equals("decoy_summary")
                || aSectionName.equals("decoy_peptides")) {
            return true;
        } else {
            return false;
        }
    }

    private int getStringLengthInBytes(String s) {
        return s.getBytes().length;

        /*if(iByteLengthStatus == -1){
            if(s.length() == s.getBytes().length){
                 iByteLengthStatus = 1;
            }else{
                iByteLengthStatus = 0;
            }
        }
        if(iByteLengthStatus == 1){
            return s.length();
        }else{
            return s.getBytes().length;
        }*/
    }
}
