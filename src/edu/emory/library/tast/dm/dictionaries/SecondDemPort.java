package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class SecondDemPort extends Dictionary {
	public static final Integer TYPE = new Integer(41);
	public static final String NAME = "SecondDemPort";
	
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
