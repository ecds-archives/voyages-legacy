package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Userid extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "Userid";
	
	public Userid() {
		setType(TYPE);
	}
	
	public static Userid loadUserid(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Userid)dicts[0];
		} else {
			return null;
		}
		
	}
}
