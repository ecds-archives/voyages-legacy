package edu.emory.library.tas.attrGroups;

import java.util.HashSet;
import java.util.Set;

public class Group {
	private Long id;
	private String name;
	private Set attributes = new HashSet();;
	
	public Group() {
		
	}
	
	public Set getAttributes() {
		return attributes;
	}
	public void setAttributes(Set attributes) {
		this.attributes = attributes;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "Group of attributes " + this.attributes;
	}
}
