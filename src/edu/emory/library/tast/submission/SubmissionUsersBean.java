package edu.emory.library.tast.submission;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.dm.User;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class SubmissionUsersBean {
	
	public static final String ERROR_LOGIN = "Login error! Try again";
	public static final String ERROR_INACTIVE = "Your account is inactive, please contact us.";
	
	private String userName;
	private String password;
	private SubmissionBean submissionBean;
	private AdminSubmissionBean adminSubmissionBean;
	private String errorMessage;
	
	private String newUserName;
	private String newUserPassword;
	
	public String auth() {
		Conditions c = new Conditions();
		c.addCondition(User.getAttribute("userName"), userName, Conditions.OP_EQUALS);
		c.addCondition(User.getAttribute("password"), password, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("User", c);
		Object[] users = qValue.executeQuery();
		if (users.length == 0) {
			this.errorMessage = ERROR_LOGIN;
			return null;
		} else {
			User user = (User) users[0];
			if (!user.isEnabled()) {
				this.errorMessage = ERROR_INACTIVE;
				return null;
			}
			this.errorMessage = "";
			if (user.isAdmin() || user.isEditor()) {
				this.adminSubmissionBean.setAuthenticateduser(user);
				return "admin-logged-in";
			} else {
				this.submissionBean.setAuthenticatedUser(user);
				return "logged-in";
			}
		}
	}
	
	public GridColumn[] getUserColumns() {
		return new GridColumn[] {
				new GridColumn("User name"),
				new GridColumn("Password"),
				new GridColumn("User active")
		};
	}
	
	public GridRow[] getUserRows() {
		Conditions c = new Conditions();
		c.addCondition(User.getAttribute("editor"), new Boolean(false), Conditions.OP_EQUALS);
		c.addCondition(User.getAttribute("admin"), new Boolean(false), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("User", c);
		qValue.setOrderBy(new Attribute[] {User.getAttribute("userName")});
		qValue.setOrder(QueryValue.ORDER_ASC);
		Object[] users = qValue.executeQuery();
		if (users.length == 0) {
			return new GridRow[] {};
		} else {
			GridRow[] response = new GridRow[users.length];
			for (int i = 0; i < response.length; i++) {
				User user = (User) users[i];
				response[i] = new GridRow(user.getId().toString(), new String[] {
					user.getUserName(),
					user.getPassword(),
					user.isEnabled() ? "Yes":"No"
				});
			}
			return response;
		}
	}
		
	public AdminSubmissionBean getAdminSubmissionBean() {
		return adminSubmissionBean;
	}
	public void setAdminSubmissionBean(AdminSubmissionBean adminSubmissionBean) {
		this.adminSubmissionBean = adminSubmissionBean;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public SubmissionBean getSubmissionBean() {
		return submissionBean;
	}
	public void setSubmissionBean(SubmissionBean submissionBean) {
		this.submissionBean = submissionBean;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getNewUserName() {
		return newUserName;
	}

	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}

	public String getNewUserPassword() {
		return newUserPassword;
	}

	public void setNewUserPassword(String newUserPassword) {
		this.newUserPassword = newUserPassword;
	}
	
	public String createNewUser() {
		if (StringUtils.isNullOrEmpty(this.newUserName) || 
				StringUtils.isNullOrEmpty(this.newUserPassword)) {
			return null;
		}
		
		User user = new User(this.newUserName, this.newUserPassword);
		user.setEnabled(true);
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		session.save(user);
		t.commit();
		session.close();		
		this.newUserName = "";
		this.newUserPassword = "";
		return null;
	}
	
}
