package edu.emory.library.tas.dicts;

import edu.emory.library.tas.Dictionary;

public class VesselRig extends Dictionary {
	private static final Integer TYPE = new Integer(2);
	private static final String NAME = "VesselRig";
	
	public VesselRig() {
		setType(TYPE);
	}
	
	public static VesselRig loadVesselRig(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionary(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (VesselRig)dicts[0];
		} else {
			return null;
		}
		
	}
}
