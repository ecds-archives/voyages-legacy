package edu.emory.library.tas.attrGroups;

public class Attribute {
	private Long id;
	private String attrName;
	
	public Attribute() {
		
	}
	
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
