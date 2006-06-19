<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Search</title>
</head>
<frameset rows="35,*,35">
	
	<frame
		frameborder="0"
		marginwidth="0"
		marginheight="0"
		scrolling="no"
		name="quicksearch"
		src="dictionary-list-search.jsp?<%= request.getQueryString() %>">
	
	<frame
		frameborder="0"
		marginwidth="0"
		marginheight="0"
		scrolling="auto"
		name="items"
		src="dictionary-list-items.faces?<%= request.getQueryString() %>">
	
	<frame
		frameborder="0"
		marginwidth="0"
		marginheight="0"
		scrolling="no"
		name="buttons"
		src="dictionary-list-buttons.jsp?<%= request.getQueryString() %>">

</frameset>
</html>