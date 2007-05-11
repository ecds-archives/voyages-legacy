package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;

public class SubmissionEdit extends Submission
{
	private EditedVoyage editorVoyage;
	private EditedVoyage newVoyage;
	private EditedVoyage oldVoyage;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("time", new DateAttribute("id", null));
		attributes.put("newVoyage", new EditedVoyageAttribute("newVoyage", "Voyage"));
		attributes.put("oldVoyage", new EditedVoyageAttribute("oldVoyage", "Voyage"));
		attributes.put("solved", new BooleanAttribute("solved", "SubmissionEdit", null));
		attributes.put("accepted", new BooleanAttribute("accepted", "Submission", null));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}

	public EditedVoyage getNewVoyage()
	{
		return newVoyage;
	}
	
	public void setNewVoyage(EditedVoyage voyageNew)
	{
		this.newVoyage = voyageNew;
	}
	
	public EditedVoyage getOldVoyage()
	{
		return oldVoyage;
	}
	
	public void setOldVoyage(EditedVoyage voyageOld)
	{
		this.oldVoyage = voyageOld;
	}

	public EditedVoyage getEditorVoyage() {
		return editorVoyage;
	}

	public void setEditorVoyage(EditedVoyage modifiedVoyage) {
		this.editorVoyage = modifiedVoyage;
	}

}