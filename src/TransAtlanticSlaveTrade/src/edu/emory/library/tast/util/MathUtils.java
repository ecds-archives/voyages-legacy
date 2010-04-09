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

public class MathUtils
{
	
	public static int roundedLog10(int n)
	{
		int j = 0;
		int p = 1;
		while (p < n)
		{
			p *= 10;
			j++;
		}
		return j;
	}
	
	public static double firstGreaterOrEqualPow10(double x)
	{
		double j = 0;
		double p = 1;
		if (x > 1)
		{
			while (p < x)
			{
				p *= 10;
				j++;
			}
		}
		else if (x < 1)
		{
			while (x <= p)
			{
				p /= 10;
				j++;
			}
			p *= 10;
		}
		return p;
	}

	public static int firstGreaterPow10(int n)
	{
		int j = 0;
		int p = 1;
		while (p <= n)
		{
			p *= 10;
			j++;
		}
		return p;
	}

}
