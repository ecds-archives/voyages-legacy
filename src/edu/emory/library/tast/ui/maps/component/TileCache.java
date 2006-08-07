package edu.emory.library.tast.ui.maps.component;

import java.util.HashMap;
import java.util.Map;

public class TileCache
{
	
	private static final int MAX_SIZE = 1024 * 1024; //10 * 1024 * 1024;
	private static final long CLEAN_PERIOD = 10000;
	
	private Map keyMap = new HashMap();
	private CachedTile cacheHead = null;
	private CachedTile cacheTail = null;
	private int size = 0;
	private long lastClean = System.currentTimeMillis();
	
	private synchronized CachedTile getTileAndTouchIt(CachedTileKey key)
	{
		
		// get it
		CachedTile tile = (CachedTile) keyMap.get(key);
		if (tile == null) return null;
			
		// change timestamp
		tile.touch();
		
		// make it the first
		if (!tile.isFirst())
		{
			tile.removeFromList();
			tile.makeFirst(cacheHead);
			cacheHead = tile;
		}
		
		// return image
		return tile;
			
	}
	
	private void cutTail()
	{
		size -= cacheTail.getSize(); 
		keyMap.remove(cacheTail.getKey());
		cacheTail = cacheTail.getPrev();
		if (cacheTail == null)
		{
			cacheHead = null;
		}
		else
		{
			cacheTail.makeLast();
		}
	}

	public byte[] get(CachedTileKey key)
	{
		CachedTile tile = getTileAndTouchIt(key);
		if (tile == null)
		{
			return null;
		}
		else
		{
			return tile.getImg();
		}
	}
	
	public byte[] get(String mapFile, int col, int row, int scale)
	{
		return get(new CachedTileKey(mapFile, col, row, scale));
	}

	public synchronized void put(CachedTileKey key, byte[] img)
	{
		
		// if key exists -> make it first
		CachedTile tile = getTileAndTouchIt(key);
		
		// key does not exist -> insert it
		if (tile == null)
		{
		
			// make some room, if we've reached max size
			while (cacheTail != null && size + img.length > MAX_SIZE)
				cutTail();
	
			// insert
			tile = new CachedTile(key, img);
			size += img.length; 
			keyMap.put(key, tile);
			tile.makeFirst(cacheHead);
			cacheHead = tile;
			if (cacheTail == null) cacheTail = tile;
		}
			
	}

	public void put(String mapFile, int col, int row, int scale, byte[] img)
	{
		put(new CachedTileKey(mapFile, col, row, scale), img);
	}
	
	public synchronized void clean()
	{
		
		long time = System.currentTimeMillis();
		if (lastClean + CLEAN_PERIOD > time) return;
		lastClean = time;
		
		while (cacheTail != null && cacheTail.isExpired(time))
			cutTail();

	}

}
