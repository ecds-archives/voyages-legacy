package edu.emory.library.tast.common.voyage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Node;

import edu.emory.library.tast.common.MessageBarComponent;
import edu.emory.library.tast.common.table.TableData;
import edu.emory.library.tast.database.table.DetailVoyageMap;
import edu.emory.library.tast.database.table.formatters.SimpleDateAttributeFormatter;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.SourceInformation;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.XMLExportable;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.images.GalleryImage;
import edu.emory.library.tast.images.site.ImagesBean;
import edu.emory.library.tast.maps.LegendItemsGroup;
import edu.emory.library.tast.maps.component.PointOfInterest;
import edu.emory.library.tast.maps.component.StandardMaps;
import edu.emory.library.tast.maps.component.ZoomLevel;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.SourceInformationUtils;
import edu.emory.library.tast.util.XMLUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

/**
 * Bean which features functionality for details of given voyage.
 * It's main role is to query database for given voyage (provided by method openVoyage).
 *
 */
public class VoyageDetailBean
{

	/**
	 * Data visible in table.
	 */
	private TableData detailData = new TableData();
	
	/**
	 * Map data for voyage.
	 */
	private DetailVoyageMap detailVoyageMap = new DetailVoyageMap();
	
	/**
	 * Voyage ids (global and internal)
	 */
	private long voyageIid = -1;
	private int voyageId = -1;
	
	/**
	 * Remembers last view - the one which should be restored when 'go back' is hit
	 */
	private String previousViewId;

	/**
	 * Images associated with voyage
	 * 
	 */
	private GalleryImage[] imagesGallery = new GalleryImage[0];
	private String selectedImageId;
	
	/**
	 * Selected tab (variables/map/images)
	 */
	private String selectedTab = "variables";
	
	/**
	 * Message bar to show link information
	 */
	private MessageBarComponent messageBar = null;
	
	/**
	 * Provider of source rollovers.
	 */
	private SourceInformationUtils sourceInfoUtils = SourceInformationUtils.createSourceInformationUtils();
	
	/**
	 * Opens given voyage - main function in this bean.
	 * Called when one clicks on given voyage in table.
	 * @param voyageIid
	 */
	public void openVoyage(long voyageIid)
	{
		
		this.voyageIid = voyageIid;
		this.detailVoyageMap.setVoyageIid(voyageIid);
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Conditions c = new Conditions();
		c.addCondition(Voyage.getAttribute("iid"), new Long(this.voyageIid), Conditions.OP_EQUALS);
		
		List validAttrs = new ArrayList();
		VisibleAttributeInterface[] attrs = VisibleAttribute.getAllAttributes();
		for (int i = 0; i < attrs.length; i++)
		{
			VisibleAttributeInterface column = attrs[i];
			validAttrs.add(column);
			if (column.getType().equals(VisibleAttribute.DATE_ATTRIBUTE)) {
				this.detailData.setFormatter(column, new SimpleDateAttributeFormatter(new SimpleDateFormat("yyyy-MM-dd")));
			}
		}
		this.detailData.setVisibleColumns(validAttrs);
		
		// Build query
		QueryValue qValue = new QueryValue("Voyage", c);
		
		// Dictionaries - list of columns with dictionaries.
		this.detailData.setKeyAttribute(Voyage.getAttribute("iid"));
		Attribute[] populatedAttributes = this.detailData.getAttributesForQuery();
		if (populatedAttributes != null)
		{
			for (int i = 0; i < populatedAttributes.length; i++)
			{
				if (populatedAttributes[i] != null)
				{
					qValue.addPopulatedAttribute(populatedAttributes[i]);
				}
			}
		}
		
		// last is voyage id
		qValue.addPopulatedAttribute(VisibleAttribute.getAttribute("voyageid").getAttributes()[0]);
		
		VisibleAttributeInterface vattr = this.detailData.getOrderByColumn();
		if (this.detailData.getOrderByColumn() == null)
		{
			qValue.setOrderBy(new Attribute[] { Voyage.getAttribute("voyageid") });
		}
		else
		{
			Attribute[] attr = vattr.getAttributes();
			if (attr != null) {
				Attribute[] order = new Attribute[attr.length];
				for (int i = 0; i < attr.length; i++) {
					if (!(attr[i] instanceof DictionaryAttribute)) {
						order[i] = attr[i];
					} else {
						order[i] = new SequenceAttribute(new Attribute[] {
								attr[i], ((DictionaryAttribute) attr[i]).getAttribute("name") });
					}
				}
				qValue.setOrderBy(order);
				qValue.setOrder(this.detailData.getOrder());
			}
		}

		// Execute query
		Object[] ret = qValue.executeQuery(sess);
		this.detailData.setData(ret);
		
		//get additional info for sources
		for (int i = 0; i < populatedAttributes.length; i++) {
			if (populatedAttributes[i].getName().startsWith("source")) {
				for (int j = 0; j < ret.length; j++) {						
					if (((Object[])ret[j])[i] != null) {
						SourceInformation info = sourceInfoUtils.match((String)((Object[])ret[j])[i]);
						if (info != null) {
							detailData.setRollover(((Object[])ret[j])[i], info.getInformation());
						}
					}
				}
			}
		}
		
		
		// voyage id (is the last)
		Object[] voyageValues = (Object[]) ret[0];
		voyageId = ((Integer)(voyageValues[voyageValues.length - 1])).intValue();
		
		// load related images
		List images = Image.getImagesByVoyageId(sess, voyageId);
		imagesGallery = new GalleryImage[images.size()];
		int imageIndex = 0;
		for (Iterator iter = images.iterator(); iter.hasNext();)
		{
			Image image = (Image) iter.next();
			imagesGallery[imageIndex++] = new GalleryImage(
					String.valueOf(image.getId()),
					image.getFileName(),
					image.getTitle());
		}

		// done with db
		transaction.commit();
		sess.close();

	}

