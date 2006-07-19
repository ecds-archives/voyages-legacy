<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test</title>

<style type="text/css">

body {
	background-color: #F1E7C8;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 8pt;
	margin: 0px;
	padding: 0px; }

input, textarea, select {
	border: 0px;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 8pt; }

div.header {
	padding-top: 10px;
	padding-left: 10px;
	padding-bottom: 5px;
	background-color: #571B1D;
	background-repeat: repeat-x;
	background-position: left -3px;
	background-image: url(header-decoration.png); }

div.header-path {
	font-weight: bold;
	padding-left: 10px;
	padding-top: 5px;
	padding-bottom: 5px;
	color: White;
	background-color: #CA4223; }

/* main two pannels */

td.side-panel {
	padding-top: 10px;
	padding-bottom: 10px;
	padding-left: 10px;
	padding-right: 5px;
	width: 300px;
	vertical-align: top; }

td.main-panel {
	padding-top: 10px;
	padding-bottom: 10px;
	padding-left: 5px;
	padding-right: 10px;
	vertical-align: top; }

/* expandable sections */

div.section-title {
	font-weight: bold; }

div.section-title-dark {
	color: White;
	background-color: #571B1D; }

div.section-title-middle {
	color: White;
	background-color: #CA4223; }

div.section-title-light {
	color: White;
	background-color: #E2873B; }

div.section-title-line-dark {
	border-bottom: 2px solid #571B1D; }

div.section-title-line-middle {
	border-bottom: 2px solid #CA4223; }

div.section-title-line-light {
	border-bottom: 2px solid #E2873B; }

table.section-title {
	width: 100%; }

td.section-title-left {
	text-align: left; }

td.section-title-buttons {
	padding-right: 10px;
	text-align: right; }

img.section-button {
	cursor: pointer; }

div.section-title-text {
	padding-right: 10px;
	padding-left: 10px;
	padding-top: 5px;
	padding-bottom: 5px; }

td.section-title-tab {
	cursor: pointer;
	padding-right: 10px;
	padding-left: 10px;
	padding-top: 5px;
	padding-bottom: 5px; }

td.section-title-tab-light {
	}

td.section-title-tab-light:hover,
td.section-title-tab-light-selected {
	background-color: #E2873B; }

td.section-title-tab-middle {
	}

td.section-title-tab-middle:hover,
td.section-title-tab-middle-selected {
	background-color: #CA4223; }

td.section-title-tab-dark {
	}

td.section-title-tab-dark:hover,
td.section-title-tab-dark-selected {
	background-color: #571B1D; }

div.section-body {
	margin-bottom: 10px;
	background-color: White; }

</style>

</head>
<body>
<f:view>
	<h:form id="form">
	
		<s:sectionGroup
			backgroundStyle="dark"
			tabsStyle="middle"
			buttonsStyle="middle"
			selectedSectionId="abc">
			<s:section title="ABC" sectionId="abc">
			
				<h:outputText value="a" />
				<h:outputText value="b" />
				<h:outputText value="c" />
			
			</s:section>
			<s:section title="DEF" sectionId="def">
			
				<h:outputText value="d" />
				<h:outputText value="e" />
				<h:outputText value="f" />
			
			</s:section>
		</s:sectionGroup>
	
	</h:form>
</f:view>
</body>
</html>
