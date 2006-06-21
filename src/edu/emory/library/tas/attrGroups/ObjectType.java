package edu.emory.library.tas.attrGroups;

import java.io.Serializable;

public class ObjectType implements Serializable {
	
	private static final long serialVersionUID = -5680579514956701065L;

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
