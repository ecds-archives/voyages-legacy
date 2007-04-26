package edu.emory.library.tast.dm;

import java.util.Date;

public class Submission
{
	
	private Long id;
	private Date time;
	private int type;
	private SubmissionNew submissionNew;
	private SubmissionEdit submissionEdit;
	private SubmissionMerge submissionMerge;

	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Date getTime()
	{
		return time;
	}
	
	public void setTime(Date time)
	{
		this.time = time;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}

	public SubmissionNew getSubmissionNew()
	{
		return submissionNew;
	}

	public void setSubmissionNew(SubmissionNew submissionNew)
	{
		this.submissionNew = submissionNew;
	}

	public SubmissionEdit getSubmissionEdit()
	{
		return submissionEdit;
	}

	public void setSubmissionEdit(SubmissionEdit submissionEdit)
	{
		this.submissionEdit = submissionEdit;
	}

	public SubmissionMerge getSubmissionMerge()
	{
		return submissionMerge;
	}

	public void setSubmissionMerge(SubmissionMerge submissionMerge)
	{
		this.submissionMerge = submissionMerge;
	}

}