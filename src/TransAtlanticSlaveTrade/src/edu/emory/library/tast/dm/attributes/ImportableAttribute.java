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
