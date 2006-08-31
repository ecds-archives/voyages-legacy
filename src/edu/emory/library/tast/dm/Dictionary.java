package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.HibernateConnector;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Superclass for any dictionary in the application.
 * @author Pawel Jurczyk
 *
 */
public class Dictionary {

	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", "Dictionary"));
		attributes.put("name", new StringAttribute("name", "Dictionary"));
		attributes.put("type", new NumericAttribute("type", "Dictionary"));
		attributes.put("remoteId", new NumericAttribute("remoteId", "Dictionary"));
	}
	
	
	/**
	 * ID of dictionary
	 */
	private Long id;

	/**
	 * Name (Dictionary entry name)
	 */
	private String name;

	/**
	 * Type of dictionary.
	 */
	private Integer type;
	
	/**
	 * ID in the oryginal data (before import).
	 */
	private Integer remoteId;

	/**
	 * Creates new dictionary of given type.
	 * @param p_dictionaryName dictionary name (should be located in ./dicts package)
	 * @return
	 */
	public static Dictionary createNew(String p_dictionaryName)
	{
		try
		{
			Class clazz = Class.forName("edu.emory.library.tas.dm.dictionaries." + p_dictionaryName);
			return (Dictionary) clazz.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loads given dictionary name with given name value
	 * @param p_dictionaryName
	 * @param id
	 * @return dictionary array, empty array if there is no desired dictionary entry
	 */
	public static Dictionary loadDictionaryById(String p_dictionaryName, Long id) {
		
//		Criteria crit = HibernateUtil.getSession().createCriteria("edu.emory.library.tast.dm.dictionaries." + p_dictionaryName);
//		crit.add(Expression.eq("id", id));
//		crit.setMaxResults(1);
//		List list = crit.list();
//		if (list == null || list.size() == 0)
//			return null;
//		return (Dictionary) list.get(0);

//		Object[] ret = HibernateConnector.getConnector().loadObjects(
//				"edu.emory.library.tast.dm.dictionaries." + p_dictionaryName,
//				new String[] { "id" },
//				new String[] { id + "" },
//				new boolean[] { false });
//
//		if (ret.length != 0) {
//			Dictionary[] dict = new Dictionary[ret.length];
//			for (int i = 0; i < dict.length; i++) {
//				dict[i] = (Dictionary) ret[i];
//			}
//			return dict;
//		} else {
//			return new Dictionary[] {};
//		}
		
		Dictionary[] dictionary = loadDictionaryInternal(p_dictionaryName, "id", id);
		if (dictionary == null || dictionary.length == 0)
			return null;
		else
			return dictionary[0];

	}
	

	/**
	 * Loads given dictionary name with given name value
	 * @param p_dictionaryName
	 * @param p_dictVal
	 * @return dictionary array, empty array if there is no desired dictionary entry
	 */
	public static Dictionary[] loadDictionaryByName(String p_dictionaryName,
			String p_dictVal) {
		
		return loadDictionaryInternal(p_dictionaryName, "name", p_dictVal);
	}

	/**
	 * Loads given dictionary with given remote id value.
	 * @param p_dictionaryName
	 * @param remoteId
	 * @return dictionary array, empty array if there is no desired dictionary entry
	 */
	public static Dictionary[] loadDictionaryByRemoteId(String p_dictionaryName,
			Integer remoteId) {
		
		return loadDictionaryInternal(p_dictionaryName, "remoteId", remoteId);
	}
	
	/**
	 * Loads dictionary with desired paraameters.
	 * @param p_dictionaryName
	 * @param p_attrName
	 * @param p_dictVal
	 * @return dictionary array, empty array if there is no desired dictionary entry
	 */
	private static Dictionary[] loadDictionaryInternal(String dictionaryName, String p_attrName, Object p_dictVal)
	{
		
		Conditions conditions = new Conditions();
		conditions.addCondition(Dictionary.getAttribute(p_attrName), p_dictVal, Conditions.OP_EQUALS);
		
		QueryValue qv = new QueryValue(dictionaryName, conditions);
		qv.setOrderBy(new Attribute[] {Dictionary.getAttribute("name")});
		qv.setOrder(QueryValue.ORDER_ASC);
		Object[] ret = qv.executeQuery();
		
		if (ret.length != 0) {
			Dictionary[] dict = new Dictionary[ret.length];
			for (int i = 0; i < dict.length; i++) {
				dict[i] = (Dictionary) ret[i];
			}
			return dict;
		} else {
			return new Dictionary[] {};
		}

	}
	
	
	public static List loadDictionaryList(String dictionaryName)
	{
		QueryValue qv = new QueryValue(dictionaryName);
		qv.setOrderBy(new Attribute[] {Dictionary.getAttribute("name")});
		qv.setOrder(QueryValue.ORDER_ASC);
		return qv.executeQueryList();
	}
	
	/**
	 * Loads all entries of given dictionary type
	 * @param p_dictionaryName
	 * @return
	 */
	public static Dictionary[] loadDictionary(String p_dictionaryName) {

		QueryValue query = new QueryValue(p_dictionaryName);
		Object[] ret = query.executeQuery();

		if (ret.length != 0) {
			Dictionary[] dict = new Dictionary[ret.length];
			for (int i = 0; i < dict.length; i++) {
				dict[i] = (Dictionary) ret[i];
			}
			return dict;
		} else {
			return new Dictionary[] {};
		}
	}

	/**
	 * Constructor
	 *
	 */
	public Dictionary() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public Integer getType() {
		return type;
	}

	protected void setType(Integer type) {
		this.type = type;
	}

	public Integer getRemoteId() {
		return remoteId;
	}

	public void setRemoteId(Integer remoteId) {
		this.remoteId = remoteId;
	}
	
	public void save() {
		HibernateConnector.getConnector().saveObject(this);
	}
	
	public void delete() {
		HibernateConnector.getConnector().deleteObject(this);
	}
	
	public void update() {
		HibernateConnector.getConnector().updateObject(this);
	}
	
	public boolean equals(Object that) {
		if (that instanceof Dictionary) {
			Dictionary dict = (Dictionary)that;
			return dict.getId().equals(this.getId());
		}
		return false;
	}
	
	
	public int hashCode()
	{
		return id.hashCode();
	}
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
}
