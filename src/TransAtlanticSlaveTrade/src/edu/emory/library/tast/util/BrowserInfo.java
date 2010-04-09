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
package edu.emory.library.tast.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/***
 * This class is very incomplete. I just needed to detect in one
 * not so important place whether the browser is IE or not, and
 * so I though I wrap it into some general class which could be
 * tuned possibly to something useful later.
 * 
 * @author Jan Zich
 */

public class BrowserInfo
{
	
	public final static int INTERNET_EXPLORER = 0;
	public final static int FIREFOX = 1;
	public final static int SAFARI = 2;
	public final static int OPERA = 3;
	public final static int UNKNOWN = -1;
	
	private String userAgent;
	private int browserName = UNKNOWN;
	private int majorVersion = UNKNOWN;
	private int minorVersion = UNKNOWN;
	
	public BrowserInfo(FacesContext context)
	{
		
		HttpServletRequest request =
			(HttpServletRequest) FacesContext.getCurrentInstance().
			getExternalContext().getRequest();
		
		userAgent = request.getHeader("user-agent");
		analyze();
		
	}
	
	private void analyze()
	{
		if (userAgent.indexOf("MSIE 7.0") != -1)
		{
			browserName = INTERNET_EXPLORER;
			majorVersion = 7;
			minorVersion = 0;
		}
		else if (userAgent.indexOf("MSIE 6.0") != -1)
		{
			browserName = INTERNET_EXPLORER;
			majorVersion = 6;
			minorVersion = 0;
		}
		else if (userAgent.indexOf("MSIE 5.5") != -1)
		{
			browserName = INTERNET_EXPLORER;
			majorVersion = 5;
			minorVersion = 5;
		}
		else if (userAgent.indexOf("MSIE 5.0") != -1)
		{
			browserName = INTERNET_EXPLORER;
			majorVersion = 5;
			minorVersion = 0;
		}
		else
		{
			browserName = UNKNOWN;
			majorVersion = UNKNOWN;
			minorVersion = UNKNOWN;
		}
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public int getBrowserName()
	{
		return browserName;
	}

	public static boolean isInternetExplorer(FacesContext context)
	{
		return new BrowserInfo(context).getBrowserName() == INTERNET_EXPLORER;
	}

	public int getMajorVersion()
	{
		return majorVersion;
	}

	public int getMinorVersion()
	{
		return minorVersion;
	}

}
