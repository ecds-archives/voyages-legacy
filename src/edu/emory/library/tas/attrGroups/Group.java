package edu.emory.library.tas.attrGroups;

import java.util.HashSet;
import java.util.Set;

public class Group {
	
	private Long id;
	private String name;
	private String userLabel;
	private Set compoundAttributes  = new HashSet();
	private Set attributes = new HashSet();
	
	public Group() {		
	}
	
	public Set getCompoundAttributes() {
		return compoundAttributes;
	}
	public void setCompoundAttributes(Set groups) {
		this.compoundAttributes = groups;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set getAttributes() {
		return attributes;
	}
	public void setAttributes(Set attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	
	public String toString() {
		return "Set " + this.name +": groups " + this.compoundAttributes + "\n     attributes " + this.attributes;
	}
}
