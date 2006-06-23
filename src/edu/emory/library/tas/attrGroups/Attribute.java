package edu.emory.library.tas.attrGroups;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import edu.emory.library.tas.util.HibernateUtil;

public class Attribute extends AbstractAttribute {
	
	private static final long serialVersionUID = -8780232223504322861L;

	public final static int IMPORT_TYPE_IGNORE = -1;
	public final static int IMPORT_TYPE_NUMERIC = 0; 
	public final static int IMPORT_TYPE_STRING = 1; 
	public final static int IMPORT_TYPE_DATE = 2; 
		
	private Integer importType;
	private String importName;
	private String importDateDay;
	private String importDateMonth;
	private String importDateYear;
	
	public Attribute() {
	}
	
	public Attribute(String name, Integer type, String dictionary, Integer importType, String importName, String importDateDay, String importDateMonth, String importDateYear, String userLabel, Integer length, ObjectType objType)
	{
		super(name, type, dictionary, userLabel, objType, length);
		this.importType = importType;
		this.importName = importName;
		this.importDateDay = importDateDay;
		this.importDateMonth = importDateMonth;
		this.importDateYear = importDateYear;
	}
	
	
	public static AbstractAttribute loadById(Long id) {
		Session session = HibernateUtil.getSession();
		AbstractAttribute attr = loadById(id, session);
		session.close();
		return attr;
	}
	
	public static AbstractAttribute loadById(Long id, Session session) {
		Criteria crit = session.createCriteria(Attribute.class);
		crit.add(Expression.eq("id", id));
		crit.setMaxResults(1);
		List list = crit.list();
		if (list == null || list.size() == 0) return null;
		return (AbstractAttribute) list.get(0);
	}

	public static Attribute newForVoyages() {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getVoyages());
		return attribute;
	}
	
	public static Attribute newForVoyages(Session session) {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getVoyages(session));
		return attribute;
	}

	public static Attribute newForSlaves() {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getSlaves());
		return attribute;
	}
	
	public static Attribute newForSlaves(Session session) {
		Attribute attribute = new Attribute();
		attribute.setObjectType(ObjectType.getSlaves(session));
		return attribute;
	}

	public String getImportDateDay()
	{
		return importDateDay;
	}
	
	public String getImportDateMonth()
	{
		return importDateMonth;
	}
	
	public String getImportDateYear()
	{
		return importDateYear;
	}
	
	public String getImportName()
	{
		if (importName == null) return getName();
		return importName;
	}
	
	public Integer getImportType()
	{
		return importType;
	}

	public void setImportDateDay(String importDateDay) {
		this.importDateDay = importDateDay;
	}

	public void setImportDateMonth(String importDateMonth) {
		this.importDateMonth = importDateMonth;
	}

	public void setImportDateYear(String importDateYear) {
		this.importDateYear = importDateYear;
	}

	public void setImportName(String importName) {
		this.importName = importName;
	}

	public void setImportType(Integer importType) {
		this.importType = importType;
	}

	public List loadContainingCompoundAttributes(Session session)
	{
		Query query = session.createQuery("from CompoundAttribute a where :attr = some elements(a.attributes)");
		query.setParameter("attr", this);
		return query.list();
	}

	public List loadContainingCompoundAttributes()
	{
		Session session = HibernateUtil.getSession();
		List groups = loadContainingCompoundAttributes(session);
		session.close();
		return groups;
	}

}