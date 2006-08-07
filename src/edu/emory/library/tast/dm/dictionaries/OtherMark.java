package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

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
