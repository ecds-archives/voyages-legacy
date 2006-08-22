package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class DepDefinition extends Dictionary {
	public static final Integer TYPE = new Integer(8);
	public static final String NAME = "DepDefinition";
	
	public DepDefinition() {
		setType(TYPE);
	}
	
	public static DepDefinition loadDepDefinition(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (DepDefinition)dicts[0];
		} else {
			return null;
		}
		
	}
}
