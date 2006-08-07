package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class MarkType extends Dictionary {

	public static final Integer TYPE = new Integer(25);
	public static final String NAME = "MarkType";
	
	public MarkType() {
		setType(TYPE);
	}
	
	public static MarkType loadMarkType(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (MarkType)dicts[0];
		} else {
			return null;
		}
		
	}
}
