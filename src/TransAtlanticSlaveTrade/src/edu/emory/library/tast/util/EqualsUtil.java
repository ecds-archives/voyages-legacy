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

public class EqualsUtil
{

	static public boolean areEqual(boolean a, boolean b)
	{
		return a == b;
	}

	static public boolean areEqual(char a, char b)
	{
		return a == b;
	}

	static public boolean areEqual(byte a, byte b)
	{
		return a == b;
	}

	static public boolean areEqual(int a, int b)
	{
		return a == b;
	}
	
	static public boolean areEqual(long a, long b)
	{
		return a == b;
	}

	static public boolean areEqual(float a, float b)
	{
		return Float.floatToIntBits(a) == Float.floatToIntBits(b);
	}

	static public boolean areEqual(double a, double b)
	{
		return Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
	}

	static public boolean areEqual(Object a, Object b)
	{
		return a == null ? b == null : a.equals(b);
	}

	static public boolean areEqual(String[] a, String[] b)
	{
		
		if (a == null && b == null)
			return true;

		if (a == null || b == null)
			return false;
		
		if (a.length != b.length)
			return false;
		
		for (int i = 0; i < a.length; i++)
			if (!EqualsUtil.areEqual(a[i], b[i]))
				return false;
		
		return true;

	}

}