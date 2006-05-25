package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class ShipNationality extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "ShipNationality";
	
	public ShipNationality() {
		setType(TYPE);
	}
	
	public static ShipNationality loadShipNationality(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (ShipNationality)dicts[0];
		} else {
			return null;
		}
		
	}
}
