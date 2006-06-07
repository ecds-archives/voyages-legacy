package edu.emory.library.tas.web.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamSplitter
{
	
	private byte[] buffer = null;
	private int bufferPos = 0;
	private byte[] boundary = null;
	private boolean[] matches = null;
	private InputStream inputStream = null;
	private int extraBytesStart = 0;
	private int extraBytesEnd = -1;
	private boolean boundaryFound = false;
	private boolean eofFound = false;
	
	public StreamSplitter(InputStream inputStream, byte[] buffer)
	{
		this.inputStream = inputStream;
		this.buffer = buffer;
	}

	public boolean gotoNextPart()
	{
		return gotoNextPart(null);
	}

	public boolean gotoNextPart(byte []boundaryParam)
	{
		
		if (eofFound)
			return false;
	
		if (boundaryParam != null)
		{
			boundary = boundaryParam;
			matches = new boolean[boundary.length];
		}
		
		boundaryFound = false;
		extraBytesStart = 0;
		extraBytesEnd = -1;
		bufferPos = 0;
		
		return true;
		
	}

	public int getNextChunk() throws IOException
	{
		
		int i, j;
		int b;
		
		// we have seen the end of stream already
		if (eofFound)
			return -1;
		
		// don't do anything if we have seen the boundary
		if (boundaryFound)
			return 0;
		
		// init slots for matches
		int matchesPos = 0;
		for (i=0; i<boundary.length; i++)
			matches[i] = false;
		
		// main loop
		bufferPos = 0;
		while (true)
		{
			
			// boundary found or end of buffer
			// -> stop reading
			if (matches[matchesPos] || bufferPos == buffer.length)
			{
				boundaryFound = matches[matchesPos]; 
				extraBytesStart = buffer.length - boundary.length;
				extraBytesEnd = bufferPos - 1;
				return bufferPos - boundary.length;
			}
			
			// read next char
			if (extraBytesStart <= extraBytesEnd)
			{
				b = buffer[extraBytesStart++];
			}
			else
			{
				b = inputStream.read();
				if (b == -1)
				{
					eofFound = true;
					return bufferPos;
				}
			}
			
			// save the char to the buffer
			buffer[bufferPos++] = (byte)b;
			
			// start matching the boundary from
			// this position, i.e. use the free
			// slot in "matches" array
			matches[matchesPos] = b == boundary[0];
			
			// complete the other partial matches
			i = 1;
			j = (matchesPos + 1) % boundary.length;
			while (i < boundary.length)
			{
				if (b != boundary[i]) matches[j] = false;
				i = (i + 1);
				j = (j + 1) % boundary.length; 
			}
			
			// advance to the next position in
			// the slots
			matchesPos = (matchesPos + boundary.length - 1) % boundary.length;

		}
		
	}

	public static void main(String[] args) throws IOException
	{
		
		byte[] buf = new byte[1024];
		FileInputStream file = new FileInputStream("C:\\Documents and Settings\\zich\\Desktop\\test3.txt");
		//InputStream is = new ByteArrayInputStream(new byte[] {10,11,1,2,3,4,5,10,11,6,7,10,11,1,2,5,10,11});
		StreamSplitter sp = new StreamSplitter(file, buf);
		
		int read = 0;
		
		sp.gotoNextPart("-----------------------------4664151417711".getBytes());
		read = sp.getNextChunk();
		System.out.print(new String(buf, 0, read));
		
		sp.gotoNextPart("\r\n".getBytes());
		read = sp.getNextChunk();
		System.out.print(new String(buf, 0, read));

		//sp.gotoNextPart("\r\n".getBytes());
		read = sp.getNextChunk();
		System.out.print(new String(buf, 0, read));

//		while (sp.gotoNextPart("-----------------------------4664151417711".getBytes()))
//		{
//			readTotal = 0;
//			System.out.print("Part: ");
//			while ((read = sp.getNextChunk()) > 0)
//			{
//				if (readTotal > 0) System.out.print(" + ");
//				System.out.print(read);
//				readTotal += read;
//			}
//			System.out.println(" = " + readTotal);
//			System.out.flush();
//		}
		
	}

}
