package edu.emory.library.tast.common.grideditor;

public class Column
{
	
	private String label;
	private String name;
	private boolean readOnly = false;
	private String copyToColumn = null;
	private String copyToLabel = null;
	private boolean compareTo = false;
	
	public Column(String name, String label)
	{
		this.label = label;
		this.name = name;
	}

	public Column(String name, String label, boolean readOnly)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
	}

	public Column(String name, String label, boolean readOnly, boolean compareTo)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
		this.compareTo = compareTo;
	}

	public Column(String name, String label, boolean readOnly, String copyToColumnName, String copyToLabel)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
		this.copyToColumn = copyToColumnName;
		this.copyToLabel = copyToLabel;
	}

	public Column(String name, String label, boolean readOnly, String copyToColumnName, String copyToLabel, boolean compareTo)
	{
		this.label = label;
		this.name = name;
		this.readOnly = readOnly;
		this.copyToColumn = copyToColumnName;
		this.copyToLabel = copyToLabel;
		this.compareTo = compareTo;
	}

	public String getLabel()
	{
		return label;
	}
	public String getName()
	{
		return name;
	}

	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	public void enableCopy(String columnName, String label)
	{
		copyToColumn = columnName;
		copyToLabel = label;
	}
	
	public boolean isCopyToEnabled()
	{
		return copyToColumn != null;
	}

	public String getCopyToColumn()
	{
		return copyToColumn;
	}

	public void setCopyToColumn(String copyToColumn)
	{
		this.copyToColumn = copyToColumn;
	}

	public String getCopyToLabel()
	{
		return copyToLabel;
	}

	public void setCopyToLabel(String copyToLabel)
	{
		this.copyToLabel = copyToLabel;
	}

	public boolean isCompareTo()
	{
		return compareTo;
	}

	public void setCompareTo(boolean compareTo)
	{
		this.compareTo = compareTo;
	}

}
