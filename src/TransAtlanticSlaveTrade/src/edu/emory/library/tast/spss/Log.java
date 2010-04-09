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

import java.util.Date;
import java.util.List;

public class Log
{
	
	private Date timeStart = null;
	private Date timeFinish = null;
	private List items  = null;
	private boolean finishedOK;
	
	public boolean isFinished()
	{
		return timeFinish != null;
	}

	public boolean isFinishedOK()
	{
		return finishedOK;
	}
	
	public void setFinishedOK(boolean finishedOK)
	{
		this.finishedOK = finishedOK;
	}
	
	public List getItems()
	{
		return items;
	}
	
	public void setItems(List items)
	{
		this.items = items;
	}
	
	public Date getTimeFinish()
	{
		return timeFinish;
	}
	
	public void setTimeFinish(Date timeFinish)
	{
		this.timeFinish = timeFinish;
	}
	
	public Date getTimeStart()
	{
		return timeStart;
	}
	
	public void setTimeStart(Date timeStart)
	{
		this.timeStart = timeStart;
	}

}
