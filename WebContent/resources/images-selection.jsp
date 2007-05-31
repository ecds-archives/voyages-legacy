<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:expandableBox text="Search for images">

	<t:htmlTag value="table">
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Image title"/>
		</t:htmlTag>
		<t:htmlTag value="td">	
			<h:inputText style="width: 150px;" value="#{ImagesBean.searchQueryTitle}"/>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Description"/>
		</t:htmlTag>
		<t:htmlTag value="td">	
			<h:inputText style="width: 150px;" value="#{ImagesBean.searchQueryDescription}"/>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Voyage ID"/>
		</t:htmlTag>
		<t:htmlTag value="td">	
			<h:inputText style="width: 150px;" value="#{ImagesBean.searchVoyageId}"/>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Category"/>
		</t:htmlTag>
		<t:htmlTag value="td">			
			<h:selectOneMenu style="width: 150px;" 
					value="#{ImagesBean.searchQueryCategory}">
				<f:selectItems value="#{ImagesBean.categories}" />
			</h:selectOneMenu>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Date range"/>
		</t:htmlTag>
		<t:htmlTag value="td">
			<h:inputText style="width: 40px;" value="#{ImagesBean.searchQueryFrom}"/>
			<h:outputText escape="false" value="&nbsp;-&nbsp;"/>
			<h:inputText style="width: 40px;" value="#{ImagesBean.searchQueryTo}"/>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td">						
		</t:htmlTag>
		<t:htmlTag value="td">			
			<h:commandButton value="Search images" action="#{ImagesBean.search}"/>
		</t:htmlTag>
	</t:htmlTag>
	
	</t:htmlTag>
	
</s:expandableBox>