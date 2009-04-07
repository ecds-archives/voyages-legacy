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
				<f:param value="essays-intro-09" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>A Brief Overview of the Trans-Atlantic Slave Trade</h1>
					
					<div class="essay-info">
						<span class="essay-author">David Eltis</span>
						<span class="essay-location">(Emory University)</span>,
						<span class="essay-date">2007</span>
					</div>

					<h2>The Trade’s Influence on Ethnic and Racial Identity</h2>

					<p>In the Atlantic after 1492, oceans that had hermetically
					sealed peoples and cultures from each other sprouted sea-lanes
					almost overnight. Cultural accommodation between peoples, in this
					case between Europeans and non-Europeans, always took time. The big
					difference was that before Columbus, migrations had been gradual
					and tended to move outwards from the more to the less densely
					populated parts of the globe. But Columbian contact was sudden, and
					inhibited any gradual adjustment, cultural as well as
					epidemiological. A merging of perceptions of right and wrong, group
					identities, and relations between the sexes, to look only at the
					top of a very long list of social values, could not be expected to
					occur quickly in a post-Columbian world. In short, cultural
					adjustment could not keep pace with transportation technology. The
					result was first the rise, and then, as perceptions of the
					insider-outsider divide slowly changed, the fall, of the
					trans-Atlantic trade in enslaved Africans.</p>

					<p>During the long coercive interlude of forced trans-Atlantic
					migration European and African conceptions of self and community
					(and eligibility for enslavement) did not remain static. On the
					African side, the major effect of the African-European exchange was
					to encourage an elementary pan-Africanism, at least among victims.
					The initial and unintentional impact of European sea-borne contact
					was to force non-elite Africans to think of themselves as part of a
					wider African group. Initially, this group might be Igbo, or
					Yoruba, and soon, in addition, blacks as opposed to whites. At the
					most elemental level, by the late eighteenth century, the slaves at
					James Island vowed to drink the blood of the whitemen. In Gorée,
					a little later, one third of the slaves in a carefully planned
					conspiracy, “would go in the village and be dispersed to massacre
					the whites”. When asked “[w]hether it were true that they had
					planned to massacre all the whites of the island....[t]he two
					leaders, far from denying the fact or looking for prevarication,
					answered with boldness and courage: that nothing was truer”.(3)
					Many similar incidents could be cited from the Americas side of the
					Atlantic. And on board a slave ship with all the slaves always
					black, and the crew largely white, skin color defined ethnicity.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-08.faces">The Ending of the Slave Trade</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-10.faces">Eventual Abolition</a>
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