package edu.emory.library.tast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

public class Languages
{
	
	private static final String LANGUAGES_XML = "/languages.xml";

	private static boolean loaded = false;
	private static Map allByCodes = null;
	private static Language[] allSorted = null;
	private static Language[] activeSorted = null;
	
	private synchronized static void ensureLoaded()
	{
		
		// already loaded
		if (loaded) return;
		
		// load main document
		try
		{
		
			// we have a DTD and we want to get rid of whitespace
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(true);
			factory.setIgnoringElementContentWhitespace(true);

			// load main document
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream str = Languages.class.getResourceAsStream(LANGUAGES_XML);
			Document document = builder.parse(str);
			
			// all <lang>'s
			NodeList xmlLangs = document.getDocumentElement().getChildNodes();
			allSorted = new Language[xmlLangs.getLength()];
			allByCodes = new HashMap();
			
			// main loop over <langs>
			int activeCount = 0;
			for (int i = 0; i < xmlLangs.getLength(); i++)
			{
				
				Node xmlLang = xmlLangs.item(i);
				NamedNodeMap xmlLangAttrs = xmlLang.getAttributes();
				
				String code = xmlLangAttrs.getNamedItem("code").getNodeValue();
				String name = xmlLangAttrs.getNamedItem("name").getNodeValue();
				Node nodeActive = xmlLangAttrs.getNamedItem("active");
				boolean active = nodeActive != null && "true".equals(nodeActive.getNodeValue());
				
				Language lang = new Language(code, name, active);
			
				allSorted[i] = lang;
				allByCodes.put(lang.getCode(), lang);
				
				if (lang.isActive())
					activeCount++;
				
			}
			
			// sort
			Arrays.sort(allSorted, new LanguageComparator());
			
			// and create the active list
			activeSorted = new Language[activeCount];
			for (int i = 0, j = 0; i < allSorted.length; i++)
			{
				Language lang = allSorted[i];
				if (lang.isActive()) activeSorted[j++] = lang; 
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
		
	}
	
	public static Language getByCode(String code)
	{
		ensureLoaded();
		return (Language) allByCodes.get(code);
	}
	
	public static String getNameByCode(String code)
	{
		ensureLoaded();
		Language lang = Languages.getByCode(code); 
		return lang != null ? lang.getName() : null;
	}

	public static boolean isActive(String code)
	{
		ensureLoaded();
		Language lang = (Language) allByCodes.get(code);
		return lang!= null && lang.isActive();
	}

	public static Language[] getAll()
	{
		ensureLoaded();
		return allSorted;
	}

	public static Language[] getActive()
	{
		ensureLoaded();
		return activeSorted;
	}
	
//	public static void main(String[] args)
//	{
//		
//		Language langs[] = Languages.getActive();
//		
//		for (int i = 0; i < langs.length; i++)
//		{
//			Language lang = langs[i];
//			System.out.println(
//					lang.getName() + " (" + lang.getCode() + ") - " +
//					(lang.isActive() ? "OK" : "NA"));
//		}
//		
//	}

}
