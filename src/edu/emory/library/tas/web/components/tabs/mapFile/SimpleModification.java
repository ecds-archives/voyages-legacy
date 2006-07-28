package edu.emory.library.tas.web.components.tabs.mapFile;

import java.util.Map;

public class SimpleModification implements Modification {

	private String key;
	private String substitution;
	
	public SimpleModification(String key, String substitution) {
		this.key = key;
		this.substitution = substitution;
	}
	
	public void apply(StringBuffer file, Map markers) {
		int begin = ((Integer)markers.get(this.key)).intValue();
		int end = begin + this.key.length();
		file.replace(begin, end, substitution);
	}

}
