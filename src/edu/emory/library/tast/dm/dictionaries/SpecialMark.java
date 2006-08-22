package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class SpecialMark extends Dictionary {

	public static final Integer TYPE = new Integer(44);
	public static final String NAME = "SpecialMark";
	
	public SpecialMark() {
		setType(TYPE);
	}
	
	public static SpecialMark loadSpecialMark(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (SpecialMark)dicts[0];
		} else {
			return null;
		}
		
	}
}
