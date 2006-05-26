package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class New extends Dictionary {
	public static final Integer TYPE = new Integer(30);
	public static final String NAME = "New";
	
	public New() {
		setType(TYPE);
	}
	
	public static New loadNew(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (New)dicts[0];
		} else {
			return null;
		}
		
	}
}
