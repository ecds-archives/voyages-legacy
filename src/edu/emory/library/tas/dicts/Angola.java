package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Angola extends Dictionary {
	public static final Integer TYPE = new Integer(1);
	public static final String NAME = "Angola";
	
	public Angola() {
		setType(TYPE);
	}
	
	public static Angola loadAngola(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Angola)dicts[0];
		} else {
			return null;
		}
		
	}
}
