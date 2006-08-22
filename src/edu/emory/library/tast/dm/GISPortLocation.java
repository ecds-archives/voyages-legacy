package edu.emory.library.tast.dm;

import edu.emory.library.tast.ui.maps.component.PointOfInterest;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class GISPortLocation {

//	private static final String PROJ_IN = StringUtils.getProjectionStringForProj4(AppConfig.getConfiguration()
//			.getStringArray(AppConfig.MAP_PROJ_IN));
//
//	private static final String PROJ_OUT = StringUtils.getProjectionStringForProj4(AppConfig.getConfiguration()
//			.getStringArray(AppConfig.MAP_PROJ_OUT));

	private String portName;

	private double x;

	private double y;

	// static {
	// System.loadLibrary("proj");
	// }

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
			PointOfInterest that = (PointOfInterest) o;
			return this.x == that.getX() && this.y == that.getY();
		} else if (o instanceof GISPortLocation) {
			GISPortLocation that = (GISPortLocation) o;
			return this.x == that.getX() && this.y == that.getY();
		}
		return false;
	}

	public static GISPortLocation getGISPortLocation(String portName) {
		if (portName == null) {
			return null;
		}
		Conditions c = new Conditions();
		c.addCondition("portName", portName, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("GISPortLocation", c);
		Object[] ports = qValue.executeQuery();
		if (ports.length == 0) {
			return null;
		} else {
			return (GISPortLocation) ports[0];
		}
	}

	public static GISPortLocation getGISPortLocation(Dictionary port) {
		if (port == null) {
			return null;
		}
		Conditions c = new Conditions();
		c.addCondition("portName", port.getName(), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("GISPortLocation", c);
		Object[] ports = qValue.executeQuery();
		if (ports.length == 0) {
			return null;
		} else {
			return (GISPortLocation) ports[0];
		}
	}

	public double[] getXYProjected() {
//		Proj4 projClass = new Proj4(PROJ_IN, PROJ_OUT);
//		ProjectionData data = new ProjectionData(new double[] { x }, new double[] { y });
//		projClass.transform(data, 1, 0);
//		return new double[] { data.x[0], data.y[0] };
		return new double[] { x, y };
	}

}
