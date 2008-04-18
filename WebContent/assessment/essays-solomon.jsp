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
				<f:param value="essays-solomon" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>Job Ben Solomon and the Curse of Slavery in the Atlantic World</h1>
					
					<div class="essay-info">
						<span class="essay-author">Domingues da Silva, Daniel</span>
						<span class="essay-location">(Emory University)</span>,
						<span class="essay-date">July 2007</span>
					</div>

					<p>Before the nineteenth century, most people in the world
					lived under some type of dependency. Slavery was just one of them.
					The story of Ayuba Suleiman Diallo provides an insight into a world
					where slavery was just another form of social relationship.
					Suleiman was a Fulbe Muslim better known as Job Ben Solomon. In
					1731, he traveled from Bondu to sell two slaves upon his father’s
					request at River Gambia. There he tried to sell the slaves to a
					Captain Pike but they could not agree on a price, so Solomon sold
					the slaves for cows to another African trader. Ironically, on the
					way home Solomon was captured by raiders and sold to the same
					captain he had met previously. Captain Pike embarked Solomon to
					Maryland, one of the British colonies on the North American
					mainland, where he spent about a year as a slave before returning
					to Africa via England.</p>

					<p>In the Americas, Solomon was able to share some of the
					experiences of many enslaved Africans, and like many of his fellow
					Africans he attempted to run away. Solomon was a slave at the
					tobacco plantations of Kent Island, Maryland. He had probably never
					worked under a labor regime comparable to that held in the American
					plantations. On one of his attempts to escape, he met Thomas
					Bluett, an Englishmen, who, impressed by Solomon’s Muslim
					education, helped him gain his liberty and return to Africa.
					Indeed, one of Solomon’s letters still survives in the British
					Library and his religious convictions inspired images portraying
					him, such as the one available in our gallery of images, published
					originally in 1750 in the <i>Gentleman’s Magazine</i>.</p>

					<p>Bluett’s memoirs, published in 1734, provide one of the most
					complete accounts of Solomon’s life. Another is available by
					Francis Moore, who in 1744 published his accounts of travel up the
					River Gambia, where he met Solomon. Thanks to the accounts of
					Bluett and Moore, it is possible to trace in the archives the
					vessel in which Solomon crossed the Atlantic. He sailed in the ship
					“Arabella,” commanded by the said Captain Pike and owned by William
					and Henry Hunt, merchants of London. (For more details see VoyageID
					75094).</p>

					<p>Despite his life trajectory, Solomon’s story has received
					much less attention than have other known survivors of the Middle
					Passage, such as Olaudah Equiano or Venture Smith. Job Ben Solomon
					was clearly a victim of the traffic as well as a trader in slaves,
					and indeed he resumed his slave trading activities when he returned
					to Africa, participating in the activities of the famous Royal
					African Company. Perhaps, the most important lesson to be drawn
					from Solomon’s life was that, while slavery in Africa was very
					different from slavery in the Americas, slavery was widely accepted
					in the mid-eighteenth century among both Europeans and Africans.</p>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>
</body>
</html>