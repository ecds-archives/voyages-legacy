package edu.emory.library.tas.attrGroups;

import java.io.Serializable;

public class ObjectType implements Serializable {
	private Long id;
	private String typeName;
	
	public ObjectType() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String toString() {
		return "Type " + this.typeName;
	}
	
}
