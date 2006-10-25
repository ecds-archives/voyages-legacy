<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:tabletab id="listingEstimates"
	data="#{EstimateListingBean.tableData}"
	sortChanged="#{EstimateListingBean.sortChanged}"
	style="overflow: auto;" />

<t:htmlTag value="div" styleClass="pager">
	<t:htmlTag id="listingEstimates-bottom-bar" value="table" style="border-collapse: collapse; width: 100%;">
		<t:htmlTag value="tr">
			<t:htmlTag value="td" style="padding: 0px;">
				<h:commandLink style="font-weight: bold; text-decoration: none;" 
					id="listingEstimates-bottom-prev"
					value="< Previous page"
					action="#{EstimateListingBean.prev}" />
				<h:outputText value=" | " />
				<h:commandLink style="font-weight: bold; text-decoration: none;" 
					id="listingEstimates-bottom-next"
					value="Next page >"
					action="#{EstimateListingBean.next}" />
				<h:outputText value=" | " />
				<h:outputText value="Showing " />
				<h:outputText value="#{EstimateListingBean.firstDisplayed}" />
				<h:outputText value="-" />
				<h:outputText value="#{EstimateListingBean.lastDisplayed}" />
				<h:outputText value=" out of " />
				<h:outputText value="#{EstimateListingBean.totalRows}" />
				<h:outputText value=" | Show " />
				<h:selectOneMenu onchange="submit()" value="#{EstimateListingBean.step}">
					<f:selectItem itemValue="10" itemLabel="10" />
					<f:selectItem itemValue="25" itemLabel="25" />
					<f:selectItem itemValue="50" itemLabel="50" />
					<f:selectItem itemValue="100" itemLabel="100" />
					<f:selectItem itemValue="all" itemLabel="all" />
				</h:selectOneMenu>
				<h:outputText value="  results per page." />
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
</t:htmlTag>
