package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class TonesType extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "TonesType";
	
	public TonesType() {
		setType(TYPE);
	}
	
	public static TonesType loadTonesType(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (TonesType)dicts[0];
		} else {
			return null;
		}
		
	}
}
