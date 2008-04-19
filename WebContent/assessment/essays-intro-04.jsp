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
				<f:param value="essays-intro-04" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Currents Driving the Trade</h2>

					<p>The transatlantic slave trade therefore grew from a strong
					demand for labor in the Americas driven consumers of plantation
					produce and precious metals, initially in Europe. Because
					Amerindians died in large numbers, and insufficient numbers of
					European were prepared to cross the Atlantic, the form that this
					demand took was shaped by conceptions of social identity on four
					continents which ensured that the labor would comprise mainly
					slaves from Africa. But the central question of which peoples from
					Africa went to a given region of the Americas, and which group of
					Europeans or their descendants organized such a movement cannot be
					answered without an understanding of the wind and ocean currents of
					the North and South Atlantics. There are two systems of wind and
					ocean currents in the North and South Atlantic that follow the
					pattern of giant wheels - one lies north of the equator turns
					clockwise, while its counterpart to the south turns
					counterclockwise.i The northern wheel largely shaped the north
					European slave trade and was dominated by the English. The southern
					wheel shaped the huge traffic to Brazil which for three centuries
					was almost the almost exclusive preserve of the largest slave
					traders of all, the Portuguese.ii Despite their use of the
					Portuguese flag, slave traders using the southern wheel ran their
					business from ports in Brazil, not in Portugal. Winds and currents
					thus ensured two major slave trades – the first rooted in Europe,
					the second in Brazil. Winds and currents also ensured that Africans
					carried to Brazil came overwhelmingly from Angola, with south-east
					Africa and the Bight of Benin playing smaller roles, and that
					Africans carried to North America, including the Caribbean, left
					from mainly West Africa with the Bights of Biafra and Benin and the
					Gold Coast predominating. Just as Brazil overlapped on the northern
					system by drawing on the Bight of Benin, the English, French and
					Dutch carried some slaves from northern Angola into the Caribbean.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="essay-prev-next">
					<tr>
						<td class="essay-prev">
							<a href="essays-intro-03.faces">The Enslavement of Africans</a>
						</td>
						<td class="essay-next">
							<a href="essays-intro-05.faces">The Enslavement of Africans</a>
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