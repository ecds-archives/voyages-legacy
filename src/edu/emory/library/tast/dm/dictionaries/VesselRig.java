package edu.emory.library.tast.dm.dictionaries;

import edu.emory.library.tast.dm.Dictionary;

public class VesselRig extends Dictionary {
	public static final Integer TYPE = new Integer(52);
	public static final String NAME = "VesselRig";
	
	public VesselRig() {
		setType(TYPE);
	}
	
	public static VesselRig loadVesselRig(String p_dictVal) {
		Dictionary[] dicts = Dictionary.loadDictionaryByName(NAME, p_dictVal);
		if (dicts.length != 0) {
			return (VesselRig)dicts[0];
		} else {
			return null;
		}
		
	}
}
