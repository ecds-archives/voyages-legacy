package edu.emory.library.tast.maps;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.db.TastDbQuery;

/**
 * Abstract class that holds queries that should be executed for given map implementation.
 * Subclasses create queries and call addQuery method.
 * When query is executed, performExecuteQuery is invoked.
 */
public abstract class AbstractTransformerQueryHolder {
	
	/**
	 * List of query labels - probably not used :)
	 */
	private List queryLabels = new ArrayList();
	
	/**
	 * Currently executed query
	 */
	private int executedQuery;
	
	/**
	 * Registered queries (by addQuery method)
	 */
	private List queries = new ArrayList();
	
	/**
	 * Attributes returned by query - maps [row][column] to given Attribute of queried object.
	 */
	private AttributesMap map = null;
	
	/**
	 * Query response that will be parsed later.
	 */
	private Object[] rawQueryResponse = null;
	
	public AbstractTransformerQueryHolder() {		
	}
	
	/**
	 * Registers new query
	 * @param userLabel
	 * @param querySet
	 */
	protected void addQuery(String userLabel, TastDbQuery[] querySet) {
		this.queryLabels.add(userLabel);
		this.queries.add(querySet);
	}
	
	/**
	 * Executes query
	 * @param session
	 * @param queryNumber
	 * @param type
	 */
	public final void executeQuery(Session session, int queryNumber, int type) {
		if (queryNumber > queries.size()) {
			throw new RuntimeException("Query number out of range!");
		}
		this.executedQuery = queryNumber;
		performExecuteQuery(session, (TastDbQuery[])this.queries.get(queryNumber), type);
	}
	
	/**
	 * Implementation of query execution
	 * @param session
	 * @param querySet
	 * @param type
	 */
	protected abstract void performExecuteQuery(Session session, TastDbQuery[] querySet, int type);

	/**
	 * Gets query labels - problably not used currently.
	 * @return
	 */
	public String[] getQueryLabels() {
		return (String[])this.queryLabels.toArray(new String[] {});
	}

	/**
	 * Gets query that should be executed (or was executed)
	 * @return
	 */
	public int getExecutedQuery() {
		return executedQuery;
	}

	/**
	 * Gets query response 
	 * @return
	 */
	public Object[] getRawQueryResponse() {
		return rawQueryResponse;
	}

	/**
	 * Invoked when query response should be set. Usually called by superclasses.
	 * @param rawQueryResponse
	 */
	protected void setRawQueryResponse(Object[] rawQueryResponse) {
		this.rawQueryResponse = rawQueryResponse;
	}

	/**
	 * Gets attributes mapping in response
	 * @return
	 */
	public AttributesMap getAttributesMap() {
		return map;
	}
	
	/**
	 * Sets attribute mapping. Usually called by superclasses.
	 * @param map
	 */
	protected void setAttributesMap(AttributesMap map) {
		this.map = map;
	}
}
