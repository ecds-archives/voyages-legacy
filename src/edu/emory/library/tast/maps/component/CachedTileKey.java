package edu.emory.library.tast.maps.component;

public class CachedTileKey {

	private String mapFile;

	private int col;

	private int row;

	private int scale;

	
	private boolean hasComputed = false;
	private int hash;

	public CachedTileKey(String mapFile, int col, int row, int scale) {
		this.mapFile = mapFile;
		this.col = col;
		this.row = row;
		this.scale = scale;
	}


	public Object getServlet() {
		return mapFile;
	}

	public int getCol() {
		return col;
	}

	public int getRow() {
		return row;
	}

	public double getScale() {
		return scale;
	}

	public boolean equals(Object o) {
		if (!(o instanceof CachedTileKey)) {
			return false;
		}
		CachedTileKey that = (CachedTileKey) o;
		return this.mapFile.equals(that.mapFile) && this.col == that.col && this.row == that.row && this.scale == that.scale;
	}
	
	public int hashCode()
	{
		if (!hasComputed)
		{
			StringBuffer buffer = new StringBuffer();
			buffer.append(mapFile);
			buffer.append(":");
			buffer.append(col);
			buffer.append(":");
			buffer.append(row);
			buffer.append(":");
			buffer.append(scale);
			hash = buffer.toString().hashCode();
		}
		return hash;
	}
}
