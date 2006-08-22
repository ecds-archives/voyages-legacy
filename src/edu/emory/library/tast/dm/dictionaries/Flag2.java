package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Flag2 extends Dictionary {
	public static final Integer TYPE = new Integer(16);
	public static final String NAME = "Flag2";
	
	public Flag2() {
		setType(TYPE);
	}
	
	public static Flag2 loadFlag2(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Flag2)dicts[0];
		} else {
			return null;
		}
		
	}
}
