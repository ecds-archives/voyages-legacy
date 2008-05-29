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
					
					<p style="text-align: center;">Appendix</p>
					<p style="text-align: center;">Derivation of Estimated number of Captives Carried on Vessels in the Voyages
					Database for which such Information cannot be obtained from the Sources </p>
					
					<center>
					<table border="1">
					<tr>
						<td>Voyage groupings for estimating imputed slaves</td>
						<td>Average number of captives embarked</td>

						<td>number</td>
						<td>Sd</td>
						<td>Average number of captives disembarked</td>
						<td>number</td>
						<td>sd</td>
					</tr>

					<tr>
						<td>Spanish, America, pre-1626</td>
						<td class="number">200.1</td>
						<td class="number">202</td>
						<td class="number">91.0</td>
						<td class="number">166.7</td>

						<td class="number">202</td>
						<td class="number">91.0</td>
					</tr>
					<tr>
						<td>Spanish, America, 1626-41</td>
						<td class="number">194.0</td>
						<td class="number">6</td>

						<td class="number">148.6</td>
						<td class="number">152.9</td>
						<td class="number">148</td>
						<td class="number">98.4</td>
					</tr>
					<tr>	
						<td>Steamer</td>

						<td class="number">897.3</td>
						<td class="number">6</td>
						<td class="number">259.8</td>
						<td class="number">1004.5</td>
						<td class="number">17</td>
						<td class="number">399.9</td>

					</tr>
					<tr>		
						<td>pre1716 French</td>
						<td class="number">337.7</td>
						<td class="number">77</td>
						<td class="number">166.6</td>
						<td class="number">241.5</td>

						<td class="number">103</td>
						<td class="number">134.0</td>
					</tr>
					<tr>		
						<td>Sumaca, pre-1751</td>
						<td class="number">211.5</td>
						<td class="number">14</td>

						<td class="number">68.1</td>
						<td class="number">229.6</td>
						<td class="number">25</td>
						<td class="number">170.4</td>
					</tr>
					<tr>	
						<td>Steamer</td>

						<td class="number">897.3</td>
						<td class="number">6</td>
						<td class="number">259.8</td>
						<td class="number">1004.5</td>
						<td class="number">17</td>
						<td class="number">399.9</td>

					</tr>
					</table>	
					</center>
					
								<%/*
					<h:dataTable value="#{MethodAppendixBean.datalist}" var="m" style="border: 1px solid #CCCCCC;">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Voyage groupings for estimating imputed slaves"/>
							</f:facet>
 							<h:outputText value="#{m.group}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Average number of captives embarked"/>
							</f:facet>
 							<h:outputText value="#{m.ave_em}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="number"/>
							</f:facet>
							<h:outputText value="#{m.number_em}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Sd"/>
							</f:facet>
							<h:outputText value="#{m.sd_em}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Average number of captives disembarked"/>
							</f:facet>
							<h:outputText value="#{m.ave_disem}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="number"/>
							</f:facet>
							<h:outputText value="#{m.number_disem}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Sd"/>
							</f:facet>
							<h:outputText value="#{m.sd_disem}"/>		
						</h:column>						
					</h:dataTable>
					
				*/ %>		
			
					<br>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-19.faces">Resistance and Price of Slaves</a>
						</td>
						<td class="method-next">
							<a href="methodology-21.faces">Note</a>
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