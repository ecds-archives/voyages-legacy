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
