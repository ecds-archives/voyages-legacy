package edu.emory.library.tast.maps;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.util.query.QueryValue;

public abstract class AbstractTransformerQueryHolder {
	
	private List queryLabels = new ArrayList();
	private int executedQuery;
	private List queries = new ArrayList();
	private AttributesMap map = null;
	
	private Object[] rawQueryResponse = null;
	
	public AbstractTransformerQueryHolder() {		
	}
	
	protected void addQuery(String userLabel, QueryValue[] querySet) {
		this.queryLabels.add(userLabel);
		this.queries.add(querySet);
	}
	
	public final void executeQuery(Session session, int queryNumber, int type) {
		if (queryNumber > queries.size()) {
			throw new RuntimeException("Query number out of range!");
		}
		this.executedQuery = queryNumber;
		performExecuteQuery(session, (QueryValue[])this.queries.get(queryNumber), type);
	}
	
	protected abstract void performExecuteQuery(Session session, QueryValue[] querySet, int type);

	public String[] getQueryLabels() {
		return (String[])this.queryLabels.toArray(new String[] {});
	}

	public int getExecutedQuery() {
		return executedQuery;
	}

	public Object[] getRawQueryResponse() {
		return rawQueryResponse;
	}

	protected void setRawQueryResponse(Object[] rawQueryResponse) {
		this.rawQueryResponse = rawQueryResponse;
	}

	public AttributesMap getAttributesMap() {
		return map;
	}
	
	protected void setAttributesMap(AttributesMap map) {
		this.map = map;
	}
}
