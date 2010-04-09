/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.spss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogReader
{
	
	private static Pattern regexLine = null;
	
	private String dirName;
	
	private void initRegex()
	{
		
		if (regexLine != null)
			return;

		regexLine = Pattern.compile(
			"(\\d+) (.) (.) (.*)");
			
	}
	
	public LogReader(String fileName)
	{
		this.dirName = fileName;
	}
	
	public Log loadAll() throws IOException
	{
		return load(0);
	}

	public Log load(int skip) throws IOException
	{
		
		BufferedReader rdrItems = new BufferedReader(new FileReader(LogWriter.getLogItemsFileName(dirName)));
		BufferedReader rdrTimes = new BufferedReader(new FileReader(LogWriter.getLogTimesFileName(dirName)));

		Log importLog = new Log();

		initRegex();
		List items = new ArrayList();

		String line;
		int i = 0;
		while ((line = rdrItems.readLine()) != null)
		{
			if (skip <= i++)
			{
				Matcher matcher = regexLine.matcher(line);
				if (!matcher.matches()) break;
					
				long time = Long.parseLong(matcher.group(1));
				int stage = LogItem.getStageFromString(matcher.group(2));
				int type = LogItem.getTypeFromString(matcher.group(3));
				String message = matcher.group(4);
				
				LogItem logItem = new LogItem();
				logItem.setTime(time);
				logItem.setStage(stage);
				logItem.setType(type);
				logItem.setMessage(message);
				items.add(logItem);
			}
		}
		rdrItems.close();
		importLog.setItems(items);
		
		String timeStartStr = rdrTimes.readLine();
		String timeFinishStr = rdrTimes.readLine();
		String finishStatus = rdrTimes.readLine();
		rdrTimes.close();
		
		try
		{
			importLog.setTimeStart(new Date(Long.parseLong(timeStartStr)));
			importLog.setTimeFinish(new Date(Long.parseLong(timeFinishStr)));
		}
		catch (NumberFormatException nfe)
		{
		}
		
		if (finishStatus != null && "OK".equals(finishStatus))
			importLog.setFinishedOK(true);
		
		return importLog;
		
	}
	
//	public LogItemStage[] loadGroupedByStages() throws IOException, LogReaderException
//	{
//		List stages = load(true);
//		LogItemStage[] stagesArray = new LogItemStage[stages.size()];
//		int i = 0;
//		for (Iterator iterStage = stages.iterator(); iterStage.hasNext();)
//		{		
//			List stage = (ArrayList) iterStage.next();
//			stagesArray[i].setLogItems((LogItem[]) stage.toArray(new LogItem[] {new LogItem()}));
//		}
//		return stagesArray;
//	}
//	
//	public LogItem[] loadFlatList() throws IOException, LogReaderException
//	{
//		List items = load(false);
//		return (LogItem[]) items.toArray(new LogItem[items.size()]);
//	}
	
}
