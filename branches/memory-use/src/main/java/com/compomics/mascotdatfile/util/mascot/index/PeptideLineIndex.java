package com.compomics.mascotdatfile.util.mascot.index;

import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 3-jul-2008 Time: 15:23:01 To change this template use File | Settings |
 * File Templates.
 */
public class PeptideLineIndex {
    // Class specific log4j logger for PeptideLineIndex instances.
    private static Logger logger = Logger.getLogger(PeptideLineIndex.class);
    Integer[] iLines;

    public PeptideLineIndex(final Integer[] aLines) {
        iLines = aLines;
    }

    public int getNumberOfPeptides() {
        return iLines.length;
    }

    public int getLine(int aPeptideNumber) {
        if (aPeptideNumber <= iLines.length) {
            return iLines[aPeptideNumber - 1];
        } else {
            return -1;
        }
    }

    public class LineIterator implements Iterator {

        int index = -1;

        public boolean hasNext() {
            return ((index + 1) < iLines.length);
        }

        public Integer next() {
            index++;
            return iLines[index];
        }

        public void remove() {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
