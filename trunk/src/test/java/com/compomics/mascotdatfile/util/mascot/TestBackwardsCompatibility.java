package com.compomics.mascotdatfile.util.mascot;


import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.Vector;

import static com.compomics.util.junit.TestCaseLM.getFullFilePath;

/**
 * Created by IntelliJ IDEA.
 * User: Davy
 * Date: 7/26/12
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestBackwardsCompatibility  extends TestCase {

    private static Logger logger = Logger.getLogger(TestBackwardsCompatibility.class);
    private ProteinMap iProteinMap;
    private MascotDatfile lMascotDatfile;
    private Header h;
    private  Masses m;
    private QueryToPeptideMap lQueryToPeptideMap;
    private PeptideHit lPeptideHit;
    private ProteinHit lProteinHit;
    private ModificationList lModificationList;

    public TestBackwardsCompatibility() {
        super("Testscenario for different Mascot Datfile versions.");
    }



    public void testGetVersionTwoPointOne() {
        String lFilename = "F009911.dat";
        lMascotDatfile = new MascotDatfile(getFullFilePath(lFilename));
        Assert.assertEquals(lFilename, lMascotDatfile.getFileName());
        h = lMascotDatfile.getHeaderSection();
        //3.Test ALL instance variables.
        Assert.assertEquals(7718999, h.getSequences());
        Assert.assertEquals(7718999, h.getSequences_after_tax());
        Assert.assertEquals(142534249, h.getResidues());
        Assert.assertEquals(1963, h.getExecutionTime());
        Assert.assertEquals(1140512555, h.getDate());
        Assert.assertEquals("10:02:35", h.getTime());
        Assert.assertEquals(817, h.getQueries());
        Assert.assertEquals(50, h.getMaxHits());
        Assert.assertEquals("2.1.02", h.getVersion());
        Assert.assertEquals("SP_human_truncAll_NR_20060207.fas", h.getRelease());
        Assert.assertEquals("114051059101", h.getTaskID());
        Assert.assertEquals("Taxonomy 'Homo sapiens (human)' ignored. No taxonomy indexes for this database", h.getWarnings().get(0));
        //No further variables or functions in the Header instance. If this works, this object works fine.
        m = lMascotDatfile.getMasses();
        //3.Test ALL instance variables.
        Assert.assertEquals(71.037110, m.getMass("A"), 0.0);
        Assert.assertEquals(114.534930, m.getMass("B"), 0.0);
        Assert.assertEquals(160.030649, m.getMass("C"), 0.0);
        Assert.assertEquals(115.026940, m.getMass("D"), 0.0);
        Assert.assertEquals(129.042590, m.getMass("E"), 0.0);
        Assert.assertEquals(147.068410, m.getMass("F"), 0.0);
        Assert.assertEquals(57.021460, m.getMass("G"), 0.0);
        Assert.assertEquals(137.058910, m.getMass("H"), 0.0);
        Assert.assertEquals(113.084060, m.getMass("I"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("J"), 0.0);
        Assert.assertEquals(173.124358, m.getMass("K"), 0.0);
        Assert.assertEquals(113.084060, m.getMass("L"), 0.0);
        Assert.assertEquals(131.040490, m.getMass("M"), 0.0);
        Assert.assertEquals(114.042930, m.getMass("N"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("O"), 0.0);
        Assert.assertEquals(97.052760, m.getMass("P"), 0.0);
        Assert.assertEquals(128.058580, m.getMass("Q"), 0.0);
        Assert.assertEquals(162.187600, m.getMass("R"), 0.0);
        Assert.assertEquals(87.032030, m.getMass("S"), 0.0);
        Assert.assertEquals(101.047680, m.getMass("T"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("U"), 0.0);
        Assert.assertEquals(99.068410, m.getMass("V"), 0.0);
        Assert.assertEquals(186.079310, m.getMass("W"), 0.0);
        Assert.assertEquals(111.000000, m.getMass("X"), 0.0);
        Assert.assertEquals(163.063330, m.getMass("Y"), 0.0);
        Assert.assertEquals(128.550590, m.getMass("Z"), 0.0);
        Assert.assertEquals(1.007825, m.getMass("Hydrogen"), 0.0);
        Assert.assertEquals(12.000000, m.getMass("Carbon"), 0.0);
        Assert.assertEquals(14.003070, m.getMass("Nitrogen"), 0.0);
        Assert.assertEquals(15.994910, m.getMass("Oxygen"), 0.0);
        Assert.assertEquals(0.000549, m.getMass("Electron"), 0.0);
        Assert.assertEquals(17.002735, m.getMass("C_term"), 0.0);
        Assert.assertEquals(1.007825, m.getMass("N_term"), 0.0);
        Parameters lParameters = lMascotDatfile.getParametersSection();
        //3.Test ALL instance variables.
        Assert.assertEquals("Licensed to: Vlaams Interuniversitair Instituut voor Biotechnologie, (2 processors).", lParameters.getLicense());
        Assert.assertTrue(null == lParameters.getMP());
        Assert.assertTrue(null == lParameters.getNM());
        Assert.assertEquals("GranzymeB-SILAC-Diff-060221-C13 | (C:\\Program Files\\Matrix Science\\Mascot Daemon\\2.SP_SILAC_QTOF_C13_4.par), submitted from Daemon on PCJOSE", lParameters.getCom());
        Assert.assertTrue(null == lParameters.getIATOL());
        Assert.assertTrue(null == lParameters.getIA2TOL());
        Assert.assertTrue(null == lParameters.getIASTOL());
        Assert.assertTrue(null == lParameters.getIBTOL());
        Assert.assertTrue(null == lParameters.getIB2TOL());
        Assert.assertTrue(null == lParameters.getIBSTOL());
        Assert.assertTrue(null == lParameters.getIYTOL());
        Assert.assertTrue(null == lParameters.getIY2TOL());
        Assert.assertTrue(null == lParameters.getIYSTOL());
        Assert.assertTrue(null == lParameters.getSEG());
        Assert.assertTrue(null == lParameters.getSEGT());
        Assert.assertTrue(null == lParameters.getSEGTU());
        Assert.assertTrue(null == lParameters.getLTOL());
        Assert.assertEquals("0.3", lParameters.getTOL());
        Assert.assertEquals("Da", lParameters.getTOLU());
        Assert.assertTrue(null == lParameters.getITH());
        Assert.assertEquals("0.3", lParameters.getITOL());
        Assert.assertEquals("Da", lParameters.getITOLU());
        Assert.assertEquals("0", lParameters.getPFA());
        Assert.assertEquals("SP_human_trunc_all", lParameters.getDatabase());
        Assert.assertEquals("Acetyl_heavy (K),Carbamidomethyl (C),Arg 6xC(13) (R)", lParameters.getFixedModifications());
        Assert.assertEquals("Monoisotopic", lParameters.getMass());
        Assert.assertEquals("no_cleavage", lParameters.getCleavage());
        Assert.assertEquals("C:\\Petra\\185.Jurkat_Granzyme_substr_SILAC\\Mergefiles\\mergefile_21022006_084216296.txt", lParameters.getFile());
        Assert.assertTrue(null == lParameters.getPeak());
        Assert.assertTrue(null == lParameters.getQue());
        Assert.assertTrue(null == lParameters.getTwo());
        Assert.assertEquals("MIS", lParameters.getSearch());
        Assert.assertEquals("Petra", lParameters.getUserName());
        Assert.assertTrue(null == lParameters.getUserEmail());
        Assert.assertEquals("2+ and 3+", lParameters.getCharge());
        Assert.assertEquals("../data/20060221/F009907.dat", lParameters.getIntermediate());
        Assert.assertEquals("AUTO", lParameters.getReport());
        Assert.assertTrue(null == lParameters.getOverview());
        Assert.assertEquals("Mascot generic", lParameters.getFormat());
        Assert.assertEquals("1.01", lParameters.getFormVersion());
        Assert.assertTrue(null == lParameters.getFrag());
        Assert.assertEquals("Acetyl (N-term),Acetyl_heavy (N-term),Deamidation (NQ),Oxidation (M),Pyro-cmC (N-term camC),Pyro-glu (N-term Q),Pro 5xC(13) (P)", lParameters.getVariableModifications());
        Assert.assertTrue(null == lParameters.getUser01());
        Assert.assertTrue(null == lParameters.getUser02());
        Assert.assertTrue(null == lParameters.getUser03());
        Assert.assertTrue(null == lParameters.getUser04());
        Assert.assertTrue(null == lParameters.getUser05());
        Assert.assertTrue(null == lParameters.getUser06());
        Assert.assertTrue(null == lParameters.getUser07());
        Assert.assertTrue(null == lParameters.getUser08());
        Assert.assertTrue(null == lParameters.getUser09());
        Assert.assertTrue(null == lParameters.getUser10());
        Assert.assertTrue(null == lParameters.getUser11());
        Assert.assertTrue(null == lParameters.getUser12());
        Assert.assertTrue(null == lParameters.getPrecursor());
        Assert.assertEquals(". . . . . . . . . . . . . . . . Homo sapiens (human)", lParameters.getTaxonomy());
        Assert.assertTrue(null == lParameters.getAccession());
        Assert.assertEquals("Peptide", lParameters.getReportType());
        Assert.assertTrue(null == lParameters.getSubcluster());
        Assert.assertTrue(null == lParameters.getICAT());
        Assert.assertEquals("ESI-QUAD-TOF", lParameters.getInstrument());
        Assert.assertTrue(null == lParameters.getErrorTolerant());
        Assert.assertTrue(null == lParameters.getFrames());
        Assert.assertTrue(null == lParameters.getCutout());
        Assert.assertFalse(lParameters.isDistillerMultiFile());
        Assert.assertFalse(lParameters.isDistillerProcessing());
        Assert.assertNull(lParameters.getDistillerMultiFileNames());
        //Test int[] rules (Make a sum, if the sum is'nt 72, fail() will be called.
        int[] lRules = lParameters.getRules();
        int sum = 0;
        for (int i = 0; i < lRules.length; i++) {
            sum += lRules[i];
        }
        Assert.assertEquals("lRules int[] was not formed correctly.", 72, sum);

        iProteinMap = lMascotDatfile.getProteinMap();
        Assert.assertEquals("(*CE*) ZMY15_HUMAN Zinc finger MYND domain-containing protein 15.", iProteinMap.getProteinDescription("Q9H091 (256-262)"));
        Assert.assertNotNull(iProteinMap.getProteinDescription("Q92576 (732-737)"));
        Assert.assertEquals("(*CE*) HD_HUMAN Huntingtin (Huntington disease protein) (HD protein).", iProteinMap.getProteinDescription("P42858 (1265-1270)"));
        ProteinID lProteinID = iProteinMap.getProteinID("Q9H091 (256-262)");
        Assert.assertEquals("Q9H091 (256-262)", lProteinID.getAccession());
        Assert.assertEquals(724.39, lProteinID.getMass(), 0.0);


        lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        //F009911  q447_p3
        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(447, 3);

        //Test all the PeptideHit instance variables.
        Assert.assertEquals(0, lPeptideHit.getMissedCleavages());
        Assert.assertEquals(1802.063690, lPeptideHit.getPeptideMr(), 0.0);
        Assert.assertEquals(0.196582, lPeptideHit.getDeltaMass(), 0.0);
        Assert.assertEquals(6, lPeptideHit.getNumberOfIonsMatched());
        Assert.assertEquals("GKYQIHTGLQHSIIR", lPeptideHit.getSequence());
        Assert.assertEquals(56, lPeptideHit.getPeaksUsedFromIons1());
        Assert.assertEquals(3, lPeptideHit.getVariableModificationsArray()[10]);
        Assert.assertEquals(3.76, lPeptideHit.getIonsScore(), 0.0);
        //ion series found:     -- 00000020000000000 --
        Assert.assertEquals(2, lPeptideHit.getIonSeriesFound()[6]);
        Assert.assertEquals(0, lPeptideHit.getIonSeriesFound()[3]);
        Assert.assertEquals(17, lPeptideHit.getIonSeriesFound().length);
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons2());
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons3());

        lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(0);
        Assert.assertEquals("Q5FYB0 (132-146)", lProteinHit.getAccession());
        Assert.assertEquals(0, lProteinHit.getFrameNumber());
        Assert.assertEquals(1, lProteinHit.getStart());
        Assert.assertEquals(15, lProteinHit.getStop());
        Assert.assertEquals(1, lProteinHit.getMultiplicity());


        lModificationList = lMascotDatfile.getModificationList();

        Vector lVariableModificationsVec = lModificationList.getVariableModifications();
        VariableModification lVariableModification = null;

        //First modification_Acetyl
        lVariableModification = (VariableModification) lVariableModificationsVec.get(0);
        Assert.assertEquals(42.010559, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Acetyl", lVariableModification.getType());
        Assert.assertEquals("N-term", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(1, lVariableModification.getModificationID());
        Assert.assertEquals("Ace", lVariableModification.getShortType());

        //Second modification_Acetyl_heavy
        lVariableModification = (VariableModification) lVariableModificationsVec.get(1);
        Assert.assertEquals(45.029388, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Acetyl_heavy", lVariableModification.getType());
        Assert.assertEquals("N-term", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(2, lVariableModification.getModificationID());
        Assert.assertEquals("AcD3", lVariableModification.getShortType());

        //Thirth modification_Deamidation
        lVariableModification = (VariableModification) lVariableModificationsVec.get(2);
        Assert.assertEquals(0.984009, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Deamidation", lVariableModification.getType());
        Assert.assertEquals("NQ", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(3, lVariableModification.getModificationID());
        Assert.assertEquals("Dam", lVariableModification.getShortType());

        //Fourth modification_Oxidation
        lVariableModification = (VariableModification) lVariableModificationsVec.get(3);
        Assert.assertEquals(15.994904, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Oxidation", lVariableModification.getType());
        Assert.assertEquals("M", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(4, lVariableModification.getModificationID());
        Assert.assertEquals("Mox", lVariableModification.getShortType());

        //Fifth modification_Pyro-cmC
        lVariableModification = (VariableModification) lVariableModificationsVec.get(4);
        Assert.assertEquals(-17.026535, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Pyro-cmC", lVariableModification.getType());
        Assert.assertEquals("N-term camC", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(5, lVariableModification.getModificationID());
        Assert.assertEquals("Pyc", lVariableModification.getShortType());

        //Sixth modification_Pyro-glu
        lVariableModification = (VariableModification) lVariableModificationsVec.get(5);
        Assert.assertEquals(-17.026535, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Pyro-glu", lVariableModification.getType());
        Assert.assertEquals("N-term Q", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(6, lVariableModification.getModificationID());
        Assert.assertEquals("Pyr", lVariableModification.getShortType());

        //Seventh modification_Pro 5xC(13)
        lVariableModification = (VariableModification) lVariableModificationsVec.get(6);
        Assert.assertEquals(5.000000, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Pro 5xC(13)", lVariableModification.getType());
        Assert.assertEquals("P", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(7, lVariableModification.getModificationID());
        Assert.assertEquals("C13", lVariableModification.getShortType());

        Assert.assertFalse(lVariableModificationsVec.get(0).equals(lVariableModificationsVec.get(1)));

    }

    public void testGetVersionTwoPointFour() {
        String lFilename = "mascot_24_F633433.dat";
        lMascotDatfile = new MascotDatfile(getFullFilePath(lFilename));
        Assert.assertEquals(lFilename, lMascotDatfile.getFileName());
        h = lMascotDatfile.getHeaderSection();
        //3.Test ALL instance variables.
        Assert.assertEquals(536029, h.getSequences());
        Assert.assertEquals(65973, h.getSequences_after_tax());
        Assert.assertEquals(190235160, h.getResidues());
        Assert.assertEquals(38, h.getExecutionTime());
        Assert.assertEquals(1339692279, h.getDate());
        Assert.assertEquals("12:44:39", h.getTime());
        Assert.assertEquals(2252, h.getQueries());
        Assert.assertEquals(50, h.getMaxHits());
        Assert.assertEquals("2.4.0", h.getVersion());
        Assert.assertEquals("Sprot_051612.fas", h.getRelease());
        Assert.assertEquals("133969224101", h.getTaskID());
        m = lMascotDatfile.getMasses();
        //3.Test ALL instance variables.
        Assert.assertEquals(71.037114, m.getMass("A"), 0.0);
        Assert.assertEquals(114.53494, m.getMass("B"), 0.0);
        Assert.assertEquals(160.030649, m.getMass("C"), 0.0);
        Assert.assertEquals(115.026943, m.getMass("D"), 0.0);
        Assert.assertEquals(129.042593, m.getMass("E"), 0.0);
        Assert.assertEquals(147.068414, m.getMass("F"), 0.0);
        Assert.assertEquals(57.021464, m.getMass("G"), 0.0);
        Assert.assertEquals(137.058912, m.getMass("H"), 0.0);
        Assert.assertEquals(113.084064, m.getMass("I"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("J"), 0.0);
        Assert.assertEquals(128.094963, m.getMass("K"), 0.0);
        Assert.assertEquals(113.084064, m.getMass("L"), 0.0);
        Assert.assertEquals(131.040485, m.getMass("M"), 0.0);
        Assert.assertEquals(114.042927, m.getMass("N"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("O"), 0.0);
        Assert.assertEquals(97.052764, m.getMass("P"), 0.0);
        Assert.assertEquals(128.058578, m.getMass("Q"), 0.0);
        Assert.assertEquals(156.101111, m.getMass("R"), 0.0);
        Assert.assertEquals(87.032028, m.getMass("S"), 0.0);
        Assert.assertEquals(101.047679, m.getMass("T"), 0.0);
        Assert.assertEquals(150.95363, m.getMass("U"), 0.0);
        Assert.assertEquals(99.068414, m.getMass("V"), 0.0);
        Assert.assertEquals(186.079313, m.getMass("W"), 0.0);
        Assert.assertEquals(111.000000, m.getMass("X"), 0.0);
        Assert.assertEquals(163.063329, m.getMass("Y"), 0.0);
        Assert.assertEquals(128.550590, m.getMass("Z"), 0.0);
        Assert.assertEquals(1.007825, m.getMass("Hydrogen"), 0.0);
        Assert.assertEquals(12.000000, m.getMass("Carbon"), 0.0);
        Assert.assertEquals(14.003074, m.getMass("Nitrogen"), 0.0);
        Assert.assertEquals(15.994915, m.getMass("Oxygen"), 0.0);
        Assert.assertEquals(0.000549, m.getMass("Electron"), 0.0);
        Assert.assertEquals(17.00274, m.getMass("C_term"), 0.0);
        Assert.assertEquals(1.007825, m.getMass("N_term"), 0.0);
        Parameters lParameters = lMascotDatfile.getParametersSection();
        //3.Test ALL instance variables.
        Assert.assertEquals("NIH  (9M8N-4YN6-2J3A-Q46H-GC7B)", lParameters.getLicense());
        Assert.assertTrue(null == lParameters.getMP());
        Assert.assertTrue(null == lParameters.getNM());
        Assert.assertEquals("Submitted from Mouse XMD samples by Mascot Daemon on NIMH-BING", lParameters.getCom());
        Assert.assertTrue(null == lParameters.getIATOL());
        Assert.assertTrue(null == lParameters.getIA2TOL());
        Assert.assertTrue(null == lParameters.getIASTOL());
        Assert.assertTrue(null == lParameters.getIBTOL());
        Assert.assertTrue(null == lParameters.getIB2TOL());
        Assert.assertTrue(null == lParameters.getIBSTOL());
        Assert.assertTrue(null == lParameters.getIYTOL());
        Assert.assertTrue(null == lParameters.getIY2TOL());
        Assert.assertTrue(null == lParameters.getIYSTOL());
        Assert.assertTrue(null == lParameters.getSEG());
        Assert.assertTrue(null == lParameters.getSEGT());
        Assert.assertTrue(null == lParameters.getSEGTU());
        Assert.assertTrue(null == lParameters.getLTOL());
        Assert.assertEquals("8", lParameters.getTOL());
        Assert.assertEquals("ppm", lParameters.getTOLU());
        Assert.assertTrue(null == lParameters.getITH());
        Assert.assertEquals("0.8", lParameters.getITOL());
        Assert.assertEquals("Da", lParameters.getITOLU());
        Assert.assertEquals("1", lParameters.getPFA());
        Assert.assertEquals("Sprot", lParameters.getDatabase());
        Assert.assertEquals("Carbamidomethyl (C)", lParameters.getFixedModifications());
        Assert.assertEquals("Monoisotopic", lParameters.getMass());
        Assert.assertEquals("Trypsin", lParameters.getCleavage());
        Assert.assertEquals("\\\\nimh-maine\\lntdfs\\MS-MS Data\\blacklerar\\20120613_XMD_MouseNUPVsHistone\\XMD_Histone.RAW", lParameters.getFile());
        Assert.assertTrue(null == lParameters.getPeak());
        Assert.assertTrue(null == lParameters.getQue());
        Assert.assertTrue(null == lParameters.getTwo());
        Assert.assertEquals("MIS", lParameters.getSearch());
        Assert.assertEquals("blacklerar", lParameters.getUserName());
        Assert.assertEquals("blacklerar@mail.nih.gov", lParameters.getUserEmail());
        Assert.assertEquals("1+, 2+ and 3+", lParameters.getCharge());
        Assert.assertTrue(null == lParameters.getIntermediate());
        Assert.assertEquals("AUTO", lParameters.getReport());
        Assert.assertTrue(null == lParameters.getOverview());
        Assert.assertEquals("Mascot generic", lParameters.getFormat());
        Assert.assertEquals("1.01", lParameters.getFormVersion());
        Assert.assertTrue(null == lParameters.getFrag());
        Assert.assertEquals("Oxidation (M)", lParameters.getVariableModifications());
        Assert.assertTrue(null == lParameters.getUser01());
        Assert.assertTrue(null == lParameters.getUser02());
        Assert.assertTrue(null == lParameters.getUser03());
        Assert.assertTrue(null == lParameters.getUser04());
        Assert.assertTrue(null == lParameters.getUser05());
        Assert.assertTrue(null == lParameters.getUser06());
        Assert.assertTrue(null == lParameters.getUser07());
        Assert.assertTrue(null == lParameters.getUser08());
        Assert.assertTrue(null == lParameters.getUser09());
        Assert.assertTrue(null == lParameters.getUser10());
        Assert.assertTrue(null == lParameters.getUser11());
        Assert.assertTrue(null == lParameters.getUser12());
        Assert.assertTrue(null == lParameters.getPrecursor());
        Assert.assertEquals(". . . . . . . . . . . . Mammalia (mammals)", lParameters.getTaxonomy());
        Assert.assertTrue(null == lParameters.getAccession());
        Assert.assertTrue(null == lParameters.getReportType());
        Assert.assertTrue(null == lParameters.getSubcluster());
        Assert.assertTrue(null == lParameters.getICAT());
        Assert.assertEquals("Default", lParameters.getInstrument());
        Assert.assertTrue(null == lParameters.getErrorTolerant());
        Assert.assertTrue(null == lParameters.getFrames());
        Assert.assertTrue(null == lParameters.getCutout());
        Assert.assertFalse(lParameters.isDistillerMultiFile());
        Assert.assertTrue(lParameters.isDistillerProcessing());
        Assert.assertNull(lParameters.getDistillerMultiFileNames());
        //Test int[] rules (Make a sum, if the sum is'nt 72, fail() will be called.
        int[] lRules = lParameters.getRules();
        int sum = 0;
        for (int i = 0; i < lRules.length; i++) {
            sum += lRules[i];
        }
        Assert.assertEquals("lRules int[] was not formed correctly.", 58, sum);

        iProteinMap = lMascotDatfile.getProteinMap();
        Assert.assertEquals("(P56501) Mitochondrial uncoupling protein 3", iProteinMap.getProteinDescription("UCP3_MOUSE"));
        Assert.assertNotNull(iProteinMap.getProteinDescription("Q92576 (732-737)"));
        Assert.assertEquals("(P10111) Peptidyl-prolyl cis-trans isomerase A", iProteinMap.getProteinDescription("PPIA_RAT"));
        ProteinID lProteinID = iProteinMap.getProteinID("SURF4_PONAB");
        Assert.assertEquals("SURF4_PONAB", lProteinID.getAccession());
        Assert.assertEquals(30601.87, lProteinID.getMass(), 0.0);


        lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(1774, 3);

        //Test all the PeptideHit instance variables.
        Assert.assertEquals(1, lPeptideHit.getMissedCleavages());
        Assert.assertEquals(1863.969864, lPeptideHit.getPeptideMr(), 0.0);
        Assert.assertEquals(0.006384, lPeptideHit.getDeltaMass(), 0.0);
        Assert.assertEquals(6, lPeptideHit.getNumberOfIonsMatched());
        Assert.assertEquals("MVFINNIALAQMKNNK", lPeptideHit.getSequence());
        Assert.assertEquals(15, lPeptideHit.getPeaksUsedFromIons1());
        Assert.assertEquals(0, lPeptideHit.getVariableModificationsArray()[10]);
        Assert.assertEquals(7.3, lPeptideHit.getIonsScore(), 0.0);
        //ion series found:     -- 00000020000000000 --
        Assert.assertEquals(2, lPeptideHit.getIonSeriesFound()[6]);
        Assert.assertEquals(2, lPeptideHit.getIonSeriesFound()[3]);
        Assert.assertEquals(19, lPeptideHit.getIonSeriesFound().length);
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons2());
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons3());

        lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(0);
        Assert.assertEquals("LPPRC_MOUSE", lProteinHit.getAccession());
        Assert.assertEquals(0, lProteinHit.getFrameNumber());
        Assert.assertEquals(1175, lProteinHit.getStart());
        Assert.assertEquals(1190, lProteinHit.getStop());
        Assert.assertEquals(1, lProteinHit.getMultiplicity());


        lModificationList = lMascotDatfile.getModificationList();

        Vector lVariableModificationsVec = lModificationList.getVariableModifications();
        VariableModification lVariableModification = null;

        //First modification_Acetyl
        lVariableModification = (VariableModification) lVariableModificationsVec.get(0);
        Assert.assertEquals(15.994915, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Oxidation", lVariableModification.getType());
        Assert.assertEquals("M", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(1, lVariableModification.getModificationID());
        Assert.assertEquals("Mox", lVariableModification.getShortType());
    }

    public void testGetVersionTwoPointTwo() {
        String lFilename = "F016528.dat";
        lMascotDatfile = new MascotDatfile(getFullFilePath(lFilename));
        Assert.assertEquals(lFilename, lMascotDatfile.getFileName());
        h = lMascotDatfile.getHeaderSection();
        //3.Test ALL instance variables.
        Assert.assertEquals(33406, h.getSequences());
        Assert.assertEquals(33406, h.getSequences_after_tax());
        Assert.assertEquals(18467258, h.getResidues());
        Assert.assertEquals(31, h.getExecutionTime());
        Assert.assertEquals(1194533739, h.getDate());
        Assert.assertEquals("15:55:39", h.getTime());
        Assert.assertEquals(1000, h.getQueries());
        Assert.assertEquals(50, h.getMaxHits());
        Assert.assertEquals("2.2.1", h.getVersion());
        Assert.assertEquals("uniprot_sprot_complete_human_FORWARD_SHUFFLED_CONCAT_53.2.fasta", h.getRelease());
        Assert.assertEquals("119453370801", h.getTaskID());
        //No further variables or functions in the Header instance. If this works, this object works fine.

        m = lMascotDatfile.getMasses();
        //3.Test ALL instance variables.
        Assert.assertEquals(71.037114, m.getMass("A"), 0.0);
        Assert.assertEquals(114.53494, m.getMass("B"), 0.0);
        Assert.assertEquals(160.030649, m.getMass("C"), 0.0);
        Assert.assertEquals(115.026943, m.getMass("D"), 0.0);
        Assert.assertEquals(129.042593, m.getMass("E"), 0.0);
        Assert.assertEquals(147.068414, m.getMass("F"), 0.0);
        Assert.assertEquals(57.021464, m.getMass("G"), 0.0);
        Assert.assertEquals(137.058912, m.getMass("H"), 0.0);
        Assert.assertEquals(113.084064, m.getMass("I"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("J"), 0.0);
        Assert.assertEquals(173.124358, m.getMass("K"), 0.0);
        Assert.assertEquals(113.084064, m.getMass("L"), 0.0);
        Assert.assertEquals(147.0354, m.getMass("M"), 0.0);
        Assert.assertEquals(114.042927, m.getMass("N"), 0.0);
        Assert.assertEquals(0.000000, m.getMass("O"), 0.0);
        Assert.assertEquals(97.052764, m.getMass("P"), 0.0);
        Assert.assertEquals(128.058578, m.getMass("Q"), 0.0);
        Assert.assertEquals(156.101111, m.getMass("R"), 0.0);
        Assert.assertEquals(87.032028, m.getMass("S"), 0.0);
        Assert.assertEquals(101.047679, m.getMass("T"), 0.0);
        Assert.assertEquals(150.95363, m.getMass("U"), 0.0);
        Assert.assertEquals(99.068414, m.getMass("V"), 0.0);
        Assert.assertEquals(186.079313, m.getMass("W"), 0.0);
        Assert.assertEquals(111.000000, m.getMass("X"), 0.0);
        Assert.assertEquals(163.063329, m.getMass("Y"), 0.0);
        Assert.assertEquals(128.550590, m.getMass("Z"), 0.0);
        Assert.assertEquals(1.007825, m.getMass("Hydrogen"), 0.0);
        Assert.assertEquals(12.000000, m.getMass("Carbon"), 0.0);
        Assert.assertEquals(14.003074, m.getMass("Nitrogen"), 0.0);
        Assert.assertEquals(15.994915, m.getMass("Oxygen"), 0.0);
        Assert.assertEquals(0.000549, m.getMass("Electron"), 0.0);
        Assert.assertEquals(17.00274, m.getMass("C_term"), 0.0);
        Assert.assertEquals(43.01839, m.getMass("N_term"), 0.0);
        Parameters lParameters = lMascotDatfile.getParametersSection();
        //3.Test ALL instance variables.
        Assert.assertEquals("Licensed to: Vlaams Interuniversitair Instituut voor Biotechnologie, (2 processors).", lParameters.getLicense());
        Assert.assertTrue(null == lParameters.getMP());
        Assert.assertTrue(null == lParameters.getNM());
        Assert.assertEquals("peptizer_324_concat", lParameters.getCom());
        Assert.assertTrue(null == lParameters.getIATOL());
        Assert.assertTrue(null == lParameters.getIA2TOL());
        Assert.assertTrue(null == lParameters.getIASTOL());
        Assert.assertTrue(null == lParameters.getIBTOL());
        Assert.assertTrue(null == lParameters.getIB2TOL());
        Assert.assertTrue(null == lParameters.getIBSTOL());
        Assert.assertTrue(null == lParameters.getIYTOL());
        Assert.assertTrue(null == lParameters.getIY2TOL());
        Assert.assertTrue(null == lParameters.getIYSTOL());
        Assert.assertTrue(null == lParameters.getSEG());
        Assert.assertTrue(null == lParameters.getSEGT());
        Assert.assertTrue(null == lParameters.getSEGTU());
        Assert.assertTrue(null == lParameters.getLTOL());
        Assert.assertEquals("0.5", lParameters.getTOL());
        Assert.assertEquals("Da", lParameters.getTOLU());
        Assert.assertTrue(null == lParameters.getITH());
        Assert.assertEquals("0.5", lParameters.getITOL());
        Assert.assertEquals("Da", lParameters.getITOLU());
        Assert.assertEquals("1", lParameters.getPFA());
        Assert.assertEquals("Swissprot_532_FWSH", lParameters.getDatabase());
        Assert.assertEquals("Acetyl (N-term),Acetyl:2H(3) (K),Carbamidomethyl (C),Oxidation (M)", lParameters.getFixedModifications());
        Assert.assertEquals("Monoisotopic", lParameters.getMass());
        Assert.assertEquals("Arg-C/P", lParameters.getCleavage());
        Assert.assertEquals("F:\\be_ugent\\proteomics\\docs\\projects\\0711\\0711_peptizer_324\\searches\\mergefiles\\324\\mergefile_26102007_143142703.txt", lParameters.getFile());
        Assert.assertTrue(null == lParameters.getPeak());
        Assert.assertTrue(null == lParameters.getQue());
        Assert.assertTrue(null == lParameters.getTwo());
        Assert.assertEquals("MIS", lParameters.getSearch());
        Assert.assertEquals("Kenny", lParameters.getUserName());
        Assert.assertEquals("Kenny.helsens@ugent.Be", lParameters.getUserEmail());
        Assert.assertEquals("1+, 2+ and 3+", lParameters.getCharge());
        Assert.assertTrue(null == lParameters.getIntermediate());
        Assert.assertEquals("AUTO", lParameters.getReport());
        Assert.assertTrue(null == lParameters.getOverview());
        Assert.assertEquals("Mascot generic", lParameters.getFormat());
        Assert.assertEquals("1.01", lParameters.getFormVersion());
        Assert.assertTrue(null == lParameters.getFrag());
        Assert.assertEquals("Acetyl:2H(3) (N-term),Deamidated (NQ),Gln->pyro-Glu (N-term Q),Pyro-carbamidomethyl (N-term C)", lParameters.getVariableModifications());
        Assert.assertTrue(null == lParameters.getUser01());
        Assert.assertTrue(null == lParameters.getUser02());
        Assert.assertTrue(null == lParameters.getUser03());
        Assert.assertTrue(null == lParameters.getUser04());
        Assert.assertTrue(null == lParameters.getUser05());
        Assert.assertTrue(null == lParameters.getUser06());
        Assert.assertTrue(null == lParameters.getUser07());
        Assert.assertTrue(null == lParameters.getUser08());
        Assert.assertTrue(null == lParameters.getUser09());
        Assert.assertTrue(null == lParameters.getUser10());
        Assert.assertTrue(null == lParameters.getUser11());
        Assert.assertTrue(null == lParameters.getUser12());
        Assert.assertTrue(null == lParameters.getPrecursor());
        Assert.assertEquals("All entries", lParameters.getTaxonomy());
        Assert.assertTrue(null == lParameters.getAccession());
        Assert.assertTrue(null == lParameters.getReportType());
        Assert.assertTrue(null == lParameters.getSubcluster());
        Assert.assertTrue(null == lParameters.getICAT());
        Assert.assertEquals("ESI-TRAP", lParameters.getInstrument());
        Assert.assertTrue(null == lParameters.getErrorTolerant());
        Assert.assertTrue(null == lParameters.getFrames());
        Assert.assertTrue(null == lParameters.getCutout());
        Assert.assertFalse(lParameters.isDistillerMultiFile());
        Assert.assertFalse(lParameters.isDistillerProcessing());
        Assert.assertNull(lParameters.getDistillerMultiFileNames());
        //Test int[] rules (Make a sum, if the sum is'nt 72, fail() will be called.
        int[] lRules = lParameters.getRules();
        int sum = 0;
        for (int i = 0; i < lRules.length; i++) {
            sum += lRules[i];
        }
        Assert.assertEquals("lRules int[] was not formed correctly.", 72, sum);

        iProteinMap = lMascotDatfile.getProteinMap();
        Assert.assertEquals("SYNE2_HUMAN Nesprin-2 (Nuclear envelope spectrin repeat protein 2) (Syne-2)(Synaptic nuclear envelope protein 2) (Nucleus and actin connectingelement protein) (Protein NUANCE).", iProteinMap.getProteinDescription("Q8WXH0"));
        Assert.assertNotNull(iProteinMap.getProteinDescription("Q92576 (732-737)"));
        Assert.assertEquals("DISC1_HUMAN Disrupted in schizophrenia 1 protein.", iProteinMap.getProteinDescription("Q9NRI5"));
        ProteinID lProteinID = iProteinMap.getProteinID("Q9NRI5");
        Assert.assertEquals("Q9NRI5", lProteinID.getAccession());
        Assert.assertEquals(96848.5, lProteinID.getMass(), 0.0);


        lQueryToPeptideMap = lMascotDatfile.getQueryToPeptideMap();

        lPeptideHit = lQueryToPeptideMap.getPeptideHitOfOneQuery(250, 3);

        //Test all the PeptideHit instance variables.
        Assert.assertEquals(1, lPeptideHit.getMissedCleavages());
        Assert.assertEquals(2381.176971, lPeptideHit.getPeptideMr(), 0.0);
        Assert.assertEquals(-0.182799, lPeptideHit.getDeltaMass(), 0.0);
        Assert.assertEquals(3, lPeptideHit.getNumberOfIonsMatched());
        Assert.assertEquals("SILSKYDEELEGERPHSFR", lPeptideHit.getSequence());
        Assert.assertEquals(17, lPeptideHit.getPeaksUsedFromIons1());
        Assert.assertEquals(0, lPeptideHit.getVariableModificationsArray()[10]);
        Assert.assertEquals(6.49, lPeptideHit.getIonsScore(), 0.0);
        //ion series found:     -- 00000020000000000 --
        Assert.assertEquals(2, lPeptideHit.getIonSeriesFound()[6]);
        Assert.assertEquals(0, lPeptideHit.getIonSeriesFound()[3]);
        Assert.assertEquals(19, lPeptideHit.getIonSeriesFound().length);
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons2());
        Assert.assertEquals(0, lPeptideHit.getPeaksUsedFromIons3());

        lProteinHit = (ProteinHit) lPeptideHit.getProteinHits().get(0);
        Assert.assertEquals("O43290", lProteinHit.getAccession());
        Assert.assertEquals(0, lProteinHit.getFrameNumber());
        Assert.assertEquals(332, lProteinHit.getStart());
        Assert.assertEquals(350, lProteinHit.getStop());
        Assert.assertEquals(1, lProteinHit.getMultiplicity());


        lModificationList = lMascotDatfile.getModificationList();

        Vector lVariableModificationsVec = lModificationList.getVariableModifications();
        VariableModification lVariableModification = null;

        //First modification_Acetyl
        lVariableModification = (VariableModification) lVariableModificationsVec.get(0);
        Assert.assertEquals(45.029388, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Acetyl:2H(3)", lVariableModification.getType());
        Assert.assertEquals("N-term", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(1, lVariableModification.getModificationID());
        Assert.assertEquals("AcD3", lVariableModification.getShortType());

        lVariableModification = (VariableModification) lVariableModificationsVec.get(1);
        Assert.assertEquals(0.984009, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Deamidated", lVariableModification.getType());
        Assert.assertEquals("NQ", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(2, lVariableModification.getModificationID());
        Assert.assertEquals("Dam", lVariableModification.getShortType());

        //Thirth modification_Deamidation
        lVariableModification = (VariableModification) lVariableModificationsVec.get(2);
        Assert.assertEquals(-17.026535, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Gln->pyro-Glu", lVariableModification.getType());
        Assert.assertEquals("N-term Q", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(3, lVariableModification.getModificationID());
        Assert.assertEquals("Pyr", lVariableModification.getShortType());

        //Fourth modification_Oxidation
        lVariableModification = (VariableModification) lVariableModificationsVec.get(3);
        Assert.assertEquals(-17.026535, lVariableModification.getMass(), 0.0);
        Assert.assertEquals("Pyro-carbamidomethyl", lVariableModification.getType());
        Assert.assertEquals("N-term C", lVariableModification.getLocation());
        Assert.assertEquals(0.0, lVariableModification.getNeutralLoss(), 0.0);
        Assert.assertEquals(4, lVariableModification.getModificationID());
        Assert.assertEquals("Pyr", lVariableModification.getShortType());
    }


}
