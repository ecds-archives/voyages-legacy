package edu.emory.library.tas.spss;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.context.FacesContext;

import edu.emory.library.tas.DateTimeUtils;

public class ImportLogDetailBean
{
	
	private String importDirName;
	private boolean loaded = false;
	private String started;
	private String finished;
	private String duration;
	private int noOfWarnings;
	
	private void loadLog()
	{
		
		try
		{
		
			if (loaded) return;
			loaded = true;
	
			DateFormat df =
				SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
	
			LogReader rdr = new LogReader(importDirName + File.separatorChar + "import.log");
			LogItem[] items = rdr.loadFlatList();
			
			LogItem firstItem = items[0];
			LogItem lastItem = items[items.length - 1];
			
			Date start = new Date(firstItem.getTime());
			Date end = new Date(lastItem.getTime());
			
			started = df.format(start);
	
			if (lastItem.getType() == LogItem.STAGE_END_OF_IMPORT)
			{
				finished = df.format(end);
			}
			else
			{
				finished = "-";
			}
			
			duration = DateTimeUtils.formatTimeSpan(
					start, end, DateTimeUtils.TIME_INTERVAL_ROUND_TO_SEC);
			
			noOfWarnings = 0;
			for (int i = 0; i < items.length; i++)
			{
				LogItem item = items[i];
				if (item.getType() == LogItem.TYPE_WARN) noOfWarnings ++;
			}
		
		}
		catch (IOException e)
		{
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch (LogReaderException e)
		{
			FacesContext.getCurrentInstance().responseComplete();
		}

	}

	public String getImportDirName()
	{
		return importDirName;
	}

	public void setImportDirName(String currentImportDirName)
	{
		this.importDirName = currentImportDirName;
	}

	public String getDuration()
	{
		loadLog();
		return duration;
	}

	public String getFinished()
	{
		loadLog();
		return finished;
	}

	public int getNoOfWarnings()
	{
		loadLog();
		return noOfWarnings;
	}

	public String getStarted()
	{
		loadLog();
		return started;
	}

}
