<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" styleClass="top-bar">

	<t:htmlTag value="table" styleClass="top-bar">
	<t:htmlTag value="tr">
	
		<t:htmlTag value="td">
			<h:outputLink value="index.faces">
				<h:graphicImage width="260" height="60" value="./images/main-menu/logo.png" />
			</h:outputLink>
		</t:htmlTag>
		
		<t:htmlTag value="td">
			<s:mainMenuBar
				menuItems="#{MainMenuBean.mainMenu}"
				activeSectionId="#{MainMenuBean.activeSectionId}" />
		</t:htmlTag>
		
	</t:htmlTag>
	</t:htmlTag>
	
</t:htmlTag>
