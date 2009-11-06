package test.tast.submissions;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.junit.Before;
import org.junit.Test;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.TonType;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.VoyagesCalculation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
public class VoyageCalcTest2 extends TestCase {
	
	//boolean RUN_ALL_MODE = false; 
	//boolean  RUN_SPECIFIC_MODE = true;	
	
	Session session = null;
	Voyage voyage = null;
	Transaction tran = null;
	
	@Before
	public void setUp() throws Exception {	
		if (session == null) {
			session = HibernateConn.getSession();
			tran = session.beginTransaction();
		}			
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
	
	public VoyageCalcTest2(String testName) {
		super(testName);
	}	
	/*
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
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByPortdep"));
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByMajselpt"));
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByNullValues"));	
		//suite.addTest(new VoyageCalcTest("testCalculateTslmtimp"));
		//suite.addTest(new VoyageCalcTest("testCalculateTslmtimpWithAllVars"));
		suite.addTest(new VoyageCalcTest("testTest"));
		
			
		return suite;
	}
	
	/*
	 * For all tests to run successfully, session has to be recreated each time
	 * /
	public TestSuite runAllTest() {
		System.out.println("Running All Tests");
		TestSuite suite = new TestSuite(this.getClass().getName());
		//add all testcases here
		suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate2"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByPortdep"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByMajselpt"));
		return suite;
	}
	*/
	
	
	
	//Helper Functions
	//recode
	public void testDefValInt()
	{
	  Integer I1 =null;
	  Integer I2=10;
	  Integer result=null;
	  VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
	  
	   result= voyageCalc.defVal(I1, 5);
	   assertEquals((Integer)result, (Integer)5);
	   
	   result= voyageCalc.defVal(I2, 1);
	   assertEquals(result, (Integer)10);
	}
	
	public void testDefValDouble()
	{
	  Double D1 =null;
	  Double D2=10.5;
	  Double result=null;
	  VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
	  
	   result= voyageCalc.defVal(D1, 5.5);
	   assertEquals(result, 5.5);
	   
	   result= voyageCalc.defVal(D2, 1d);
	   assertEquals(result, 10.5);
	}
	
	public void testRoundLong()
	{
		Long L1=5l;
		Long L2=null;
		Long result=null;
		
		 VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
		 
		 result=voyageCalc.round(L1,0);
		 assertEquals(result, (Long)5l);
		 
		 result=voyageCalc.round(L2,0);
		 assertNull(result);
	}
	
	public void testRoundDouble()
	{
		Double D1=5d;
		Double D2=10.5;
		Double D3=20.7;
		Double D4=20.3;
		Double D5=null;
		Double result=null;
		
		 VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
		 
		 result=voyageCalc.round(D1,0);
		 assertEquals(result, (Double)5d);
		 
		 result=voyageCalc.round(D2,0);
		 assertEquals(result, (Double)11d);
		 
		 result=voyageCalc.round(D3,0);
		 assertEquals(result, (Double)21d,0);
		 
		 result=voyageCalc.round(D4,0);
		 assertEquals(result, (Double)20d);
		 
		 result=voyageCalc.round(D5,0);
		 assertNull(result);
	}
	
	public void testDateDiff()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		
		try {
			Date D1 = sdf.parse("2009-05-11 10:00");
			Date D2 = sdf.parse("2009-05-16 20:00");
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
			
			Integer result = voyageCalc.dateDiff(D1, D2);
			assertEquals(result, (Integer)5);
			
			result = voyageCalc.dateDiff(D2, D1);
			assertEquals(result, new Integer("5"));
			
			result = voyageCalc.dateDiff(D1, null);
			assertNull(result);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void testRecode()
	{
		VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
		
		//Include Endpoints
		ArrayList inputs = new ArrayList();
		inputs.add(new Integer ("10"));
		inputs.add(new Integer ("15"));
		inputs.add(new Integer ("35"));
		inputs.add(new Integer ("0"));
		
		ArrayList ranges = new ArrayList();
		ranges.add(new Integer[]{new Integer("1"), new Integer("10"), new Integer("100")});
		ranges.add(new Integer[]{new Integer("11"), new Integer("20"), new Integer("200")});
		ranges.add(new Integer[]{new Integer("21"), new Integer("35"), new Integer("300")});
		
		ArrayList results = voyageCalc.recode(inputs, ranges, true);
		
		assertEquals((Integer)results.get(0), new Integer("100"));
		assertEquals((Integer)results.get(1), new Integer("200"));
		assertEquals((Integer)results.get(2), new Integer("300"));
		assertEquals((Integer)results.get(3), new Integer("-1"));
		
		//Do Not Include Endpoints
		inputs = new ArrayList();
		inputs.add(new Integer ("10"));
		inputs.add(new Integer ("15"));
		inputs.add(new Integer ("35"));
		inputs.add(new Integer ("0"));
		
		ranges = new ArrayList();
		ranges.add(new Integer[]{new Integer("1"), new Integer("10"), new Integer("100")});
		ranges.add(new Integer[]{new Integer("11"), new Integer("20"), new Integer("200")});
		ranges.add(new Integer[]{new Integer("21"), new Integer("36"), new Integer("300")});
		
		results = voyageCalc.recode(inputs, ranges, false);
		
		assertEquals((Integer)results.get(0), new Integer("-1"));
		assertEquals((Integer)results.get(1), new Integer("200"));
		assertEquals((Integer)results.get(2), new Integer("300"));
		assertEquals((Integer)results.get(3), new Integer("-1"));
	}
	
	public void testNatinImpRet(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			long nat = 1;
			Nation nation = Nation.loadById(session, nat);
			voyage.setNational(nation);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateNatinimp();
			
			saveVoyage(voyage);
			assertEquals(voyage.getNatinimp().getId(), new Long("3"));
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void testNatinImpNoRet(){
		try {	
			deleteVoyage(99901);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99901), "shipName_99901");
			long nat = 0;
			Nation nation = Nation.loadById(session, nat);
			voyage.setNational(nation);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateNatinimp();
			
			saveVoyage(voyage);
			assertNull(voyage.getNatinimp());
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testVoyageLens(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date datedep=sdf.parse("1829-06-11 00:00");
			Date dateland1=sdf.parse("1830-03-31 00:00");
			Date dateleftafr=sdf.parse("2009-05-27 00:00");
			
			voyage.setDatedep(datedep);
			voyage.setDateland1(dateland1);
			voyage.setDateleftafr(dateleftafr);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateVoyLengths();
			
			saveVoyage(voyage);
			assertEquals(voyage.getVoy1imp(), new Integer("293"));
		    //assertEquals(voyage.getVoy2imp(), new Integer("10"));
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testVoyageLensWithNull(){
		try {	
			deleteVoyage(99901);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99901), "shipName_99901");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date datedep=null;
			Date dateland1=sdf.parse("2009-05-17 23:59");
			Date dateleftafr=sdf.parse("2009-05-27 00:00");
			
			voyage.setDatedep(datedep);
			voyage.setDateland1(dateland1);
			voyage.setDateleftafr(dateleftafr);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateVoyLengths();
			
			saveVoyage(voyage);
			assertNull(voyage.getVoy1imp());
		    assertNull(voyage.getVoy2imp());
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testRegions1(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			//Input variables
			
			voyage.setPlaccons(Port.loadById(session, 10201));  //not in db
			voyage.setPlacreg(Port.loadById(session, 10299)); // in db
			voyage.setPortdep(null); 
			voyage.setPtdepimp(null);   
			voyage.setEmbport(null); 
			voyage.setEmbport2(null);
			voyage.setPlac1tra(null); 
			voyage.setPlac2tra(null);
			voyage.setPlac3tra(null);
			voyage.setMajbuypt(null);
			voyage.setMjbyptimp(null);
			voyage.setArrport(null);
			voyage.setArrport2(null);
			voyage.setSla1port(null);
			voyage.setAdpsale1(null);
			voyage.setAdpsale2(null);
			voyage.setMajselpt(null);
			voyage.setMjslptimp(null);
			voyage.setPortret(null);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateValuesRegion1();
			
			saveVoyage(voyage);
			assertNull(voyage.getConstreg());
			assertEquals(voyage.getRegisreg().getId(), new Long("10200"));
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testRegions2(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			
			//Input variables for second region calculation
			voyage.setPortdep(Port.loadById(session, 10101));  
			voyage.setPtdepimp(Port.loadById(session, 2)); //look-up not in DB 
			voyage.setMjbyptimp(null); 
			voyage.setMjslptimp(null);  
			voyage.setPortret(null);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateValuesRegion2();
			
			saveVoyage(voyage);
			assertNull(voyage.getDeptreg1());
			assertNull(voyage.getDeptregimp1());			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public void testTonmod(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			
			//Input variables for second region calculation
			voyage.setTontype(TonType.loadById(session, 4)); 
			voyage.setTonnage(300); 
			voyage.setYearam(1770); 
			voyage.setNatinimp(null);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateTonmod();
			
			saveVoyage(voyage);
			assertEquals(voyage.getTonmod(), 343.1f);
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testTonmodTest2(){
		try {	
			deleteVoyage(99901);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99901), "shipName_99901");
			
			//Input variables for second region calculation
			voyage.setTontype(TonType.loadById(session, 4));
			voyage.setTonnage(null); 
			voyage.setYearam(1770); 
			voyage.setNatinimp(null);
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateTonmod();
			
			saveVoyage(voyage);
			//assertEquals(voyage.getTonmod(), 9999f); No loger valid
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void testYear(){
		try {	
			deleteVoyage(99900);			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");			
			//Input variables for second region calculation
			voyage.setYearam(1770); 
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);		
			voyageCalc.calculateValuesYear();			
			saveVoyage(voyage);
			assertEquals(voyage.getYear5(), new Integer("49"));
			assertEquals(voyage.getYear10(), new Integer("27"));
			assertEquals(voyage.getYear25(), new Integer("11"));
			assertEquals(voyage.getYear100(), new Integer("1700"));
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 	
	public void testSlavesEmbarkDisembark(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			
			//Input variables for second region calculation
			voyage.setNcar13(10);
			voyage.setNcar13(20);
			voyage.setNcar13(30);
			
			voyage.setSlas32(10);
			voyage.setSlas36(20);
			voyage.setSlas39(30);
			
			voyage.setXmimpflag(127d);
			voyage.setTslavesp(100);
			voyage.setSlaarriv(75);
			voyage.setTslavesd(100);
			voyage.setFate2(FateSlaves.loadById(session, 5)); 
;
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateSlavesEmbarkDisembark();
			
			saveVoyage(voyage);
			assertEquals(voyage.getSlaximp(), new Integer("100"));
			assertEquals(voyage.getSlamimp(), new Integer("75"));
						
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testSlavesEmbarkDisembarkTest2(){
		try {	
			deleteVoyage(99901);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99901), "shipName_99901");
			
			//Input variables for second region calculation
			voyage.setNcar13(10);
			voyage.setNcar13(20);
			voyage.setNcar13(30);
			
			voyage.setSlas32(10);
			voyage.setSlas36(20);
			voyage.setSlas39(30);
			
			voyage.setXmimpflag(127d);
			voyage.setTslavesp(100);
			voyage.setSlaarriv(75);
			voyage.setTslavesd(100);
			voyage.setFate2(FateSlaves.loadById(session, 2)); 
;
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateSlavesEmbarkDisembark();
			
			saveVoyage(voyage);
			assertEquals(voyage.getSlaximp(),new Integer(100));
			assertEquals(voyage.getSlamimp(),new Integer(75));
						
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testPeople(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			
			//Input variables for people calculation
			voyage.setMen1(100);
			voyage.setWomen1(100);
			voyage.setBoy1(100);
			voyage.setGirl1(100);
			voyage.setChild1(100);
			voyage.setInfant1(100);
			voyage.setAdult1(100);
			voyage.setMen4(400);
			voyage.setWomen4(400);
			voyage.setBoy4(400);
			voyage.setGirl4(400);
			voyage.setChild4(400);
			voyage.setInfant4(400);
			voyage.setAdult4(400);
			voyage.setMen5(500);
			voyage.setWomen5(500);
			voyage.setBoy5(500);
			voyage.setGirl5(500);
			voyage.setChild5(500);
			voyage.setAdult5(500);
			voyage.setMale1(100);
			voyage.setFemale1(100);
			voyage.setMale4(400);
			voyage.setFemale4(400);
			voyage.setMale5(500);
			voyage.setFemale5(500);
			voyage.setTslavesd(500);
			voyage.setTslavesp(10);
			voyage.setMen3(300);
			voyage.setWomen3(300);
			voyage.setBoy3(300);
			voyage.setGirl3(300);
			voyage.setChild3(300);
			voyage.setInfant3(300);
			voyage.setinfantm3(300);
			voyage.setInfantf3(300);
			voyage.setAdult3(300);
			voyage.setMen6(600);
			voyage.setWomen6(600);
			voyage.setBoy6(600);
			voyage.setGirl6(600);
			voyage.setChild6(600);
			voyage.setAdult6(600);
			voyage.setMale3(300);
			voyage.setFemale3(300);
			voyage.setMale6(300);
			voyage.setFemale6(600);
			voyage.setSlaarriv(10);
			voyage.setMen2(200);
			voyage.setWomen2(200);
			voyage.setBoy2(200);
			voyage.setGirl2(200);
			voyage.setChild2(200);
			voyage.setAdult2(200);
			voyage.setMale2(200);
			voyage.setFemale2(200);
			 

			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateValuesPeople();
			
			saveVoyage(voyage);
			assertEquals(voyage.getAdlt1imp(), new Integer("3000"));
			assertEquals(voyage.getBoyrat1(), new Double("0.25"));
			//assertNull(voyage.getSlamimp());
						
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testPeopleNull(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");
			
			//Input variables for people calculation
			voyage.setMen1(null);
			voyage.setWomen1(null);
			voyage.setBoy1(null);
			voyage.setGirl1(null);
			voyage.setChild1(null);
			voyage.setInfant1(null);
			voyage.setAdult1(null);
			voyage.setMen4(null);
			voyage.setWomen4(null);
			voyage.setBoy4(null);
			voyage.setGirl4(null);
			voyage.setChild4(null);
			voyage.setInfant4(null);
			voyage.setAdult4(null);
			voyage.setMen5(null);
			voyage.setWomen5(null);
			voyage.setBoy5(null);
			voyage.setGirl5(null);
			voyage.setChild5(null);
			voyage.setAdult5(null);
			voyage.setMale1(null);
			voyage.setFemale1(null);
			voyage.setMale4(null);
			voyage.setFemale4(null);
			voyage.setMale5(null);
			voyage.setFemale5(null);
			voyage.setTslavesd(null);
			voyage.setTslavesp(null);
			voyage.setMen3(null);
			voyage.setWomen3(null);
			voyage.setBoy3(null);
			voyage.setGirl3(null);
			voyage.setChild3(null);
			voyage.setInfant3(null);
			voyage.setinfantm3(null);
			voyage.setInfantf3(null);
			voyage.setAdult3(null);
			voyage.setMen6(null);
			voyage.setWomen6(null);
			voyage.setBoy6(null);
			voyage.setGirl6(null);
			voyage.setChild6(null);
			voyage.setAdult6(null);
			voyage.setMale3(null);
			voyage.setFemale3(null);
			voyage.setMale6(null);
			voyage.setFemale6(null);
			voyage.setSlaarriv(null);
			voyage.setMen2(null);
			voyage.setWomen2(null);
			voyage.setBoy2(null);
			voyage.setGirl2(null);
			voyage.setChild2(null);
			voyage.setAdult2(null);
			voyage.setMale2(null);
			voyage.setFemale2(null);
			 

			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateValuesPeople();
			
			saveVoyage(voyage);
			assertNull(voyage.getAdlt1imp());
			assertNull(voyage.getBoyrat1());
			//assertNull(voyage.getSlamimp());
						
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public void testPeopleTest2(){
		try {	
			deleteVoyage(99901);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99901), "shipName_99901");
			
			//Input variables for second region calculation
			//voyage.setMen1(100);
			//voyage.setWomen1(100);
			voyage.setBoy1(100);
			voyage.setGirl1(100);
			voyage.setChild1(100);
			voyage.setInfant1(100);
			//voyage.setAdult1(100);
			//voyage.setMen4(400);
			//voyage.setWomen4(400);
			voyage.setBoy4(400);
			voyage.setGirl4(400);
			voyage.setChild4(400);
			voyage.setInfant4(400);
			//voyage.setAdult4(400);
			//voyage.setMen5(500);
			//voyage.setWomen5(500);
			voyage.setBoy5(500);
			voyage.setGirl5(500);
			voyage.setChild5(500);
			//voyage.setAdult5(500);
			voyage.setMale1(100);
			voyage.setFemale1(100);
			voyage.setMale4(400);
			voyage.setFemale4(400);
			voyage.setMale5(500);
			voyage.setFemale5(500);
			voyage.setTslavesd(5000);
			voyage.setTslavesp(10);
			voyage.setMen3(300);
			voyage.setWomen3(300);
			voyage.setBoy3(300);
			voyage.setGirl3(300);
			voyage.setChild3(300);
			voyage.setInfant3(300);
			voyage.setinfantm3(300);
			voyage.setInfantf3(300);
			voyage.setAdult3(300);
			voyage.setMen6(600);
			voyage.setWomen6(600);
			voyage.setBoy6(600);
			voyage.setGirl6(600);
			voyage.setChild6(600);
			voyage.setAdult6(600);
			voyage.setMale3(300);
			voyage.setFemale3(300);
			voyage.setMale6(300);
			voyage.setFemale6(600);
			voyage.setSlaarriv(2000);
			voyage.setMen2(200);
			voyage.setWomen2(200);
			voyage.setBoy2(200);
			voyage.setGirl2(200);
			voyage.setChild2(200);
			voyage.setAdult2(200);
			voyage.setMale2(200);
			voyage.setFemale2(200);
			 
;
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateValuesPeople();
			
			saveVoyage(voyage);
			assertNull(voyage.getAdlt1imp());
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testYearamNULL(){
		try {	
			deleteVoyage(99900);
			
			//add test specific variables in voyage object
			setValuesVoyage(new Integer(99900), "shipName_99900");

			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
			
			voyageCalc.calculateYearVariables();
			
			saveVoyage(voyage);
			assertNull(voyage.getYearam());
			
			
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testOperators()
	{
		
		VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);			
		
		Integer I1 = 10;
		Integer I2 = 5;
		Integer I3 = null;
		Integer I4=10;
		Double D1=5.5d;
		Double D2=10.5d;
		Double D3=null;
		Double D4=5.5d;
		
		assertEquals(voyageCalc.add(new Integer[]{I1, I2}), new Integer(15));
		assertEquals(voyageCalc.add(new Integer[]{I1, 2}), new Integer(12));
		assertNull(voyageCalc.add(new Integer[]{I1, I3}));
		
		assertEquals(voyageCalc.sub(I1, I2), new Integer(5));
		assertEquals(voyageCalc.sub(I1, 2), new Integer(8));
		assertNull(voyageCalc.sub(I1, I3));
		
		assertEquals(voyageCalc.mult(I1, I2), new Integer(50));
		assertEquals(voyageCalc.mult(I1, 2), new Integer(20));
		assertNull(voyageCalc.mult(I1, I3));
		
		assertEquals(voyageCalc.mult(D1, D2), new Double(57.75));
		assertEquals(voyageCalc.mult(D1, 2d), new Double(11));
		assertNull(voyageCalc.mult(D1, D3));
		
		assertEquals(voyageCalc.div(I1, I2), new Double(2));
		assertEquals(voyageCalc.div(I1, 2), new Double(5));
		assertNull(voyageCalc.div(I1, I3));
		
		assertEquals(voyageCalc.div(D2, D1), new Double(1.9090909090909092));
		assertEquals(voyageCalc.div(D1, 2d), new Double(2.75));
		assertNull(voyageCalc.div(D1, D3));
		
		assertEquals(voyageCalc.eq(I1, I4), true);
		assertEquals(voyageCalc.eq(I1, 10), true);
		assertEquals(voyageCalc.eq(I1, I2), false);
		assertEquals(voyageCalc.eq(I1, I3), false);
		
		assertEquals(voyageCalc.eq(D1, D4), true);
		assertEquals(voyageCalc.eq(D1, 5.5), true);
		assertEquals(voyageCalc.eq(D1, 2d), false);
		assertEquals(voyageCalc.eq(D1, D3), false);
		
		
		assertEquals(voyageCalc.ne(I1, I4), false);
		assertEquals(voyageCalc.ne(I1, 10), false);
		assertEquals(voyageCalc.ne(I1, I2), true);
		assertEquals(voyageCalc.ne(I1, I3), false);
		
		assertEquals(voyageCalc.ne(D1, D4), false);
		assertEquals(voyageCalc.ne(D1, 5.5), false);
		assertEquals(voyageCalc.ne(D1, 2d), true);
		assertEquals(voyageCalc.ne(D1, D3), false);
		
		assertEquals(voyageCalc.lt(I1, I4), false);
		assertEquals(voyageCalc.lt(I1, 10), false);
		assertEquals(voyageCalc.lt(I2, I1), true);
		assertEquals(voyageCalc.lt(I1, I3), false);
		
		assertEquals(voyageCalc.lt(D1, D4), false);
		assertEquals(voyageCalc.lt(D1, 5.5), false);
		assertEquals(voyageCalc.lt(D1, 10d), true);
		assertEquals(voyageCalc.lt(D1, D3), false);
		
		assertEquals(voyageCalc.gt(I1, I4), false);
		assertEquals(voyageCalc.gt(I1, 5), true);
		assertEquals(voyageCalc.gt(I1, I2), true);
		assertEquals(voyageCalc.gt(I1, I3), false);
		
		assertEquals(voyageCalc.gt(D2, D1), true);
		assertEquals(voyageCalc.gt(D1, D4), false);
		assertEquals(voyageCalc.gt(D1, 5.5), false);
		assertEquals(voyageCalc.gt(D1, 2.5), true);
		assertEquals(voyageCalc.gt(D1, D3), false);
		
		assertEquals(voyageCalc.lte(I1, I4), true);
		assertEquals(voyageCalc.lte(I1, 5), false);
		assertEquals(voyageCalc.lte(I2, I1), true);
		assertEquals(voyageCalc.lte(I1, I3), false);
		
		assertEquals(voyageCalc.lte(D2, D1), false);
		assertEquals(voyageCalc.lte(D1, D2), true);
		assertEquals(voyageCalc.lte(D1, D4), true);
		assertEquals(voyageCalc.lte(D1, 2.5), false);
		assertEquals(voyageCalc.lte(D1, D3), false);
		
		assertEquals(voyageCalc.gte(I1, I4), true);
		assertEquals(voyageCalc.gte(I1, 10), true);
		assertEquals(voyageCalc.gte(I2, I1), false);
		assertEquals(voyageCalc.gte(I1, I3), false);
		
		assertEquals(voyageCalc.gte(D2, D1), true);
		assertEquals(voyageCalc.gte(D1, D2), false);
		assertEquals(voyageCalc.gte(D1, D4), true);
		assertEquals(voyageCalc.gte(D1, 2.5), true);
		assertEquals(voyageCalc.gte(D1, D3), false);
		
		assertEquals(voyageCalc.gte(D1, 1), true);
		assertEquals(voyageCalc.gte(D1, 20), false);
		assertEquals(voyageCalc.gte(D2, I1), true);
		
		assertEquals(voyageCalc.gt(I1, D1), true);
		assertEquals(voyageCalc.gt(I1, D2), false);
		assertEquals(voyageCalc.gt(5, 5.7), false);
		assertEquals(voyageCalc.gt(5, 4.6), true);
		assertEquals(voyageCalc.gt(I1, 5.2), true);
		assertEquals(voyageCalc.gt(10, 11.5), false);
		
		
		assertEquals(voyageCalc.lte(D1, I1), true);
		assertEquals(voyageCalc.lte(D2, I1), false);
		assertEquals(voyageCalc.lte(5.7, 5), false);
		assertEquals(voyageCalc.lte(3.6, 5), true);
		assertEquals(voyageCalc.lte(5.2, I1), true);
		assertEquals(voyageCalc.lte(11.5, 10), false);
		
		
	}
	
	
	public void testRand()
	{
		
		VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
		Long l = 5l;
		Port P = Port.loadById(session, voyageCalc.defVal(l, 0l));
		assertNull(P);
	}
		
}