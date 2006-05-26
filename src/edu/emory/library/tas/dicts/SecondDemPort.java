package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class SecondDemPort extends Dictionary {
	private static final Integer TYPE = new Integer(41);
	private static final String NAME = "SecondDemPort";
	
	public SecondDemPort() {
		setType(TYPE);
	}
	
	public static SecondDemPort loadSecondDemPort(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (SecondDemPort)dicts[0];
		} else {
			return null;
		}
		
	}
}
