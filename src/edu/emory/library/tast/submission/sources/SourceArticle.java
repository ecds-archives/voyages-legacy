package edu.emory.library.tast.submission.sources;

public class SourceArticle {
	
	private String authors;
	private String title;
	private String journal;
	private String volume;
	private String year;
	private String pageFrom;
	private String pageTo;
	private String otherInfo;
	
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getJournal() {
		return journal;
	}
	public void setJournal(String journal) {
		this.journal = journal;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
	public String getPageFrom() {
		return pageFrom;
	}
	public void setPageFrom(String pageFrom) {
		this.pageFrom = pageFrom;
	}
	public String getPageTo() {
		return pageTo;
	}
	public void setPageTo(String pageTo) {
		this.pageTo = pageTo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
}
