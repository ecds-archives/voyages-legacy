package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class RetPort extends Dictionary {
	public static final Integer TYPE = new Integer(39);
	public static final String NAME = "RetPort";
	
	public RetPort() {
		setType(TYPE);
	}
	
	public static RetPort loadRetPort(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (RetPort)dicts[0];
		} else {
			return null;
		}
		
	}
}
