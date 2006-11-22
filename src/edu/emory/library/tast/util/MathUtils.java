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
	
	public static int firstGreaterOrEqualPow10(int n)
	{
		int j = 0;
		int p = 1;
		while (p < n)
		{
			p *= 10;
			j++;
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
