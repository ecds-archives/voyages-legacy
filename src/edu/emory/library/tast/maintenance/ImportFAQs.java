package edu.emory.library.tast.maintenance;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import edu.emory.library.tast.util.XMLUtils;

public class ImportFAQs
{
	
	private static final String FAQ_HTML = "F:\\Emory\\TAST\\data\\faq.xhtml";
	
	private static final String DB_CONN_STRING = "jdbc:postgresql://localhost/tast";
	private static final String DB_USER = "tast";
	private static final String DB_PASS = "tast";

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, SQLException, TransformerException
	{
		
		Class.forName("org.postgresql.Driver");
		
		Connection conn = DriverManager.getConnection(DB_CONN_STRING, DB_USER, DB_PASS);
		Statement st = conn.createStatement();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//factory.setValidating(true);
		//factory.setIgnoringElementContentWhitespace(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(FAQ_HTML));
		
		Node body = document.getElementsByTagName("body").item(0);
		
		String categoryName = null;
		String question = null;
		StringBuffer answer = new StringBuffer();
		Node node = body.getFirstChild();
		
		while (node != null)
		{
			node = XMLUtils.findSibling(node, "h1");

			if (node != null)
			{
				categoryName = XMLUtils.nodeToString(node.getFirstChild());
				node = node.getNextSibling();
			
				while (!(node == null ||
						(node.getNodeType() == Node.ELEMENT_NODE &&
						(node.getNodeName().equals("h1")))))
				{
					node = XMLUtils.findSibling(node, "h2");
					
					if (node != null)
					{
						question = XMLUtils.nodeToString(node.getFirstChild());
						node = node.getNextSibling();
						
						answer.setLength(0);
						
						while (!(node == null ||
								(node.getNodeType() == Node.ELEMENT_NODE &&
								(node.getNodeName().equals("h1") || node.getNodeName().equals("h2")))))
						{
							answer.append(XMLUtils.nodeToString(node));
							node = node.getNextSibling();
						}
						
						System.out.println("C: " + categoryName);
						System.out.println("Q: " + question);
						System.out.println("A: " + answer.toString().trim());
						
					}
					
				}
			
			}
		
		}
		
	}

}
