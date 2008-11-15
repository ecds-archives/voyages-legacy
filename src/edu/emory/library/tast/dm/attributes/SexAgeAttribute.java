package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.SexAge;

public class SexAgeAttribute extends DictionaryAttribute {

	public SexAgeAttribute(String name, String objType) {
		super(name, objType);
	}
	
	public Attribute getAttribute(String name) {
		return SexAge.getAttribute(name);
	}

	public NumericAttribute getIdAttribute() {
		return (NumericAttribute) SexAge.getAttribute("id");
	}

	public List loadAllObjects(Session sess) {
		return null;
	}

	public Dictionary loadObjectById(Session sess, long id) {
		return null;
	}

	public Class getDictionayClass()
	{
		return SexAge.class;
	}

}
