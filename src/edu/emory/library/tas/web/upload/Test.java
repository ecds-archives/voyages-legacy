package edu.emory.library.tas.web.upload;

import java.io.File;
import java.io.IOException;

public class Test
{
	
	private static void printFileInfo(File x)
	{
		System.out.println("Name          = " + x.getName());
		System.out.println("Parent        = " + x.getParent());
		System.out.println("AbsolutePath  = " + x.getAbsolutePath());
//		System.out.println("CanonicalPath = " + x.getCanonicalPath());
	}

	public static void main(String[] args) throws IOException
	{

		printFileInfo(new File("C:\\Documents and Settings\\zich\\Desktop\\test2.txt"));
		printFileInfo(new File("/home/jzich/test2.txt"));
		
	}

}
