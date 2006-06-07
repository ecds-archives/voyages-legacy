package edu.emory.library.tas.attrGroups;

import java.util.Set;

public class Group {
	private Long id;
	private Set attributes;
	
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
}
