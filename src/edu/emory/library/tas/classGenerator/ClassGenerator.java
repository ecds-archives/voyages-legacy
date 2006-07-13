package edu.emory.library.tas.classGenerator;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.crimson.tree.XmlDocument;
import org.hibernate.Session;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.attrGroups.ObjectType;
import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

public class ClassGenerator {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SAXException {
		if (args.length == 0) {
			throw new RuntimeException("Provide list of xml files!");
		}

		boolean shouldSave = "true".equals(System.getProperty("saveTypes"));
		shouldSave = true;
		
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
				ArrayList attributes = new ArrayList();
				String className = rootNode.getAttributes()
						.getNamedItem("name").getNodeValue();
				BufferedOutputStream oStream = new BufferedOutputStream(
						new FileOutputStream(className + ".tmpClass"));
				List attrNames = new ArrayList();
				List attrTypes = new ArrayList();
				//List attrLabels = new ArrayList();
				List columns = new ArrayList();
				NodeList children = rootNode.getChildNodes();
				StringBuffer types = new StringBuffer();
//				oStream.write("/** Static construction **/\n".getBytes());
				for (int j = 0; j < children.getLength(); j++) {
					Node child = children.item(j);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						String attrName = child.getAttributes().getNamedItem(
								"name").getNodeValue();
						String attrType = child.getAttributes().getNamedItem(
								"type").getNodeValue();
						String attrLabel = child.getAttributes().getNamedItem(
								"userLabel").getNodeValue();
						Node node = child.getAttributes().getNamedItem(
								"column");
						String column = null;
						if (node != null) {
							column = node.getNodeValue();
						}
						attrNames.add(attrName);
						attrTypes.add(attrType);
						columns.add(column);
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
						if (shouldSave) {
							attributes.add(doDBUpdate(args, i, attrName, attrLabel, 
									attrImportName, attrImportDateYear, attrImportDateMonth, 
									attrImportDateDay, length, type, dict, importType));
						}
					}
				}
				
				if (shouldSave) {
					cleanDB(attributes, args, i);
				}

//				oStream.write(types.toString().getBytes());

				oStream.write("\n\n\n/** Getters/setters **/\n".getBytes());
				
				prepareSetters(oStream, attrNames, attrTypes);
				prepareGetters(oStream, attrNames, attrTypes);
				
				oStream.flush();
				oStream.close();
				
