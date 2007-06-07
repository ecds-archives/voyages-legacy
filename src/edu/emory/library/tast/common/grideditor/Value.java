package edu.emory.library.tast.common.grideditor;

import edu.emory.library.tast.util.StringUtils;

abstract public class Value
{
	
	private String errorMessage;
	private String note;
	private boolean noteExpanded;
	private String[] pastNotes;
	
	public abstract boolean isCorrectValue();

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
	
	public boolean hasEditableNote()
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

	public boolean hasReadOnlyNotes()
	{
		return pastNotes != null && pastNotes.length != 0;
	}

	public String[] getPastNotes()
	{
		return pastNotes;
	}

	public void setPastNotes(String[] readOnlyNotes)
	{
		this.pastNotes = readOnlyNotes;
	}
	
}