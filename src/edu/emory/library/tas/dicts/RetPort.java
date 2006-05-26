package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class RetPort extends Dictionary {
	private static final Integer TYPE = new Integer(39);
	private static final String NAME = "RetPort";
	
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
