package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class ImputedNation extends Dictionary {
	private static final Integer TYPE = new Integer(19);
	private static final String NAME = "ImputedNation";
	
	public ImputedNation() {
		setType(TYPE);
	}
	
	public static ImputedNation loadImputedNation(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (ImputedNation)dicts[0];
		} else {
			return null;
		}
		
	}
}
