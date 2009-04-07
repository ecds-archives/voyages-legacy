<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:dataList
	value="#{ImagesBean.homepageGallerySamples}"
	var="gallery"
	layout="simple">

	<t:htmlTag value="div" styleClass="images-category">
		<t:htmlTag value="div" styleClass="images-category-name">
			<h:outputText value="#{gallery.categoryName}" />
		</t:htmlTag>
		<t:htmlTag value="div" styleClass="images-category-link">
			<h:commandLink
				value="See all images ..."
				action="#{ImagesBean.openCategoryFromHomepage}"
				styleClass="link-images"/>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="div" styleClass="images-category-sample">
	
		<s:gallery
			images="#{gallery.images}" 
			showLabels="true"
			columnsCount="4"
			thumbnailHeight="100" 
			thumbnailWidth="100" 
			action="#{ImagesBean.openDetailFromHomepage}" 
			selectedImageId="#{ImagesBean.imageId}"/>
	
	</t:htmlTag>

</t:dataList>
