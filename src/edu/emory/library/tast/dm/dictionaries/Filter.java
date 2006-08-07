package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Filter extends Dictionary {
	public static final Integer TYPE = new Integer(14);
	public static final String NAME = "Filter";
	
	public Filter() {
		setType(TYPE);
	}
	
	public static Filter loadFilter(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Filter)dicts[0];
		} else {
			return null;
		}
		
	}
}
