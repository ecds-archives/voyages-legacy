package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class OldWorldPlace extends Dictionary {
	public static final Integer TYPE = new Integer(32);
	public static final String NAME = "OldWorldPlace";
	
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
