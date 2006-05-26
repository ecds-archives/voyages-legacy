package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class IntDisembPort extends Dictionary {
	public static final Integer TYPE = new Integer(20);
	public static final String NAME = "IntDisembPort";
	
	public IntDisembPort() {
		setType(TYPE);
	}
	
	public static IntDisembPort loadIntDisembPort(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (IntDisembPort)dicts[0];
		} else {
			return null;
		}
		
	}
}
