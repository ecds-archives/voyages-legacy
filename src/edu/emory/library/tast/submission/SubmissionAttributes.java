package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SubmissionAttributes {

	private static SubmissionAttributes instance = null;
	private SubmissionAttribute[] attributes;
	private SubmissionAttribute[] attributesPublic;
	private Map map = new HashMap();
	
	public synchronized static SubmissionAttributes getConfiguration() {
		if (instance == null) {
			instance = new SubmissionAttributes();
		}
		return instance;
	}
	
	private SubmissionAttributes() {
		System.out.println("***************************************************");
		attributes = loadConfig();
		ArrayList tmp = new ArrayList();
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].isPublic()) {
				tmp.add(attributes[i]);
			}
		}
		attributesPublic = (SubmissionAttribute[])tmp.toArray(new SubmissionAttribute[] {});
		
//		attributes = new SubmissionAttribute[10];
//		
//		attributes[0] = new SubmissionAttribute("shipname", Voyage.getAttribute("shipname"), "Name of ship", null, TextboxAdapter.TYPE);
//		attributes[1] = new SubmissionAttribute("datedep", Voyage.getAttribute("datedep"), "Departure date", null, DateAdapter.TYPE);
//		attributes[2] = new SubmissionAttribute("captaina", Voyage.getAttribute("captaina"), "First captain", null, TextboxAdapter.TYPE);
//		attributes[3] = new SubmissionAttribute("captainb", Voyage.getAttribute("captainb"), "Second captain", null, TextboxAdapter.TYPE);
//		attributes[4] = new SubmissionAttribute("portdep", Voyage.getAttribute("portdep"), "Port of departure", null, TextboxAdapter.TYPE);
//		attributes[5] = new SubmissionAttribute("arrport", Voyage.getAttribute("arrport"), "Port of arrival", null, TextboxAdapter.TYPE);
//		attributes[6] = new SubmissionAttribute("crew1", Voyage.getAttribute("crew1"), "Crew 1", null, TextboxIntegerAdapter.TYPE);
//		attributes[7] = new SubmissionAttribute("crew3", Voyage.getAttribute("crew3"), "Crew 2", null, TextboxIntegerAdapter.TYPE);
//		attributes[8] = new SubmissionAttribute("tonnage", Voyage.getAttribute("tonnage"), "Tonnage of vessel", null, TextboxFloatAdapter.TYPE);
//		attributes[9] = new SubmissionAttribute("guns", Voyage.getAttribute("guns"), "Guns", null, TextboxIntegerAdapter.TYPE);
		
	}

	public SubmissionAttribute[] getSubmissionAttributes() {
		return attributes;
	}
	
	private SubmissionAttribute[] loadConfig() {
		try {
			ArrayList list = new ArrayList();
			Document document = new XMLConfiguration("submission-attributes.xml")
					.getDocument();
			Node mainNode = document.getFirstChild();
			if (mainNode != null) {
				if (mainNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList attrs = mainNode.getChildNodes();
					for (int j = 0; j < attrs.getLength(); j++) {
						if (attrs.item(j).getNodeType() == Node.ELEMENT_NODE) {
							SubmissionAttribute attr = SubmissionAttribute.fromXML(attrs.item(j));
							if (attr.getKey() == null) {
								list.add(attr);
							} else {
								map.put(attr.getKey(), attr);
							}							
						}
					}
				}
			}
			return (SubmissionAttribute[])list.toArray(new SubmissionAttribute[] {});
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SubmissionAttribute[] getAttributes() {
		return attributes;
	}

	public SubmissionAttribute[] getPublicAttributes() {
		return attributesPublic;
	}
	
	public SubmissionAttribute getAttribute(String key) {
		return (SubmissionAttribute) this.map.get(key);
	}
	
}
