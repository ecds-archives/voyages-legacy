package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Filter extends Dictionary {
	private static final Integer TYPE = new Integer(14);
	private static final String NAME = "Filter";
	
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
