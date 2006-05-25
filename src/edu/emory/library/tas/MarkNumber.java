package edu.emory.library.tas;

public class MarkNumber extends Dictionary {

	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "MarkNumber";
	
	public MarkNumber() {
		setType(TYPE);
	}
	
	public static MarkNumber loadMarkNumber(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (MarkNumber)dicts[0];
		} else {
			return null;
		}
		
	}
}
