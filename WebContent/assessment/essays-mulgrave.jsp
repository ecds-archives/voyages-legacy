<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Essays</title>
	
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	
	<link href="../styles/assessment.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/assessment-essays.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="SlaveTradeResources" var="res"/>

	<s:siteHeader activeSectionId="assessment">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Assessing the Slave Trade" /></h:outputLink>
		<h:outputText value="Essays" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" class="essays-layout">
		<tr>
			<td id="essays-left-column">
				<f:param value="essays-mulgrave" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="essays-toc.jsp" %>
			</td>
			<td id="essays-right-column">
				<s:simpleBox>
			
					<h1>Catherine  Zimmermann-Mulgrave: A Slave Odyssey</h1>
					
					<div class="essay-info">
						<span class="essay-author">Daniel Domingues da Silva</span>
						<span class="essay-location">(Emory University)</span>,
						<span class="essay-date">August 2007</span>
					</div>
					
					
					<div class="essay-image-left">
						<a href="../resources/images-detail.faces?image=mulgrave"><img src="essays-images/mulgrave.jpg" width="200" height="180" border="0" alt="Catherine Zimmermann-Mulgrave (1873)"/></a>
						<div style="width: 200px;">
							<div class="caption">
								Catherine Zimmermann-Mulgrave (1873)
							</div>
						</div>
					</div>			

					<p>Catherine Zimmermann-Mulgrave’s life experiences after imprisonment 
					on a slave vessel were highly unusual when measured against the stories 
					of most captive Africans. She was an eight year old survivor of the 
					Portuguese Schooner “Heroína,” shipwrecked on the coast of Jamaica in 
					1833. The vessel had embarked 303 slaves on the coast of Angola, and 
					was intended for Cuba. Contemporary records indicate that only four 
					slaves had drowned in the wreck, but many more had died the transatlantic 
					crossing. (For additional details, see  <a href="../database/voyage.faces?voyageId=41890">VoyageID
					41890</a>).</p>
	
					<p>The shipwreck occurred on what was then British soil and, under 
					the terms of the 1807 act abolishing the slave trade, she and the 
					other survivors of the wreck were freed and apprenticed – in her 
					case to members of the Moravian church. Catherine left no memoirs, 
					and details of her life come from letters written by her second 
					husband, Johannes Zimmermann, a missionary from the Basel Mission 
					of Switzerland who eventually lived in what is today Ghana, and 
					from accounts of the shipwreck. Catherine’s African name was Gewe, 
					and she was apparently renamed after the Earl of Mulgrave, the 
					Governor of Jamaica at the time of the shipwreck. She was descended 
					from a family of chiefs on her father’s side, and belonged to a 
					prominent family of mulattos on her mother’s side. Catherine described 
					her home town to Zimmermann as a major seaport where several Europeans 
					lived. Many of the details she provided indicate that she may have been 
					born and spent her early years in Luanda, a Portuguese port located on 
					the coast of Angola, although we cannot verify this.</p>
					
					<p>Sometime around 1833, Catherine was kidnapped on the way to school 
					along with a group of friends. The group was lured to the “Heroína,” 
					by sailors who offered them candies. Catherine, together with her 
					friends, was carried away from the coast of Angola. She reported to 
					her husband that the captain had treated her well all the way to the 
					Americas, and, like many other children caught up in the slave trade, 
					she did not travel chained in the hold with the other slaves. Nevertheless, 
					Catherine must have witnessed every day life on board a slave vessel. 
					Zimmermann tells that Catherine saw a slave badly beaten because he had 
					attempted to commit suicide, and that slaves were brought from the hold 
					daily for air. Conditions in that hold were no doubt appalling.</p>
					
					<p>Catherine may have avoided the slave hold, but undoubtedly felt the 
					fear of being kidnapped and carried to a strange land. In the Americas, 
					she married for the first time in 1843 to Georg Peter Thompson, by whom 
					she had two children, Rosie and Georg. In 1849, she divorced Thompson, 
					and two years later, in 1851, married Zimmermann. <a href="../resources/images-detail.faces?image=mulgrave">The image</a> available 
					in our gallery of images is a photograph of the new family showing her 
					husband standing behind her chair. Catherine became a teacher and returned 
					to Africa with her husband to engage in missionary work in Ghana.</p>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>	
</f:view>
</body>
</html>