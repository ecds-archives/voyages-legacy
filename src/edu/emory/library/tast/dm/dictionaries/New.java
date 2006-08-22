package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class New extends Dictionary {
	public static final Integer TYPE = new Integer(30);
	public static final String NAME = "New";
	
	public New() {
		setType(TYPE);
	}
	
	public static New loadNew(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (New)dicts[0];
		} else {
			return null;
		}
		
	}
}
