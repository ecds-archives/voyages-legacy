package edu.emory.library.tas.attrGroups;

import java.util.Set;

public class GroupSet {
	private Long id;
	private Set groups;
	
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
}
