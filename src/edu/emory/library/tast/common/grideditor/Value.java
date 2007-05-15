package edu.emory.library.tast.common.grideditor;

import edu.emory.library.tast.util.StringUtils;

abstract public class Value
{
	
	private String errorMessage;
	private String note;
	private boolean noteExpanded;

	public boolean isError()
	{
		return !StringUtils.isNullOrEmpty(errorMessage);
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
	
	public boolean hasNote()
	{
		return !StringUtils.isNullOrEmpty(note);
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public boolean isNoteExpanded()
	{
		return noteExpanded;
	}

	public void setNoteExpanded(boolean noteExpanded)
	{
		this.noteExpanded = noteExpanded;
	}
	
	public abstract boolean isCorrectValue();
	
}