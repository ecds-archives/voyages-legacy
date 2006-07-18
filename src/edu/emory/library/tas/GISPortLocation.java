package edu.emory.library.tas;

public class GISPortLocation {

	public String portName;
	public float x;
	public float y;
	
	public GISPortLocation() {		
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public String toString() {
		return "Port: " + this.getPortName() + " [" + this.x + ", " + this.y + "]";
	}
	
}
