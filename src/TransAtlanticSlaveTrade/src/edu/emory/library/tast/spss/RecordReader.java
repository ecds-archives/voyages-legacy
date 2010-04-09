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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class RecordReader
{
	protected BufferedReader rdr;
	protected int startColumn;
	protected int endColumn;
	protected int columnsCount;
	
	public RecordReader(File file, int startColumn, int endColumn, int columnsCount) throws FileNotFoundException
	{

		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.columnsCount = columnsCount;

		try
		{
			rdr = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file), "windows-1252"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

	}
	
	public Record readRecord() throws IOException
	{
		
		// read entire row
		char buf[] = new char[columnsCount];
		if (rdr.read(buf, 0, columnsCount) != columnsCount) return null;
		
		// extract the key column
		String key = new String(buf, startColumn-1, endColumn-startColumn+1);
		
		// consume \n and \r
		rdr.readLine();
		
		// return the record
		return new Record(key, buf);
		
	}
	
	public void close() throws IOException
	{
		rdr.close();
	}

}
