package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Fate2 extends Dictionary {
	public static final Integer TYPE = new Integer(11);
	public static final String NAME = "Fate2";
	
	public Fate2() {
		setType(TYPE);
	}
	
	public static Fate2 loadFate2(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Fate2)dicts[0];
		} else {
			return null;
		}
		
	}
}
