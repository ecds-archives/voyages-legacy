package edu.emory.library.tast.util.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
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
	 * Objects type that will be quered.
	 */
	private String[] objects;
	
	/**
	 * Aliases of objects.
	 */
	private Map bindings = new HashMap();

	/**
	 * Conditions for query.
	 */
	private Conditions conditions;

	/**
	 * Group by expression.
	 */
	private Attribute[] groupBy = null;

	/**
	 * Order by expression.
	 */
	private Attribute[] orderBy = null;

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
	 * SELECT distincs
	 */
	private boolean distinct = false;

	/**
	 * Array of Populated attributes.
	 */
	private ArrayList populateValues = null;

	/**
	 * Constructor. Will use empty conditions.
	 * @param objType object type
	 */
	public QueryValue(String objType) {
		this(objType, new Conditions(Conditions.JOIN_AND));
	}
	
	/**
	 * Constructor. Will use empty conditions.
	 * @param objType object type
	 */
	public QueryValue(String [] objTypes, String[] aliases) {
		this(objTypes, aliases, new Conditions(Conditions.JOIN_AND));
	}

	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 */
	public QueryValue(String objType, Conditions cond) {
		this(new String[] {objType}, new String[] {}, cond, LIMIT_NO_LIMIT);
	}
	
	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 */
	public QueryValue(String [] objTypes, String [] aliases, Conditions cond) {
		this(objTypes, aliases, cond, LIMIT_NO_LIMIT);
	}

	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 * @param limit
	 */
	public QueryValue(String [] objTypes, String [] aliases, Conditions cond, int limit) {
		if (aliases.length == 0) {
			aliases = new String[objTypes.length];
			for (int i = 0; i < aliases.length; i++) {
				String alias = null;
				if (objTypes[i].indexOf(".") == -1) {
					alias = objTypes[i];
				} else {
					alias = objTypes[i].substring(1 + objTypes[i].lastIndexOf("."));
				}
				aliases[i] = alias.toLowerCase() + "_" + i;
			}
		}
		this.objects = objTypes;
		this.conditions = cond;
		this.limit = limit;
		this.firstResult = FIRST_NO_FIRST;
		for (int i = 0; i < aliases.length; i++) {
			this.bindings.put(objTypes[i], aliases[i]);
//			System.out.println(objects[i].toString()+"*****DEBUG******"+aliases[i]);
		}
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
	public void setGroupBy(Attribute[] groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * Sets order by columns.
	 * @param orderBy
	 */
	public void setOrderBy(Attribute[] orderBy) {
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
			if (distinct) buf.append("distinct ");
			boolean first = true;
			Iterator iter = this.populateValues.iterator();
			while (iter.hasNext()) {
				Attribute attr = (Attribute) iter.next();
				if (!first) {
					buf.append(",");
				}
				first = false;
				buf.append(attr.getHQLSelectPath(bindings)).append(" ");
				if (attr.isOuterjoinable()) {
					fetchClause.append(" left outer join ").append(attr.getHQLOuterJoinPath(bindings));
				}
			}
		}
		if (orderBy != null)
		{
			for (int i = 0; i < orderBy.length; i++)
			{
				Attribute attr = orderBy[i];
				if (attr.isOuterjoinable()) {
					fetchClause.append(" left outer join ").append(attr.getHQLOuterJoinPath(bindings));
				}
			}
		}
		
		HashMap allProperties = new HashMap();
		
		buf.append("from ");
		for (int  i = 0; i < this.objects.length; i++) {
		//Set from clause - adds object type and left outer join part prepared above
			if (i > 0) {
				buf.append(", ");
			}
			buf.append(this.objects[i]).append(" ");
			if (this.bindings.get(this.objects[i]) != null) {
				buf.append("as ").append(this.bindings.get(objects[i])).append(" ");
			}
		}
		buf.append(fetchClause);
		

		//Create where clause
		ConditionResponse response = this.conditions.getConditionHQL(this.bindings);
		if (!response.conditionString.toString().trim().equals("")) {
			buf.append(" where ").append(response.conditionString);
		}
		allProperties.putAll(response.properties);

		//Set group by clause
		if (groupBy != null) {
			StringBuffer groupBuf = new StringBuffer();
			for (int i = 0; i < this.groupBy.length; i++) {
				groupBuf.append(groupBy[i].getHQLSelectPath(bindings));
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
					orderBuf.append(this.orderBy[i].getHQLSelectPath(bindings));
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
	public void addPopulatedAttribute(Attribute p_attr) {
		if (this.populateValues == null) {
			this.populateValues = new ArrayList();
		}
		this.populateValues.add(p_attr);
	}

	/**
	 * Gets HQL query.
	 * @param session session context.
	 * @return
	 */
	public Query getQuery(Session session) {
		//Get HQL query
		ConditionResponse response = toStringWithParams();
		
		//System.out.println(response.conditionString.toString());
		
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
	 * @return query results
	 */
	public List executeQueryList() {
		return HibernateConnector.getConnector().loadObjectList(this);
	}
	
	/**
	 * Executes query.
	 * @return query results
	 */
	public List executeQueryList(Session p_session) {
		return HibernateConnector.getConnector().loadObjectList(p_session, this);
	}

	/**
	 * Executes query.
	 * @param p_session session context
	 * @return query results
	 */
	public Object[] executeQuery(Session p_session) {
		return HibernateConnector.getConnector().loadObjects(p_session, this);
	}
	
	public ScrollableResults executeScrollableQuery(Session p_session) {
		return HibernateConnector.getConnector().loadScroll(p_session, this);
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

	public boolean isDistinct()
	{
		return distinct;
	}

	public void setDistinct(boolean distinct)
	{
		this.distinct = distinct;
	}

	public Attribute[] getPopulatedAttributes() {
		return (Attribute[]) this.populateValues.toArray(new Attribute[] {});
	}
}
