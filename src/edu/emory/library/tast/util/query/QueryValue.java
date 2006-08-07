package edu.emory.library.tast.util.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tast.util.HibernateConnector;

/**
 * Class that represents query built for hibernate.
 * An example usage/results:
 * 
 * Example 1:
 * -->Conditions
 * Conditions c = new Conditions();
 * QueryValue qValue = new QueryValue("Configuration", c);
 * -->QueryValue
 * from Configuration
 * -->HQL
 * select configurat0_.id as id5_, configurat0_.map_entries as map2_5_ from configurations configurat0_
 * 
 * Example 2:
 * -->Conditions
 * Conditions cMain = new Conditions(Conditions.JOIN_AND);
 * cMain.addCondition("vi.voyageId", new Long(104), Conditions.OP_SMALLER_OR_EQUAL);
 * cMain.addCondition(VoyageIndex.getRecent());
 * cMain.addCondition("vi.remoteVoyageId", new DirectValue("v.id"), Conditions.OP_EQUALS);
 * QueryValue qValue = new QueryValue("VoyageIndex as vi, Voyage v", cMain);
 * qValue.addPopulatedAttribute("sum(v.voyage)", false);
 * qValue.setGroupBy(new String[] {"v.datedep"});
 * -->QueryValue
 * select sum(v.voyage) 
 * from VoyageIndex as vi, Voyage v 
 * where vi.voyageId <=  :vivoyageId_494255733104 and
 *           vi.remoteVoyageId = v.id and
 *           (latest =  :latest_11098809531)
 * group by v.datedep
 * -->HQL
 * select sum(voyage1_.voyage) as col_0_0_
 * from voyages_index voyageinde0_, voyages voyage1_
 * where voyageinde0_.vid<=? and voyageinde0_.r_voyage_iid=voyage1_.iid and latest=?
 * group by voyage1_.datedep 
 * 
 * @author Pawel Jurczyk
 *
 */
public class QueryValue {

	public static final int LIMIT_NO_LIMIT = -1;

	public static final int FIRST_NO_FIRST = -1;

	public static final int ORDER_DEFAULT = 0;

	public static final int ORDER_ASC = 1;

	public static final int ORDER_DESC = -1;

	/**
	 * Object type that will be quered.
	 */
	private String object;

	/**
	 * Nested query value (not used currently).
	 */
	private QueryValue qValue;

	/**
	 * Alias for sub query (not used currently).
	 */
	private String alias;

	/**
	 * Conditions for query.
	 */
	private Conditions conditions;

	/**
	 * Group by expression.
	 */
	private String[] groupBy = null;

	/**
	 * Order by expression.
	 */
	private String[] orderBy = null;

	/**
	 * Deired order.
	 */
	private int order;

	/**
	 * Max number of results.
	 */
	private int limit;

	/**
	 * First result row.
	 */
	private int firstResult;

	/**
	 * Information abour query cacheability.
	 */
	private boolean cacheable = false;

	/**
	 * Array of Populated attributes.
	 */
	private ArrayList populateValues = null;

	/**
	 * Array telling which of populated attributes are dictionaries. 
	 */
	private ArrayList populateValuesDictInfo = null;

	/**
	 * Constructor. Will use empty conditions.
	 * @param objType object type
	 */
	public QueryValue(String objType) {
		this(objType, new Conditions(Conditions.JOIN_AND));
	}

	/**
	 * Constructor. Will use empty conditions.
	 * @param qValue
	 * @param alias
	 */
	public QueryValue(QueryValue qValue, String alias) {
		this(qValue, alias, new Conditions(Conditions.JOIN_AND));
	}

	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 */
	public QueryValue(String objType, Conditions cond) {
		this(objType, cond, LIMIT_NO_LIMIT);
	}

	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 * @param limit
	 */
	public QueryValue(String objType, Conditions cond, int limit) {
		this.object = objType;
		this.conditions = cond;
		this.limit = limit;
		this.firstResult = FIRST_NO_FIRST;
	}

	/**
	 * Constructor.
	 * @param value
	 * @param alias
	 * @param conditions2
	 */
	public QueryValue(QueryValue value, String alias, Conditions conditions2) {
		this.conditions = conditions2;
		this.qValue = value;
		this.alias = alias;
	}

	/**
	 * Sets limit of result.
	 * @param limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Sets first retrieved row.
	 * @param first
	 */
	public void setFirstResult(int first) {
		this.firstResult = first;
	}

	/**
	 * Sets order.
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Sets group by columns.
	 * @param groupBy
	 */
	public void setGroupBy(String[] groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * Sets order by columns.
	 * @param orderBy
	 */
	public void setOrderBy(String[] orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * Sets cinditions used in query.
	 * @param cond
	 */
	public void setConditions(Conditions cond) {
		this.conditions = cond;
	}

	/**
	 * Creates HQL query.
	 * @return ConditionResponse object with HQL parametrized query and map of parameters.
	 */
	public ConditionResponse toStringWithParams() {
		StringBuffer buf = new StringBuffer();
		StringBuffer fetchClause = new StringBuffer();
		
		//Prepare select part and left outer join part (for dictionaries)
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

		//Set from clause - adds object type and left outer join part prepared above
		HashMap allProperties = new HashMap();
		if (this.object != null) {
			buf.append("from ").append(this.object).append(" ").append(fetchClause);
		} else {
			ConditionResponse response = this.qValue.toStringWithParams();
			buf.append("from (").append(response.conditionString).append(") as ").append(this.alias);
			allProperties.putAll(response.properties);
		}

		//Create where clause
		ConditionResponse response = this.conditions.getConditionHQL();
		if (!response.conditionString.toString().trim().equals("")) {
			buf.append(" where ").append(response.conditionString);
		}
		allProperties.putAll(response.properties);

		//Set group by clause
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
		
		//Set order by clause
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

		//Prepare return value
		ConditionResponse res = new ConditionResponse();
		res.conditionString = buf;
		res.properties = allProperties;
		return res;
	}

	/**
	 * Adds new populated attribute.
	 * @param p_attrName attribute name
	 * @param dictionary true  if attribute is dictionary
	 */
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

	/**
	 * Gets HQL query.
	 * @param session session context.
	 * @return
	 */
	public Query getQuery(Session session) {
		//Get HQL query
		ConditionResponse response = toStringWithParams();
		Query q = session.createQuery(response.conditionString.toString());

		//Set properties of query
		Iterator iter = response.properties.keySet().iterator();
		while (iter.hasNext()) {
			String param = iter.next().toString();
			q.setParameter(param, response.properties.get(param));
		}

		//Set bounds of query results.
		if (this.limit != LIMIT_NO_LIMIT) {
			q.setMaxResults(this.limit);
		}
		if (this.firstResult != FIRST_NO_FIRST) {
			q.setFirstResult(this.firstResult);
		}
		
		//Set cache info
		q.setCacheable(this.isCacheable());

		return q;
	}

	/**
	 * Executes query.
	 * @return query results
	 */
	public Object[] executeQuery() {
		return HibernateConnector.getConnector().loadObjects(this);
	}

	/**
	 * Executes query.
	 * @param p_session session context
	 * @return query results
	 */
	public Object[] executeQuery(Session p_session) {
		return HibernateConnector.getConnector().loadObjects(p_session, this);
	}

	/**
	 * Sets cache usage for query cache.
	 * @return
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * Checks query useage for query cache.
	 * @param cacheable
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}
}
