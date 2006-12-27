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
