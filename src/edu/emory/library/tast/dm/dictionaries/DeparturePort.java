package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

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
