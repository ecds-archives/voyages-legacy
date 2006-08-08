package edu.emory.library.tast.misc.prelim.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.metaparadigm.jsonrpc.JSONRPCBridge;

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
	
	public static JSONRPCBridge getJSONRPCBridge(HttpSession session)
	{
		JSONRPCBridge bridge = (JSONRPCBridge)session.getAttribute("JSONRPCBridge");
		if (bridge == null)
		{
		    bridge = new JSONRPCBridge();
		    session.setAttribute("JSONRPCBridge", bridge);
		}
		try
		{
			bridge.registerClass("search", Search.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bridge;
	}

}
