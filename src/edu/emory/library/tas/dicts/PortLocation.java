package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class PortLocation extends Dictionary {
	
	public static final Integer TYPE = new Integer(36);
	private static final String NAME = "PortLocation";
	
	public PortLocation() {
		setType(TYPE);
	}
	
	public static PortLocation loadPortLocation(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (PortLocation)dicts[0];
		} else {
			return null;
		}
		
	}
}
