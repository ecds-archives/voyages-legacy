package edu.emory.library.tas.web.upload;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHeader
{
	
	private static Pattern regexValue = null;
	private static Pattern regexName = null;

	private String headerName;
	private Hashtable partsMap = new Hashtable();
	private String mainValue = null; 
	
	private void initRegexs()
	{
		
		if (regexName != null && regexValue != null)
			return;

		regexName = Pattern.compile(
			"([a-zA-Z-]+)\\s*:");
			
		regexValue = Pattern.compile(
			"(?:\\s*([a-zA-Z-]+)\\s*=\\s*\"([^\"]+)\"\\s*(?:;|$))" +
			"|" +
			"(?:\\s*([a-zA-Z-]+)\\s*=\\s*([^;]+)\\s*(?:;|$))" +
			"|" +
			"([^;]+)");

	}
	
	public HttpHeader(String value, boolean containsName)
	{
		
		// make sure that we the regex
		initRegexs();
		
		// extract name
		Matcher matcherName = regexName.matcher(value);
		if (containsName && matcherName.lookingAt())
		{
			headerName = matcherName.group(1);
			value = value.substring(matcherName.end());
			//System.out.println("Header name = '" + headerName  + "'");
		}
		
		// start
		Matcher matcherValue = regexValue.matcher(value);
		while (matcherValue.find())
		{
			
			// quoted name=value pair
			String fldName = matcherValue.group(1);
			String fldValue = matcherValue.group(2);
			
			// unquoted name=value pair
			if (fldName == null)
			{
				fldName = matcherValue.group(3);
				fldValue = matcherValue.group(4);
			}
			
			// simple field
			if (fldName == null)
			{
				fldValue = matcherValue.group(5).trim();
			}
			
			// for hashmap store only the named fields
			if (fldName != null)
				partsMap.put(fldName, fldValue);
			
			// the default value
			if (fldName == null)
				mainValue = fldValue;
			
			// debug:
			// if (fldName != null) System.out.print(fldName + " = ");
			// System.out.println("'" + fldValue + "'");

		}
		
	}
	
	public String getNamedPart(String name)
	{
		return (String) partsMap.get(name);
	}
	
	public String getMainValue()
	{
		return mainValue;
	}
	
	public String getHeaderName()
	{
		return headerName;
	}

	public static void main(String[] args)
	{
		//HttpHeader h = new HttpHeader("Content-Type: multipart/form-data; boundary=---------------------------295771922930", true);
		HttpHeader h = new HttpHeader("Content-Disposition: form-data; name=\"file2\"; filename=\"test2.txt\"", true);
		//HttpHeader h = new HttpHeader("form-data; name=\"file2\"; filename=\"C:\\Documents and Settings\\zich\\Desktop\\test2.txt\"");
		System.out.println(h.getHeaderName());
		System.out.println(h.getMainValue());
		//System.out.println(h.getNamedPart("boundary"));
		//System.out.println(UUID.randomUUID());
	}

}
