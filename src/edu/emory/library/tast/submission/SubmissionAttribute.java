package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.date.DateAdapter;
import edu.emory.library.tast.common.grideditor.date.DateValue;
import edu.emory.library.tast.common.grideditor.list.ListValue;
import edu.emory.library.tast.common.grideditor.textbox.TextareaAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextareaValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxLongAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxLongValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxValue;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.FateAttribute;
import edu.emory.library.tast.dm.attributes.NationAttribute;
import edu.emory.library.tast.dm.attributes.VesselRigAttribute;

public class SubmissionAttribute {
	
	private Attribute attribute[];
	private String userLabel;
	private String comment;
	private String type;
	private String name;
	private String group;
	private String key;
	private boolean editable = false;
	
	public SubmissionAttribute(String name, Attribute attr, String userLabel, String group, String comment, String type, boolean editable, String key) {
		this.name = name;
		this.attribute = new Attribute[] {attr};
		this.userLabel = userLabel;
		this.comment = comment;
		this.type = type;
		this.editable = editable;
		this.group = group;
		this.key = key;
	}
	
	public SubmissionAttribute(String name, Attribute[] attrs, String userLabel, String group, String comment, String type, boolean editable, String key) {
		this.attribute = attrs;
		this.userLabel = userLabel;
		this.comment = comment;
		this.name = name;
		this.type = type;
		this.editable = editable;
		this.group = group;
		this.key = key;
	}
	
