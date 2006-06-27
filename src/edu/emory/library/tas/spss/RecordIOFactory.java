package edu.emory.library.tas.spss;

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
