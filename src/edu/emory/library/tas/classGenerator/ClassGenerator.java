package edu.emory.library.tas.classGenerator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.crimson.tree.XmlDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClassGenerator {

	public static void main(String[] args) throws FileNotFoundException, IOException, SAXException {
		if (args.length == 0) {
			throw new RuntimeException("Provide list of xml files!");
		}
		
		for (int i = 0; i < args.length; i++) {
			String fileName = args[i];
			File file = new File(fileName);
			if (!file.exists() || !file.canRead()) {
				System.out.println("File " + fileName + " does not exist or cannot be red!");
				continue;
			}

			XmlDocument document = XmlDocument.createXmlDocument(new FileInputStream(file), false);
			Node rootNode = document.getLastChild();
			if (rootNode != null) {
				String className = rootNode.getAttributes().getNamedItem("name").getNodeValue();
				BufferedOutputStream oStream = 
					new BufferedOutputStream(new FileOutputStream(className + ".tmpClass"));
				List attrNames = new ArrayList();
				List attrTypes = new ArrayList();
				List attrLabels = new ArrayList();
				NodeList children = rootNode.getChildNodes();
				for (int j = 0; j < children.getLength(); j++) {
					Node child = children.item(j);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
					String attrName = child.getAttributes().getNamedItem("name").getNodeValue();
					String attrType = child.getAttributes().getNamedItem("type").getNodeValue();
					String attrLabel = child.getAttributes().getNamedItem("userLabel").getNodeValue();
					attrNames.add(attrName);
					attrTypes.add(attrType);
					attrLabels.add(attrLabel);
					}
				}
				
				oStream.write("/** Static construction **/\n".getBytes());
				StringBuffer types = new StringBuffer();
				StringBuffer userLabels = new StringBuffer();
				for (int j = 0; j < attrNames.size(); j++) {
					types.append("types.put(\"");
					types.append(attrNames.get(j));
					types.append("\", \"");
					types.append(attrTypes.get(j));
					types.append("\");\n");
					userLabels.append("userLabels.put(\"");
					userLabels.append(attrNames.get(j));
					userLabels.append("\", \"");
					userLabels.append(attrLabels.get(j));
					userLabels.append("\");\n");
				}
				oStream.write(types.toString().getBytes());
				oStream.write(userLabels.toString().getBytes());
				
				
				oStream.write("\n\n\n/** Getters/setters **/\n".getBytes());
				for (int j = 0; j < attrNames.size(); j++) {
					StringBuffer buffer = new StringBuffer();
					String name = (String)attrNames.get(j);
					buffer.append("public void set").append(name.substring(0, 1).toUpperCase());
					buffer.append(name.substring(1)).append("(").append(attrTypes.get(j));
					buffer.append(" ").append(name).append(") {\n\t");
					buffer.append("if ((").append(name).append(" == null && this.values.get(\"").append(name).append("\") != null");
					buffer.append(") \n\t\t|| (").append(name).append(" != null && !").append(name).append(".equals(this.values.get(\"").append(name).append("\"))))");
					buffer.append(" {\n\t\tthis.modified = UPDATED;\n\t}");
					buffer.append("\n\tthis.values.put(\"");
					buffer.append(name).append("\", ").append(name).append(");\n}\n");
					oStream.write(buffer.toString().getBytes());
				}
				for (int j = 0; j < attrNames.size(); j++) {
					StringBuffer buffer = new StringBuffer();
					String name = (String)attrNames.get(j);
					buffer.append("public ").append(attrTypes.get(j)).append(" get");
					buffer.append(name.substring(0, 1).toUpperCase());
					buffer.append(name.substring(1)).append("() {\n\treturn (").append(attrTypes.get(j));
					buffer.append(")this.values.get(\"").append(name).append("\");\n}\n");
					
					oStream.write(buffer.toString().getBytes());
				}
								
				oStream.flush();
				oStream.close();
			}
		}
		
	}

}
