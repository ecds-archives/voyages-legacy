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

/**
 * Represents a single row in the fixed width format in an imported data file.
 * 
 * @author Jan Zich
 */
public class Record
{
	protected char[] line;
	protected String key;

	public Record(String key, char[] line)
	{
		this.line = line;
		this.key = key;
	}
	
	public int compareTo(Record record)
	{
		return this.key.compareTo(record.getKey());
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getKey()
	{
		return key;
	}

	public void setLine(char[] line)
	{
		this.line = line;
	}

	public char[] getLine()
	{
		return line;
	}
	
	public String getValue(STSchemaVariable var)
	{
		return new String(line, var.getStartColumn()-1, var.getLength()).trim();
	}
	
}
