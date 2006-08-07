package edu.emory.library.tast.ui.maps.mapfile;

import java.util.Map;

public abstract class Modification {
	
	public String beginKey;
	
	protected Modification(String beginIndex) {
		this.beginKey = beginIndex;
	}
	
	public abstract int apply(StringBuffer file, Integer modificationIndex, Map markers, int offset);

	public String getBeginKey() {
		return beginKey;
	}		
}
