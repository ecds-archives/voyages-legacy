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

import java.util.Arrays;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.attributes.Attribute;


public class TableBuilderBreakdown extends TableBuilder
{
	
	public static final int BREAKDOWN_ROWS = 1;
	public static final int BREAKDOWN_COLUMNS = 2;
	
	private int breakdownType = BREAKDOWN_ROWS;
	private Attribute attribute;

	public TableBuilderBreakdown(String colLabel, int breakdownType, Attribute attribute)
	{
		super(colLabel);
		this.breakdownType = breakdownType;
		this.attribute = attribute;
	}
	
	public Attribute[] getAttributes()
	{
		return new Attribute[] {attribute};
	}

	public int getAttributeCount()
	{
		return 1;
	}
	
	public String getTotalLabel()
	{
		return TastResource.getText("database_tableview_averages");
	}
	
	public Table formTable(Object[] data, int dataIdxCol, Grouper rowGrouper, Grouper colGrouper)
	{
		
		int rowCount = rowGrouper.getLeafLabelsCount();
		int colCount = colGrouper.getLeafLabelsCount();

		Table table = new Table(rowCount, colCount);

		long[][] counts = new long[rowCount][colCount];
		long totalCount = 0;
		boolean[][] used = new boolean[rowCount][colCount];

		long rowTotals[] = new long[rowCount];
		long colTotals[] = new long[colCount];
		
		boolean[] rowUsed = new boolean[rowCount];
		boolean[] colUsed = new boolean[colCount];
		boolean someValue = false;
		
		for (int i = 0; i < rowCount; i++)
		{
			Arrays.fill(counts[i], 0);
			Arrays.fill(used[i], false);
		}
		
		Arrays.fill(rowTotals, 0);
		Arrays.fill(colTotals, 0);

		Arrays.fill(rowUsed, false);
		Arrays.fill(colUsed, false);

		for (int k = 0; k < data.length; k++)
		{
			Object[] dataRow = (Object[]) data[k];

			int rowIdx = rowGrouper.lookupIndex(dataRow);
			int colIdx = colGrouper.lookupIndex(dataRow);

			Number valObj = (Number) dataRow[dataIdxCol];
			
			if (valObj != null)
			{

				long val = valObj.longValue();
				
				someValue = true;
				rowUsed[rowIdx] = true;
				colUsed[colIdx] = true;
				used[rowIdx][colIdx] = true;
				
				rowTotals[rowIdx] += val;
				colTotals[colIdx] += val;
				totalCount += val;
				
				counts[rowIdx][colIdx] = val;
				
			}
			
		}
		
		if (breakdownType == BREAKDOWN_COLUMNS)
		{
			
			for (int j = 0; j < colCount; j++)
			{
				if (colUsed[j])
				{
					for (int i = 0; i < rowCount; i++)
					{
						if (used[i][j])
						{
							table.setValue(i, j, (double)counts[i][j] / (double)colTotals[j] * 100);
						}
					}
					table.setLastRowValue(j, 100);
				}
			}

			for (int i = 0; i < rowCount; i++)
			{
				if (rowUsed[i])
				{
					table.setLastColValue(i, (double)rowTotals[i] / (double)totalCount * 100);
				}
			}
			
		}
		else
		{
			
			for (int i = 0; i < rowCount; i++)
			{
				if (rowUsed[i])
				{
					for (int j = 0; j < colCount; j++)
					{
						if (used[i][j])
						{
							table.setValue(i, j, (double)counts[i][j] / (double)rowTotals[i] * 100);
						}
					}
					table.setLastColValue(i, 100);
				}
			}

			for (int j = 0; j < colCount; j++)
			{
				if (colUsed[j])
				{
					table.setLastRowValue(j, (double)colTotals[j] / (double)totalCount * 100);
				}
			}
			
		}

		if (someValue)
		{
			table.setBottomLeft(100);
		}
		
		return table;

	}

}