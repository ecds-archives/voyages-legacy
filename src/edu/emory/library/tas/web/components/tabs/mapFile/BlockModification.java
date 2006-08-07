package edu.emory.library.tas.web.components.tabs.mapFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class BlockModification extends Modification {

	private String begin;
	private String end;
	private String substitution;

	public BlockModification(String begin, String end, String substitution) {
		super(begin);
		this.begin = begin;
		this.end = end;
		this.substitution = substitution;
	}
	
	public int apply(StringBuffer file, Integer modificationIndex, Map markers, int offset) {
		
		ArrayList ends = (ArrayList)markers.get(this.end);
		//Integer begin = (Integer) beginsiter.next();
		
		Integer end = getFirstAfter(modificationIndex.intValue(), ends);
		int begini = modificationIndex.intValue() + offset;
		int endi = end.intValue() + this.end.length() + offset;
		file.replace(begini, endi, substitution);
		offset += -(endi - begini) + substitution.length();
		
		return offset;
	}

	private Integer getFirstAfter(int modificationIndex, ArrayList ends) {
		for (Iterator iter = ends.iterator(); iter.hasNext();) {
			Integer element = (Integer) iter.next();
			if (modificationIndex < element.intValue()) {
				return element; 
			}
		}
		return null;
	}


}
