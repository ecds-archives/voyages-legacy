package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Us extends Dictionary {
	public static final Integer TYPE = new Integer(50);
	public static final String NAME = "Us";
	
	public Us() {
		setType(TYPE);
	}
	
	public static Us loadUs(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Us)dicts[0];
		} else {
			return null;
		}
		
	}
}
