package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.SubmissionAttribute;
import edu.emory.library.tast.dm.attributes.UserAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

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
		Conditions c = new Conditions();
		c.addCondition(getAttribute("id"), editorId, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("SubmissionEditor", c);
		Object[] ret = qValue.executeQuery(session);
		if (ret.length == 0) {
			return null;
		}
		
		return (SubmissionEditor) ret[0];
	}
	
	
	
}
