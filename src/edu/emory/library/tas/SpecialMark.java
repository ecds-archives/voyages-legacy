package edu.emory.library.tas;

public class SpecialMark extends Dictionary {

	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "SpecialMark";
	
	public SpecialMark() {
		setType(TYPE);
	}
	
	public static SpecialMark loadSpecialMark(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (SpecialMark)dicts[0];
		} else {
			return null;
		}
		
	}
}
