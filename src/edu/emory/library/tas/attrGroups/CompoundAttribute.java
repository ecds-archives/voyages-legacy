package edu.emory.library.tas.attrGroups;

import java.util.HashSet;
import java.util.Set;

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
		return null;
	}
	
}
