package edu.emory.library.tast.ui.search.query.searchables;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigLoader
{
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(args[0]));
		
		NodeList xmlSearchableAttrs = document.getChildNodes();
		SearchableAttribute[] searchableAttrs = new SearchableAttribute[xmlSearchableAttrs.getLength()]; 
		
		for (int i = 0; i < xmlSearchableAttrs.getLength(); i++)
		{
			Node xmlSearchableAttr = xmlSearchableAttrs.item(i);
			String type = xmlSearchableAttr.getAttributes().getNamedItem("type").getNodeValue();
			SearchableAttribute searchableAttribute = null;
			if ("simple".equals(type))
			{
			}
			else if ("location".equals(type))
			{
			}
			if (searchableAttribute != null) searchableAttrs[i] = searchableAttribute;
		}
		
	}

}