	/**
	 * Go back button
	 *
	 */
	public void back()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().getNavigationHandler().handleNavigation(context, null, this.previousViewId);
	}

	public String refresh()
	{
		this.detailVoyageMap.refresh();
		return null;
	}
	
	/**
	 * Opens image detail for voyage's image
	 * @return
	 */
	public String openImageDetail()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ImagesBean imagesBean = (ImagesBean) context.getApplication().createValueBinding("#{ImagesBean}").getValue(context);
		imagesBean.openImageFromVoyageDetail(this.voyageId, this.selectedImageId);
		return "images-detail";
	}
	
	/**
	 * Returns data visible in variables table
	 * @return
	 */
	public TableData getDetailData()
	{
		return detailData;
	}

	/**
	 * Gets points visible on map.
	 * @return
	 */
	public PointOfInterest[] getPointsOfInterest()
	{
		return this.detailVoyageMap.getPointsOfInterest();
	}

	/**
	 * Gets legend for map
	 * @return
	 */
	public LegendItemsGroup[] getLegend()
	{
		return this.detailVoyageMap.getLegend();
	}
	
	/**
	 * Sets previous view.
	 * @param viewId
	 */
	public void setPreviousView(String viewId)
	{
		this.previousViewId = viewId;
	}

	public void setBackPageToCurrentView()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		this.previousViewId = context.getViewRoot().getViewId();
	}

	/**
	 * Gets zoom level of map.
	 * @return
	 */
	public ZoomLevel[] getZoomLevels()
	{
		return StandardMaps.getZoomLevels(this);
	}
	
	/**
	 * Gets minimap
	 * @return
	 */
	public ZoomLevel getMiniMapZoomLevel()
	{
		return StandardMaps.getMiniMapZoomLevel(this);
	}

	/**
	 * gets id of selected image
	 * @return
	 */
	public String getSelectedImageId()
	{
		return selectedImageId;
	}

	/**
	 * sets id of selected image
	 * @param selectedImageId
	 */
	public void setSelectedImageId(String selectedImageId)
	{
		this.selectedImageId = selectedImageId;
	}

	public GalleryImage[] getImagesGallery()
	{
		return imagesGallery;
	}

	public int getVoyageId()
	{
		return voyageId;
	}
	
	public String saveLink() {
		
		Configuration conf = new Configuration();
		DetailVoyageQuery query = new DetailVoyageQuery();
		query.tab = selectedTab;
		query.voyageIid = new Long(voyageIid);
		conf.addEntry("permlinkDetailVoyage", query);
		conf.save();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		messageBar.setMessage(request.getRequestURL() + "?permlink=" + conf.getId());
		messageBar.setRendered(true);
		
		return null;
	}
	
	public void restoreLink(Long id) {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			Configuration conf = Configuration.loadConfiguration(id);
			if (conf == null) {
				return;
			}

			if (conf.getEntry("permlinkDetailVoyage") != null) {
				DetailVoyageQuery selection = (DetailVoyageQuery) conf.getEntry("permlinkDetailVoyage");
				this.selectedTab = selection.tab;
				openVoyage(selection.voyageIid.longValue());
			}
			
		} finally {
			t.commit();
			session.close();
		}
	}
	
	public static class DetailVoyageQuery implements XMLExportable {

		public Long voyageIid;
		public String tab;
		
		public void restoreFromXML(Node entry) {
			Node config = XMLUtils.getChildNode(entry, "config");
			if (config != null) {
				this.voyageIid = new Long(XMLUtils.getXMLProperty(config, "voyageId"));
				this.tab = XMLUtils.getXMLProperty(config, "tab");
			}
		}

		public String toXML() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<config ");
			buffer.append("voyageId=\"").append(voyageIid).append("\" ");
			buffer.append("tab=\"").append(tab).append("\" ");
			buffer.append("/>");
			return buffer.toString();
		}
	}

	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public MessageBarComponent getMessageBar() {
		return messageBar;
	}

	public void setMessageBar(MessageBarComponent message) {
		this.messageBar = message;
	}

	public long getVoyageIid() {
		return voyageIid;
	}

}