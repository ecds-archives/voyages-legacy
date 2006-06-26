package edu.emory.library.tas.spss;

import java.util.ArrayList;

public class STSchemaVariable
{
	
	private String name;
	private int startColumn;
	private int endColumn;
	private int type;
	private String label;
	private String tag;
	private boolean doImport;
	private ArrayList labels = new ArrayList();
	//private Hashtable dictionaryCache = new Hashtable();
	
	public final static int TYPE_STRING = 0; 
	public final static int TYPE_NUMERIC = 1; 
	
	public STSchemaVariable()
	{
	}

	public STSchemaVariable(String name, int startColumn, int endColumn)
	{
		this.name = name;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.type = TYPE_STRING;
	}

	public STSchemaVariable(String name, int startColumn, int endColumn, int type)
	{
		this.name = name;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.type = type;
	}

	public STSchemaVariable(String name, int startColumn, int endColumn, String tag)
	{
		this.name = name;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.type = TYPE_NUMERIC;
		this.tag = tag;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public String getLabel()
	{
		return label;
	}

	public void setStartColumn(int startColumn)
	{
		this.startColumn = startColumn;
	}
	
	public int getStartColumn()
	{
		return startColumn;
	}
	
	public void setEndColumn(int endColumn)
	{
		this.endColumn = endColumn;
	}
	
	public int getEndColumn()
	{
		return endColumn;
	}
	
	public int getLength()
	{
		return endColumn - startColumn + 1;
	}

	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public boolean hasTag()
	{
		return tag != null;
	}

	public void setLabels(ArrayList labels)
	{
		this.labels = labels;
	}
	
	public ArrayList getLabels()
	{
		return labels;
	}

	public boolean hasLabels()
	{
		return labels != null && labels.size() > 0;
	}

	public void setDoImport(boolean doImport)
	{
		this.doImport = doImport;
	}

	public boolean isDoImport()
	{
		return doImport;
	}

}