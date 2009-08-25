package test.tast.submissions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import au.com.bytecode.opencsv.CSVWriter;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.submission.AdminSubmissionBean;
import edu.emory.library.tast.util.CSVUtils;


public class DbExportTest extends TestCase{

	Session session = null;	
	Transaction tran = null;
	boolean RUN_ALL_MODE = false; 
	boolean  RUN_SPECIFIC_MODE = true;	
	
	public DbExportTest(String testName) {
		super(testName);
	}
	
	public static TestSuite suite() {
		return new DbExportTest("testing").invokeTestSuite();
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

		//suite.addTest(new DbExportTest("testBuildSubmissionQuery"));
	//	suite.addTest(new DbExportTest("testSaveFile"));
		suite.addTest(new DbExportTest("testAdminBean"));
			
		//suite.addTest(new DbExportTest("testAccentCharacters"));
				
		return suite;
	}
	
	/*
	 * For all tests to run successfully, session has to be recreated each time
	 */
	public TestSuite runAllTest() {
		System.out.println("Running All Tests");
		TestSuite suite = new TestSuite(this.getClass().getName());
		//add all testcases here		
		suite.addTest(new DbExportTest("testDbExport"));
		suite.addTest(new DbExportTest("testDbExportToCSV"));
		suite.addTest(new DbExportTest("testBuildSubmissionQuery"));
		suite.addTest(new DbExportTest("testDbExportToCSV"));
		
		return suite;
	}

	public void setUpSession() throws Exception {
		session = HibernateConn.getSession();
		tran = session.beginTransaction();
	}
	
