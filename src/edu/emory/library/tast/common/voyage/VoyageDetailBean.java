package edu.emory.library.tast.common.voyage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.w3c.dom.Node;

import edu.emory.library.tast.common.table.TableData;
import edu.emory.library.tast.database.table.formatters.SimpleDateAttributeFormatter;
import edu.emory.library.tast.database.tabscommon.VisibleAttribute;
import edu.emory.library.tast.database.tabscommon.VisibleAttributeInterface;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.SourceInformation;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.XMLExportable;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.images.GalleryImage;
import edu.emory.library.tast.images.site.ImagesBean;
import edu.emory.library.tast.maps.component.Line;
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
	 * Provider of source rollovers.
	 */
	private SourceInformationUtils sourceInfoUtils = SourceInformationUtils.createSourceInformationUtils();
	
	private VoyageRoute route;

	/**
	 * Opens given voyage - main function in this bean.
	 * Called when one clicks on given voyage in table.
	 * @param voyageIid
	 */
	public void openVoyage(long voyageIid)
	{
		
		this.voyageIid = voyageIid;
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		loadVoyageData(sess);
		loadVoyageMapData(sess);
		loadRelatedImages(sess);

		transaction.commit();
		sess.close();

	}

	private void loadVoyageData(Session sess)
	{
		
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
	}

	private void loadRelatedImages(Session sess)
	{
		
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
		
	}
	
	private Port loadVoyagePlace(Session sess, String place)
	{
		
		String hqlImages =
			"select v." + place + " " +
			"from Voyage as v " +
			"where v.iid = " + voyageIid + " and " +
			"v." + place + ".showOnVoyageMap = '1'";
		
		List lst = sess.createQuery(hqlImages).list();
		if (lst.size() == 0)
			return null;
		else
			return (Port) lst.get(0);

	}

	/*
	private void addPointOfInterest(
			ArrayList pointsOfInterest,
			Port port,
			String symbol)
	{
		pointsOfInterest.add(
				new PointOfInterest(
						port.getLongitude(),
						port.getLatitude(),
						new String[]{symbol},
						port.getName(),
						port.getName()));
	}
	*/
	
	private void loadVoyageMapData(Session sess)
	{
		
		// load ports from database
		Port departurePort = loadVoyagePlace(sess, "ptdepimp");
		Port prchPort1 = loadVoyagePlace(sess, "plac1tra");
		Port prchPort2 = loadVoyagePlace(sess, "plac2tra");
		Port prchPort3 = loadVoyagePlace(sess, "plac3tra");
		Port prchPortPrincipal = loadVoyagePlace(sess, "mjbyptimp");
		Port sellPort1 = loadVoyagePlace(sess, "sla1port");
		Port sellPort2 = loadVoyagePlace(sess, "adpsale1");
		Port sellPort3 = loadVoyagePlace(sess, "adpsale2");
		Port sellPortPrincipal = loadVoyagePlace(sess, "mjslptimp");
		Port returnPort = loadVoyagePlace(sess, "portret");
		
		// create a new route
		route = new VoyageRoute();
		
		// place where voyage began
		if (departurePort != null)
		{
			VoyageRouteLeg startLeg = new VoyageRouteLeg(); 
			route.addLeg(startLeg);
			startLeg.addPlace(new VoyageRoutePlace(
					departurePort.getId().longValue(),
					departurePort.getName(),
					"Place where voyage began",
					departurePort.getLongitude(),
					departurePort.getLatitude(),
					new VoyageRouteSymbolBegin()));
			
		}
		
		// ports of purchases
		if (prchPort1 != null || prchPort2 != null || prchPort3 != null || prchPortPrincipal != null)
		{
			VoyageRouteLeg purchasesLeg = new VoyageRouteLeg(); 
			route.addLeg(purchasesLeg);
			
			if (prchPort1 != null)
				purchasesLeg.addPlace(new VoyageRoutePlace(
						prchPort1.getId().longValue(),
						prchPort1.getName(),
						"First place of slave purchase",
						prchPort1.getLongitude(),
						prchPort1.getLatitude(),
						new VoyageRouteSymbolPurchase(0,
								prchPort1.equals(prchPortPrincipal))));
			
			if (prchPort2 != null)
				purchasesLeg.addPlace(new VoyageRoutePlace(
						prchPort2.getId().longValue(),
						prchPort2.getName(),
						"Second place of slave purchase",
						prchPort2.getLongitude(),
						prchPort2.getLatitude(),
						new VoyageRouteSymbolPurchase(1,
								prchPort2.equals(prchPortPrincipal))));
			
			if (prchPort3 != null)
				purchasesLeg.addPlace(new VoyageRoutePlace(
						prchPort3.getId().longValue(),
						prchPort3.getName(),
						"Third place of slave purchase",
						prchPort3.getLongitude(),
						prchPort3.getLatitude(),
						new VoyageRouteSymbolPurchase(2,
								prchPort3.equals(prchPortPrincipal))));
			
			if (prchPort1 == null && prchPort2 == null && prchPort3 == null)
				purchasesLeg.addPlace(new VoyageRoutePlace(
						prchPortPrincipal.getId().longValue(),
						prchPortPrincipal.getName(),
						"Principal place of slave purchase",
						prchPortPrincipal.getLongitude(),
						prchPortPrincipal.getLatitude(),
						new VoyageRouteSymbolPurchasePrincipal()));

		}
		
		// port of sells
		if (sellPort1 != null || sellPort2 != null || sellPort3 != null || sellPortPrincipal != null)
		{
			VoyageRouteLeg sellsLeg = new VoyageRouteLeg(); 
			route.addLeg(sellsLeg);
			
			if (sellPort1 != null)
				sellsLeg.addPlace(new VoyageRoutePlace(
						sellPort1.getId().longValue(),
						sellPort1.getName(),
						"First place of slave purchase",
						sellPort1.getLongitude(),
						sellPort1.getLatitude(),
						new VoyageRouteSymbolSell(0,
								sellPort1.equals(sellPortPrincipal))));
			
			if (sellPort2 != null)
				sellsLeg.addPlace(new VoyageRoutePlace(
						sellPort2.getId().longValue(),
						sellPort2.getName(),
						"Second place of slave purchase",
						sellPort2.getLongitude(),
						sellPort2.getLatitude(),
						new VoyageRouteSymbolSell(1,
								sellPort2.equals(sellPortPrincipal))));
			
			if (sellPort3 != null)
				sellsLeg.addPlace(new VoyageRoutePlace(
						sellPort3.getId().longValue(),
						sellPort3.getName(),
						"Third place of slave purchase",
						sellPort3.getLongitude(),
						sellPort3.getLatitude(),
						new VoyageRouteSymbolSell(2,
								sellPort3.equals(sellPortPrincipal))));
			
			if (sellPort1 == null && sellPort2 == null && sellPort3 == null)
				sellsLeg.addPlace(new VoyageRoutePlace(
						sellPortPrincipal.getId().longValue(),
						sellPortPrincipal.getName(),
						"Principal place of slave purchase",
						sellPortPrincipal.getLongitude(),
						sellPortPrincipal.getLatitude(),
						new VoyageRouteSymbolSellPrincipal()));
			
		}
		
		// place where voyage ended
		if (returnPort != null)
		{
			VoyageRouteLeg endLeg = new VoyageRouteLeg(); 
			route.addLeg(endLeg);
			endLeg.addPlace(new VoyageRoutePlace(
					returnPort.getId().longValue(),
					returnPort.getName(),
					"Place where voyage ended",
					returnPort.getLongitude(),
					returnPort.getLatitude(),
					new VoyageRouteSymbolEnd()));
			
		}

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

	public String refreshMap()
	{
		// TODO refresh map 
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
	public PointOfInterest[] getVoyageMapPoints()
	{
		return route.createMapPointsOfInterest();
	}

	public Line[] getVoyageMapLines()
	{
		return route.createMapLines();
	}

	public VoyageRoute getVoyageRoute()
	{
		return route;
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
	
	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public long getVoyageIid() {
		return voyageIid;
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

}