package edu.emory.library.tas;

import edu.emory.library.tas.util.HibernateConnector;

public class Dictionary {

	private Long id;

	private String name;

	private Integer type;
	
	private Integer remoteId;

	public static Dictionary createNew(String p_dictionaryName)
	{
		try
		{
			Class clazz = Class.forName("edu.emory.library.tas.dicts." + p_dictionaryName);
			return (Dictionary) clazz.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Dictionary[] loadDictionary(String p_dictionaryName,
			String p_dictVal) {
		
		return loadDictionaryInternal(p_dictionaryName, "name", p_dictVal);
	}

	public static Dictionary[] loadDictionary(String p_dictionaryName,
			Integer p_dictVal) {
		
		return loadDictionaryInternal(p_dictionaryName, "remoteId", p_dictVal);
	}
	
	private static Dictionary[] loadDictionaryInternal(String p_dictionaryName, String p_attrName, Object p_dictVal) {
		int dictType = -1;
		try {
			Class clazz = Class.forName("edu.emory.library.tas.dicts." + p_dictionaryName);
			dictType = ((Integer)clazz.getField("TYPE").get(null)).intValue();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		Object[] ret = HibernateConnector.getConnector().loadObjects(
				p_dictionaryName, new String[] { p_attrName, "obj_type" },
				new String[] { p_dictVal.toString(), dictType + "" }, new boolean[] { false, false });

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
	
	public static Dictionary[] loadDictionary(String p_dictionaryName) {

		int dictType = -1;
		try {
			Class clazz = Class.forName("edu.emory.library.tas.dicts." + p_dictionaryName);
			dictType = ((Integer)clazz.getField("TYPE").get(null)).intValue();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		Object[] ret = HibernateConnector.getConnector().loadObjects(
				p_dictionaryName, new String[] {"obj_type"}, new String[] {dictType + ""},
				new boolean[] {false});

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
		return "Dictionary val: " + name;
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
}
