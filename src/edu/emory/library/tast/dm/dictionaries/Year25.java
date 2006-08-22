package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Year25 extends Dictionary {
	public static final Integer TYPE = new Integer(55);
	public static final String NAME = "Year25";
	
	public Year25() {
		setType(TYPE);
	}
	
	public static Year25 loadYear25(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Year25)dicts[0];
		} else {
			return null;
		}
		
	}
}
