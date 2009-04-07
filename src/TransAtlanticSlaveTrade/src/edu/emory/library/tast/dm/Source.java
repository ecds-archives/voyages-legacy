package edu.emory.library.tast.dm;


public class Source
{
	
	private long iid;
	private String sourceId;
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

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public long getIid()
	{
		return iid;
	}

	public void setIid(long internalId)
	{
		this.iid = internalId;
	}

	public String getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(String souceId)
	{
		this.sourceId = souceId;
	}
	
}
