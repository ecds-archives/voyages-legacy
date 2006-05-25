package edu.emory.library.tas.spss;

import java.io.IOException;

public abstract class RecordReader
{
	public abstract Record readRecord() throws IOException;
	public abstract void close() throws IOException;
}
