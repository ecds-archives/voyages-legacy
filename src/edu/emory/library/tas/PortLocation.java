package edu.emory.library.tas;

public class PortLocation extends Dictionary {
	
	private static final Integer PORTLOC_TYPE = new Integer(1);
	private static final String PORTLOC_NAME = "PortLocation";
	
	public PortLocation() {
		setType(PORTLOC_TYPE);
	}
	
	public static PortLocation loadPortLocation(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(PORTLOC_NAME, p_dictVal);
		if (dicts.length != 0) {
			return (PortLocation)dicts[0];
		} else {
			return null;
		}
		
	}
}
