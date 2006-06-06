package edu.emory.library.tas.spss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReader
{
	
	private static Pattern regexLine = null;
	
	private String fileName;
	
	private void initRegex()
	{
		
		if (regexLine != null)
			return;

		regexLine = Pattern.compile(
			"(\\d+) (.) (.) (.*)");
			
	}
	
	public LogReader(String fileName)
	{
		this.fileName = fileName;
	}
	
	private ArrayList load(boolean groupByStages) throws IOException
	{
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));

		initRegex();
		ArrayList stages = null;
		ArrayList messages = null;
		
		if (groupByStages)
			stages = new ArrayList();
		else
			messages = new ArrayList();

		String line;
		int lastStage = -1;
		int noOfMessages = 0;
		while ((line = reader.readLine()) != null)
		{
			Matcher matcher = regexLine.matcher(line);
			if (matcher.matches())
			{
				
				long time = Long.parseLong(matcher.group(1));
				int stage = LogItem.getStageFromString(matcher.group(2));
				int type = LogItem.getTypeFromString(matcher.group(3));
				String message = matcher.group(4);
				lastStage = stage;
				noOfMessages ++;
				
				if (groupByStages)
				{
					if (noOfMessages == 0 || stage != lastStage)
					{
						messages = new ArrayList();
						stages.add(messages);
					}
				}
				
				LogItem logItem = new LogItem();
				logItem.setTime(time);
				logItem.setStage(stage);
				logItem.setType(type);
				logItem.setMessage(message);
				messages.add(logItem);
				
			}
		}
		reader.close();
		
		if (groupByStages)
			return stages;
		else
			return messages;
		
	}
	
	public LogItemStage[] loadGroupedByStages() throws IOException
	{
		ArrayList stages = load(true);
		LogItemStage[] stagesArray = new LogItemStage[stages.size()];
		int i = 0;
		for (Iterator iterStage = stages.iterator(); iterStage.hasNext();)
		{		
			ArrayList stage = (ArrayList) iterStage.next();
			stagesArray[i].setLogItems((LogItem[]) stage.toArray(new LogItem[] {new LogItem()}));
		}
		return stagesArray;
	}
	
	public LogItem[] loadFlatList() throws IOException
	{
		return (LogItem[]) load(false).toArray(new LogItem[] {new LogItem()});
	}
	
}
