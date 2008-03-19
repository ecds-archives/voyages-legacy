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
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		
		// get terms from bean
		GlossaryList terms = getTerms();
		
		// if search -> prepare regex for highlighting
		KeywordHighlighter highlighter = new KeywordHighlighter(
				terms.getKeywords(),
				"span", "search-keyword");
		
		for (Iterator letterIt = terms.getLetters().iterator(); letterIt.hasNext();)
		{
			GlossaryListLetter letter = (GlossaryListLetter) letterIt.next();
			
			// anchor
			writer.startElement("a", this);
			writer.writeAttribute("name", String.valueOf(letter.getLetter()), null);
			writer.endElement("a");

			// container for table for all terms corresponding to one letter
			writer.startElement("div", this);
			writer.writeAttribute("class", "glossary-terms", null);

			// table for all terms corresponding to one letter
			writer.startElement("table", this);
			writer.writeAttribute("class", "glossary-terms", null);
			writer.writeAttribute("border", "0", null);
			writer.writeAttribute("cellspacing", "0", null);
			writer.writeAttribute("cellpadding", "0", null);
			writer.startElement("tr", this);
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "glossary-letter-info", null);

			// the letter itself
			writer.startElement("div", this);
			writer.writeAttribute("class", "glossary-letter", null);
			writer.write(letter.getLetter());
			writer.endElement("div");

			// total number of terms / matched number of terms
			writer.startElement("div", this);
			writer.writeAttribute("class", "glossary-counts", null);
			if (terms.isListingAll())
			{
				writer.write(String.valueOf(letter.getTotalTermsCount()));
			}
			else
			{
				writer.write(String.valueOf(letter.getMatchesTermsCount()));
				writer.write("&nbsp;/&nbsp;");
				writer.write(String.valueOf(letter.getTotalTermsCount()));
			}
			writer.endElement("div");

			writer.endElement("td");

			// td containing all terms
			writer.startElement("td", this);
			writer.writeAttribute("class", "glossary-terms", null);

			// terms
			for (Iterator termIt = letter.getTerms().iterator(); termIt.hasNext();)
			{
				GlossaryListTerm term = (GlossaryListTerm) termIt.next();

				// term
				writer.startElement("div", this);
				writer.writeAttribute("class", "glossary-term", null);
				writer.write(highlighter.highlight(term.getTerm()));
				writer.endElement("div");
			
				// description
				writer.startElement("div", this);
				writer.writeAttribute("class", "glossary-description", null);
				writer.write(highlighter.highlight(term.getDescription()));
				writer.endElement("div");

			}
			
			// end of td containing all terms of one letter
			writer.endElement("td");

			// end of table for all terms corresponding of one letter
			writer.endElement("tr");
			writer.endElement("table");
			writer.endElement("div");
		
		}
		
	}

	public GlossaryList getTerms()
	{
		return (GlossaryList) JsfUtils.getCompPropObject(this, getFacesContext(),
				"terms", false, null);
	}

}
