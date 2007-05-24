package edu.emory.library.tast.dm;

import org.w3c.dom.Node;

public interface XMLExportable {
	public String toXML();
	public void restoreFromXML(Node entry);
}
