package edu.emory.library.tas.spss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class RecordWriter
{
	protected BufferedWriter wrt;
	
	public RecordWriter(File file) throws IOException
	{
		try
		{
			wrt = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(file), "windows-1252"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	public void writeRecord(Record record) throws IOException
	{
		if (record == null) return;
		wrt.write(record.getLine());
		wrt.write('\n');
	}
	
	public void write(char[] data) throws IOException
	{
		if (data == null) return;
		write(data, data.length);
	}

	public void write(char[] data, int length) throws IOException
	{
		if (data == null) return;
		wrt.write(data);
		for (int i = 0; i < length - data.length; i++) wrt.write(' ');
	}

	public void write(String data) throws IOException
	{
		if (data == null) return;
		write(data, data.length());
	}

	public void write(String data, int length) throws IOException
	{
		if (data == null) return;
		wrt.write(data);
		for (int i = 0; i < length - data.length(); i++) wrt.write(' ');
	}

	public void finishRecord() throws IOException
	{
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
