<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>History</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-info.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>

	<s:siteHeader activeSectionId="about">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="./index.faces"><h:outputText value="About the Project" /></h:outputLink>
		<h:outputText value="History" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<% /* <f:param value="essays-solomon" binding="#{EssaysBean.paramActiveMenuId}" /> */ %>
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>History of the Project</h1>
					
						<p>A glance at the Sources section of “Understanding the Database” establishes Voyages as the product 
						of an international research endeavor that has ranged far beyond the labors of the current project team. 
						From the late 1960s, Herbert S. Klein and other scholars began to collect archival data on slave-trading 
						voyages from unpublished sources and to code them into a machine-readable format. In the 1970s and 1980s, 
						scholars created a number of slave ship datasets, several of which the current authors chose to 
						recode from the primary sources rather than integrate the datasets of those scholars into the present set. By the late 1980s, there were 
						records of approximately 11,000 individual trans-Atlantic voyages in sixteen separate datasets, not all 
						of which were trans-Atlantic, nor, as it turned out, slave voyages. And of course, some sets overlapped 
						others. Several listings of voyages extracted from more than one source had appeared in hard copy form, 
						notably three volumes of voyages from French ports published by Jean Mettas and Serge and Michelle Daget 
						and two volumes of Bristol voyages (expanded to four by 1996) authored by David Richardson. The basis for 
						each dataset was usually the records of a specific European nation or the particular port where slaving 
						voyages originated, with the information available reflecting the nature of the records that had survived 
						rather than the structure of the voyage itself. Scholars of the slave trade spent the first quarter century 
						of the computer era working largely in isolation, each using one source only as well as a separate format, 
						though the Curtin, Mettas, and Richardson collections were early exceptions to this pattern. </p>
						
						<p>The idea of creating a single multisource dataset of trans-Atlantic slave voyages emerged from a 
						chance meeting of David Eltis and Stephen Behrendt in the British Public Record Office in 1990 while 
						they were working independently on the early and late British slave trades. At about the same time, David 
						Richardson was taking over detailed multisource work on the large mid-eighteenth-century Liverpool shipping 
						business begun years earlier by Maurice Schofield. All this work, together with the Bristol volumes that Richardson 
						had already published, made it seem feasible to integrate the records for the very large British slave trade for the 
						first time, and beyond that, given the available Dutch, French, and Portuguese data, to collect a single dataset for 
						the trade as a whole. Meetings in January, 1991 at the American Historical Association and, in 1992, at the 
						W. E. B. Du Bois Institute for Afro-American Research at Harvard University, headed by Professor Henry L. Gates, 
						Jr resulted in grant proposals to major funding agencies. In July 1993 the project received funding from the National 
						Endowment for the Humanities with supplementary support coming from the Mellon Foundation. </p>
						
						<p>By the time the project began, Johannes Postma’s Dutch data had become available (subsequently revised in 2003), 
						as had Stephen Behrendt’s compilation of the extensive British trade after 1779, and also the large and complex Richardson, 
						Beedham, and Schofield pre-1787 Liverpool Plantation Register data set, all in machine-readable format. Quantities of smaller 
						sets of published material available only in hard-copy form had been available for some time, and as awareness of the project increased, 
						scholars volunteered unpublished data. In the course of the next three years, the project undertook three 
						major tasks. The first was standardizing the existing data. Pioneers in the field had collected their data using different 
						definitions of variables, sometimes of apparently similar items of information, as well as a range of organizational formats 
						(for example using ship-based rather than voyage-based data). The second task was collating voyages which appeared in several 
						different sets, converting single-source data sets into multisource equivalents and even checking on the validity of old 
						compilations. The third task, which became increasingly important as the project progressed, was adding new information. 
						About half of the 27,233 voyage records subsequently published on the 1999 CD-ROM in 1999 were new. </p>
						
						<p>Voyages is the product of a further great surge of information on the slave trade that has happened since 1999. Latin-American 
						slaving expeditions were seriously under-represented on the CD-ROM, and, as a consequence, between 2001 and 2005 a major research 
						initiative was undertaken in Portuguese and Spanish language archives around the Atlantic basins to address this deficiency. 
						Three years of funding for this work (from 2002 to 2005) came from the Arts and Humanities Research Board of the United Kingdom 
						and was administered through the University of Hull with David Richardson and David Eltis as the principal investigators. Manolo 
						Florentino anchored the work in Rio de Janeiro, Roquinaldo Ferreira in Luanda, and Jelmer Vos in Lisbon, and other European 
						archives. The major documentary collections explored in this period were in archives in Luanda, Rio de Janeiro, Bahia, Lisbon, 
						Havana, Madrid, Sevilla, Amsterdam, Middelburg, Copenhagen, and London as well as the extensive eighteenth century newspaper 
						holdings of the Bodleian and British Libraries. But while this was happening scholars unconnected with the project continued 
						to give generously of their time and the archival data that they themselves had collected. While the range and depth of work 
						completed before the year 2000 was impressive, the size and scope of this post-1999 research effort can be gauged by the fact 
						that no less than sixty percent of the slave voyages in the Voyages Database contain information unavailable in 1999. As the 
						core dataset expanded, and called on greater varieties of sources, the problem of double-counting grew ever larger and more 
						complicated. In this period we reached the point at which almost as many resources had to be devoted to this as to gathering 
						the data in the first place. </p>
						
						<p>The construction of the present open access web site, Voyages was made possible by yet a third major award in 2006, this one 
						launched from Emory University. Once more the National Endowment for the Humanities was the source, but supplementary funding 
						came from the W. E. B. Du Bois Institute for Afro-American Research at Harvard. While the research work continued during the 
						two year period of these grants, the thrust of the effort shifted to presentation. The project team developed sophisticated 
						search interfaces for three different kinds of data, as well as estimates of the size and direction of the trade. The web site would 
						provide a range of ancillary material for educators including lesson plans and maps. Perhaps most important, the new site would provide 
						an opportunity for researchers everywhere to continue to contribute their discoveries and correct errors in the data that they might find. After 
						a peer-review process any new data or corrections will be added to the core database at three year intervals. It is difficult to 
						think of any international project of preserving and reconstructing history which has depended more on collaboration than Voyages 
						has. It is even harder to think of one that provides a better basis for such collaboration to continue into the future.  </p>
											
						<div style="border-top: 1px solid #CCCCCC;">
							<p>1 Franz Binder, Ernst van den Boogart, Henk den Heijer and Johannes Postma, James Pritchard, Andrea Weindl, 
							Antonio de Almeida Mendes, Manuel Barcia Paz, Alexandre Ribeiro, David Wheat and José Capela were among those 
							making major contributions from data collected for their own research. </p>
						</div>
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