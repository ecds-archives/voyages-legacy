package edu.emory.library.tast.dm.attributes;

import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.User;

public class UserAttribute extends DictionaryAttribute {

	
	
	public UserAttribute(String name, String objType) {
		super(name, objType);
	}

	public Attribute getAttribute(String name) {
		return null;
	}

	public NumericAttribute getIdAttribute() {
		return null;
	}

	public List loadAllObjects(Session sess) {
		return null;
	}

	public Dictionary loadObjectById(Session sess, long id) {
		return null;
	}

	public Class getDictionayClass()
	{
		return User.class;
	}

}
