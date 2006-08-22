package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class ShipNationality extends Dictionary {
	public static final Integer TYPE = new Integer(43);
	public static final String NAME = "ShipNationality";
	
	public ShipNationality() {
		setType(TYPE);
	}
	
	public static ShipNationality loadShipNationality(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (ShipNationality)dicts[0];
		} else {
			return null;
		}
		
	}
}
