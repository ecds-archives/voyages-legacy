package edu.emory.library.tas.util.query;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tas.util.HibernateConnector;

public class QueryValue {
	
	public static final int LIMIT_NO_LIMIT = -1;
	
	public static final int ORDER_DEFAULT = 0;
	public static final int ORDER_ASC = 1;
	public static final int ORDER_DESC = -1;
	
	private String object;
	private Conditions conditions;
	private String groupBy;
	private String orderBy;
	private int order;
	private int limit;
	
	private ArrayList populateValues = null;
	
	public QueryValue(String objType) {
		this(objType, null);
	}
	
	public QueryValue(String objType, Conditions cond) {
		this(objType, cond, LIMIT_NO_LIMIT);
	}
	
	public QueryValue(String objType, Conditions cond, int limit) {
		this.object = objType;
		this.conditions = cond;
		this.limit = limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public void setConditions(Conditions cond) {
		this.conditions = cond;
	}
	
	public ConditionResponse toStringWithParams() {
		StringBuffer buf = new StringBuffer();
		
		if (this.populateValues != null) {
			buf.append("select ");
			boolean first = true;
			Iterator iter = this.populateValues.iterator();
			while (iter.hasNext()) {
				if (!first) {
					buf.append(",");
				}
				first = false;
				buf.append(iter.next()).append(" ");
			}
		}
		
		buf.append("from ").append(this.object);
		ConditionResponse response = this.conditions.getConditionHQL();
		if (!response.conditionString.toString().trim().equals("")) {
			buf.append(" where ").append(response.conditionString);
		}
		
		if (orderBy != null) {		
			buf.append(" order by ").append(orderBy);
			if (order != ORDER_DEFAULT) {
				buf.append(" ");
				if (order == ORDER_ASC) {
					buf.append("asc");
				} else {
					buf.append("desc");
				}
			}
		}
		
		if (groupBy != null) {
			buf.append(" group by ").append(groupBy);
		}
		
		ConditionResponse res = new ConditionResponse();
		res.conditionString = buf;
		res.properties = response.properties;
		return res;
	}
	
	public void addPopulatedAttribute(String p_attrName) {
		if (this.populateValues == null) {
			this.populateValues = new ArrayList();
		}
		this.populateValues.add(p_attrName);
	}
	
	public Query getQuery(Session session) {
		ConditionResponse response = toStringWithParams();
		System.out.println("My query: " + response.conditionString);
		Query q = session.createQuery(response.conditionString.toString());
		
		Iterator iter = response.properties.keySet().iterator();
		while (iter.hasNext()) {
			String param = iter.next().toString();
			q.setParameter(param, response.properties.get(param));
		}
		
		if (this.limit != LIMIT_NO_LIMIT) {
			q.setMaxResults(this.limit);
		}
		return q;
	}
	 
	public Object[] executeQuery() {
		return HibernateConnector.getConnector().loadObjects(this);
	}
	
	public Object[] executeQuery(Session p_session) {
		return HibernateConnector.getConnector().loadObjects(p_session, this);
	}
}
