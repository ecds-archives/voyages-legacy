<%@ page isErrorPage="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%

session.invalidate();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error</title>
	
<style type="text/css">

body {
	margin: 0px;
	padding: 0px;
	font-family: Arial, Helvetica, Bitstream Vera Sans, Sans, sans-serif;
	background-color: #E5EFF1; }
	
div#top-band {
	padding-top: 100px;
	background-color: #B0C8C5; }
	
div#header-outer {
	margin-left: auto;
	margin-right: auto;
	width: 500px;
	background-image: url(<%= request.getContextPath() %>/images/error-top-left.png);
	background-position: left top;
	background-repeat: no-repeat; }
	
div#header-inner {
	background-image: url(<%= request.getContextPath() %>/images/error-top-right.png);
	background-position: right top;
	background-repeat: no-repeat; }
	
div#header-text {
	background-image: url(<%= request.getContextPath() %>/images/error-icon.png);
	background-position: 15px center;
	background-repeat: no-repeat;
	padding-top: 15px;
	padding-bottom: 15px;
	padding-left: 60px;
	font-size: 16pt; }
	
div#bottom-band {
	}
	
div#message-frame-1 {
	margin-left: auto;
	margin-right: auto;
	width: 500px;
	background-image: url(<%= request.getContextPath() %>/images/error-middle-left.png);
	background-position: left top;
	background-repeat: no-repeat; }
	
div#message-frame-2 {
	background-image: url(<%= request.getContextPath() %>/images/error-middle-right.png);
	background-position: right top;
	background-repeat: no-repeat; }	
	
div#message-frame-3 {
	background-image: url(<%= request.getContextPath() %>/images/error-bottom-left.png);
	background-position: left bottom;
	background-repeat: no-repeat; }	

div#message-frame-4 {
	background-image: url(<%= request.getContextPath() %>/images/error-bottom-right.png);
	background-position: right bottom;
	background-repeat: no-repeat; }	

div#message-text {
	font-size: 9pt;
	padding-top: 5px;
	padding-bottom: 10px;
	padding-left: 60px;  
	padding-right: 30px; }
	
p {
	margin: 1em 0em; }

ul {
	padding: 0em 0em 0em 2.5em;
	margin: 1em 0em; }

li {
	padding: 0em;
	margin: 0em;
	list-style-type: square;
	color: #227766; }

a {
	text-decoration: none;
	color: #FF6600; }

</style>
		
</head>
<body>

<div id="top-band">
	<div id="header-outer">
		<div id="header-inner">
			<div id="header-text">
				An error has occurred
			</div>
		</div>
	</div>
</div>

<div id="middle-band">
	<div id="message-frame-1">
		<div id="message-frame-2">
		 	<div id="message-frame-3">
		 		<div id="message-frame-4">
		 		
					<div id="message-text">
				
						<p>
						Please inform us about the time and nature of the error.
						If you are able to reproduce the steps leading to the error
						message, please provide us with this information.
						We will address the problem as soon as possible. 
						</p>
						
						<ul>
							<li><a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">Voyages website administrator email</a></li>
						</ul>
						
						<p>
						To continue using the application, you session information has been
						reset. Use the following links to get back to track:
						</p>
						
						<ul>
							<li><a href="<%= request.getContextPath() %>/index.faces">Homepage</a></li>
							<li><a href="<%= request.getContextPath() %>/database/search.faces">Search the Voyages Database</a></li>
							<li><a href="<%= request.getContextPath() %>/assessment/estimates.faces">Examine Estimates of the Slave Trade</a></li>
							<li><a href="<%= request.getContextPath() %>/resources/slaves.faces">Explore the African Names Database</a></li>
						</ul>
						
						<p>
						We apologize for the inconvenience.
						</p> 
		
					</div>
					
				</div>
			</div>
		</div>
	</div>
</div>

</body>
</html>