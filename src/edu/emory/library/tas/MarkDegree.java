package edu.emory.library.tas;

public class MarkDegree extends Dictionary {

	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "MarkDegree";
	
	public MarkDegree() {
		setType(TYPE);
	}
	
	public static MarkDegree loadMarkDegree(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (MarkDegree)dicts[0];
		} else {
			return null;
		}
		
	}
}
