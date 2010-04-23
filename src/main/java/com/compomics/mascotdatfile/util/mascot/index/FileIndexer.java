package com.compomics.mascotdatfile.util.mascot.index;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 25-jun-2008 Time: 16:12:10 To change this template use File | Settings |
 * File Templates.
 */
public class FileIndexer {
    // Class specific log4j logger for FileIndexer instances.
    private static Logger logger = Logger.getLogger(FileIndexer.class);

    private HashMap<String, ByteOffset> iSectionMap = null;
    private SummaryIndex iSummaryIndex = SummaryIndex.getInstance();
    private SummaryIndex iDecoySummaryIndex = SummaryIndex.getDecoyInstance();

    private HashMap<Integer, Long> iLineList = null;
    private ArrayList<ByteOffset> iQueryList = null;
    private ArrayList<PeptideLineIndex> iPeptideLineList;
    private ArrayList<PeptideLineIndex> iDecoyPeptideLineList;

    public FileIndexer() {
        iSectionMap = new HashMap<String, ByteOffset>();

        iLineList = new HashMap<Integer, Long>();

        iQueryList = new ArrayList<ByteOffset>(1000);

        iPeptideLineList = new ArrayList<PeptideLineIndex>(10000);

        iDecoyPeptideLineList = new ArrayList<PeptideLineIndex>();
    }

    // Section indexing.

    protected ByteOffset getSectionIndex(String aSectionName) {
        if (aSectionName.startsWith("query")) {
            Integer lQueryNumber = Integer.parseInt(aSectionName.substring(5));
            lQueryNumber = lQueryNumber - 1;
            return iQueryList.get(lQueryNumber);
        } else {
            return iSectionMap.get(aSectionName);
        }
    }

    protected void addSectionIndex(String aSectionName, ByteOffset aByteIndex) {
        if (aSectionName.startsWith("query")) {
            Integer lQueryNumber = Integer.parseInt(aSectionName.substring(5));
            iQueryList.add(aByteIndex);
        } else {
            iSectionMap.put(aSectionName, aByteIndex);
        }
    }

    // Line indexing.

    protected long getLineIndex(int aLineNumber) {
        // minus 1 as the arraylist is zero based in opposite to the line numbers.
        if (iLineList.get(aLineNumber) != null) {
            return iLineList.get(aLineNumber);
        } else {
            throw new IllegalArgumentException("Line number \'" + aLineNumber + "\' was not indexed and can therefore not be accessed.");
        }
    }

    protected void addLineIndex(int aLine, long aByte) {
        iLineList.put(aLine, aByte);
    }

    // Peptide hit indexing.

    protected long getPeptideLineIndex(int aQueryNumber, int aPeptideHitNumber) {
        aQueryNumber = aQueryNumber - 1;
        int lLineNumber = iPeptideLineList.get(aQueryNumber).getLine(aPeptideHitNumber);
        if (lLineNumber != -1) {
            return getLineIndex(lLineNumber);
        } else {
            return -1l;
        }
    }

    public long getDecoyPeptideLineIndex(int aQueryNumber, final int aPeptideHitNumber) {
        aQueryNumber = aQueryNumber - 1;
        int lLineNumber = iDecoyPeptideLineList.get(aQueryNumber).getLine(aPeptideHitNumber);
        if (lLineNumber != -1) {
            return getLineIndex(lLineNumber);
        } else {
            return -1l;
        }
    }

    protected void addPeptideLineIndex(Integer[] aLines) {
        iPeptideLineList.add(new PeptideLineIndex(aLines));
    }

    protected void addDecoyPeptideLineIndex(Integer[] aLines) {
        iDecoyPeptideLineList.add(new PeptideLineIndex(aLines));
    }

    protected int getNumberOfPeptides(int aQueryNumber) {
        aQueryNumber = aQueryNumber - 1;
        return iPeptideLineList.get(aQueryNumber).getNumberOfPeptides();
    }

    protected int getNumberOfDecoyPeptides(int aQueryNumber) {
        aQueryNumber = aQueryNumber - 1;
        return iDecoyPeptideLineList.get(aQueryNumber).getNumberOfPeptides();
    }

    protected int getNumberOfQueries() {
        return iPeptideLineList.size();
    }

    public long getSummaryLineIndex(final int aQueryNumber, final int aSummaryIndex) {
        int lLineNumber = iSummaryIndex.getSummaryLine(aQueryNumber, aSummaryIndex);
        return getLineIndex(lLineNumber);
    }

    public long getDecoySummaryLineIndex(final int aQueryNumber, final int aSummaryIndex) {
        int lLineNumber = iDecoySummaryIndex.getSummaryLine(aQueryNumber, aSummaryIndex);
        return getLineIndex(lLineNumber);
    }
}
