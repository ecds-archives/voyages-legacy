package edu.emory.library.tast.glossary;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class GlossaryLettersComponentV1 extends UIComponentBase
{
	
	private boolean columnsSet = false;
	private int columns = 5;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = new Integer(columns);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		columns = ((Integer) values[1]).intValue();
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from bean
		GlossaryLetter[] letters = getLetters();
		columns = getColumns();
		
		// main table
		writer.startElement("table", this);
		writer.writeAttribute("class", "glossary-selector-letters", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
		
		// render
		for (int i = 0; i < letters.length; i++)
		{
			GlossaryLetter letter = letters[i];
			
			// new row
			if (i % columns == 0)
			{
				if (i != 0)
					writer.endElement("tr");
				if (i != letters.length - 1)
					writer.startElement("tr", this);
			}
			
			// cell itself
			writer.startElement("td", this);
			writer.writeAttribute("class", "glossary-selector-letter", null);
			if (letter.isMatched())
			{
				writer.startElement("a", this);
				writer.writeAttribute("class", "glossary-selector-letter-normal", null);
				writer.writeAttribute("href", "#" + letter.getLetter(), null);
			}
			else
			{
				writer.startElement("div", this);
				writer.writeAttribute("class", "glossary-selector-letter-dimmed", null);
			}
			writer.write(letter.getLetter());
			if (letter.isMatched())
			{
				writer.endElement("a");
			}
			else
			{
				writer.endElement("div");
			}
			writer.endElement("td");
			
		}
		
		// main table
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	public GlossaryLetter[] getLetters()
	{
		return (GlossaryLetter[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"letters", false, null);
	}

	public int getColumns()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"columns", columnsSet, columns);
	}

	public void setColumns(int columns)
	{
		columnsSet = true;
		this.columns = columns;
	}

}
