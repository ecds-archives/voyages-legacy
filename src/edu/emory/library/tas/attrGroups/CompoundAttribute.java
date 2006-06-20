package edu.emory.library.tas.attrGroups;

import java.util.HashSet;
import java.util.Set;

import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

public class CompoundAttribute extends AbstractAttribute {
	private Set attributes = new HashSet();
	
	
	public CompoundAttribute() {	
	}
	
	public Set getAttributes() {
		return attributes;
	}
	public void setAttributes(Set attributes) {
		this.attributes = attributes;
	}
	
	public String toString() {
		return "Group of attributes " + this.attributes;
	}
	
	public static AbstractAttribute loadById(Long id) {
		Conditions conditions = new Conditions();
		conditions.addCondition("id", id, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("CompoundAttribute", conditions);
		Object[] groups = (Object[]) query.executeQuery();
		if (groups.length == 0) {
			return null;
		}
		return (AbstractAttribute) groups[0];
	}
	
}
