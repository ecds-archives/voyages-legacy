package edu.emory.library.tas.web.components.tabs.mapFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class BlockModification implements Modification {

	private String begin;
	private String end;
	private String substitution;

	public BlockModification(String begin, String end, String substitution) {
		this.begin = begin;
		this.end = end;
		this.substitution = substitution;
	}
	
	public void apply(StringBuffer file, Map markers) {
		ArrayList begins = (ArrayList)markers.get(this.begin);
		ArrayList ends = (ArrayList)markers.get(this.end);
		Iterator endsiter = ends.iterator();
		for (Iterator beginsiter = begins.iterator(); beginsiter.hasNext() && endsiter.hasNext();) {
			Integer begin = (Integer) beginsiter.next();
			Integer end = (Integer) endsiter.next();
			int begini = begin.intValue();
			int endi = end.intValue() + this.end.length();
			file.replace(begini, endi, substitution);
		}
		
	}

}
