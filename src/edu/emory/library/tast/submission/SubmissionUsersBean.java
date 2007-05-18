package edu.emory.library.tast.submission;

import java.util.Date;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.User;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
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
	
	private Long checkedUserId;
	private String checkedUserName;
	private String checkedPassword;
	private String checkedDate;
	private Boolean checkedActive;
	
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
				new GridColumn("User active"),
				new GridColumn("Number of requests")
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
				
				c = new Conditions();
				c.addCondition(Submission.getAttribute("user"), user, Conditions.OP_EQUALS);
				qValue = new QueryValue("Submission", c);
				qValue.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] {Submission.getAttribute("id")}));
				Object[] res = qValue.executeQuery();
				String numPosts = "N/A";
				if (res.length != 0) {
					numPosts = ((Long)res[0]).toString();
				}
				
				response[i] = new GridRow(user.getId().toString(), new String[] {
					user.getUserName(),
					user.getPassword(),
					user.isEnabled() ? "Yes":"No",
					numPosts
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
		user.setCreateDate(new Date());
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		session.save(user);
		t.commit();
		session.close();		
		this.newUserName = "";
		this.newUserPassword = "";
		return null;
	}
		
	public void editUser(GridOpenRowEvent event) {
		this.checkedUserId = new Long(event.getRowId());
	}
	
	public String enterEditUser() {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		User user = User.loadById(session, this.checkedUserId);
		if (user == null) {
			t.commit();
			session.close();
			return null;
		}
		
		this.checkedUserName = user.getUserName();		
		this.checkedPassword = user.getPassword();
		this.checkedDate = user.getCreateDate().toString();
		this.checkedActive = new Boolean(user.isEnabled());
		t.commit();
		session.close();
		return "edit-user";
	}
	
	public String updateUser() {
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		User user = User.loadById(session, this.checkedUserId);
		if (user == null) {
			t.commit();
			session.close();
			return null;
		}
		user.setEnabled(this.checkedActive.booleanValue());
		user.setUserName(this.checkedUserName);
		user.setPassword(this.checkedPassword);
		
		t.commit();
		session.close();
		this.checkedUserId = null;
		return "main-menu";
	}

	public Boolean getCheckedActive() {
		return checkedActive;
	}

	public void setCheckedActive(Boolean checkedActive) {
		this.checkedActive = checkedActive;
	}

	public String getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(String checkedDate) {
		this.checkedDate = checkedDate;
	}

	public String getCheckedPassword() {
		return checkedPassword;
	}

	public void setCheckedPassword(String checkedPassword) {
		this.checkedPassword = checkedPassword;
	}

	public Long getCheckedUserId() {
		return checkedUserId;
	}

	public void setCheckedUserId(Long checkedUserId) {
		this.checkedUserId = checkedUserId;
	}

	public String getCheckedUserName() {
		return checkedUserName;
	}

	public void setCheckedUserName(String checkedUserName) {
		this.checkedUserName = checkedUserName;
	}
	
}
