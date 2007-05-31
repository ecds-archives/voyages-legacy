package edu.emory.library.tast.dm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.types.CommandlineJava;
import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class User {
	
	private Long id;
	private String userName;
	private String password;
	private boolean editor;
	private boolean admin;
	private boolean enabled;
	private Date createDate;
	
	private String firstName;
	private String lastName;
	private String institution;
	private String email;
	
	private Set submissions;
	
	
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

	public static User loadById(Session session, Long checkedUserId) {
		return (User) Dictionary.loadById(User.class, session, checkedUserId.longValue());
	}
	
	public static User loadByName(Session session, String newUserName) {
		Conditions c = new Conditions();
		c.addCondition(getAttribute("userName"), newUserName, Conditions.OP_EQUALS);
		QueryValue qVal = new QueryValue("User", c);
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

	public Set getSubmissions() {
		return submissions;
	}	
	
	public void setSubmissions(Set submissions) {
		this.submissions = submissions;
	}
}
