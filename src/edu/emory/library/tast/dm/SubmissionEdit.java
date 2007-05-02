package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;

public class SubmissionEdit extends Submission
{
	
	private Voyage newVoyage;
	private Voyage oldVoyage;
	private Map attributeNotes;
	
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

}