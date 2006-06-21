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
	
	
	public CompoundAttribute() {	
	}
	
	public Set getAttributes() {
		return attributes;
	}
	public void setAttributes(Set attributes) {
		this.attributes = attributes;
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
	
}
