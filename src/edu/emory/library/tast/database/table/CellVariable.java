package edu.emory.library.tast.database.table;

import java.text.MessageFormat;

import edu.emory.library.tast.dm.attributes.Attribute;

public class CellVariable
{
	
	public static final int COUNT = 1;
	public static final int SUM = 2;
	public static final int AVG = 3;
	
	private Attribute[] attributes;
	private Attribute[] nonNullAttributes;
	private String userLabel;
	private String id;
	private String[] labels;
	private MessageFormat format;
	private String zeroValue;
	private int groupType; 

	public CellVariable(String id, int groupType, String userLabel, Attribute[] attrs, Attribute[] nonNullAttributes, String[] colLabels, MessageFormat format, String zeroValue)
	{
		this.id = id;
		this.groupType = groupType;
		this.userLabel = userLabel;
		this.attributes = attrs;
		this.labels = colLabels;
		this.format = format;
		this.zeroValue = zeroValue;
		this.nonNullAttributes = nonNullAttributes;
	}

	public Attribute[] getAttributes()
	{
		return attributes != null ? attributes : new Attribute[] {};
	}

	public String getId()
	{
		return id;
	}

	public String getUserLabel()
	{
		return userLabel;
	}

	public String[] getLabels()
	{
		return labels;
	}

	public MessageFormat getFormat()
	{
		return format;
	}

	public String getZeroValue()
	{
		return zeroValue;
	}

	public void setZeroValue(String zeroValue)
	{
		this.zeroValue = zeroValue;
	}

	public int getGroupType()
	{
		return groupType;
	}

	public boolean isAvg()
	{
		return groupType == AVG;
	}

	public int getValuesCount()
	{
		return labels.length;
	}

	public Attribute[] getNonNullAttributes()
	{
		return nonNullAttributes != null ? nonNullAttributes : new Attribute[] {};
	}

}
