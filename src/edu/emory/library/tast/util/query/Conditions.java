package edu.emory.library.tast.util.query;

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
public class Conditions {

	/**
	 * JOIN_ operators are used as constructor of Conditions class. It tells how
	 * to connect attributes/subconditions.
	 */
	public static final int JOIN_AND = 1;

	public static final int JOIN_OR = 2;

	public static final int JOIN_NOT = 0;

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
	
	public static final int OP_ILIKE = 11;

	public static final int OP_IS = 8;

	public static final int OP_IS_NOT = 9;

	public static final int OP_IN = 10;

	/**
	 * Operator used to join conditions/subconditions
	 */
	private int joinCondition = JOIN_AND;

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
	private class Condition {

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
		public Condition(Attribute attr, String op, Object val) {
			this.op = op;
			this.value = val;
			this.attribute = attr;
		}

	}

	// /**
	// * Gets attribute from given expression (expression can be e.g. SQL
	// function.)
	// * @param exp expression containing attribute
	// * @return attribute name
	// */
	// private String getAttribute(String attributeString) {
	//		
	// //Use appropriate dialect - the same as hibernate
	// Dialect dialect =
	// ((SessionFactoryImpl)HibernateUtil.getSessionFactory()).getSettings().getDialect();
	//		
	// //Check registered functions
	// Map functions = dialect.getFunctions();
	// for (Iterator iter = functions.values().iterator(); iter.hasNext();) {
	// SQLFunction element = (SQLFunction) iter.next();
	// if (attributeString.startsWith(element.toString())) {
	// //If we have SQL function, extract attribute
	// return attributeString.substring(attributeString.indexOf(",") + 1,
	// attributeString.indexOf(")")).trim();
	// }
	// }
	//		
	// //No function - passed just attribute
	// return attributeString;
	// }

	/**
	 * Constructor. Uses by default JOIN_AND.
	 * 
	 */
	public Conditions() {

		this.joinCondition = JOIN_AND;

	}

