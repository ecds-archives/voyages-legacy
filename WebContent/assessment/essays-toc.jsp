<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div styleClass="left-menu-box">

	<s:simpleBox>
	
		<t:div styleClass="left-menu-title">
			<h:outputText value="ESSAYS" />
		</t:div>
		
		<s:secondaryMenu>
		
			<s:secondaryMenuItem
				label="Introductory essay"
				menuId="essays-intro" />
			
			<s:secondaryMenuItem
				label="Slavery and Freedom in the Early Modern World"
				menuId="guide" />
			
			<s:secondaryMenuItem
				label="The English, the Dutch, and Transoceanic Migration"
				menuId="demo" />
			
			<s:secondaryMenuItem
				label="Europeans and African Slavery in the Americas"
				menuId="faq" />
			
			<s:secondaryMenuItem
				label="Gender and Slavery in the Modern Atlantic World"
				menuId="methodology" />
		
			<s:secondaryMenuItem
				label="Africa and Europe in the Early Modern Era"
				menuId="variables" />
		
			<s:secondaryMenuItem
				label="The African Impact on the Tranatlatic Slave Trade"
				menuId="sources-documentary" />
			
			<s:secondaryMenuItem
				label="# The English Plantation Americas in Comparative Perspective"
				menuId="sources-newspapers" />
			
			<s:secondaryMenuItem
				label="Ethnicity in the Early Modern Atlatic World"
				menuId="sources-published" />
			
		</s:secondaryMenu>
		
	</s:simpleBox>

</t:div>