package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

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
