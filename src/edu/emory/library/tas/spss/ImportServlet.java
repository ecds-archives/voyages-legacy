package edu.emory.library.tas.spss;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.emory.library.tas.web.test.JavaProgramRunner;
import edu.emory.library.tas.web.upload.Upload;
import edu.emory.library.tas.web.upload.UploadedFile;

public class ImportServlet extends HttpServlet
{
	
	private static final long serialVersionUID = 1L;
	private static final String DIRECTORY_MASK = "yyyy-MM-dd-HH-mm-ss-SSSS";
	
	private static String generateImportDirectory()
	{
		SimpleDateFormat df = new SimpleDateFormat(DIRECTORY_MASK);
		return df.format(new Date());
	}
	
	public static boolean isValidImportDirectoryName(String name)
	{
		SimpleDateFormat df = new SimpleDateFormat(DIRECTORY_MASK);
		try
		{
			df.parse(name);
			return true;
		}
		catch (ParseException e)
		{
			return false;
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		// import directory
		String importsRootDir = getInitParameter("imports.dir");
		String importDir = importsRootDir + File.separatorChar + ImportServlet.generateImportDirectory();
		new File(importDir).mkdir();
		
		// create a log
		String logFileName = importDir + File.separatorChar + "import.log";
		LogWriter log = new LogWriter(logFileName);
		log.startStage(LogItem.STAGE_UPLOADING);

		// upload files
		Upload upload = new Upload(request, importDir, log);
		upload.setSaveAs("voyages", "voyages.sav");
		upload.setSaveAs("slaves", "slaves.sav");
		if (!upload.upload())
		{
			log.logInfo("Import terminated.");
			log.close();
			return;
		}
		
		// was at least something uploaded?
		UploadedFile voyagesFile = upload.getFileByHtmlFieldName("voyages"); 
		UploadedFile slavesFile = upload.getFileByHtmlFieldName("slaves"); 
		if (voyagesFile == null && slavesFile == null)
		{
			log.logError("No files were uploaded. Expected at least one file.");
			log.logInfo("Import terminated.");
			log.close();
			return;
		}
		
		// we don't need the log anymore
		log.close();
		
		// log and the data files as parameters
		String[] params = new String[4];
		params[0] = importDir;
		params[1] = getInitParameter("stattransfer.exe");
		params[2] = voyagesFile != null ? voyagesFile.getServerFileName() : "*";
		params[3] = slavesFile != null ? slavesFile.getServerFileName() : "*";
		
		// run
		JavaProgramRunner javaRunner = new JavaProgramRunner("edu.emory.library.tas.spss.Import");
		javaRunner.setClasspath(getInitParameter("classpath"));
		javaRunner.setMemoryHeap(getInitParameter("java.memory"));
		javaRunner.setParameters(params);
		javaRunner.run();
		
		// some output
		response.getWriter().println("import started ... look inside: " + importDir);
		
	}
	
	public static void main(String[] args) throws IOException
	{
		

		// log and the data files as parameters
		String[] params = new String[4];
//		params[0] = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\imports\\2006-06-07-15-30-13-0423";
//		params[1] = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\imports\\2006-06-07-15-30-13-0423\\import.log";
//		params[2] = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\imports\\2006-06-07-15-30-13-0423\\voyages.sav";
//		params[3] = "C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\imports\\2006-06-07-15-30-13-0423\\slaves.sav";
		params[0] = "D:\\Library\\SlaveTrade\\imports\\2006-06-07-21-12-21-0296";
		params[1] = "D:\\Library\\SlaveTrade\\imports\\2006-06-07-21-12-21-0296\\import.log";
		params[2] = "D:\\Library\\SlaveTrade\\imports\\2006-06-07-21-12-21-0296\\voyages.sav";
		params[3] = "D:\\Library\\SlaveTrade\\imports\\2006-06-07-21-12-21-0296\\slaves.sav";
		
		// run
		JavaProgramRunner javaRunner = new JavaProgramRunner("edu.emory.library.tas.spss.Import");
//		javaRunner.setClasspath("C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\bin\\;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-1.6.5.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-antlr-1.6.5.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-junit-1.6.5.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-launcher-1.6.5.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\antlr-2.7.6rc1.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-swing-1.6.5.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-2.2.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-analysis-2.2.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-attrs.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-attrs-2.2.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-tree-2.2.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-util-2.2.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-xml-2.2.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\c3p0-0.9.0.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\cglib-2.1.3.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\cleanimports.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\commons-collections-2.1.1.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\commons-logging-1.0.4.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\concurrent-1.3.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\connector.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\crimson.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\dom4j-1.6.1.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ehcache-1.1.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\hibernate3.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jaas.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jacc-1_0-fr.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jaxen-1.1-beta-7.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-cache.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-common.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-jmx.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-system.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jdbc2_0-stdext.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jgroups-2.2.8.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jsonrpc-1.0.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jta.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\junit-3.8.1.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\log4j-1.2.11.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\oscache-2.1.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\postgresql-8.1-405.jdbc3.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\proxool-0.8.3.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\servlet-api.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\swarmcache-1.0rc2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\syndiag2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\versioncheck.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\xerces-2.6.2.jar;C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\xml-apis.jar");
		javaRunner.setClasspath("D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\build\\classes\\;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-1.6.5.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-antlr-1.6.5.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-junit-1.6.5.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-launcher-1.6.5.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\antlr-2.7.6rc1.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ant-swing-1.6.5.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-2.2.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-analysis-2.2.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-attrs.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-attrs-2.2.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-tree-2.2.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-util-2.2.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\asm-xml-2.2.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\c3p0-0.9.0.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\cglib-2.1.3.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\cleanimports.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\commons-collections-2.1.1.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\commons-logging-1.0.4.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\concurrent-1.3.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\connector.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\crimson.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\dom4j-1.6.1.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\ehcache-1.1.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\hibernate3.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jaas.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jacc-1_0-fr.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jaxen-1.1-beta-7.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-cache.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-common.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-jmx.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jboss-system.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jdbc2_0-stdext.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jgroups-2.2.8.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jsonrpc-1.0.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\jta.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\junit-3.8.1.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\log4j-1.2.11.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\oscache-2.1.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\postgresql-8.1-405.jdbc3.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\proxool-0.8.3.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\servlet-api.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\swarmcache-1.0rc2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\syndiag2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\versioncheck.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\xerces-2.6.2.jar;D:\\Library\\SlaveTrade\\projects\\TransAtlanticSlaveTrade\\lib\\xml-apis.jar");
		javaRunner.setMemoryHeap("256m");
		javaRunner.setParameters(params);
		javaRunner.run();
	
	}

}