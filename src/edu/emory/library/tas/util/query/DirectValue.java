package edu.emory.library.tas.util.query;

public class DirectValue {
	private String value;
	
	public DirectValue(String str) {
		this.value = str;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
}
