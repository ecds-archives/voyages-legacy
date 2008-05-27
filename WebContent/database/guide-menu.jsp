<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div styleClass="left-menu-box">

	<s:simpleBox>
	
		<t:div styleClass="left-menu-title">
			<h:outputText value="UNDERSTANDING THE DATABASE" />
		</t:div>
		
		<s:secondaryMenu>
		
			<s:secondaryMenuItem
				label="Guide"
				menuId="start"
				href="guide/VoyagesGuide.pdf"/>			
			
			<s:secondaryMenuItem
				label="Methodology"
				menuId="methodology"
				href="methodology.faces" />
		
			<s:secondaryMenuItem
				label="Variable List"
				menuId="variables"
				href="variables.faces" />
		
			<s:secondaryMenuItem
				label="Sources"
				menuId="sources"
				href="sources.faces?type=documentary">
			
				<s:secondaryMenuItem
					label="Documentary Sources"
					menuId="sources-documentary"
					href="sources.faces?type=documentary" />
				
				<s:secondaryMenuItem
					label="Newspapers"
					menuId="sources-newspapers"
					href="sources.faces?type=newspapers"  />
				
				<s:secondaryMenuItem
					label="Published Sources"
					menuId="sources-published"
					href="sources.faces?type=published" />
				
				<s:secondaryMenuItem
					label="Unpublished Secondary Sources"
					menuId="sources-unpublished"
					href="sources.faces?type=unpublished" />
				
				<s:secondaryMenuItem
					label="Private Notes and Collections"
					menuId="sources-private"
					href="sources.faces?type=private" />
			
			</s:secondaryMenuItem>
		
		</s:secondaryMenu>
		
	</s:simpleBox>

</t:div>