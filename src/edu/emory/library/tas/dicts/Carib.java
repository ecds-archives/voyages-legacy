package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Carib extends Dictionary {
	public static final Integer TYPE = new Integer(3);
	public static final String NAME = "Carib";
	
	public Carib() {
		setType(TYPE);
	}
	
	public static Carib loadCarib(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Carib)dicts[0];
		} else {
			return null;
		}
		
	}
}
