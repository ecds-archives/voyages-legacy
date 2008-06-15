<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Methodology</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/tooltip.js" type="text/javascript" language="javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Understanding the database" />
		<h:outputText value="Methodology" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<f:param value="methodology-01" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="guide-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>
				
					<h1>Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods</h1>
					
                    <div class="method-info">
						<span class="method-author"><b>David Eltis</b></span>
						<span class="method-location">(Emory University)</span>,
						<span class="method-date">2007</span>
					</div>
					
					<p>It is difficult to believe in the first decade of the twenty-first century that just over two centuries
					ago, for those Europeans who thought about the issue, the shipping of enslaved Africans across the Atlantic
					was morally indistinguishable from shipping textiles, wheat, or even sugar. Our reconstruction of a major
					part of this migration experience covers an era in which there was massive technological change (steamers
					were among the last slave ships), as well as very dramatic shifts in perceptions of good and evil. Just as
					important perhaps were the relations between the Western and non-Western worlds that the trade both reflected
					and encapsulated. Slaves constituted the most important reason for contact between Europeans and Africans for
					nearly two centuries. The shipment of slaves from Africa was related to the demographic disaster consequent to
					the meeting of Europeans and Amerindians, which greatly reduced the numbers of Amerindian laborers and raised
					the demand for labor drawn from elsewhere, particularly Africa. As Europeans colonized the Americas, a steady
					stream of European peoples migrated to the Americas between 1492 and the early nineteenth century. But what is
					often overlooked is that, before 1820, perhaps three times as many enslaved Africans crossed the Atlantic as
					Europeans. This was the largest transoceanic migration of a people until that day, and it provided the Americas
					with a crucial labor force for their own economic development. The slave trade is thus a vital part of the history
					of some millions of Africans and their descendants who helped shape the modern Americas culturally as well as in
					the material sense.</p>
					
					<p>The genesis and history of Voyages Database is laid out on a separate page. In this essay we wish to alert users
					to its structure and to its limitations as well as its strengths. The data set contains thousands of names of ship
					owners and ship captains, but it contains no names of the millions of slaves carried to the Americas. On the other
					hand, this web site does provide the African names of and personal information about 67,004 captives who were found
					on board slave vessels detained by naval cruisers attempting to suppress the slave trade in the nineteenth century.
					These people can be searched and analyzed using the names interface. Although of limited utility for persons seeking
					their own family histories, our data set does provide an extraordinary source for historical reconstruction of the
					history of the African peoples in America. The details of the 34,941 voyages presented here greatly facilitate the
					study of cultural, demographic, and economic change in the Atlantic world from the late sixteenth to the mid-nineteenth
					centuries. Trends and cycles in the flow of African captives from specific coastal outlets should provide scholars with
					new, basic information useful in examining the relationships among slaving, warfare—in both Africa and Europe—political
					instability, and climatic and ecological change, among other forces. The data set in its earlier manifestations has already
					provided new impetus to assessments of the volume and demographic structure of the trans-Atlantic slave trade, and, when the
					African Names Database is properly interpreted, it will contribute as well to our understanding of slaving routes from the African
					interior to the coast. </p>
					
					<p>For European societies located on either side of the Atlantic, the data set contains new information on ship
					construction and registration and relatively extensive records of owners’ and captains’ names. It will now be
					easier to pursue connections between the slave trade and other sectors of European and American economies.
					Researchers should be able to unravel trends in long-distance shipping activities, particularly important
					because no comparable body of data exists for other transoceanic trades. Data on crew mortality are abundant.
					The implications for new assessments of the social as well as the economic role of the slave trade in the regions
					where the slave voyage originated are obvious. In short, the major aim of this Emory supported Voyages web resource is
					to facilitate and stimulate new research on the slave trade, the implications of which reach far beyond the slave
					trade itself.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">							
						</td>
						<td class="method-next">
							<a href="methodology-02.faces">Coverage of the Slave Trade</a>
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