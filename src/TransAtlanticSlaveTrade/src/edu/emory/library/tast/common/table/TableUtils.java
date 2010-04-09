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

import edu.emory.library.tast.common.SimpleTableCell;

public class TableUtils
{
	
	public static void addColumnLabel(SimpleTableCell table[][], Label label, int rowIdx, int colIdx, int depth, int maxDepth, int subCols, String cssClassTdLabel)
	{
		
		SimpleTableCell cell = new SimpleTableCell(label.getText());
		cell.setColspan(subCols*label.getLeavesCount());
		cell.setCssClass(cssClassTdLabel);
		if (!label.hasBreakdown()) cell.setRowspan(maxDepth - depth + 1);
		table[rowIdx][colIdx] = cell;
		
		if (label.hasBreakdown())
		{
			int colOffset = 0;
			Label[] breakdown = label.getBreakdown();
			for (int j = 0; j < breakdown.length; j++)
			{
				addColumnLabel(table, breakdown[j], rowIdx + 1, colIdx + colOffset, depth + 1, maxDepth, subCols, cssClassTdLabel);
				colOffset += subCols*breakdown[j].getLeavesCount();
			}
		}

	}
	
	public static void addRowLabel(SimpleTableCell table[][], Label label, int rowIdx, int colIdx, int depth, int maxDepth, String cssClassTdLabel)
	{
		
		SimpleTableCell cell = new SimpleTableCell(label.getText());
		cell.setRowspan(label.getLeavesCount());
		cell.setCssClass(cssClassTdLabel);
		if (!label.hasBreakdown()) cell.setColspan(maxDepth - depth + 1);
		table[rowIdx][colIdx] = cell;
		
		if (label.hasBreakdown())
		{
			int rowOffset = 0;
			Label[] breakdown = label.getBreakdown();
			for (int i = 0; i < breakdown.length; i++)
			{
				addRowLabel(table, breakdown[i], rowIdx + rowOffset, colIdx + 1, depth + 1, maxDepth, cssClassTdLabel);
				rowOffset += breakdown[i].getLeavesCount();
			}
		}

	}
	

}
