package edu.emory.library.tast.util;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLUtils
{
	
	public static void nodeToString(Node node, StringBuffer buf) throws TransformerException
	{
		
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		
		DOMSource dSource = new DOMSource(node);
		StringWriter sw = new StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(dSource, sr);
		StringWriter anotherSW = (StringWriter) sr.getWriter();
		
		buf.append(anotherSW.getBuffer());
		
	}

	public static String nodeToString(Node node, boolean inner) throws TransformerException
	{
		if (!inner)
		{
			return nodeToString(node);
		}
		else
		{
			StringBuffer buf = new StringBuffer();
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				nodeToString(nodeList.item(i), buf);
			}
			return buf.toString();
		}
	}
	
	public static String nodeToString(Node node) throws TransformerException
	{
		StringBuffer buf = new StringBuffer();
		nodeToString(node, buf);
		return buf.toString();
	}
	
	public static int indexOf(NodeList nodeList, String tagName, int startIdx)
	{
		for (int i = startIdx; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
			{
				if (node.getNodeName().equals(tagName))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public static Node findSibling(Node node, String tagName)
	{
		Node n = node;
		while (n != null && n.getNodeType() != Node.ELEMENT_NODE && !n.getNodeName().equals(tagName))
			n = n.getNextSibling();
		return n;
	}
	
	
}
