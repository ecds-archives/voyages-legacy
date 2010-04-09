/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.dm;

import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.property.Getter;

/**
 * not used now, maybe in future
 */

public class VoyagePropertyGetter implements Getter
{

	private static final long serialVersionUID = 4279478352498578783L;
	
	private String attrName;
	
	public VoyagePropertyGetter(String attrName)
	{
		this.attrName = attrName;
	}

	public Object get(Object voyage) throws HibernateException
	{
		return ((Voyage) voyage).getAttrValue(attrName);
	}

	public Object getForInsert(Object arg0, Map arg1, SessionImplementor arg2) throws HibernateException
	{
		return null;
	}

	public Method getMethod()
	{
		return null;
	}

	public String getMethodName()
	{
		return null;
	}

	public Class getReturnType()
	{
		return null;
	}

}
