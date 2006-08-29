<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Reports</title>
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="reports.css" rel="stylesheet" type="text/css">
</head>
<body>
<f:view>

	<div class="header">
		<img src="header-text.png" width="600" height="40" border="0" alt="TAST">
	</div>
	<div class="header-path">
		<a href="search.faces">Database search</a> |
		<a href="reports.faces">Precompiled reports</a> |
		<a href="gallery.faces">Images database</a>
	</div>

	<h:form>
	<div style="margin-top: 10px;">

		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" class="report">
			
				<div class="report">
			
					<div class="report-section">Volume of slave trade by African origin</div>
					<div class="report-desciption">
					Some desciption of the report.
					Some desciption of the report.
					Some desciption of the report.
					Some desciption of the report.
					</div>
					
					<h:dataTable
						id="regions"
						var="region"
						value="#{ReportBean.africanRegions}"
						cellpadding="0" cellspacing="0" border="0">
	
						<h:column>
							<h:commandLink actionListener="#{ReportBean.showRegionReport}" action="showreport">
								<f:param id="regionId" name="regionId" value="#{region.id}" />
								<h:outputText value="#{region.name}" />
							</h:commandLink>
						</h:column>
					
					</h:dataTable>
					
				</div>
			
			</td>	
			<td valign="top" class="report">
	
				<div class="report">
	
					<div class="report-section">Distribution of slaves</div>
					<div class="report-desciption">
					Some desciption of the report.
					Some desciption of the report.
					Some desciption of the report.
					Some desciption of the report.
					Some desciption of the report.
					This report does not have any options.
					Click on Show report to see it.
					</div>
					
					<h:commandLink
						id="distributionReport"
						actionListener="#{ReportBean.showDistributionReport}"
						action="showreport"
						value="Show report" />
						
				</div>
	
			</td>
			<td valign="top" class="report">
			
				<div class="report">
	
					<div class="report-section">Volume of slave trade by national carrier</div>
					<div class="report-desciption">
					Some desciption of the report.
					Some desciption of the report.
					Some desciption of the report.
					Some desciption of the report.</div>
		
					<h:dataTable var="nation" value="#{ReportBean.nationalCarriers}" id="nations" cellpadding="0" cellspacing="0" border="0">
					<h:column>
						<h:commandLink actionListener="#{ReportBean.showNationalReport}" action="showreport">
							<f:param id="nationId" name="nationId" value="#{nation.id}" />
							<h:outputText value="#{nation.name}" />
						</h:commandLink>
					</h:column>
					</h:dataTable>
					
				</div>
	
			</td>
		</tr>
		</table>

	</div>
	</h:form>

</f:view>
</body>
</html>