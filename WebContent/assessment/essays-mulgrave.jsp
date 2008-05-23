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

					<p>The Middle Passage experience generally marked its survivors
					for life, but Catherine Zimmermann-Mulgrave gives a lesson of hope.
					Catherine was a survivor of the Portuguese Schooner “Heroína”,
					which wrecked off the coast of Jamaica in 1833. The vessel had
					embarked 303 slaves on the coast of Angola, intending to sail for
					Cuba, but apparently it became lost, given that Jamaica was well
					off the normal shipping route to Cuba. Contemporary records say
					that only four slaves had drowned in the wreck, but many more had
					died at sea, during the Middle Passage. (For additional details,
					see <a href="../database/voyage.faces?voyageId=41890">VoyageID
					41890</a>).</p>
	
					<p>Catherine left no accounts of her own life. Most details
					known about her came from letters authored by her second husband,
					Johannes Zimmermann, a missionary at Ghana from the Basel Mission
					of Switzerland, and a few accounts about the shipwreck of the
					“Heroína” in Jamaica. Catherine received her western name after the
					Governor of Jamaica at the time of the shipwreck, the Earl of
					Mulgrave. However, Zimmermann recalls that her African name was
					Gewe, and that she descended from a family of chiefs by her
					father’s side, and belonged to a prominent family of mulattos by
					her mother’s side. Catherine described her home town to Zimmermann
					as a major seaport, where several Europeans lived. Much of the
					details she provided to him indicate that she grew up at Luanda, a
					Portuguese port located on the coast of Angola, but, although the
					“Heroína” departed from that coast, no clear references allow us to
					assume that Luanda was her place of birth.</p>
					
					<p>Sometime around 1833, Catherine was kidnapped on the way to
					school. She was still a little girl, when she and her friends were
					attracted by some European sailors who offered them candies.
					Promises of candies eventually turned into a forced invitation, and
					Catherine, together with her friends, was carried away from the
					coast of Angola. She reported to her husband that the captain had
					treated her well all the way to the Americas, and that she did not
					travel chained in the hold with the other slaves. Nevertheless,
					Catherine witnessed the every day life on board a slave vessel.
					Zimmermann tells that Catherine saw a slave beaten badly because he
					had attempted to commit suicide, and that the slaves were brought
					from the hold daily to enjoy some fresh air, though no doubt the
					conditions in that hold were appalling.</p>
					
					<p>Catherine, therefore, avoided the heat of a slave hold, but
					felt the fear of being kidnapped and carried to a strange land with
					just a few friends; a dreadful experience to any child. However,
					she survived the Middle Passage as she survived its miserable
					memories. In the Americas, she was able to grow up and married for
					the first time in 1843 a man named Georg Peter Thompson, and had
					two children with him; Rosie and Georg. In 1849, she divorced
					Thompson, and two years later, in 1851, she married Zimmermann, and
					formed the new family pictured <a href="../resources/images-detail.faces?image=mulgrave">
					in the image</a> available in our gallery of images. Catherine
					eventually became a teacher and returned to Africa to help her
					husband in his missionary work in Ghana, offering the world a
					message of hope from a survivor of the Middle Passage.</p>

				</s:simpleBox>
			</td>
		</tr>
		</table>
	</div>

</h:form>	
</f:view>
</body>
</html>