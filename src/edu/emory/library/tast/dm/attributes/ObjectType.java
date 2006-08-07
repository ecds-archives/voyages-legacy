package edu.emory.library.tast.dm.attributes;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import edu.emory.library.tast.util.HibernateUtil;

public class ObjectType implements Serializable {
	
	private static final long serialVersionUID = -5680579514956701065L;

	private Long id;
	private String typeName;
	
	public ObjectType() {
		
	}

	public static ObjectType loadById(Long id) {
		Session session = HibernateUtil.getSession();
		ObjectType objType = loadById(id, session);
		session.close();
		return objType;
	}
	
	public static ObjectType loadById(Long id, Session session) {
		Criteria crit = session.createCriteria(ObjectType.class);
		crit.add(Expression.eq("id", id));
		crit.setMaxResults(1);
		List list = crit.list();
		if (list == null || list.size() == 0) return null;
		return (ObjectType) list.get(0);
	}
	
	public static ObjectType getVoyages() {
		return loadById(new Long(1));
	}
	
	public static ObjectType getVoyages(Session session) {
		return loadById(new Long(1), session);
	}

	public static ObjectType getSlaves() {
		return loadById(new Long(2));
	}
	
	public static ObjectType getSlaves(Session session) {
		return loadById(new Long(2), session);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String toString() {
		return "Type " + this.typeName;
	}
	
}
