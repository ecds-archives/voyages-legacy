package edu.emory.library.tas.attrGroups;

public abstract class AbstractAttribute {
	
	public final static int TYPE_INTEGER = 0;
	public final static int TYPE_LONG = 1; 
	public final static int TYPE_FLOAT = 5; 
	public final static int TYPE_STRING = 2; 
	public final static int TYPE_DATE = 3;
	public final static int TYPE_DICT = 4;
	
	private ObjectType objectType;
	private String name;
	private Long id;
	private String userLabel;
	private int type;
	private String dictionary;
	
	public AbstractAttribute() {
	}
	
	public AbstractAttribute(String name2, int type2, String dictionary2, String userLabel2, ObjectType objType) {
		this.name = name2;
		this.type = type2;
		this.dictionary = dictionary2;
		this.userLabel = userLabel2;
		this.objectType = objType;
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
	
	public String getName() {
		return name;
	}
	public void setName(String attrName) {
		this.name = attrName;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isDictinaory()
	{
		return dictionary != null;
	}
	
	public String getDictionary()
	{
		return dictionary;
	}
	
	public String toString() {
		return "Attribute: " + this.name;
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AbstractAttribute)) {
			return false;
		}
		AbstractAttribute theOther = (AbstractAttribute) obj;
		if (theOther == null) return false;
		return 
			(id == null && theOther.getId() == null) ||
			(id != null && id.equals(theOther.getId()));
	}
}
