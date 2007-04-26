package edu.emory.library.tast.dm;

import java.util.Set;

public class SubmissionMerge
{
	
	private Voyage voyageNew;
	private Set voyagesOld;
	
	public Voyage getVoyageNew()
	{
		return voyageNew;
	}
	
	public void setVoyageNew(Voyage voyageNew)
	{
		this.voyageNew = voyageNew;
	}
	
	public Set getVoyagesOld()
	{
		return voyagesOld;
	}

	public void setVoyagesOld(Set voyagesOld)
	{
		this.voyagesOld = voyagesOld;
	}

}
