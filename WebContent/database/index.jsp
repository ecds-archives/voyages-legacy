<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>The Database</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/tabs.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-tabs.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputText value="Voyage Database" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="section-index-layout">
		<tr>
			<td class="section-index-left-column">
			
				<s:expandableBox text="Quick Start">
					<f:verbatim escape="false">
						<a href="search.faces">Click here</a> to go directly to the query page for searching information in the main database.
					</f:verbatim>
				</s:expandableBox>
				
				<br>
				<% /* 
				<s:expandableBox text="Sample Queries">
					<f:verbatim escape="false">
						<ul class="box">
							<li>What was the first country to get slaves from Africa?</li>
							<li>How did the atlantic slave trade effect the economy of Europe, Africa, and America?</li>
							<li>Why did European interest in Africa turn from the slave trade to colonization?</li>
							<li>Where were they taken to?</li>
							<li>Which European country was the first to engage in the slave trade in Central Africa?</li>
							<li>Who sold the slaves to the European slavers?</li>
							<li>Has there ever been evidence of a slave earning his freedom and returning to Africa in their lifetime?</li>
							<li>Why were the slaves taken from Africa?</li>
							<li>How long did it take to transport slaves fom Africa to America?</li>
							<li>Which country brought more slaves from Africa than any other country?</li>
							<li>How long did it take to transport slaves fom Africa to America?</li>
							<li>show me more queries...</li>
						</ul>
					</f:verbatim>
				</s:expandableBox>
				*/ %>
			</td>
			<td class="section-index-right-column">

				<img src="../images/database-index-title.png" width="350" height="40" border="0" alt="The Database">
				
				<p class="section-index-intro">Over 34,000 individual slaving expeditions between 1527 and 1866 that have been identified and verified to have actually occurred make up the Trans-Atlantic Slave Trade Database.  Records of the voyages have been found in multiple archival sources which are listed in a variable in the dataset.  These records provide details about vessels, enslaved peoples, slave traders and owners, and trading routes.  The database enables users to search for information about a particular voyage or group of voyages and it provides full interactive capability to analyze the data and report results in the form of statistical tables, graphs, maps, or on a timeline.  In addition to information in the database itself, specific voyages are linked to images and to copies of primary sources in the “Resources” section, and “Educational materials” like lesson-plans are linked in turn to relevant voyages in the main database.  Users are encouraged to compare findings from the main database with “Estimates” in the first section.  The latter are somewhat higher because they represent an attempt to take into account the number of slaves on voyages for which information is lacking or not yet included in the main database.</p>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="search.faces">Search the Database</a></div>
				
					<p class="section-index-intro">
					This link takes you to the page for querying the main database.  You can search for information about a specific voyage, a small group of voyages, or a large subset of the data set such as all voyages under the Portuguese flag.  There is a choice between a smaller set of 24 “basic” variables or a larger set of 65 variables.  Each groups variables into the same eight categories: 
					<ol>
						<li>information about the slave ship such as name, place of construction, owners, and national flag under which it sailed</li>
						<li>the outcome of the voyage from different perspectives, and incidents of capture by another ship and of resistance and revolt by slaves</li>
						<li>the itinerary of the voyage from port of departure to places of embarkation of slaves in Africa to places where they were disembarked, most often in the Americas, and finally the port to which the slave ship returned</li>
						<li>dates of each stage of the voyage</li>
						<li>size of the crew at different stages of the voyage and names of the captain(s)</li>
						<li>the number of slaves at different stages of the voyage</li>
						<li>characteristics of the cargo of slaves such as age and gender distribution, mortality, and price when sold</li>
						<li>sources in which evidence of the voyage was found</li>
					</ol>
					The interface of the query section is designed to be as intuitive as possible, but more detailed instructions about how to use it are given in the“Methodology” sub-section to which there is a link below.
					</p>					
					

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Download the Database</a></div>
				
					<p class="section-index-intro">Although fewer than the number of variables in the original CD-ROM version of the database, the variables accessible on-line cover the full range of information available in the data set.   The interactive tools for analysis of the data permit examination and printing of results as tables, graphs, maps, or on a timeline.  However, recognizing that some users will desire the ability to work with variables that have not been included, to recode data in a manner different from that adopted in design of the website, and to use spreadsheets like Excel or statistics software like SPSS to analyze the data, the website provides through this link a form to request delivery by way of the internet of the full set of the 174 data variables in Excel or SPSS format as well as a copy of the codebook.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="#">Methodology</a></div>
				
					<p class="section-index-intro">The database is the product of unprecedented collaboration among scholars from all countries in Europe, Africa, and North and South America involved in the past in the trans-Atlantic slave trade.  It consists of data variables, which incorporate information drawn directly from archival and published variables, and imputed variables inferred by researchers who have developed the database from their knowledge of voyages or calculated directly from data encountered in primary sources.  Essays in the methodology sub-section describe the procedures followed in verifying the integrity and consistency of the data and in imputing new from raw data.   The sub-section also includes the on-line version of the codebook, a list of variables and table showing their availability in different sections of the website, and detailed instructions about how to use the query interface for the data base.</p>

				</div>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>