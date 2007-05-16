package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;

public class SubmissionMerge extends Submission
{
	
	private EditedVoyage editorVoyage;
	private EditedVoyage proposedVoyage;
	private Set mergedVoyages;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("time", new DateAttribute("id", null));
		attributes.put("solved", new BooleanAttribute("solved", "SubmissionMerge", null));
		attributes.put("accepted", new BooleanAttribute("accepted", "Submission", null));
		attributes.put("editorVoyage", new EditedVoyageAttribute("editorVoyage", "EditedVoyage"));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public EditedVoyage getProposedVoyage()
	{
		return proposedVoyage;
	}
	
	public void setProposedVoyage(EditedVoyage voyageNew)
	{
		this.proposedVoyage = voyageNew;
	}
	
	public Set getMergedVoyages()
	{
		return mergedVoyages;
	}

	public void setMergedVoyages(Set mergedVoyages)
	{
		this.mergedVoyages = mergedVoyages;
	}

	public EditedVoyage getEditorVoyage() {
		return editorVoyage;
	}

	public void setEditorVoyage(EditedVoyage modifiedVoyage) {
		this.editorVoyage = modifiedVoyage;
	}

}