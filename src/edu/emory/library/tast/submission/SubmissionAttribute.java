package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.date.DateAdapter;
import edu.emory.library.tast.common.grideditor.date.DateValue;
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
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;

public class SubmissionAttribute {
	
	private Attribute attribute[];
	private String userLabel;
	private String comment;
	private String type;
	private String name;
	private boolean editable = false;
	
	public SubmissionAttribute(String name, Attribute attr, String userLabel, String comment, String type, boolean editable) {
		this.name = name;
		this.attribute = new Attribute[] {attr};
		this.userLabel = userLabel;
		this.comment = comment;
		this.type = type;
		this.editable = editable;
	}
	
	public SubmissionAttribute(String name, Attribute[] attrs, String userLabel, String comment, String type, boolean editable) {
		this.attribute = attrs;
		this.userLabel = userLabel;
		this.comment = comment;
		this.name = name;
		this.type = type;
		this.editable = editable;
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
				return new TextboxValue("Empty");
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
				strArr[i] = toBeFormatted[i].toString();
			}
			return new TextareaValue(strArr);
		} else {
			throw new RuntimeException("Attribute type " + type + " not defined in Submission attribute");
		}
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
			throw new RuntimeException("Attribute type " + type + " not defined in Submission attribute");
		}
	}

	public String getType() {
		return type;
	}

	public Object[] getValues(Object object) {
		if (type.equals(TextboxAdapter.TYPE)) {
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
		if (node.getAttributes().getNamedItem("comment") != null) {
			comment = node.getAttributes().getNamedItem("comment").getNodeValue();
		}
		boolean editable = false;
		if (node.getAttributes().getNamedItem("editable") != null) {
			editable = node.getAttributes().getNamedItem("editable").getNodeValue().equals("Y");
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
			attrType = TextboxAdapter.TYPE;
		} else if (type.equals("dictionary")) {
			attrType = TextboxAdapter.TYPE;
		} else if (type.equals("boolean")) {
			attrType = TextboxAdapter.TYPE;
		} else if (type.equals("long")) {
			attrType = TextboxLongAdapter.TYPE;
		} else {
			throw new RuntimeException("Type: " + type + " not allowed in submission attributes");
		}
		
		SubmissionAttribute attr = new SubmissionAttribute(name, 
				(Attribute[]) attributesList.toArray(new Attribute[] {}), 
				userLabel, comment, attrType, editable);
		System.out.println("Added attribute: " + attr);
		return attr;
	}
	
	public String toString() {
		return "Submission attribute: [" + this.name + "]";
	}

	public boolean isPublic() {
		return this.editable;
	}
}
