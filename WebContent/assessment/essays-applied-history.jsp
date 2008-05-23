<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Essays</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-essays.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-applied-history" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>Voyages and Applied history</h1>

					<div class="essay-info">
						<span class="essay-author">Jelmer Vos</span>
						<span class="essay-location">(Emory University)</span>,
						<span class="essay-date">2007</span>
					</div>

					<p>The Transatlantic Slave Trade Database has the potential to
					be of value to those interested in current affairs, as well as to
					scholars involved in strictly academic research. One area where the
					database can be useful is the debate over the payment of
					reparations, monetary or otherwise, to descendants from the
					survivors of the Middle Passage. Other possible fields are those of
					business and family history. In this context, the database has
					played a key part in establishing connections between the
					predecessors of a major international financial institution, the
					Dutch bank ABN AMRO, and African slavery in the Americas. It proved
					especially helpful in tracing the bank’s historical involvement in
					various financial aspects of the slave trade. Employing some of
					database’s basic variables, this recent piece of research provides
					an example of how the database can be effectively used in applied
					history, a joint endeavor in which academically-trained historians
					collaborate to answer questions raised by other professionals.</p>

					<p>In 2005-2006, at the direction of LaSalle Bank Corporation,
					at the time the U.S. subsidiary of ABN AMRO, History Associates
					Incorporated conducted a study of the Dutch bank’s predecessors to
					determine historical connections to African slavery in the United
					States and elsewhere in the Americas. In combination with archival
					research, the Transatlantic Slave Trade Database substantiated
					evidence that some of the bank’s predecessors provided insurance
					for slaving voyages, purchased interest in slaving voyages, or
					supplied credit to clients participating in the slave trade.</p>

					<p>Thus, logbooks at the Rotterdam municipal archives indicated
					that the Rotterdam banking firm of Chabot brokered insurance on
					cargo carried by the ship Vrouw Maria Isabella, commanded by
					Carsten Edebool, in 1774. The database confirmed that this vessel
					set out on a slaving voyage to Africa and Surinam (<a
						href="../database/voyage.faces?voyageId=10830">Voyage ID 10830</a>).
					Moreover, comparing entries from the account books of Mallet Frères
					with vessel names in the database, it was found that this French
					predecessor held interests in several vessels connected with the
					trans-Atlantic slave trade during the late 1700s. Examples of
					slaving vessels in which Mallet had invested included the Infant
					d’Angole (owned by Dubois le jeune), La Dame Cécile (owned by J. R.
					Wirtz & Cie) as well as the Madame, Henri Quatre and Madeleine (all
					owned by A. F. Delaville & Barthelemy). Similarly, the house of
					Mallet as well as another French predecessor, Banque André, had
					business dealings with numerous persons and firms which the
					database identified as outfitters of slaving voyages. For example,
					the Nantes houses of Ambroise Perrotin, Auguste Simon, and Fruchard
					Fils all received substantial loans from Mallet at the time they
					organized slaving voyages to Africa. Meantime, in 1819 the bank of
					André negotiated a loan of FF 15,000 with Vasse-Mancel from Le
					Havre, who five years later organized a slaving voyage to Senegal (<a
						href="../database/voyage.faces?voyageId=34411">Voyage ID 34411</a>),
					while in 1823 they arranged payments on behalf of Philippon & Cie
					from Le Havre for purchases in Liverpool for a slaving voyage to
					Brazil (<a href="../database/voyage.faces?voyageId=34376">Voyage
					ID 34376</a>).</p>

					<p>The study by History Associates made use of only three basic
					variables in the database: vessel, captain, and owner names.
					Nevertheless, the database was essential in establishing important
					connections that existed between the European world of high finance
					and the transatlantic slave trade in the eighteenth and early
					nineteenth centuries.</p>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>
	
</f:view>
</body>
</html>