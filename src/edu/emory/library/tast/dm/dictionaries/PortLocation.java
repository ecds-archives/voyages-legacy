package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class PortLocation extends Dictionary {
	
	public static final Integer TYPE = new Integer(36);
	public static final String NAME = "PortLocation";
	
	public PortLocation() {
		setType(TYPE);
	}
	
	public static PortLocation loadPortLocation(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (PortLocation)dicts[0];
		} else {
			return null;
		}
		
	}
}
