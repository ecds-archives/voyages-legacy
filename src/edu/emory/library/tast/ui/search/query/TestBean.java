package edu.emory.library.tast.ui.search.query;

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
