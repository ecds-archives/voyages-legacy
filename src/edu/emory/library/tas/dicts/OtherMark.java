package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class OtherMark extends Dictionary {

	public  static final Integer TYPE = new Integer(33);
	public static final String NAME = "OtherMark";
	
	public OtherMark() {
		setType(TYPE);
	}
	
	public static OtherMark loadOtherMark(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (OtherMark)dicts[0];
		} else {
			return null;
		}
		
	}
}
