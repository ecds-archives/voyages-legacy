package edu.emory.library.tas.util.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
		default:
			throw new RuntimeException("Wrong operand!");
		}
		this.conditions.add(new Condition(attrName, opStr, value));
		
	}
	
	public void addCondition(Conditions subCond) {
		this.subConditions.add(subCond);
	}
	
	public StringBuffer getConditionHQL() {
		int size = this.conditions.size() + this.subConditions.size();
		if (this.joinCondition == JOIN_NOT && size != 1) {
			throw new RuntimeException("With JOIN_NOT only one condition allowable!");
		}
		
		boolean last = false;
		int processed = 0;
		StringBuffer ret = new StringBuffer();
		
		if (this.joinCondition == JOIN_NOT) {
			ret.append("not (");
		}
		
		Iterator iter = this.conditions.iterator();
		while (iter.hasNext()) {
			Condition c = (Condition)iter.next();
			String attr = c.attribute;
			Object value = c.value;
			processed++;
			ret.append(attr);
			ret.append(c.op);
			if (value instanceof String) {
				if (c.op.equals(" like ")) {
					ret.append("(");
				}
				ret.append("'");
				ret.append(value);
				ret.append("'");
				if (c.op.equals(" like ")) {
					ret.append(")");
				}
			} else {
				ret.append(value);
			}
			if (processed < size) {
				ret.append(this.joinCondition == JOIN_AND ? " and ":" or ");
			}
		}
		
		iter = this.subConditions.iterator();
		while (iter.hasNext()) {
			ret.append("(").append(((Conditions)iter.next()).getConditionHQL()).append(")");
		}
		
		if (this.joinCondition == JOIN_NOT) {
			ret.append(")");
		}
		
		return ret;
	}
}
