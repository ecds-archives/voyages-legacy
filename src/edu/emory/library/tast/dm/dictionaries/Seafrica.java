package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Seafrica extends Dictionary {
	public static final Integer TYPE = new Integer(40);
	public static final String NAME = "Seafrica";
	
	public Seafrica() {
		setType(TYPE);
	}
	
	public static Seafrica loadSeafrica(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Seafrica)dicts[0];
		} else {
			return null;
		}
		
	}
}
