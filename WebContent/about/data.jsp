<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Contributors of data</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-team.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="about">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="./index.faces"><h:outputText value="About the Project" /></h:outputLink>
		<h:outputText value="Contributors of data" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>Contributors of data</h1>					
				
					<p>The following have added data to the Voyages Database since the publication of 
					the 1999 CD-ROM, <font style="font-style: italic;">The Trans-Atlantic Slave Trade: A Database on CD-ROM:</font> </p>

					
					<ul>
						<li>Rosanne Adderley</li>
						<li>Richard A Arzill</li>
						<li>Joseph Avitable</li>
						<li>James G. Basker</li>
						<li>Stephen D. Behrendt</li>
						<li>Franz Binder</li>
						<li>Richard Birkett</li>
						<li>Ernst van den Boogart</li>
						<li>Alex Borucki</li>
						<li>Angela J Campbell</li>
						<li>James Campbell</li>						
						<li>Mariana Pinho Candido</li>
						<li>José Capela</li>
						<li>Comité de Liaison et d'Application des Sources Historiques (Saint-Barthélemy), <a href="http://www.c-l-a-s-h.info">http://www.c-l-a-s-h.info</a></li>
						<li>John C. Coombs</li>
						<li>Jose Curto</li>
						<li>Daniel B. Domingues da Silva</li>
						<li>David Eltis</li>
						<li>Roquinaldo Ferreira</li>
						<li>Manolo Florentino</li>
						<li>Charles Foy</li>
						<li>David Geggus</li>
						<li>Oscar Grandío Moráguez</li>
						<li>Jerome Handler</li>
						<li>Robert Harms</li>
						<li>Candice Harrison</li>
						<li>Henk den Heijer</li>
						<li>Willem Klooster</li>
						<li>Ruud Koopman</li>
						<li>Robin Law</li>						
						<li>Jean-Pierre Le Glaunec</li>						
						<li>Pedro Machado</li>
						<li>Antonio de Almeida Mendes</li>
						<li>Philp Misevich</li>
						<li>Greg O’Malley</li>
						<li>Cláudia Paixão</li>
						<li>Manuel Barcia Paz</li>
						<li>Janaína Perrayon Lopes</li>
						<li>David Pettee</li>
						<li>Johannes Postma</li>
						<li>Fabricio Prado</li>
						<li>James Pritchard</li>
						<li>Vanessa Ramos</li>
						<li>Alexandre Vieira Ribeiro</li>
						<li>David Richardson</li>
						<li>Justin Roberts</li>
						<li>Jelmer Vos</li>
						<li>Lorena Walsh</li>
						<li>Andrea Weindl</li>
						<li>Rik van Welie</li>
						<li>David Wheat</li>
					</ul>

						

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