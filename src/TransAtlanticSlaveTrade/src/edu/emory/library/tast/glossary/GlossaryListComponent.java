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
package edu.emory.library.tast.glossary;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.KeywordHighlighter;

public class GlossaryListComponent extends UIComponentBase
{
	
	public String getFamily()
	{
		return null;
	}
	
	private String createLetterElementId(FacesContext context, GlossaryListLetter letter)
	{
		return getClientId(context).replace(":", "_") + "_" + letter.getLetter();
	}
	
	private void renderJavaScript(ResponseWriter writer, FacesContext context, String mainId, String frameId, GlossaryList terms) throws IOException
	{
		
		// registration script
		StringBuffer regJS = new StringBuffer();
		regJS.append("GlossaryListGlobals.registerGlossaryList(new GlossaryList(");
		
		// main and frame id
		regJS.append("'").append(mainId).append("', ");
		regJS.append("'").append(frameId).append("', ");
		
		// letter -> element id's
		regJS.append("{");
		int letterIdx = 0;
		for (Iterator letterIt = terms.getLetters().iterator(); letterIt.hasNext();)
		{
			GlossaryListLetter letter = (GlossaryListLetter) letterIt.next();
			if (letterIdx > 0) regJS.append(", ");
			regJS.append("'").append(letter.getLetter()).append("'");
			regJS.append(": ");
			regJS.append("'").append(createLetterElementId(context, letter)).append("'");
			letterIdx++;
		}
		regJS.append("}");
		
		// end of JS
		regJS.append("))");
		
		// render
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);
		
	}

	private void renderHTML(ResponseWriter writer, FacesContext context, String frameId, GlossaryList terms) throws IOException
	{
		
		// if search -> prepare regex for highlighting
		KeywordHighlighter highlighter = new KeywordHighlighter(
				terms.getKeywords(),
				"span", "search-keyword");
		
		// for position: fixed
		writer.startElement("div", this);
		writer.writeAttribute("id", frameId, null);
		writer.writeAttribute("class", "glossary-terms", null);

		// all has to be one big table, unfortunately
		writer.startElement("table", this);
		writer.writeAttribute("class", "glossary-terms", null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);

		// iterate over letters
		for (Iterator letterIt = terms.getLetters().iterator(); letterIt.hasNext();)
		{
			GlossaryListLetter letter = (GlossaryListLetter) letterIt.next();
			
			// start row
			writer.startElement("tr", this);
			
			// start letter cell
			writer.startElement("td", this);
			writer.writeAttribute("id", createLetterElementId(context, letter), null);
			writer.writeAttribute("class", "glossary-letter", null);
			writer.writeAttribute("rowspan", String.valueOf(letter.getMatchesTermsCount()), null);

			// the letter itself
			writer.startElement("div", this);
			writer.writeAttribute("class", "glossary-letter", null);
			writer.write(letter.getLetter());
			writer.endElement("div");

			// end letter cell
			writer.endElement("td");

			// terms
			int termIdx = 0;
			for (Iterator termIt = letter.getTerms().iterator(); termIt.hasNext();)
			{
				GlossaryListTerm term = (GlossaryListTerm) termIt.next();

				// start row
				if (termIdx > 0) writer.startElement("tr", this);

				// term
				writer.startElement("td", this);
				writer.writeAttribute("class", "glossary-term", null);
				writer.write(highlighter.highlight(term.getTerm()));
				writer.endElement("td");
			
				// description
				writer.startElement("td", this);
				writer.writeAttribute("class", "glossary-description", null);
				writer.write(highlighter.highlight(term.getDescription()));
				writer.endElement("td");
				
				// end row
				writer.endElement("tr");
				termIdx++;

			}
		
		}
		
		// end of main table
		writer.endElement("table");
		writer.endElement("div");
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// main parameters
		GlossaryList terms = getTerms();
		String mainId = getClientId(context);
		String frameId = mainId + "_frame";  

		// and now the main methods
		renderJavaScript(writer, context, mainId, frameId, terms);
		renderHTML(writer, context, frameId, terms);

	}
	
	public GlossaryList getTerms()
	{
		return (GlossaryList) JsfUtils.getCompPropObject(this, getFacesContext(),
				"terms", false, null);
	}

}
