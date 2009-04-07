package edu.emory.library.tast.submission;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionSource;
import edu.emory.library.tast.dm.SubmissionSourceBook;
import edu.emory.library.tast.dm.SubmissionSourceOther;
import edu.emory.library.tast.dm.SubmissionSourcePaper;
import edu.emory.library.tast.dm.SubmissionSourcePrimary;
import edu.emory.library.tast.submission.sources.SourceArticle;
import edu.emory.library.tast.submission.sources.SourceBook;
import edu.emory.library.tast.submission.sources.SourceOther;
import edu.emory.library.tast.submission.sources.SourcePrimary;

public class SourcesBean {

	private static final int NEW_SOURCE_BOOK = 1;
	private static final int NEW_SOURCE_ARTICLE = 2;
	private static final int NEW_SOURCE_OTHER = 3;
	private static final int NEW_SOURCE_PRIMARY = 4;
	
	private int newSourceType = -1;
	
	private Submission submission;
	
	private String errorMessage = null;
	
	///////////////////////////////////
	/////////New article stuff/////////
	private SourceArticle newArticle;
	
	///////////////////////////////////
	/////////New book stuff////////////
	private SourceBook newBook;
	
	///////////////////////////////////
	/////////New book stuff////////////
	private SourceOther newOther;
	
	///////////////////////////////////
	/////New primary source stuff//////
	private SourcePrimary newPrimary;
	
	private boolean editMode = false;
	
