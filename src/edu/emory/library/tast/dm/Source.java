package edu.emory.library.tast.dm;


public class Source
{
	
	private String id;
	private String name;
	private int type;
	
	public static final int TYPE_DOCUMENTARY_SOURCE = 0;
	public static final int TYPE_NEWSPAPER = 1;
	public static final int TYPE_PUBLISHED_SOURCE = 2;
	public static final int TYPE_UNPUBLISHED_SECONDARY_SOURCE = 3; 
	public static final int TYPE_PRIVATE_NOTE_OR_COLLECTION = 4;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String description)
	{
		this.name = description;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}
	
}
