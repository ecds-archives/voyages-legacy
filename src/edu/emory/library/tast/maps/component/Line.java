package edu.emory.library.tast.maps.component;

public class Line
{
	
	private double x1;
	private double y1;
	private double x2;
	private double y2;
	private String symbol;
	private int symbolSpacing;
	
	public Line()
	{
	}
	
	public Line(double x1, double y1, double x2, double y2)
	{
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public Line(double x1, double y1, double x2, double y2, String symbol,
			int symbolSpacing)
	{
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.symbol = symbol;
		this.symbolSpacing = symbolSpacing;
	}

	public double getX1()
	{
		return x1;
	}
	public void setX1(double x1)
	{
		this.x1 = x1;
	}
	public double getY1()
	{
		return y1;
	}
	public void setY1(double y1)
	{
		this.y1 = y1;
	}
	public double getX2()
	{
		return x2;
	}
	public void setX2(double x2)
	{
		this.x2 = x2;
	}
	public double getY2()
	{
		return y2;
	}
	public void setY2(double y2)
	{
		this.y2 = y2;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public int getSymbolSpacing()
	{
		return symbolSpacing;
	}

	public void setSymbolSpacing(int symbolSpacing)
	{
		this.symbolSpacing = symbolSpacing;
	}

}
