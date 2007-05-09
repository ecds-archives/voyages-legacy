package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;

public class SubmissionMerge extends Submission
{
	
	private Voyage modifiedVoyage;
	private Voyage proposedNewVoyage;
	private Set mergedVoyages;
	private Map attributeNotes;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("time", new DateAttribute("id", null));
		attributes.put("solved", new BooleanAttribute("solved", "SubmissionMerge", null));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public Voyage getProposedNewVoyage()
	{
		return proposedNewVoyage;
	}
	
	public void setProposedNewVoyage(Voyage voyageNew)
	{
		this.proposedNewVoyage = voyageNew;
	}
	
	public Set getMergedVoyages()
	{
		return mergedVoyages;
	}

	public void setMergedVoyages(Set mergedVoyages)
	{
		this.mergedVoyages = mergedVoyages;
	}

	public Map getAttributeNotes()
	{
		return attributeNotes;
	}

	public void setAttributeNotes(Map attributeNotes)
	{
		this.attributeNotes = attributeNotes;
	}

	public Voyage getModifiedVoyage() {
		return modifiedVoyage;
	}

	public void setModifiedVoyage(Voyage modifiedVoyage) {
		this.modifiedVoyage = modifiedVoyage;
	}

}