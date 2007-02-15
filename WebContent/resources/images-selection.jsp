<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:expandableBox text="Sample queries">
	<f:verbatim escape="false">
		<ul class="box">
			<li>How did the atlantic slave trade effect the economy of Europe, Africa, and America?</li>
			<li>Where were they taken to?</li>
			<li>Which European country was the first to engage in the slave trade in Central Africa?</li>
			<li>Has there ever been evidence of a slave earning his freedom and returning to Africa in their lifetime?</li>
			<li>Why were the slaves taken from Africa?</li>
			<li>show me more queries...</li>
		</ul>
	</f:verbatim>
</s:expandableBox>

<br>

<s:expandableBox text="Search for images">
	<t:htmlTag value="table">
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Image: "/>
		</t:htmlTag>
		<t:htmlTag value="td">	
			<h:inputText style="width: 150px;" value="#{NewImagesBean.imageLike}"/>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Category: "/>
		</t:htmlTag>
		<t:htmlTag value="td">			
			<h:selectOneMenu style="width: 150px;" 
					value="#{NewImagesBean.selectedCategory}">
				<f:selectItems value="#{NewImagesBean.categories}" />
			</h:selectOneMenu>
		</t:htmlTag>
	</t:htmlTag>

	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:outputText value="Date range: "/>
		</t:htmlTag>
		<t:htmlTag value="td">
			<h:inputText style="width: 40px;" value="#{NewImagesBean.from}"/>
			<h:outputText escape="false" value="&nbsp;-&nbsp;"/>
			<h:inputText style="width: 40px;" value="#{NewImagesBean.to}"/>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="tr">
		<t:htmlTag value="td">						
		</t:htmlTag>
		<t:htmlTag value="td">			
			<h:commandButton value="Search images" action="#{NewImagesBean.search}"/>
		</t:htmlTag>
	</t:htmlTag>
	</t:htmlTag>
</s:expandableBox>