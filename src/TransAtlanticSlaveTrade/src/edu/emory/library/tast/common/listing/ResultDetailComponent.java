package edu.emory.library.tast.common.listing;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.common.listing.TableData.ColumnData;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.dm.attributes.Group;

public class ResultDetailComponent extends UIComponentBase
{

	public String getFamily()
	{
		return null;
	}

	public void encodeBegin(FacesContext context) throws IOException
	{

		ResponseWriter writer = context.getResponseWriter();

		//Encode table
		writer.startElement("table", this);
		writer.writeAttribute("class", "detail", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);

		//Get data to fill in table
		TableData data = this.getData();

		//Voyages info
		TableData.DataTableItem[] objs = data.getData();

		//Columns of voyage
		VisibleAttributeInterface[] columns = data.getVisibleAttributes();

		Group[] groups = data.loadAttrGroups();

		List colsCollection = Arrays.asList(columns);
		for (int i = 0; i < groups.length; i++)
		{
			Group group = groups[i];

			writer.startElement("tr", this);
			writer.startElement("td", this);
			writer.writeAttribute("class", "detail-label-main", null);
			writer.writeAttribute("rowspan", String.valueOf(group.getAllVisibleAttributes().length), null);
			writer.write(group.getUserLabel());
			writer.endElement("td");

			for (int k = 0; k < group.getAllVisibleAttributes().length; k++)
			{
				VisibleAttributeInterface attr = group.getAllVisibleAttributes()[k];
				int index = colsCollection.indexOf(attr);

				if (k != 0)
				{
					writer.startElement("tr", this);
				}
				writer.startElement("td", this);
				writer.writeAttribute("class", "detail-label", null);
				writer.write(columns[index].toString());
				writer.endElement("td");
				for (int j = 0; j < objs.length; j++)
				{
					boolean changed = false;
					Object[] row = objs[j].dataRow;
					if (j > 0)
					{
						Object[] oldRow = objs[j - 1].dataRow;
						Object old = oldRow[index];
						Object newO = row[index];
						if (!old.equals(newO))
						{
							changed = true;
						}
					}
					if (j < objs.length - 1)
					{
						Object[] oldRow = objs[j + 1].dataRow;
						Object old = oldRow[index];
						Object newO = row[index];
						if (!old.equals(newO))
						{
							changed = true;
						}
					}

					writer.startElement("td", this);
					if (changed)
					{
						writer.writeAttribute("class", "detail-data-changed", null);
					}
					else
					{
						writer.writeAttribute("class", "detail-data", null);
					}

					if (row[i] != null)
					{
						TableData.ColumnData columnData = (ColumnData) row[index];

						if (columnData != null)
						{
							String[] formatted = columnData.getDataToDisplay();
							String[] rollovers = columnData.getRollovers();

							for (int kk = 0; kk < formatted.length; kk++)
							{
								
								writer.startElement("div", this);
								
								if (formatted[kk] != null)
								{
									writer.write(formatted[kk]);
								}

								if (formatted[kk] != null && rollovers[kk] != null)
								{
									writer.write(": ");
									writer.startElement("span", this);
									writer.writeAttribute("class", "detail-data-rollover", null);
									writer.write(rollovers[kk]);
									writer.endElement("span");
								}

								writer.endElement("div");

							}
						}
					}
					writer.endElement("td");
				}

			}

		}

		writer.endElement("table");

	}

	public TableData getData()
	{
		ValueBinding vb = getValueBinding("data");
		if (vb == null)
			return null;
		return (TableData) vb.getValue(getFacesContext());
	}

}
