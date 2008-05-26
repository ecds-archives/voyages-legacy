package edu.emory.library.tast.common.table;

import java.util.Arrays;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.attributes.Attribute;


public class TableBuilderAverage extends TableBuilder
{
	
	public static final int SUM_DATA_COLUMN_IDX = 0;
	public static final int COUNT_DATA_COLUMN_IDX = 1;
	
	private Attribute sumAttribute;
	private Attribute countAttribute;
	
	public TableBuilderAverage(String colLabel, Attribute sumAttribute, Attribute countAttribute)
	{
		super(colLabel);
		this.sumAttribute = sumAttribute;
		this.countAttribute = countAttribute;
	}

	public int getAttributeCount()
	{
		return 2;
	}

	public Attribute[] getAttributes()
	{
		Attribute[] attrs = new Attribute[2];
		attrs[SUM_DATA_COLUMN_IDX] = sumAttribute;
		attrs[COUNT_DATA_COLUMN_IDX] = countAttribute;
		return attrs;
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
		
		double[] rowTotals = new double[rowCount];
		double[] colTotals = new double[colCount]; 
		double total = 0;

		boolean[] rowUsed = new boolean[rowCount];
		boolean[] colUsed = new boolean[colCount]; 
		boolean totalUsed = false;
		
		long[] rowTotalCounts = new long[rowCount];
		long[] colTotalCounts = new long[colCount];
		long totalCount = 0;
		
		Arrays.fill(rowTotals, 0.0);
		Arrays.fill(colTotals, 0.0);
		
		Arrays.fill(rowUsed, false);
		Arrays.fill(colUsed, false);

		Arrays.fill(rowTotalCounts, 0);
		Arrays.fill(colTotalCounts, 0);

		for (int k = 0; k < data.length; k++)
		{
			Object[] dataRow = (Object[]) data[k];

			int rowIdx = rowGrouper.lookupIndex(dataRow);
			int colIdx = colGrouper.lookupIndex(dataRow);

			Long cntObj = (Long) dataRow[dataIdxCol + COUNT_DATA_COLUMN_IDX];
			Number valObj = (Number) dataRow[dataIdxCol + SUM_DATA_COLUMN_IDX];

			long cnt = cntObj == null ? 0 : cntObj.longValue();
			
			if (cnt != 0 && valObj != null)
			{
				
				double val = valObj.doubleValue();				
				
				rowUsed[rowIdx] = true;
				colUsed[colIdx] = true;
				totalUsed = true;

				rowTotals[rowIdx] += val;
				colTotals[colIdx] += val;
				total += val;
				
				rowTotalCounts[rowIdx] += cnt;
				colTotalCounts[colIdx] += cnt;
				totalCount += cnt;
				
				table.setValue(rowIdx, colIdx, val / cnt);
				
			}

		}
		
		for (int i = 0; i < rowCount; i++)
		{
			if (rowUsed[i])
			{
				table.setLastColValue(i, rowTotals[i] / rowTotalCounts[i]);
			}
		}
		
		for (int j = 0; j < colCount; j++)
		{
			if (colUsed[j])
			{
				table.setLastRowValue(j, colTotals[j] / colTotalCounts[j]);
			}
		}

		if (totalUsed)
		{
			table.setBottomLeft(total / totalCount);
		}
		
		return table;
		
	}

}
