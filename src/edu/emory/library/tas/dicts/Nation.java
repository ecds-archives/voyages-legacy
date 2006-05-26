package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Nation extends Dictionary {

	public static final Integer TYPE = new Integer(29);
	public static final String NAME = "Nation";
	
	public Nation() {
		setType(TYPE);
	}
	
	public static Nation loadNation(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Nation)dicts[0];
		} else {
			return null;
		}
		
	}
}
