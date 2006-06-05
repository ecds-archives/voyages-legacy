package edu.emory.library.tas.web.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Utils
{
	
	public static int getIntParameter(HttpServletRequest request, String name, int def)
	{
		if (request.getParameter(name) == null) return def;
		try
		{
			return Integer.parseInt(request.getParameter(name));
		}
		catch (NumberFormatException nfe)
		{
			return def;
		}
	}

	public static int getIntParameter(HttpServletRequest request, String name)
	{
		return getIntParameter(request, name, 0);
	}
	
	public static long getLongParameter(HttpServletRequest request, String name, long def)
	{
		if (request.getParameter(name) == null) return def;
		try
		{
			return Long.parseLong(request.getParameter(name));
		}
		catch (NumberFormatException nfe)
		{
			return def;
		}
	}

	public static long getLongParameter(HttpServletRequest request, String name)
	{
		return getLongParameter(request, name, 0);
	}

	public static void setEncodingToUTF8(HttpServletResponse response) 
	{
		response.setCharacterEncoding("UTF8");
		response.setContentType("text/html; charset=UTF-8");
	}

}
