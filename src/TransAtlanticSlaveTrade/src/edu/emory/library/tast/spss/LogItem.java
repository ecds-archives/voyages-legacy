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

public class LogItem
{

	public static final int STAGE_UPLOADING = 0;
	public static final int STAGE_CONVERSION = 1;
	public static final int STAGE_SCHEMA_LOADING = 2;
	public static final int STAGE_SCHEMA_MATCHING = 3;
	public static final int STAGE_NUMBERING = 4;
	public static final int STAGE_SORTING = 5;
	public static final int STAGE_UPDATING_LABELS = 6;
	public static final int STAGE_IMPORTING_DATA = 7;
	public static final int STAGE_SUMMARY = 8;

	public static final int TYPE_INFO = 0;
	public static final int TYPE_WARN = 1;
	public static final int TYPE_ERROR = 2;	
	
	private static final String[] stageStrings = new String[] {"U", "C", "S", "M", "N", "O", "D", "I", "E"};
	private static final String[] typeStrings = new String[] {"I", "W", "E"};
	
	private static final String[] stageLabels = new String[] {
		"File uploading",
		"File conversion from SPSS",
		"Reading data schema",
		"Matching and verifying schema",
		"Numbering voyages and slaves",
		"Sorting voyages and slaves",
		"Synchronizing labels",
		"Importing data to database",
		"Summary" };
	
	private static final String[] typeLabels = new String[] {
		"Information",
		"Warning",
		"Error" };
	
	private int stage;
	private int type;
	private long time;
	private String message;
	
	private static int findInArray(String[] array, String type)
	{
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(type))
				return i;
		return -1;
	}
	
	public static int getTypeFromString(String type)
	{
		return findInArray(typeStrings, type);
	}

	public static int getStageFromString(String stage)
	{
		return findInArray(stageStrings, stage);
	}

	public static String getStringForType(int type)
	{
		if (type < 0 || typeStrings.length <= type) return "?";
		return typeStrings[type];
	}

	public static String getStringForStage(int stage)
	{
		if (stage < 0 || stageStrings.length <= stage) return "?";
		return stageStrings[stage];
	}

	public static String getLabelForType(int type)
	{
		if (type < 0 || typeLabels.length <= type) return "?";
		return typeLabels[type];
	}

	public static String getLabelForStage(int stage)
	{
		if (stage < 0 || stageLabels.length <= stage) return "?";
		return stageLabels[stage];
	}

	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public int getStage()
	{
		return stage;
	}

	public String getStageAsLabel()
	{
		return LogItem.getLabelForStage(stage);
	}

	public void setStage(int stage)
	{
		this.stage = stage;
	}

	public void setStage(String stage)
	{
		this.stage = getStageFromString(stage);
	}

	public long getTime()
	{
		return time;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public int getType()
	{
		return type;
	}

	public String getTypeAsLabel()
	{
		return LogItem.getLabelForType(type);
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public void setType(String type)
	{
		this.type = getTypeFromString(type);
	}
	
}