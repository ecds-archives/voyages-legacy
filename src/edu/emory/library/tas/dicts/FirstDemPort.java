package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class FirstDemPort extends Dictionary {
	public static final Integer TYPE = new Integer(15);
	public static final String NAME = "FirstDemPort";
	
	public FirstDemPort() {
		setType(TYPE);
	}
	
	public static FirstDemPort loadFirstDemPort(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (FirstDemPort)dicts[0];
		} else {
			return null;
		}
		
	}
}
