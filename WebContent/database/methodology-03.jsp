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
					
					<h2>Nature of Sources</h2>
					
					<p>Much of what is new in this data set lies in the sources, which call for some discussion. The published data
					draw on a wide range of published and archival information. Postma, Mettas, and Richardson used new material in
					the preparation of their published catalogues of voyages. Our data set does not reproduce all the sources that
					previous authors used and listed. Thus, voyages drawn from published sources are represented here by that single
					reference to them alone. Anyone wishing to consult their archival references will have to look them up in the
					hard copy of these published volumes. We edited individual voyage details in these collections only when we found
					new documentary evidence to support a change; consequently, we added these new references to the source record.
					Pulling together the results of work carried out in separate national archives was particularly fruitful because
					trans-Atlantic slave vessels could clear from one jurisdiction and arrive in the Americas in another. Specifically,
					the international nature of the slave trade meant that a voyage that might appear primarily in one national body of
					records had a very good chance of showing up, in addition, in the records of other countries. Thus, voyages organized
					by London merchants operating independently of the Royal African Company (RAC) in the 1670s and 1680s obtained their
					slaves in South-east Africa, outside the RAC’s English monopoly and where the English East India Company had little
					physical presence. Because almost all these ships called at the Cape before beginning their trans-Atlantic journeys,
					several of them appear in the Dutch Cape of Good Hope archives.<span class="superscript">(3
					)</span> Portuguese ships leaving Bahia in Brazil for the "Mina"
					coast appear in English Cape Coast Castle material at the Public Record Office. Slave ships of every nationality appear
					in Lloyd’s Lists, and of course the hundreds of slave ships captured in the many European wars are often carefully
					documented in the archives of the captors, as well as in the records of the nations to which the ships belonged. Indeed,
					the South Atlantic Portuguese trade  has fewer sources per voyage, precisely because this branch of the traffic operated
					independently of the others. Winds and ocean currents kept the South Atlantic trade out of the non-Portuguese archives,
					as well as keeping northern Europeans out of South Atlantic ports.</p>
					
					<p>Of the 34,941 voyages in the data set, 12,452 have only one source listed. In fact, more than half of these apparently
					single-source records are taken from already published material where, in nearly all cases, additional primary references
					are to be found. Furthermore, other publications on which the present data set draws, such as Coughtry’s listing of Rhode
					Island voyages, are based on a range of primary documents which are not listed by voyage in the publication itself. After
					allowing for these multi-sourced single references, it would appear that perhaps only one in six of the 34,941 voyages are
					based on a single historical record. Sixty percent of the voyages in the set have three or more separate sources each.
					Researchers should no longer need to depend on data collected on the basis of a single source. Abbreviated reference to
					the sources for any particular voyage can be viewed in the results section of the database by clicking on “configure columns”
					and transferring “sources” from left box to the right box in the resulting window. The full reference may be viewed by rolling
					the cursor over the abbreviated source or by clicking on the row of a voyage to view voyage details. Alphabetic listings of
					references and full descriptions of locations are to be found in <a href="sources.faces">Home> Voyages Database> Understanding
					the database> Sources </a>.</p>

					
					<p>While the sources are relatively rich, diversity brings a new set of problems. We can hardly expect that reports on voyages
					made several thousand miles—as well as several months—apart, often in different languages and under different bureaucracies,
					each with a separate set of official procedures to follow, to always generate perfectly consistent information. For example,
					216 voyages in the data set apparently arrived in the Americas with more slaves on board than when they left Africa. Others
					left port more than once on the same voyage, and some ships reportedly changed tonnage and even rig in the course of the voyage.
					The same ship occasionally appears under more than one name on the same voyage. Those used to working with a single source per
					voyage and generating data sets without any conflicting information should be warned that the editors have not attempted to
					correct all these problems. The data set offered here is by no means "clean" in the sense of being entirely internally consistent.
					We have pursued and eliminated many of the inconsistencies, but to eliminate all would have imposed an order on the historical
					record that anyone who has visited the archives (or indeed examined published sources such as Mettas or Richardson) knows does
					not exist. The editors always entered only one value per variable when faced with alternative information. In making such choices,
					we followed certain rules that researchers can change after going back to the sources. If users elect to do this, however, they,
					too, will have a set which is both not "clean" and not necessarily reflective of the historical records from which it is drawn.</p>
				
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-02.faces">Coverage of the Slave Trade</a>
						</td>
						<td class="method-next">
							<a href="methodology-04.faces">Cases and Variables</a>
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