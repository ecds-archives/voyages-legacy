package edu.emory.library.tas.web.spss;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
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

public class ImportLogBean
{
	
	private static final String BEAN_NAME = "ImportLog";
	
	private String currentImportDir;
	private int currentNoOfMessages;
	private int currentNoOfWarnings;
	private LogItemForDisplayInDetail[] currentLogItems;
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
		
		String importDirFromURL = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("importDir");
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

			for (int j = items.size() - 1, i = 0; 0 <= j; j--, i++)
			{
				LogItem item = (LogItem) items.get(j);
				if (item.getType() == LogItem.TYPE_WARN) currentNoOfWarnings ++;
				currentLogItems[i] = new LogItemForDisplayInDetail(item, dateFormatLogItem); 
			}
			
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

	public static LogForDisplayInDetail loadDetail(HttpServletRequest request, String importDirName, int skip)
	{
		
		Configuration conf = AppConfig.getConfiguration();
		DateFormat dateFormat = new SimpleDateFormat(conf.getString(AppConfig.IMPORT_DATEFORMAT));
		DateFormat dateFormatLogItem = new SimpleDateFormat(conf.getString(AppConfig.IMPORT_LOGITEM_DATEFORMAT));
		
		try
		{
		
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

			for (int j = items.size() - 1, i = 0; 0 <= j; j--, i++)
			{
				LogItem item = (LogItem) items.get(j);
				itemsForDisplay[i] = new LogItemForDisplayInDetail(item, dateFormatLogItem); 
			}
			
			detail.setLogItems(itemsForDisplay);
			
			detail.setTimeStart(dateFormat.format(importLog.getTimeStart()));
	
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
			
			detail.setDuration(DateTimeUtils.formatTimeSpan(
					importLog.getTimeStart(), lastActivityTime,
					DateTimeUtils.TIME_INTERVAL_ROUND_TO_SEC));
			
			
			
			return detail;

		}
		catch (IOException e)
		{
		}

		return null;
		
	}

}