package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Year5 extends Dictionary {
	public static final Integer TYPE = new Integer(56);
	public static final String NAME = "Year5";
	
	public Year5() {
		setType(TYPE);
	}
	
	public static Year5 loadYear5(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Year5)dicts[0];
		} else {
			return null;
		}
		
	}
}
