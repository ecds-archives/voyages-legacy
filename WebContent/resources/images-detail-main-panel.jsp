<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:htmlTag value="div" styleClass="image-title"><h:outputText value="#{NewImagesBean.imageTitle}" /></t:htmlTag>

<t:htmlTag value="table" style="border-collapse: collapse;" styleClass="image-navigation">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="padding: 0px 5px 0px 0px;">
			<h:commandLink value="#{res.images_back}" action="#{NewImagesBean.back}" styleClass="link-images"/>
		</t:htmlTag>				
		<t:htmlTag value="td" style="padding: 0px 5px 0px 0px;">
			<h:commandLink value="#{res.images_prev_detail_image}" action="#{NewImagesBean.prev}" styleClass="link-images"/>
		</t:htmlTag>
		<t:htmlTag value="td" style="padding: 0px;">
			<h:commandLink value="#{res.images_next_detail_image}" action="#{NewImagesBean.next}" styleClass="link-images"/>
		</t:htmlTag>				
	</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">

	<t:htmlTag value="td" style="padding: 0px; vertical-align: top;">
		<h:graphicImage url="#{NewImagesBean.imageURL}" styleClass="image-detail" onclick="#{NewImagesBean.expandJavaScript}"/>
	</t:htmlTag>

	<t:htmlTag value="td" style="padding: 0px 10px 0px 10px; vertical-align: top;">

		<t:htmlTag value="div" styleClass="image-description"><h:outputText value="#{NewImagesBean.imageDescription}" /></t:htmlTag>

		<h:dataTable var="info" value="#{NewImagesBean.imageInfo}" styleClass="image-detail-info">
			<h:column>
				<t:htmlTag value="div" styleClass="image-detail-info-label">
					<h:outputText value="#{info.name}" />
				</t:htmlTag>
			</h:column>
			<h:column>
				<h:outputText value="#{info.value}" />
			</h:column>
		</h:dataTable>
		
	</t:htmlTag>

</t:htmlTag>
</t:htmlTag>
