<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Introductory Maps</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-expandable-box.css" rel="stylesheet" type="text/css">

	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-essays.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/newMaps.css" rel="stylesheet" type="text/css">

	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/wz_dragdrop.js" language="javascript" type="text/javascript"></script>
	
	<script language="javascript" type="text/javascript">
	function popitup(url) {
		newwindow=window.open(url,'name','height=768,width=1024,resizable, scrollbars, location=0');
		if (window.focus) {newwindow.focus()}
		return false;
	}
	</script>

</head>
<body>
<a name="top"></a>

<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home page" /></h:outputLink>
		<h:outputLink value="index.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Introductory Maps" />
	</s:siteHeader>
	
		<div id="content" style="padding-left: 20px">

<!-- last update: -->


<!-- first map -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 1: Overview of the slave trade out of Africa, 1500-1900" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3" >
		Captive Africans followed many routes from their homelands to other parts of the world. 
		The map shows the transatlantic movement of these captives in comparative perspective for the centuries 
		since 1500 only. Estimates of the ocean-borne trade are more robust than are those for the trans-Saharan, 
		Red sea and Persian Gulf routes, but it is thought that for the period from the end of the Roman Empire 
		to 1900 about the same number of captives crossed the Atlantic as left Africa by all other routes 
		combined.
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/1_Slave_Trade_Overview_preview.jsp" onclick="return popitup('../newMaps/1_Slave_Trade_Overview_preview.jsp')">
<img src = "../newMaps/1_Slave_Trade_Overview.gif" width="200px" border=0></img>

</a>
</td>
</tr>
</table>
</t:htmlTag>
	
<!-- the second map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 2: Migration of sugar cultivation from Asia into the Atlantic" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
		Sugar cultivation began in the Pacific in the pre-Christian era and gradually 
		spread to the eastern Mediterranean, the Gulf of Guinea, then to Brazil, before entering 
		the Caribbean in the mid-seventeenth century. Eighty percent of all captives carried from 
		Africa were taken to sugar-growing areas. 
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/2_Migration_Sugar_Cultivation_preview.jsp" onClick = "return popitup('../newMaps/2_Migration_Sugar_Cultivation_preview.jsp')">
<img src = "../newMaps/2_Migration_Sugar_Cultivation.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>

<!-- the third map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 3: Old World slave trade routes in the Atlantic before 1759" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
		Before the Atlantic slave trade began and for two centuries thereafter, some African captives 
		were taken to Europe as well as to the Atlantic islands and between African ports. It is hard to 
		get precise estimates of these flows, but they were certainly much smaller than the transatlantic 
		traffic. Many of the captives involved in this traffic were subsequently carried to sugar 
		plantations in the Old World.
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/3_Old_World_SlaveTrade_preview.jsp" onclick="return popitup('../newMaps/3_Old_World_SlaveTrade_preview.jsp')">
<img src = "../newMaps/3_Old_World_SlaveTrade.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>



<!-- the fourth map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 4: Wind and ocean currents of the Atlantic basins" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
	In the age of sail, winds and ocean currents shaped the direction of the transatlantic slave trade, 
	effectively creating two separate slave-trading systems – one in the north with voyages originating 
	in Europe and North America, the other in the south with voyages originating in Brazil.
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/4_Wind_currents_preview.jsp" onClick = "return popitup('../newMaps/4_Wind_currents_preview.jsp')">
<img src = "../newMaps/4_Wind_currents.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>



<!-- the fifth map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 5: Major regions and ports involved in the transatlantic slave trade, all years" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
	Few commercial centers in the Atlantic world were untouched by the slave 
	trade, and all the major ports had strong connections with the traffic. 
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/5_Atlantic_Basin_preview.jsp" onClick = "return popitup('../newMaps/5_Atlantic_Basin_preview.jsp')">
<img src = "../newMaps/5_Atlantic_Basin.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>


