package edu.emory.library.tast.ui.maps.component;

public class CachedTile
{
	
	public static final long EXPIRATION = 60000;
	
	private CachedTileKey key;
	private long time;
	private byte[] img = null;
	private CachedTile next = null;
	private CachedTile prev = null;
	
	public CachedTile(CachedTileKey key, byte[] img)
	{
		this.img = img;
		this.key = key;
		this.time = System.currentTimeMillis();
	}

	public void touch()
	{
		this.time = System.currentTimeMillis();
	}
	
	public boolean isExpired(long time)
	{
		return this.time + EXPIRATION < time;
	}

	public long getTime()
	{
		return time;
	}

	public byte[] getImg()
	{
		return img;
	}

	public int getSize()
	{
		return img.length;
	}

	public CachedTileKey getKey()
	{
		return key;
	}

	public CachedTile getNext()
	{
		return next;
	}

	public void setNext(CachedTile next)
	{
		this.next = next;
	}

	public boolean isFirst()
	{
		return prev == null;
	}

	public boolean isLast()
	{
		return next == null;
	}

	public CachedTile getPrev()
	{
		return prev;
	}

	public void setPrev(CachedTile prev)
	{
		this.prev = prev;
	}

	public void makeLast()
	{
		this.next = null;
	}

	public void makeFirst()
	{
		this.prev = null;
	}

	public void makeFirst(CachedTile oldHead)
	{
		this.prev = null;
		this.next = oldHead;
	}

	public void removeFromList()
	{
		if (prev != null) prev.next = next; 
		if (next != null) next.prev = prev; 
	}

}
