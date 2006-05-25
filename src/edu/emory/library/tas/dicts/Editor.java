package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Editor extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "Editor";
	
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
