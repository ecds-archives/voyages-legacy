package edu.emory.library.tas;

import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

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
	
	public static GISPortLocation getGISPortLocation(Dictionary dictionary) {
		Conditions c = new Conditions();
		c.addCondition("portName", dictionary.getName(), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("GISPortLocation", c);
		Object[] ports = qValue.executeQuery();
		if (ports.length == 0) {
			return null;
		} else {
			return (GISPortLocation)ports[0];
		}
	}
	
}
