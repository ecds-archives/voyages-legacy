package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class MarkNumber extends Dictionary {

	public static final Integer TYPE = new Integer(23);
	public static final String NAME = "MarkNumber";
	
	public MarkNumber() {
		setType(TYPE);
	}
	
	public static MarkNumber loadMarkNumber(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (MarkNumber)dicts[0];
		} else {
			return null;
		}
		
	}
}
