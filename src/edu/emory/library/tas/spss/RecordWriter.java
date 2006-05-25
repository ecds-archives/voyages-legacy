package edu.emory.library.tas.spss;

import java.io.IOException;

public abstract class RecordWriter
{
	public abstract void writeRecord(Record record) throws IOException;
	public abstract void writeRecords(Record[] records) throws IOException;
	public abstract void close() throws IOException;
}
