package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Gc extends Dictionary {
	public static final Integer TYPE = new Integer(17);
	public static final String NAME = "Gc";
	
	public Gc() {
		setType(TYPE);
	}
	
	public static Gc loadGc(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Gc)dicts[0];
		} else {
			return null;
		}
		
	}
}
