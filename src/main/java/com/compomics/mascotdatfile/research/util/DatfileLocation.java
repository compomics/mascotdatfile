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

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import org.apache.log4j.Logger;

import com.compomics.mascotdatfile.util.mascot.MascotDatfile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 23-apr-2006 Time: 19:20:40
 */
public class DatfileLocation {
    // Class specific log4j logger for DatfileLocation instances.
    private static Logger logger = Logger.getLogger(DatfileLocation.class);

    /**
     * This String represents the path and filename of the datfile is the datfile is located on the hard disk.
     */
    private String iPathAndFilename = null;
    /**
     * This String[] must supply URL parameters.<br><ul><li>1. Server (ex: http://cavell.ugent.be/)<li>2. Date
     * (ex:20061130)<li> 3. DatfileName (ex:F009987.dat)</ul><br>.
     */
    private String[] iURLInformation = null;
    /**
     * This integer represents the type of the datfilelocation.<br> Its initialised as a negative value, it will be set
     * by one of the final static integers defining the different types of datfileLocations.
     */
    private int iDatfileLocationType = -1;

    /**
     * This final static int types this instance that the datfile is located on the hard disk.
     */
    public final static int HARDDISK = 0;
    /**
     * This final static int types the instance that the datfile is located on the internet.
     */
    public final static int URL = 1;


    /**
     * Constructor sets up a new DatfileLocation.
     *
     * @param aDatfileLocationType source of the datfile ( 0 is hard disk, 1 is localDB, 2 is Muppet)
     * @param aParams              <ol> <li> if datfile is on hard disk; this String[] must first supply <b>the path and
     *                             filename as 1 String</b><br>ex: <i>c:\mascot\F010356.dat</i>. <li> if datfile comes
     *                             from local db, this String[] must supply only the <b>datfileID or the filename</b>.
     *                             <li> if datfile comes from muppet db, this String[] must supply the
     *                             <ol><li><b>datfileID or the filename</b>.<li>username<li>password</ol><br> <li> if
     *                             datfile comes from a New Database that still has to be defined, this String[] must
     *                             supply the database parameters and the datfile.<ol><li><b>datfileID or
     *                             filename</b>.<li>username<li>password<li><b>database server</b></ol><br> <li> if
     *                             datfile comes from the internet, this String[] must supply URL
     *                             parameters.<br><ol><li>Server (ex: http://cavell.ugent.be/)<li>Date (ex:20061130)<li>
     *                             DatfileName (ex:F009987.dat)</ol><br> </ol>
     */
    public DatfileLocation(int aDatfileLocationType, String[] aParams) {
        iDatfileLocationType = aDatfileLocationType;
        switch (iDatfileLocationType) {
            case HARDDISK: {
                // Datfile from hard disk.
                iPathAndFilename = aParams[0];
                break;
            }
            case URL: {
                iURLInformation = aParams;
                break;
            }
        }
    }

    /**
     * Constructor sets up a new DatfileLocation.
     *
     * @param aDatfileLocationType source of the datfile ( 0 is hard disk, 1 is localDB, 2 is Muppet)
     * @param aParam               <ol> <li> if datfile is on hard disk; this String must first supply <b>the path and
     *                             filename in 1 String</b>. </ol>
     */
    public DatfileLocation(int aDatfileLocationType, String aParam) {
        this(aDatfileLocationType, new String[]{aParam});
    }


    /**
     * Constructor sets up a new DatfileLocation.
     *
     * @param aDatfileLocationType source of the datfile ( 0 is hard disk, 1 is localDB, 2 is Muppet)
     * @param aParams              <ol> <li> if datfile comes from local db, this int must supply only the
     *                             <b>datfileID</b>. </ol>
     */
    public DatfileLocation(int aDatfileLocationType, int aParams) {
        this(aDatfileLocationType, new String[]{Integer.toString(aParams)});
    }

    /**
     * This method returns an MascotDatfile instance based on the location type that was defined in the constructor.
     *
     * @return MascotDatfile    MascotDatfile instance defined on the constructor.
     *
     * @throws java.lang.ClassNotFoundException if a ClassNotFoundException is thrown
     * @throws java.lang.InstantiationException if an InstantiationException is thrown
     * @throws java.lang.IllegalAccessException if an IllegalAccessException is thrown
     * @throws java.sql.SQLException if an SQLException is thrown
     */
    public MascotDatfileInf getDatfile() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        return this.getDatfile(false);
    }

    /**
     * This method returns an MascotDatfile instance based on the location type that was defined in the constructor.
     *
     * @param aIndexed boolean to indicate whether the file should be loaded using an index or not.
     * @return MascotDatfile    MascotDatfile instance defined on the constructor.
     *
     * @throws java.lang.ClassNotFoundException if a ClassNotFoundException is thrown
     * @throws java.lang.InstantiationException if an InstantiationException is thrown
     * @throws java.lang.IllegalAccessException if an IllegalAccessException is thrown
     * @throws java.sql.SQLException if an SQLException is thrown
     */
    public MascotDatfileInf getDatfile(boolean aIndexed) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        MascotDatfileInf mdf = null;

        Label:
        if (iDatfileLocationType == HARDDISK) {
            {
                // Datfile from HardDisk.
                // See if we need indexed or not indexed.
                if(aIndexed) {
                    mdf = new MascotDatfile_Index(iPathAndFilename);
                } else {
                    mdf = new MascotDatfile(iPathAndFilename);
                }
                break Label;
            }
        } else if (iDatfileLocationType == URL) {
            {
                // Datfile from URL.
                String lServer = iURLInformation[0];
                String lDate = iURLInformation[1];
                String lDatfileFilename = iURLInformation[2];
                // example: ***http://cavell.ugent.be/mascot/x-cgi/ms-status.exe?Autorefresh=false&Show=RESULTFILE&DateDir=20060419&ResJob=F011580.dat***
                String URL = lServer + "mascot/x-cgi/ms-status.exe?Autorefresh=false&Show=RESULTFILE&DateDir=" + lDate + "&ResJob=" + lDatfileFilename;
                try {
                    URL lDatfileLocation = new URL(URL);
                    URLConnection lURLConnection = lDatfileLocation.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(lURLConnection.getInputStream()));
                    mdf = new MascotDatfile(br);

                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return mdf;
    }

    /**
     * This method returns the type of datfileLocation this is.
     *
     * @return String with the datfileLocationType.
     */
    public String toString() {
        String toString = null;
        switch (iDatfileLocationType) {
            case HARDDISK:
                toString = "Harddisk";
                break;
            case URL:
                toString = "URL";
                break;
        }
        return toString;
    }
}
