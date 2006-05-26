package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class ThirdDemPort extends Dictionary {
	private static final Integer TYPE = new Integer(48);
	private static final String NAME = "ThirdDemPort";
	
	public ThirdDemPort() {
		setType(TYPE);
	}
	
	public static ThirdDemPort loadThirdDemPort(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (ThirdDemPort)dicts[0];
		} else {
			return null;
		}
		
	}
}
