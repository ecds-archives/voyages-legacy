package edu.emory.library.tast.submission;

import java.util.Date;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.date.DateAdapter;
import edu.emory.library.tast.common.grideditor.date.DateValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxValue;
import edu.emory.library.tast.dm.attributes.Attribute;

public class SubmissionAttribute {
	
	private Attribute attribute[];
	private String userLabel;
	private String comment;
	private String type;
	
	public SubmissionAttribute(Attribute attr, String userLabel, String comment, String type) {
		this.attribute = new Attribute[] {attr};
		this.userLabel = userLabel;
		this.comment = comment;
		this.type = type;
	}
	
	public SubmissionAttribute(Attribute[] attrs, String userLabel, String comment) {
		this.attribute = attrs;
		this.userLabel = userLabel;
		this.comment = comment;
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
		if (toBeFormatted[0] == null) {
			return new TextboxValue("Empty");
		}
		if (type.equals(TextboxAdapter.TYPE)) {
			return new TextboxValue(toBeFormatted[0].toString());
		} else if (type.equals(DateAdapter.TYPE)) {
			return new DateValue((Date)toBeFormatted[0]);
		} else if (type.equals(TextboxIntegerAdapter.TYPE)) {
			return new TextboxIntegerValue(toBeFormatted[0].toString());
		} else if (type.equals(TextboxFloatAdapter.TYPE)) {
			return new TextboxFloatValue(toBeFormatted[0].toString());
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
		} else {
			throw new RuntimeException("Attribute type " + type + " not defined in Submission attribute");
		}
		
	}
}
