package edu.emory.library.tast.glossary;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class GlossaryLettersComponent extends UIComponentBase
{
	
	private boolean glossaryListIdSet = false;
	private String glossaryListId;
	
	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = glossaryListId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		glossaryListId = (String) values[1];
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get data from bean
		GlossaryLetter[] letters = getLetters();
		
		// id of the glossary list (we need this for JS)
		glossaryListId = getGlossaryListId();
		
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
			
			// cell
			writer.startElement("td", this);
			writer.writeAttribute("class", "glossary-selector-letter", null);

			// start div in it
			writer.startElement("div", this);
			if (letter.isMatched())
			{
				String onclick =
					"GlossaryListGlobals.scrollTo(" +
					"'" + glossaryListId + "', " +
					"'" + letter.getLetter() + "')";
				writer.writeAttribute("class", "glossary-selector-letter", null);
				writer.writeAttribute("onclick", onclick, null);
			}
			else
			{
				writer.writeAttribute("class", "glossary-selector-letter-dimmed", null);
			}
			
			// letter
			writer.write(letter.getLetter());
			
			// div and td end
			writer.endElement("div");
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

	public String getGlossaryListId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"glossaryListId", glossaryListIdSet, glossaryListId);
	}

	public void setGlossaryListId(String glossaryListId)
	{
		this.glossaryListIdSet = true;
		this.glossaryListId = glossaryListId;
	}

}