	@Test
	public void testDbExport(){
		try {
			setUpSession();
			long startTime = System.currentTimeMillis();
			
			TastDbQuery query = getQuery();
			CSVUtils.writeResponse(session, query, false, true);
			tran.commit();
			session.close();
			long endTime = System.currentTimeMillis();
			long elapsed = endTime - startTime;			
			System.out.println("time taken: " + elapsed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private TastDbQuery getQuery() {
		long startTime = System.currentTimeMillis();
		TastDbConditions cond = new TastDbConditions();
		int revision = -1;
		cond.addCondition(Voyage.getAttribute("revision"), new Integer(revision), TastDbConditions.OP_EQUALS);
		TastDbQuery q = new TastDbQuery(new String[] {"Voyage"}, new String[] {}, cond, 1000);
		//TastDbQuery q = new TastDbQuery("Voyage", cond);
		String[] attrs = Voyage.getAllAttrNames();
		for (int i = 0; i < attrs.length; i++) {
			q.addPopulatedAttribute(Voyage.getAttribute(attrs[i]));
		}
		return q;
	}
	
	@Test
	public void testDbExportToCSV(){
		try {			
			long startTime = System.currentTimeMillis();
			
			String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
			String DB_USER = "tast";
			//TODO enter correct password for the connection
			String DB_PASS = "enter password";

			File file = new File("c:\\tmp\\voyage.csv");
			FileOutputStream fout = new FileOutputStream(file);
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(fout), ',');
			
			Class.forName("org.postgresql.Driver");
				
			Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
			conn.setAutoCommit(false);
				
			Statement stSelectVoyages = conn.createStatement();
			ResultSet rs = stSelectVoyages.executeQuery("select voyageid, suggestion, revision, iid, adlt1imp, adlt2imp, adlt3imp, adpsale1, adpsale2, adult1, adult2, adult3, adult4, adult5, adult6, adult7, arrport, arrport2, boy1, boy2, boy3, boy4, boy5, boy6, boy7, boyrat1, boyrat3, boyrat7, captaina, captainb, captainc, chil1imp, chil2imp, chil3imp, child1, child2, child3, child4, child5, child6, child7, chilrat1, chilrat3, chilrat7, constreg, crew, crew1, crew2, crew3, crew4, crew5, crewdied, datedepa, datedepb, datedepc, d1slatra, d1slatrb, d1slatrc, dlslatra, dlslatrb, dlslatrc, ddepam, ddepamb, ddepamc, datarr32, datarr33, datarr34, datarr36, datarr37, datarr38, datarr39, datarr40, datarr41, datarr43, datarr44, datarr45, datedep, datebuy, dateleftafr, dateland1, dateland2, dateland3, datedepam, dateend, deptregimp, deptregimp1, embport, embport2, embreg, embreg2, evgreen, fate, fate2, fate3, fate4, female1, female2, female3, female4, female5, female6, female7, feml1imp, feml2imp, feml3imp, girl1, girl2, girl3, girl4, girl5, girl6, girl7, girlrat1, girlrat3, girlrat7, guns, infant1, infant3, infant4, jamcaspr, majbuypt, majbyimp, majbyimp1, majselpt, male1, male1imp, male2, male2imp, male3, male3imp, male4, male5, male6, male7, malrat1, malrat3, malrat7, men1, men2, men3, men4, men5, men6, men7, menrat1, menrat3, menrat7, mjbyptimp, mjselimp, mjselimp1, mjslptimp, natinimp, national, ncar13, ncar15, ncar17, ndesert, npafttra, nppretra, npprior, ownera, ownerb, ownerc, ownerd, ownere, ownerf, ownerg, ownerh, owneri, ownerj, ownerk, ownerl, ownerm, ownern, ownero, ownerp, plac1tra, plac2tra, plac3tra, placcons, placreg, portdep, portret, ptdepimp, regarr, regarr2, regdis1, regdis2, regdis3, regem1, regem2, regem3, regisreg, resistance, retrnreg, retrnreg1, rig, saild1, saild2, saild3, saild4, saild5, shipname, sla1port, slaarriv, sladafri, sladamer, sladied1, sladied2, sladied3, sladied4, sladied5, sladied6, sladvoy, slamimp, slas32, slas36, slas39, slavema1, slavema3, slavema7, slavemx1, slavemx3, slavemx7, slavmax1, slavmax3, slavmax7, slaximp, slinten2, slintend, sourcea, sourceb, sourcec, sourced, sourcee, sourcef, sourceg, sourceh, sourcei, sourcej, sourcek, sourcel, sourcem, sourcen, sourceo, sourcep, sourceq, sourcer, tonmod, tonnage, tontype, tslavesd, tslavesp, tslmtimp, voy1imp, voy2imp, voyage, vymrtimp, vymrtrat, women1, women2, women3, women4, women5, women6, women7, womrat1, womrat3, womrat7, xmimpflag, year10, year100, year25, year5, yearaf, yearam, yeardep, yrcons, yrreg FROM voyages where revision = 1");
			
			//int colCount = rs.getMetaData().getColumnCount();
			String[] str = {"voyageid", "suggestion", "revision", "iid", "adlt1imp*", "adlt2imp*", "adlt3imp*", "adpsale1", "adpsale2", "adult1", "adult2", "adult3", "adult4", "adult5", "adult6", "adult7*", "arrport", "arrport2", "boy1", "boy2", "boy3", "boy4", "boy5", "boy6", "boy7*", "boyrat1*", "boyrat3*", "boyrat7*", "captaina", "captainb", "captainc", "chil1imp*", "chil2imp*", "chil3imp*", "child1", "child2", "child3", "child4", "child5", "child6", "child7*", "chilrat1*", "chilrat3*", "chilrat7*", "constreg*", "crew", "crew1", "crew2", "crew3", "crew4", "crew5", "crewdied", "datedepa", "datedepb", "datedepc", "d1slatra", "d1slatrb", "d1slatrc", "dlslatra", "dlslatrb", "dlslatrc", "ddepam", "ddepamb", "ddepamc", "datarr32", "datarr33", "datarr34", "datarr36", "datarr37", "datarr38", "datarr39", "datarr40", "datarr41", "datarr43", "datarr44", "datarr45", "datedep", "datebuy", "dateleftafr", "dateland1", "dateland2", "dateland3", "datedepam", "dateend", "deptregimp*", "deptregimp1*", "embport", "embport2", "embreg*", "embreg2*", "evgreen", "fate", "fate2*", "fate3*", "fate4*", "female1", "female2", "female3", "female4", "female5", "female6", "female7", "feml1imp*", "feml2imp*", "feml3imp*", "girl1", "girl2", "girl3", "girl4", "girl5", "girl6", "girl7*", "girlrat1*", "girlrat3*", "girlrat7*", "guns", "infant1", "infant3", "infant4", "jamcaspr", "majbuypt", "majbyimp*", "majbyimp1*", "majselpt", "male1", "male1imp*", "male2", "male2imp*", "male3", "male3imp*", "male4", "male5", "male6", "male7*", "malrat1*", "malrat3*", "malrat7*", "men1", "men2", "men3", "men4", "men5", "men6", "men7*", "menrat1*", "menrat3*", "menrat7*", "mjbyptimp*", "mjselimp*", "mjselimp1*", "mjslptimp*", "natinimp*", "national", "ncar13", "ncar15", "ncar17", "ndesert", "npafttra", "nppretra", "npprior", "ownera", "ownerb", "ownerc", "ownerd", "ownere", "ownerf", "ownerg", "ownerh", "owneri", "ownerj", "ownerk", "ownerl", "ownerm", "ownern", "ownero", "ownerp", "plac1tra", "plac2tra", "plac3tra", "placcons", "placreg", "portdep", "portret", "ptdepimp*", "regarr*", "regarr2*", "regdis1*", "regdis2*", "regdis3*", "regem1*", "regem2*", "regem3*", "regisreg*", "resistance", "retrnreg*", "retrnreg1*", "rig", "saild1", "saild2", "saild3", "saild4", "saild5", "shipname", "sla1port", "slaarriv", "sladafri", "sladamer", "sladied1", "sladied2", "sladied3", "sladied4", "sladied5", "sladied6", "sladvoy", "slamimp*", "slas32", "slas36", "slas39", "slavema1*", "slavema3*", "slavema7*", "slavemx1*", "slavemx3*", "slavemx7*", "slavmax1*", "slavmax3*", "slavmax7*", "slaximp*", "slinten2", "slintend", "sourcea", "sourceb", "sourcec", "sourced", "sourcee", "sourcef", "sourceg", "sourceh", "sourcei", "sourcej", "sourcek", "sourcel", "sourcem", "sourcen", "sourceo", "sourcep", "sourceq", "sourcer", "tonmod*", "tonnage", "tontype", "tslavesd", "tslavesp", "tslmtimp*", "voy1imp*", "voy2imp*", "voyage", "vymrtimp*", "vymrtrat*", "women1", "women2", "women3", "women4", "women5", "women6", "women7*", "womrat1*", "womrat3*", "womrat7*", "xmimpflag*", "year10*", "year100*", "year25*", "year5*", "yearaf*", "yearam*", "yeardep*", "yrcons", "yrreg"};
			writer.writeNext(str);
			writer.writeAll(rs, false);
			rs.close();
			writer.close();
			
			long endTime = System.currentTimeMillis();
			long elapsed = endTime - startTime;			
			System.out.println("time taken: " + elapsed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testBuildSubmissionQuery() {
		long startTime = System.currentTimeMillis();
		
		TastDbQuery q = new TastDbQuery(new String("SubmissionNew"));
		session = HibernateConn.getSession();
		tran = session.beginTransaction();
		Object[] rslt = q.executeQuery(session);
		for (int i=0; i < rslt.length; i++) {
			Submission sub = (Submission)rslt[i];
			EditedVoyage voy= ((SubmissionNew) sub).getNewVoyage();
			String name = ((SubmissionNew) sub) .getUser().getUserName();
			System.out.println("voyage iid:" + voy.getVoyage().getIid());
			System.out.println("user name:" + name);
			
		}
	
	}
	
	@Test
	public void testAdminBean() {
		AdminSubmissionBean bean = new AdminSubmissionBean();		
		bean.getFileAllData();
	}
	
	@Test
	public void testAccentedCharacters(){
		try {			
			long startTime = System.currentTimeMillis();
			
			String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
			String DB_USER = "tast";
			String DB_PASS = "pass_1234";

			CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream("c:\\tmp\\accents.csv"),"UTF-8"));
			Class.forName("org.postgresql.Driver");
				
			Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
			conn.setAutoCommit(false);
				
			Statement stSelectVoyages = conn.createStatement();
			//ResultSet rs = stSelectVoyages.executeQuery("select captaina FROM voyages order by voyageid asc limit 100");
			ResultSet rs = stSelectVoyages.executeQuery("select captaina from voyages where captaina like 'Coelho%Batista'	order by voyageid asc limit 1");
			String captaina = null;
			while (rs.next()) {			
				if (rs.getBytes(1) != null) {
					captaina = new String(rs.getBytes(1),"UTF-8");
				}
				System.out.println("captaina: " + captaina);
			}
			
			rs.close();
			writer.close();
			
			long endTime = System.currentTimeMillis();
			long elapsed = endTime - startTime;			
			System.out.println("time taken: " + elapsed);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAccentCharacters(){
		try {		      
		      char[] accentChars = "\u00C1\u00C2\u00C3\u00C7\u00C9\u00CD\u00D3\u00DA\u00E1\u00E2\u00E3\u0103\u00E6\u00E7\u010D\u00E8\u00E9\u00EA\u00EB\u0119\u00ED\u00EE\u00EF\u00F1\u00F3\u00F4\u00F5\u00F6\u00FA\u00FC".toCharArray();
		      String[] accentStr = {"\u0102", "\u00C1","\u00C2", "\u00C3", "\u00C7", "\u00C9", "\u00CD", "\u00D3", "\u00DA", "\u00E1", "\u00E2", "\u00E3", "\u0103", "\u00E6", "\u00E7", "\u010D", "\u00E8", "\u00E9", "\u00EA", "\u00EB", "\u0119", "\u00ED", "\u00EE", "\u00EF", "\u00F1", "\u00F3", "\u00F4", "\u00F5", "\u00F6", "\u00FA", "\u00FC"};
		      String[] str = {"Coelho, Joăo Batista"};		     
		      String encoding = "UTF8";
		      CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream("c:\\tmp\\myFile.csv"), encoding));		      
		      writer.writeNext(str);
		      writer.close();
		} 
		catch (Exception e) {
		      System.out.println(e.toString());
		    }
	}
	
	@Test
	public void testSaveFile() throws IOException {
	      BufferedWriter bw = null;
	      OutputStreamWriter osw = null;
			
	      File f = new File("c:\\tmp\\test1.csv");
	      FileOutputStream fos = new FileOutputStream(f, true);
	      try {
	         // write UTF8 BOM mark if file is empty
	         if (f.length() < 1) {
	            final byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };	        	
	            fos.write(bom);
	         }

	         osw = new OutputStreamWriter(fos, "UTF-8");
	         bw = new BufferedWriter(osw);
	         String data = "Coelho, Joăo Batista";
	         if (data != null) bw.write(data);
	      } catch (IOException ex) {
	         throw ex;
	      } finally {
	         try { bw.close(); fos.close(); } catch (Exception ex) { }
	      }
	   }


}
