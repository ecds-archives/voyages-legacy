package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Userid extends Dictionary {
	public static final Integer TYPE = new Integer(51);
	public static final String NAME = "Userid";
	
	public Userid() {
		setType(TYPE);
	}
	
	public static Userid loadUserid(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Userid)dicts[0];
		} else {
			return null;
		}
		
	}
}
