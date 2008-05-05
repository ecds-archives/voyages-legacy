package edu.emory.library.tast.submission;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridColumnClickEvent;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionEditor;
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
	
	private static final String USER_NAME_COL_NAME = "user-name";
	private static final String FIRST_NAME_COL_NAME = "first-name";
	private static final String LAST_NAME_COL_NAME = "last-name";
	private static final String SORTED_COLUMN_CSS_CLASS = "grid-sorted";
	
	private SubmissionBean submissionBean;

	private AdminSubmissionBean adminSubmissionBean;

	private String errorMessage;

	///////////////////////////////
	////LOGIN STUFF////////////////
	private String userName;
	private String password;
	
	///////////////////////////////////
	/////////Admin user edit STUFF/////
	private Long checkedUserId;

	private String checkedUserName;

	private String checkedPassword;

	private String checkedFirstName;
	
	private String checkedLastName;
	
	private String checkedEmail;
	
	private String checkedInstitution;
	
	private String checkedDate;

	private Boolean checkedActive;
	
	private Boolean checkedEditor;
	
	private Boolean checkedChiefEditor;
	
	private String checkedUserErrorMessage;
	
	private String userListSortedColumn = USER_NAME_COL_NAME;

	
	//////////////////////////////////
	////New user stuff////////////////
	private String newUserName;

	private String newUserFirstName;

	private String newUserLastName;

	private String newUserInstitution;

	private String newUserEmail;

	private String newUserVerificationString;

	private String verificationString;

	private String newUserErrorMessage;

	private String newUserPassword;

	private String newUserPasswordAgain;

	
	////////////////////////////////////
	/////Admin users list stuff/////////
	public static final SelectItem[] accountTypes = new SelectItem[] {
		new SelectItem("1", "All accounts"),
		new SelectItem("2", "Only active accounts"),
		new SelectItem("3", "Only inactive accounts")
	};
	
	private String accountType;
	
	public SubmissionUsersBean() {
		char[] verif = new char[6];
		for (int i = 0; i < verif.length; i++) {
			verif[i] = (char) (Math.random()* (double)26 + 65);
		}
		this.verificationString = new String(verif);
	}
	
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
			if (user.isAdmin() || user.isEditor() || user.isChiefEditor()) {
				this.adminSubmissionBean.setAuthenticateduser(user);
				return "admin-logged-in";
			} else {
				this.submissionBean.setAuthenticatedUser(user);
				Submission submission = tryRestoreState(user);
				if (submission != null) {
					this.submissionBean.setSubmission(submission);
					return submission.getSavedState();
				}
				return "logged-in";
			}
		}
	}

	private Submission tryRestoreState(User user) {
		Conditions c = new Conditions();
		c.addCondition(Submission.getAttribute("user"), user, Conditions.OP_EQUALS);
		c.addCondition(Submission.getAttribute("submitted"), new Boolean(false), Conditions.OP_EQUALS);
		c.addCondition(Submission.getAttribute("savedState"), null, Conditions.OP_IS_NOT);
		QueryValue qValue = new QueryValue("Submission", c);
		Object[] submissions = qValue.executeQuery();
		if (submissions.length != 0) {
			return (Submission) submissions[0];
		}
		return null;
	}

	public GridColumn[] getUserColumns() {
		return new GridColumn[] {
				
				new GridColumn(
						USER_NAME_COL_NAME,
						"User name",
						true,
						USER_NAME_COL_NAME.equals(userListSortedColumn) ?
								SORTED_COLUMN_CSS_CLASS : null), 
				
				new GridColumn( 
						FIRST_NAME_COL_NAME,
						"First name",
						true,
						FIRST_NAME_COL_NAME.equals(userListSortedColumn) ?
								SORTED_COLUMN_CSS_CLASS : null), 
				
				new GridColumn(
						LAST_NAME_COL_NAME,
						"Last name",
						true,
						LAST_NAME_COL_NAME.equals(userListSortedColumn) ?
								SORTED_COLUMN_CSS_CLASS : null), 
				
				new GridColumn("E-mail"), 
				
				new GridColumn("User active"),
				
				new GridColumn("Reviewer"),
				
				new GridColumn("Chief editor"),
				
				new GridColumn("Number of requests") 
		};
	}
	
	public void onGridColumnClick(GridColumnClickEvent event)
	{
		userListSortedColumn = event.getColumnName();
	}

	public GridRow[] getUserRows() {
		Conditions c = new Conditions();
		//c.addCondition(User.getAttribute("admin"), new Boolean(false), Conditions.OP_EQUALS);
		if ("2".equals(this.accountType)) {
			c.addCondition(User.getAttribute("enabled"), new Boolean(true), Conditions.OP_EQUALS);
		} else if ("3".equals(this.accountType)) {
			c.addCondition(User.getAttribute("enabled"), new Boolean(false), Conditions.OP_EQUALS);
		}
		
		QueryValue qValue = new QueryValue("User", c);
		qValue.setOrderBy(new Attribute[] { User.getAttribute("userName") });
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
				qValue.addPopulatedAttribute(new FunctionAttribute("count", new Attribute[] { Submission.getAttribute("id") }));
				Object[] res = qValue.executeQuery();
				String numPosts = "N/A";
				if (res.length != 0) {
					numPosts = ((Long) res[0]).toString();
				}

				
				boolean editor = false;
				boolean chiefEditor = false;
				if (user.isChiefEditor()) {
					chiefEditor = true;
				} else if (user.isEditor()) {
					editor = true;
				}
				response[i] = new GridRow(user.getId().toString(), 
						new String[] { 
							user.getUserName(), 
							user.getFirstName(), 
							user.getLastName(), 
							user.getEmail(),
							user.isEnabled() ? "Yes" : "No",
							editor ? "Yes" : "No",
							chiefEditor ? "Yes" : "No",
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

	public void editUser(GridOpenRowEvent event) {
		this.checkedUserId = new Long(event.getRowId());
	}
	
	public String refresh() {
		return null;
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
		this.checkedEditor = new Boolean(user.isEditor());
		this.checkedChiefEditor = new Boolean(user.isChiefEditor());
		this.checkedFirstName = user.getFirstName();
		this.checkedLastName = user.getLastName();
		this.checkedInstitution = user.getInstitution();
		this.checkedEmail = user.getEmail();
		this.checkedUserErrorMessage = null;
		t.commit();
		session.close();
		return "edit-user";
	}

	public String deleteUser() {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		User user = User.loadById(session, this.checkedUserId);
		if (user.getSubmissions().size() != 0) {
			this.checkedUserErrorMessage = "Cannot delete user who has submitted some requests.";
			return null;
		}
		session.delete(user);
		t.commit();
		session.close();
		return "main-menu";
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
		user.setEditor(this.checkedEditor.booleanValue());
		boolean chiefEditor = this.checkedChiefEditor.booleanValue();
		if (chiefEditor) {
			user.setChiefEditor(chiefEditor);
			user.setAdmin(chiefEditor);
			user.setEditor(false);
		}
		user.setUserName(this.checkedUserName);
		user.setPassword(this.checkedPassword);
		user.setEmail(this.checkedEmail);
		user.setFirstName(this.checkedFirstName);
		user.setLastName(this.checkedLastName);
		
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

	// /////////////////////////////////////////////////////////////
	// / NEW USER REGISTRATION /////////////////////////////////////
	// /////////////////////////////////////////////////////////////

	public String getNewUserEmail() {
		return newUserEmail;
	}

	public void setNewUserEmail(String newUserEmail) {
		this.newUserEmail = newUserEmail;
	}

	public String getNewUserFirstName() {
		return newUserFirstName;
	}

	public void setNewUserFirstName(String newUserFirstName) {
		this.newUserFirstName = newUserFirstName;
	}

	public String getNewUserInstitution() {
		return newUserInstitution;
	}

	public void setNewUserInstitution(String newUserInstitution) {
		this.newUserInstitution = newUserInstitution;
	}

	public String getNewUserLastName() {
		return newUserLastName;
	}

	public void setNewUserLastName(String newUserLastName) {
		this.newUserLastName = newUserLastName;
	}

	public String getNewUserVerificationString() {
		return newUserVerificationString;
	}

	public void setNewUserVerificationString(String newUserVerificationString) {
		this.newUserVerificationString = newUserVerificationString;
	}

	public String getVerificationString() {
		return verificationString;
	}
	
	public String getNewUserErrorMessage() {
		return newUserErrorMessage;
	}

	public String getNewUserPassword() {
		return newUserPassword;
	}

	public void setNewUserPassword(String newUserNamePassword) {
		this.newUserPassword = newUserNamePassword;
	}

	public String getNewUserPasswordAgain() {
		return newUserPasswordAgain;
	}

	public void setNewUserPasswordAgain(String newUserPasswordAgain) {
		this.newUserPasswordAgain = newUserPasswordAgain;
	}

	public String createNewUser() {
		if (StringUtils.isNullOrEmpty(this.newUserName)) {
			this.newUserErrorMessage = "User name cannot be empty!";
			return null;
		} else if (StringUtils.isNullOrEmpty(this.newUserFirstName)) {
			this.newUserErrorMessage = "User's first name cannot be empty!";
			return null;
		} else if (StringUtils.isNullOrEmpty(this.newUserLastName)) {
			this.newUserErrorMessage = "User's last name cannot be empty!";
			return null;
		} else if (StringUtils.isNullOrEmpty(this.newUserPassword)) {
			this.newUserErrorMessage = "Password cannot be empty!";
			return null;
		} else if (StringUtils.isNullOrEmpty(this.newUserEmail)) {
			this.newUserErrorMessage = "Email cannot be empty!";
			return null;
		} else if (!this.newUserPassword.equals(this.newUserPasswordAgain)) {
			this.newUserErrorMessage = "Passwords have to match!";
			return null;
		} else if (!this.verificationString.equals(this.newUserVerificationString)) {
			this.newUserErrorMessage = "Verification strings have to match!";
			return null;
		}

		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		
		User existingUser = User.loadByName(session, this.newUserName);
		if (existingUser != null) {
			this.newUserErrorMessage = "User name already exists!";	
			return null;
		} else {		
		User user = new User(this.newUserName, this.newUserPassword);
		user.setEnabled(false);
		user.setCreateDate(new Date());
		user.setEmail(this.newUserEmail);
		user.setFirstName(this.newUserFirstName);
		user.setLastName(this.newUserLastName);
		user.setInstitution(this.newUserInstitution);
		this.newUserErrorMessage = "";
		session.save(user);
		t.commit();
		session.close();
		return "created";
		}
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public SelectItem[] getAccountTypes() {
		return accountTypes;
	}

	public String getCheckedEmail() {
		return checkedEmail;
	}

	public void setCheckedEmail(String checkedEmail) {
		this.checkedEmail = checkedEmail;
	}

	public String getCheckedFirstName() {
		return checkedFirstName;
	}

	public void setCheckedFirstName(String checkedFistName) {
		this.checkedFirstName = checkedFistName;
	}

	public String getCheckedInstitution() {
		return checkedInstitution;
	}

	public void setCheckedInstitution(String checkedInstitution) {
		this.checkedInstitution = checkedInstitution;
	}

	public String getCheckedLastName() {
		return checkedLastName;
	}

	public void setCheckedLastName(String checkedLastName) {
		this.checkedLastName = checkedLastName;
	}

	public String getCheckedUserErrorMessage() {
		return checkedUserErrorMessage;
	}

	public SelectItem[] getEditorUsers() {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			Long id = this.adminSubmissionBean.getSubmissionId();
			Submission submission = Submission.loadById(session, id);
			Set currEditors = submission.getSubmissionEditors();
			Object[] existingUsers = new Object[currEditors.size()];
			Iterator iter = currEditors.iterator();
			for (int i = 0; i < existingUsers.length; i++) {
				existingUsers[i] = ((SubmissionEditor)iter.next()).getUser().getId();
			}

			Conditions c = new Conditions();
			if (existingUsers.length != 0) {
				Conditions cnot = new Conditions(Conditions.NOT);
				cnot.addCondition(User.getAttribute("id"), existingUsers, Conditions.OP_IN);
				c.addCondition(cnot);
			}
			c.addCondition(User.getAttribute("editor"), new Boolean(true), Conditions.OP_EQUALS);
			QueryValue qValue = new QueryValue("User", c);
			Object[] users = qValue.executeQuery(session);
			SelectItem[] items = new SelectItem[users.length];
			for (int i = 0; i < items.length; i++) {
				items[i] = new SelectItem(((User)users[i]).getId().toString(), 
					((User)users[i]).getUserName());
			}
			return items;
		} finally {
			t.commit();
			session.close();
		}
	}

	public Boolean getCheckedEditor() {
		return checkedEditor;
	}

	public void setCheckedEditor(Boolean checkedEditor) {
		this.checkedEditor = checkedEditor;
	}

	public Boolean getCheckedChiefEditor() {
		return checkedChiefEditor;
	}

	public void setCheckedChiefEditor(Boolean checkedChiefEditor) {
		this.checkedChiefEditor = checkedChiefEditor;
	}
	
}
