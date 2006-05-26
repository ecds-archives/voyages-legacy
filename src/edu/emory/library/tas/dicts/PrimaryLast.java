package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class PrimaryLast extends Dictionary {
	public static final Integer TYPE = new Integer(37);
	public static final String NAME = "PrimaryLast";
	
	public PrimaryLast() {
		setType(TYPE);
	}
	
	public static PrimaryLast loadPrimaryLast(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (PrimaryLast)dicts[0];
		} else {
			return null;
		}
		
	}
}
