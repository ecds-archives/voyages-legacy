package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class Newamreg extends Dictionary {
	public static final Integer TYPE = new Integer(31);
	public static final String NAME = "Newamreg";
	
	public Newamreg() {
		setType(TYPE);
	}
	
	public static Newamreg loadNew(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Newamreg)dicts[0];
		} else {
			return null;
		}
		
	}
}
