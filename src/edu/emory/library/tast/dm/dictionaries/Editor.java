package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Editor extends Dictionary {
	public static final Integer TYPE = new Integer(9);
	public static final String NAME = "Editor";
	
	public Editor() {
		setType(TYPE);
	}
	
	public static Editor loadEditor(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Editor)dicts[0];
		} else {
			return null;
		}
		
	}
}
