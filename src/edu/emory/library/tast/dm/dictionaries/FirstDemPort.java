package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class FirstDemPort extends Dictionary {
	public static final Integer TYPE = new Integer(15);
	public static final String NAME = "FirstDemPort";
	
	public FirstDemPort() {
		setType(TYPE);
	}
	
	public static FirstDemPort loadFirstDemPort(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (FirstDemPort)dicts[0];
		} else {
			return null;
		}
		
	}
}
