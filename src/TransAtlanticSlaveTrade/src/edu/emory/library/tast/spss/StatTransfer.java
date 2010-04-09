/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.spss;

import java.io.IOException;

public class StatTransfer
{
	
	private String inputFileType;
	private String inputFileName;
	private String outputFileType;
	private String outputFileName;
	private String exeStatTransfer;
	private String stdoutStatTransfer;
	private String erroutStatTransfer;
	
	public StatTransfer(String exeStatTransfer)
	{
		this.exeStatTransfer = exeStatTransfer;
	}
	
	public void transfer() throws IOException, StatTransferException, InterruptedException
	{
		
		// parameters
		int paramsCount = 4;
		if (inputFileType != null) paramsCount++;
		if (outputFileType != null) paramsCount++;
		String [] params = new String[paramsCount];
		
		// fill parameters
		int i = 0;
		params[i++] = exeStatTransfer;
		if (inputFileType != null) params[i++] = inputFileType;
		params[i++] = inputFileName;
		if (outputFileType != null) params[i++] = outputFileType;
		params[i++] = outputFileName;
		params[i++] = "/Y"; // -y in Linux
		//params[i++] = "/Q"; // -q in Linux
		
		// run
		Process st = Runtime.getRuntime().exec(params);
		
		// important! if we don't eat the entire
		// output, the program blocks, and even if
		// the ouput is supresses by /Q (or -q),
		// there are still some characters like \n
		// probably when the program stops
		StreamConsumer istrConsumer = new StreamConsumer(st.getInputStream());
		StreamConsumer estrConsumer = new StreamConsumer(st.getErrorStream());
		istrConsumer.start();
		estrConsumer.start();
		
		// does not work: deadlock
//		InputStream es = st.getErrorStream();
//		InputStream is = st.getInputStream();
//		while (es.read() != -1);
//		while (is.read() != -1);
//		erroutStatTransfer = consumeStream(st.getErrorStream());
//		stdoutStatTransfer = consumeStream(st.getInputStream());

		// throw exception if problem
		if (st.waitFor() != 0)
			throw new StatTransferException();
		
	}
	
//	private String consumeStream(InputStream str) throws IOException
//	{
//		BufferedReader outReader = new BufferedReader(new InputStreamReader(str));
//		StringWriter outputBuffer = new StringWriter();
//		PrintWriter outputBufferPrinter = new PrintWriter(new StringWriter());
//		String line = null;
//		while ((line = outReader.readLine()) != null) outputBufferPrinter.println(line);
//		outReader.close();
//		outputBufferPrinter.close();
//		return outputBuffer.getBuffer().toString();
//	}

	public void setInputFileType(String inputFileType)
	{
		this.inputFileType = inputFileType;
	}

	public String getInputFileType()
	{
		return inputFileType;
	}

	public void setInputFileName(String inputFileName)
	{
		this.inputFileName = inputFileName;
	}

	public String getInputFileName()
	{
		return inputFileName;
	}

	public void setOutputFileType(String outputFileType)
	{
		this.outputFileType = outputFileType;
	}

	public String getOutputFileType()
	{
		return outputFileType;
	}

	public void setOutputFileName(String outputFileName)
	{
		this.outputFileName = outputFileName;
	}

	public String getOutputFileName()
	{
		return outputFileName;
	}

	public String getStatTransferErrout()
	{
		return erroutStatTransfer;
	}

	public String getStatTransferStdout()
	{
		return stdoutStatTransfer;
	}

}