package edu.emory.library.tas.util.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.impl.SessionFactoryImpl;

import edu.emory.library.tas.util.HibernateUtil;

public class Conditions {

	public static final int JOIN_AND = 1;

	public static final int JOIN_OR = 2;

	public static final int JOIN_NOT = 0;

	public static final int OP_EQUALS = 1;

	public static final int OP_NOT_EQUALS = 2;

	public static final int OP_GREATER = 3;

	public static final int OP_GREATER_OR_EQUAL = 4;

	public static final int OP_SMALLER = 5;

	public static final int OP_SMALLER_OR_EQUAL = 6;

	public static final int OP_LIKE = 7;

	public static final int OP_IS = 8;

	public static final int OP_IS_NOT = 9;

	public static final int OP_IN = 10;

	private int joinCondition = JOIN_AND;

	private ArrayList conditions = new ArrayList();

	private ArrayList subConditions = new ArrayList();

	private class Condition {
		public String op;

		public Object value;

		public String attribute;

		public Condition(String attr, String op, Object val) {
			this.op = op;
			this.value = val;
			this.attribute = attr;
		}

	}
	
	private String getAttribute(String exp) {
		Dialect dialect = 
			((SessionFactoryImpl)HibernateUtil.getSessionFactory()).getSettings().getDialect();
		
		Map functions = dialect.getFunctions();
//		System.out.println("Checking: " + exp);
		for (Iterator iter = functions.values().iterator(); iter.hasNext();) {
			SQLFunction element = (SQLFunction) iter.next();
//			System.out.println("Functon: " + element);
			if (exp.startsWith(element.toString())) {
//				System.out.println("Returning: " + exp.substring(exp.indexOf(",") + 1, exp.indexOf(")")).trim());
				return exp.substring(exp.indexOf(",") + 1, exp.indexOf(")")).trim();
			}
		}  
		
		return exp;
	}

	
	
	
	public Conditions() {
		
		this.joinCondition = JOIN_AND;
		
	}

	public Conditions(int joinCondition) {
		this.joinCondition = joinCondition;
	}

	public void addCondition(String attrName, Object value, int op) {
		String opStr = null;

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
		this.conditions.add(new Condition(attrName, opStr, value));

	}

	public void addCondition(Conditions subCond) {
		this.subConditions.add(subCond);
	}

	public ConditionResponse getConditionHQL() {
		int size = this.conditions.size() + this.subConditions.size();
		if (this.joinCondition == JOIN_NOT && size != 1) {
			throw new RuntimeException(
					"With JOIN_NOT only one condition allowable!");
		}

		int processed = 0;
		StringBuffer ret = new StringBuffer();

		if (this.joinCondition == JOIN_NOT) {
			ret.append("not (");
		}

		HashMap retMap = new HashMap();

		Iterator iter = this.conditions.iterator();
		while (iter.hasNext()) {
			Condition c = (Condition) iter.next();
			if (c.value == null) {
				String attr = this.getAttribute(c.attribute);
				processed++;
				ret.append(attr);
				ret.append(c.op);
				ret.append("null");
			} else if (c.op.equals(" in ")) {
				String attr = c.attribute;
				String val = (this.getAttribute(attr).replaceAll("\\.", "") + attr.hashCode() + c.value.hashCode()).replace('-', '_');
				Object[] values = (Object[])c.value;
				processed++;
				ret.append(attr);
				ret.append(c.op);
				ret.append("(");
				for (int i = 0; i < values.length; i++) {
					ret.append(" :");
					ret.append(val + "_" + i);
					retMap.put(val + "_" + i, values[i]);
					if (i < values.length - 1) {
						ret.append(", ");
					}
				}
				ret.append(") ");
			} else if (!(c.value instanceof DirectValue)) {
				String attr = c.attribute;
				String val = (this.getAttribute(attr).replaceAll("\\.", "") + attr.hashCode() + c.value.hashCode()).replace('-', '_');
				Object value = c.value;
				processed++;
				ret.append(attr);
				ret.append(c.op);
				ret.append(" :");
				ret.append(val);
				retMap.put(val, value);
			} else {
				processed++;
				ret.append(c.attribute);
				ret.append(c.op);
				ret.append(c.value.toString());
			}
			if (processed < size) {
				ret.append(this.joinCondition == JOIN_AND ? " and " : " or ");
			}
		}

		iter = this.subConditions.iterator();
		while (iter.hasNext()) {
			processed++;
			ConditionResponse child = ((Conditions) iter.next())
					.getConditionHQL();
			ret.append("(").append(child.conditionString).append(")");
			retMap.putAll(child.properties);
			if (processed < size) {
				ret.append(this.joinCondition == JOIN_AND ? " and " : " or ");
			}
		}

		if (this.joinCondition == JOIN_NOT) {
			ret.append(")");
		}

		ConditionResponse res = new ConditionResponse();
		res.conditionString = ret;
		res.properties = retMap;
		return res;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Conditions) {
			Conditions that = (Conditions)o;
			return (this.conditions.equals(that.conditions) 
					&& this.joinCondition == that.joinCondition
					&& this.subConditions.equals(that.subConditions));
		}
		return false;
	}
	
	public Object clone() {
		Conditions newC = new Conditions();
		newC.conditions = (ArrayList) this.conditions.clone();
		newC.joinCondition = this.joinCondition;
		newC.subConditions = (ArrayList) this.subConditions.clone();
		return newC;
	}

	public Conditions addAttributesPrefix(String prefix) {
		Conditions newC = new Conditions();
		ArrayList conditions = new ArrayList();
		for (Iterator iter = this.conditions.iterator(); iter.hasNext();) {
			Condition condition = (Condition) iter.next();
			Condition newCondition;
			if (condition.attribute.startsWith("date_")) {
				String attribute = getAttribute(condition.attribute); 
				newCondition = new Condition(condition.attribute.replaceAll(attribute, prefix + attribute), condition.op, condition.value);
			} else {
				newCondition = new Condition(prefix + condition.attribute, condition.op, condition.value);
			}
			conditions.add(newCondition);
		}
		ArrayList newSubconditions = new ArrayList();
		for (Iterator iter = this.subConditions.iterator(); iter.hasNext();) {
			Conditions subConditions = (Conditions) iter.next();
			newSubconditions.add(subConditions.addAttributesPrefix(prefix));			
		}
		newC.conditions = conditions;
		newC.subConditions = newSubconditions;
		newC.joinCondition = this.joinCondition;
		return newC;
	}
	
	public String toString() {
		ConditionResponse response = this.getConditionHQL();
		String out = response.conditionString.toString();
		System.out.println(out);
		Iterator iter = response.properties.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			System.out.println("Replacing " + key + " by " + response.properties.get(key).toString());
			out = out.replaceAll(":" + key, response.properties.get(key).toString());
		}
		return out;
	}
	
	public List getConditionedAttributes() {
		ArrayList list = new ArrayList();
		for (Iterator iter = this.conditions.iterator(); iter.hasNext();) {
			Condition element = (Condition) iter.next();
			list.add(element.attribute);			
		}
		for (Iterator iter = this.subConditions.iterator(); iter.hasNext();) {
			Conditions element = (Conditions) iter.next();
			list.addAll(element.getConditionedAttributes());
		}
		return list;
	}
}
