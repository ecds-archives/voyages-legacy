package edu.emory.library.tast.submission;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.common.GridColumn;
import edu.emory.library.tast.common.GridRow;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionSourceBook;
import edu.emory.library.tast.dm.SubmissionSourceOther;
import edu.emory.library.tast.dm.SubmissionSourcePaper;
import edu.emory.library.tast.submission.sources.SourceArticle;
import edu.emory.library.tast.submission.sources.SourceBook;
import edu.emory.library.tast.submission.sources.SourceOther;
import edu.emory.library.tast.util.HibernateUtil;

public class SourcesBean {

	private static final int NEW_SOURCE_BOOK = 1;
	private static final int NEW_SOURCE_ARTICLE = 2;
	private static final int NEW_SOURCE_OTHER = 3;
	private static final int NEW_SOURCE_PRIMARY = 4;
	
	private int newSourceType = -1;
	
	private Submission submission;
	private Set bookSources = new HashSet();
	private Set articleSources = new HashSet();
	private Set primarySources = new HashSet();
	private Set otherSources = new HashSet();
	
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
	
	public Submission getSubmission() {
		return submission;
	}

	public void setSubmission(Submission submission) {
		this.submission = submission;
	}

	public Set getArticleSources() {
		return articleSources;
	}

	public void setArticleSources(Set articleSources) {
		this.articleSources = articleSources;
	}

	public Set getBookSources() {
		return bookSources;
	}

	public Set getOtherSources() {
		return otherSources;
	}

	public Set getPrimarySources() {
		return primarySources;
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
			this.addNewArticle();
			break;
		case NEW_SOURCE_BOOK:
			this.addNewBook();
			break;
		case NEW_SOURCE_PRIMARY:
			break;
		case NEW_SOURCE_OTHER:
			break;
		}
		if (this.errorMessage != null) {
			return null;
		}
		this.newSourceType = -1;
		return null;
	}
	
	private void addNewBook() {
		SubmissionSourceBook book = new SubmissionSourceBook();
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
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(book);
			this.bookSources.add(book);
			this.newBook = null;
			this.errorMessage = null;
		} finally {
			t.commit();
			session.close();
		}
	}
	
	private void addNewArticle() {
		SubmissionSourcePaper paper = new SubmissionSourcePaper();
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
		
		
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(paper);
			this.articleSources.add(paper);
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
		GridRow[] rows = new GridRow[this.articleSources.size() + 
		                             this.bookSources.size() + 
		                             this.otherSources.size() +
		                             this.primarySources.size()];
		int i = 0;
		for (Iterator iter = this.articleSources.iterator(); iter.hasNext();) {
			SubmissionSourcePaper element = (SubmissionSourcePaper) iter.next();
			String desc = "Title: " + this.shortenIfNecessary(element.getTitle()) + 
							"; Authors: " + this.shortenIfNecessary(element.getAuthors());
			rows[i++] = new GridRow(element.getId().toString(), new String[] {
				"Article",
				desc
			});
		}
		
		for (Iterator iter = this.bookSources.iterator(); iter.hasNext();) {
			SubmissionSourceBook element = (SubmissionSourceBook) iter.next();
			String desc = "Title: " + this.shortenIfNecessary(element.getTitle()) + 
			"; Authors: " + this.shortenIfNecessary(element.getAuthors());
			rows[i++] = new GridRow(element.getId().toString(), new String[] {
				"Article",
				desc
			});
		}
		
		for (Iterator iter = this.otherSources.iterator(); iter.hasNext();) {
			SubmissionSourceOther element = (SubmissionSourceOther) iter.next();
			String desc = "Title: " + this.shortenIfNecessary(element.getTitle()) + 
			"; Location: " + this.shortenIfNecessary(element.getLocation());
			rows[i++] = new GridRow(element.getId().toString(), new String[] {
				"Other source",
				desc
			});
		}
		
		return rows;
	}

	private String shortenIfNecessary(String string) {
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
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void submit() {
		System.out.println("Submit!");
	}
}
