<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t" %>

<t:htmlTag value="table" style="border-collapse: collapse;">
<t:htmlTag value="tr">
	<t:htmlTag value="td" style="vertical-align: top; padding: 0px 10px 0px 0px;">
	
		<t:htmlTag value="div" style="margin-bottom: 5px; padding: 2px 10px 2px 5px; background-color: #F2E3B8; font-weight: bold;">
			<h:outputText value="#{res.database_search_chartsetup}" />
		</t:htmlTag>
		
		<t:htmlTag value="table" style="border-collapse: collapse;">
		<t:htmlTag value="tr">

			<t:htmlTag value="td" style="padding: 5px 10px 5px 5px;">
				<h:outputText value="#{res.database_graph_type}" />
			</t:htmlTag>

			<t:htmlTag value="td" style="padding: 0px">
				<h:commandButton action="#{GraphsBean.switchToXY}" value="XY graph" />
				<h:commandButton action="#{GraphsBean.switchToBar}" value="Bar graph" />
				<h:commandButton action="#{GraphsBean.switchToPie}" value="Pie graph" />
			</t:htmlTag>

		</t:htmlTag>
		<t:htmlTag value="tr">

			<t:htmlTag value="td" style="padding: 5px 10px 5px 5px;">
				<h:outputText value="#{res.database_search_xaxisval}" />
			</t:htmlTag>
			
			<t:htmlTag value="td" style="padding: 0px;">
				<h:selectOneMenu onchange="submit()" style="width: 300px;" value="#{GraphsBean.selectedIndependentVariableId}">
					<f:selectItems value="#{GraphsBean.independentVariables}" />
				</h:selectOneMenu>
			</t:htmlTag>
		
		</t:htmlTag>
		<t:htmlTag value="tr">

			<t:htmlTag value="td" style="padding: 5px 10px 5px 5px;">
				<h:outputText value="#{res.database_search_yaxisval}" />
			</t:htmlTag>

			<t:htmlTag value="td" style="padding: 0px;">
				<h:selectOneMenu style="width: 300px;" value="#{GraphsBean.selectedDependentVariableId}" id="yaxis_select">
					<f:selectItems value="#{GraphsBean.dependentVariables}" />
				</h:selectOneMenu>
			</t:htmlTag>

		</t:htmlTag>
		<t:htmlTag value="tr">

			<t:htmlTag value="td">
			</t:htmlTag>

			<t:htmlTag value="td" style="padding: 5px 10px 0px 0px;">

				<h:commandButton
					id="addSeries"
					value="#{res.database_search_addseries}"
					action="#{GraphsBean.addSeries}" />
					
				<h:outputText value=" " />
					
				<h:commandButton
					id="showGraph"
					value="#{res.database_search_show}"
					action="#{GraphsBean.showGraph}" />
					
			</t:htmlTag>

		</t:htmlTag>
		</t:htmlTag>
		
	</t:htmlTag>
	<t:htmlTag value="td" style="vertical-align: top;">

		<t:htmlTag value="div" style="padding: 2px 10px 2px 5px; background-color: #F2E3B8; font-weight: bold;">
			<h:outputText value="#{res.database_search_currentseries}" />
		</t:htmlTag>
	
		<t:htmlTag value="div" rendered="#{GraphsBean.haveSeries}">
			<h:selectManyCheckbox id="to_remove_check" layout="pageDirection" value="#{GraphsBean.toRemove}">
				<f:selectItems value="#{GraphsBean.series}" />
			</h:selectManyCheckbox>
			<h:commandButton id="removeSeries"
				value="#{res.database_search_remseries}"
				action="#{GraphsBean.removeSeries}" />
		</t:htmlTag>
		
		<t:htmlTag value="div" style="padding: 5px;" rendered="#{!GraphsBean.haveSeries}">
			<h:outputText value="#{res.database_search_noseriesmsg}" />
		</t:htmlTag>
		
	</t:htmlTag>
</t:htmlTag>
</t:htmlTag>

<t:htmlTag value="div" style="padding: 2px 10px 2px 5px; background-color: #F2E3B8; font-weight: bold; margin-top: 10px;">
	<h:outputText value="Graph" />
</t:htmlTag>

<t:htmlTag value="div"
	style="margin-top: 4px; width: 640px; height: 480px; overflow:auto; width: 100%; height: 500px">
	<h:graphicImage value="#{GraphsBean.chartPath}" />
</t:htmlTag>
