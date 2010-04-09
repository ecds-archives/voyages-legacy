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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RecordIOFactory
{
	private int startColumn;
	private int endColumn;
	private int columnsCount;
	
	public RecordIOFactory(int startColumn, int endColumn, int columnsCount) throws FileNotFoundException
	{
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.columnsCount = columnsCount;
	}
	
	public RecordWriter createWriter(File file) throws IOException
	{
		return new RecordWriter(file);
	}
	
	public RecordReader createReader(File file) throws IOException
	{
		return new RecordReader(file, startColumn, endColumn, columnsCount);
	}
	
	public RecordWriter createWriter(String fileName) throws IOException
	{
		return createWriter(new File(fileName));
	}
	
	public RecordReader createReader(String fileName) throws IOException
	{
		return createReader(new File(fileName));
	}
	
	public int getColumnsCount()
	{
		return columnsCount;
	}

	public void setColumnsCount(int columnsCount)
	{
		this.columnsCount = columnsCount;
	}

	public int getEndColumn()
	{
		return endColumn;
	}

	public void setEndColumn(int endColumn)
	{
		this.endColumn = endColumn;
	}

	public int getStartColumn()
	{
		return startColumn;
	}

	public void setStartColumn(int startColumn)
	{
		this.startColumn = startColumn;
	}
}
