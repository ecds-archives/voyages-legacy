package edu.emory.library.tast.spss;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogWriter
{
	
	private int stage = LogItem.STAGE_UPLOADING;
	private PrintWriter writerItems = null;
	private PrintWriter writerTimes = null;
	
	public static String getLogItemsFileName(String dirName)
	{
		return dirName + File.separatorChar + "import-items.log";
	}
	
	public static String getLogTimesFileName(String dirName)
	{
		return dirName + File.separatorChar + "import-times.log";
	}

	public LogWriter(String dirName) throws IOException
	{
		writerItems = new PrintWriter(new FileWriter(LogWriter.getLogItemsFileName(dirName), true));
		writerTimes = new PrintWriter(new FileWriter(LogWriter.getLogTimesFileName(dirName), true));
	}

	public void close()
	{
		writerItems.close();
		writerTimes.close();
	}
	
	public void startImport()
	{
		writerTimes.println(System.currentTimeMillis());
		writerTimes.flush();
	}

	public void startStage(int stage)
	{
		this.stage = stage;
		writerItems.flush();
	}

	private void logEvent(int type, String message)
	{
		
		// timestamp
		writerItems.print(System.currentTimeMillis());
		writerItems.print(" ");
		
		// stage (easier for parsing)
		String stageString = LogItem.getStringForStage(stage);
		writerItems.print(stageString);
		writerItems.print(" ");

		// type (easier for parsing)
		String typeString = LogItem.getStringForType(type);
		writerItems.print(typeString);
		writerItems.print(" ");
		
		// message
		if (message != null)
		{
			message = message.replaceAll("\\r\\n", " ").replaceAll("\\r", " ").replaceAll("\\n", " ");
			writerItems.print(message);
		}
		else
		{
			writerItems.print("-");
		}
		writerItems.println();
		
		// we want to see it
		writerItems.flush();

	}
	
	public void logInfo(String message)
	{
		logEvent(LogItem.TYPE_INFO, message);
	}

	public void logWarn(String message)
	{
		logEvent(LogItem.TYPE_WARN, message);
	}

	public void logError(String message)
	{
		logEvent(LogItem.TYPE_ERROR, message);
	}
	
	public void doneWithSuccess()
	{
		writerTimes.println(System.currentTimeMillis());
		writerTimes.println("OK");
	}

	public void doneWithErrors()
	{
		writerTimes.println(System.currentTimeMillis());
		writerTimes.println("KO");
	}

	public static void main(String[] args) throws IOException, LogReaderException
	{
		
//		LogWriter w = new LogWriter("x.log");
//		
//		w.startStage(LogItem.STAGE_UPLOADING);
//		w.logInfo("Seems to be ok.");
//		w.logInfo("Hmm.");
//		w.logInfo("Strange\r\nline\r.");
//		
//		w.startStage(LogItem.STAGE_CONVERSION);
//		w.logInfo("Seems to be ok.");
//		w.logInfo("Hmm.");
//		w.logInfo("Strange\r\nline\r.");
//
//		w.close();
//		
//		LogReader r = new LogReader("x.log");
//		LogItem log[] = r.loadFlatList();
//		
//		for (int i = 0; i < log.length; i++)
//		{
//			LogItem li = log[i];
//			System.out.println("time    = " + li.getTime());
//			System.out.println("type    = " + li.getTypeAsLabel());
//			System.out.println("stage   = " + li.getStageAsLabel());
//			System.out.println("message = " + li.getMessage());
//			System.out.println("------------------------------------------");
//		}
		
		
		
	}

}