	public Attribute[] getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute[] attribute) {
		this.attribute = attribute;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getUserLabel() {
		return userLabel;
	}
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public Value getValue(Object[] toBeFormatted) {
		if (type.equals(TextboxAdapter.TYPE)) {
			if (toBeFormatted[0] == null) {
				return new TextboxValue(null);
			}
			return new TextboxValue(toBeFormatted[0].toString());
		} else if (type.equals(DateAdapter.TYPE)) {
			if (toBeFormatted[0] == null) {
				return new DateValue(null, null, null);
			}
			return new DateValue((Date)toBeFormatted[0]);
		} else if (type.equals(TextboxIntegerAdapter.TYPE)) {
			if (toBeFormatted[0] == null) {
				return new TextboxIntegerValue((Integer)null);
			}
			return new TextboxIntegerValue(toBeFormatted[0].toString());
		} else if (type.equals(TextboxFloatAdapter.TYPE)) {
			if (toBeFormatted[0] == null) {
				return new TextboxFloatValue((Float)null);
			}
			return new TextboxFloatValue(toBeFormatted[0].toString());
		} else if (type.equals(TextboxLongAdapter.TYPE)) {
			if (toBeFormatted[0] == null) {
				return new TextboxLongValue((Integer)null);
			}
			return new TextboxFloatValue(toBeFormatted[0].toString());
		} else if (type.equals(TextareaAdapter.TYPE)) {
			String[] strArr = new String[toBeFormatted.length];
			for (int i = 0; i < strArr.length; i++) {
				if (toBeFormatted[i] != null) {
					strArr[i] = toBeFormatted[i].toString();
				}
			}
			return new TextareaValue(strArr);
		} else if (type.equals(SubmissionBean.LOCATIONS)) {
			if (toBeFormatted.length == 1) {
				if (toBeFormatted[0] == null) {
					return new ListValue();
				}
				Port port = (Port) toBeFormatted[0];
				return new ListValue(new String[] {port.getRegion().getArea().getId().toString(),
		            	   port.getRegion().getId().toString(),
			            	   port.getId().toString()});
			} else {
				if (toBeFormatted[1] == null) {
					return new ListValue();
				}
				Port port = (Port) toBeFormatted[1];
				return new ListValue(new String[] 
				     			               {port.getRegion().getArea().getId().toString(),
				     			            	   port.getRegion().getId().toString(),
				     			            	   port.getId().toString()});
			}			
		} else if (type.equals(SubmissionBean.PORTS)) {
			if (toBeFormatted[0] == null) {
				return new ListValue();
			}
			Port port = (Port) toBeFormatted[0];			
			return new ListValue(new String[] 
			               {port.getRegion().getArea().getId().toString(),
			            	   port.getRegion().getId().toString(),
			            	   port.getId().toString()});
		} else if (type.equals(SubmissionDictionaries.BOOLEAN)) {
			if (toBeFormatted[0] == null) {
				return new ListValue();
			}
			return new ListValue(new String[] {((Integer)toBeFormatted[0]).intValue() == 1 ? "true" : "false"});
		} else {
			if (toBeFormatted[0] == null) {
				return new ListValue();
			}
			Dictionary dict = (Dictionary) toBeFormatted[0];			
			return new ListValue(new String[] {dict.getId().toString()});
		} 
//		else {
//			throw new RuntimeException("Attribute type " + type + " not defined in Submission attribute");
//		}
	}

	public Value getEmptyValue() {
		if (type.equals(TextboxAdapter.TYPE)) {
			return new TextboxValue(null);
		} else if (type.equals(DateAdapter.TYPE)) {
			return new DateValue(null, null, null);
		} else if (type.equals(TextboxIntegerAdapter.TYPE)) {
			return new TextboxIntegerValue((Integer)null);
		} else if (type.equals(TextboxFloatAdapter.TYPE)) {
			return new TextboxFloatValue((Float)null);
		} else if (type.equals(TextboxLongAdapter.TYPE)) {
			return new TextboxLongValue((Integer)null);
		} else if (type.equals(TextareaAdapter.TYPE)) {
			return new TextareaValue((String)null);
		} else {
			return new ListValue();
		}
//		else {
//			throw new RuntimeException("Attribute type " + type + " not defined in Submission attribute");
//		}
	}

	public String getType() {
		return type;
	}

	public Object[] getValues(Session sess, Object object) {
		if (type.equals(TextboxAdapter.TYPE)) {
			if ("".equals(((TextboxValue)object).getText())) {
				return new Object[] {null};
			}
			return new Object[]{((TextboxValue)object).getText()};
		} else if (type.equals(DateAdapter.TYPE)) {
			return new Object[]{((DateValue)object).getDate()};
		} else if (type.equals(TextboxIntegerAdapter.TYPE)) {
			return new Object[]{((TextboxIntegerValue)object).getInteger()};
		} else if (type.equals(TextboxFloatAdapter.TYPE)) {
			return new Object[]{((TextboxFloatValue)object).getFloat()};
		} else if (type.equals(TextboxLongAdapter.TYPE)) {
			return new Object[]{((TextboxLongValue)object).getInteger()};
		} else if (type.equals(TextareaAdapter.TYPE)) {
			return ((TextareaValue)object).getTexts();
		} else if (type.equals(SubmissionBean.LOCATIONS)) {
			if (this.attribute.length == 1) {
				String portId = ((ListValue)object).getValues()[((ListValue)object).getValues().length - 1];
				Port port = null;
				if (!portId.equals("-1")) {
					port = Port.loadById(sess, portId);
				}
				return new Object[] {port};
			} else {
				String regionId = "-1";
				String portId = "-1";
				if (((ListValue)object).getValues().length != 1) {
					regionId = ((ListValue)object).getValues()[1];
					portId = ((ListValue)object).getValues()[2];
				}
				Region region = null;
				Port port = null;
				if (!portId.equals("-1")) {
					port = Port.loadById(sess, portId);
				}
				if (!regionId.equals("-1")) {
					region = Region.loadById(sess, portId);
				}
				return new Object[] {region, port};
			}
		} else if (type.equals(SubmissionBean.PORTS)) {
			String portId = ((ListValue)object).getValues()[0];
			if (portId.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {Port.loadById(null, portId)};
		} else if (type.equals(SubmissionDictionaries.FATES)) {
			String id = ((ListValue)object).getValues()[0];
			if (id.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {Dictionary.loadById(Fate.class, sess, id)};
		} else if (type.equals(SubmissionDictionaries.RIGS)) {
			String id = ((ListValue)object).getValues()[0];
			if (id.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {Dictionary.loadById(VesselRig.class, sess, id)};
		} else if (type.equals(SubmissionDictionaries.NATIONALS)) {
			String id = ((ListValue)object).getValues()[0];
			if (id.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {Dictionary.loadById(Nation.class, sess, id)};
		} else if (type.equals(SubmissionDictionaries.FATE2)) {
			String id = ((ListValue)object).getValues()[0];
			if (id.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {Dictionary.loadById(FateSlaves.class, sess, id)};
		} else if (type.equals(SubmissionDictionaries.FATE3)) {
			String id = ((ListValue)object).getValues()[0];
			if (id.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {Dictionary.loadById(FateVessel.class, sess, id)};
		} else if (type.equals(SubmissionDictionaries.FATE4)) {
			String id = ((ListValue)object).getValues()[0];
			if (id.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {Dictionary.loadById(FateOwner.class, sess, id)};
		} else if (type.equals(SubmissionDictionaries.BOOLEAN)) {
			String id = ((ListValue)object).getValues()[0];
			if (id.equals("-1")) {
				return new Object[] {null};
			}
			return new Object[] {"true".equals(id) ? new Integer(1) : new Integer(0)};
		} else {
			throw new RuntimeException("Attribute type " + type + " not defined in Submission attribute");
		}
		
	}
	 

	public String getName()
	{
		return name;
	}

	public static SubmissionAttribute fromXML(Node node) {
				
		String name = node.getAttributes().getNamedItem("name").getNodeValue();
		String userLabel = node.getAttributes().getNamedItem("userLabel").getNodeValue();
		String type = node.getAttributes().getNamedItem("type").getNodeValue();
		String comment = null;
		String key = null;
		if (node.getAttributes().getNamedItem("comment") != null) {
			comment = node.getAttributes().getNamedItem("comment").getNodeValue();
		}
		boolean editable = false;
		if (node.getAttributes().getNamedItem("editable") != null) {
			editable = node.getAttributes().getNamedItem("editable").getNodeValue().equals("Y");
		}
		if (node.getAttributes().getNamedItem("key") != null) {
			key = node.getAttributes().getNamedItem("key").getNodeValue();
		}
		
		List attributesList = new ArrayList();
		NodeList attrsList = node.getChildNodes();
		for (int j = 0; j < attrsList.getLength(); j++) {
			Node attrsListChild = attrsList.item(j);
			if (attrsListChild != null) {
				NodeList attrs = attrsListChild.getChildNodes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					if (attr.getNodeType() == Node.ELEMENT_NODE) {
						String attrName = attr.getAttributes().getNamedItem("name").getNodeValue();
						attributesList.add(Voyage.getAttribute(attrName));
					}
				}
			}
		}
		
		String attrType = null;
		if (type.equals("string")) {
			if (attributesList.size() > 1) {
				attrType = TextareaAdapter.TYPE;
			} else {
				attrType = TextboxAdapter.TYPE;
			}
		} else if (type.equals("integer")) {
			attrType = TextboxIntegerAdapter.TYPE;
		} else if (type.equals("float")) {
			attrType = TextboxFloatAdapter.TYPE;
		} else if (type.equals("date")) {
			attrType = DateAdapter.TYPE;
		} else if (type.equals("port")) {
			if (attributesList.size() > 1) {
				attrType = SubmissionBean.LOCATIONS;
			} else {
				attrType = SubmissionBean.PORTS;
			}
		} else if (type.equals("dictionary")) {
			attrType = TextboxAdapter.TYPE;
			
		} else if (type.equals("long")) {
			attrType = TextboxLongAdapter.TYPE;
		} else {
			attrType = type;
			//throw new RuntimeException("Type: " + type + " not allowed in submission attributes");
		}
		String group = null;
		if (node.getAttributes().getNamedItem("group") != null) {
			group = node.getAttributes().getNamedItem("group").getNodeValue();
		}
		
		SubmissionAttribute attr = new SubmissionAttribute(name, 
				(Attribute[]) attributesList.toArray(new Attribute[] {}), 
				userLabel, group, comment, attrType, editable, key);
		//System.out.println("Added attribute: " + attr);
		return attr;
	}
	
	public String toString() {
		return "Submission attribute: [" + this.name + "]";
	}

	public boolean isPublic() {
		return this.editable;
	}

	public String getGroupName() {
		return this.group;
	}

	public Object getKey() {
		return key;
	}

}
