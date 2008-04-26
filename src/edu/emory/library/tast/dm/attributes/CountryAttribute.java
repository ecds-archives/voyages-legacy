package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Country;
import edu.emory.library.tast.dm.Dictionary;

public class CountryAttribute extends DictionaryAttribute {

	public CountryAttribute(String name, String objType) {
		super(name, objType);
	}
	
	public Attribute getAttribute(String name) {
		return Country.getAttribute(name);
	}

	public NumericAttribute getItAttribute() {
		return (NumericAttribute) Country.getAttribute("id");
	}

	public List loadAllObjects(Session sess) {
		return null;
	}

	public Dictionary loadObjectById(Session sess, long id) {
		return null;
	}

	public Class getDictionayClass()
	{
		return Country.class;
	}

}
