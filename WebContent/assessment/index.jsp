<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Assessing the Slave Trade</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-expandable-box.css" rel="stylesheet" type="text/css">
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="../index.faces"><img src="../new-images/logo.png" border="0" width="300" height="127"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="assessment" /></td>
		</tr>
		</table>
	</div>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="section-index-layout">
		<tr>
			<td class="section-index-left-column">

				<s:expandableBox text="Quick Start">
					<f:verbatim escape="false">
						<ul class="box">
							<li><a href="essays-intro.faces">Click here</a> to go directly to the “Introductory essay” by David Eltis. </li>
							<li><a href="estimates.faces">Click here</a> to view a table showing the estimates of the total number of captives exported from Africa to the Americas by the major slave-trading powers from the beginning of the sixteenth to the middle of the nineteenth century. </li>
						</ul>
					</f:verbatim>
				</s:expandableBox>
				
				<br>
				<% /*
				<s:expandableBox text="Top FAQ">
					<f:verbatim escape="false">
						<ul class="box">
							<li>Which European country was the first to engage in the slave trade in Central Africa?</li>
							<li>Why were the slaves taken from Africa?</li>
							<li>Where were they taken to?</li>
							<li>Who sold the slaves to the European slavers?</li>
							<li>Has there ever been evidence of a slave earning his freedom and returning to Africa in their lifetime?</li>
							<li>Which country brought more slaves from Africa than any other country?</li>
							<li>What was the first country to get slaves from Africa?</li>
							<li>How long did it take to transport slaves fom Africa to America?</li>
							<li>Why did European interest in Africa turn from the slave trade to colonization?</li>
							<li>How did the atlantic slave trade effect the economy of Europe, Africa, and America?</li>
							<li>show me more questions ...</li>
						</ul>
					</f:verbatim>
				</s:expandableBox>
				*/ %>

			</td>
			<td class="section-index-right-column">

				<img src="../images/assessment-index-title.png" width="350" height="40" border="0" alt="Assessing the Slave Trade">
				
				<p class="section-index-intro">
				The Trans-Atlantic Slave Trade Database in the second section of this website contains all slaving expeditions documented up until now.  It consists of information about 34,000 voyages embarking over 10,000,000 Africans and delivering alive 8,800,000 to markets for slave labor in the New World from North America to South-east Brazil and the Rio de Plata estuary.    Nevertheless, the main database cannot claim to be complete.  Records of many voyages have disappeared, in some cases irretrievably, while other documents remain to be discovered in public and private archives.  Thus the first section of the website provides “Estimates” of how large the slave trade actually was.  
				</p>

				<p class="section-index-intro">
				The estimates are derived for the most part from the main database, but also incorporate findings from recent scholarship on the slave trade, such as the series developed by António de Almeida Mendes showing arrivals of slaves in the Spanish Americas in the sixteenth and early seventeenth centuries and the comprehensive studies by Per Hernæs of the Danish slave trade from West Africa.  Careful evaluation has been made of gaps in coverage of the slave trade of each national carrier.  Taking relative completeness of coverage into account, the data provided in the “Estimates” section adjusts upwards the number of slaves exported and imported each year in the main database.  It may be considered the most reliable appraisal available today of the actual magnitude of the trans-Atlantic slave trade.  Altogether, the estimates are about 20 percent higher than unadjusted numbers in the main database.  They raise the final totals to over 12,500,000 Africans forced to undertake the Middle Passage and nearly 10,800,000 who completed it, the largest forced migration in modern history.</p>
				
				<p class="section-index-intro">
				The quality of data, and therefore of estimates derived from them, varies considerably over time and space.  To facilitate the user’s ability to specify periods of analysis, estimates are provided of the annual number of slaves shipped by a national carrier from 8 regions in Africa to 7 larger regions and 34 more precisely defined regions in the Americas.   In addition, the dataset also shows the smaller number of captives shipped instead to Europe or disembarked in Africa and Atlantic islands, mostly in the nineteenth century after British abolition of the international slave trade and deployment of naval squadrons to enforce this measure.  For the eighteenth century, when the slave trade reached its apogee, estimates for individual years are based on a large enough number of voyages to capture annual fluctuations.  For the sixteenth century, on the other hand, patterns observed 5 and 25 year periods are more meaningful that statistics for single years.</p>
				
				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="essays-intro.faces">Essays</a></div>
				
					<p class="section-index-intro">
					This sub-section contains a short “Introductory essay” by David Eltis summarizing his description and interpretation of the volume and structure of the trans-Atlantic slave trade from inception to suppression.  It updates an earlier assessment based on the version of the Trans-Atlantic Slave Trade Database published as a CD-ROM in 1998.  The reader will note that the figures given by Eltis correspond to those which can be calculated in the “Estimates” section of the website.  The “Essays” sub-section also contains a bibliography of published and unpublished scholarly studies citing the Trans-Atlantic Slave Trade Database as a major source, with links to the complete text of several which illustrate how to observe patterns in the slave trade using the estimates.</p>

				</div>

				<div class="section-index-intro-icon section-index-intro-blank-icon">
				
					<div class="section-index-intro-icon-title"><a href="estimates.faces">Estimates</a></div>
				
					<p class="section-index-intro">
					Here you will find a table showing, by quarter-century, how many slaves are estimated to have been exported by the seven major slave-trading nations between 1501 and 1867.  It is generated by the default parameters in the frame to its left.  Users can focus on a particular part of the trans-Atlantic slave trade by specifying a shorter time frame, one or several of the national carriers, and a region or group of regions where slaves were embarked and/or disembarked.  The current query box indicates selections as they are made.  Presentation of results is controlled by selecting parameters in boxes for rows and columns and whether you wish to see the number of slaves exported or imported or both.  Finally, tabs at the top of the default “Table” allow for choosing another way of representing results for voyages selected by criteria to the left – as a listing of cases, or on a map, or in a timeline.</p>

				</div>

			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>