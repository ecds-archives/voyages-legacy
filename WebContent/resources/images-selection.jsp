<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:messageBar rendered="false" binding="#{ImagesBean.messageBar}" />

<s:expandableBox text="Search for images">

	<t:htmlTag value="table">
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Image title"/>
		</t:htmlTag>
		<t:htmlTag value="td">	
			<h:inputText style="width: 150px;" value="#{ImagesBean.currentQuery.searchQueryTitle}"/>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Description"/>
		</t:htmlTag>
		<t:htmlTag value="td">	
			<h:inputText style="width: 150px;" value="#{ImagesBean.currentQuery.searchQueryDescription}"/>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Voyage ID"/>
		</t:htmlTag>
		<t:htmlTag value="td">	
			<h:inputText style="width: 150px;" value="#{ImagesBean.currentQuery.searchVoyageId}"/>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Category"/>
		</t:htmlTag>
		<t:htmlTag value="td">			
			<h:selectOneMenu style="width: 150px;" 
					value="#{ImagesBean.currentQuery.searchQueryCategory}">
				<f:selectItems value="#{ImagesBean.categories}" />
			</h:selectOneMenu>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Date range"/>
		</t:htmlTag>
		<t:htmlTag value="td">
			<h:inputText style="width: 40px;" value="#{ImagesBean.currentQuery.searchQueryFrom}"/>
			<h:outputText escape="false" value="&nbsp;-&nbsp;"/>
			<h:inputText style="width: 40px;" value="#{ImagesBean.currentQuery.searchQueryTo}"/>
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

<br>

<s:expandableBox text="#{res.images_current_query}">

	<s:querySummary
		items="#{ImagesBean.querySummary}"
		noQueryText="#{res.images_current_no_query}" />
		
	<t:div style="margin-top: 5px;">
		<h:commandButton value="permlink" styleClass="button-save" action="#{ImagesBean.permLink}"/>
	</t:div>
	
</s:expandableBox>