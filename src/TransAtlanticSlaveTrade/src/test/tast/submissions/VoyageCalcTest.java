package test.tast.submissions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.junit.Test;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.TonType;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.VoyagesCalculation;

public class VoyageCalcTest extends TestCase {
	
	boolean RUN_ALL_MODE = true; 
	boolean  RUN_SPECIFIC_MODE = false;	
	Session session = null;
	Voyage voyage = null;
	Transaction tran = null;

	public void setUpSession() throws Exception {
			session = HibernateConn.getSession();
			tran = session.beginTransaction();
	}

	private void setValuesVoyage(Integer voyageId, String shipName) {	
		voyage = new Voyage();
		voyage.setVoyageid(voyageId);
		voyage.setShipname(shipName);
		int revision = 1;
		voyage.setRevision(revision);		
	}
	
	private void deleteVoyage(int voyageId ) {
		Voyage vNew = Voyage.loadByVoyageId(session, voyageId);	
		if (vNew != null) {
			session.delete(vNew);		
			//tran.commit();
		}	
	}
	
	private void saveVoyage(Voyage voyage) {
		session.save(voyage);
		tran.commit();
		session.close();		
	}
	
	public VoyageCalcTest(String testName) {
		super(testName);
	}	
	
	public static TestSuite suite() {
		return new VoyageCalcTest("testing").invokeTestSuite();
	}
	
	public TestSuite invokeTestSuite() {
		if (RUN_ALL_MODE) {
			return runAllTest();
		}else if (RUN_SPECIFIC_MODE) {
			return runSpecificTest(); 
		}
		else {		
			return null;
		}
	}    
	
	public TestSuite runSpecificTest() {
		//overrides so that test can be executed for localized change
		System.out.println("Running Specific Tests");
		TestSuite suite = new TestSuite(this.getClass().getName());
		//suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate2"));
		//suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate3"));
		//suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate4"));
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByPortdep"));
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByMajselpt"));
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByNullValues"));	
		//suite.addTest(new VoyageCalcTest("testCalculateTslmtimp"));
		//suite.addTest(new VoyageCalcTest("testCalculateTslmtimpWithAllVars"));
		//suite.addTest(new VoyageCalcTest("testCalculateValueMajbuyptVariableValues"));
		//suite.addTest(new VoyageCalcTest("testCalculateValueMajbuyptVariableValues1"));
		//suite.addTest(new VoyageCalcTest("testCalculateValueMajselptVariableValues"));
		//suite.addTest(new VoyageCalcTest("testCalculateXmImpflag"));
		//suite.addTest(new VoyageCalcTest("testTonmodTest"));
		suite.addTest(new VoyageCalcTest("testCalculateYearVariables"));
		
		return suite;
	}
	
