<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Glossary</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/section-index.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/lessons.css" rel="stylesheet" type="text/css">
	<link href="../styles/lessons-glossary.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/expandable-box.css" rel="stylesheet" type="text/css">
	<link href="../styles/lessons-expandable-box.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/utils.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/sliding-box.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<div id="top-bar">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><a href="index.faces"><img src="../images/logo.png" border="0" width="300" height="100"></a></td>
			<td class="main-menu-container"><s:mainMenuBar menuItems="#{MainMenuBean.mainMenu}" activeSectionId="lessons" activePageId="glossary" /></td>
		</tr>
		</table>
	</div>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td id="glossary-side-panel">
			
				<div id="letter-selector">
			
					<div id="glossary-search-label">Search term</div>
					<div id="glossary-search-box"><h:inputText value="#{GlossaryBean.searchTerm}" /></div>
					<div id="glossary-search-button"><h:commandButton value="Search" /></div>
				
					<s:glossaryLetters
						letters="#{GlossaryBean.letters}"
						columns="6" />
				</div>
				
				<script language="javascript" type="text/javascript">
				new SlidingBox("glossary-side-panel", "letter-selector");
				</script>
			
			</td>
			<td id="glossary-main-panel">
			
				<div id="glossary-title">Glossary</div>
	
				<s:glossaryList terms="#{GlossaryBean.terms}" />
			
			</td>
		</tr>
		</table>

	</div>

</h:form>
</f:view>
</body>
</html>