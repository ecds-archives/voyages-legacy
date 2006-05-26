package edu.emory.library.tas;

import edu.emory.library.tas.util.HibernateConnector;

public class Dictionary {

	private Long id;

	private String name;

	private Integer type;
	
	private Integer remoteId;

	public static Dictionary[] loadDictionary(String p_dictionaryName,
			String p_dictVal) {

		Object[] ret = HibernateConnector.getConnector().loadObjects(
				p_dictionaryName, new String[] { "name" },
				new String[] { p_dictVal }, new boolean[] { true });

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

		Object[] ret = HibernateConnector.getConnector().loadObjects(
				p_dictionaryName, new String[] {}, new String[] {},
				new boolean[] {});

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

	private void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
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
}
