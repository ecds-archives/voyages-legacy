package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class OldWorldPlace extends Dictionary {
	private static final Integer TYPE = new Integer(32);
	private static final String NAME = "OldWorldPlace";
	
	public OldWorldPlace() {
		setType(TYPE);
	}
	
	public static OldWorldPlace loadOldWorldPlace(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (OldWorldPlace)dicts[0];
		} else {
			return null;
		}
		
	}
}
