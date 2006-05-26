package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Us extends Dictionary {
	private static final Integer TYPE = new Integer(50);
	private static final String NAME = "Us";
	
	public Us() {
		setType(TYPE);
	}
	
	public static Us loadUs(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Us)dicts[0];
		} else {
			return null;
		}
		
	}
}
