package edu.emory.library.tas.web.spss;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.emory.library.tas.DateTimeUtils;
import edu.emory.library.tas.spss.Log;
import edu.emory.library.tas.spss.LogReader;
import edu.emory.library.tas.spss.LogReaderException;

public class LogForDisplayInDetail
{
	
	private String timeStart;
	private String timeFinish;
	private String duration;
	private String statusText;
	private boolean finished = false;
	private boolean voyagesPresent = false;
	private boolean slavesPresent = false;
	private List logItems;
	
	public static LogForDisplayInDetail load(String importDirName, int skip)
	{
		
		DateFormat df = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

		try
		{
		
			LogForDisplayInDetail detail = new LogForDisplayInDetail();
			
			File importDir = new File("C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\imports" + File.separatorChar + importDirName);
			
			File[] files = importDir.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				File file = files[i];
				if (file.getName().equals("voyages.sav")) detail.setVoyagesPresent(true);
				if (file.getName().equals("slaves.sav")) detail.setSlavesPresent(true);
			}

			LogReader rdr = new LogReader(importDir.getAbsolutePath());
			Log importLog = rdr.load(skip);
			detail.setLogItems(importLog.getItems());
			
			detail.setTimeStart(df.format(importLog.getTimeStart()));
	
			Date lastActivityTime = null;
			detail.setFinished(importLog.isFinished());
			if (importLog.isFinished())
			{
				lastActivityTime = importLog.getTimeFinish();
				detail.setTimeFinish(df.format(importLog.getTimeFinish()));
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
		catch (LogReaderException e)
		{
		}

		return null;
		
	}

	public String getDuration()
	{
		return duration;
	}

	public void setDuration(String duration)
	{
		this.duration = duration;
	}

	public String getTimeFinish()
	{
		return timeFinish;
	}

	public void setTimeFinish(String finished)
	{
		this.timeFinish = finished;
	}

	public List getLogItems()
	{
		return logItems;
	}

	public void setLogItems(List logItems)
	{
		this.logItems = logItems;
	}

	public boolean isSlavesPresent()
	{
		return slavesPresent;
	}

	public void setSlavesPresent(boolean slavesPresent)
	{
		this.slavesPresent = slavesPresent;
	}

	public String getTimeStart()
	{
		return timeStart;
	}

	public void setTimeStart(String started)
	{
		this.timeStart = started;
	}

	public String getStatusText()
	{
		return statusText;
	}

	public void setStatusText(String status)
	{
		this.statusText = status;
	}

	public boolean isVoyagesPresent()
	{
		return voyagesPresent;
	}

	public void setVoyagesPresent(boolean voyagesPresent)
	{
		this.voyagesPresent = voyagesPresent;
	}

	public boolean isFinished()
	{
		return finished;
	}

	public void setFinished(boolean finished)
	{
		this.finished = finished;
	}

}