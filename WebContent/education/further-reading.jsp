<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<title>Further reading</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/education.css" rel="stylesheet" type="text/css">
	<link href="../styles/education-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/education-further-reading.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="lessons">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="index.faces"><h:outputText value="Educational Materials" /></h:outputLink>
		<h:outputText value="Further reading" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="education-layout">
		<tr>
			<td id="education-left-column">
				<%@ include file="education-menu.jsp" %>
			</td>
			<td id="education-right-column">
				<s:simpleBox>
				
					<h1>Further reading</h1>

					<p>A temporary text follows. The websites listed here provide a
					variety of resources, including but not limited to lesson plans,
					interactive maps, and narratives of enslaved Africans. To help give
					additional context to the Atlantic slave trade, a list of scholarly
					books are also provided to users. These titles are just a few of
					the numerous works related to the forced migration of millions of
					Africans.</p>

					<ul id="further-reading">
						<li>
							<span class="author">Ira Berlin</span>,
							<span class="title">Generations of Captivity: A History of African-American Slaves</span>
							<span class="year">(2003)</span>
						</li>
						<li>
							<span class="author">Phillip Curtin,</span>
							<span class="title">The Rise and Fall of the Plantation Complex: Essays in Atlantic History</span>
							<span class="year">(1998)</span>
						</li>
						<li>
							<span class="author">David Eltis,</span>
							<span class="title">The Rise of African Slavery in the Americas</span>
							<span class="year">(2000)</span>
						</li>
						<li>
							<span class="author">Olaudah Equiano,</span>
							<span class="title">The Interesting Narrative</span>
							<span class="year">(1789)</span>
						</li>
						<li>
							<span class="author">James Horton &amp; Lois Horton,</span>
							<span class="title">Slavery and the Making of America</span>
							<span class="year">(2005)</span>
						</li>
						<li>
							<span class="author">Herbert Klein,</span>
							<span class="title">The Atlantic Slave Trade</span>
							<span class="year">(1999)</span>
						</li>
						<li>
							<span class="author">Peter Kolchin,</span>
							<span class="title">American Slavery: 1619-1877</span>
							<span class="year">(1995)</span>
						</li>
						<li>
							<span class="author">Phillip Morgan,</span>
							<span class="title">Slave Counterpoint: Black Culture in the Eighteenth-Century Chesapeake and Lowcountry</span>
							<span class="year">(1998)</span>
						</li>
						<li>
							<span class="author">David Northrup (ed),</span>
							<span class="title">The Atlantic Slave Trade</span>
							<span class="title">(2001)</span>
						</li>
					</ul>

				</s:simpleBox>
			</td>
		</tr>
		</table>
		
	</div>

</h:form>
</f:view>
</body>
</html>