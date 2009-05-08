package test.tast.submissions;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.junit.Before;
import org.junit.Test;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.VoyagesCalculation;

public class VoyageCalcTest extends TestCase {
	
	boolean RUN_ALL_MODE = false; 
	boolean  RUN_SPECIFIC_MODE = true;	
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
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByPortdep"));
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByMajselpt"));
		//suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByNullValues"));	
		//suite.addTest(new VoyageCalcTest("testCalculateTslmtimp"));
		suite.addTest(new VoyageCalcTest("testCalculateTslmtimpWithAllVars"));
			
		return suite;
	}
	
	/*
	 * For all tests to run successfully, session has to be recreated each time
	 */
	public TestSuite runAllTest() {
		System.out.println("Running All Tests");
		TestSuite suite = new TestSuite(this.getClass().getName());
		//add all testcases here
		suite.addTest(new VoyageCalcTest("testCalculateImputedValueFate2"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByPortdep"));
		suite.addTest(new VoyageCalcTest("testCalculatePtDepImpByMajselpt"));
		return suite;
	}
	
	@Test
	public void testCalculatePtDepImpByPortdep(){
		try {	
			deleteVoyage(99900);
			setValuesVoyage(new Integer(99900), "shipName_99900");
			long portId = 50105;//Rio Amazona
			Port portdep = Port.loadById(session, portId);			
			voyage.setPortdep(portdep);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(voyage, session);			
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
			deleteVoyage(99901);
			setValuesVoyage(new Integer(99901), "shipName_99901");
			long portId = 50420;//Parati
			Port majselpt = Port.loadById(session, portId);
			voyage.setMajselpt(majselpt);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(voyage, session);			
			voyageCalc.calculatePtDepImp();
			saveVoyage(voyage);
			assertEquals(voyage.getPtdepimp().getId().longValue(), portId);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCalculatePtDepImpByNullValues(){
		try {	
			deleteVoyage(99902);
			setValuesVoyage(new Integer(99902), "shipName_99902");
			VoyagesCalculation voyageCalc = new VoyagesCalculation(voyage, session);			
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
			deleteVoyage(99903);
			setValuesVoyage(new Integer(99903), "shipName_99903");
			//add test specific variables in voyage object
			long rigId = 15;
			Fate fate = Fate.loadById(session, rigId);
			voyage.setFate(fate);
			VoyagesCalculation voyageCalc = new VoyagesCalculation(voyage, session);			
			voyageCalc.calculateImputedValueFate2();
			saveVoyage(voyage);
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
			deleteVoyage(99900);
			setValuesVoyage(new Integer(99900), "shipName_99900");
			voyage.setSladvoy(new Integer(75));
			
			/*Integer sladvoy = voyage.getSladvoy();
			Integer tslavesd = voyage.getTslavesd();
			Integer slaarriv = voyage.getSlaarriv();*/
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(voyage, session);			
			voyageCalc.calculateTslmtimp();
			saveVoyage(voyage);
			//assertEquals(voyage.getPtdepimp().getId().longValue(), portId);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testCalculateTslmtimpWithAllVars(){
		try {	
			deleteVoyage(99900);
			setValuesVoyage(new Integer(99900), "shipName_99900");
			voyage.setTslavesd(new Integer(100));
			voyage.setSladvoy(new Integer(200));
			//voyage.setSlaarriv(new Integer(60));
			
			VoyagesCalculation voyageCalc = new VoyagesCalculation(voyage, session);			
			voyageCalc.calculateTslmtimp();
			saveVoyage(voyage);
			//assertEquals(voyage.getPtdepimp().getId().longValue(), portId);
			voyage = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
