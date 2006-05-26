package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class MarkSize extends Dictionary {

	public static final Integer TYPE = new Integer(24);
	public static final String NAME = "MarkSize";
	
	public MarkSize() {
		setType(TYPE);
	}
	
	public static MarkSize loadMarkSize(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (MarkSize)dicts[0];
		} else {
			return null;
		}
		
	}
}
