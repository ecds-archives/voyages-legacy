package edu.emory.library.tas.attrGroups;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import edu.emory.library.tas.util.HibernateUtil;

public class CompoundAttribute extends AbstractAttribute {
	
	private static final long serialVersionUID = -6276849726137818052L;

	private Set attributes = new HashSet();
	private Set groups = new HashSet();
	
	
	public CompoundAttribute() {	
	}
	
	public Set getAttributes() {
		return attributes;
	}
	public void setAttributes(Set attributes) {
		this.attributes = attributes;
	}
	
	public Set getGroups() {
		return groups;
	}

	public void setGroups(Set groups) {
		this.groups = groups;
	}

	public int getGroupsCount() {
		if (groups == null) return 0;
		return groups.size();
	}

	public String toString() {
		return "Group of attributes " + this.attributes;
	}
	
	public static AbstractAttribute loadById(Long id) {
		Session session = HibernateUtil.getSession();
		AbstractAttribute attr = loadById(id, session);
		session.close();
		return attr;
	}
	
	public static AbstractAttribute loadById(Long id, Session session) {
		Criteria crit = session.createCriteria(CompoundAttribute.class);
		crit.add(Expression.eq("id", id));
		crit.setMaxResults(1);
		List list = crit.list();
		if (list == null || list.size() == 0) return null;
		return (AbstractAttribute) list.get(0);
	}
	
	public static CompoundAttribute newForVoyages() {
		CompoundAttribute attribute = new CompoundAttribute();
		attribute.setObjectType(ObjectType.getVoyages());
		return attribute;
	}
	
	public static CompoundAttribute newForVoyages(Session session) {
		CompoundAttribute attribute = new CompoundAttribute();
		attribute.setObjectType(ObjectType.getVoyages(session));
		return attribute;
	}

	public static CompoundAttribute newForSlaves() {
		CompoundAttribute attribute = new CompoundAttribute();
		attribute.setObjectType(ObjectType.getSlaves());
		return attribute;
	}
	
	public static CompoundAttribute newForSlaves(Session session) {
		CompoundAttribute attribute = new CompoundAttribute();
		attribute.setObjectType(ObjectType.getSlaves(session));
		return attribute;
	}

	public int getAttributesCount()
	{
		if (attributes == null) return 0;
		return attributes.size();
	}

}