	private Long editedId = null;
	
	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}
	
	public int getNewSourceType() {
		return this.newSourceType;
	}
	
	public String primarySource() {
		this.newSourceType = NEW_SOURCE_PRIMARY;
		return null;
	}
	
	public String bookSource() {
		this.newSourceType = NEW_SOURCE_BOOK;
		return null;
	}
	
	public String articleSource() {
		this.newSourceType = NEW_SOURCE_ARTICLE;
		return null;
	}
	
	public String otherSource() {
		this.newSourceType = NEW_SOURCE_OTHER;
		return null;
	}
	
	public String addSource() {
		System.out.println("Add source..:" + this.newSourceType);
		switch (this.newSourceType) {
		case NEW_SOURCE_ARTICLE:
			this.addNewArticle(this.editMode);
			break;
		case NEW_SOURCE_BOOK:
			this.addNewBook(this.editMode);
			break;
		case NEW_SOURCE_PRIMARY:
			this.addNewPrimary(this.editMode);
			break;
		case NEW_SOURCE_OTHER:
			this.addNewOther(this.editMode);
			break;
		}
		if (this.errorMessage != null) {
			return null;
		}
		this.newSourceType = -1;
		this.editMode = false;
		this.editedId = null;
		
		return null;
	}
	
	public String cancelAddSource() {
		
		switch (this.newSourceType) {
		case NEW_SOURCE_ARTICLE:
			this.newArticle = null;
			break;
		case NEW_SOURCE_BOOK:
			this.newBook = null;
			break;
		case NEW_SOURCE_PRIMARY:
			this.newPrimary = null;
			break;
		case NEW_SOURCE_OTHER:
			this.newOther = null;
			break;
		}
		
		this.newSourceType = -1;
		this.editMode = false;
		
		return null;
	}
	
	public String deleteSource() {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try {
			SubmissionSource source = SubmissionSource.loadById(session, this.editedId);
			session.delete(source);
			
			switch (this.newSourceType) {
			case NEW_SOURCE_ARTICLE:
				this.newArticle = null;
				break;
			case NEW_SOURCE_BOOK:
				this.newBook = null;
				break;
			case NEW_SOURCE_PRIMARY:
				this.newPrimary = null;
				break;
			case NEW_SOURCE_OTHER:
				this.newOther = null;
				break;
			}
			
			this.newSourceType = -1;
			this.editMode = false;
		} finally {
			t.commit();
			session.close();
		}
		return null;
	}
	
	private void addNewPrimary(boolean update) {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		SubmissionSourcePrimary primary;
		try {
			if (this.editMode) {
				primary = (SubmissionSourcePrimary) SubmissionSource.loadById(session, this.editedId);
			} else {
				primary = new SubmissionSourcePrimary();
			}
			primary.setNote(this.newPrimary.getNote());
			primary.setDetails(this.newPrimary.getDetails());
			primary.setLocation(this.newPrimary.getLocation());
			primary.setName(this.newPrimary.getName());
			primary.setSeries(this.newPrimary.getSeries());
			primary.setVolume(this.newPrimary.getVolume());
			primary.setSubmission(this.submission);

			if (update) {
				session.update(primary);
			} else {
				session.save(primary);
			}
			this.newPrimary = null;
			this.errorMessage = null;
		} finally {
			t.commit();
			session.close();
		}
	}

	private void addNewOther(boolean update) {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		SubmissionSourceOther other;
		try {
			if (this.editMode) {
				other = (SubmissionSourceOther) SubmissionSource.loadById(session, this.editedId);
			} else {
				other = new SubmissionSourceOther();
			}
			other.setTitle(this.newOther.getTitle());
			other.setLocation(this.newOther.getLocation());
			other.setNote(this.newOther.getNote());
			other.setFolioOrPage(this.newOther.getPageOrFolio());
			other.setSubmission(this.submission);

			if (update) {
				session.update(other);
			} else {
				session.save(other);
			}
			this.newOther = null;
			this.errorMessage = null;
		} finally {
			t.commit();
			session.close();
		}
	}

	private void addNewBook(boolean update) {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		SubmissionSourceBook book;
		try {
			if (this.editMode) {
				book = (SubmissionSourceBook) SubmissionSource.loadById(session, this.editedId);
			} else {
				book = new SubmissionSourceBook();
			}
			book.setAuthors(this.newBook.getAuthors());
			book.setTitle(this.newBook.getTitle());
			book.setPublisher(this.newBook.getPublisher());
			book.setNote(this.newBook.getOtherInfo());
			book.setPlaceOfPublication(this.newBook.getPlaceOfPublication());
			try {
				book.setPageFrom(Integer.parseInt(this.newBook.getPageFrom()));
			} catch (NumberFormatException nex) {
				this.errorMessage = "Numeric value expected for page number";
				return;
			}
			try {
				book.setPageTo(Integer.parseInt(this.newBook.getPageTo()));
			} catch (NumberFormatException nex) {
				this.errorMessage = "Numeric value expected for page number";
				return;
			}
			try {
				book.setYear(Integer.parseInt(this.newBook.getYear()));
			} catch (NumberFormatException nex) {
				this.errorMessage = "Numeric value expected for year of publication";
				return;
			}
			book.setSubmission(this.submission);

			if (update) {
				session.update(book);
			} else {
				session.save(book);
			}
			this.newBook = null;
			this.errorMessage = null;
		} finally {
			t.commit();
			session.close();
		}
	}

	private void addNewArticle(boolean update) {
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		SubmissionSourcePaper paper;
		try {
			if (this.editMode) {
				paper = (SubmissionSourcePaper) SubmissionSource.loadById(session, this.editedId);
			} else {
				paper = new SubmissionSourcePaper();
			}
			paper.setAuthors(this.newArticle.getAuthors());
			paper.setTitle(this.newArticle.getTitle());
			paper.setJournal(this.newArticle.getJournal());
			paper.setNote(this.newArticle.getOtherInfo());
			try {
				paper.setVolume(Integer.parseInt(this.newArticle.getVolume()));
			} catch (NumberFormatException nex) {
				this.errorMessage = "Numeric value expected for volume";
				return;
			}
			try {
				paper.setPageFrom(Integer.parseInt(this.newArticle.getPageFrom()));
			} catch (NumberFormatException nex) {
				this.errorMessage = "Numeric value expected for page number";
				return;
			}
			try {
				paper.setPageTo(Integer.parseInt(this.newArticle.getPageTo()));
			} catch (NumberFormatException nex) {
				this.errorMessage = "Numeric value expected for page number";
				return;
			}
			try {
				paper.setYear(Integer.parseInt(this.newArticle.getYear()));
			} catch (NumberFormatException nex) {
				this.errorMessage = "Numeric value expected for article year";
				return;
			}
			paper.setSubmission(this.submission);

			if (update) {
				session.update(paper);
			} else {
				session.save(paper);
			}
			this.newArticle = null;
			this.errorMessage = null;
		} finally {
			t.commit();
			session.close();
		}
	}

	public GridColumn[] getColumns() {
		return new GridColumn[] {
				new GridColumn("Source type"),
				new GridColumn("Source details")
		};
	}
	
	public GridRow[] getRows() {
		
		int i = 0;
		
		TastDbConditions c = new TastDbConditions();
		c.addCondition(SubmissionSource.getAttribute("submission"), this.submission, TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("SubmissionSource", c);
		List list = qValue.executeQueryList();
		Iterator iter = list.iterator();
		
		GridRow[] rows = new GridRow[list.size()];
		
		while (iter.hasNext()) {
			SubmissionSource source = (SubmissionSource)iter.next();
			if (source instanceof SubmissionSourceBook) {
				SubmissionSourceBook book = (SubmissionSourceBook)source;
				String desc = "Title: " + this.shortenIfNecessary(book.getTitle()) + 
				"; Authors: " + this.shortenIfNecessary(book.getAuthors());
				rows[i++] = new GridRow(book.getId().toString(), new String[] {
					"Book",
					desc
				});
			} else if (source instanceof SubmissionSourcePaper) {
				SubmissionSourcePaper paper = (SubmissionSourcePaper)source;
				String desc = "Title: " + this.shortenIfNecessary(paper.getTitle()) + 
					"; Authors: " + this.shortenIfNecessary(paper.getAuthors());
				rows[i++] = new GridRow(paper.getId().toString(), new String[] {
					"Article",
					desc
				});
			} else if (source instanceof SubmissionSourceOther) {
				SubmissionSourceOther other = (SubmissionSourceOther)source;
				String desc = "Title: " + this.shortenIfNecessary(other.getTitle()) + 
				"; Location: " + this.shortenIfNecessary(other.getLocation());
				rows[i++] = new GridRow(other.getId().toString(), new String[] {
					"Other",
					desc
				});
			} else if (source instanceof SubmissionSourcePrimary) {
				SubmissionSourcePrimary primary = (SubmissionSourcePrimary)source;
				String desc = "Name: " + this.shortenIfNecessary(primary.getName()) + 
				"; Location: " + this.shortenIfNecessary(primary.getLocation());
				rows[i++] = new GridRow(primary.getId().toString(), new String[] {
					"Primary source",
					desc
				});
			}
		}
		
		return rows;
	}

	private String shortenIfNecessary(String string) {
		if (string == null) {
			return "none";
		}
		if (string.length() > 40) {
			return string.substring(0, 40) + "...";
		} else {
			return string;
		}
	}

	public SourceArticle getNewArticle() {
		if (newArticle == null) {
			newArticle = new SourceArticle();
		}
		return newArticle;
	}

	public SourceBook getNewBook() {
		if (newBook == null) {
			newBook = new SourceBook();
		}
		return newBook;
	}
	
	public SourceOther getNewOther() {
		if (newOther == null) {
			newOther = new SourceOther();
		}
		return newOther;
	}
	
	public SourcePrimary getNewPrimary() {
		if (newPrimary == null) {
			newPrimary = new SourcePrimary();
		}
		return newPrimary;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void submit() {
		this.submission.setSubmitted(true);
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		try {
			session.update(this.submission);
		} finally {
			t.commit();
			session.close();
		}
	}
	
	public String getModeLabel() {
		if (!this.editMode) {
			return "New";
		} else {
			return "Edit";
		}
	}
	
	public String getApplyLabel() { 
		if (!this.editMode) {
			return "Enter";
		} else {
			return "Apply changes";
		} 
	}
	
	public void openRow(GridOpenRowEvent event) {
		this.editedId = new Long(event.getRowId());
		Session session = HibernateConn.getSession();
		Transaction t = session.beginTransaction();
		
		SubmissionSource source = SubmissionSource.loadById(session, this.editedId);
		if (source instanceof SubmissionSourceBook) {
			SubmissionSourceBook book = (SubmissionSourceBook)source;
			this.newBook = new SourceBook();
			this.newBook.setAuthors(book.getAuthors());
			this.newBook.setOtherInfo(book.getNote());
			this.newBook.setPageFrom(String.valueOf(book.getPageFrom()));
			this.newBook.setPageTo(String.valueOf(book.getPageTo()));
			this.newBook.setPlaceOfPublication(book.getPlaceOfPublication());
			this.newBook.setPublisher(book.getPublisher());
			this.newBook.setTitle(book.getTitle());
			this.newBook.setYear(String.valueOf(book.getYear()));
			this.newSourceType = NEW_SOURCE_BOOK;
		} else if (source instanceof SubmissionSourcePaper) {
			SubmissionSourcePaper paper = (SubmissionSourcePaper)source;
			this.newArticle = new SourceArticle();
			this.newArticle.setAuthors(paper.getAuthors());
			this.newArticle.setOtherInfo(paper.getNote());
			this.newArticle.setPageFrom(String.valueOf(paper.getPageFrom()));
			this.newArticle.setPageTo(String.valueOf(paper.getPageTo()));
			this.newArticle.setTitle(paper.getTitle());
			this.newArticle.setYear(String.valueOf(paper.getYear()));
			this.newArticle.setJournal(paper.getJournal());
			this.newArticle.setVolume(String.valueOf(paper.getVolume()));
			this.newSourceType = NEW_SOURCE_ARTICLE;
		} else if (source instanceof SubmissionSourceOther) {
			SubmissionSourceOther other = (SubmissionSourceOther)source;
			this.newOther = new SourceOther();
			this.newOther.setLocation(other.getLocation());
			this.newOther.setNote(other.getNote());
			this.newOther.setPageOrFolio(other.getFolioOrPage());
			this.newOther.setTitle(other.getTitle());
			this.newSourceType = NEW_SOURCE_OTHER;
		} else if (source instanceof SubmissionSourcePrimary) {
			SubmissionSourcePrimary primary = (SubmissionSourcePrimary)source;
			this.newPrimary = new SourcePrimary();
			this.newPrimary.setNote(primary.getNote());
			this.newPrimary.setDetails(primary.getDetails());
			this.newPrimary.setLocation(primary.getLocation());
			this.newPrimary.setName(primary.getName());
			this.newPrimary.setSeries(primary.getSeries());
			this.newPrimary.setVolume(primary.getVolume());
			this.newSourceType = NEW_SOURCE_PRIMARY;
		} 
		this.editMode = true;
		
		t.commit();
		session.close();
	}
	
	public String getApplyButtonLabel() {
		if (this.editMode) {
			return "Apply changes";
		} else {
			return "Cancel";
		}
	}
	
	public Boolean getMode() {
		return new Boolean(this.editMode);
	}
}
