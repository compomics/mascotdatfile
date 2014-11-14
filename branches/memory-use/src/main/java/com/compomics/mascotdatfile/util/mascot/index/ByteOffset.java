package com.compomics.mascotdatfile.util.mascot.index;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 3-jul-2008 Time: 10:25:54 To change this template use File | Settings |
 * File Templates.
 */
public class ByteOffset {
    // Class specific log4j logger for ByteOffset instances.
    private static Logger logger = Logger.getLogger(ByteOffset.class);
    private long iStartByte;
    private long iStopByte;

    public ByteOffset(long iStartByte, long iStopByte) {
        this.iStartByte = iStartByte;
        this.iStopByte = iStopByte;
    }


    /**
     * Getter for property 'stopByte'.
     *
     * @return Value for property 'stopByte'.
     */
    public long getStopByte() {
        return iStopByte;
    }

    /**
     * Setter for property 'stopByte'.
     *
     * @param aStopByte Value to set for property 'stopByte'.
     */
    public void setStopByte(final long aStopByte) {
        iStopByte = aStopByte;
    }

    /**
     * Getter for property 'startByte'.
     *
     * @return Value for property 'startByte'.
     */
    public long getStartByte() {
        return iStartByte;
    }

    /**
     * Setter for property 'startByte'.
     *
     * @param aStartByte Value to set for property 'startByte'.
     */
    public void setStartByte(final long aStartByte) {
        iStartByte = aStartByte;
    }
}
