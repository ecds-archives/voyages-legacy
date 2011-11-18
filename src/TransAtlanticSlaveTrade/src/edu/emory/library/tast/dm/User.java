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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class User {
	
	private Long id;
	private String userName;
	private String password;
	private boolean chiefEditor;
	private boolean editor;
	private boolean admin;
	private boolean enabled;
	private Date createDate;
	
	private String firstName;
	private String lastName;
	private String institution;
	private String email;
	private String phone1;
	private String phone2;
	private String description;
	
	private Set submissions;
	
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
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
		try{
			return admin;
		}catch(Exception e){
			return false;
		}
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

	public static User loadById(Session session, Long checkedUserId) {
		return (User) Dictionary.loadById(User.class, session, checkedUserId.longValue());
	}
	
	public static User loadByName(Session session, String newUserName) {
		TastDbConditions c = new TastDbConditions();
		c.addCondition(getAttribute("userName"), newUserName, TastDbConditions.OP_EQUALS);
		TastDbQuery qVal = new TastDbQuery("User", c);
		Object[] objects = qVal.executeQuery(session);
		if (objects.length == 0) {
			return null;
		}
		return (User) objects[0];
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1=phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2=phone2;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description=description;
	}
	
	public Set getSubmissions() {
		return submissions;
	}	
	
	public void setSubmissions(Set submissions) {
		this.submissions = submissions;
	}

	public boolean isChiefEditor() {
		try{
			return chiefEditor;
		}catch(Exception e){
			return false;
		}
	}

	public void setChiefEditor(boolean chiefEditor) {
		this.chiefEditor = chiefEditor;
	}
}
