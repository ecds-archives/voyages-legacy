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

import com.metaparadigm.jsonrpc.JSONRPCBridge;

import edu.emory.library.tas.DateTimeUtils;
import edu.emory.library.tas.spss.Log;
import edu.emory.library.tas.spss.ImportServlet;
import edu.emory.library.tas.spss.LogReader;
import edu.emory.library.tas.spss.LogReaderException;

public class ImportLogBean
{
	
	private String currentImportDirName;
	
	public ImportLogBean()
	{
		try
		{
			JSONRPCBridge.getGlobalBridge().registerClass(
					"ImportLogDetail", LogForDisplayInDetail.class);
		}
		catch (Exception e)
		{
		}
	}
	
	private class InvalidImportDirectoryException extends Exception
	{
		private static final long serialVersionUID = 1019360649699803025L;
	}
	
//	private File findImportLog(File[] files)
//	{
//		for (int j = 0; j < files.length; j++)
//		{
//			if ("import.log".equals(files[j].getName()))
//			{
//				return files[j]; 
//			}
//		}
//		return null;
//	}

	public List getImportLogs()
	{
		
		File importDir = new File("C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\imports");
		if (!importDir.isDirectory()) return null;
		
		DateFormat df = SimpleDateFormat.getDateTimeInstance(
				DateFormat.SHORT, DateFormat.SHORT);
		
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
				
				importLogForDisplay.setId(dir.getName());

				LogReader rdr = new LogReader(dir.getAbsolutePath());
				Log importLog = rdr.load(Integer.MAX_VALUE);
				
				importLogForDisplay.setStarted(df.format(importLog.getTimeStart()));
				
				Date lastActivityTime = null;
				if (importLog.isFinished())
				{
					lastActivityTime = importLog.getTimeFinish();
					importLogForDisplay.setFinished(df.format(importLog.getTimeFinish()));
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
			catch (LogReaderException e)
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
		UIParameter itemIdParam = (UIParameter) event.getComponent().findComponent("itemId");
		if (itemIdParam == null) return;
		currentImportDirName = (String) itemIdParam.getValue();
	}

	public String getCurrentImportDirName()
	{
		return currentImportDirName;
	}

	public void setCurrentImportDirName(String currentImportDirName)
	{
		this.currentImportDirName = currentImportDirName;
	}

}