package edu.emory.library.tas.spss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AsciiFixedFormatRecordWriter extends RecordWriter
{
	
	private BufferedWriter wrt;
	
	public AsciiFixedFormatRecordWriter(File file) throws IOException
	{
		wrt = new BufferedWriter(new FileWriter(file));
	}
	
	public void writeRecord(Record record) throws IOException
	{
		if (record == null) return;
		//wrt.write(AsciiFixedFormatRecordReader.buf, ((AsciiFixedFormatRecord)record).bufPtr, 2973);
		wrt.write(((AsciiFixedFormatRecord)record).getLine());
		wrt.write('\n');
	}

	public void writeRecords(Record[] records) throws IOException
	{
		for (int i=0; i<records.length; i++)
			writeRecord(records[i]);
	}

	public void close() throws IOException
	{
		wrt.close();
	}

}
