package edu.emory.library.tast.misc;

import java.io.IOException;
import java.util.ArrayList;

public class JavaProgramRunner
{
	
	private String classpath;
	private String memoryHeap;
	private String startClass;
	private String[] parameters;
	
	public JavaProgramRunner()
	{
	}
	
	public JavaProgramRunner(String startClass)
	{
		this.startClass = startClass;
	}

	public void run() throws IOException
	{
		
		// prepate aguments
		ArrayList execParams = new ArrayList();
		execParams.add("java");
		
		// classpath
		execParams.add("-classpath");
		execParams.add(classpath);
		
		// memory
		if (memoryHeap != null)
			execParams.add("-Xmx" + memoryHeap);
		
		// import class
		execParams.add(startClass);
		
		// extra params
		for (int i = 0; i < parameters.length; i++)
			execParams.add(parameters[i]);
		
		// debug
//		for (Iterator iter = execParams.iterator(); iter.hasNext();)
//		{
//			String p = (String) iter.next();
//			System.out.println(p);
//		}
		
		// run import
		Runtime runtime = Runtime.getRuntime();
		runtime.exec((String[]) execParams.toArray(new String[] {""}));
		// p.getInputStream();
		
	}
	
	public String getClasspath()
	{
		return classpath;
	}
	
	public void setClasspath(String classpath)
	{
		this.classpath = classpath;
	}
	
	public String getMemoryHeap()
	{
		return memoryHeap;
	}
	
	public void setMemoryHeap(String memoryHeap)
	{
		this.memoryHeap = memoryHeap;
	}
	
	public String[] getParameters()
	{
		return parameters;
	}
	
	public void setParameters(String[] parameters)
	{
		this.parameters = parameters;
	}

	public String getStartClass()
	{
		return startClass;
	}

	public void setStartClass(String startClass)
	{
		this.startClass = startClass;
	}
	
}
