package edu.emory.library.tast.common.table;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.attributes.Attribute;


public class TableBuilderSimple extends TableBuilder
{
	
	private Attribute attribute;
	
	public TableBuilderSimple(String colLabel, Attribute attribute)
	{
		super(colLabel);
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
		return TastResource.getText("database_tableview_totals");
	}

	public Table formTable(Object[] data, int dataIdxCol, Grouper rowGrouper, Grouper colGrouper)
	{
		
		int rowCount = rowGrouper.getLeafLabelsCount();
		int colCount = colGrouper.getLeafLabelsCount();

		Table table = new Table(rowCount, colCount);
		
		for (int k = 0; k < data.length; k++)
		{
			Object[] dataRow = (Object[]) data[k];

			int rowIdx = rowGrouper.lookupIndex(dataRow);
			int colIdx = colGrouper.lookupIndex(dataRow);

			double val = 0.0; 
				
			Number valObj = (Number) dataRow[dataIdxCol];
			val = valObj == null ? 0.0 : valObj.doubleValue();
			
			table.addToLastRow(colIdx, val);
			table.addToLastCol(rowIdx, val);
			table.addToBottomRight(val);
					
			table.setValue(rowIdx, colIdx, val);

		}
		
		return table;

	}

}