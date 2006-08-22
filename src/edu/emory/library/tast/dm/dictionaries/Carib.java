package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Carib extends Dictionary {
	public static final Integer TYPE = new Integer(3);
	public static final String NAME = "Carib";
	
	public Carib() {
		setType(TYPE);
	}
	
	public static Carib loadCarib(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Carib)dicts[0];
		} else {
			return null;
		}
		
	}
}
