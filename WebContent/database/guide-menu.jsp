<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div styleClass="left-menu-box">

	<s:simpleBox>
	
		<t:div styleClass="left-menu-title">
			<h:outputText value="UNDERSTANDING THE DATABASE" />
		</t:div>
		
		<s:secondaryMenu>
		
			<s:secondaryMenuItem
				label="Guide"
				menuId="guide"
				href="guide.faces"/>			
			
			<s:secondaryMenuItem
				label="Methodology"
				menuId="methodology"
				href="methodology-01.faces">
				
				<s:secondaryMenuItem
					label="Introduction"
					menuId="methodology-01"
					href="methodology-01.faces"/>
				<s:secondaryMenuItem
					label="Coverage of the Slave Trade"
					menuId="methodology-02"
					href="methodology-02.faces"/>				
				<s:secondaryMenuItem
					label="Nature of Sources"
					menuId="methodology-03"
					href="methodology-03.faces"/>
				<s:secondaryMenuItem
					label="Cases and Variables"
					menuId="methodology-04"
					href="methodology-04.faces"/>
				<s:secondaryMenuItem
					label="Data Variables"
					menuId="methodology-05"
					href="methodology-05.faces"/>
				<s:secondaryMenuItem
					label="Dates"
					menuId="methodology-06"
					href="methodology-06.faces"/>
				<s:secondaryMenuItem
					label="Names"
					menuId="methodology-07"
					href="methodology-07.faces"/>
				<s:secondaryMenuItem
					label="Imputed Variables"
					menuId="methodology-08"
					href="methodology-08.faces"/>
				<s:secondaryMenuItem
					label="Geographic Data"
					menuId="methodology-09"
					href="methodology-09.faces"/>
				<s:secondaryMenuItem
					label="Imputed Voyage Dates"
					menuId="methodology-10"
					href="methodology-10.faces"/>
				<s:secondaryMenuItem
					label="Classification as a Trans-Atlantic Slaving Voyage"
					menuId="methodology-11"
					href="methodology-11.faces"/>
				<s:secondaryMenuItem
					label="Voyage Outcomes"
					menuId="methodology-12"
					href="methodology-12.faces"/>
				<s:secondaryMenuItem
					label="Inferring Places of Trade"
					menuId="methodology-13"
					href="methodology-13.faces"/>
				<s:secondaryMenuItem
					label="Imputing Numbers of Slaves"
					menuId="methodology-14"
					href="methodology-14.faces"/>
				<s:secondaryMenuItem
					label="Regions of Embarkation and Disembarkation"
					menuId="methodology-15"
					href="methodology-15.faces"/>
				<s:secondaryMenuItem
					label="Age and Gender Ratios"
					menuId="methodology-16"
					href="methodology-16.faces"/>
				<s:secondaryMenuItem
					label="National Carriers"
					menuId="methodology-17"
					href="methodology-17.faces"/>
				<s:secondaryMenuItem
					label="Tonnage"
					menuId="methodology-18"
					href="methodology-18.faces"/>
				<s:secondaryMenuItem
					label="Resistance and Price of Slaves"
					menuId="methodology-19"
					href="methodology-19.faces"/>
				<s:secondaryMenuItem
					label="Appendix"
					menuId="methodology-20"
					href="methodology-20.faces"/>
				<s:secondaryMenuItem
					label="Notes"
					menuId="methodology-21"
					href="methodology-21.faces"/>															
			</s:secondaryMenuItem>								
		
			<s:secondaryMenuItem
				label="Variable List"
				menuId="variables"
				href="variables.faces" />
		
			<s:secondaryMenuItem
				label="Sources"
				menuId="sources"
				href="sources.faces?type=documentary">
			
				<s:secondaryMenuItem
					label="Documentary Sources"
					menuId="sources-documentary"
					href="sources.faces?type=documentary" />
				
				<s:secondaryMenuItem
					label="Newspapers"
					menuId="sources-newspapers"
					href="sources.faces?type=newspapers"  />
				
				<s:secondaryMenuItem
					label="Published Sources"
					menuId="sources-published"
					href="sources.faces?type=published" />
				
				<s:secondaryMenuItem
					label="Unpublished Secondary Sources"
					menuId="sources-unpublished"
					href="sources.faces?type=unpublished" />
				
				<s:secondaryMenuItem
					label="Private Notes and Collections"
					menuId="sources-private"
					href="sources.faces?type=private" />
			
			</s:secondaryMenuItem>
		
		</s:secondaryMenu>
		
	</s:simpleBox>

</t:div>