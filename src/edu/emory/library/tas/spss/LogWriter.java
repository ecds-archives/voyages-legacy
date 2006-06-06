package edu.emory.library.tas.spss;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogWriter
{
	
	private int stage = LogItem.STAGE_UPLOADING;
	private PrintWriter writer = null;
	
	public LogWriter(String fileName) throws IOException
	{
		writer = new PrintWriter(new FileWriter(fileName));
	}

	public void close()
	{
		writer.close();
	}

	public void startStage(int stage)
	{
		this.stage = stage;
	}

	private void logEvent(int type, String message)
	{
		
		// timestamp
		writer.print(System.currentTimeMillis());
		writer.print(" ");
		
		// stage (easier for parsing)
		String stageString = LogItem.getStringForStage(stage);
		writer.print(stageString);
		writer.print(" ");

		// type (easier for parsing)
		String typeString = LogItem.getStringForType(type);
		writer.print(typeString);
		writer.print(" ");
		
		// message
		message = message.replaceAll("\r\n", " ").replace("\r", " ").replace("\n", " ");
		writer.println(message);

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
	
	public static void main(String[] args) throws IOException
	{
		
		LogWriter w = new LogWriter("x.log");
		
		w.startStage(LogItem.STAGE_UPLOADING);
		w.logInfo("Seems to be ok.");
		w.logInfo("Hmm.");
		w.logInfo("Strange\r\nline\r.");
		
		w.startStage(LogItem.STAGE_CONVERSION);
		w.logInfo("Seems to be ok.");
		w.logInfo("Hmm.");
		w.logInfo("Strange\r\nline\r.");

		w.close();
		
		LogReader r = new LogReader("x.log");
		LogItem log[] = r.loadFlatList();
		
		for (int i = 0; i < log.length; i++)
		{
			LogItem li = log[i];
			System.out.println("time    = " + li.getTime());
			System.out.println("type    = " + li.getTypeAsLabel());
			System.out.println("stage   = " + li.getStageAsLabel());
			System.out.println("message = " + li.getMessage());
			System.out.println("------------------------------------------");
		}
		
		
		
	}

}