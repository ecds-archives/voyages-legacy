package edu.emory.library.tas.web.spss;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;

import com.metaparadigm.jsonrpc.JSONRPCBridge;

import edu.emory.library.tas.AppConfig;
import edu.emory.library.tas.DateTimeUtils;
import edu.emory.library.tas.spss.Import;
import edu.emory.library.tas.spss.ImportServlet;
import edu.emory.library.tas.spss.Log;
import edu.emory.library.tas.spss.LogItem;
import edu.emory.library.tas.spss.LogReader;
import edu.emory.library.tas.web.UtilsJSF;

public class ImportLogBean
{
	
	private static final String BEAN_NAME = "ImportLog";
	
	private String currentImportDir;
	private int currentNoOfMessages;
	private int currentNoOfWarnings;
	private int currentLastStage;
	private LogItemForDisplayInDetail[] currentLogItems;
	private String currentLogRowClasses;
	private boolean initialDetailDataLoaded = false;
	
	public ImportLogBean()
	{
		try
		{
			JSONRPCBridge.getGlobalBridge().registerClass(BEAN_NAME, ImportLogBean.class);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private class InvalidImportDirectoryException extends Exception
	{
		private static final long serialVersionUID = 1019360649699803025L;
	}
	
	private static String makeImportFullDir(String rootImportsDir, String importDir)
	{
		return rootImportsDir + File.separatorChar + importDir;
	}

	public List getImportLogs()
	{
		
		Configuration conf = AppConfig.getConfiguration();
		DateFormat dateFormat = new SimpleDateFormat(conf.getString(AppConfig.IMPORT_DATEFORMAT));

		File importDir = new File(conf.getString(AppConfig.IMPORT_ROOTDIR));
		if (!importDir.isDirectory()) return null;
		
		List importLogs = new ArrayList();
	
		File[] importsDir = importDir.listFiles();
		for (int i = 0; i < importsDir.length; i++)
		{
			File dir = importsDir[i];
			
			try
			{
				
				LogForDisplayInList importLogForDisplay = new LogForDisplayInList();
			
				if (!ImportServlet.isValidImportDirectoryName(dir.getName()))
					throw new InvalidImportDirectoryException();
				
				importLogForDisplay.setImportDir(dir.getName());

				LogReader rdr = new LogReader(dir.getAbsolutePath());
				Log importLog = rdr.load(Integer.MAX_VALUE);
				
				importLogForDisplay.setStarted(dateFormat.format(importLog.getTimeStart()));
				
				Date lastActivityTime = null;
				if (importLog.isFinished())
				{
					lastActivityTime = importLog.getTimeFinish();
					importLogForDisplay.setFinished(dateFormat.format(importLog.getTimeFinish()));
				}
				else
				{
					lastActivityTime = new Date();
					importLogForDisplay.setFinished("not yet");
				}
				
				importLogForDisplay.setDuration(DateTimeUtils.formatTimeSpan(
						importLog.getTimeStart(), lastActivityTime,
						DateTimeUtils.TIME_INTERVAL_ROUND_TO_SEC));
				
				importLogs.add(importLogForDisplay);

			}
			catch (InvalidImportDirectoryException e)
			{
			}
			catch (IOException e)
			{
			}
			
		}
		
		return importLogs;

	}
	
	public void openDetail(ActionEvent event)
	{
		UIParameter importDirParam = (UIParameter) event.getComponent().findComponent("importDir");
		currentImportDir = (String) importDirParam.getValue();
		initialDetailDataLoaded = false;
	}

	private void loadInitialDetailData()
	{
		
		String importDirFromURL = UtilsJSF.getParam("importDir");
		if (importDirFromURL != null && importDirFromURL.length() > 0)
		{
			if (importDirFromURL.equals(currentImportDir) && initialDetailDataLoaded) return;
			currentImportDir = importDirFromURL; 
		}
		else
		{
			if (initialDetailDataLoaded) return;
		}
		initialDetailDataLoaded = true;
		
		currentNoOfMessages = 0;
		currentNoOfWarnings = 0;
		
		Configuration conf = AppConfig.getConfiguration();
		DateFormat dateFormatLogItem = new SimpleDateFormat(conf.getString(AppConfig.IMPORT_LOGITEM_DATEFORMAT));

		try
		{
			
			LogReader rdr = new LogReader(
					makeImportFullDir(
							conf.getString(AppConfig.IMPORT_ROOTDIR),
							currentImportDir));
			
			Log importLog = rdr.loadAll();
			currentNoOfMessages = importLog.getItems().size();
			
			List items = importLog.getItems();
			currentLogItems = new LogItemForDisplayInDetail[items.size()];
			
			StringBuffer rowClasses = new StringBuffer();
			
			currentLastStage = -1;

			if (items.size() > 0)
			{
				
				LogItem currItem = null;
				LogItem nextItem = (LogItem) items.get(items.size() - 1);
				currentLastStage = nextItem.getStage();
				
				for (int j = items.size() - 1, i = 0; 0 <= j; j--, i++)
				{
					
					currItem = nextItem;
					if (0 < j) nextItem = (LogItem) items.get(j - 1);
					else nextItem = null;
					
					if (currItem.getType() == LogItem.TYPE_WARN) currentNoOfWarnings ++;
					currentLogItems[i] = new LogItemForDisplayInDetail(currItem, dateFormatLogItem);
					
					if (rowClasses.length() > 0) rowClasses.append(",");

					if (nextItem == null || currItem.getStage() != nextItem.getStage()) rowClasses.append("log-item-new-stage");
					else rowClasses.append("log-item");
				}
			
			}
			
			currentLogRowClasses = rowClasses.toString();
			
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
	}

	public String getCurrentImportDir()
	{
		loadInitialDetailData();
		return currentImportDir;
	}

	public LogItemForDisplayInDetail[] getCurrentLogItems()
	{
		loadInitialDetailData();
		return currentLogItems;
	}

	public int getCurrentNoOfMessages()
	{
		loadInitialDetailData();
		return currentNoOfMessages;
	}

	public int getCurrentNoOfWarnings()
	{
		loadInitialDetailData();
		return currentNoOfWarnings;
	}

	public String getCurrentLogRowClasses()
	{
		loadInitialDetailData();
		return currentLogRowClasses;
	}
	
	public int getCurrentLastStage()
	{
		loadInitialDetailData();
		return currentLastStage;
	}

	public void setCurrentImportDir(String arg)
	{
		// required by JSF because it is bind to <h:inputHidden>
		// otherwise JSF does not work correctly
	}

	public void setCurrentNoOfMessages(int arg)
	{
		// required by JSF because it is bind to <h:inputHidden>
		// otherwise JSF does not work correctly
	}

	public void setCurrentNoOfWarnings(int arg)
	{
		// required by JSF because it is bind to <h:inputHidden>
		// otherwise JSF does not work correctly
	}

	public void setCurrentLastStage(int arg)
	{
		// required by JSF because it is bind to <h:inputHidden>
		// otherwise JSF does not work correctly
	}

	public static LogForDisplayInDetail loadDetail(HttpServletRequest request, String importDirName, int skip) throws IOException
	{
		
		try
		{
		
			Configuration conf = AppConfig.getConfiguration();
			DateFormat dateFormat = new SimpleDateFormat(conf.getString(AppConfig.IMPORT_DATEFORMAT));
			DateFormat dateFormatLogItem = new SimpleDateFormat(conf.getString(AppConfig.IMPORT_LOGITEM_DATEFORMAT));
			
			LogForDisplayInDetail detail = new LogForDisplayInDetail();
			
			File importDir = new File(
					ImportLogBean.makeImportFullDir(
							conf.getString(AppConfig.IMPORT_ROOTDIR),
							importDirName));
			
			File[] files = importDir.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				File file = files[i];
				if (file.getName().equals(Import.VOYAGES_SAV)) detail.setVoyagesPresent(true);
				if (file.getName().equals(Import.SLAVES_SAV)) detail.setSlavesPresent(true);
			}
	
			LogReader rdr = new LogReader(importDir.getAbsolutePath());
			Log importLog = rdr.load(skip);
			
			List items = importLog.getItems();
			LogItemForDisplayInDetail[] itemsForDisplay =
				new LogItemForDisplayInDetail[items.size()];
	
			for (int i = 0; i < items.size(); i++)
			{
				LogItem item = (LogItem) items.get(i);
				itemsForDisplay[i] = new LogItemForDisplayInDetail(item, dateFormatLogItem); 
			}
			
			detail.setLogItems(itemsForDisplay);
			
			if (importLog.getTimeStart() != null)
				detail.setTimeStart(dateFormat.format(importLog.getTimeStart()));
			else
				detail.setTimeStart("?");
	
			Date lastActivityTime = null;
			detail.setFinished(importLog.isFinished());
			if (importLog.isFinished())
			{
				lastActivityTime = importLog.getTimeFinish();
				detail.setTimeFinish(dateFormat.format(importLog.getTimeFinish()));
				detail.setStatusText(importLog.isFinishedOK() ? "import finished with no errors" : "import terminated with errors");
			}
			else
			{
				lastActivityTime = new Date();
				detail.setTimeFinish("not yet");
				detail.setStatusText("import still running");
			}
			
			if (importLog.getTimeStart() != null && lastActivityTime != null) 
				detail.setDuration(DateTimeUtils.formatTimeSpan(
						importLog.getTimeStart(), lastActivityTime,
						DateTimeUtils.TIME_INTERVAL_ROUND_TO_SEC));
			else
				detail.setDuration("?");
		
			return detail;
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}

}