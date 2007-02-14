<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<t:htmlTag value="table" styleClass="table-main-images">
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="border-bottom: 3px solid #165B31;">
			<t:htmlTag value="h1"><h:outputText value="Vessles"/> </t:htmlTag >
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td">	
			<s:pictures images="#{NewImagesBean.sampleVessels}" 
				columnsCount="5"
				thumbnailHeight="100" 
				thumbnailWidth="100" />
			<t:htmlTag value="div" style="width: 100%; margin-left: auto; text-align: right;">
				<h:commandLink value="See all ->" action="#{NewImagesBean.seeVessels}" styleClass="link-images"/>
			</t:htmlTag>
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="border-bottom: 3px solid #165B31;">	
			<t:htmlTag value="h1"><h:outputText value="Slaves"/></t:htmlTag >
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td">	
			<s:pictures images="#{NewImagesBean.sampleSlaves}" 
				columnsCount="5"
				thumbnailHeight="100" 
				thumbnailWidth="100" />
			<t:htmlTag value="div" style="width: 100%; margin-left: auto; text-align: right;">
				<h:commandLink value="See all ->" action="#{NewImagesBean.seeSlaves}" styleClass="link-images"/>
			</t:htmlTag>
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="border-bottom: 3px solid #165B31;">
			<t:htmlTag value="h1"><h:outputText value="Slavers"/></t:htmlTag >
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td">	
			<s:pictures images="#{NewImagesBean.sampleSlavers}" 
				columnsCount="5"
				thumbnailHeight="100" 
				thumbnailWidth="100" />
			<t:htmlTag value="div" style="width: 100%; margin-left: auto; text-align: right;">
				<h:commandLink value="See all ->" action="#{NewImagesBean.seeSlavers}" styleClass="link-images"/>
			</t:htmlTag>
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="border-bottom: 3px solid #165B31;">
			<t:htmlTag value="h1"><h:outputText value="Ports"/></t:htmlTag >
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td">	
			<s:pictures images="#{NewImagesBean.samplePorts}" 
				columnsCount="5"
				thumbnailHeight="100" 
				thumbnailWidth="100" />
			<t:htmlTag value="div" style="width: 100%; margin-left: auto; text-align: right;">
				<h:commandLink value="See all ->" action="#{NewImagesBean.seePorts}" styleClass="link-images"/>
			</t:htmlTag>
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td" style="border-bottom: 3px solid #165B31;">
			<t:htmlTag value="h1"><h:outputText value="Regions"/></t:htmlTag >
		</t:htmlTag >
	</t:htmlTag >
	<t:htmlTag value="tr">
		<t:htmlTag value="td">	
			<s:pictures images="#{NewImagesBean.sampleRegions}" 
				columnsCount="5"
				thumbnailHeight="100" 
				thumbnailWidth="100" />
			<t:htmlTag value="div" style="width: 100%; margin-left: auto; text-align: right;">
				<h:commandLink value="See all ->" action="#{NewImagesBean.seeRegions}" styleClass="link-images"/>
			</t:htmlTag>			
		</t:htmlTag >
	</t:htmlTag >
</t:htmlTag>