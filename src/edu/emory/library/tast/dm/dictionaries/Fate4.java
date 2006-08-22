package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Fate4 extends Dictionary {
	public static final Integer TYPE = new Integer(13);
	public static final String NAME = "Fate4";
	
	public Fate4() {
		setType(TYPE);
	}
	
	public static Fate4 loadFate4(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Fate4)dicts[0];
		} else {
			return null;
		}
		
	}
}
