package edu.emory.library.tast.dm;

import java.util.Map;

public class SubmissionNew extends Submission
{
	
	private Voyage newVoyage;
	private Map attributeNotes;

	public Voyage getNewVoyage()
	{
		return newVoyage;
	}

	public void setNewVoyage(Voyage voyage)
	{
		this.newVoyage = voyage;
	}

	public Map getAttributeNotes()
	{
		return attributeNotes;
	}

	public void setAttributeNotes(Map attributeNotes)
	{
		this.attributeNotes = attributeNotes;
	}

}