				generateHibernateXml(className, attrNames, attrTypes, columns);
			}
		}

	}

	private static void generateHibernateXml(String file, List attrNames, List attrTypes, List columns) throws IOException {
		String tableName = file.substring(0, 1).toLowerCase() + file.substring(1, file.length());
		tableName = tableName + "s";		
		BufferedWriter bWrite = new BufferedWriter(new FileWriter(file + ".hbm.xml"));
		bWrite.write("<?xml version=\"1.0\"?>\n");
		bWrite.write("<!DOCTYPE hibernate-mapping PUBLIC\n");
		bWrite.write("		\"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"\n");
		bWrite.write("		\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n");
		bWrite.write("<hibernate-mapping>\n");
		bWrite.write("	<class name=\"edu.emory.library.tas." + file + "\" table=\"" + tableName + "\">\n");
		bWrite.write("		<id name=\"iid\" column=\"iid\">\n");
		bWrite.write("			<generator class=\"native\"/>\n");
		bWrite.write("		</id>\n");
				    		        		        			      		        
		/** Generate hbm.xml file **/
		for (int i = 0; i < attrNames.size(); i++) {
			String attrName = (String)attrNames.get(i);
			String attrType = (String)attrTypes.get(i);
			String colName = (String)columns.get(i);
			if (colName == null || colName.equals("")) {
				colName = attrName;
			}
			if ("String".equals(attrType)
					|| "Float".equals(attrType)
					|| "Integer".equals(attrType)
					|| "Long".equals(attrType)
					|| "Date".equals(attrType)) {
				bWrite.write("		<property name=\"" + attrName + "\" column=\"" + colName + "\"/>\n");
			} else {
				bWrite.write("		<many-to-one name=\"" + attrName + "\"");
				bWrite.write(" class=\"edu.emory.library.tas.dicts." + attrType + "\"");
				bWrite.write(" unique=\"true\" lazy=\"false\"/>\n");				 
			}
		}
		
		bWrite.write("	</class>\n");			   
		bWrite.write("</hibernate-mapping>\n");
		
		bWrite.flush();
		bWrite.close();
	}

	private static void prepareSetters(BufferedOutputStream oStream, List attrNames, List attrTypes) throws IOException {
		/** Generate setters **/
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
	}

	private static void prepareGetters(BufferedOutputStream oStream, List attrNames, List attrTypes) throws IOException {
		/** Generate getters **/
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
	}

	private static void cleanDB(ArrayList attributes, String[] args, int i) {
		
		Session session = HibernateUtil.getSession();
		
		String objType = args[i].substring(args[i].lastIndexOf(File.separator)+1, args[i].lastIndexOf("."));
		Conditions c = new Conditions(Conditions.JOIN_AND);
		c.addCondition("typeName", objType, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("ObjectType", c);
		Object[] otypes = HibernateConnector.getConnector().loadObjects(session, qValue);
		ObjectType otype = null;
		if (otypes.length != 0) {
			otype = (ObjectType)otypes[0];
		} else {
			otype = new ObjectType();
			otype.setTypeName(objType);
			HibernateConnector.getConnector().saveObject(session, otype);
		}
		
		Conditions cAttr = new Conditions(Conditions.JOIN_AND);
		cAttr.addCondition("objectType", otype, Conditions.OP_EQUALS);
		Object[] objs = new QueryValue("Attribute", cAttr).executeQuery(session);
		
		for (int k = 0; k < objs.length; k++) {
			Attribute attr = (Attribute)objs[k];
			boolean found = false;
			for (int l = 0; l < attributes.size(); l++) {
				
				Attribute that = (Attribute)attributes.get(l);
				if (attr.getId().equals(that.getId())) {
					found = true;
					break;
				}	
			}
			if (!found) {
				System.out.println("Removing attribute: " + attr.getName());
				c = new Conditions();
				c.addCondition("objectType", otype, Conditions.OP_EQUALS);
				Object[] compounds = new QueryValue("CompoundAttribute", c).executeQuery(session);
				for (int j = 0; j < compounds.length; j++) {
					CompoundAttribute compound = (CompoundAttribute)compounds[j];
					Set set = compound.getAttributes();
					for (Iterator iter = set.iterator(); iter.hasNext();) {
						Attribute element = (Attribute) iter.next();
						if (element.getId().equals(attr.getId())) {
							set.remove(attr);
						}
					}
					compound.setAttributes(set);
					HibernateConnector.getConnector().updateObject(session, compound);
				}
				
				c = new Conditions();
				c.addCondition("objectType", otype, Conditions.OP_EQUALS);
				Object[] groups = new QueryValue("Group", c).executeQuery(session);
				ArrayList toRemove = new ArrayList();
				for (int j = 0; j < groups.length; j++) {
					Group group = (Group)groups[j];
					Set set = group.getAttributes();
					for (Iterator iter = set.iterator(); iter.hasNext();) {
						Attribute element = (Attribute) iter.next();
						if (element.getId().equals(attr.getId())) {
							toRemove.add(attr);
						}
					}
					for (Iterator iter = toRemove.iterator(); iter.hasNext();) {
						Attribute element = (Attribute) iter.next();
						set.remove(element);
					} 
						
					group.setAttributes(set);
					HibernateConnector.getConnector().updateObject(session, group);
				}
				
				HibernateConnector.getConnector().deleteObject(session, attr);
				
			}
		}
		session.close();
	}

	private static Attribute doDBUpdate(String[] args, int i, String attrName, String attrLabel, String attrImportName, String attrImportDateYear, String attrImportDateMonth, String attrImportDateDay, Integer length, int type, String dict, int importType) {
		
		String objType = args[i].substring(args[i].lastIndexOf(File.separator)+1, args[i].lastIndexOf("."));
		Conditions c = new Conditions(Conditions.JOIN_AND);
		c.addCondition("typeName", objType, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("ObjectType", c);
		Object[] otypes = HibernateConnector.getConnector().loadObjects(qValue);
		ObjectType otype = null;
		if (otypes.length != 0) {
			otype = (ObjectType)otypes[0];
		} else {
			otype = new ObjectType();
			otype.setTypeName(objType);
			HibernateConnector.getConnector().saveObject(otype);
		}
		
		Conditions cAttr = new Conditions(Conditions.JOIN_AND);
		cAttr.addCondition("objectType", otype, Conditions.OP_EQUALS);
		cAttr.addCondition("name", attrName, Conditions.OP_EQUALS);
		Object[] objs = new QueryValue("Attribute", cAttr).executeQuery();
		
		if (objs.length == 0) {
			Attribute attr = new Attribute();
			attr.setName(attrName);
			attr.setUserLabel(attrLabel);
			attr.setObjectType(otype);
			attr.setImportType(new Integer(importType));
			attr.setImportName(attrImportName);
			attr.setImportDateYear(attrImportDateYear);
			attr.setImportDateMonth(attrImportDateMonth);
			attr.setImportDateDay(attrImportDateDay);
			attr.setLength(length);
			attr.setType(new Integer(type));
			attr.setDictionary(dict);
			HibernateConnector.getConnector().saveObject(attr);
			return attr;
		} else {
			Attribute attr = (Attribute)objs[0];
			attr.setName(attrName);
			//attr.setUserLabel(attrLabel);
			attr.setObjectType(otype);
			attr.setImportType(new Integer(importType));
			attr.setImportName(attrImportName);
			attr.setImportDateYear(attrImportDateYear);
			attr.setImportDateMonth(attrImportDateMonth);
			attr.setImportDateDay(attrImportDateDay);
			attr.setLength(length);
			attr.setType(new Integer(type));
			attr.setDictionary(dict);
			HibernateConnector.getConnector().updateObject(attr);
			return attr;
		}
	}
}
