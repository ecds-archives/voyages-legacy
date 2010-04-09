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
package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;

public class SubmissionNew extends Submission
{
	
	private EditedVoyage newVoyage;
	private EditedVoyage editorVoyage;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("time", new DateAttribute("id", null));
		attributes.put("editorVoyage", new EditedVoyageAttribute("editorVoyage", "EditedVoyage"));
		attributes.put("newVoyage", new EditedVoyageAttribute("newVoyage", "EditedVoyage"));
		attributes.put("solved", new BooleanAttribute("solved", "SubmissionNew", null));
		attributes.put("accepted", new BooleanAttribute("accepted", "Submission", null));
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public EditedVoyage getNewVoyage()
	{
		return newVoyage;
	}

	public void setNewVoyage(EditedVoyage voyage)
	{
		this.newVoyage = voyage;
	}

	public EditedVoyage getEditorVoyage() {
		return editorVoyage;
	}

	public void setEditorVoyage(EditedVoyage modifiedVoyage) {
		this.editorVoyage = modifiedVoyage;
	}

}