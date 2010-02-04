package com.compomics.mascotdatfile.util.mascot.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Kenny
 * Date: 25-jun-2008
 * Time: 16:26:44
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class controlls a byte-based File Index and a Reader for a single Mascot result file.
 */
public class Controller {

    private Reader iReader = null;
    private FileIndexer iIndex = new FileIndexer();

    public Controller(String aFileName) {
        iReader = new Reader(aFileName, this);
    }

    public Controller(final File aFile) {
        iReader = new Reader(aFile, this);
    }

    public Controller(BufferedReader aBufferedReader){
        iReader = new Reader(aBufferedReader, this);
    }

    protected void addLineIndex(int aLineNumber, long aByte){
        iIndex.addLineIndex(aLineNumber, aByte);
    }

    protected void addPeptideLineIndex(Integer[] aPeptideNumbers){
        iIndex.addPeptideLineIndex(aPeptideNumbers);
    }

    protected void addDecoyPeptideLineIndex(Integer[] aPeptideNumbers){
        iIndex.addDecoyPeptideLineIndex(aPeptideNumbers);
    }

    protected void addSectionIndex(String aSectionName, ByteOffset aByteIndex){
        iIndex.addSectionIndex(aSectionName, aByteIndex);
    }

    public String readSection(String aSection) {
        ByteOffset lIndex = iIndex.getSectionIndex(aSection);
        if(lIndex == null){
            return null;
        }else{
            int lLength = ((Long)(lIndex.getStopByte() - lIndex.getStartByte())).intValue();
            return iReader.readInterval(lIndex.getStartByte(), lLength);
        }
    }

    public HashMap readSectionAsHashMap(String aSection){
        String s = readSection(aSection);
        if(s==null){
            return null;
        }else{
            return processSectionToHashMap(readSection(aSection));
        }
    }

    public String readPeptideHit(int aQueryNumber, int aPeptideHitNumber){
        long lIndex = iIndex.getPeptideLineIndex(aQueryNumber, aPeptideHitNumber);
        if (lIndex != -1l) {
            String s = iReader.readLine(lIndex);
            return s.substring(s.indexOf('=')+1);
        } else {
            return null;
        }
    }

    public String readDecoyPeptideHit(final int aQueryNumber, final int aPeptideHitNumber) {
        long lIndex = iIndex.getDecoyPeptideLineIndex(aQueryNumber, aPeptideHitNumber);
        if (lIndex != -1l) {
            String s = iReader.readLine(lIndex);
            return s.substring(s.indexOf('=')+1);
        } else {
            return null;
        }
    }

    public int getNumberOfQueries(int aQueryNumber){
        return iIndex.getNumberOfPeptides(aQueryNumber);
    }

    public String readSummary(int aQueryNumber, int aSummaryIndex){
        String s = iReader.readLine(iIndex.getSummaryLineIndex(aQueryNumber, aSummaryIndex));
        return s.substring(s.indexOf('=') + 1);
    }

    public String readDecoySummary( int aQueryNumber, int aSummaryIndex) {
        String s = iReader.readLine(iIndex.getDecoySummaryLineIndex(aQueryNumber, aSummaryIndex));
        return s.substring(s.indexOf('=') + 1);
    }

    /**
     * This method parses the content of a section into key-value pairs and
     * stores these in a HashMap.
     *
     * @param   aContent    the content of the section to be parsed.
     * @return  HashMap with the contents of the section as key-value pairs.
     */
    private HashMap processSectionToHashMap(String aContent) {
        HashMap lhmResult = new HashMap();
        try {
            BufferedReader lbr = new BufferedReader(new StringReader(aContent));
            String line = null;
            while((line = lbr.readLine()) != null) {
                // Structure in each section is identical:
                // KEY=VALUE
                // However, for header lines ('hx_text'; holding the protein description)
                // or Protein section names (starting with the accession, encircled by '"')
                // we need to make sure we have the full description line (it might
                // contain an '=', as IPI headers often do)!
                Object key = null;
                Object value = null;
                if( (line.startsWith("h") && line.indexOf("_text=") >= 0) || (line.startsWith("\"") && line.endsWith("\"")) ) {
                    key = line.substring(0, line.indexOf("=")).trim();
                    if(line.length() > line.indexOf("=")+1) {
                        value = line.substring(line.indexOf("=")+1).trim();
                    }
                } else {
                    StringTokenizer lst = new StringTokenizer(line, "=");
                    key = lst.nextToken().trim();
                    value = null;
                    if(lst.hasMoreTokens()) {
                        value = lst.nextToken().trim();
                    }
                }
                lhmResult.put(key, value);
            }
            lbr.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return lhmResult;
    }

    /**
     * Getter for property 'numberOfQueries'.
     *
     * @return Value for property 'numberOfQueries'.
     */
    public int getNumberOfQueries(){
       return iIndex.getNumberOfQueries();
    }

    public void close(){
        try {
            iReader.close();
        } catch (IOException e) {
            System.err.println("Failed to finish the Reader.");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int getNumberOfPeptideHits(final int aQueryNumber) {
        return iIndex.getNumberOfPeptides(aQueryNumber);
    }

    public int getNumberOfDecoyPeptideHits(final int aQueryNumber) {
        return iIndex.getNumberOfDecoyPeptides(aQueryNumber);
    }

    public String toString() {
        return "Controller{" +
               "iReader=" + iReader +
               ", iIndex=" + iIndex +
               '}';
    }
}
