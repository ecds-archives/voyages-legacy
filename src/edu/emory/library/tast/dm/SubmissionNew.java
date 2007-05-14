package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;

public class SubmissionNew extends Submission
{
	
	private EditedVoyage newVoyage;
	private EditedVoyage editorVoyage;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("time", new DateAttribute("id", null));
		attributes.put("newVoyage", new EditedVoyageAttribute("newVoyage", "EditedVoyage"));
		attributes.put("solved", new BooleanAttribute("solved", "SubmissionNew", null));
		attributes.put("accepted", new BooleanAttribute("accepted", "Submission", null));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public EditedVoyage getNewVoyage()
	{
		return newVoyage;
	}

	public void setNewVoyage(EditedVoyage voyage)
	{
		this.newVoyage = voyage;
	}

	public EditedVoyage getEditorVoyage() {
		return editorVoyage;
	}

	public void setEditorVoyage(EditedVoyage modifiedVoyage) {
		this.editorVoyage = modifiedVoyage;
	}

}