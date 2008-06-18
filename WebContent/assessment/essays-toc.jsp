<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div styleClass="left-menu-box">

	<s:simpleBox>
	
		<t:div styleClass="left-menu-title">
			<h:outputText value="ESSAYS" />
		</t:div>
		
		<s:secondaryMenu activeItemId="#{EssaysBean.activeMenuId}">
		
			<s:secondaryMenuItem
				label="Interpretations"
				menuId="interpretations">
			
				<s:secondaryMenuItem
					label="A Brief Overview of the Trans-Atlantic Slave Trade"
					href="essays-intro-01.faces"
					menuId="essays-intro"
					expanded="#{EssaysBean.introExpanded}">
				
					<s:secondaryMenuItem
						label="Introduction"
						href="essays-intro-01.faces"
						menuId="essays-intro-01" />
					
					<s:secondaryMenuItem
						label="The Enslavement of Africans"
						href="essays-intro-02.faces"
						menuId="essays-intro-02" />
					
					<s:secondaryMenuItem
						label="African Agency and Resistance"
						href="essays-intro-03.faces"
						menuId="essays-intro-03" />
				
					<s:secondaryMenuItem
						label="Early Slaving Voyages"
						href="essays-intro-04.faces"
						menuId="essays-intro-04" />
				
					<s:secondaryMenuItem
						label="Empire and Slavery"
						href="essays-intro-05.faces"
						menuId="essays-intro-05" />
					
					<s:secondaryMenuItem
						label="The African Side of the Trade"
						href="essays-intro-06.faces"
						menuId="essays-intro-06" />
					
					<s:secondaryMenuItem
						label="The Middle Passage"
						href="essays-intro-07.faces"
						menuId="essays-intro-07" />
						
					<s:secondaryMenuItem
						label="The Ending of the Slave Trade"
						href="essays-intro-08.faces"
						menuId="essays-intro-08" />
	
					<s:secondaryMenuItem
						label="The Trade’s Influence on Ethnic and Racial Identity"
						href="essays-intro-09.faces"
						menuId="essays-intro-09" />
	
					<s:secondaryMenuItem
						label="Eventual Abolition"
						href="essays-intro-10.faces"
						menuId="essays-intro-10" />
						
					<s:secondaryMenuItem
						label="Notes"
						href="essays-intro-11.faces"
						menuId="essays-intro-11" />
						
				</s:secondaryMenuItem>
				
				<s:secondaryMenuItem
					label="Seasonality in the Trans-Atlantic Slave Trade"
					href="essays-seasonality-01.faces"
					menuId="essays-seasonality"
					expanded="#{EssaysBean.seasonalityExpanded}">
					
					<s:secondaryMenuItem
						label="Introduction"
						href="essays-seasonality-01.faces"
						menuId="essays-seasonality-01" />
					
					<s:secondaryMenuItem
						label="Agriculture in the era of the trans-Atlantic slave trade"
						href="essays-seasonality-02.faces"
						menuId="essays-seasonality-02" />
					
					<s:secondaryMenuItem
						label="Seasonal rainfall in the Atlantic slaving world"
						href="essays-seasonality-03.faces"
						menuId="essays-seasonality-03" />
				
					<s:secondaryMenuItem
						label="Rainfall, crop type and agricultural calendars"
						href="essays-seasonality-04.faces"
						menuId="essays-seasonality-04" />
				
					<s:secondaryMenuItem
						label="Agricultural calendars and labor requirements"
						href="essays-seasonality-05.faces"
						menuId="essays-seasonality-05" />
					
					<s:secondaryMenuItem
						label="Provisioning-slaving seasons"
						href="essays-seasonality-06.faces"
						menuId="essays-seasonality-06" />
					
					<s:secondaryMenuItem
						label="Slave-trading seasonality: case studies"
						href="essays-seasonality-07.faces"
						menuId="essays-seasonality-07" />
						
					<s:secondaryMenuItem
						label="Trans-Atlantic pathways and harvest cycles"
						href="essays-seasonality-08.faces"
						menuId="essays-seasonality-08" />
	
					<s:secondaryMenuItem
						label="Conclusion"
						href="essays-seasonality-09.faces"
						menuId="essays-seasonality-09" />
				
				</s:secondaryMenuItem>
				
				<s:secondaryMenuItem
					label="Dobo: A Liberated African in Nineteenth-Century Havana"
					href="essays-grandio.faces"
					menuId="essays-grandio"/>

			</s:secondaryMenuItem>
			
			<s:secondaryMenuItem
				label="Vignettes"
				menuId="vignettes">
				
				<s:secondaryMenuItem
					label="Ayuba Suleiman Diallo and Slavery in the Atlantic World"
					href="essays-solomon.faces"
					menuId="essays-solomon"/>
				
				<s:secondaryMenuItem
					label="Catherine  Zimmermann-Mulgrave: A Slave Odyssey"
					href="essays-mulgrave.faces"
					menuId="essays-mulgrave"/>
				
			</s:secondaryMenuItem>

			<s:secondaryMenuItem
				label="Research notes"
				menuId="research-notes">
				
				<s:secondaryMenuItem
					label="Voyages and Applied History"
					href="essays-applied-history.faces"
					menuId="essays-applied-history"/>		
				

			</s:secondaryMenuItem>

		</s:secondaryMenu>
		
	</s:simpleBox>

</t:div>