package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class ImpPrincipalRegion extends Dictionary {
	public static final Integer TYPE = new Integer(18);
	public static final String NAME = "ImpPrincipalRegion";
	
	public ImpPrincipalRegion() {
		setType(TYPE);
	}
	
	public static ImpPrincipalRegion loadImpPrincipalRegion(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (ImpPrincipalRegion)dicts[0];
		} else {
			return null;
		}
		
	}
}
