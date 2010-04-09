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
package edu.emory.library.tast.database.query;

public class TestBean
{
	
	private int a;
	private int b;
	private int c;
	
	public String add()
	{
		c = a + b;
		return null;
	}
	
	public int getA()
	{
		return a;
	}
	
	public void setA(int a)
	{
		this.a = a;
	}
	
	public int getB()
	{
		return b;
	}
	
	public void setB(int b)
	{
		this.b = b;
	}
	
	public int getC()
	{
		return c;
	}
	
	public void setC(int c)
	{
		this.c = c;
	}
	
	public boolean getIsResultBig()
	{
		return c > 100;
	}

}
