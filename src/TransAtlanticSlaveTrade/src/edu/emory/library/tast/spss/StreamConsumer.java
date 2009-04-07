package edu.emory.library.tast.spss;

import java.io.IOException;
import java.io.InputStream;

public class StreamConsumer extends Thread
{
	
	private InputStream istr;
	private boolean read = false;
	
	public StreamConsumer(InputStream istr)
	{
		this.istr = istr;
	}
	
	public void run()
	{
		try
		{
			while (istr.read() != -1);
			istr.close();
			read = true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean haveRead()
	{
		return read;
	}

}