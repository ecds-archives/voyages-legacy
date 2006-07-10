package edu.emory.library.tas.web.spss;

import java.text.DateFormat;
import java.util.Date;

import edu.emory.library.tas.spss.LogItem;

public class LogItemForDisplayInDetail
{

	private int stage;
	private String stageLabel;
	private int type;
	private String typeImg;
	private String timeText;
	private String message;
	
	public LogItemForDisplayInDetail(LogItem item, DateFormat df)
	{

		this.timeText = df.format(new Date(item.getTime()));

		this.type = item.getType();
		switch (item.getType())
		{
			case LogItem.TYPE_INFO:
				this.typeImg = "import-log-info.png";
				break;
			case LogItem.TYPE_WARN:
				this.typeImg = "import-log-warn.png";
				break;
			case LogItem.TYPE_ERROR:
				this.typeImg = "import-log-error.png";
				break;
		}
		
		this.stage = item.getStage();
		this.stageLabel = LogItem.getLabelForStage(item.getStage());
		
		this.message = item.getMessage();

	}
	
	public String getMessage()
	{
		return message;
	}
	
	public int getStage()
	{
		return stage;
	}
	
	public String getStageLabel()
	{
		return stageLabel;
	}
	
	public String getTimeText()
	{
		return timeText;
	}
	
	public int getType()
	{
		return type;
	}

	public String getTypeImg()
	{
		return typeImg;
	}
	
}