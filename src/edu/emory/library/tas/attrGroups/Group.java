package edu.emory.library.tas.attrGroups;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import edu.emory.library.tas.util.HibernateUtil;

public class Group implements Serializable {
	
	private static final long serialVersionUID = -2786637413827394050L;
	
	private Long id;
	private String name;
	private String userLabel;
	private Set compoundAttributes  = new HashSet();
	private Set attributes = new HashSet();
	private ObjectType objectType;
	private String description;
	
	public Group() {		
	}
	
	public static Group loadById(Long id) {
		Session session = HibernateUtil.getSession();
		Group group = loadById(id, session);
		session.close();
		return group;
	}
	
	public static Group loadById(Long id, Session session) {
		Criteria crit = session.createCriteria(Group.class);
		crit.add(Expression.eq("id", id));
		crit.setMaxResults(1);
		List list = crit.list();
		if (list == null || list.size() == 0) return null;
		return (Group) list.get(0);
	}
	
	public Set getCompoundAttributes() {
		return compoundAttributes;
	}
	public void setCompoundAttributes(Set groups) {
		this.compoundAttributes = groups;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set getAttributes() {
		return attributes;
	}
	public void setAttributes(Set attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	
	public String toString() {
		return "Set " + this.name +": groups " + this.compoundAttributes + "\n     attributes " + this.attributes;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
