package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Fate3 extends Dictionary {
	public static final Integer TYPE = new Integer(12);
	public static final String NAME = "Fate3";
	
	public Fate3() {
		setType(TYPE);
	}
	
	public static Fate3 loadFate3(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Fate3)dicts[0];
		} else {
			return null;
		}
		
	}
}
