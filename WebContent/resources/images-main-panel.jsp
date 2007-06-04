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
		<h:commandLink value="Show all images ..." action="#{ImagesBean.seePresentation}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{ImagesBean.samplePresentation}" 
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{ImagesBean.gotoDetailFromHomepage}" 
		selectedImageId="#{ImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_slaves}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_slaves_link}" action="#{ImagesBean.seeSlaves}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{ImagesBean.sampleSlaves}" 
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{ImagesBean.gotoDetailFromHomepage}" 
		selectedImageId="#{ImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_slavers}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_slavers_link}" action="#{ImagesBean.seeSlavers}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{ImagesBean.sampleSlavers}" 
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{ImagesBean.gotoDetailFromHomepage}" 
		selectedImageId="#{ImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_vessels}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_vessels_link}" action="#{ImagesBean.seeVessels}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{ImagesBean.sampleVessels}" 
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{ImagesBean.gotoDetailFromHomepage}" 
		selectedImageId="#{ImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_ports}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_ports_link}" action="#{ImagesBean.seePorts}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{ImagesBean.samplePorts}" 
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{ImagesBean.gotoDetailFromHomepage}" 
		selectedImageId="#{ImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_regions}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_regions_link}" action="#{ImagesBean.seeRegions}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{ImagesBean.sampleRegions}" 
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{ImagesBean.gotoDetailFromHomepage}" 
		selectedImageId="#{ImagesBean.imageId}"/>

</t:htmlTag>


<t:htmlTag value="table" styleClass="images-category">
<t:htmlTag value="tr">
	<t:htmlTag value="td" styleClass="images-category-name">
		<h:outputText value="#{res.images_manuscripts}" />
	</t:htmlTag>
	<t:htmlTag value="td" styleClass="images-category-link">
		<h:commandLink value="#{res.images_manuscripts_link}" action="#{ImagesBean.seeManuscripts}" styleClass="link-images"/>
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" styleClass="images-category-sample">

	<s:pictures
		images="#{ImagesBean.sampleManuscripts}" 
		showLabels="true"
		columnsCount="5"
		thumbnailHeight="100" 
		thumbnailWidth="100" 
		action="#{ImagesBean.gotoDetailFromHomepage}" 
		selectedImageId="#{ImagesBean.imageId}"/>

</t:htmlTag>
