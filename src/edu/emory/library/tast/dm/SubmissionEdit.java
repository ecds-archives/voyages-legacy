package edu.emory.library.tast.dm;

import java.util.Map;

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