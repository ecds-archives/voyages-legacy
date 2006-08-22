package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class DemBroadRegion extends Dictionary {
	public static final Integer TYPE = new Integer(5);
	public static final String NAME = "DemBroadRegion";
	
	public DemBroadRegion() {
		setType(TYPE);
	}
	
	public static DemBroadRegion loadDemBroadRegion(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (DemBroadRegion)dicts[0];
		} else {
			return null;
		}
		
	}
}
