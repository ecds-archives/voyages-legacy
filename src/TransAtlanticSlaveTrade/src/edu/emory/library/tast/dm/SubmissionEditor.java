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

import org.hibernate.Session;

import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.SubmissionAttribute;
import edu.emory.library.tast.dm.attributes.UserAttribute;

public class SubmissionEditor {
	
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("editedVoyage", new EditedVoyageAttribute("editorVoyage", "EditedVoyage"));
		attributes.put("user", new UserAttribute("user", "User"));
		attributes.put("submission", new SubmissionAttribute("submission", "Submission"));
	}
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	private Long id;
	private Submission submission;
	private EditedVoyage editedVoyage;
	private User user;
	private boolean finished;
	
	public EditedVoyage getEditedVoyage() {
		return editedVoyage;
	}
	public void setEditedVoyage(EditedVoyage editedVoyage) {
		this.editedVoyage = editedVoyage;
	}
	public Submission getSubmission() {
		return submission;
	}
	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public static SubmissionEditor loadById(Session session, Long editorId)
	{
		TastDbConditions c = new TastDbConditions();
		c.addCondition(getAttribute("id"), editorId, TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("SubmissionEditor", c);
		Object[] ret = qValue.executeQuery(session);
		if (ret.length == 0) {
			return null;
		}
		
		return (SubmissionEditor) ret[0];
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	
	
}
