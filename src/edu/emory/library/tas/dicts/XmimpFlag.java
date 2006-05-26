package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class XmimpFlag extends Dictionary {
	public static final Integer TYPE = new Integer(53);
	public static final String NAME = "XmimpFlag";
	
	public XmimpFlag() {
		setType(TYPE);
	}
	
	public static XmimpFlag loadXmimpFlag(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (XmimpFlag)dicts[0];
		} else {
			return null;
		}
		
	}
}
