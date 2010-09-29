<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Methodology</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">

	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/tooltip.js" type="text/javascript" language="javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Understanding the Satabase" />
		<h:outputText value="Methodology" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<f:param value="agecategories" binding="#{EssaysBean.paramActiveMenuId}" />
				<%@ include file="guide-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>
				
					<h1>Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods</h1>
					
                    <div class="method-info">
						<span class="method-author"><b>David Eltis</b></span>
						<span class="method-location">(Emory University)</span>,
						<span class="method-date">2010</span>
					</div>
									
					<h2>Age categories</h2>	
				
					<p>Age categories must also be used with care.(6) The expanded version of the Voyages Database includes four variables 
					distinguishing the number of captives by age and gender: adult males ("men"), adult females ("women"), male children ("boys"), 
					and female children ("girls");  three variables classifying slaves by age, without specifying gender: “adults,” “children,” 
					and “infants” (reported often as "infants at the breast"),; and two variables classifying them by gender, but not by age: “males” 
					and “females.”  These data variables replicate how slaves were categorized in primary sources.</p>
					
					<p>Age and sex definitions changed over time and among carriers. Arrivals in the early Iberian Americas were assigned 
					a ratio of what a prime male slave would cost—the latter being termed a pieça da Indes. A child would receive a rating 
					of half a <i>pieça</i>, a woman 0.8, and so on. It has not proved possible to infer age and gender breakdowns from aggregated 
					<i>pieças</i>. In the 1660–1730 period, the London-based Royal African Company (RAC) defined children as about ten years of 
					age or younger. For most of the British and French slave trades, a height (about four feet four inches) and/or age 
					(about puberty) criterion distinguished adults from children. In the nineteenth century, captured slave ships of all 
					nations, but mainly Spanish and Brazilian, had their human cargoes recorded by a variety of courts, some British, some 
					international. There is little doubt that the criterion used to separate out adults was sexual maturity as assessed by 
					physical appearance, which for most Africans at this time would probably occur in the mid-teens, but could vary according 
					to the diet prevalent in the areas from which Africans were drawn as well as according to the eye of the purchasers. 
					Yet another categorization emerges from Cuban slave trade data (1790–1820) taken from the Seville archives, which 
					adds "men-boys" and "women-girls" to the previous categories. These we included among men and women, respectively.</p>
					
					<p>All these measurements are of course imprecise, with even a clear age definition of "ten years and younger" hinging 
					on casual inspection by Europeans, because many African cultures did not attach importance to knowledge of exact ages. 
					In nineteenth-century court records, different officials often recorded slightly different distributions of the same 
					group of slaves. However, in documents with information on height of slaves, classification as an adult is there is 
					correlated with height (specifically the teenage growth spurt) and sexual maturity.  Thus the RAC’s definition of 
					children as under the age of ten less excluded individuals that other definitions would have included as children 
					As the RAC records form the bulk of the age and gender information for 1660–1710, the share of children for this 
					period is biased downward.</p>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-05.faces">Data Variables</a>
						</td>
						<td class="method-next">
							<a href="methodology-06.faces">Dates</a>
						</td>
					</tr>
					</table>
				</s:simpleBox>
			</td>
		</tr>
		</table>
		
	</div>
</h:form>
</f:view>

<%@ include file="../footer.jsp" %>

</body>
</html>