package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Mjselrg2 extends Dictionary {
	public static final Integer TYPE = new Integer(26);
	public static final String NAME = "Mjselrg2";
	
	public Mjselrg2() {
		setType(TYPE);
	}
	
	public static Mjselrg2 loadMjselrg2(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Mjselrg2)dicts[0];
		} else {
			return null;
		}
		
	}
}
