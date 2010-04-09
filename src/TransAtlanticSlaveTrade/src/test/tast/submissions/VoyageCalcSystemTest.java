/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package test.tast.submissions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.junit.Test;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.VoyagesCalculation;
import test.tast.submissions.Test1;

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
		//suite.addTest(new VoyageCalcSystemTest("testImputedVars"));  //run from array and compare
		//suite.addTest(new VoyageCalcSystemTest("testImputedVars1")); //run from file and compare
		suite.addTest(new VoyageCalcSystemTest("testImputedVars2"));  //run from file and DO NOT compare
		
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
	//read from array and compare
	public void testImputedVars(){
		Test1 t1 = new Test1(); // comparison class
		BufferedWriter fOut=null; //writes to output file
		Hashtable ht = new Hashtable();
		
		try
		{
		  fOut = new BufferedWriter(new FileWriter("/temp/tests.csv"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try{
			/*28,32,36,38,56,72,99,100,106,109*/
			//Integer[] voyageIdArray = {28,32,36,38,56,72,99,100,106,109};						
			//Integer[] voyageIdArray = {28, 72, 245};
			//Integer [] voyageIdArray = {115, 162, 255, 557, 991, 1177, 1509, 1845, 1860, 1939, 1942, 1960, 2110, 2142, 2171, 2380, 4313, 4407, 4925, 4993, 5503, 7395, 24770, 25511, 25514, 25516, 25530, 25537, 34161, 36375, 36798, 36850, 36915, 37225, 40306, 40800, 41846, 46258, 46260, 46274, 46751, 46987, 47027, 49221, 51065, 51080, 51222, 51855, 51856, 51857, 51858, 78078, 78146, 78811, 80685, 83487, 84108, 90660};
			Integer[] voyageIdArray = {32904};
			int revision = 1;
			for (int i=0; i < voyageIdArray.length; i++){
				
				System.out.println("Processing...."+voyageIdArray[i]);
				setUpSession();
				Voyage voy = null;
				voy = Voyage.loadByVoyageId(session, voyageIdArray[i], revision);
				
				if(voy==null)  //If voyage does not exist skip the rest of the loop
				  {
					  fOut.write("VoyageId: "+voyageIdArray[i]+" MISSING");
					  fOut.newLine();
					  fOut.write(",,");
					  fOut.newLine();
					  continue;
				  }	
				
				initAllImputedValuesVoyage(voy);
				//System.out.println("voyage before: " + voy.toString());
				VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voy);	
				voy = voyageCalc.calculateImputedVariables();
				tran.commit();
				session.close();
				//System.out.println("voyage after: " + voy.toString());
				//put check code here
				String[][] voyAnswer = t1.answer(voyageIdArray[i]);
				
								
				fOut.write("VoyageId: "+voyageIdArray[i]+",,");
				fOut.newLine();
				
				for(int j=0; j < voyAnswer.length; j++)
				{
					if( 
						(voyAnswer[j][1]!=null && voyAnswer[j][2]!=null && !voyAnswer[j][1].equals(voyAnswer[j][2]) ) || 
						(voyAnswer[j][1]==null && voyAnswer[j][2]!=null) || 
						(voyAnswer[j][2]==null && voyAnswer[j][1]!=null) 
						)
					{
						fOut.write(voyAnswer[j][0]+","+ voyAnswer[j][1] + "," + voyAnswer[j][2]);
						fOut.newLine();
						
						Integer temp = (Integer)ht.get(voyAnswer[j][0]);
						if(temp==null) {temp=0;}
						ht.put(voyAnswer[j][0],temp+1);
						//System.out.println(voyAnswer[j][0]+": "+ voyAnswer[j][1] + " " + voyAnswer[j][2]);                                                           
					}
					
					
				}
				fOut.write(",,");
				fOut.newLine();
			}
			
			fOut.write(",,");
			fOut.newLine();
			fOut.write("REPORT,,");
			fOut.newLine();
			System.out.println("REPORT:");
			
			Enumeration e = ht.keys();
			
			while (e.hasMoreElements())
			{
				String key = (String) e.nextElement ();
				Integer val = (Integer)ht.get(key);
				fOut.write(key+","+val+",");
				fOut.newLine();
				System.out.println(key + ": " + val);
			}
			
			
			fOut.close();
			System.out.println("END");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	//read from file and compare
	public void testImputedVars1(){
		Test1 t1 = new Test1(); // comparison class
		BufferedWriter fOut=null; //writes to output file
		Hashtable ht = new Hashtable();
		
		try
		{
		  fOut = new BufferedWriter(new FileWriter("/temp/tests.csv"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try{			
			String fPath = "/dev/voyages/src/test/tast/submissions/batch_voyages.txt";		
			FileInputStream fstream = new FileInputStream(fPath);
			//Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    Integer revision = 1;
		    		    
		    while ((strLine = br.readLine()) != null)   {		    
		      //System.out.println (strLine);
		      Integer voyageId = Integer.valueOf(strLine);
		      System.out.println("Processing...."+voyageId);
		      setUpSession();
			  Voyage voy = null;
			  voy = Voyage.loadByVoyageId(session, voyageId, revision);

			  if(voy==null)  //If voyage does not exist skip the rest of the loop
			  {
				  fOut.write("VoyageId: "+voyageId+" MISSING");
				  fOut.newLine();
				  fOut.write(",,");
				  fOut.newLine();
				  continue;
			  }
			  
			  initAllImputedValuesVoyage(voy);
			  //System.out.println("voyage before: " + voy.toString());
			  VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voy);	
			  voy = voyageCalc.calculateImputedVariables();
			  saveVoyage(voy);			  
			  //System.out.println("voyage after: " + voy.toString());
			  
			  String[][] voyAnswer = t1.answer(voyageId);
				
								
				fOut.write("VoyageId: "+voyageId+",,");
				fOut.newLine();
				
				for(int j=0; j < voyAnswer.length; j++)
				{
					if( 
						(voyAnswer[j][1]!=null && voyAnswer[j][2]!=null && !voyAnswer[j][1].equals(voyAnswer[j][2]) ) || 
						(voyAnswer[j][1]==null && voyAnswer[j][2]!=null) || 
						(voyAnswer[j][2]==null && voyAnswer[j][1]!=null) 
						)
					{
						fOut.write(voyAnswer[j][0]+","+ voyAnswer[j][1] + "," + voyAnswer[j][2]);
						fOut.newLine();
						
						Integer temp = (Integer)ht.get(voyAnswer[j][0]);
						if(temp==null) {temp=0;}
						ht.put(voyAnswer[j][0],temp+1);
						
						//System.out.println(voyAnswer[j][0]+": "+ voyAnswer[j][1] + " " + voyAnswer[j][2]);                                                           
					}
					
					
				}
				fOut.write(",,");
				fOut.newLine();
			  		      
		    }
		    //write report close the input / output 
		    
		    fOut.write(",,");
			fOut.newLine();
			fOut.write("REPORT,,");
			fOut.newLine();
			System.out.println("REPORT:");
			
			Enumeration e = ht.keys();
			
			while (e.hasMoreElements())
			{
				String key = (String) e.nextElement ();
				Integer val = (Integer)ht.get(key);
				fOut.write(key+","+val+",");
				fOut.newLine();
				System.out.println(key + ": " + val);
			}
		    
		    in.close();
		    fOut.close();
		    System.out.println("END");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	//read from file and DO NOT compare
	public void testImputedVars2(){
				
				
		try{			
			String fPath = "/dev/voyages/src/test/tast/submissions/batch_voyages.txt";		
			FileInputStream fstream = new FileInputStream(fPath);
			//Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    Integer revision = 1;
		    		    
		    while ((strLine = br.readLine()) != null)   {		    
		      //System.out.println (strLine);
		      Integer voyageId = Integer.valueOf(strLine);
		      System.out.println("Processing...."+voyageId);
		      setUpSession();
			  Voyage voy = null;
			  voy = Voyage.loadByVoyageId(session, voyageId, revision);

			  if(voy==null)  //If voyage does not exist skip the rest of the loop
			  {
				   continue;
			  }
			  
			  initAllImputedValuesVoyage(voy);
			  //System.out.println("voyage before: " + voy.toString());
			  VoyagesCalculation voyageCalc = new VoyagesCalculation(session, voy);	
			  voy = voyageCalc.calculateImputedVariables();
			  saveVoyage(voy);			  
			  //System.out.println("voyage after: " + voy.toString());
			  

			  		      
		    }
		    			
			System.out.println("END");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
