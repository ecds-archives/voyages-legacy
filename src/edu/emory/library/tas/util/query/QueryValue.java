package edu.emory.library.tas.util.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tas.util.HibernateConnector;

/**
 * 
 * @author juri
 *
 */
public class QueryValue {

	public static final int LIMIT_NO_LIMIT = -1;

	public static final int FIRST_NO_FIRST = -1;

	public static final int ORDER_DEFAULT = 0;

	public static final int ORDER_ASC = 1;

	public static final int ORDER_DESC = -1;

	private String object;

	private QueryValue qValue;

	private String alias;

	private Conditions conditions;

	private String[] groupBy = null;

	private String[] orderBy = null;

	private int order;

	private int limit;

	private int firstResult;

	private boolean cacheable = false;

	private ArrayList populateValues = null;

	private ArrayList populateValuesDictInfo = null;

	public QueryValue(String objType) {
		this(objType, new Conditions(Conditions.JOIN_AND));
	}

	public QueryValue(QueryValue qValue, String alias) {
		this(qValue, alias, new Conditions(Conditions.JOIN_AND));
	}

	public QueryValue(String objType, Conditions cond) {
		this(objType, cond, LIMIT_NO_LIMIT);
	}

	public QueryValue(String objType, Conditions cond, int limit) {
		this.object = objType;
		this.conditions = cond;
		this.limit = limit;
		this.firstResult = FIRST_NO_FIRST;
	}

	public QueryValue(QueryValue value, String alias, Conditions conditions2) {
		this.conditions = conditions2;
		this.qValue = value;
		this.alias = alias;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setFirstResult(int first) {
		this.firstResult = first;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setGroupBy(String[] groupBy) {
		this.groupBy = groupBy;
	}

	public void setOrderBy(String[] orderBy) {
		this.orderBy = orderBy;
	}

	public void setConditions(Conditions cond) {
		this.conditions = cond;
	}

	public ConditionResponse toStringWithParams() {
		StringBuffer buf = new StringBuffer();
		StringBuffer fetchClause = new StringBuffer();
		if (this.populateValues != null) {
			buf.append("select ");
			boolean first = true;
			Iterator iter = this.populateValues.iterator();
			Iterator iterInfo = this.populateValuesDictInfo.iterator();
			while (iter.hasNext() && iterInfo.hasNext()) {
				String attr = (String) iter.next();
				if (!first) {
					buf.append(",");
				}
				first = false;
				buf.append(attr).append(" ");
				if (((Boolean) iterInfo.next()).booleanValue()) {
					fetchClause.append(" left outer join ").append(attr);
				}
			}
		}

		HashMap allProperties = new HashMap();
		if (this.object != null) {
			buf.append("from ").append(this.object).append(" ").append(fetchClause);
		} else {
			ConditionResponse response = this.qValue.toStringWithParams();
			buf.append("from (").append(response.conditionString).append(") as ").append(this.alias);
			allProperties.putAll(response.properties);
		}

		ConditionResponse response = this.conditions.getConditionHQL();
		if (!response.conditionString.toString().trim().equals("")) {
			buf.append(" where ").append(response.conditionString);
		}
		allProperties.putAll(response.properties);

		if (groupBy != null) {
			StringBuffer groupBuf = new StringBuffer();
			for (int i = 0; i < this.groupBy.length; i++) {
				groupBuf.append(groupBy[i]);
				if (i < this.groupBy.length - 1) {
					groupBuf.append(", ");
				}
			}
			if (!groupBuf.toString().trim().equals("")) {
				buf.append(" group by ").append(groupBuf);
			}
		}
		
		if (orderBy != null) {
			StringBuffer orderBuf = new StringBuffer();
			if (order != ORDER_DEFAULT) {
				for (int i = 0; i < this.orderBy.length; i++) {
					orderBuf.append(this.orderBy[i]);
					if (order == ORDER_ASC) {
						orderBuf.append(" asc");
					} else {
						orderBuf.append(" desc");
					}
					if (i < this.orderBy.length - 1) {
						orderBuf.append(", ");
					}
				}
			}
			if (!orderBuf.toString().trim().equals("")) {
				buf.append(" order by ").append(orderBuf);
			}
		}

		ConditionResponse res = new ConditionResponse();
		res.conditionString = buf;
		res.properties = allProperties;
		return res;
	}

	public void addPopulatedAttribute(String p_attrName, boolean dictionary) {
		if (this.populateValues == null) {
			this.populateValues = new ArrayList();
		}
		if (this.populateValuesDictInfo == null) {
			this.populateValuesDictInfo = new ArrayList();
		}
		this.populateValues.add(p_attrName);
		this.populateValuesDictInfo.add(new Boolean(dictionary));
	}

	public Query getQuery(Session session) {
		ConditionResponse response = toStringWithParams();
		// System.out.println("My query: " + response.conditionString);
		Query q = session.createQuery(response.conditionString.toString());

		Iterator iter = response.properties.keySet().iterator();
		while (iter.hasNext()) {
			String param = iter.next().toString();
			q.setParameter(param, response.properties.get(param));
		}

		if (this.limit != LIMIT_NO_LIMIT) {
			q.setMaxResults(this.limit);
		}
		if (this.firstResult != FIRST_NO_FIRST) {
			q.setFirstResult(this.firstResult);
		}
		q.setCacheable(this.isCacheable());

		return q;
	}

	public Object[] executeQuery() {
		return HibernateConnector.getConnector().loadObjects(this);
	}

	public Object[] executeQuery(Session p_session) {
		return HibernateConnector.getConnector().loadObjects(p_session, this);
	}

	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
}
