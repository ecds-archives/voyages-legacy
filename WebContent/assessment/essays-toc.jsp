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
		
		<s:secondaryMenu>
		
			<s:secondaryMenuItem
				label="Interpretations"
				menuId="interpretations">
			
				<s:secondaryMenuItem
					label="A Brief Overview of the Trans-Atlantic Slave Trade"
					href="essays-intro-01.faces"
					menuId="essays-intro">
				
					<s:secondaryMenuItem
						label="Introduction"
						href="essays-intro-01.faces"
						menuId="essays-intro-01" />
					
					<s:secondaryMenuItem
						label="New Products for Trade in the Americas"
						href="essays-intro-02.faces"
						menuId="essays-intro-02" />
					
					<s:secondaryMenuItem
						label="The Enslavement of Africans"
						href="essays-intro-03.faces"
						menuId="essays-intro-03" />
				
					<s:secondaryMenuItem
						label="Currents Driving the Trade"
						href="essays-intro-04.faces"
						menuId="essays-intro-04" />
				
					<s:secondaryMenuItem
						label="African Agency and Resistance"
						href="essays-intro-05.faces"
						menuId="essays-intro-05" />
					
					<s:secondaryMenuItem
						label="Early Slaving Voyages"
						href="essays-intro-06.faces"
						menuId="essays-intro-06" />
					
					<s:secondaryMenuItem
						label="National Sponsors of Slaving Voyages"
						href="essays-intro-07.faces"
						menuId="essays-intro-07" />
						
					<s:secondaryMenuItem
						label="The African Side of the Trade"
						href="essays-intro-08.faces"
						menuId="essays-intro-08" />
	
					<s:secondaryMenuItem
						label="The Middle Passage"
						href="essays-intro-12.faces"
						menuId="essays-intro-12" />
	
					<s:secondaryMenuItem
						label="The Decline of the Slave Trade"
						href="essays-intro-10.faces"
						menuId="essays-intro-10" />
						
					<s:secondaryMenuItem
						label="The Trade’s Influence on Ethnic and Racial Identity"
						href="essays-intro-11.faces"
						menuId="essays-intro-11" />
					
					<s:secondaryMenuItem
						label="Eventual Abolition"
						href="essays-intro-12.faces"
						menuId="essays-intro-12" />

				</s:secondaryMenuItem>
				
			</s:secondaryMenuItem>
			
			<s:secondaryMenuItem
				label="Vignettes"
				menuId="vignettes">
				
				<s:secondaryMenuItem
					label="Job Ben Solomon and the Curse of Slavery in the Atlantic World"
					href="essays-solomon.faces"
					menuId="solomon"/>
				
				<s:secondaryMenuItem
					label="Catherine Zimmermann-Mulgrave: A Message of Hope"
					href="essays-message-of-hope.faces"
					menuId="message-of-hope"/>
				
			</s:secondaryMenuItem>

			<s:secondaryMenuItem
				label="Research notes"
				menuId="research-notes">
				
				<s:secondaryMenuItem
					label="Voyages and Applied History"
					href="essays-applied-history.faces"
					menuId="applied-history"/>
			
			</s:secondaryMenuItem>

		</s:secondaryMenu>
		
	</s:simpleBox>

</t:div>