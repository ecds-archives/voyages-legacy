package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class ThirdDemPort extends Dictionary {
	public static final Integer TYPE = new Integer(48);
	public static final String NAME = "ThirdDemPort";
	
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
