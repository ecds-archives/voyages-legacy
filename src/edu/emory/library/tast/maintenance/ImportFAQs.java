package edu.emory.library.tast.maintenance;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
		conn.setAutoCommit(false);
		
		Statement stTruncateFaqs = conn.createStatement();
		stTruncateFaqs.execute("DELETE FROM faqs");

		Statement stTruncateCategories = conn.createStatement();
		stTruncateCategories.execute("DELETE FROM faqs_categories");
	
		PreparedStatement stInsertCategory = conn.prepareStatement("INSERT INTO faqs_categories (id, name) VALUES (?, ?)");		
		PreparedStatement stInsertFaq = conn.prepareStatement("INSERT INTO faqs (id, cat_id, question, answer) VALUES (?, ?, ?, ?)");		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new File(FAQ_HTML));
		
		Node body = document.getElementsByTagName("body").item(0);
		
		String categoryName = null;
		String question = null;
		StringBuffer answer = new StringBuffer();
		Node node = body.getFirstChild();
		
		long cat_id = 0;
		long faq_id = 0;
		
		int maxCategoryLen = 0;
		int maxQuestionLen = 0;
		int maxAnswerLen = 0;
		
		while (node != null)
		{
			node = XMLUtils.findSibling(node, "h1");

			if (node != null)
			{
				categoryName = XMLUtils.nodeToString(node, true);
				node = node.getNextSibling();
				
				if (maxCategoryLen < categoryName.length())
					maxCategoryLen = categoryName.length();
				
				stInsertCategory.setLong(1, ++cat_id);
				stInsertCategory.setString(2, categoryName);
				stInsertCategory.execute();
				
				while (!(node == null ||
						(node.getNodeType() == Node.ELEMENT_NODE &&
						(node.getNodeName().equals("h1")))))
				{
					node = XMLUtils.findSibling(node, "h2");
					
					if (node != null)
					{
						question = XMLUtils.nodeToString(node, true);
						node = node.getNextSibling();
						
						answer.setLength(0);
						
						while (!(node == null ||
								(node.getNodeType() == Node.ELEMENT_NODE &&
								(node.getNodeName().equals("h1") || node.getNodeName().equals("h2")))))
						{
							answer.append(XMLUtils.nodeToString(node));
							node = node.getNextSibling();
						}
						
						System.out.println("Q: " + question);
						
						if (maxQuestionLen < question.length())
							maxQuestionLen = question.length();
						
						if (maxAnswerLen < answer.length())
							maxAnswerLen = answer.length();

						stInsertFaq.setLong(1, ++faq_id);
						stInsertFaq.setLong(2, cat_id);
						stInsertFaq.setString(3, question);
						stInsertFaq.setString(4, answer.toString().trim());
						stInsertFaq.execute();
						
					}
					
				}
			
			}
		
		}
		
		conn.commit();
		
		System.out.println("max category len = " + maxCategoryLen);
		System.out.println("max question len = " + maxQuestionLen);
		System.out.println("max answer len   = " + maxAnswerLen);
		
	}

}
