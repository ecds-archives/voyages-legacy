package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Status extends Dictionary {
	private static final Integer TYPE = new Integer(45);
	private static final String NAME = "Status";
	
	public Status() {
		setType(TYPE);
	}
	
	public static Status loadStatus(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Status)dicts[0];
		} else {
			return null;
		}
		
	}
}
