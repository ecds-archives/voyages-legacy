package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.database.query.QueryCondition;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.SubmissionAttribute;
import edu.emory.library.tast.dm.attributes.UserAttribute;
import edu.emory.library.tast.util.StringUtils;

public class SubmissionSource
{

	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("submission", new SubmissionAttribute("submission", "Submission"));
	}
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
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

	public static SubmissionSource loadById(Session session, Long id) {
		TastDbConditions c = new TastDbConditions();
		c.addCondition(getAttribute("id"), id, TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("SubmissionSource", c);
		Object[] response = qValue.executeQuery(session);
		if (response.length != 0) {
			return (SubmissionSource)response[0];		
		}
		return null;
	}

}
