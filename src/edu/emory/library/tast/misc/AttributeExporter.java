package edu.emory.library.tast.misc;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.jasper.xmlparser.TreeNode;

import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;

public class AttributeExporter {
	public static void main(String[] args) {
		AttributeExporter exporter = new AttributeExporter();
		TreeNode xmlNode = new TreeNode("attributes");
		xmlNode.addChild(exporter.exportVoyageAttributesToXML());
		xmlNode.addChild(exporter.exportSlaveAttributesToXML());
		
		try {
			FileWriter writer = new FileWriter("Attributes.xml");
			writer.write(xmlNode.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private TreeNode exportSlaveAttributesToXML() {
		Attribute[] attrs = Slave.getAttributes();
		TreeNode node = new TreeNode("objectAttributes");
		node.addAttribute("objectType", "Slave");
		for (int i = 0; i < attrs.length; i++) {
			Attribute attribute = attrs[i];
			node.addChild(attribute.toXML());
		}
		return node;
	}

	private TreeNode exportVoyageAttributesToXML() {
		Attribute[] attrs = Voyage.getAttributes();
		TreeNode node = new TreeNode("objectAttributes");
		node.addAttribute("objectType", "Voyage");
		for (int i = 0; i < attrs.length; i++) {
			Attribute attribute = attrs[i];
			node.addChild(attribute.toXML());
		}
		return node;
	}
}
