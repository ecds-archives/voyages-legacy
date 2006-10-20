<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:tabletab id="listingEstimates"
	data="#{EstimateListingBean.tableData}"
	sortChanged="#{EstimateListingBean.sortChanged}"
	style="overflow: auto;" />
