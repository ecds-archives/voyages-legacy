package edu.emory.library.tast.submissions;

import edu.emory.library.tast.common.grideditor.Value;
import edu.emory.library.tast.common.grideditor.textbox.TextboxValue;
import edu.emory.library.tast.dm.attributes.Attribute;

public class SubmissionAttribute {
	
	private Attribute attribute[];
	private String userLabel;
	private String comment;
	
	public SubmissionAttribute(Attribute attr, String userLabel, String comment) {
		this.attribute = new Attribute[] {attr};
		this.userLabel = userLabel;
		this.comment = comment;
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
		return new TextboxValue(toBeFormatted[0].toString());
	}
}
