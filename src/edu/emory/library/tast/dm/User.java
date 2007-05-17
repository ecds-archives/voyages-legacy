package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class User {
	
	private Long id;
	private String userName;
	private String password;
	private boolean editor;
	private boolean admin;
	private boolean enabled;
	
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("userName", new StringAttribute("userName", null));
		attributes.put("password", new StringAttribute("password", null));
		attributes.put("editor", new BooleanAttribute("editor", "User", null));
		attributes.put("admin", new BooleanAttribute("admin", "User", null));
		attributes.put("enabled", new BooleanAttribute("enabled", "User", null));
	}
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public User() {}
	
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public boolean isEditor() {
		return editor;
	}
	public void setEditor(boolean editor) {
		this.editor = editor;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
}
