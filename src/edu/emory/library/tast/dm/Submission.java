package edu.emory.library.tast.dm;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EditedVoyageAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public abstract class Submission
{
	
	private Long id;
	private Date time;
	private String note;
	private Set sources;
	private boolean solved;
	private boolean accepted;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("time", new DateAttribute("id", null));
		attributes.put("solved", new BooleanAttribute("solved", "Submission", null));
		attributes.put("accepted", new BooleanAttribute("accepted", "Submission", null));
		attributes.put("editedVoyage", new EditedVoyageAttribute("editedVoyage", "EditedVoyage"));
	}
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

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
	
	public Set getSources()
	{
		return sources;
	}

	public void setSources(Set sources)
	{
		this.sources = sources;
	}
	
	public static void main(String[] args)
	{
		
		Session sess = HibernateUtil.getSession(); 
		Transaction	transaction = sess.beginTransaction();
		
		Voyage v1 = (Voyage) sess.createCriteria(Voyage.class).add(Restrictions.eq("id", new Long(1311700))).list().get(0);
		Voyage v2 = (Voyage) sess.createCriteria(Voyage.class).add(Restrictions.eq("id", new Long(1316184))).list().get(0);
		
		System.out.println("v1 ship = " + v1.getShipname());
		System.out.println("v2 ship = " + v2.getShipname());
		
		Map notes = new HashMap();
		notes.put("shipname", "xyz");
		notes.put("captain", "abc");
		
		Map notesEditor = new HashMap();
		notesEditor.put("shipname1", "xyz");
		notesEditor.put("captain1", "abc");
		
		SubmissionSourceBook book = new SubmissionSourceBook();
		book.setAuthors("Jan Zich");
		book.setTitle("Some title");
		book.setPublisher("Emory University");
		book.setYear(2007);
		book.setPageFrom(10);
		book.setPageTo(20);
		book.setPlaceOfPublication("Atlanta, GA, USA");
		
		Set sources = new HashSet();
		sources.add(book);
		
		SubmissionNew sn = new SubmissionNew();
		book.setSubmission(sn);
		//sn.setNewVoyage(v1);
		sn.setTime(new Date());
		//sn.setAttributeNotes(notes);
		//sn.setEditorNotes(notesEditor);
		sn.setSources(sources);
		sess.save(sn);
		
		System.out.println("new submission created, id = " + sn.getId());

		SubmissionEdit se = new SubmissionEdit();
		//se.setOldVoyage(v1);
		//se.setNewVoyage(v2);
		se.setTime(new Date());
		sess.save(se);
		
		System.out.println("new submission created, id = " + se.getId());
		
		SubmissionMerge sm = new SubmissionMerge();
		//sm.setProposedNewVoyage(v1);
		Set mergedVoyages = new HashSet();
		mergedVoyages.add(v1);
		mergedVoyages.add(v2);
		sm.setMergedVoyages(mergedVoyages);
		sm.setTime(new Date());
		sess.save(sm);
		
		System.out.println("new submission created, id = " + sm.getId());

		transaction.commit();
		sess.close();
		
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	public static Submission loadById(Session session, Long id) {
		Conditions c = new Conditions();
		c.addCondition(getAttribute("id"), id, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Submission", c);
		Object[] ret = qValue.executeQuery(session);
		if (ret.length == 0) {
			return null;
		} else {
			return (Submission) ret[0];
		}
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}


}