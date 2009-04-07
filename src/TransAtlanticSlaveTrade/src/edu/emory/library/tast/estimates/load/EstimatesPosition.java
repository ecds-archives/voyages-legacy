package edu.emory.library.tast.estimates.load;

public class EstimatesPosition {
	private int[] port;
	private int[] majselimp;
	private int natimp;
	private int year;
	
	private String exportFormula;
	private String importFormula;
	private String properties;
	
	public EstimatesPosition(int natimp, int[] port, int[] majselimp, 
			int year, String exportString, String importString,
			String properties) {
		this.natimp = natimp;
		this.port = port;
		this.majselimp = majselimp;
		this.year = year;
		this.exportFormula = exportString;
		this.importFormula = importString;
		this.properties = properties;
	}

	public String getExportFormula() {
		return exportFormula;
	}

	public void setExportFormula(String exportFormula) {
		this.exportFormula = exportFormula;
	}

	public String getImportFormula() {
		return importFormula;
	}

	public void setImportFormula(String importFormula) {
		this.importFormula = importFormula;
	}

	public int getNatimp() {
		return natimp;
	}

	public void setNatimp(int natimp) {
		this.natimp = natimp;
	}

	public int[] getPort() {
		return port;
	}

	public void setPort(int[] port) {
		this.port = port;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public boolean isExportSimpleValue() {
		if (this.properties.trim().equals("")) {
			return false;
		} else {
			String [] props = this.properties.split(",");
			return props[0].trim().equals("0");
		}
	}
	
	public boolean isImportSimpleValue() {
		if (this.properties.trim().equals("")) {
			return false;
		} else {
			String [] props = this.properties.split(",");
			return props[1].trim().equals("0");
		}
	}
	
	public String toString() {
		return "EstimatesPosition[" + natimp + ", " + year + ", " + arrayToStr(port) + ", " + arrayToStr(majselimp) + 
					", " + exportFormula + ", " + importFormula + ", " + properties + "]";
	}

	private String arrayToStr(int[] num) {
		String str = "(";
		if (num != null) {
			for (int i = 0; i < num.length; i++) {
				str += num[i];
				if (i + 1 < num.length) {
					str += ",";
				}
			}
		}
		str += ")";
		return str;
	}

	public int[] getMajselimp() {
		return majselimp;
	}

	public void setMajselimp(int[] majselimp) {
		this.majselimp = majselimp;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}
}
