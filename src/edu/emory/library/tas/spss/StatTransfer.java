package edu.emory.library.tas.spss;

import java.io.IOException;
import java.io.InputStream;

public class StatTransfer
{
	
	private String inputFileType;
	private String inputFileName;
	private String outputFileType;
	private String outputFileName;
	
	public StatTransfer()
	{
	}
	
	public boolean transfer() throws IOException
	{
		
		// parameters
		int paramsCount = 5;
		if (inputFileType != null) paramsCount++;
		if (outputFileType != null) paramsCount++;
		String [] params = new String[paramsCount];
		
		// fill parameters
		int i = 0;
		params[i++] = "C:\\Program Files\\StatTransfer8\\st.exe";
		if (inputFileType != null) params[i++] = inputFileType;
		params[i++] = inputFileName;
		if (outputFileType != null) params[i++] = outputFileType;
		params[i++] = outputFileName;
		params[i++] = "/Y"; // -y in Linux
		params[i++] = "/Q"; // -q in Linux
		
		// run
		Process st = Runtime.getRuntime().exec(params);
		
		// important! if we don't eat the entire
		// output, the program blocks, and even if
		// the ouput is supresses by /Q (or -q),
		// there are still some characters like \n
		// probably when the program stops
		InputStream io = st.getInputStream();
		while (io.read() != -1);
		
		// done, check exit code
		return st.exitValue() != 0;
		
	}

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

}
