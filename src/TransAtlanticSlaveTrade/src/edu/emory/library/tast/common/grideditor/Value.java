/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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

	public abstract boolean isEmpty();

	
}