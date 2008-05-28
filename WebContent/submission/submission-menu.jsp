<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div styleClass="left-menu-box">

	<s:simpleBox>
	
		<t:div styleClass="left-menu-title">
			<h:outputText value="Contribute" />
		</t:div>
		
		<s:secondaryMenu>
			<s:secondaryMenuItem
				label="Guidelines for Contributors"
				menuId="guidelines"
				href="guidelines.faces"/>			
			
			<s:secondaryMenuItem
				label="Sign in"
				menuId="signin"
				href="submission-login.faces" />
		</s:secondaryMenu>
		
	</s:simpleBox>
</t:div>
