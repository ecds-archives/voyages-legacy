package edu.emory.library.tast.common.voyage;

import java.text.DecimalFormat;
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

	private void loadVoyageMapData(Session sess)
	{
		
		SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat slavesFmt = new DecimalFormat(",#,###,###");
		
		// Load the entire voyage. Originally, I tried to
		// load only the necessary fields from it, but it turned
		// out that we need about 20 fields to load, and
		// nobody wants to use constantly things like
		// ((Port)(Object[])result[4]).getName() ...
		Voyage voyage = Voyage.loadById(sess, voyageIid);
		
		// load ports from database
		Port departurePort = voyage.getPtdepimp();
		Port prchPort1 = voyage.getPlac1tra();
		Port prchPort2 = voyage.getPlac2tra();
		Port prchPort3 = voyage.getPlac3tra();
		Port prchPortPrincipal = voyage.getMjbyptimp();
		Port sellPort1 = voyage.getSla1port();
		Port sellPort2 = voyage.getAdpsale1();
		Port sellPort3 = voyage.getAdpsale2();
		Port sellPortPrincipal = voyage.getMjslptimp();
		Port returnPort = voyage.getPortret();
		
		// create a new route
		route = new VoyageRoute();
		
		// place where voyage began
		if (departurePort != null)
		{
			
			VoyageRouteLeg startLeg = new VoyageRouteLeg(); 
			route.addLeg(startLeg);

			VoyageRoutePlace departurePlace = new VoyageRoutePlace(
					departurePort.getId().longValue(),
					departurePort.getName(),
					departurePort.getLongitude(),
					departurePort.getLatitude(),
					new VoyageRouteSymbolBegin());
			
			departurePlace.setPurpose(
					"Place where voyage began");
			
			departurePlace.addInfoLine(
					"Date that voyage began",
					dateFmt.format(voyage.getDatedep()));
			
			startLeg.addPlace(departurePlace);
		
		}
		
		// ports of purchases
		if (	
				prchPort1 != null ||
				prchPort2 != null ||
				prchPort3 != null ||
				prchPortPrincipal != null)
		{
			VoyageRouteLeg purchasesLeg = new VoyageRouteLeg(); 
			route.addLeg(purchasesLeg);
			
			if (prchPort1 != null)
			{
				
				boolean isPrincipal = prchPort1.equals(prchPortPrincipal);

				VoyageRoutePlace prchPlace1 = new VoyageRoutePlace(
						prchPort1.getId().longValue(),
						prchPort1.getName(),
						prchPort1.getLongitude(),
						prchPort1.getLatitude(),
						new VoyageRouteSymbolPurchase(0, isPrincipal));
				
				if (!isPrincipal)
					prchPlace1.setPurpose(
							"First place of slave purchase");
				else
					prchPlace1.setPurpose(
							"First (and major) place of slave purchase");
				
				if (voyage.getDatebuy() != null)
					prchPlace1.addInfoLine(
							"Date that slave purchase began",
							dateFmt.format(voyage.getDatebuy()));
				
				if (voyage.getDateleftafr() != null && prchPort2 == null)
					prchPlace1.addInfoLine(
							"Date that vessel left",
							dateFmt.format(voyage.getDateleftafr()));
				
				if (voyage.getSlintend() != null)
					prchPlace1.addInfoLine(
							"Intended number of slaves to purchase",
							slavesFmt.format(voyage.getSlintend()));
				
				if (voyage.getNcar13() != null)
					prchPlace1.addInfoLine(
							"Slaves purchased",
							slavesFmt.format(voyage.getNcar13()));
				
				if (isPrincipal)
				{
					if (voyage.getTslavesd() != null)
						prchPlace1.addInfoLine(
								"Total slaves purchased",
								slavesFmt.format(voyage.getTslavesd()));
					
					if (voyage.getSlaximp() != null)
						prchPlace1.addInfoLine(
								"Total slaves embarked",
								slavesFmt.format(voyage.getSlaximp()));					
				}

				purchasesLeg.addPlace(prchPlace1);
				
			}
			
			if (prchPort2 != null)
			{
				
				boolean isPrincipal = prchPort2.equals(prchPortPrincipal);
				
				VoyageRoutePlace prchPlace2 = new VoyageRoutePlace(
						prchPort2.getId().longValue(),
						prchPort2.getName(),
						prchPort2.getLongitude(),
						prchPort2.getLatitude(),
						new VoyageRouteSymbolPurchase(1, isPrincipal));
				
				if (!isPrincipal)
					prchPlace2.setPurpose(
							"Second place of slave purchase");
				else
					prchPlace2.setPurpose(
							"Second (and major) place of slave purchase");
				
				if (voyage.getDateleftafr() != null && prchPort3 == null)
					prchPlace2.addInfoLine(
							"Date that vessel left",
							dateFmt.format(voyage.getDateleftafr()));
				
				if (voyage.getNcar15() != null)
					prchPlace2.addInfoLine(
							"Slaves purchased",
							slavesFmt.format(voyage.getNcar15()));
				
				if (isPrincipal)
				{
					if (voyage.getTslavesd() != null)
						prchPlace2.addInfoLine(
								"Total slaves purchased",
								slavesFmt.format(voyage.getTslavesd()));
					
					if (voyage.getSlaximp() != null)
						prchPlace2.addInfoLine(
								"Total slaves embarked",
								slavesFmt.format(voyage.getSlaximp()));					
				}
				
				purchasesLeg.addPlace(prchPlace2);
				
			}
			
			if (prchPort3 != null)
			{
				
				boolean isPrincipal = prchPort3.equals(prchPortPrincipal);
				
				VoyageRoutePlace prchPlace3 = new VoyageRoutePlace(
						prchPort3.getId().longValue(),
						prchPort3.getName(),
						prchPort3.getLongitude(),
						prchPort3.getLatitude(),
						new VoyageRouteSymbolPurchase(2, isPrincipal));
				
				if (!isPrincipal)
					prchPlace3.setPurpose(
							"Third place of slave purchase");
				else
					prchPlace3.setPurpose(
							"Third (and major) place of slave purchase");
				
				if (voyage.getDateleftafr() != null)
					prchPlace3.addInfoLine(
							"Date that vessel left",
							dateFmt.format(voyage.getDateleftafr()));
				
				if (voyage.getNcar17() != null)
					prchPlace3.addInfoLine(
							"Slaves purchased",
							slavesFmt.format(voyage.getNcar17()));
				
				if (isPrincipal)
				{
					if (voyage.getTslavesd() != null)
						prchPlace3.addInfoLine(
								"Total slaves purchased",
								slavesFmt.format(voyage.getTslavesd()));
					
					if (voyage.getSlaximp() != null)
						prchPlace3.addInfoLine(
								"Total slaves embarked",
								slavesFmt.format(voyage.getSlaximp()));					
				}
				
				purchasesLeg.addPlace(prchPlace3);
				
			}
			
			if (prchPort1 == null && prchPort2 == null && prchPort3 == null)
			{
				
				VoyageRoutePlace prchPlacePrincipal = new VoyageRoutePlace(
						prchPortPrincipal.getId().longValue(),
						prchPortPrincipal.getName(),
						prchPortPrincipal.getLongitude(),
						prchPortPrincipal.getLatitude(),
						new VoyageRouteSymbolPurchasePrincipal());
				
				prchPlacePrincipal.setPurpose(
						"Principal place of slave purchase");
				
				if (voyage.getDatebuy() != null)
					prchPlacePrincipal.addInfoLine(
							"Date that slave purchase began",
							dateFmt.format(voyage.getDatebuy()));
				
				if (voyage.getDateleftafr() != null)
					prchPlacePrincipal.addInfoLine(
							"Date that vessel left last slaving port",
							dateFmt.format(voyage.getDatebuy()));
				
				if (voyage.getTslavesd() != null)
					prchPlacePrincipal.addInfoLine(
							"Total slaves purchased",
							slavesFmt.format(voyage.getTslavesd()));
				
				if (voyage.getSlaximp() != null)
					prchPlacePrincipal.addInfoLine(
							"Total slaves embarked",
							slavesFmt.format(voyage.getSlaximp()));					
				
				purchasesLeg.addPlace(prchPlacePrincipal);
				
			}

		}
		
		// port of sells
		if (
				sellPort1 != null ||
				sellPort2 != null ||
				sellPort3 != null ||
				sellPortPrincipal != null)
		{
			VoyageRouteLeg sellsLeg = new VoyageRouteLeg(); 
			route.addLeg(sellsLeg);
			
			if (sellPort1 != null)
			{
				
				boolean isPrincipal = sellPort1.equals(sellPortPrincipal);
				
				VoyageRoutePlace sellPlace1 = new VoyageRoutePlace(
						sellPort1.getId().longValue(),
						sellPort1.getName(),
						sellPort1.getLongitude(),
						sellPort1.getLatitude(),
						new VoyageRouteSymbolSell(0, isPrincipal));
				
				if (!isPrincipal)
					sellPlace1.setPurpose(
							"First place of slave landing");
				else
					sellPlace1.setPurpose(
							"First (and major) place of slave landing");
				
				sellPlace1.setPurpose("First place of slave landing");
				
				if (voyage.getDateland1() != null)
					sellPlace1.addInfoLine(
							"Date of landing",
							dateFmt.format(voyage.getDateland1()));
				
				if (voyage.getDatedepam() != null && sellPort2 == null)
					sellPlace1.addInfoLine(
							"Date left on return voyage",
							dateFmt.format(voyage.getDatedepam()));
				
				if (voyage.getSlaarriv() != null)
					sellPlace1.addInfoLine(
							"Slaves arrived",
							slavesFmt.format(voyage.getSlaarriv()));
				
				if (voyage.getSlas32() != null)
					sellPlace1.addInfoLine(
							"Slaves disembarked",
							slavesFmt.format(voyage.getSlas32()));		
				
				if (isPrincipal)
					if (voyage.getSlamimp() != null)
						sellPlace1.addInfoLine(
								"Total slaves disembarked",
								slavesFmt.format(voyage.getSlamimp()));				
				

				sellsLeg.addPlace(sellPlace1);

			}
			
			if (sellPort2 != null)
			{
			
				boolean isPrincipal = sellPort2.equals(sellPortPrincipal);
				
				VoyageRoutePlace sellPlace2 = new VoyageRoutePlace(
						sellPort2.getId().longValue(),
						sellPort2.getName(),
						sellPort2.getLongitude(),
						sellPort2.getLatitude(),
						new VoyageRouteSymbolSell(1, isPrincipal));
				
				if (!isPrincipal)
					sellPlace2.setPurpose(
							"Second place of slave landing");
				else
					sellPlace2.setPurpose(
							"Second (and major) place of slave landing");

				if (voyage.getDateland2() != null)
					sellPlace2.addInfoLine(
							"Date of landing",
							dateFmt.format(voyage.getDateland2()));
				
				if (voyage.getDatedepam() != null && sellPort3 == null)
					sellPlace2.addInfoLine(
							"Date left on return voyage",
							dateFmt.format(voyage.getDatedepam()));
				
				if (voyage.getSlas36() != null)
					sellPlace2.addInfoLine(
							"Slaves disembarked",
							slavesFmt.format(voyage.getSlas36()));
				
				if (isPrincipal)
					if (voyage.getSlamimp() != null)
						sellPlace2.addInfoLine(
								"Total slaves disembarked",
								slavesFmt.format(voyage.getSlamimp()));				
				
				sellsLeg.addPlace(sellPlace2);
			
			}
			
			if (sellPort3 != null)
			{
				
				boolean isPrincipal = sellPort3.equals(sellPortPrincipal);
				
				VoyageRoutePlace sellPlace3 = new VoyageRoutePlace(
						sellPort3.getId().longValue(),
						sellPort3.getName(),
						sellPort3.getLongitude(),
						sellPort3.getLatitude(),
						new VoyageRouteSymbolSell(2, isPrincipal));
				
				if (!isPrincipal)
					sellPlace3.setPurpose(
							"Third place of slave landing");
				else
					sellPlace3.setPurpose(
							"Third (and major) place of slave landing");

				if (voyage.getDateland3() != null)
					sellPlace3.addInfoLine(
							"Date of landing",
							dateFmt.format(voyage.getDateland3()));
				
				if (voyage.getDatedepam() != null)
					sellPlace3.addInfoLine(
							"Date left on return voyage",
							dateFmt.format(voyage.getDatedepam()));
				
				if (voyage.getSlas39() != null)
					sellPlace3.addInfoLine(
							"Slaves disembarked",
							slavesFmt.format(voyage.getSlas39()));				

				if (isPrincipal)
					if (voyage.getSlamimp() != null)
						sellPlace3.addInfoLine(
								"Total slaves disembarked",
								slavesFmt.format(voyage.getSlamimp()));				
				
				sellsLeg.addPlace(sellPlace3);
				
			}
			
			if (sellPort1 == null && sellPort2 == null && sellPort3 == null)
			{
				
				VoyageRoutePlace sellPlacePricipal = new VoyageRoutePlace(
						sellPortPrincipal.getId().longValue(),
						sellPortPrincipal.getName(),
						sellPortPrincipal.getLongitude(),
						sellPortPrincipal.getLatitude(),
						new VoyageRouteSymbolSellPrincipal());
				
				sellPlacePricipal.setPurpose(
						"Principal place of slave landing");
				
				if (voyage.getDatedepam() != null)
					sellPlacePricipal.addInfoLine(
							"Date left on return voyage",
							dateFmt.format(voyage.getDatedepam()));
				
				if (voyage.getSlamimp() != null)
					sellPlacePricipal.addInfoLine(
							"Total slaves disembarked",
							slavesFmt.format(voyage.getSlamimp()));				
				
				sellsLeg.addPlace(sellPlacePricipal);
			
			}
			
		}
		
		// place where voyage ended
		if (returnPort != null)
		{
			VoyageRouteLeg endLeg = new VoyageRouteLeg(); 
			route.addLeg(endLeg);
			
			VoyageRoutePlace returnPlace = new VoyageRoutePlace(
					returnPort.getId().longValue(),
					returnPort.getName(),
					returnPort.getLongitude(),
					returnPort.getLatitude(),
					new VoyageRouteSymbolEnd());
			
			returnPlace.setPurpose(
					"Place place of slave landing");

			if (voyage.getDateend() != null)
				returnPlace.addInfoLine(
						"Date when voyage completed",
						dateFmt.format(voyage.getDateend()));
			
			endLeg.addPlace(returnPlace);
			
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