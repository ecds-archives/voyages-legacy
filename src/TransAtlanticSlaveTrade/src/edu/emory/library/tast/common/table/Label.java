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
package edu.emory.library.tast.common.table;


public class Label
{
	
	private String text;
	private Label[] breakdown;
	private int leavesCount;
	
	public Label(String text)
	{
		this.text = text;
		this.breakdown = null;
	}

	public Label[] getBreakdown()
	{
		return breakdown;
	}
	
	public void setBreakdown(Label[] breakdown)
	{
		this.breakdown = breakdown;
	}
	
	public boolean hasBreakdown()
	{
		return breakdown != null && breakdown.length != 0;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public int getLeavesCount()
	{
		return leavesCount;
	}

	public int calculateLeaves()
	{
		if (this.breakdown == null || this.breakdown.length == 0)
		{
			leavesCount = 1;
		}
		else
		{
			leavesCount = 0;
			for (int i = 0; i < breakdown.length; i++)
			{
				leavesCount += breakdown[i].calculateLeaves();
			}
		}
		return leavesCount;
	}

}
