package edu.emory.library.tast.dm;

import java.util.Map;
import java.util.Set;

public class SubmissionMerge extends Submission
{
	
	private Voyage proposedNewVoyage;
	private Set mergedVoyages;
	private Map attributeNotes;
	
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

}