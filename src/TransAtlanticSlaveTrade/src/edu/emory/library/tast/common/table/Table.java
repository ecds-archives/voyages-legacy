package edu.emory.library.tast.common.table;

public class Table
{
	
	private Double[][] table;
	private Double[] lastRow;
	private Double[] lastCol;
	private Double bottomRight;
	
	public Table(int rowCount, int colCount)
	{
		table = new Double[rowCount][colCount];
		lastRow = new Double[colCount];
		lastCol = new Double[rowCount];
		bottomRight = null;
	}
	
	public Double[][] getTable()
	{
		return table;
	}
	
	public void setTable(Double[][] table)
	{
		this.table = table;
	}
	
	public Double[] getLastRow()
	{
		return lastRow;
	}
	
	public void setLastRow(Double[] lastRow)
	{
		this.lastRow = lastRow;
	}
	
	public Double[] getLastCol()
	{
		return lastCol;
	}
	
	public void setLastCol(Double[] lastCol)
	{
		this.lastCol = lastCol;
	}
	
	public Double getBottomRight()
	{
		return bottomRight;
	}

	public void setBottomLeft(double bottomLeft)
	{
		this.bottomRight = new Double(bottomLeft);
	}
	
	public void setValue(int rowIdx, int colIdx, double value)
	{
		table[rowIdx][colIdx] = new Double(value);
	}

	public void setLastColValue(int rowIdx, double value)
	{
		this.lastCol[rowIdx] = new Double(value);
	}
	
	public void setLastRowValue(int colIdx, double value)
	{
		this.lastRow[colIdx] = new Double(value);
	}

	public void addToLastRow(int colIdx, double value)
	{
		Double prevValue = lastRow[colIdx];
		if (prevValue == null)
		{
			lastRow[colIdx] = new Double(value);
		}
		else
		{
			lastRow[colIdx] = new Double(prevValue.doubleValue() + value);
		}
	}
	
	public void addToLastCol(int rowIdx, double value)
	{
		Double prevValue = lastCol[rowIdx];
		if (prevValue == null)
		{
			lastCol[rowIdx] = new Double(value);
		}
		else
		{
			lastCol[rowIdx] = new Double(prevValue.doubleValue() + value);
		}
	}

	public void addToBottomRight(double value)
	{
		if (bottomRight == null)
		{
			bottomRight = new Double(value);
		}
		else
		{
			bottomRight = new Double(bottomRight.doubleValue() + value);
		}
	}

}
