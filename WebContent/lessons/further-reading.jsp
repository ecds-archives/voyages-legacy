<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Links</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/lessons.css" rel="stylesheet" type="text/css">
	<link href="../styles/lessons-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/lessons-links.css" rel="stylesheet" type="text/css">
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
	
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" style="width: 210px;">
			
				<div class="left-menu-box">
					<s:simpleBox>
						<div class="left-menu-title">FURTHER READING</div>
						<s:secondaryMenu>
							<s:secondaryMenuItem label="In Motion: African-American Migration Experience Schomburg Center (NYPL)" menuId="inmotionaame" href="#inmotionaame" />
							<s:secondaryMenuItem label="YES / College Board" menuId="collegeboard" href="#collegeboard" />
							<s:secondaryMenuItem label="Thirteen / National Teacher Training Institute" menuId="thirteen" href="#thirteen" />
							<s:secondaryMenuItem label="National Center for History in the Schools" menuId="nchs" href="#nchs" />
							<s:secondaryMenuItem label="Slavery in America" menuId="slaveryinamerica" href="#slaveryinamerica" />
							<s:secondaryMenuItem label="Breaking the Silence / Anti-Slavery" menuId="antislavery" href="#antislavery" />
							<s:secondaryMenuItem label="Understanding Slavery" menuId="understandingslavery" href="#understandingslavery" />
							<s:secondaryMenuItem label="Abolition of the Slave Trade / BBC" menuId="bbc" href="#bbc" />
						</s:secondaryMenu>
					</s:simpleBox>
				</div>
				
			</td>
			<td valign="top" style="width: 680px;">

				<s:simpleBox>
				
					<h1>Further reading</h1>
					
					<ul>
						<li>Ira Berlin, Generations of Captivity: A History of African-American Slaves (2003)</li>
						<li>Phillip Curtin, The Rise and Fall of the Plantation Complex: Essays in Atlantic History (1998)</li>
						<li>David Eltis, The Rise of African Slavery in the Americas (2000)</li>
						<li>Olaudah Equiano, The Interesting Narrative (1789)</li>
						<li>James Horton & Lois Horton, Slavery and the Making of America (2005)</li>
						<li>Herbert Klein, The Atlantic Slave Trade (1999)</li>
						<li>Peter Kolchin, American Slavery: 1619-1877 (1995)</li>
						<li>Phillip Morgan, Slave Counterpoint: Black Culture in the Eighteenth-Century Chesapeake and Lowcountry (1998)</li>
						<li>David Northrup (ed), The Atlantic Slave Trade (2001)</li>
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