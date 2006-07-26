package edu.emory.library.tas;

import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;
import edu.emory.library.tas.web.maps.PointOfInterest;

public class GISPortLocation {

	public String portName;
	public double x;
	public double y;
	
	public GISPortLocation() {		
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public String toString() {
		return "Port: " + this.getPortName() + " [" + this.x + ", " + this.y + "]";
	}
	
	public boolean equals(Object o) {
		if (o instanceof PointOfInterest) {
			PointOfInterest that = (PointOfInterest)o;
			return this.x == that.getX() && this.y == that.getY();
		} else if (o instanceof GISPortLocation) {
			GISPortLocation that = (GISPortLocation)o;
			return this.x == that.getX() && this.y == that.getY();
		}
		return false;
	}
	
	public static GISPortLocation getGISPortLocation(String portName) {
		Conditions c = new Conditions();
		c.addCondition("portName", portName, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("GISPortLocation", c);
		Object[] ports = qValue.executeQuery();
		if (ports.length == 0) {
			return null;
		} else {
			return (GISPortLocation)ports[0];
		}
	}
	
}
