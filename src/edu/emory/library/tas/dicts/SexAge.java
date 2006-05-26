package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class SexAge extends Dictionary {

	public static final Integer TYPE = new Integer(42);
	public static final String NAME = "SexAge";
	
	public SexAge() {
		setType(TYPE);
	}
	
	public static SexAge loadSexAge(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (SexAge)dicts[0];
		} else {
			return null;
		}
		
	}
}
