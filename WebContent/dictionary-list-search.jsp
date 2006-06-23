<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="search-jsf.css" rel="stylesheet" type="text/css">

<style type="text/css">
body {
	color: White;
	background-color: #555555; }
</style>

</head>
<body>
<form name="form">

<table border="0" cellspacing="0" cellpadding="0">
<tr>
	<td style="height: 35px; padding-left: 10px;">Quick search</td>
	<td style="height: 35px; padding-left: 10px;"><input type="text" name="searchFor" onchange="window.parent.frames['items'].searchFor(this.value)" onkeyup="window.parent.frames['items'].searchFor(this.value)" style="width: 150px;"></td>
</tr>
</table>

</form>
</body>
</html>