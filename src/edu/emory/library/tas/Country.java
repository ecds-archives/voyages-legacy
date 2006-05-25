package edu.emory.library.tas;

public class Country extends Dictionary {
	
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "Country";
	
	public Country() {
		setType(TYPE);
	}
	
	public static Country loadCountry(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (Country)dicts[0];
		} else {
			return null;
		}
		
	}
}
