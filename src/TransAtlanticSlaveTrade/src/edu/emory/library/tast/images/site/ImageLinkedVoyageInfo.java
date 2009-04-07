package edu.emory.library.tast.images.site;

public class ImageLinkedVoyageInfo
{
	
	private long voyageIid;
	private int voyageId;
	private String info;
	
	public ImageLinkedVoyageInfo(long voyageIid, int voyageId, String info)
	{
		this.voyageIid = voyageIid;
		this.voyageId = voyageId;
		this.info = info;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}
	
	public int getVoyageId()
	{
		return voyageId;
	}
	
	public void setVoyageId(int voyageId)
	{
		this.voyageId = voyageId;
	}

	public long getVoyageIid()
	{
		return voyageIid;
	}

	public void setVoyageIid(long voyageIid)
	{
		this.voyageIid = voyageIid;
	}

}
