package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class DeparturePort extends Dictionary {
	public static final Integer TYPE = new Integer(6);
	public static final String NAME = "DeparturePort";
	
	public DeparturePort() {
		setType(TYPE);
	}
	
	public static DeparturePort loadDepDefinition(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (DeparturePort)dicts[0];
		} else {
			return null;
		}
		
	}
}
