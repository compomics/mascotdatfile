package com.compomics.mascotdatfile.util.mascot.factory;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.mascotdatfile.util.mascot.enumeration.MascotDatfileType;

import java.io.BufferedReader;

/**
 * Created by IntelliJ IDEA. User: Kenny Date: 14-jul-2008 Time: 15:19:49 To change this template use File | Settings |
 * File Templates.
 */
public class MascotDatfileFactory {

    /**
     * Creates a new MascotDatfile implementation.
     * @param aDatFile String with path and filename of the Mascot result file.
     * @param aType type of MascotDatfile (defined on enum MascotDatfileType)
     * @return A MascotDatfile implementation from the given file.
     */
    public static MascotDatfileInf create(String aDatFile, MascotDatfileType aType){
        MascotDatfileInf lMascotDatfile = null;
        if(aType == MascotDatfileType.MEMORY){
            lMascotDatfile = new MascotDatfile(aDatFile);
        }else if(aType == MascotDatfileType.INDEX){
            lMascotDatfile = new MascotDatfile_Index(aDatFile);
        }
        return lMascotDatfile;
    }

    /**
     * Creates a new MascotDatfile implementation.
     * @param aReader BufferedReader with reading from a Mascot result file.
     * @param aType type of MascotDatfile (defined on enum MascotDatfileType)
     * @return A MascotDatfile implementation from the given reader.
     */
    public static MascotDatfileInf create(BufferedReader aReader, MascotDatfileType aType){
        MascotDatfileInf lMascotDatfile = null;
        if(aType == MascotDatfileType.MEMORY){
            lMascotDatfile = new MascotDatfile(aReader);
        }else if(aType == MascotDatfileType.INDEX){
            lMascotDatfile = new MascotDatfile_Index(aReader);
        }
        return lMascotDatfile;
    }

    /**
     * @param aReader BufferedReader with reading from a Mascot result file.
     * @param aSource String with the source of the BufferedStream.
     * @param aType type of MascotDatfile (defined on enum MascotDatfileType)
     * @return A MascotDatfile implementation from the given reader.
     */
    public static MascotDatfileInf create(BufferedReader aReader, String aSource, MascotDatfileType aType){
        MascotDatfileInf lMascotDatfile = null;
        if(aType == MascotDatfileType.MEMORY){
            lMascotDatfile = new MascotDatfile(aReader, aSource);
        }else if(aType == MascotDatfileType.INDEX){
            lMascotDatfile = new MascotDatfile_Index(aReader, aSource);
        }
        return lMascotDatfile;
    }
}