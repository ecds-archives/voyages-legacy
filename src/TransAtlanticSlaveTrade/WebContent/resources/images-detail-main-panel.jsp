<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" styleClass="image-title">
	<h:outputText value="#{ImagesBean.imageTitle}" />
</t:htmlTag>

<t:htmlTag value="div" styleClass="image-navigation">
	<t:htmlTag value="table" styleClass="image-navigation">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 0px 10px 0px 0px;">
				<h:commandLink value="#{ImagesBean.backFromDetailTitle}" action="list" styleClass="link-back-to-list"/>
			</t:htmlTag>				
			<t:htmlTag value="td" style="padding: 0px 0px 0px 0px;">
				<h:commandLink value="#{res.images_prev_detail_image}" action="#{ImagesBean.gotoPrev}" styleClass="link-prev-image"/>
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 0px 10px 0px 10px">
				<h:outputText value="#{ImagesBean.galleryPositionIndicator}" />
			</t:htmlTag>				
			<t:htmlTag value="td" style="padding: 0px;">
				<h:commandLink value="#{res.images_next_detail_image}" action="#{ImagesBean.gotoNext}" styleClass="link-next-image"/>
			</t:htmlTag>				
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">

	<t:htmlTag value="td" style="padding: 0px; vertical-align: top;">
		<h:graphicImage
			url="#{ImagesBean.imageURL}"
			styleClass="image-detail"
			onclick="#{ImagesBean.expandJavaScript}"/>
	</t:htmlTag>

	<t:htmlTag value="td" style="padding: 0px 10px 0px 10px; vertical-align: top;">

		<t:htmlTag value="div" styleClass="image-description"><h:outputText value="#{ImagesBean.imageDescription}" /></t:htmlTag>

		<h:dataTable var="info" value="#{ImagesBean.imageInfo}" styleClass="image-detail-info">
			<h:column>
				<t:htmlTag value="div" styleClass="image-detail-info-label">
					<h:outputText value="#{info.name}" />
				</t:htmlTag>
			</h:column>
			<h:column>
				<h:outputText value="#{info.value}" escape="false" />
			</h:column>
		</h:dataTable>
		
		<t:htmlTag value="div" styleClass="image-detail-voyages-title" rendered="#{ImagesBean.hasImageVoyages}"><h:outputText value="#{res.images_related_voyages}" /></t:htmlTag>
		
		<h:dataTable var="voyage" value="#{ImagesBean.imageVoyages}" binding="#{ImagesBean.linkedVoyagesTable}" rendered="#{ImagesBean.hasImageVoyages}" styleClass="image-detail-voyages">
			<h:column>
				<f:param id="voyageId" value="#{voyage.voyageId}" />
				<h:outputText value="#{res.images_voyage_id}" />
				<h:commandLink action="#{ImagesBean.gotoVoyage}" value="#{voyage.voyageId}" />
				<h:outputText value=" - " />
				<h:outputText value="#{voyage.info}" />
			</h:column>
		</h:dataTable>
		
	</t:htmlTag>

</t:htmlTag>
</t:htmlTag>