	/*
	 * For all tests to run successfully, session has to be recreated each time
	 */
	public TestSuite runAllTest() {
		System.out.println("Running All Tests");
		TestSuite suite = new TestSuite(this.getClass().getName());
		//add all testcases here		
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByPortdep"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByMajselpt"));
		suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate2"));
		suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate3"));
		suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate4"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByPortdep"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByMajselpt"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByNullValues"));		
		suite.addTest(new VoyageCalcTest("testCalculateTslmtimpWithAllVars"));
		suite.addTest(new VoyageCalcTest("testCalculateValueMajbuyptVariableValues"));
		suite.addTest(new VoyageCalcTest("testCalculateValueMajbuyptVariableValues1"));
		suite.addTest(new VoyageCalcTest("testCalculateValueMajselptVariableValues"));
		suite.addTest(new VoyageCalcTest("testCalculateXmImpflag"));
		suite.addTest(new VoyageCalcTest("testTonmodTest"));
	
		return suite;
	}
	
	@Test
	public void testCalculatePtDepImpByPortdep(){
		try {
			setUpSession();
			deleteVoyage(99900);
			setValuesVoyage(new Integer(99900), "shipName_99900");
			long portId = 50105;//Rio Amazona
			Port portdep = Port.loadById(session, portId);			
			voyage.setPortdep(portdep);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculatePtDepImp();
			saveVoyage(voyage);
			assertEquals(voyage.getPtdepimp().getId().longValue(), portId);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculatePtDepImpByMajselpt(){
		try {	
			setUpSession();
			deleteVoyage(99901);
			setValuesVoyage(new Integer(99901), "shipName_99901");
			long portId = 50420;//Parati
			Port majselpt = Port.loadById(session, portId);
			voyage.setMajselpt(majselpt);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculatePtDepImp();
			saveVoyage(voyage);			
			assertNull(voyage.getPtdepimp());
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculatePtDepImpByNullValues(){
		try {	
			setUpSession();
			deleteVoyage(99902);
			setValuesVoyage(new Integer(99902), "shipName_99902");
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculatePtDepImp();
			saveVoyage(voyage);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateImputedValueFate2(){
		try {	
			setUpSession();
			deleteVoyage(99903);
			setValuesVoyage(new Integer(99903), "shipName_99903");
			//add test specific variables in voyage object
			long rigId = 15;
			Fate fate = Fate.loadById(session, rigId);
			voyage.setFate(fate);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculateFate2();
			saveVoyage(voyage);
			assertEquals(voyage.getFate2().getId().longValue(), 1);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateImputedValueFate3(){
		try {	
			setUpSession();
			deleteVoyage(99904);
			setValuesVoyage(new Integer(99904), "shipName_99904");
			//add test specific variables in voyage object
			long rigId = 15;
			Fate fate = Fate.loadById(session, rigId);
			voyage.setFate(fate);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculateFate3();
			saveVoyage(voyage);
			assertEquals(voyage.getFate3().getId().longValue(), 4);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateImputedValueFate4(){
		try {	
			setUpSession();
			deleteVoyage(99905);
			setValuesVoyage(new Integer(99905), "shipName_99905");
			//add test specific variables in voyage object
			long rigId = 15;
			Fate fate = Fate.loadById(session, rigId);
			voyage.setFate(fate);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculateFate4();
			saveVoyage(voyage);
			assertEquals(voyage.getFate4().getId().longValue(), 3);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	/*
	 * tslmtimp:  Imputed total of slaves embarked for mortality calculation
	 * vymrtimp:  Imputed number of slaves died in  middle passage
	 * vymrtrat:  Slaves died on voyage/Slaves embarked	  
	 */	
	public void testCalculateTslmtimpWithSladvoy(){
		try {	
			setUpSession();
			deleteVoyage(99900);
			setValuesVoyage(new Integer(99900), "shipName_99900");
			voyage.setSladvoy(new Integer(75));
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculateMortality();
			saveVoyage(voyage);
			//assertEquals(voyage.getPtdepimp().getId().longValue(), portId);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateTslmtimpWithAllVars(){
		try {
			setUpSession();
			deleteVoyage(99900);
			setValuesVoyage(new Integer(99900), "shipName_99900");
			voyage.setTslavesd(new Integer(100));
			voyage.setSladvoy(new Integer(200));
			//voyage.setSlaarriv(new Integer(60));
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculateMortality();
			saveVoyage(voyage);
			//assertEquals(voyage.getPtdepimp().getId().longValue(), portId);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	@Test
	public void testCalculateValueMajbuyptVariableValues(){
		try {
			/*setUpSession();
			deleteVoyage(99900);*/
			long[] port1Array = {10112, 10480, 0, 42001, 50303};
			long[] port2Array = {10106, 0, 37010, 80199, 60661};
			long[] port3Array = {35113, 32140, 32120, 21402, 0};
			Integer[] shipName = {99900, 99901, 99902, 99903, 99904};
			Integer[] ncar13Array = {10, 20, 30, 40, 50};
			Integer[] ncar15Array = {50, 60, 70, 80, 90};
			Integer[] ncar17Array = {100, 200, 300, 400, 500};
			Integer[] tslavesdArray = {10, 10, 10, 10, 10};
			Integer[] tslavespArray = {20, 20, 20, 20, 20};
			Integer[] rsltArray = {35113, 10480, 37010, 21402, 60661};
			
			for (int i=0; i < port1Array.length; i++){
				setUpSession();
				deleteVoyage((Integer)shipName[i]);
				setValuesVoyage((Integer)shipName[i], "shipName_"+ shipName[i]);
				Port plac1tra = Port.loadById(session, (long)port1Array[i]);
				Port plac2tra = Port.loadById(session, (long)port2Array[i]);
				Port plac3tra = Port.loadById(session, (long)port3Array[i]);
				if (plac1tra != null) {
					voyage.setPlac1tra(plac1tra);
				}
				if (plac2tra != null) {
					voyage.setPlac2tra(plac2tra);
				}
				if (plac3tra != null) {
					voyage.setPlac3tra(plac3tra);
				}
				voyage.setNcar13(ncar13Array[i]);
				voyage.setNcar15(ncar15Array[i]);
				voyage.setNcar17(ncar17Array[i]);
				voyage.setTslavesd(tslavesdArray[i]);
				voyage.setTslavesp(tslavespArray[i]);
				
				VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
				voyageCalc.calculateMjbyptimp();
				saveVoyage(voyage);
				assertNull(voyage.getMajbuypt());				
				//Ist result majbuypt = plac3tra=35113, Spanish Town 
				//2nd result majbuypt= plac1tra = 10480, Falmouth (Eng.) 
				//3rd result majbuypt= plac2tra = 37010, St.Croix
				//4th result majbuypt= plac3tra = 21402 Sunbury
				//5th result majbuypt= plac3tra Annobon
				voyage = null;
			}
			
			//rslt_d = ncartot.doubleValue()/tslavesd_int;			
			//assertEquals(voyage.getMajbuypt().getId().longValue(), 50105);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	@Test
	public void testCalculateValueMajbuyptVariableValues1(){
		try {		
			long[] port1Array = {10112, 10112, 10480, 42001, 0, 10112};
			long[] port2Array = {10106, 0, 37010, 80199, 0, 37010};
			long[] port3Array = {35113, 32140, 0, 32120, 0, 35113};
			Integer[] shipName = {99900, 99901, 99902, 99903, 99904, 99905};
			Integer[] ncar13Array = {0, 20, 30, 40, 0, 10};
			Integer[] ncar15Array = {50, 0, 70, 80, 0, 10};
			Integer[] ncar17Array = {100, 200, 0, 400, 0, 10};
			Integer[] tslavesdArray = {null, 10, 10, null, null, null};
			Integer[] tslavespArray = {null, 20, 20, 20, null, null};
			Integer[] rsltArray = {35113, 10112, 37010, 42001, null, 35113};
			for (int i=0; i < port1Array.length; i++){
				setUpSession();
				deleteVoyage((Integer)shipName[i]);
				setValuesVoyage((Integer)shipName[i], "shipName_"+ shipName[i]);
				Port plac1tra = Port.loadById(session, (long)port1Array[i]);
				Port plac2tra = Port.loadById(session, (long)port2Array[i]);
				Port plac3tra = Port.loadById(session, (long)port3Array[i]);
				if (plac1tra != null) {
					voyage.setPlac1tra(plac1tra);
				}
				if (plac2tra != null) {
					voyage.setPlac2tra(plac2tra);
				}
				if (plac3tra != null) {
					voyage.setPlac3tra(plac3tra);
				}
				voyage.setNcar13(ncar13Array[i]);
				voyage.setNcar15(ncar15Array[i]);
				voyage.setNcar17(ncar17Array[i]);
				voyage.setTslavesd(tslavesdArray[i]);
				voyage.setTslavesp(tslavespArray[i]);
				
				VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
				voyageCalc.calculateMjbyptimp();
				saveVoyage(voyage);		
				if (voyage.getMajbuypt() != null) {
					assertEquals(voyage.getMajbuypt().getId().longValue(), (long)rsltArray[i]);
				}
				voyage = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateValueMajselptVariableValues(){
		try {			
			//double rslt_sla = slastot.doubleValue()/slaarriv_doub;			 
			long[] sla1portArray = {10112, 0, 10112, 10480, 0, 10112};
			long[] adpsale1Array = {10106, 37010, 0, 80199, 0, 0};
			long[] adpsale2Array = {35113, 32140, 32120, 0, 35113, 0};
			Integer[] shipName = {99900, 99901, 99902, 99903, 99904, 99905};
			Integer[] slas32Array = {20, null, 30, 40, null, 50};
			Integer[] slas36Array = {10, 20, null, 30, null, null};
			Integer[] slas39Array = {5, 10, 20, null, 30, null};					
			Integer[] slaarrivArray = {null, 1000, 2000, null, 3000, 500};
			Integer[] rsltArray = {10112, null, 10112, 80199, 35113, 10112};
						
			for (int i=0; i < sla1portArray.length; i++){
				setUpSession();
				deleteVoyage((Integer)shipName[i]);
				setValuesVoyage((Integer)shipName[i], "shipName_"+ shipName[i]);
				Port sla1port = Port.loadById(session, (long)sla1portArray[i]);
				Port adpsale1 = Port.loadById(session, (long)adpsale1Array[i]);
				Port adpsale2 = Port.loadById(session, (long)adpsale2Array[i]);
				voyage.setSla1port(sla1port);
				voyage.setAdpsale1(adpsale1);
				voyage.setAdpsale2(adpsale2);
				
				voyage.setSlas32(slas32Array[i]);
				voyage.setSlas36(slas36Array[i]);
				voyage.setSlas39(slas39Array[i]);
			
				voyage.setSlaarriv(slaarrivArray[i]);
				
				VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
				//voyageCalc.calculateMajselpt();
				saveVoyage(voyage);	
				System.out.println("voyage after:" + voyage.toString());
				if (voyage.getMajselpt() != null) {
					assertEquals(voyage.getMajselpt().getId().longValue(), (long)rsltArray[i]);
				}
				voyage = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateXmImpflag(){
		try {			
			Integer[] shipName = {99900, 99901, 99902, 99903, 99904, 99905};
			long[] rigArray = {29, 0, 90, 10, 25, 0};	
			Integer[] yearamArray = {1500, 1720, 1500, 1710, 1800, null};
			long[] majbyimpArray = {20, 0, 60100, 60600, 0, 0};
			long[] mjselimpArray = {80400, 0, 36200, 0, 50200, 0};
			long[] mjselimp1Array = {80400, 0, 36200, 0, 50200, 0};
			long[] natinimpArray = {20, 0, 30, 15, 20, 9};

			double[] rsltArray = {1, 130, 3, 49, 62, 0};
						
			for (int i=0; i < rigArray.length; i++){
				setUpSession();
				deleteVoyage((Integer)shipName[i]);
				setValuesVoyage((Integer)shipName[i], "shipName_"+ shipName[i]);
				VesselRig rig = VesselRig.loadById(session, (long)rigArray[i]);
				voyage.setRig(rig);
				Region majbyimp = Region.loadById(session, (long)majbyimpArray[i]);
				voyage.setMajbyimp(majbyimp);
				Region mjselimp = Region.loadById(session, (long)mjselimpArray[i]);
				voyage.setMjselimp(mjselimp);
				Area mjselimp1 = Area.loadById(session, (long)mjselimp1Array[i]);
				voyage.setMjselimp1(mjselimp1);
				Nation natinimp = Nation.loadById(session, (long)natinimpArray[i]);
				voyage.setNatinimp(natinimp);				
				voyage.setYearam((Integer)yearamArray[i]);
								
				VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
				voyageCalc.calculateXmImpflag();
				saveVoyage(voyage);			
				if (voyage.getXmimpflag() != null) {
					assertEquals(voyage.getXmimpflag().doubleValue(), (double)rsltArray[i]);
				}
				voyage = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBinarySearch(){
		int[] rigArray = {29, 42, 43, 54, 59, 61, 65, 80, 86};
		int rig = 29;
		int rig1 = 30;
		   
		int a = Arrays.binarySearch(rigArray, rig);
		int b = Arrays.binarySearch(rigArray, rig1);
		
		System.out.println("a:" + a);
		System.out.println("b:" + b);
		
	}
	
	@Test
	public void testCalculateImputedVariables(){
		try{
			setUpSession();
			deleteVoyage(99910);
			setValuesVoyage(new Integer(99910), "shipName_99910");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTonmodTest(){
		try {	
			setUpSession();
			deleteVoyage(99901);
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99901), "shipName_99901");
			
			//Input variables for second region calculation
			voyage.setTontype(TonType.loadById(session, 4)); 
			voyage.setTonnage(100); 
			voyage.setYearam(1770); 
			voyage.setNatinimp(null);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculateTonmod();
			saveVoyage(voyage);
			assertEquals(voyage.getTonmod(), 182.3f);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculateYearVariables(){
		try {	
			setUpSession();
			deleteVoyage(99901);
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99901), "shipName_99901");
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			Integer obj = new Integer(1);
			voyage.setDatedep(df.parse("1800-01-01"));
			voyage.setDatebuy(df.parse("1801-01-01"));
			voyage.setDateleftafr(df.parse("1802-01-01"));
			voyage.setDateland1(df.parse("1803-01-01"));
			voyage.setDatedepam(df.parse("1804-01-01"));
			voyage.setDateend(df.parse("1805-01-01"));
			
			voyage.setDatedepc(obj);
			voyage.setD1slatrc(obj);
			voyage.setDlslatrc(obj);
			voyage.setDatarr34(obj);
			voyage.setDdepamc(obj); 
			voyage.setDatarr45(obj);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			voyageCalc.calculateYearVariables();
			saveVoyage(voyage);
			//assertEquals(voyage.getTonmod(), 182.3f);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
