package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class Nation extends DictionaryOrdered
{

	private static Map attributes = new HashMap();
	static
	{
		attributes.put("id", new NumericAttribute("id", "Nation", NumericAttribute.TYPE_LONG));
		attributes.put("name", new StringAttribute("name", "Nation"));
		attributes.put("order", new NumericAttribute("order", "Nation", NumericAttribute.TYPE_INTEGER));
	}
	
	public static List loadAll(Session sess)
	{
		return Dictionary.loadAll(Fate.class, sess);
	}
	
	public static Nation loadById(Session sess, long nationId)
	{
		return (Nation) Dictionary.loadById(Nation.class, sess, nationId);
	}
	
	public static Attribute getAttribute(String name)
	{
		return (Attribute)attributes.get(name);
	}
	
	public static String[] nationNamesToArray(List nations)
	{
		return nationNamesToArray(nations, null);
	}

	public static String[] nationNamesToArray(List nations, Set includeOnly)
	{
		String[] names = new String[nations.size()];
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			if (includeOnly == null || includeOnly.contains(nation.getId()))
			{
				names[i++] = nation.getName();
			}
		}
		
		return names;
	}

	public static Map createIdIndexMap(List nations)
	{
		Map map = new HashMap();
		
		int i = 0;
		for (Iterator iter = nations.iterator(); iter.hasNext();)
		{
			Nation nation = (Nation) iter.next();
			map.put(nation.getId(), new Integer(i++));
		}
		
		return map;
	}

}