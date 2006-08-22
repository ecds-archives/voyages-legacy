package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class EmbRegion extends Dictionary {
	public static final Integer TYPE = new Integer(10);
	public static final String NAME = "EmbRegion";
	
	public EmbRegion() {
		setType(TYPE);
	}
	
	public static EmbRegion loadEmbRegion(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (EmbRegion)dicts[0];
		} else {
			return null;
		}
		
	}
}
