package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class Mjselrg2 extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "Mjselrg2";
	
	public Mjselrg2() {
		setType(TYPE);
	}
	
	public static Mjselrg2 loadMjselrg2(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Mjselrg2)dicts[0];
		} else {
			return null;
		}
		
	}
}
