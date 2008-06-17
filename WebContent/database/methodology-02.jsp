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
		<h:outputText value="Understanding the Database" />
		<h:outputText value="Methodology" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<f:param value="methodology-02" binding="#{EssaysBean.paramActiveMenuId}" />
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
					
					<h2>Coverage of the Slave Trade</h2>

					<p>One immediate question is how complete are the data? It is probable that our data set now includes more than
					95 percent of all voyages that left British ports—and the British were the second largest of the national slave
					trader groups. The data on the eighteenth-century French and Dutch slave trades are also largely complete. The
					reasons for such comprehensive coverage are fairly obvious. Compared with other slave traders, northwestern
					European trading nations conducted the great bulk of their business relatively late in the slave trade era when
					everyone kept better records. Surviving sources in these countries are therefore abundant. Casual inspection of
					the relevant variables in the data set shows that almost all the voyages leaving ports in these countries have
					more than one source of information, and some have as many as eighteen. Yet the data on the Iberian and Brazilian
					trades after 1750 are also relatively complete, and information on the earlier period for these regions is vastly
					greater than it was ten years ago. For a country by country assessment of the completeness of the data, readers are
					referred to Chapter 1 of Extending the Frontiers <span onmouseover=tooltipShow() onmouseout=tooltipHide(event,"")
					class="superscript">(1)</span> and the spreadsheets downloadable from this web site that underpin
					our estimates of the overall size of the slave trade. Our estimate of the total volume of slaves carried off from
					Africa to the Americas is 12.5 million, and that the total number of voyages that set out to obtain captives was
					43,600. New information will certainly emerge from the archives, but we think it unlikely that future scholars will
					dramatically increase the  size or scale of the trans-Atlantic slave trade. </p>

					<p>The 34,941 trans-Atlantic voyages contained in the database allows us to infer the total number of voyages
					carrying slaves from Africa. The Estimates page suggests that 12 ½  million captives (12,520,000) departed
					Africa for the Americas. Dividing this total by the average number of people embarked per voyage, 304 individuals,
					yields 41,190 voyages. Similarly, the Estimates pages suggests that 10.7 million enslaved Africans disembarked,
					mainly in the Americas. Given the average number disembarked per voyage, 265 people, yields an estimated 40,380
					voyages arriving. Not all 34,941 voyages in the database carried slaves from Africa. A total of 1,536 ships (4.4%)
					never reached the African coast because they were lost at sea, captured or suffered some other misfortune. After
					removing these voyages, the database contains some trace of 81 percent of voyages that embarked captives. The
					database also contains records of 32,788 voyages that disembarked slaves, or could have done so (in other words,
					for some of these we do not know the outcome of the voyage). A total of 763 of these disembarked their slaves in
					the Old World. The latter group comprised mainly ships captured in the nineteenth century which were taken to Sierra
					Leone and St. Helena as part of the attempt to suppress the trade. A further 167 sank after leaving Africa with the
					loss of their slaves. In all, the database contains some record of almost 80 percent of vessels disembarking captives.
					Of course, there are other estimates of the volume of the trans-Atlantic slave trade. If we take a higher estimate of,
					say, 15.4 million departures,<span class="superscript">(2)</span> then the Voyages Database documents
					two-thirds of all slaving voyages that sailed between 1514 and 1866.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-01.faces">Introduction</a>
						</td>
						<td class="method-next">
							<a href="methodology-03.faces">Nature of Sources</a>
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