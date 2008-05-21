package edu.emory.library.tast.faq;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.KeywordHighlighter;

public class FAQListComponent extends UIComponentBase
{

	public String getFamily()
	{
		return null;
	}
	
	private String createCategoryUrlFragment(int catIdx)
	{
		return "faq" + catIdx;
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		
		FAQList faqList = getFAQList();
		
		KeywordHighlighter highlighter = new KeywordHighlighter(
				faqList.getKeywords(),
				"span", "search-keyword");

		writer.startElement("div", this);
		writer.writeAttribute("class", "faq-list", null);
		
		writer.startElement("ul", this);
		writer.writeAttribute("class", "faq-toc", null);

		int catIdx = 0;
		for (Iterator catIt = faqList.getCategories().iterator(); catIt.hasNext(); catIdx++)
		{
			FAQListCategory cat = (FAQListCategory) catIt.next();
			
			writer.startElement("li", this);
			writer.writeAttribute("class", "faq-category", null);
			writer.startElement("a", this);
			writer.writeAttribute("href", "#" + createCategoryUrlFragment(catIdx), null);
			writer.write(cat.getName());
			writer.endElement("a");
			writer.endElement("li");

		}

		writer.endElement("ul");

		writer.startElement("div", this);
		writer.writeAttribute("class", "faq-main", null);

		catIdx = 0;
		for (Iterator catIt = faqList.getCategories().iterator(); catIt.hasNext(); catIdx++)
		{
			FAQListCategory cat = (FAQListCategory) catIt.next();
			
			writer.startElement("a", this);
			writer.writeAttribute("name", createCategoryUrlFragment(catIdx), null);
			writer.endElement("a");

			writer.startElement("div", this);
			writer.writeAttribute("class", "faq-category", null);
			writer.write(cat.getName());
			writer.endElement("div");
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "faq", null);
			
			for (Iterator faqIt = cat.getQuestions().iterator(); faqIt.hasNext();)
			{
				FAQListQuestion faq = (FAQListQuestion) faqIt.next();
				
				writer.startElement("div", this);
				writer.writeAttribute("class", "faq-question", null);
				writer.write(highlighter.highlight(faq.getQuestion()));
				writer.endElement("div");
				
				writer.startElement("div", this);
				writer.writeAttribute("class", "faq-answer", null);
				writer.write(highlighter.highlight(faq.getAnswer()));
				writer.endElement("div");

			}
			
			writer.endElement("div");

		}
		
		writer.endElement("div");

		writer.endElement("div");

	}
	
	public FAQList getFAQList()
	{
		return (FAQList) JsfUtils.getCompPropObject(this, getFacesContext(),
				"faqList", false, null);
	}

}
