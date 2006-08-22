package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Temp extends Dictionary {
	public static final Integer TYPE = new Integer(46);
	public static final String NAME = "Temp";
	
	public Temp() {
		setType(TYPE);
	}
	
	public static Temp loadTemp(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Temp)dicts[0];
		} else {
			return null;
		}
		
	}
}
