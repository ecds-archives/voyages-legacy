package test.tast.submissions;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.junit.Test;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.TonType;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.VoyagesCalculation;

public class VoyageCalcAllTest extends TestCase {
	
	boolean RUN_ALL_MODE = false; 
	boolean  RUN_SPECIFIC_MODE = true;	
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
	
	private void setAllValuesVoyage(Integer voyageId, String shipName) {
		try {
			voyage = new Voyage();
			voyage.setVoyageid(voyageId);
			voyage.setShipname(shipName);
			int revision = 1;
			voyage.setRevision(revision);
			long rigId = 15;
			Fate fate = Fate.loadById(session, rigId);
			voyage.setFate(fate);
			//tslmtimp
			voyage.setTslavesd(new Integer(100));
			voyage.setSladvoy(new Integer(200));
	
			//natinimp
			long nat = 1;
			Nation nation = Nation.loadById(session, nat);
			voyage.setNational(nation);
	
			//imputed var
			voyage.setYearam(1770);
	
			//voyagelength
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date datedep=sdf.parse("2009-05-11 00:00");
			Date dateland1=sdf.parse("2009-05-17 23:59");
			Date dateleftafr=sdf.parse("2009-05-27 00:00");
				
			voyage.setDatedep(datedep);
			voyage.setDateland1(dateland1);
			voyage.setDateleftafr(dateleftafr);
	
			//majbuypt
			long port1 = 10112;
			long port2 = 10106;
			long port3 = 35113;
			Port plac1tra = Port.loadById(session, port1);
			Port plac2tra = Port.loadById(session, port2);
			Port plac3tra = Port.loadById(session, port3);
			if (plac1tra != null) {
				voyage.setPlac1tra(plac1tra);
			}
			if (plac2tra != null) {
				voyage.setPlac2tra(plac2tra);
			}
			if (plac3tra != null) {
				voyage.setPlac3tra(plac3tra);
			}
			voyage.setNcar13(new Integer(10));
			voyage.setNcar15(new Integer(50));
			voyage.setNcar17(new Integer(100));
			
			voyage.setTslavesp(new Integer(20));
	
			//majselpt
			port1 = 10112;
			port2 = 10106;
			port3 = 35113;
			Port sla1port = Port.loadById(session, port1);
			Port adpsale1 = Port.loadById(session, port2);
			Port adpsale2 = Port.loadById(session, port3);
			voyage.setSla1port(sla1port);
			voyage.setAdpsale1(adpsale1);
			voyage.setAdpsale2(adpsale2);
						
			voyage.setSlas32(new Integer(20));
			voyage.setSlas36(new Integer(10));
			voyage.setSlas39(new Integer(5));
			voyage.setSlaarriv(new Integer(1000));
	
			//tonmod
			voyage.setTontype(TonType.loadById(session, 4)); 
			voyage.setTonnage(300); 
		
			//xmImpflag
			long rig1 = 29;
			long imp0 = 60100;
			long imp1 = 80400;
			long imp2 = 36200;
			long imp3 = 20;
	
			VesselRig rig = VesselRig.loadById(session, rig1);
			voyage.setRig(rig);
			/*Region majbyimp = Region.loadById(session, imp0);
			voyage.setMajbyimp(majbyimp);
			Region mjselimp = Region.loadById(session, imp1);
			voyage.setMjselimp(mjselimp);
			Region mjselimp1 = Region.loadById(session, imp2);
			voyage.setMjselimp1(mjselimp1);*/
			
			//calculatePeople
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
			//voyage.setTslavesd(500);
			//voyage.setTslavesp(10);
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
			//voyage.setSlaarriv(10);
			voyage.setMen2(200);
			voyage.setWomen2(200);
			voyage.setBoy2(200);
			voyage.setGirl2(200);
			voyage.setChild2(200);
			voyage.setAdult2(200);
			voyage.setMale2(200);
			voyage.setFemale2(200);
			
			//ptdepimp
			long portId = 50105;//Rio Amazona
			Port portdep = Port.loadById(session, portId);			
			voyage.setPortdep(portdep);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
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
	
	public VoyageCalcAllTest(String testName) {
		super(testName);
	}	
	
	public static TestSuite suite() {
		return new VoyageCalcAllTest("testing").invokeTestSuite();
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
		suite.addTest(new VoyageCalcAllTest("testCalculateImputedVariables"));
		return suite;
	}
	
	/*
	 * For all tests to run successfully, session has to be recreated each time
	 */
	public TestSuite runAllTest() {
		System.out.println("Running All Tests");
		TestSuite suite = new TestSuite(this.getClass().getName());
		//add all testcases here
		suite.addTest(new VoyageCalcAllTest("testCalculateImputedVariables"));
		return suite;
	}	
	
	@Test
	public void testCalculateImputedVariables(){
		try{
			setUpSession();
			deleteVoyage(99910);
			setAllValuesVoyage(new Integer(99910), "shipName_99910");
						
			VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voyage);
			System.out.println("voyage before: " + voyage.toString());
			voyageCalc.calculateFate2();
			voyageCalc.calculateFate3();
			voyageCalc.calculateFate4();
			voyageCalc.calculateMortality();
			voyageCalc.calculateNatinimp();
			voyageCalc.calculateValuesYear();
			voyageCalc.calculateVoyLengths();		
			voyageCalc.calculateMjbyptimp();
			//voyageCalc.calculateMajselpt();
			//voyageCalc.calculateMjbyptimp();
			voyageCalc.calculateMjslptimp();			
			voyageCalc.calculateTonmod();
			voyageCalc.calculateValuesPeople();
			voyageCalc.calculatePtDepImp();
			voyageCalc.calculateValuesRegion1();
			voyageCalc.calculateValuesRegion2();
			voyageCalc.calculateXmImpflag();
			voyageCalc.calculateSlavesEmbarkDisembark();

			saveVoyage(voyage);		
			System.out.println("voyage after: " + voyage.toString());			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
