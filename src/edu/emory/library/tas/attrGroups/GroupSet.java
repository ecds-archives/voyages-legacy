package edu.emory.library.tas.attrGroups;

import java.util.HashSet;
import java.util.Set;

public class GroupSet {
	private Long id;
	private String name;
	private Set groups  = new HashSet();;
	private Set attributes = new HashSet();;
	
	public GroupSet() {
		
	}
	
	public Set getGroups() {
		return groups;
	}
	public void setGroups(Set groups) {
		this.groups = groups;
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
	
	public String toString() {
		return "Set " + this.name +": groups " + this.groups + "\n     attributes " + this.attributes;
	}
}
