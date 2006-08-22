package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class XmimpFlag extends Dictionary {
	public static final Integer TYPE = new Integer(53);
	public static final String NAME = "XmimpFlag";
	
	public XmimpFlag() {
		setType(TYPE);
	}
	
	public static XmimpFlag loadXmimpFlag(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (XmimpFlag)dicts[0];
		} else {
			return null;
		}
		
	}
}
