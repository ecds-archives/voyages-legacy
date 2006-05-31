package edu.emory.library.tas.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AsciiFixedFormatRecordIOFactory extends RecordIOFactory
{
	
	private int startColumn;
	private int endColumn;
	private int columnsCount;
	
	public AsciiFixedFormatRecordIOFactory(int startColumn, int endColumn, int columnsCount) throws FileNotFoundException
	{
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.columnsCount = columnsCount;
	}
	
	public RecordWriter createWriter(File file) throws IOException
	{
		return new AsciiFixedFormatRecordWriter(file);
	}

	public RecordReader createReader(File file) throws IOException
	{
		return new AsciiFixedFormatRecordReader(file, startColumn, endColumn, columnsCount);
	}

	public RecordWriter createWriter(String fileName) throws IOException
	{
		return createWriter(new File(fileName));
	}

	public RecordReader createReader(String fileName) throws IOException
	{
		return createReader(new File(fileName));
	}

}
