package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;

public class SubmissionEdit extends Submission
{
	private Voyage modifiedVoyage;
	private Voyage newVoyage;
	private Voyage oldVoyage;
	private Map attributeNotes;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("time", new DateAttribute("id", null));
		attributes.put("newVoyage", new VoyageAttribute("newVoyage", "Voyage"));
		attributes.put("oldVoyage", new VoyageAttribute("oldVoyage", "Voyage"));
		attributes.put("solved", new BooleanAttribute("solved", "SubmissionEdit", null));
		attributes.put("accepted", new BooleanAttribute("accepted", "Submission", null));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public Map getAttributeNotes()
	{
		return attributeNotes;
	}

	public void setAttributeNotes(Map attributeNotes)
	{
		this.attributeNotes = attributeNotes;
	}

	public Voyage getNewVoyage()
	{
		return newVoyage;
	}
	
	public void setNewVoyage(Voyage voyageNew)
	{
		this.newVoyage = voyageNew;
	}
	
	public Voyage getOldVoyage()
	{
		return oldVoyage;
	}
	
	public void setOldVoyage(Voyage voyageOld)
	{
		this.oldVoyage = voyageOld;
	}

	public Voyage getModifiedVoyage() {
		return modifiedVoyage;
	}

	public void setModifiedVoyage(Voyage modifiedVoyage) {
		this.modifiedVoyage = modifiedVoyage;
	}

}