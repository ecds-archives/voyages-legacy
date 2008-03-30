<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" styleClass="top-bar">

	<t:htmlTag value="table" styleClass="top-bar">
	<t:htmlTag value="tr">
	
		<t:htmlTag value="td" styleClass="top-bar-logo">
			<h:outputLink value="#{MainMenuBean.indexUrl}">
				<h:graphicImage
					value="/images/main-menu/logo.png"
					width="260" height="60" />
		</h:outputLink>
		</t:htmlTag>
		
		<t:htmlTag value="td" styleClass="top-bar-menu">
			<s:mainMenuBar
				menuItems="#{MainMenuBean.mainMenu}"
				activeSectionId="#{MainMenuBean.activeSectionId}" />
		</t:htmlTag>
		
	</t:htmlTag>
	</t:htmlTag>
	
</t:htmlTag>
