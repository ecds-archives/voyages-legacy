package edu.emory.library.tast.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.HibernateUtil;

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
public class TastDbQuery {

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
	private TastDbConditions conditions;

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
	 * SELECT distinct
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
	public TastDbQuery(String objType) {
		this(objType, new TastDbConditions(TastDbConditions.AND));
	}
	
	/**
	 * Constructor. Will use empty conditions.
	 * @param objType object type
	 */
	public TastDbQuery(String [] objTypes, String[] aliases) {
		this(objTypes, aliases, new TastDbConditions(TastDbConditions.AND));
	}

	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 */
	public TastDbQuery(String objType, TastDbConditions cond) {
		this(new String[] {objType}, new String[] {}, cond, LIMIT_NO_LIMIT);
	}
	
	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 */
	public TastDbQuery(String [] objTypes, String [] aliases, TastDbConditions cond) {
		this(objTypes, aliases, cond, LIMIT_NO_LIMIT);
	}

	/**
	 * Constructor.
	 * @param objType
	 * @param cond
	 * @param limit
	 */
	public TastDbQuery(String [] objTypes, String [] aliases, TastDbConditions cond, int limit) {
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
		}
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
		ConditionResponse response = this.conditions.createWhereForHQL(this.bindings);
		if (!response.conditionString.toString().trim().equals("")) {
			buf.append(" where ").append(response.conditionString);
		}
		allProperties.putAll(response.parameters);

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
		res.parameters = allProperties;
		return res;
	}
	
	/**
	 * Creates SQL query for Hibernate. This is not a general purpose functionality.
	 * It has been created only for. .. 
	 * @return ConditionResponse object with HQL parametrised query and map of parameters.
	 */
	public ConditionResponse toSQLStringWithParams()
	{
		
		if (objects == null)
		{
			throw new RuntimeException("objects is null");			
		}		
		
		if (objects.length != 1)
		{
			throw new RuntimeException("more than one objects not supported");			
		}
		
		// master table and master alias
		String masterTable = HibernateUtil.getTableName("edu.emory.library.tast.dm." + objects[0]);

		// indexes of tables to avoid duplicates
		Map tablesIndexes = new HashMap();
		
		// the list of already existing joins (to avoid unnecessary number of them)
		Map existingJoins = new HashMap(); 
		
		// start the FROM part
		StringBuffer sqlFrom = new StringBuffer();
		sqlFrom.append(masterTable).append("\n");
		
		// start the SELECT part
		int selectItemsCount = 0;
		StringBuffer sqlSelect = new StringBuffer();

		// generate the SELECT part
		if (this.populateValues != null)
		{
			int colIdx = 0;
			for (Iterator iterator = populateValues.iterator(); iterator.hasNext();)
			{
				Attribute attr = (Attribute) iterator.next();
				if (selectItemsCount > 0) sqlSelect.append(",\n    ");
				sqlSelect.append(attr.getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
				sqlSelect.append(" AS col_").append(colIdx++);
				selectItemsCount++;
			}
		}
		
		// start the WHERE part
		StringBuffer sqlWhere = new StringBuffer();
		HashMap parameters = null;
		if (!conditions.isEmpty())
		{
			ConditionResponse whereRes = conditions.createWhereForSQL(masterTable, tablesIndexes, existingJoins, sqlFrom);
			sqlWhere = whereRes.conditionString;
			parameters = whereRes.parameters;
		}
		
		// group by
		StringBuffer sqlGroupBy = new StringBuffer();
		if (groupBy != null && groupBy.length != 0)
		{
			for (int i = 0; i < groupBy.length; i++)
			{
				if (i > 0) sqlGroupBy.append(",\n    ");
				sqlGroupBy.append(groupBy[i].getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
			}
		}
		
		// generate the ORDER BY
		StringBuffer sqlOrderBy = new StringBuffer();
		if (orderBy != null && orderBy.length != 0 && order != ORDER_DEFAULT)
		{
			for (int i = 0; i < orderBy.length; i++)
			{
				if (i > 0) sqlOrderBy.append(",\n    ");
				sqlOrderBy.append(orderBy[i].getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
				sqlOrderBy.append(" ").append(order == ORDER_ASC ? "ASC" : "DESC");
			}
		}
		
		// the entire query
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT\n    ").append(sqlSelect).append("\n");
		sql.append("FROM\n    ").append(sqlFrom);
		if (sqlWhere.length() > 0) sql.append("WHERE ").append(sqlWhere).append("\n");
		if (sqlGroupBy.length() > 0) sql.append("GROUP BY ").append(sqlGroupBy).append("\n");
		if (sqlOrderBy.length() > 0) sql.append("ORDER BY ").append(sqlOrderBy).append("\n");
		if (this.limit != LIMIT_NO_LIMIT) sql.append("LIMIT ").append(this.limit).append("\n");
		if (this.limit != FIRST_NO_FIRST) sql.append("OFFSET ").append(this.firstResult);
		
		//Prepare return value
		ConditionResponse res = new ConditionResponse();
		res.conditionString = sql;
		res.parameters = parameters;
		return res;
		
	}
	
	public Query getQuery(Session session)
	{
		return getQuery(session, false);
	}

	public Query getQuery(Session session, boolean useSQL)
	{
		
		ConditionResponse response;
		Query query;
		
		if (!useSQL)
		{
			response = toStringWithParams();
			query = session.createQuery(response.conditionString.toString());
		}
		else
		{
			response = toSQLStringWithParams();
			query = session.createSQLQuery(response.conditionString.toString());
		}
		
		Iterator iter = response.parameters.keySet().iterator();
		while (iter.hasNext())
		{
			String param = iter.next().toString();
			query.setParameter(param, response.parameters.get(param));
		}
		
		if (!useSQL)
		{

			if (this.limit != LIMIT_NO_LIMIT)
			{
				query.setMaxResults(this.limit);
			}
			
			if (this.firstResult != FIRST_NO_FIRST)
			{
				query.setFirstResult(this.firstResult);
			}
			
		}
		
		query.setCacheable(this.isCacheable());	
		
		return query;

	}
	
	public Object[] executeQuery()
	{
		return executeQuery(false);
	}
	
	public Object[] executeQuery(boolean usqSQL)
	{
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Object[] ret = executeQuery(session, usqSQL);
		transaction.commit();
		session.close();
		return ret;
	}	

	public Object[] executeQuery(Session session)
	{
		return executeQuery(session, false);
	}
	
	public Object[] executeQuery(Session session, boolean useSQL)
	{
		List list = executeQueryList(session, useSQL);
		if (list.size() != 0)
		{
			return list.toArray();
		}
		else
		{
			return new Object[] {};
		}
	}
	
	public List executeQueryList()
	{
		return executeQueryList(false);
	}

	public List executeQueryList(boolean usqSQL)
	{
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		List list = executeQueryList(session, usqSQL);
		transaction.commit();
		session.close();
		return list;
	}
	
	public List executeQueryList(Session session)
	{
		return this.getQuery(session).list();
	}

	public List executeQueryList(Session session, boolean useSQL)
	{
		return this.getQuery(session, useSQL).list();
	}

	public ScrollableResults executeScrollableQuery(Session session)
	{
		ScrollableResults list = this.getQuery(session).scroll();
		return list;
	}

	public boolean isCacheable()
	{
		return cacheable;
	}

	public void setCacheable(boolean cacheable)
	{
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

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public void setFirstResult(int first)
	{
		this.firstResult = first;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public void setGroupBy(Attribute[] groupBy)
	{
		this.groupBy = groupBy;
	}

	public void setOrderBy(Attribute[] orderBy)
	{
		this.orderBy = orderBy;
	}

	public void setConditions(TastDbConditions cond)
	{
		this.conditions = cond;
	}

	public Attribute[] getPopulatedAttributes()
	{
		return (Attribute[]) this.populateValues.toArray(new Attribute[] {});
	}

}
