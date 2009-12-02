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
								label="The Trans-Atlantic Slave Trade SPSS Database (expanded data set)"
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
					
					<h2>The Trans-Atlantic Slave Trade SPSS Database (expanded data set)</h2>
						
					<p>The 2008 version of the Trans-Atlantic Slave Trade Database contains 8,374 voyages 
					added since the CD-Rom was published in 1999 and additional information on 19,320 voyages. 
					The expanded data set has 276 variables, compared with 99 in the Voyages Database available 
					online. Users interested in working with this larger data set can download it in a file 
					formatted for use with SPSS software. A variable in this dataset (EVGREEN) identifies 
					voyages on the CD-ROM version published in 1999. Because some users may find it useful 
					to view data as it existed in 1999, the CD-ROM version can also be selected for download. 
					A codebook describing all variable names, variable labels, and values of the expanded 
					dataset can be downloaded as a separate PDF document. With only a few exceptions, it 
					retains variable names in the original 1999 CD-ROM version.</p>
				
					<h3>Downloads:</h3>
					<ul>
						<li>Expanded dataset (2008, SPSS format): <a href="<%=dlUrl%>/tastdb-exp-2008.sav">tastdb-exp-2008.sav</a></li>
						<li>Original CD-ROM dataset (1999, SPSS format): <a href="<%=dlUrl%>/tastdb-1999.sav">tastdb-1999.sav</a></li>
						<li>Codebook: <a href="<%=dlUrl%>/Codebook-SPSS2008.pdf" target="_blank">SPSS codebook</a></li>
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
						<li>2008, SPSS: <a href="<%=dlUrl%>/tastdb-2008.sav">tastdb-2008.sav</a></li>
						<li>2008, comma-delimited: <a href="<%=dlUrl%>/tastdb-2008.csv">tastdb-2008.csv</a></li>
						<li>2008, dBase: <a href="<%=dlUrl%>/tastdb-2008.dbf">tastdb-2008.dbf</a></li>
					</ul>
						
					<a name="estimates"></a> 
						
					<h2>Estimates spreadsheet</h2>
					
					<p>The Estimates section uses data from the expanded Trans-Atlantic Slave Trade Database dataset, 
					aggregated by regions of embarkation and disembarkation, but also incorporates published series 
					of embarkations and disembarkations for particular regions when these data provide more accurate 
					estimates than the database itself. Users interested in exploring the complex methodology involved 
					in generating the estimates, or in testing the extent to which alternative assumptions lower or 
					augment estimates of the full volume of the slave trade (in its entirety or for a national carrier 
					or period of time), can download below an Excel-formatted copy of the spreadsheet and a PDF version
					of the essay detailing the methodology used.</p>
					
					<h3>Download:</h3>
					<ul>
						<li>2009 Estimates, Excel: <% /* <a href="replaceWithdlUrlVar/estimates-2009.xls">estimates-2009.xls</a> */ %> <span style="font-style: italic">will be available soon</span></li>
						<li>2009 Methodology essay, PDF: <a href="<%=dlUrl%>/estimates-method.pdf">estimates-method.pdf</a></li>
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