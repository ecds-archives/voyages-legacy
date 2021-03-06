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
package edu.emory.library.tast.maps.component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.emory.library.tast.Languages;

public class Symbol
{

	private static Map lookupByName = null;
	private static final String SYMBOLS_XML = "/symbols.xml";
	
	private String name;
	private String url;
	private int width;
	private int height;
	private int centerX;
	private int centerY;
	
	public Symbol(String name, String url, int width, int height, int centerX, int centerY)
	{
		this.name = name;
		this.url = url;
		this.width = width;
		this.height = height;
		this.centerX = centerX;
		this.centerY = centerY;
	}

	public Symbol(String name, String url, int width, int height)
	{
		this.name = name;
		this.url = url;
		this.width = width;
		this.height = height;
		this.centerX = width / 2;
		this.centerY = height / 2;
	}

	public int getCenterX()
	{
		return centerX;
	}
	
	public void setCenterX(int centerX)
	{
		this.centerX = centerX;
	}
	
	public int getCenterY()
	{
		return centerY;
	}

	public void setCenterY(int centerY)
	{
		this.centerY = centerY;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public static Symbol get(String name)
	{
		ensureLoaded();
		
		Symbol symbol = (Symbol) lookupByName.get(name);
		if (symbol == null)
			throw new RuntimeException("symbol '" + name + "' not found");
		
		return symbol;
	}

	private static void ensureLoaded()
	{
		
		if (lookupByName != null)
			return;
			
		Map lookupByNameLocal = new HashMap();
		
		try
		{
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			factory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream str = Languages.class.getResourceAsStream(SYMBOLS_XML);
			Document document = builder.parse(str);
			
			NodeList xmlSymbols = document.getDocumentElement().getChildNodes();
			
			for (int i = 0; i < xmlSymbols.getLength(); i++)
			{
				
				Node xmlLang = xmlSymbols.item(i);
				NamedNodeMap xmlSymbolAttrs = xmlLang.getAttributes();
				
				String name = xmlSymbolAttrs.getNamedItem("name").getNodeValue();
				String url = xmlSymbolAttrs.getNamedItem("url").getNodeValue();
				int width = Integer.parseInt(xmlSymbolAttrs.getNamedItem("width").getNodeValue());
				int height = Integer.parseInt(xmlSymbolAttrs.getNamedItem("height").getNodeValue());
				
				Node centerXNode = xmlSymbolAttrs.getNamedItem("centerX");
				Node centerYNode = xmlSymbolAttrs.getNamedItem("centerY");
				
				int centerX = 0;
				if (centerXNode != null)
				{
					centerX = Integer.parseInt(centerXNode.getNodeValue());
				}
				else
				{
					centerX = width / 2;
				}
				
				int centerY = 0;
				if (centerYNode != null)
				{
					centerY = Integer.parseInt(centerYNode.getNodeValue());
				}
				else
				{
					centerY = width / 2;
				}

				Symbol symbol = new Symbol(name, url, width, height, centerX, centerY);
				lookupByNameLocal.put(name, symbol);
				
			}

		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException(e);
		}
		catch (SAXException e)
		{
			throw new RuntimeException(e);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		catch (NumberFormatException e)
		{
			throw new RuntimeException(e);
		}

		lookupByName = lookupByNameLocal;

	}

}
