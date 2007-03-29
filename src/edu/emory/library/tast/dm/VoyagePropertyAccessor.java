package edu.emory.library.tast.dm;

import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;

/**
 * not used now, maybe in future
 */

public class VoyagePropertyAccessor implements PropertyAccessor
{

	public Getter getGetter(Class theClass, String propertyName) throws PropertyNotFoundException
	{
		return new VoyagePropertyGetter(propertyName);
	}

	public Setter getSetter(Class theClass, String propertyName) throws PropertyNotFoundException
	{
		return null;
	}

}