package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class PurchasePort extends Dictionary {
	public static final Integer TYPE = new Integer(38);
	public static final String NAME = "PurchasePort";
	
	public PurchasePort() {
		setType(TYPE);
	}
	
	public static PurchasePort loadPurchasePort(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (PurchasePort)dicts[0];
		} else {
			return null;
		}
		
	}
}
