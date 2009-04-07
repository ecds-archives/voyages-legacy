package edu.emory.library.tast.dm.attributes;

import org.hibernate.Session;

import edu.emory.library.tast.spss.LogWriter;

public abstract class ImportableAttribute extends Attribute
{
	
	public static final int LENGTH_UNLIMITED = -1;

	private int maxImportLength = LENGTH_UNLIMITED;
	private String importName;

	public ImportableAttribute(String object, String objectType)
	{
		super(object, objectType);
	}

	public ImportableAttribute(String object, String objectType, String importName)
	{
		super(object, objectType);
		this.importName = importName;
	}

	public String getImportName()
	{
		return importName;
	}

	public void setImportName(String importName)
	{
		this.importName = importName;
	}
	
	public boolean ignoreImport()
	{
		return importName == null;
	}

	public int getMaxImportLength()
	{
		return maxImportLength;
	}

	public boolean isImportLengthLimited()
	{
		return maxImportLength != LENGTH_UNLIMITED;
	}

	public void setMaxImportLength(int maxImportLength)
	{
		this.maxImportLength = maxImportLength;
	}

	public abstract Object importParse(Session sess, String value, LogWriter log, int recordNo);
	public abstract int getImportType();

}
