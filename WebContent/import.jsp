<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="edu.emory.library.tas.web.upload.Upload" %>
<%@ page import="com.metaparadigm.jsonrpc.JSONRPCBridge" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<% JSONRPCBridge.getGlobalBridge().registerClass("Upload", Upload.class); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Import</title>
<link href="import.css" rel="stylesheet" type="text/css">

<script language="javascript" src="jslib/jsonrpc.js"></script>
<script language="javascript">

var json;
var uploadId = <%= Upload.getNextUploadId() %>;

window.onload = function()
{
	json = new JSONRpcClient("JSON-RPC");
}

function beforeUpload()
{
	document.forms["upload"].action = "servlet/ImportServlet?UPLOAD=" + uploadId;
	window.setTimeout("refreshStatus()", 500);
	return true;
}

function refreshStatus()
{
	var uploadInfo = json.Upload.getUploadInfo(uploadId);

	if (uploadInfo)
	{
	
		var proc = uploadInfo.totalBytes / uploadInfo.expectedLength;
		
		document.getElementById("statusBar").style.width =
			Math.round(proc*100) + "%";
			
		document.getElementById("progressFileName").innerHTML = 
			uploadInfo.currentFile;
		
		document.getElementById("progressFileSize").innerHTML = 
			uploadInfo.currentFileBytes + " B";

		document.getElementById("progressTotalProgress").innerHTML = 
			uploadInfo.totalBytes + " B, " +
			(Math.round(proc * 10000) / 100) + "%";

	}		
		
	window.setTimeout("refreshStatus()", 500);
}

</script>

</head>
<body>
<f:view>
<form onsubmit="return beforeUpload()" target="upload-iframe" action="/servlet/ImportServlet" name="upload" method="post" enctype="multipart/form-data">

<h1>Import</h1>

<div class="main-container">

	<h2>Select files</h2>
	
	<table border="0" cellspacing="5" cellpadding="0">
	<tr>
		<td>Voyages</td>
		<td><input type="file" name="voyages" size="100"></td>
	</tr>
	<tr>
		<td>Slaves</td>
		<td><input type="file" name="slaves" size="100"></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" value="Upload"></td>
	</tr>
	</table>
	
	<div style="margin-top: 20px; padding: 5px; background-color: #EEEEEE; font-weight: bold;">Upload progress</div>
	
	<table border="0" cellspacing="5" cellpadding="0">
	<tr>
		<td><b>File name</b></td>
		<td id="progressFileName"></td>
	</tr>
	<tr>
		<td><b>File size</b></td>
		<td id="progressFileSize"></td>
	</tr>
	<tr>
		<td><b>Total progress</b></td>
		<td id="progressTotalProgress"></td>
	</tr>
	</table>
	
	<div id="statusBarFrame" style="width: 300px; overflow: hidden; height: 10px; border: 1px solid #CCCCCC"><div style="background-color: #EEEEEE; width: 0%; overflow: hide; height: 10px;" id="statusBar" ></div></div>
	
	<iframe style="display: none" name="upload-iframe" width="600" height="300"></iframe>
	
</div>

</form>
</f:view>
</body>
</html>