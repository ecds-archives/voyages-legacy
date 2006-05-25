package edu.emory.library.tas;

public class SexAge extends Dictionary {

	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "SexAge";
	
	public SexAge() {
		setType(TYPE);
	}
	
	public static SexAge loadSexAge(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (SexAge)dicts[0];
		} else {
			return null;
		}
		
	}
}
