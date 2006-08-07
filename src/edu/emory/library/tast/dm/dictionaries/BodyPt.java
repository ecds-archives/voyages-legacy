package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class BodyPt extends Dictionary {

	public static final Integer TYPE = new Integer(2);
	public static final String NAME = "DodyPt";
	
	public BodyPt() {
		setType(TYPE);
	}
	
	public static BodyPt loadBodyPt(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (BodyPt)dicts[0];
		} else {
			return null;
		}
		
	}
}
