package test.tast.submissions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.VoyagesCalculation;

public class VoyageCalcSystemTest extends TestCase {
	
	boolean RUN_ALL_MODE = false; 
	boolean  RUN_SPECIFIC_MODE = true;	
	Session session = null;
	Transaction tran = null;

	public void setUpSession() throws Exception {
			session = HibernateConn.getSession();
			tran = session.beginTransaction();
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
	
	private void initAllImputedValuesVoyage(Voyage voy) {
		voy.setFate2(null);
		voy.setFate3(null);		 
		voy.setFate4(null);
		voy.setTonmod(null);
		voy.setMajbuypt(null);			
		voy.setMajselpt(null);
		voy.setYear5(null);
		voy.setYear10(null);
		voy.setYear25(null);
		voy.setYear100(null);
		voy.setNatinimp(null);
		voy.setVoy1imp(null);
		voy.setVoy2imp(null);
		voy.setPtdepimp(null);
		voy.setTslmtimp(null);
		voy.setVymrtrat(null);
		voy.setVymrtimp(null);
		voy.setConstreg(null);	    
		voy.setRegisreg(null);	    
		voy.setDeptreg(null);	    
		voy.setDeptregimp(null);	    
		voy.setEmbreg(null);	    
		voy.setEmbreg2(null);	    
		voy.setRegem1(null);	    
		voy.setRegem2(null);	    
		voy.setRegem3(null);	    
		voy.setMajbuyreg(null);	    
		voy.setMajbyimp(null);	    
		voy.setRegarr(null);	    
		voy.setRegarr2(null);	    
		voy.setRegdis1(null);	    
		voy.setRegdis2(null);	    
		voy.setRegdis3(null);	    
		voy.setMajselrg(null);	    
		voy.setMjselimp(null);	    
		voy.setRetrnreg(null);	    
		voy.setDeptreg1(null);	    
		voy.setDeptregimp1(null);	    
		voy.setMajbyimp1(null);	    
		voy.setMjselimp1(null);	    
		voy.setRetrnreg1(null);	    
		voy.setSlaximp(null);
		voy.setSlamimp(null);
		voy.setAdlt1imp(null); 
		voy.setChil1imp(null);
		voy.setMale1imp(null);
		voy.setFeml1imp(null);
		voy.setAdlt2imp(null);
		voy.setChil2imp(null);
		voy.setMale2imp(null);
		voy.setFeml2imp(null);
		voy.setSlavema1(null);
		voy.setSlavemx1(null);
		voy.setSlavmax1(null);
		voy.setChilrat1(null);
		voy.setMalrat1(null);  
		voy.setMenrat1(null);
		voy.setWomrat1(null);
		voy.setBoyrat1(null);
		voy.setGirlrat1(null);
		voy.setAdlt3imp(null);
		voy.setChil3imp(null);
		voy.setMale3imp(null);
		voy.setFeml3imp(null);
		voy.setSlavema3(null);
		voy.setSlavemx3(null);
		voy.setSlavmax3(null);
		voy.setChilrat3(null);
		voy.setMalrat3(null);
		voy.setMenrat3(null);
		voy.setWomrat3(null);
		voy.setBoyrat3(null);
		voy.setGirlrat3(null);
		voy.setSlavema7(null);
		voy.setSlavemx7(null);
		voy.setSlavmax7(null);
		voy.setMen7(null);
		voy.setWomen7(null);
		voy.setBoy7(null);
		voy.setGirl7(null);
		voy.setAdult7(null);
		voy.setChild7(null);
		voy.setMale7(null);
		voy.setFemale7(null);
		voy.setMenrat7(null);
		voy.setWomrat7(null);
		voy.setBoyrat7(null);
		voy.setGirlrat7(null);
		voy.setMalrat7(null);
		voy.setChilrat7(null);
		voy.setXmimpflag(null);
		voy.setMjbyptimp(null);
		voy.setMjslptimp(null);
		voy.setYearam(null);
		voy.setYearaf(null);
		voy.setYeardep(null);				
		voy.setDatedep(null);
		voy.setDatebuy(null);
		voy.setDateleftafr(null);
		voy.setDateland1(null);
		voy.setDateland2(null);
		voy.setDateland3(null);
		voy.setDateend(null);
		voy.setDatedepam(null);
	}
	public VoyageCalcSystemTest(String testName) {
		super(testName);
	}	
	
	public static TestSuite suite() {
		return new VoyageCalcSystemTest("testing").invokeTestSuite();
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
		//suite.addTest(new VoyageCalcSystemTest("testImputedVars"));
		suite.addTest(new VoyageCalcSystemTest("testImputedVars1"));
		
		return suite;
	}
	
	/*
	 * For all tests to run successfully, session has to be recreated each time
	 */
	public TestSuite runAllTest() {
		System.out.println("Running All Tests");
		TestSuite suite = new TestSuite(this.getClass().getName());
		//add all testcases here
		return suite;
	}	
	
	@Test
	public void testImputedVars(){
		try{
			/*28,32,36,38,56,72,99,100,106,109*/
			Integer[] voyageIdArray = {28,32,36,38,56,72,99,100,106,109};						
			int revision = 1;
			for (int i=0; i < voyageIdArray.length; i++){
				setUpSession();
				Voyage voy = null;
				voy = Voyage.loadByVoyageId(session, voyageIdArray[i], revision);
				initAllImputedValuesVoyage(voy);
				System.out.println("voyage before: " + voy.toString());
				VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voy);	
				voy = voyageCalc.calculateImputedVariables();
				tran.commit();
				session.close();
				System.out.println("voyage after: " + voy.toString());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testImputedVars1(){
		try{			
			String fPath = "/dev/slavevoyages/src/test/tast/submissions/batch_voyages.txt";		
			FileInputStream fstream = new FileInputStream(fPath);
			//Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    Integer revision = 1;
		    /*while ((strLine = br.readLine()) != null)   {		    
			      //System.out.println (strLine);
			      Integer voyageId = Integer.valueOf(strLine);
			      System.out.println (voyageId);
		    }*/
		    
		    while ((strLine = br.readLine()) != null)   {		    
		      //System.out.println (strLine);
		      Integer voyageId = Integer.valueOf(strLine);
		      setUpSession();
			  Voyage voy = null;
			  voy = Voyage.loadByVoyageId(session, voyageId, revision);
			  initAllImputedValuesVoyage(voy);
			  System.out.println("voyage before: " + voy.toString());
			  VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voy);	
			  voy = voyageCalc.calculateImputedVariables();
			  saveVoyage(voy);			  
			  System.out.println("voyage after: " + voy.toString());		      
		    }
		    //Close the input stream
		    in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
