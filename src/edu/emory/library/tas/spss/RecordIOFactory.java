package edu.emory.library.tas.spss;

import java.io.File;
import java.io.IOException;

public abstract class RecordIOFactory
{
	public abstract RecordWriter createWriter(File file) throws IOException;
	public abstract RecordReader createReader(File file) throws IOException;
	public abstract RecordWriter createWriter(String fileName) throws IOException;
	public abstract RecordReader createReader(String fileName) throws IOException;
}
