<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script language="javascript" src="jsonrpc.js"></script>

<script language="javascript">

window.onload = function()
{

	var jsonrpc = new JSONRpcClient("JSON-RPC");
	//alert(jsonrpc.search.test());
	
	var request =
	{
		javaClass : "edu.emory.library.tas.web.test.SearchRequest",
		fetchResults : 222, 
		firstResult : 111,
		conditions :
		{
			javaClass : "java.util.ArrayList",
			list :
			[
				{
					javaClass : "edu.emory.library.tas.web.test.SearchCondition",
					fieldName : "shipname",
					value : "Titanic"
				},
				{
					javaClass : "edu.emory.library.tas.web.test.SearchCondition",
					fieldName : "captaina",
					value : "Nemo"
				}
			]
		}
	} 
	
	jsonrpc.search.search(requestdone, request);

}

function requestdone(result, exception)
{

	if(exception)
	{
		alert(exception.message);
		return;
	}
	
	alert(result);

}

</script>

</head>
<body>


</body>
</html>
