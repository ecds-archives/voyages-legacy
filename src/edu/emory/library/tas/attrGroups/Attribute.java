package edu.emory.library.tas.attrGroups;

public class Attribute {
	private Long id;
	private String name;
	private String userLabel;
	private ObjectType objectType;
	
	public Attribute() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String attrName) {
		this.name = attrName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserLabel() {
		return userLabel;
	}
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}
	
	public String toString() {
		return "Attribute: " + this.name;
	}
	
	public boolean equals(Object obj)
	{
		Attribute theOther = (Attribute) obj;
		if (theOther == null) return false;
		return 
			(id == null && theOther.getId() == null) ||
			(id != null && id.equals(theOther.getId()));
	}

}
