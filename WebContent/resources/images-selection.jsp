<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:expandableBox text="Search for images">

<t:htmlTag value="div" styleClass="images-query-box">

	<t:htmlTag value="table" style="border-collapse: collapse">
		
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 5px 10px 5px 0px;">
				<h:outputText value="Keyword"/>
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px">
				<h:inputText style="width: 150px;" value="#{ImagesBean.workingQuery.keyword}"/>
			</t:htmlTag>
		</t:htmlTag>
		
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 5px 10px 5px 0px;">
				<h:outputText value="Time frame"/>
			</t:htmlTag>
			<t:htmlTag value="td" style="padding: 5px 0px">
				<h:inputText style="width: 40px;" value="#{ImagesBean.workingQuery.yearFrom}"/>
				<h:outputText escape="false" value="&nbsp;-&nbsp;"/>
				<h:inputText style="width: 40px;" value="#{ImagesBean.workingQuery.yearTo}"/>
			</t:htmlTag>
		</t:htmlTag>
		
	</t:htmlTag>

</t:htmlTag>

<t:htmlTag value="div" styleClass="images-query-box">

	<h:outputText value="Category"/>
	
	<t:htmlTag value="div" style="margin: 5px 0px 0px 0px;">
		<s:checkboxListExpandable
			items="#{ImagesBean.categories}"
			selectedValues="#{ImagesBean.workingQuery.categories}" 
			showSelectAll="false" />
	</t:htmlTag>

</t:htmlTag>

<t:htmlTag value="div" styleClass="images-query-search-button">
	<h:commandButton value="Search images" action="#{ImagesBean.search}"/>
	<h:outputText value=" " />
	<h:commandButton value="Start again" action="#{ImagesBean.startAgain}"/>	
</t:htmlTag>
	
</s:expandableBox>