package com.compomics.mascotdatfile.util.mascot.index;

import com.compomics.mascotdatfile.util.interfaces.MascotDatfileInf;
import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import junit.TestCaseLM;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 11/10/11
 * Time: 14:00
 */
public class TestPortability extends TestCaseLM {
  private static Logger logger = Logger.getLogger(TestMascotDatfile.class);
    public TestPortability(String aName) {
        super("Test scenario for different kinds of newline");
    }
  public void testLinuxheadLinuxbody()
    {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F028476-linheadlinbody.dat"));
        lMascotDatfile.getParametersSection();
        lMascotDatfile.getModificationList();
        lMascotDatfile.getMasses();
        lMascotDatfile.getHeaderSection();
    }
    public void testWindowsheadLinuxbody()
    {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F028476-winheadlinbody.dat"));
        lMascotDatfile.getParametersSection();
        lMascotDatfile.getModificationList();
        lMascotDatfile.getMasses();
        lMascotDatfile.getHeaderSection();
    }
      public void testMixedheadLinuxbody()
    {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F028476-mixedheadlinbody.dat"));
        lMascotDatfile.getParametersSection();
        lMascotDatfile.getModificationList();
        lMascotDatfile.getMasses();
        lMascotDatfile.getHeaderSection();
    }
      public void testLinuxheadWindowsbody()
    {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F028476-linheadwinbody.dat"));
        lMascotDatfile.getParametersSection();
        lMascotDatfile.getModificationList();
        lMascotDatfile.getMasses();
        lMascotDatfile.getHeaderSection();
    }
          public void testWindowsheadWindowsbody()
    {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F028476-winheadwinbody.dat"));
        lMascotDatfile.getParametersSection();
        lMascotDatfile.getModificationList();
        lMascotDatfile.getMasses();
        lMascotDatfile.getHeaderSection();
    }
          public void testMixedheadWindowsbody()
    {
        MascotDatfileInf lMascotDatfile = new MascotDatfile_Index(getFullFilePath("F028476-mixedheadwinbody.dat"));
        lMascotDatfile.getParametersSection();
        lMascotDatfile.getModificationList();
        lMascotDatfile.getMasses();
        lMascotDatfile.getHeaderSection();
    }
}
