package edu.emory.library.tast.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;

/**
 * Class that represents any conditions for query. It helps to build conditions.
 * It can contain either simple conditions (attribute, value, operator) or can
 * have another level of Conditions. You can build as many levels as you want.
 * 
 * @author Pawel Jurczyk
 * 
 */
public class TastDbConditions {

	/**
	 * JOIN_ operators are used as constructor of Conditions class. It tells how
	 * to connect attributes/subconditions.
	 */
	public static final int AND = 1;
	public static final int OR = 2;
	public static final int NOT = 0;

	/**
	 * OP_ operators are operators (like == or != etc.)
	 */
	public static final int OP_EQUALS = 1;
	public static final int OP_NOT_EQUALS = 2;
	public static final int OP_GREATER = 3;
	public static final int OP_GREATER_OR_EQUAL = 4;
	public static final int OP_SMALLER = 5;
	public static final int OP_SMALLER_OR_EQUAL = 6;
	public static final int OP_LIKE = 7;
	public static final int OP_ILIKE = 8;
	public static final int OP_IS = 9;
	public static final int OP_IS_NOT = 10;
	public static final int OP_IN = 11;
	public static final int OP_TEXT_SEARCH = 12;

	/**
	 * Operator used to join conditions/subconditions
	 */
	private int joinCondition = AND;

	/**
	 * Array of simple conditions (Keeps Condition objects).
	 */
	private ArrayList conditions = new ArrayList();

	/**
	 * Array of subconditions (keeps Conditions objects).
	 */
	private ArrayList subConditions = new ArrayList();

	/**
	 * Class that represents simple condition. It has attribute in condition,
	 * its value and operator.
	 * 
	 * @author Pawel Jurczyk
	 * 
	 */
	private class Condition
	{

		/**
		 * Operator in condition.
		 */
		public String op;

		/**
		 * Desired value.
		 */
		public Object value;

		/**
		 * Attribute name.
		 */
		public Attribute attribute;

		/**
		 * Constructor of condition.
		 * 
		 * @param attr
		 *            attribute name
		 * @param op
		 *            operator
		 * @param val
		 *            value
		 */
		public Condition(Attribute attr, String op, Object val)
		{
			this.op = op;
			this.value = val;
			this.attribute = attr;
		}

	}
	
	private class NextParamIndex
	{
		public int index = 0;
	}

	/**
	 * Constructor. Uses by default JOIN_AND.
	 * 
	 */
	public TastDbConditions()
	{
		this.joinCondition = AND;
	}

	/**
	 * Constructor using desired JOIN operator.
	 * 
	 * @param joinCondition
	 *            join operator.
	 */
	public TastDbConditions(int joinCondition)
	{
		this.joinCondition = joinCondition;
	}

	/**
	 * Adds simple condition.
	 * 
	 * @param attrName
	 *            attribute
	 * @param value
	 *            value
	 * @param op
	 *            operator
	 */
	public void addCondition(Attribute attrName, Object value, int op)
	{
		String opStr = null;

		// Recognize operator
		switch (op) {
		case OP_EQUALS:
			opStr = " = ";
			break;
		case OP_NOT_EQUALS:
			opStr = " <> ";
			break;
		case OP_GREATER:
			opStr = " > ";
			break;
		case OP_GREATER_OR_EQUAL:
			opStr = " >= ";
			break;
		case OP_SMALLER:
			opStr = " < ";
			break;
		case OP_SMALLER_OR_EQUAL:
			opStr = " <= ";
			break;
		case OP_LIKE:
			opStr = " LIKE ";
			break;
		case OP_ILIKE:
			opStr = " ILIKE ";
			break;
		case OP_IS:
			opStr = " IS ";
			break;
		case OP_IS_NOT:
			opStr = " IS NOT ";
			break;
		case OP_IN:
			opStr = " IN ";
			break;
		case OP_TEXT_SEARCH:
			opStr = " @@ ";
			break;
		default:
			throw new RuntimeException("Wrong operand!");
		}

		// Add simple condition
		this.conditions.add(new Condition(attrName, opStr, value));

	}

	/**
	 * Adds subcondition
	 * 
	 * @param subCond
	 *            subcondition
	 */
	public void addCondition(TastDbConditions subCond)
	{
		this.subConditions.add(subCond);
	}

	public ConditionResponse createWhereForHQL(Map bindings)
	{
		
		NextParamIndex paramIndex = new NextParamIndex();
		paramIndex.index = 0;
		
		return createWhere(true, paramIndex, bindings, null, null, null, null);

	}
	
	public ConditionResponse createWhereForSQL(String masterTable, Map tablesIndexes, Map existingJoins, StringBuffer sqlFrom)
	{
		
		NextParamIndex paramIndex = new NextParamIndex();
		paramIndex.index = 0;
		
		return createWhere(false, paramIndex, new HashMap(), masterTable, existingJoins, tablesIndexes, sqlFrom);

	}

