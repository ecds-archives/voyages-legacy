package edu.emory.library.tas.spss;

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
			rdr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "windows-1252"));
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
