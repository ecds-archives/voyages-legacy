package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class DepDefinition extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "DepDefinition";
	
	public DepDefinition() {
		setType(TYPE);
	}
	
	public static DepDefinition loadDepDefinition(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (DepDefinition)dicts[0];
		} else {
			return null;
		}
		
	}
}
