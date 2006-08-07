package edu.emory.library.tast.ui.maps.mapfile;

import java.util.Map;

public class SimpleModification extends Modification {

	private String key;

	private String substitution;

	public SimpleModification(String key, String substitution) {
		super(key);
		this.key = key;
		this.substitution = substitution;
	}

	public int apply(StringBuffer file, Integer element, Map markers, int offset) {

		int begin = element.intValue() + offset;
		int end = begin + this.key.length();
		file.replace(begin, end, substitution);
		offset +=  substitution.length() - (end - begin);

		return offset;
	}

}
