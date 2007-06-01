package edu.emory.library.tast.submission.sources;

public class SourceBook {
	
	private String authors;
	private String title;
	private String year;
	private String placeOfPublication;
	private String publisher;
	private String pageFrom;
	private String pageTo;
	private String otherInfo;
	
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
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
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPlaceOfPublication() {
		return placeOfPublication;
	}
	public void setPlaceOfPublication(String placeOfPublication) {
		this.placeOfPublication = placeOfPublication;
	}
}
