package edu.emory.library.tas.web.components.tabs.mapFile;

import java.util.Map;

public interface Modification {
	public void apply(StringBuffer file, Map markers);
}
