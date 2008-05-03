package edu.emory.library.tast.images.site;

import org.w3c.dom.Node;

import edu.emory.library.tast.util.EqualsUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.XMLUtils;

public class ImagesQuery
{
	
	private String keyword = "";
	private Integer yearFrom = null;
	private Integer yearTo = null;
	private String[] categories;
	
	// this will have to be extended
	private Long searchPortId = null;
	private Long searchRegionId = null;
	
	public Long getSearchPortId() {
		return searchPortId;
	}
	public void setSearchPortId(Long searchPortId) {
		this.searchPortId = searchPortId;
	}
	public void setCategory(long catId) {
		this.categories = new String[] {String.valueOf(catId)};
	}
	public Integer getYearFrom() {
		return yearFrom;
	}
	public void setYearFrom(Integer searchQueryFrom) {
		this.yearFrom = searchQueryFrom;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String searchQueryTitle) {
		this.keyword = searchQueryTitle;
	}
	public Integer getYearTo() {
		return yearTo;
	}
	public void setYearTo(Integer searchQueryTo) {
		this.yearTo = searchQueryTo;
	}
	public Long getSearchRegionId() {
		return searchRegionId;
	}
	public void setSearchRegionId(Long searchRegionId) {
		this.searchRegionId = searchRegionId;
	}
	
	public String[] getCategories()
	{
		return categories;
	}
	public void setCategories(String[] categories)
	{
		this.categories = categories;
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof ImagesQuery)) return false;
		ImagesQuery that = (ImagesQuery)obj;
		return EqualsUtil.areEqual(this.keyword, that.keyword) &&
				EqualsUtil.areEqual(this.yearFrom, that.yearFrom) &&
				EqualsUtil.areEqual(this.yearTo, that.yearTo) &&
				EqualsUtil.areEqual(this.searchPortId, that.searchPortId) &&
				EqualsUtil.areEqual(this.searchRegionId, that.searchRegionId) &&
				EqualsUtil.areEqual(this.categories, that.categories);
	}
	
	protected Object clone() {
		ImagesQuery query = new ImagesQuery();
		query.keyword = keyword;
		query.yearFrom = yearFrom;
		query.yearTo = yearTo;
		query.searchPortId = searchPortId;
		query.searchRegionId = searchRegionId;
		if (categories != null) query.categories = (String[])categories.clone();
		return query;
	}
	
	public void restoreFromXML(Node entry) {
		Node config = XMLUtils.getChildNode(entry, "config");
		if (config != null) {
			this.keyword = XMLUtils.getXMLProperty(config, "searchQueryTitle");
			if (!StringUtils.isNullOrEmpty(XMLUtils.getXMLProperty(config, "searchQueryFrom"))) {
				this.yearFrom = new Integer(XMLUtils.getXMLProperty(config, "searchQueryFrom"));
			}
			if (!StringUtils.isNullOrEmpty(XMLUtils.getXMLProperty(config, "searchQueryTo"))) {
				this.yearTo = new Integer(XMLUtils.getXMLProperty(config, "searchQueryTo"));
			}
			if (!StringUtils.isNullOrEmpty(XMLUtils.getXMLProperty(config, "searchPortId"))) {
				this.searchPortId = new Long(XMLUtils.getXMLProperty(config, "searchPortId"));
			}
			if (!StringUtils.isNullOrEmpty(XMLUtils.getXMLProperty(config, "searchRegionId"))) {
				this.searchRegionId = new Long(XMLUtils.getXMLProperty(config, "searchRegionId"));
			}
		}
	}
	
	public String toXML() {
		StringBuffer buffer = new StringBuffer();		
		buffer.append("<config ");
		XMLUtils.appendAttribute(buffer, "searchQueryTitle", keyword);
		XMLUtils.appendAttribute(buffer, "searchQueryFrom", yearFrom);
		XMLUtils.appendAttribute(buffer, "searchQueryTo", yearTo);
		XMLUtils.appendAttribute(buffer, "searchPortId", searchPortId);
		XMLUtils.appendAttribute(buffer, "searchRegionId", searchRegionId);
		buffer.append("/>\n");
		return buffer.toString();
	}
}
