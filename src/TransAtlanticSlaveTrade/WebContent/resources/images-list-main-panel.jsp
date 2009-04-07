<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" styleClass="images-query-title" >
	<h:outputText value="#{ImagesBean.listTitle}" />
</t:htmlTag>

<t:htmlTag value="div" styleClass="image-navigation">
	<t:htmlTag value="table" styleClass="image-navigation">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
				<h:commandLink value="Back to start" action="images" styleClass="link-back-to-list"/>
			</t:htmlTag>				
			<t:htmlTag value="td" style="padding: 0px">
				<h:outputText value="#{ImagesBean.galleryImagesCount}" />
			</t:htmlTag>				
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-gallery-query">

	<s:gallery
		images="#{ImagesBean.galleryImages}"
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100"
		action="#{ImagesBean.gotoDetailFromGallery}" 
		selectedImageId="#{ImagesBean.imageId}" />

</t:htmlTag>