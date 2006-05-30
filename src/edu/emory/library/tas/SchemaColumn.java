package edu.emory.library.tas;

import java.util.Date;

public class SchemaColumn
{
	
	public final static int IMPORT_TYPE_IGNORE = -1;
	public final static int IMPORT_TYPE_NUMERIC = 0; 
	public final static int IMPORT_TYPE_STRING = 1; 
	public final static int IMPORT_TYPE_DATE = 2; 
	
	public final static int TYPE_INTEGER = 0;
	public final static int TYPE_LONG = 1; 
	public final static int TYPE_STRING = 2; 
	public final static int TYPE_DATE = 3;
	public final static int TYPE_DICT_LOCATION = 4;
	public final static int TYPE_DICT_PORT = 5;
	public final static int TYPE_DICT_FAITH = 6;

	private String name;
	private int type;
	private boolean dictionary;
	private int importType;
	private String importName;
	private String importDateDay;
	private String importDateMonth;
	private String importDateYear;
	
	public SchemaColumn(String name, int type, boolean dictionary, int importType, String importName, String importDateDay, String importDateMonth, String importDateYear)
	{
		super();
		this.name = name;
		this.type = type;
		this.dictionary = dictionary;
		this.importType = importType;
		this.importName = importName;
		this.importDateDay = importDateDay;
		this.importDateMonth = importDateMonth;
		this.importDateYear = importDateYear;
	}
	
	public Object parse(String[] values)
	{
		try
		{

			// base types
			if (!dictionary)
			{
				
				switch (type)
				{
					case TYPE_STRING:
						if (values.length != 1) return null;
						return values[0];
						
					case TYPE_INTEGER:
						if (values.length != 1) return null;
						return new Integer(Integer.parseInt(values[0]));
						
					case TYPE_LONG:
						if (values.length != 1) return null;
						return new Long(Long.parseLong(values[0]));
						
					case TYPE_DATE:
						if (values.length != 3) return null;
						int day = Integer.parseInt(values[0]);
						int month = Integer.parseInt(values[1]);
						int year = Integer.parseInt(values[2]);
						return new Date(year, month, day);

					default:
						return null;

				}
			}
			
			// dictionary
			else
			{
				
				// lookup dictinary based on type and locate 
				// in it the element corresponding to id, so
				// something like:
				//
				// if (values.length != 1) return null;
				// int id = Integer.parseInt(value);
				// Dictionary d = Dictionary.lookup(type); 
				// return d.loadItem(id); 
				// 
				// for now, just ...
				
				return null;
				
			}
		}
		catch (NumberFormatException nfe)
		{
			return null;
		}
	}

	public Object parse(String value)
	{
		return parse(new String[] {value});
	}

	public String getImportDateDay()
	{
		return importDateDay;
	}
	
	public String getImportDateMonth()
	{
		return importDateMonth;
	}
	
	public String getImportDateYear()
	{
		return importDateYear;
	}
	
	public String getImportName()
	{
		return importName;
	}
	
	public int getImportType()
	{
		return importType;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getType()
	{
		return type;
	}
	
	public boolean isDictinaory()
	{
		return dictionary;
	}

}
