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

	<script language="javascript" type="text/javascript">
	function popitup(url) {
		newwindow=window.open(url,'name','height=502,width=500,resizable, scrollbars, location=0');
		if (window.focus) {newwindow.focus()}
		return false;
	}
	</script>
	
</head>
<body>
<f:view>

<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-seasonality-08" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Trans-Atlantic pathways and harvest cycles</h2>

					<p>African and Atlantic coastal markets exhibited varying
					seasonal patterns in the numbers of slaves exported and imported.
					In the most seasonal slaving markets in the Atlantic
					world—Senegambia in Africa and the Chesapeake in the
					Americas—rainfall and temperature constraints reduced the number of
					“in crop months” and narrowed merchants’ trading windows.
					Comparatively few enslaved Africans shipped from northwest Africa
					between rainy July and November; Virginia and Maryland planters
					purchased most of their new agricultural workers between April and
					October. In Upper Guinea markets, most captains began sailing the
					Middle Passage in the North Atlantic spring, a time of the year
					that would place them in American markets in the North Atlantic
					summer. Strong links between Upper Guinea and the
					Carolinas-Chesapeake occurred because trans-Atlantic agricultural
					cycles meshed.</p>

					<p>Captain Robert Doegood’s voyage on the <i>Arthur</i> in 1677-78
					reached slaving markets in Africa and the Americas at the end of
					in-crop seasons, making this voyage atypical of those sailing from
					the Bight of Biafra to the West Indies. In the century prior to the
					American Revolution, British vessels departing Biafran ports in
					March or April often attempted to reach the in-season summer North
					American slaving markets rather than risk uncertain demand in
					out-of-crop Caribbean colonies. After May, with each passing month
					they decided increasingly to sell slaves in the British West
					Indies. Northern planters infrequently purchased enslaved Africans
					shipped from the Bight of Biafra’s fall provisioning-slaving season
					(Figure 3).</p>
					
					<center>
					<a href="essays-seasonality-fig-3.png" onclick="return popitup('essays-seasonality-fig-3.png')">
						<img src="essays-seasonality-fig-3.png" width="500"/></a>
					</center>
					
					<p>French and Portuguese slave traders also shifted
					agricultural workers between trans-Atlantic harvest cycles. Cap
					Français, the largest French West Indian port and the principal
					disembarkation center for French slaving vessels in northern St.
					Domingue, has the island’s rainiest October-February winter; the
					greatest number of enslaved Africans, correspondingly, arrived in
					the dry April-June quarter. In southern St. Domingue, the dry season
					occurs earlier, in December-February, the harvesting months and
					time of increased planter demand for labor. Whereas northern St.
					Domingue drew upon Senegal’s January-April provisioning-slaving
					season, French planters in the south purchased comparatively more
					Africans shipped overseas during the September-December
					provisioning-slaving seasons in the Bight of Biafra. In the late
					1700s, the Portuguese resettled trading posts in coastal
					Guinea-Bissau, a staple rice region with a marked November-April
					provisioning-slaving season. Captains purchased enslaved Africans
					during these months to work in the May-July Maranhão rice season.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-seasonality-07.faces">Slave-trading seasonality: case studies</a>
						</td>
						<td class="essay-next">
							<a href="essays-seasonality-09.faces">Conclusion</a>
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

<%@ include file="../footer.jsp" %>

</body>
</html>