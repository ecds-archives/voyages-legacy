<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" styleClass="images-nav-path" >
	<h:commandLink value="Images homepage" action="images" />
	<h:outputText value="/" />
	<h:outputText value="Found images" />
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-query-title" >
	<h:outputText value="Found images" />
</t:htmlTag>

<%
// <t:htmlTag value="h1"><h:outputText value="#{res.images_query_title}" /></t:htmlTag>
// <t:htmlTag value="div" styleClass="images-query">
// 	<h:outputText value="#{ImagesBean.queryTitle}" />
// 	(<h:commandLink value="#{res.images_back}" action="#{ImagesBean.back}" styleClass="link-images"/>)
// </t:htmlTag>
%>

<t:htmlTag value="div" styleClass="images-gallery-query">

	<s:pictures
		images="#{ImagesBean.galleryImages}"
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100"
		action="#{ImagesBean.gotoDetailFromGallery}" 
		selectedImageId="#{ImagesBean.imageId}" />

</t:htmlTag>