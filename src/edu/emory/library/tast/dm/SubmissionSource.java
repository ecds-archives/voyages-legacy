package edu.emory.library.tast.dm;

import edu.emory.library.tast.util.StringUtils;

public class SubmissionSource
{

	private Long id;
	private String note;
	private Submission submission;

	public Submission getSubmission()
	{
		return submission;
	}

	public void setSubmission(Submission submission)
	{
		this.submission = submission;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public int hashCode()
	{
		Long id = getId();
		return id == null ? 0 : id.intValue();
	}
	
	public boolean equals(Object obj)
	{
		
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;

		if (!(obj instanceof SubmissionSource))
			return false;
		
		final SubmissionSource that = (SubmissionSource) obj;
		
		// both have id
		if (this.id != null && that.id != null)
			return this.id.equals(that.id);
		
		// one is new
		if (this.id == null || that.id == null)
			return false;
		
		// both are new
		return
			StringUtils.compareStrings(this.note, that.note);

	}

}
