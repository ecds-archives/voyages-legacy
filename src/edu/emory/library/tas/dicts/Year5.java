package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Year5 extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "Year5";
	
	public Year5() {
		setType(TYPE);
	}
	
	public static Year5 loadYear5(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Year5)dicts[0];
		} else {
			return null;
		}
		
	}
}
