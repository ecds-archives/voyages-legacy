<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<t:htmlTag value="table">
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:graphicImage url="#{NewImagesBean.imageURL}" style="width: 500px;"/>
		</t:htmlTag>
		<t:htmlTag value="td">
			<h:dataTable var="info" value="#{NewImagesBean.imageInfo}">
				<h:column>
					<h:outputText value="#{info.name}"/>
				</h:column>
				<h:column>
					<h:outputText value="#{info.value}"/>
				</h:column>
			</h:dataTable>		
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>
