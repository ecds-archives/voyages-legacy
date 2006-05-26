package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Year100 extends Dictionary {
	public static final Integer TYPE = new Integer(54);
	public static final String NAME = "Year100";
	
	public Year100() {
		setType(TYPE);
	}
	
	public static Year100 loadYear100(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Year100)dicts[0];
		} else {
			return null;
		}
		
	}
}
