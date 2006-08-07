package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Temp2 extends Dictionary {
	public static final Integer TYPE = new Integer(47);
	public static final String NAME = "Temp2";
	
	public Temp2() {
		setType(TYPE);
	}
	
	public static Temp2 loadTemp2(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Temp2)dicts[0];
		} else {
			return null;
		}
		
	}
}
