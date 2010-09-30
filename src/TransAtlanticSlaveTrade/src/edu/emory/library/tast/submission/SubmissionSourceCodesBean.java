package edu.emory.library.tast.submission;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridColumnClickEvent;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Source;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.User;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.util.StringUtils;

public class SubmissionSourceCodesBean {

	private static final String SOURCE_ID_COL_NAME = "source-id";
	private static final String SOURCE_DESC_COL_NAME = "source-description";
	private static final String SOURCE_TYPE_COL_NAME = "source-type";
	private static final String SORTED_COLUMN_CSS_CLASS = "grid-sorted";
	
	private String sourceListSortedColumn = SOURCE_ID_COL_NAME;
	
	private String checkedSourceErrorMessage;
	
	private Long checkedId;
	
	private String checkedSourceId;

	private String checkedName;

	private int checkedType;
	
	
	private String newSourceErrorMessage;
	
	private String newSourceId;

	private String newName;

	private int newType;
	
	///////////////////////////////
	////LOGIN STUFF////////////////
	private String userName;
	private String password;
	public static final String ERROR_LOGIN = "Login error! Try again";

	public static final String ERROR_INACTIVE = "Your account is inactive, please contact us.";
	private String errorMessage;
	private AdminSubmissionBean adminSubmissionBean;
	private SubmissionBean submissionBean;
	
	public SubmissionSourceCodesBean() {

	}
	
	
	public GridRow[] getSourceRows() {
		TastDbConditions c = new TastDbConditions();
		
		TastDbQuery qValue = new TastDbQuery("Source", c);
		
		if(sourceListSortedColumn.equals(SOURCE_DESC_COL_NAME))
		{
			qValue.setOrderBy(new Attribute[] { Source.getAttribute("name") });
			qValue.setOrder(TastDbQuery.ORDER_ASC);
		}
		else if(sourceListSortedColumn.equals(SOURCE_TYPE_COL_NAME)) {
			qValue.setOrderBy(new Attribute[] { Source.getAttribute("type") });
			qValue.setOrder(TastDbQuery.ORDER_ASC);
		}
		else {
			qValue.setOrderBy(new Attribute[] { Source.getAttribute("sourceId") });
			qValue.setOrder(TastDbQuery.ORDER_ASC);
		}
		Object[] sources = qValue.executeQuery();
		if (sources.length == 0) {
			return new GridRow[] {};
		} else {
			GridRow[] response = new GridRow[sources.length];

			for (int i = 0; i < response.length; i++) {
				Source source = (Source)sources[i];

				response[i] = new GridRow(Long.toString(source.getIid()), 
						new String[] { 
							source.getSourceId(), 
							source.getName(), 
							Long.toString(source.getType())
						});
			}
			return response;
		}
	}
	
	public GridColumn[] getSourceColumns() {
		return new GridColumn[] {
				
				new GridColumn(
						SOURCE_ID_COL_NAME,
						"Source Short Code",
						true,
						SOURCE_ID_COL_NAME.equals(sourceListSortedColumn) ?
								SORTED_COLUMN_CSS_CLASS : null), 
				
				new GridColumn( 
						SOURCE_DESC_COL_NAME,
						"Source Description",
						true,
						SOURCE_DESC_COL_NAME.equals(sourceListSortedColumn) ?
								SORTED_COLUMN_CSS_CLASS : null), 
				
				new GridColumn(
						SOURCE_TYPE_COL_NAME,
						"Source Type",
						true,
						SOURCE_TYPE_COL_NAME.equals(sourceListSortedColumn) ?
								SORTED_COLUMN_CSS_CLASS : null), 
				 
		};
	}

	public void onGridColumnClick(GridColumnClickEvent event)
	{
		sourceListSortedColumn = event.getColumnName();
	}
	
	public void editSource(GridOpenRowEvent event) {
		this.checkedId = new Long(event.getRowId());
	}
	
	public String enterEditSource() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Source source = Source.loadById(session, this.checkedId);
		if (source == null) {
			t.commit();
			session.close();
			return null;
		}

		this.checkedSourceId = source.getSourceId();
		this.checkedName = source.getName();
		this.checkedType = source.getType();

