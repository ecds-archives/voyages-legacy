package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class MajByIm extends Dictionary {
	public static final Integer TYPE = new Integer(21);
	public static final String NAME = "MajByIm";
	
	public MajByIm() {
		setType(TYPE);
	}
	
	public static MajByIm loadMajByIm(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (MajByIm)dicts[0];
		} else {
			return null;
		}
		
	}
}
