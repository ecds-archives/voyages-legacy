<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Contribute</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Contribute" />
	</s:siteHeader>
	
	<div id="content">
	
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<%@ include file="submission-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>
				
					<h1>Guidelines for Contributors</h1>
					
					<p>Every three years, beginning 1 September 2011, an editorial board will produce 
					a revised edition of Voyages, incorporating new slaving voyage information located 
					in primary or secondary sources. Contributions may concern new voyages not currently 
					in the database, or details on some of the 34,941 ventures searchable in the set. 
					Voyages functions like an academic journal: all contributions must proceed through 
					a peer-reviewed editorial process, and must therefore be documented thoroughly to 
					enable the editors to verify the information. All contributors will be acknowledged 
					by name in the Voyages section “About the Project.”	</p>
					
					<p>Users may locate the Contribute section under “The Database” in the top navigation 
					menu. Each user will be allocated a unique username and password. Once logged in, users 
					will be given the option to make three types of contributions:</p>
					
					<ul>
						<li>New voyage</li>
						<li>Edit an existing voyage</li>
						<li>Merge existing voyages</li>
					</ul>
					
					<p><span style="text-decoration: underline;">Important</span>: Users need to search the Voyages Database to determine whether they have 
					located a new voyage. The editors recommend that users search names of ships and captains, 
					and should keep in mind that variant spellings occur in the documentary record.</p>
					
					<p>When users choose “Edit an existing voyage” or “Merge existing voyages” they will need 
					to provide the <span style="font-weight:bolder;">Voyage Identification number(s)</span>. After choosing a contribution category, 
					users will be taken step-by-step through each data variable to add new voyage details.</p>
					
					<p>Buttons at the top of the screen track your progress through the data contribution 
					process and allow you to return to previous steps to add more information. You can also move 
					forward and backward through the submission process using the “Next” and “Previous” buttons.</p>
					
					<p><span style="text-decoration: underline;">Important</span>: Do not use the browser “Back” button to return to a previous step. Information 
					entered into the data submission form cannot be saved for completion at a later time. </p>
					
					<p>The final screen, “Specify sources for your contribution,” prompts contributors to identify 
					their primary or secondary source(s). Please provide the editorial team with complete source details.</p>
					
					<p>1) New voyage</p>
					
					<p>The Voyages Database contains information on 34,941 trans-Atlantic slaving voyages, 1514-1866. The 
					editors estimate that the file contains 90-95% of all transatlantic ventures, and there are thus 
					between 1,750 and 3,500 slaving voyages not recorded in the set.</p>
					
					<p>Many of the “missing” slave-trading ventures departed from Brazil or Lisbon in 1520-1670, a period 
					when few sources survive.  Smaller gaps in the database likely occur for the:</p>
					
					<ul>
						<li>British trade before 1662</li>
						<li>British trade, 1689-1698</li>
						<li>French trade before 1708</li>
						<li>Spanish trade before 1641</li>
						<li>North America/United States trade before 1808</li>
					</ul>
					
					<p>A complete listing of sources employed in creating the set may be found in the “Sources” section, 
					under the menu “The Database” and submenu “Understanding the Database.”</p>
					
					<p>Enter data for new voyages corresponding to the appropriate variable by either typing the data in 
					a text box or selecting the appropriate data from a drop-down menu of values. For every new item of 
					data entered about a voyage, you may choose to add a note that provides some explanation of that data 
					(for example: how the data originally appeared in the source; discrepancies across sources; measurements 
					used for quantifiable data).</p>
					
					<p>2) Edit an existing voyage</p>
					
					<p>For many of the 34,941 voyages documented in the set, users may locate additional information on 
					sailing dates, markets, ship details, or data on numbers of slaves or slave characteristics. Choosing 
					this contribution category requires the Voyage Identification Number. After entering the Voyage ID, 
					click the “Lookup…” button. Three basic details about the voyage will appear on the screen: the 
					Captain’s name, Ship name and Year of Arrival. Confirm that you have retrieved the correct voyage 
					by clicking the appropriate button. The Edit voyage information screen that appears presents nine 
					groupings of variables, such as “Ship, nation, owners” or “Voyage outcome.” By clicking on these 
					labels, users retrieve the full list of editable voyage details. Two columns appear. The first column 
					contains the current voyage information. For many variables that information is “unknown.” The second 
					column, the contributors’ column, allows new information to be entered. Contributors also can add supplemental 
					information or any queries in the “Add note” box, one of which appears below each variable.</p>
					
					<p>In editing an existing voyage, users may have located information that warrants removing the voyage 
					from the dataset.  Some voyages bound to “Africa,” for example, may have been non-slavers.</p>
					
					<p>3) Merge existing voyages</p>
					
					<p>A user who chooses to merge voyages has detected two or more partially-documented voyages that 
					overlap – they include similar voyage details that suggest they may be the same voyage. As with the 
					contribution category “Edit an existing voyage”, clicking on the Merge voyages button in step 1 first 
					brings up a screen for selecting a voyage. Enter the appropriate Voyage ID number in the text box, then 
					click the “Lookup…” button. The ship, captain and year of arrival appear on the screen. Confirm that you 
					have retrieved the correct voyage by clicking the appropriate button.</p>

					<p>Once you have confirmed the first voyage, a summary of this voyage will remain on the screen and a new 
					text box will appear for selecting the next voyage. Enter the appropriate Voyage ID number in this text box, 
					then click the “Lookup…” button. You may select up to five voyages. To remove previously selected voyages from 
					the merge list, simply click the “Remove” link. Once you have retrieved all voyages for merging, click the Next 
					button.</p>
					
					<p>The merge screen presents a table of variables and voyage data, with columns showing data for existing 
					voyages and a final column providing space for merging that data. Click the “Copy>” link under the correct 
					data value for one of the existing voyages or enter new data using the textbox or drop-down menu in the 
					“Changed voyage” column. For every detail entered about a voyage, you may choose to add a note that provides 
					some explanation of that data (for example, why the name of the slave ship may have appeared under different 
					names and thus lead to the assumption of different voyages).</p>
					
					<h1>Reminders</h1>
					
					<p>Have you checked whether your potential slaving venture is included in the Voyages Database?</p>
					
					<p>If “Yes,” make a note of the <span style="font-weight:bolder;">Voyage Identification Number</span> before entering the Contributors’ page, 
					as you will need to enter this number when editing or merging existing voyages.</p>
					
					<p>Have you checked whether your source(s) are included in the Voyages Database?</p>
					
					<p>Thank you for your time and contribution.</p>
										
				</s:simpleBox>
			</td>
		</tr>
	</table>
	
	</div>
</h:form>
</f:view>
</body>
</html>