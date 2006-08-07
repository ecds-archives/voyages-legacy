package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

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