<!-- the sixth map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 6: Ports in the Atlantic where slave voyages were organized, three periods" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
	Slave voyages were organized and left from all major Atlantic ports at some point over the 
	nearly four centuries of the transatlantic slave trade. Nevertheless, vessels from the largest 
	seven ports, Rio de Janeiro, Bahia, Liverpool, London, Nantes, Bristol, and Pernambuco carried 
	off almost three-quarters of all captives removed from Africa via the Atlantic Ocean. There was 
	a major shift in the organization of slaving voyages first from the Iberian peninsular to Northern 
	Europe, and then later back again to ports in southern Europe. A similar, but less pronounced shift 
	may be observed in the Americas from South to North and then back again.  <br/> 
	<br></br>
	<table border="1" style="margin-left: 20px">
	<tr>
	<td width="250px">Total documented embarkations:<br/>8,973,701 captives	
	</td>
	<td width="250px">Percent of estimated embarkations:<br>72.1
	</td>
	</tr>
	</table>
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/6_Europe_Overview_preview.jsp" onClick = "return popitup('../newMaps/6_Europe_Overview_preview.jsp')">
<img src = "../newMaps/6_Europe_Overview.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>


<!-- the seventh map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 7: Major coastal regions from which captives left Africa, all years" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
	The limits of the regions shown here are “Senegambia,” anywhere north of the Rio Nunez. 
	Sierra Leone region comprises the Rio Nunez to just short of Cape Mount. The Windward Coast 
	is defined as Cape Mount south-east to and including the Assini river. The Gold Coast runs east 
	of here up to and including the Volta River. Bight of Benin covers the Rio Volta to Rio Nun, and 
	the Bight of Biafra, east of the Nun to Cape Lopez inclusive. West-central Africa is defined as 
	the rest of the western coast of the continent south of this point, and south-eastern Africa 
	anywhere from and to the north and east of the Cape of Good Hope. West-Central Africa was the 
	largest regional departure point for captives through most the slave trade era. Regions closer 
	to the Americas and Europe generated a relatively small share of the total carried across the 
	Atlantic. Voyage length was determined as much by wind and ocean currents shown in Map 4 as by 
	relative proximity of ports of embarkation and disembarkation.    <br/>
	<br></br>
	<table border="1" style="margin-left: 20px">
	<tr>
	<td width="250px">Total documented embarkations:<br/>7,878,500 captives	
	</td>
	<td width="250px">Percent of estimated embarkations:<br>63.3
	</td>
	</tr>
	</table>
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/7_Africa_Overview_preview.jsp" onClick = "return popitup('../newMaps/7_Africa_Overview_preview.jsp')">
<img src = "../newMaps/7_Africa_Overview.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>



<!-- the eighth map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 8: Major regions where captives disembarked, all years" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
	The Caribbean and South America received 95 percent of the slaves arriving in the Americas. 
	Some captives disembarked in Africa rather than the Americas because their transatlantic 
	voyage was diverted as a result of a slave rebellion or, during the era of suppression, 
	because of capture by patrolling naval cruisers.  Less than 4 percent disembarked in North 
	America, and only just over 10,000 in Europe. <br/>
	<br></br>
	<table border="1" style="margin-left: 20px">
	<tr>
	<td width="250px">Total documented disembarkations:<br/>9,371,001 captives	
	</td>
	<td width="280px">Percent of estimated disembarkations:<br>88.5
	</td>
	</tr>
	</table>
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/8_Americas_Overview_preview.jsp" onClick = "return popitup('../newMaps/8_Americas_Overview_preview.jsp')">
<img src = "../newMaps/8_Americas_Overview.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>


<!-- the ninth map: -->
<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
<div style="padding-top: 20px; ">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Map 9: Volume and direction of the transatlantic slave trade from all African to all American regions" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
	<a href = "#top">Return to the top</a>	
	</t:htmlTag>
</div>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

<table border="0" width="940px">
<tr>
<td align="left" width="530px" valign="top">	
<font size="3">
	This map summarizes and combines the many different paths by which captives left Africa and reached the Americas. 
	While there were strong connections between particular embarkation and disembarkation regions, it was also the case 
	that captives from any of the major regions of Africa could disembark in almost any of the major regions of the Americas. 
	Even captives leaving Southeast Africa, the region most remote from the Americas, could disembark in mainland North America, 
	as well as the Caribbean and South America. The data in this map are based on estimates of the total slave trade rather than 
	documented departures and arrivals.
</font>
</td>
<td width="300" align="right">
<a href ="../newMaps/8A_Transatlantic_preview.jsp" onClick = "return popitup('../newMaps/8A_Transatlantic_preview.jsp')">
<img src = "../newMaps/8A_Transatlantic.gif" width="200px" border=0></img>
</a>
</td>
</tr>
</table>
</t:htmlTag>
</div>
	


</h:form>
	
</f:view>


<script type="text/javascript">
<!--

SET_DHTML("testmap");

//-->
</script>
</body>
</html>