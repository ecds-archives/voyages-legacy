package edu.emory.library.tas.web.components.tabs.mapFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SimpleModification implements Modification {

	private String key;
	private String substitution;
	
	public SimpleModification(String key, String substitution) {
		this.key = key;
		this.substitution = substitution;
	}
	
	public void apply(StringBuffer file, Map markers) {
		ArrayList list = (ArrayList)markers.get(this.key);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Integer element = (Integer) iter.next();
			int begin = element.intValue();
			int end = begin + this.key.length();
			file.replace(begin, end, substitution);
		}
	}

}
