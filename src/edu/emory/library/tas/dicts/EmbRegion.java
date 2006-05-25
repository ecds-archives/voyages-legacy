package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class EmbRegion extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "EmbRegion";
	
	public EmbRegion() {
		setType(TYPE);
	}
	
	public static EmbRegion loadEmbRegion(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (EmbRegion)dicts[0];
		} else {
			return null;
		}
		
	}
}
