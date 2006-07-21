package edu.emory.library.tas.web.maps;

public class CachedTile {

	public static final long EXPIRATION = 60000;

	private Object servlet;

	private double x;

	private double y;

	private double zoom;

	private long time;

	public CachedTile(Object servlet, double x, double y, double zoom) {
		this.servlet = servlet;
		this.x = x;
		this.y = y;
		this.zoom = zoom;
		this.time = System.currentTimeMillis();
	}

	public void touch() {
		this.time = System.currentTimeMillis();
	}

	public Object getServlet() {
		return servlet;
	}

	public long getTime() {
		return time;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZoom() {
		return zoom;
	}

	public boolean isExpired(long time) {
		return this.time + EXPIRATION < time;
	}

	public boolean equals(Object o) {
		if (!(o instanceof CachedTile)) {
			return false;
		}
		CachedTile that = (CachedTile) o;
		return this.servlet.equals(that.servlet) && this.x == that.x && this.y == that.y && this.zoom == that.zoom;
	}
	
	public int hashCode() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(servlet.hashCode());
		buffer.append(x).append(y);
		buffer.append(zoom);
		return buffer.toString().hashCode();
	}
}
