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