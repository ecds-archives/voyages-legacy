package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Natinim4 extends Dictionary {
	public static final Integer TYPE = new Integer(28);
	public static final String NAME = "Natinim3";
	
	public Natinim4() {
		setType(TYPE);
	}
	
	public static Natinim4 loadNatinim4(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Natinim4)dicts[0];
		} else {
			return null;
		}
		
	}
}
