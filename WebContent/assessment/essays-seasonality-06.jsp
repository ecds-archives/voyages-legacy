<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Essays</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-essays.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-seasonality-06" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>Seasonality in the Trans-Atlantic Slave Trade</h1>
					
					<div class="essay-info">
						<span class="essay-author">Stephen D. Behrendt</span>
						<span class="essay-location">(Victoria University of Wellington)</span>,
						<span class="essay-date">2008</span>
					</div>
					
					<h2>Provisioning-slaving seasons</h2>

					<p>Seasonality in Atlantic slaving markets linked closely to
					food supplies, since merchants, whether African, European, or
					American, would not purchase large numbers of people they could not
					keep alive. There were distinct provisioning-slaving seasons in
					Atlantic regions dependent upon a short rainy season and a staple
					crop. In more diversified early modern economies, often those with
					fewer dry months (and hence more evenly distributed rainfall), food
					production and labor requirements on farms became less seasonal.
					Provisioning-slaving seasons ebbed during rainy planting months and
					increased during and after dry season harvests. After crops were
					harvested and stored, African merchants sold enslaved farmers and
					provisions to coastal middlemen who, in turn, sold these captives
					to ship captains.</p>

					<p>In Africa, Upper Guinea (from Senegal to the Ivory Coast)
					and the Bight of Biafra had marked provisioning-slaving seasons. In
					Senegal and Gambia, millet stocks increased in March-May before the
					summer rains. May-July was correspondingly the greatest quarter in
					the Senegambian slave export trade—three times greater than in the
					September-November quarter. Along the Sierra Leone/Windward Coasts,
					the rice-slave season began in November and supplies of food and
					people began to dwindle in late April. In spring 1751, south of
					Sierra Leone, Liverpool captain John Newton <a
						href="../database/voyage.faces?voyageId=90350">VoyageID 90350</a>,
					the author of “Amazing Grace,” purchased late-season rice and
					slaves. He remarked on April 30th in his logbook, held today at the
					National Maritime Museum (UK), “the season is so far advanced.”
					Sierra Leone/Windward Coast slave exports from March to May almost
					doubled totals from June to August. In the Bight of Biafra, the
					season’s first yams arrived in markets in July and August; the peak
					harvest occurred in October, and supplies remained until February
					or March. September-November slave exports doubled those totals
					from June-August. Fewer dry months along the Gold Coast or Bight of
					Benin enabled farmers to often double-crop maize and other cereals.
					Given greater food supplies, slave exports exhibited less of a
					seasonal trend. Traders also did not identify provisioning-slaving
					seasons along the West-Central African coast. With few foodstuffs
					available for export overseas, the link between harvest cycles and
					slave supplies is weakest in the South Atlantic African slaving
					markets.</p>

					<p>The timing of dry season grain and cash crop harvests
					regulated the New World demand for enslaved African labor. The
					sugar producing West Indies and Guianas imported more slaves during
					the December-May corn and cane harvests. In Brazilian
					sugar-producing centers, such as Bahia, the provisioning-slaving
					season centered on the drier November-February quarter. During
					three months of the harvest season, slave import figures ranged from
					twenty-nine percent of annual totals (Cuba, Pernambuco, Bahia) to
					forty-four percent (northwest Jamaica). In the smaller
					non-sugar-growing regions of North and South America, captains
					disembarked comparatively large numbers of slaves in-season during
					rainier months. Chesapeake tobacco planters only demanded new
					migrant farmers during the April-May spring rains, when men and
					women transplanted tobacco stalks to the fields, and in
					June-August, before harvesting and curing. The autumn corn harvest
					provided food stocks to sustain workers forced to produce the
					annual tobacco crop. In the rice-growing Carolina/Georgia
					Lowcountry, Surinam, and Maranhão, planters purchased twice as many
					workers during the four in-crop months.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-seasonality-05.faces">Agricultural calendars and labor requirements</a>
						</td>
						<td class="essay-next">
							<a href="essays-seasonality-07.faces">Slave-trading seasonality: case studies</a>
						</td>
					</tr>
					</table>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>
</body>
</html>