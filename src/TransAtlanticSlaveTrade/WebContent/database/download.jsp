<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<%
String dlUrl = AppConfig.getConfiguration().getString(AppConfig.DOWNLOADS_URL);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Downloads</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">

	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Downloads" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<t:div styleClass="left-menu-box">
				
					<s:simpleBox>
					
						<t:div styleClass="left-menu-title">
							<h:outputText value="Downloads" />
						</t:div>
						
						<s:secondaryMenu>
						
							<s:secondaryMenuItem
								label="The 2010 Version of the Trans-Atlantic Slave Trade Database (expanded data set)"
								menuId="extended"
								href="#extended"/>			
							
							<s:secondaryMenuItem
								label="The Trans-Atlantic Slave Trade Database (Voyages data set)"
								menuId="voyages"
								href="#voyages"/>
								
							<s:secondaryMenuItem
								label="Estimates spreadsheet"
								menuId="estimates"
								href="#estimates"/>

						</s:secondaryMenu>
						
					</s:simpleBox>
				</t:div>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>

					<h1>DOWNLOADS</h1>
					
					<a name="extended"></a> 
					
					<h2>The 2010 Version of the Trans-Atlantic Slave Trade Database (expanded data set)</h2>
						
					<p>TThe 2010 version of the transatlantic slave trade database contains 34,948 voyages 
					compared to 34,940 in 2008 (and 27,233 in the 1999 version of the database that appeared 
					on CD-ROM). However, since 2008, several hundred corrections have been made and additional 
					information added. Thus 86 of the 2008 voyages have been deleted either because we found 
					they had been entered twice, or because we discovered that a voyage was not involved in 
					the transatlantic slave trade. Voyage id 16772, the Pye, Captain Adam, turned out to have 
					carried slaves from Jamaica to the Chesapeake, but obtained its captives in Jamaica, not 
					Africa. Offsetting the deletions are 94 voyages added on the basis of new information. 
					Further many voyages that are common to both 2008 and 2010 versions of the database contain 
					information that was not available in 2008 (see table 1 of “Understanding the Database”). 
					The 2010 version has 276 variables, compared with 99 in the Voyages Database available 
					online. Users interested in working with this larger data set can download it in a file 
					formatted for use with SPSS software. A variable in this dataset (EVGREEN) identifies 
					voyages on the CD-ROM version published in 1999. Because some users may find it useful 
					to view data as it existed in 1999, the CD-ROM version can also be selected for download. 
					A codebook describing all variable names, variable labels, and values of the expanded 
					dataset can be downloaded as a separate PDF document. With only a few exceptions, it 
					retains variable names in the original 1999 CD-ROM version. The 2008 version of the database 
					may also be downloaded.</p>
				
					<h3>Downloads:</h3>
					<ul>
						<li>Expanded dataset (2010, SPSS format): <a href="<%=dlUrl%>/tastdb-exp-2010.sav">tastdb-exp-2010.sav</a></li>
						<li>Expanded dataset (2008, SPSS format): <a href="<%=dlUrl%>/tastdb-exp-2008.sav">tastdb-exp-2008.sav</a></li>
						<li>Original CD-ROM dataset (1999, SPSS format): <a href="<%=dlUrl%>/tastdb-1999.sav">tastdb-1999.sav</a></li>
						<li>Codebook: <a href="<%=dlUrl%>/Codebook2010.pdf" target="_blank">SPSS codebook</a></li>
					</ul>		
					
					<a name="voyages"></a> 
					
					<h2>The Trans-Atlantic Slave Trade Database (Voyages data set)</h2>
					
					<p>This data set of the Trans-Atlantic Slave Trade Database contains the 99 variables 
					available through the Voyages Database on this website. It is made available in three 
					formats: SPSS (.sav), comma delimited (.csv), and dBase (.dbf). Users interested in 
					working with this data set with their preferred software may select an edition year 
					and file format below. </p>
					
					<h3>Downloads:</h3>
					<ul>
						<li>2010, SPSS: <a href="<%=dlUrl%>/tastdb-2010.sav">tastdb-2010.sav</a></li>
						<li>2010, comma-delimited: <a href="<%=dlUrl%>/tastdb-2010.csv">tastdb-2010.csv</a></li>
						<li>2010, dBase: <a href="<%=dlUrl%>/tastdb-2010.dbf">tastdb-2010.dbf</a></li>
					</ul>
						
					<a name="estimates"></a> 
						
					<h2>Estimates spreadsheet</h2>
					
					<p>The Estimates section uses data from the expanded Trans-Atlantic Slave Trade Database dataset, 
					aggregated by regions of embarkation and disembarkation, but also incorporates published series 
					of embarkations and disembarkations for particular regions when these data provide more accurate 
					estimates than the database itself. Users interested in exploring the complex methodology involved 
					in generating the estimates, or in testing the extent to which alternative assumptions lower or 
					augment estimates of the full volume of the slave trade (in its entirety or for a national carrier 
					or period of time), can download below an Excel-formatted copy of the spreadsheet estimating the 
					full volume of the slave trade by national carrier and year of arrival, the SPSS file applying 
					estimates of volume in the spreadsheet to data on exports and imports aggregated by national carrier, 
					year of arrival, and the route of the voyage from region of embarkation to region of disembarkation, 
					and a PDF version of the essay detailing the methodology used.</p>
					
					<h3>Download:</h3>
					<ul>
						<li>Estimates spreadsheet: <a href="<%=dlUrl%>/2010-estimates-excel.xlsx">2010 estimates-excel.xlsx</a></li>
						<li>Estimates SPSS file: <a href="<%=dlUrl%>/2010-estimates-SPSS.sav">2010 estimates-SPSS.sav</a></li>
						<li>Methodology essay: <a href="<%=dlUrl%>/2010estimates-method.pdf">2010 estimates-method.pdf</a></li>
					</ul>
		
					Questions about downloading or using these datasets? Please contact 
					<a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">
					&#118;&#111;&#121;&#97;&#103;&#101;s&#64;&#101;mo&#114;y&#46;&#101;&#100;&#117;</a>.

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