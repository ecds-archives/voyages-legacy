<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<s:infoBox>
	<t:div styleClass="no-result-title">
		<h:outputText value="#{res.database_search_no_result_title}" />
	</t:div>
	<t:div styleClass="no-result-desc">
		<h:outputText value="#{res.database_search_no_result_desc}" />
	</t:div>
</s:infoBox>