	/**
	 * Constructor using desired JOIN operator.
	 * 
	 * @param joinCondition
	 *            join operator.
	 */
	public Conditions(int joinCondition) {
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
	public void addCondition(Attribute attrName, Object value, int op) {
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
			opStr = " like ";
			break;
		case OP_ILIKE:
			opStr = " ilike ";
			break;
		case OP_IS:
			opStr = " is ";
			break;
		case OP_IS_NOT:
			opStr = " is not ";
			break;
		case OP_IN:
			opStr = " in ";
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
	public void addCondition(Conditions subCond) {
		this.subConditions.add(subCond);
	}

	/**
	 * Gets HQL for conditions.
	 * 
	 * @return ConditionResponse object.
	 */
	public ConditionResponse getConditionHQL(Map bindings) {

		// Check the number of items.
		int size = this.conditions.size() + this.subConditions.size();

		if (this.joinCondition == JOIN_NOT && size != 1) {
			throw new RuntimeException(
					"With JOIN_NOT only one condition allowable!");
		}

		int processed = 0;
		StringBuffer ret = new StringBuffer();

		// Handle not
		if (this.joinCondition == JOIN_NOT) {
			ret.append("not (");
		}

		HashMap retMap = new HashMap();

		// Handle simple conditions.
		Iterator iter = this.conditions.iterator();
		while (iter.hasNext()) {
			Condition c = (Condition) iter.next();
			if (c.value == null) {
				// Handle null request
				String attr = c.attribute.getHQLWherePath(bindings);
				processed++;
				ret.append(attr);
				ret.append(c.op);
				ret.append("null");
			} else if (c.op.equals(" in ")) {
				// Handle in request
				Attribute attr = c.attribute;
				String val = (attr.getHQLParamName() + attr.hashCode() + c.value
						.hashCode()).replace('-', '_');
				if (c.value instanceof Object[]) {
					Object[] values = (Object[]) c.value;
					processed++;
					ret.append(attr.getHQLWherePath(bindings));
					ret.append(c.op);
					ret.append("(");
					for (int i = 0; i < values.length; i++) {
						ret.append(" :");
						ret.append(val + "_" + i);
						retMap.put(val + "_" + i, attr
								.getValueToCondition(values[i]));
						if (i < values.length - 1) {
							ret.append(", ");
						}
					}
					ret.append(") ");
				} else {
					Attribute attribute1 = (Attribute) c.attribute;
					Attribute attribute2 = (Attribute) c.value;
					processed++;
					ret.append(attribute1.getHQLWherePath(bindings));
					ret.append(c.op);
					ret.append(attribute2.getHQLWherePath(bindings));
				}
			} else if (!(c.value instanceof DirectValue)) {
				// Handle anything except direct value - avoid using :param
				// notation.
				Attribute attr = c.attribute;
				String val = (attr.getHQLParamName() + attr.hashCode() + c.value
						.hashCode()).replace('-', '_');
				Object value = c.value;
				processed++;
				ret.append(attr.getHQLWherePath(bindings));
				ret.append(c.op);
				ret.append(" :");
				ret.append(val);
				retMap.put(val, attr.getValueToCondition(value));
			} else {
				// Handle direct value - avoid using :param notation.
				processed++;
				ret.append(c.attribute.getHQLWherePath(bindings));
				ret.append(c.op);
				ret.append(((DirectValue) c.value).toString(bindings));
			}
			if (processed < size) {
				ret.append(this.joinCondition == JOIN_AND ? " and " : " or ");
			}
		}

		// Handle subconditions
		iter = this.subConditions.iterator();
		while (iter.hasNext()) {
			processed++;
			ConditionResponse child = ((Conditions) iter.next())
					.getConditionHQL(bindings);
			ret.append("(").append(child.conditionString).append(")");
			retMap.putAll(child.properties);
			if (processed < size) {
				ret.append(this.joinCondition == JOIN_AND ? " and " : " or ");
			}
		}

		if (this.joinCondition == JOIN_NOT) {
			ret.append(")");
		}

		// Return result.
		ConditionResponse res = new ConditionResponse();
		res.conditionString = ret;
		res.properties = retMap;
		return res;
	}

	/**
	 * Equals implementation. Two conditions are equal if the have the same set
	 * of subconditions, attributes and are joined using the same join operator.
	 */
	public boolean equals(Object o) {
		if (o instanceof Conditions) {
			Conditions that = (Conditions) o;
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
		Conditions newC = new Conditions();
		newC.conditions = (ArrayList) this.conditions.clone();
		newC.joinCondition = this.joinCondition;
		newC.subConditions = (ArrayList) this.subConditions.clone();
		return newC;
	}

	// /**
	// * Creates new Conditions object. It has the same subconditions but all
	// the attributes
	// * have added specific prefix.
	// */
	// public Conditions addAttributesPrefix(String prefix) {
	// Conditions newC = new Conditions();
	// ArrayList conditions = new ArrayList();
	// //Handle simple conditions
	// for (Iterator iter = this.conditions.iterator(); iter.hasNext();) {
	// Condition condition = (Condition) iter.next();
	// Condition newCondition;
	// if (condition.attribute.startsWith("date_")) {
	// String attribute = getAttribute(condition.attribute);
	// newCondition = new Condition(condition.attribute.replaceAll(attribute,
	// prefix + attribute), condition.op, condition.value);
	// } else {
	// newCondition = new Condition(prefix + condition.attribute, condition.op,
	// condition.value);
	// }
	// conditions.add(newCondition);
	// }
	// //Handle subconditions.
	// ArrayList newSubconditions = new ArrayList();
	// for (Iterator iter = this.subConditions.iterator(); iter.hasNext();) {
	// Conditions subConditions = (Conditions) iter.next();
	// newSubconditions.add(subConditions.addAttributesPrefix(prefix));
	// }
	//		
	// //Set values in new query.
	// newC.conditions = conditions;
	// newC.subConditions = newSubconditions;
	// newC.joinCondition = this.joinCondition;
	// return newC;
	// }

	/**
	 * Gets String representation of query.
	 */
	public String toString() {
		ConditionResponse response = this.getConditionHQL(new HashMap());
		String out = response.conditionString.toString();
		Iterator iter = response.properties.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			// System.out.println("Replacing " + key + " by " +
			// response.properties.get(key).toString());
			out = out.replaceAll(":" + key, response.properties.get(key)
					.toString());
		}
		return out;
	}

	public boolean isEmpty() {
		return this.conditions.isEmpty() && this.subConditions.isEmpty();
	}

	// /**
	// * Gets all attributes that are present in conditions.
	// * @return
	// */
	// public List getConditionedAttributes() {
	// ArrayList list = new ArrayList();
	// for (Iterator iter = this.conditions.iterator(); iter.hasNext();) {
	// Condition element = (Condition) iter.next();
	// list.add(element.attribute);
	// }
	// for (Iterator iter = this.subConditions.iterator(); iter.hasNext();) {
	// Conditions element = (Conditions) iter.next();
	// list.addAll(element.getConditionedAttributes());
	// }
	// return list;
	// }
}
