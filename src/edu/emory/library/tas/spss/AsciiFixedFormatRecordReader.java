package edu.emory.library.tas.spss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class AsciiFixedFormatRecordReader extends RecordReader
{
	
	private BufferedReader rdr;
	private int startColumn;
	private int endColumn;
	private int columnsCount;
	
	//public static char[] buf = new char[1024*1024*8];
	//public static int bufPtr = 0;
	
	public AsciiFixedFormatRecordReader(File file, int startColumn, int endColumn, int columnsCount) throws FileNotFoundException
	{
		this.startColumn = startColumn;
		this.endColumn = endColumn;
		this.columnsCount = columnsCount;
		rdr = new BufferedReader(new FileReader(file));
	}
	
	public Record readRecord() throws IOException
	{
		
		// read entire row
		char buf[] = new char[columnsCount];
		if (rdr.read(buf, 0, columnsCount) == -1) return null;
		//if (rdr.read(buf, bufPtr, columnsCount) == -1) return null;
		//bufPtr += columnsCount;
		
		// extract the key column
		//String key = new String(buf, bufPtr+startColumn-columnsCount, endColumn-startColumn+1);
		String key = new String(buf, startColumn-1, endColumn-startColumn+1);
		
		// consume \n and \r
		rdr.readLine();
		
		// return the record
		return new AsciiFixedFormatRecord(key, buf);
		
	}

	public void close() throws IOException
	{
		rdr.close();
	}

}
