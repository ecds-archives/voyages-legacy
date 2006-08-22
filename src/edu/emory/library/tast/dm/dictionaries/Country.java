package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Country extends Dictionary {
	
	public static final Integer TYPE = new Integer(4);
	public static final String NAME = "Country";
	
	public Country() {
		setType(TYPE);
	}
	
	public static Country loadCountry(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Country)dicts[0];
		} else {
			return null;
		}
		
	}
}
