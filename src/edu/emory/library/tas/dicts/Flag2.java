package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Flag2 extends Dictionary {
	private static final Integer TYPE = new Integer(16);
	private static final String NAME = "Flag2";
	
	public Flag2() {
		setType(TYPE);
	}
	
	public static Flag2 loadFlag2(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Flag2)dicts[0];
		} else {
			return null;
		}
		
	}
}
