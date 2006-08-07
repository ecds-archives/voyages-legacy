package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Status extends Dictionary {
	public static final Integer TYPE = new Integer(45);
	public static final String NAME = "Status";
	
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
