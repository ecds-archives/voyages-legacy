package edu.emory.library.tast.dm;

import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.property.Getter;

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
