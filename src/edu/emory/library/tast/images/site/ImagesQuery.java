package edu.emory.library.tast.images.site;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.XMLExportable;
import edu.emory.library.tast.util.EqualsUtil;
import edu.emory.library.tast.util.XMLUtils;

public class ImagesQuery implements XMLExportable {
	
	private String searchQueryTitle = "";
	private String searchQueryDescription = "";
	private long searchQueryCategory = ImagesBean.ALL_CATEGORIES_ID;
	private Integer searchQueryFrom = null;
	private Integer searchQueryTo = null;
	private Integer searchVoyageId = null;
	
	// this will have to be extended
	private Long searchPortId = null;
	private Long searchRegionId = null;
	
	public Long getSearchPortId() {
		return searchPortId;
	}
	public void setSearchPortId(Long searchPortId) {
		this.searchPortId = searchPortId;
	}
	public long getSearchQueryCategory() {
		return searchQueryCategory;
	}
	public void setSearchQueryCategory(long searchQueryCategory) {
		this.searchQueryCategory = searchQueryCategory;
	}
	public String getSearchQueryDescription() {
		return searchQueryDescription;
	}
	public void setSearchQueryDescription(String searchQueryDescription) {
		this.searchQueryDescription = searchQueryDescription;
	}
	public Integer getSearchQueryFrom() {
		return searchQueryFrom;
	}
	public void setSearchQueryFrom(Integer searchQueryFrom) {
		this.searchQueryFrom = searchQueryFrom;
	}
	public String getSearchQueryTitle() {
		return searchQueryTitle;
	}
	public void setSearchQueryTitle(String searchQueryTitle) {
		this.searchQueryTitle = searchQueryTitle;
	}
	public Integer getSearchQueryTo() {
		return searchQueryTo;
	}
	public void setSearchQueryTo(Integer searchQueryTo) {
		this.searchQueryTo = searchQueryTo;
	}
	public Long getSearchRegionId() {
		return searchRegionId;
	}
	public void setSearchRegionId(Long searchRegionId) {
		this.searchRegionId = searchRegionId;
	}
	public Integer getSearchVoyageId() {
		return searchVoyageId;
	}
	public void setSearchVoyageId(Integer searchVoyageId) {
		this.searchVoyageId = searchVoyageId;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof ImagesQuery)) {
			return false;
		}
		ImagesQuery that = (ImagesQuery)obj;
		return EqualsUtil.areEqual(this.searchQueryTitle, that.searchQueryTitle) &&
				EqualsUtil.areEqual(this.searchQueryDescription, that.searchQueryDescription) &&
				EqualsUtil.areEqual(this.searchQueryCategory, that.searchQueryCategory) &&
				EqualsUtil.areEqual(this.searchQueryFrom, that.searchQueryFrom) &&
				EqualsUtil.areEqual(this.searchQueryTo, that.searchQueryTo) &&
				EqualsUtil.areEqual(this.searchVoyageId, that.searchVoyageId) &&
				EqualsUtil.areEqual(this.searchPortId, that.searchPortId) &&
				EqualsUtil.areEqual(this.searchRegionId, that.searchRegionId);
	}
	
	protected Object clone() {
		ImagesQuery query = new ImagesQuery();
		query.searchQueryTitle = searchQueryTitle;
		query.searchQueryDescription = searchQueryDescription;
		query.searchQueryCategory = searchQueryCategory;
		query.searchQueryFrom = searchQueryFrom;
		query.searchQueryTo = searchQueryTo;
		query.searchVoyageId = searchVoyageId;
		query.searchPortId = searchPortId;
		query.searchRegionId = searchRegionId;
		return query;
	}
	
	public void restoreFromXML(Node entry) {
	}
	
	public String toXML() {
		StringBuffer buffer = new StringBuffer();		
		buffer.append("<config ");
		XMLUtils.appendAttribute(buffer, "searchQueryTitle", searchQueryTitle);
		XMLUtils.appendAttribute(buffer, "searchQueryDescription", searchQueryDescription);
		XMLUtils.appendAttribute(buffer, "searchQueryCategory", new Long(searchQueryCategory));
		XMLUtils.appendAttribute(buffer, "searchQueryFrom", searchQueryFrom);
		XMLUtils.appendAttribute(buffer, "searchQueryTo", searchQueryTo);
		XMLUtils.appendAttribute(buffer, "searchVoyageId", searchVoyageId);
		XMLUtils.appendAttribute(buffer, "searchPortId", searchPortId);
		XMLUtils.appendAttribute(buffer, "searchRegionId", searchRegionId);
		buffer.append("/>\n");
		return buffer.toString();
	}
}
