package edu.emory.library.tas.spss;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import edu.emory.library.tas.DateTimeUtils;

public class ImportLogListBean
{
	
	private String currentImportDirName;
	
	private class InvalidImportDirectoryException extends Exception
	{
		private static final long serialVersionUID = 1019360649699803025L;
	}
	
	private File findImportLog(File[] files)
	{
		for (int j = 0; j < files.length; j++)
		{
			if ("import.log".equals(files[j].getName()))
			{
				return files[j]; 
			}
		}
		return null;
	}

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
				
				ImportLogForDisplay importLogForDisplay = new ImportLogForDisplay();
			
				if (!ImportServlet.isValidImportDirectoryName(dir.getName()))
					throw new InvalidImportDirectoryException();
				
				importLogForDisplay.setId(dir.getAbsolutePath());

				File importLog = findImportLog(dir.listFiles());
				if (importLog == null)
					throw new InvalidImportDirectoryException();
				
				LogReader logReader = new LogReader(importLog.getAbsolutePath());
				LogItem[] items = logReader.loadFlatList();
				
				if (items == null || items.length == 0)
					throw new InvalidImportDirectoryException();
				
				LogItem firstItem = items[0];
				LogItem lastItem = items[items.length - 1];
				
				Date start = new Date(firstItem.getTime());
				Date end = new Date(lastItem.getTime());
				
				importLogForDisplay.setStarted(df.format(start));

				if (lastItem.getType() == LogItem.STAGE_END_OF_IMPORT)
				{
					importLogForDisplay.setFinished(df.format(end));
					importLogForDisplay.setOutcome("finished");
				}
				else
				{
					importLogForDisplay.setFinished("-");
					importLogForDisplay.setOutcome("running");
				}
				
				importLogForDisplay.setDuration(DateTimeUtils.formatTimeSpan(
						start, end, DateTimeUtils.TIME_INTERVAL_ROUND_TO_SEC));
				
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