	private ConditionResponse createWhere(boolean hql, NextParamIndex paramIndex, Map bindings, String masterTable, Map existingJoins, Map tablesIndexes, StringBuffer sqlFrom)
	{

		// Check the number of items.
		int size = this.conditions.size() + this.subConditions.size();
		
		if (size == 0)
		{
			ConditionResponse res = new ConditionResponse();
			StringBuffer ret = new StringBuffer();
			ret.append("1 = 1");
			res.conditionString = ret;
			return res;
		}

		if (this.joinCondition == NOT && size != 1)
		{
			throw new RuntimeException("NOT allows only one condition");
		}

		int processed = 0;
		StringBuffer ret = new StringBuffer();

		// Handle not
		if (this.joinCondition == NOT)
		{
			ret.append("NOT (");
		}

		HashMap retMap = new HashMap();

		// Handle simple conditions.
		Iterator iter = this.conditions.iterator();
		while (iter.hasNext())
		{
			Condition c = (Condition) iter.next();
			
			// something OP NULL
			if (c.value == null)
			{
				String attr = hql ? c.attribute.getHQLWherePath(bindings) :  c.attribute.getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom);
				ret.append(attr);
				ret.append(c.op);
				ret.append("NULL");
				processed++;
			}
			
			// something IN (...)
			else if (c.op.equals(" IN "))
			{
				
				Attribute attr = c.attribute;
				String paramName = "p_" + (paramIndex.index ++);

				if (c.value instanceof Object[])
				{
					Object[] values = (Object[]) c.value;
					processed++;
					ret.append(attr.getHQLWherePath(bindings));
					ret.append(" IN ");
					ret.append("(");
					for (int i = 0; i < values.length; i++)
					{
						ret.append(":");
						ret.append(paramName + "_" + i);
						retMap.put(paramName + "_" + i, values[i]);
						if (i < values.length - 1) ret.append(", ");
					}
					ret.append(") ");
				}
				else
				{
					Attribute attribute1 = (Attribute) c.attribute;
					Attribute attribute2 = (Attribute) c.value;
					processed++;
					ret.append(attribute1.getHQLWherePath(bindings));
					ret.append(" IN ");
					ret.append(attribute2.getHQLWherePath(bindings));
				}
			}
			
			// text search
			else if (c.op.equals(" @@ "))
			{

				String paramName = "p_" + (paramIndex.index ++);
				Object value = c.value;
				processed++;
				
				ret.append(hql ? c.attribute.getHQLWherePath(bindings) :  c.attribute.getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
				ret.append(c.op);
				ret.append(" CAST(");
				ret.append(":").append(paramName);
				ret.append(" AS tsquery)");

				retMap.put(paramName, value);

			}
			
			// anything else
			else
			{

				String paramName = "p_" + (paramIndex.index ++);
				Object value = c.value;
				processed++;
				
				ret.append(hql ? c.attribute.getHQLWherePath(bindings) :  c.attribute.getSQLReference(masterTable, tablesIndexes, existingJoins, sqlFrom));
				ret.append(c.op);
				ret.append(":");
				ret.append(paramName);

				retMap.put(paramName, value);

			}
			
			if (processed < size)
			{
				ret.append(this.joinCondition == AND ? " AND " : " OR ");
			}

		}

		// Handle sub-conditions
		iter = this.subConditions.iterator();
		while (iter.hasNext())
		{
			processed++;
			ConditionResponse child = ((TastDbConditions) iter.next()).createWhere(hql, paramIndex, bindings, masterTable, existingJoins, tablesIndexes, sqlFrom);
			ret.append("(").append(child.conditionString).append(")");
			retMap.putAll(child.parameters);
			if (processed < size)
			{
				ret.append(this.joinCondition == AND ? " AND " : " OR ");
			}
		}

		if (this.joinCondition == NOT)
		{
			ret.append(")");
		}

		// return result
		ConditionResponse res = new ConditionResponse();
		res.conditionString = ret;
		res.parameters = retMap;
		return res;

	}

	/**
	 * Equals implementation. Two conditions are equal if the have the same set
	 * of subconditions, attributes and are joined using the same join operator.
	 */
	public boolean equals(Object o) {
		if (o instanceof TastDbConditions) {
			TastDbConditions that = (TastDbConditions) o;
			return (this.conditions.equals(that.conditions)
					&& this.joinCondition == that.joinCondition && this.subConditions
					.equals(that.subConditions));
		}
		return false;
	}

	/**
	 * Clones conditions.
	 */
	public Object clone() {
		TastDbConditions newC = new TastDbConditions();
		newC.conditions = (ArrayList) this.conditions.clone();
		newC.joinCondition = this.joinCondition;
		newC.subConditions = (ArrayList) this.subConditions.clone();
		return newC;
	}

	/**
	 * Gets String representation of query.
	 */
	public String toString() {
		ConditionResponse response = this.createWhereForHQL(new HashMap());
		String out = response.conditionString.toString();
		Iterator iter = response.parameters.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			// System.out.println("Replacing " + key + " by " +
			// response.properties.get(key).toString());
			out = out.replaceAll(":" + key, response.parameters.get(key)
					.toString());
		}
		return out;
	}

	public boolean isEmpty() {
		return this.conditions.isEmpty() && this.subConditions.isEmpty();
	}

}
