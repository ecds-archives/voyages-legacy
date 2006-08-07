package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class MarkDegree extends Dictionary {

	public static final Integer TYPE = new Integer(22);
	public static final String NAME = "MarkDegree";
	
	public MarkDegree() {
		setType(TYPE);
	}
	
	public static MarkDegree loadMarkDegree(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (MarkDegree)dicts[0];
		} else {
			return null;
		}
		
	}
}
