<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="table" styleClass="top-bar">
<t:htmlTag value="tr">

	<t:htmlTag value="td" styleClass="top-bar-logo"><t:graphicImage url="../images/help-logo.png" width="240" height="60" border="0" /></t:htmlTag>

	<t:htmlTag value="td" styleClass="top-bar-menu">
		<t:div styleClass="top-bar-menu">
			<s:panelTabSet id="helpMenu" selectedSectionId="#{HelpMenuBean.activeSection}">
				<s:panelTab title="#{res.help_box_menu_faq}" sectionId="faq" href="faq.faces" />
				<s:panelTab title="#{res.help_box_menu_demos}" sectionId="demos" href="demos.faces" />
				<s:panelTab title="#{res.help_box_menu_sitemap}" sectionId="sitemap" href="sitemap.faces" />
				<s:panelTab title="#{res.help_box_menu_glossary}" sectionId="glossary" href="glossary.faces" />
			</s:panelTabSet>
		</t:div>
	</t:htmlTag>

</t:htmlTag>
</t:htmlTag>