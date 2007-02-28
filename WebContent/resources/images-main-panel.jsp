<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="Presentation" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="Show all images ..." action="#{NewImagesBean.seePresentation}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{NewImagesBean.samplePresentation}" 
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{NewImagesBean.detailRequested}" 
		selectedImageId="#{NewImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_vessels}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_vessels_link}" action="#{NewImagesBean.seeVessels}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{NewImagesBean.sampleVessels}" 
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{NewImagesBean.detailRequested}" 
		selectedImageId="#{NewImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_slaves}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_slaves_link}" action="#{NewImagesBean.seeSlaves}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{NewImagesBean.sampleSlaves}" 
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{NewImagesBean.detailRequested}" 
		selectedImageId="#{NewImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_slavers}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_slavers_link}" action="#{NewImagesBean.seeSlavers}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{NewImagesBean.sampleSlavers}" 
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{NewImagesBean.detailRequested}" 
		selectedImageId="#{NewImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_ports}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_ports_link}" action="#{NewImagesBean.seePorts}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{NewImagesBean.samplePorts}" 
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{NewImagesBean.detailRequested}" 
		selectedImageId="#{NewImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_regions}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_regions_link}" action="#{NewImagesBean.seeRegions}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{NewImagesBean.sampleRegions}" 
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{NewImagesBean.detailRequested}" 
		selectedImageId="#{NewImagesBean.imageId}"/>

</t:htmlTag>
