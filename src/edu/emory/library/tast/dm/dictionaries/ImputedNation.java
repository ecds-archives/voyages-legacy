package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class ImputedNation extends Dictionary {
	public static final Integer TYPE = new Integer(19);
	public static final String NAME = "ImputedNation";
	
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
