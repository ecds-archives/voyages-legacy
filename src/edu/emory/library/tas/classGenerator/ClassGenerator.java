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

import edu.emory.library.tas.SchemaColumn;

public class ClassGenerator {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SAXException {
		if (args.length == 0) {
			throw new RuntimeException("Provide list of xml files!");
		}

		for (int i = 0; i < args.length; i++) {
			String fileName = args[i];
			File file = new File(fileName);
			if (!file.exists() || !file.canRead()) {
				System.out.println("File " + fileName
						+ " does not exist or cannot be red!");
				continue;
			}
			XmlDocument document = null;
			try {
			document = XmlDocument.createXmlDocument(
					new FileInputStream(file), false);
			} catch (Exception e) {
				System.out.println("File: " + fileName);
				e.printStackTrace();
			}
			Node rootNode = document.getLastChild();
			if (rootNode != null) {
				String className = rootNode.getAttributes()
						.getNamedItem("name").getNodeValue();
				BufferedOutputStream oStream = new BufferedOutputStream(
						new FileOutputStream(className + ".tmpClass"));
				List attrNames = new ArrayList();
				List attrTypes = new ArrayList();
				List attrLabels = new ArrayList();
				NodeList children = rootNode.getChildNodes();
				StringBuffer types = new StringBuffer();
				oStream.write("/** Static construction **/\n".getBytes());
				for (int j = 0; j < children.getLength(); j++) {
					Node child = children.item(j);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						String attrName = child.getAttributes().getNamedItem(
								"name").getNodeValue();
						String attrType = child.getAttributes().getNamedItem(
								"type").getNodeValue();
						String attrLabel = child.getAttributes().getNamedItem(
								"userLabel").getNodeValue();
						attrNames.add(attrName);
						attrTypes.add(attrType);
						String attrImportType = (child.getAttributes()
								.getNamedItem("importType") != null) ? child
								.getAttributes().getNamedItem("importType")
								.getNodeValue() : null;
						String attrImportName = (child.getAttributes()
								.getNamedItem("importName") != null) ? child
								.getAttributes().getNamedItem("importName")
								.getNodeValue() : null;
						String attrImportDateYear = (child.getAttributes()
								.getNamedItem("importDateYear") != null) ? child
								.getAttributes().getNamedItem("importDateYear")
								.getNodeValue()
								: null;
						String attrImportDateMonth = child.getAttributes()
								.getNamedItem("importDateMonth") != null ? child
								.getAttributes()
								.getNamedItem("importDateMonth").getNodeValue()
								: null;
						String attrImportDateDay = child.getAttributes()
								.getNamedItem("importDateDay") != null ? child
								.getAttributes().getNamedItem("importDateDay")
								.getNodeValue() : null;
								
						Integer length = child.getAttributes()
								.getNamedItem("length") != null ? new Integer(child
								.getAttributes().getNamedItem("length")
								.getNodeValue()) : null;

						types.append("types.put(\"");
						types.append(attrName);
						types.append("\", new SchemaColumn(");

						int type = -1;
						String dict = null;
						if ("String".equals(attrType)) {
							type = SchemaColumn.TYPE_STRING;
						} else if ("Integer".equals(attrType)) {
							type = SchemaColumn.TYPE_INTEGER;
						} else if ("Long".equals(attrType)) {
							type = SchemaColumn.TYPE_LONG;
						} else if ("Float".equals(attrType)) {
							type = SchemaColumn.TYPE_FLOAT;
						} else if ("Date".equals(attrType)) {
							type = SchemaColumn.TYPE_DATE;
						} else {
							type = SchemaColumn.TYPE_DICT;
							dict = attrType;
						}
						int importType = SchemaColumn.IMPORT_TYPE_NUMERIC;
						if ("numeric".equals(attrImportType) || attrImportType == null) {
							importType = SchemaColumn.IMPORT_TYPE_NUMERIC;
						} else if ("ignore".equals(attrImportType)) {
							importType = SchemaColumn.IMPORT_TYPE_IGNORE;
						} else if ("date".equals(attrImportType)) {
							importType = SchemaColumn.IMPORT_TYPE_DATE;
						} else if ("string".equals(attrImportType)) {
							importType = SchemaColumn.IMPORT_TYPE_STRING;
						} else {
							throw new RuntimeException("Wrong switch: "
									+ attrImportType);
						}

						types.append("\"").append(attrName).append("\", ");
						types.append(type).append(", ");
						if (dict == null) {
							types.append("null, ");
						} else {
							types.append("\"").append(dict).append("\", ");
						}
						types.append(importType).append(", ");
						if (attrImportName != null) {
							types.append("\"").append(attrImportName).append(
									"\", ");
						} else {
							types.append("null, ");
						}
						if (attrImportDateDay != null) {
							types.append("\"").append(attrImportDateDay)
									.append("\", ");
						} else {
							types.append("null, ");
						}
						if (attrImportDateMonth != null) {
							types.append("\"").append(attrImportDateMonth)
									.append("\", ");
						} else {
							types.append("null, ");
						}
						if (attrImportDateYear != null) {
							types.append("\"").append(attrImportDateYear).append(
									"\", ");
						} else {
							types.append("null, ");
						}
						if (attrLabel == null || attrLabel.equals("")) {
							types.append("null, ");
						} else {
							types.append("\"").append(attrLabel).append("\", ");
						}
						if (length == null) {
							types.append("-1));\n");
						} else {
							types.append(length.intValue()).append("));\n");
						}
					}
				}

				oStream.write(types.toString().getBytes());

				oStream.write("\n\n\n/** Getters/setters **/\n".getBytes());
				for (int j = 0; j < attrNames.size(); j++) {
					StringBuffer buffer = new StringBuffer();
					String name = (String) attrNames.get(j);
					buffer.append("public void set").append(
							name.substring(0, 1).toUpperCase());
					buffer.append(name.substring(1)).append("(").append(
							attrTypes.get(j));
					buffer.append(" ").append(name).append(") {\n\t");
					buffer.append("if ((").append(name).append(
							" == null && this.values.get(\"").append(name)
							.append("\") != null");
					buffer.append(") \n\t\t|| (").append(name).append(
							" != null && !").append(name).append(
							".equals(this.values.get(\"").append(name).append(
							"\"))))");
					buffer.append(" {\n\t\tthis.modified = UPDATED;\n\t}");
					buffer.append("\n\tthis.values.put(\"");
					buffer.append(name).append("\", ").append(name).append(
							");\n}\n");
					oStream.write(buffer.toString().getBytes());
				}
				for (int j = 0; j < attrNames.size(); j++) {
					StringBuffer buffer = new StringBuffer();
					String name = (String) attrNames.get(j);
					buffer.append("public ").append(attrTypes.get(j)).append(
							" get");
					buffer.append(name.substring(0, 1).toUpperCase());
					buffer.append(name.substring(1)).append("() {\n\treturn (")
							.append(attrTypes.get(j));
					buffer.append(")this.values.get(\"").append(name).append(
							"\");\n}\n");

					oStream.write(buffer.toString().getBytes());
				}

				oStream.flush();
				oStream.close();
			}
		}

	}
}
