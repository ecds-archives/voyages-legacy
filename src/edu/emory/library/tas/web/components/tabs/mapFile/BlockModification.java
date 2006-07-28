package edu.emory.library.tas.web.components.tabs.mapFile;

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
		int begin = ((Integer)markers.get(this.begin)).intValue();
		int end = ((Integer)markers.get(this.end)).intValue() + this.end.length();
		file.replace(begin, end, substitution);
	}

}
