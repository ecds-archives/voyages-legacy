<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="search-jsp.css" rel="stylesheet" type="text/css">

<style type="text/css">
body {
	background-color: #555555; }
</style>

</head>
<body>
<form>
	<table border="0" cellspacing="0" cellpadding="0" style="width: 100%">
	<tr>
		<td style="height: 35px;">
			<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td style="padding-left: 10px;"><input type="button" onclick="window.parent.frames['items'].selectAll()" value="Select all"></td>
				<td style="padding-left: 10px;"><input type="button" onclick="window.parent.frames['items'].clearAll()" value="Clear all"></td>
			</tr>
			</table>
		</td>
		<td style="padding-right: 10px; text-align: right;">
			<input type="button" onclick="window.parent.frames['items'].saveAndClose()" value="OK">
		</td>
	</tr>
	</table>
</form>
</body>
</html>