		this.checkedSourceErrorMessage = null;
		t.commit();
		session.close();
		return "edit-source";
	}
	
	//Source Codes edit functions.
	
	public void setCheckedSourceErrorMessage(String message) {
		this.checkedSourceErrorMessage = message;
	}
	
	public String getCheckedSourceErrorMessage() {
		return checkedSourceErrorMessage;
	}
	
	public void setCheckedSourceId(String id) {
		this.checkedSourceId = id;
	}
	
	public String getCheckedSourceId() {
		return checkedSourceId;
	}
	
	public void setCheckedName(String name) {
		this.checkedName = name;
	}
	
	public String getCheckedName() {
		return checkedName;
	}
	
	public void setCheckedType(int type) {
		this.checkedType = type;
	}
	
	public int getCheckedType() {
		return checkedType;
	}
	
	public String deleteSource() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Source source = Source.loadById(session, this.checkedId); //get the user
		
		//Do the delete
		session.delete(source);
		t.commit();
		session.close();
		return "main-menu";
	}
	
	public String updateSource() {

		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		Source source = Source.loadById(session, this.checkedId);
		if (source == null) {
			t.commit();
			session.close();
			return null;
		}
		
		String checkedTypeString = null;
		try {
			checkedTypeString = Integer.toString(this.checkedType);
		} catch (NumberFormatException nex) {
			this.checkedSourceErrorMessage = "Source Type must be numeric!";
			return null;
		}
		
		if (StringUtils.isNullOrEmpty(this.checkedName)) {
			this.checkedSourceErrorMessage = "Source Description cannot be empty!";
			return null;
		} else if (StringUtils.isNullOrEmpty(checkedTypeString)) {
			this.checkedSourceErrorMessage = "Source Type cannot be empty!";
			return null;
		}

		source.setSourceId(this.checkedSourceId);
		source.setName(this.checkedName);
		source.setType(this.checkedType);
		
		t.commit();
		session.close();
		this.checkedId = null;
		return "main-menu";
	}
	
	//Code for new source codes.
	public String switchToCreateSource() {
		return "create-source";
	}
	
	public void setNewSourceErrorMessage(String message) {
		this.newSourceErrorMessage = message;
	}
	
	public String getNewSourceErrorMessage() {
		return newSourceErrorMessage;
	}
	
	public void setNewSourceId(String id) {
		this.newSourceId = id;
	}
	
	public String getNewSourceId() {
		return newSourceId;
	}
	
	public void setNewName(String name) {
		this.newName = name;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public void setNewType(int type) {
		this.newType = type;
	}
	
	public int getNewType() {
		return newType;
	}
	
	public String createNewSource() {
		String newTypeString = null;
		try {
			newTypeString = Integer.toString(this.newType);
		} catch (NumberFormatException nex) {
			this.newSourceErrorMessage = "Source Type must be numeric!";
			return null;
		}
		
		if (StringUtils.isNullOrEmpty(this.newSourceId)) {
			this.newSourceErrorMessage = "Source Short Code cannot be empty!";
			return null;
		} else if (StringUtils.isNullOrEmpty(this.newName)) {
			this.newSourceErrorMessage = "Source Description cannot be empty!";
			return null;
		} else if (StringUtils.isNullOrEmpty(newTypeString)) {
			this.newSourceErrorMessage = "Source Type cannot be empty!";
			return null;
		}

		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		
		Source existingSource = Source.loadBySourceId(session, this.newSourceId);
		if (existingSource != null) {
			this.newSourceErrorMessage = "Source Short Code already exists!";	
			return null;
		} else {		
		Source source = new Source(this.newSourceId, this.newName, this.newType);

		this.newSourceErrorMessage = "";
		session.save(source);
		t.commit();
		session.close();
		return "main-menu";
		}
	}
	
	
	//Authentication copied verbatim from SubmissionUserBean.java
	
	public AdminSubmissionBean getAdminSubmissionBean() {
		return adminSubmissionBean;
	}

	public void setAdminSubmissionBean(AdminSubmissionBean adminSubmissionBean) {
		this.adminSubmissionBean = adminSubmissionBean;
	}
	
	public SubmissionBean getSubmissionBean() {
		return submissionBean;
	}

	public void setSubmissionBean(SubmissionBean submissionBean) {
		this.submissionBean = submissionBean;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String refresh() {
		return null;
	}
	
	public String auth() {
		TastDbConditions c = new TastDbConditions();
		c.addCondition(User.getAttribute("userName"), userName, TastDbConditions.OP_EQUALS);
		c.addCondition(User.getAttribute("password"), password, TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("User", c);
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
				if (this.adminSubmissionBean.getAuthenticateduser().isEditor()){
					this.adminSubmissionBean.setSelectedTab("requests");
				}
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
	
	//Copied from SubmissionUserBean.java
	private Submission tryRestoreState(User user) {
		TastDbConditions c = new TastDbConditions();
		c.addCondition(Submission.getAttribute("user"), user, TastDbConditions.OP_EQUALS);
		c.addCondition(Submission.getAttribute("submitted"), new Boolean(false), TastDbConditions.OP_EQUALS);
		c.addCondition(Submission.getAttribute("savedState"), null, TastDbConditions.OP_IS_NOT);
		TastDbQuery qValue = new TastDbQuery("Submission", c);
		Object[] submissions = qValue.executeQuery();
		if (submissions.length != 0) {
			return (Submission) submissions[0];
		}
		return null;
	}
}
