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
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
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
					
					<h2>Cases and Variables</h2>

					<p>New material tends to raise the question of the appropriateness of the variables used. The selection offered here has changed
					several times in the last six years and will no doubt change again in the future as interests shift. Each entry in our data set
					is a single voyage, assigned a unique identification number as the first of piece of information (VOYAGEID). The question of
					what voyages to include is also to some extent arbitrary. The term "trans-Atlantic" is less straightforward than it appears.
					Omitting ships sailing to the Mascarene Islands was an easy decision, but several French ships in the late eighteenth century
					began their slaving activities in the Indian Ocean, but then on the same voyage brought slaves to the Americas after selling
					some Africans in Bourbon and the Cape of Good Hope. What to do with the British ships that carried hundreds of children from
					the Upper Guinea coast to Lisbon in the mid-eighteenth century? These we included on the basis of length of voyage. Should
					one include the Portuguese trade to o São Tomé in the Bight of Biafra—probably the most enduring branch of the Atlantic slave
					trade? (excluded on the same basis). Then there were the more than 1,200 slave ships engaged in trans-Atlantic voyages, nearly
					half with slaves on board, that the British captured and carried into Sierra Leone, the Cape of Good Hope, St. Helena, Fernando Po,
					or Luanda, before they were able to reach their intended American destinations. These we included. Or even more confusing, the
					1,060 slaves awaiting shipment in barracoons in Ambriz, Angola, in May 1842, but carried off in British cruisers to St. Helena
					and Sierra Leone and never subjected to court proceedings of any kind, because they had never been on board a slave ship (excluded).
					<span class="superscript">(4)</span>
					Limits had to be established, but the data set provides a basis for those who disagree with those limits to use our work to create their
					own data sets. A total of 65 variables are made available on the search the database interface, general category, and 293 variables are
					available in the downloadable version of the database. Users should note, though, that the website set combines all sources into one
					variable and the day, month, year values are also combined into one variable for each of the ten dates entered. The names of captives,
					names of Caribbean agents, names of crew other than captain, the details of shipboard insurrection, and much other information are not
					included in the present data set, but may be added fairly easily or linked with it via the unique voyage identification number.</p>
					
					<p>The database contains two broad types of variables: data variables and imputed variables. The largest group, 44 of those in the
					search interface, are data variables. They incorporate information collected from the sources. Imputed variables, indicated by an
					asterisk, are mainly imputed from knowledge of the relevant voyage or adjacent voyages, calculated directly from data encountered
					in archival or published sources, or inferred from patterns observed in data variables when not documented directly in primary
					sources.  An example of an imputed variable is the “Region of Slave Disembarkation Broadly Defined,” a variable that allows one to
					group voyages to Jamaica or Cuba or St. Domingue into the broadly defined region “Caribbean”. In augmenting the number of voyages
					on which analysis can be conducted, the imputed variables produce more statistically significant results in using the options in
					the “Search the database” interface to create tables and custom graphs. They form the basis of the tables and graphs that users can build. </p>

					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-03.faces">Nature of Sources</a>
						</td>
						<td class="method-next">
							<a href="methodology-05.faces">Data Variables</a>
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