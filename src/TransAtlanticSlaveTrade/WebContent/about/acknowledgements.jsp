<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Acknowledgements</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-acks.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>

	<s:siteHeader activeSectionId="about">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="./index.faces"><h:outputText value="About the Project" /></h:outputLink>
		<h:outputText value="Acknowledgements" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<% /* <f:param value="essays-solomon" binding="#{EssaysBean.paramActiveMenuId}" /> */ %>
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>Acknowledgements</h1>
					
					<h2>SPONSORS</h2>
					
					<ul class="sponsors">

						<li>
						
							<img class="sponsor-logo" src="logos/neh.png" width="150" height="105" alt="National Endowment for the Humanities" border="0" />
						
							<div class="sponsor-text">
							
								<div class="sponsor-name"><a href="http://www.neh.gov">National Endowment for the Humanities</a></div>
								
								<div class="sponsor-desc">The National Endowment for the Humanities (NEH) is an
								independent grant-making agency of the United States government
								dedicated to supporting research, education, preservation, and
								public programs in the humanities. Each year the NEH designates a
								portion of its grants as "We the People" projects -- a special
								recognition by the NEH for model projects that advance the study,
								teaching, and understanding of American history and culture. In
								addition to its financial support, the NEH has honored the Expanded
								Online Trans-Atlantic Slave Trade Database project by designating
								it a "We the People" project.</div>
							
							</div>
						
						</li>
						<li>
	
							<img class="sponsor-logo" src="../images/blank.png" width="150" height="45" alt="Emory University" border="0" />

							<div class="sponsor-text">
							
								<div class="sponsor-name"><a href="http://dubois.fas.harvard.edu">W.E.B. Du Bois Institute for African and African American Research</a></div>

								<div class="sponsor-desc">Through fellowships to scholars, sponsorship of a range
								of cultural and educational events and projects, and affiliation
								with other outreach programs, the W.E.B. Du Bois Institute for
								African and African American Research advances study and
								understanding of the African diasporic experience. The Institute
								sponsored the development of the Trans-Atlantic Slave Trade
								database (published as a CD-ROM in 1999 by Cambridge University
								Press), and has contributed funding for the dataset of the Voyages
								database.</div>

							</div>
							
						</li>
						<li>
	
							<img class="sponsor-logo" src="logos/emory-university.png" width="150" height="45" alt="Emory University" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.emory.edu">Emory University</a></div>
			
								<div class="sponsor-desc">Emory University is internationally recognized as an
								inquiry-driven, ethically engaged, and diverse community, whose
								members work collaboratively for positive transformation in the
								world through courageous leadership in teaching, research,
								scholarship, health care, and social action. Emory University
								Libraries have played a leadership role in building a national
								digital library network, supporting innovative technology
								initiatives, and developing premier research collections and
								instructional programs that make the library a destination for
								students and scholars.</div>
								
							</div>
						
						</li>
					
					</ul>
					
					<h2>INSTITUTIONAL PARTNERS</h2>
					
					<ul class="sponsors">
					
						<li>		
					
							<img class="sponsor-logo" src="logos/emory-libraries.png" width="150" height="45" alt="Emory University" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://web.library.emory.edu">Emory University Libraries</a></div>
			
								<div class="sponsor-desc">Since 2001 the Emory University Libraries has
								distinguished itself as a leader in digital information technology,
								with digital innovations ranging from tools and resources for
								interdisciplinary digital scholarship to networks and processes for
								archiving and preserving digital cultural heritage materials. Its
								Manuscript, Archives and Rare Book Library (MARBL) possesses a
								strong collection of materials relating to African American history
								and culture and Southern History. The expanded online
								trans-Atlantic slave trade database will be stored at Emory
								University’s Robert W. Woodruff Library, under the direction of
								co-PI Martin Halbert, with additional copies replicated at partner
								institutions, under the direction of members of the Steering
								Committee.</div>
								
							</div>

						</li>
						<li>		
		
							<img class="sponsor-logo" src="logos/hull.png"
							width="150" height="75" alt="The Wilberforce Institute for the Study of Slavery and Emancipation (WISE) at The University of Hull" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.hull.ac.uk/wise">The Wilberforce Institute for the Study of Slavery and Emancipation (WISE) at The University of Hull</a></div>
			
								<div class="sponsor-desc">The University of Hull addresses the ongoing concerns of
								slavery and social justice through the work of the Wilberforce
								Institute. This Institute traces connections between slavery and
								contemporary human rights issues through three main themes of
								research: the past in the present, movement and identity, and
								boundaries of freedom and coercion. A digital copy of the expanded
								online database will be stored at The University of Hull, under the
								direction of Steering Committee member David Richardson.</div>
							
							</div>
							
						</li>
						<li>		
		
							<img class="sponsor-logo" src="logos/york.png"
							width="150" height="50" alt="The Harriet Tubman Institute for Research on the Global Migrations of African Peoples at York University" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.yorku.ca/tubman/Home/index.html">The Harriet Tubman Institute for Research on the Global Migrations of African Peoples at York University</a></div>
			
								<div class="sponsor-desc">The Tubman Institute at York University researches the forced 
								and voluntary movement of African peoples around the world, as part of its mission to 
								promote a greater understanding of the history of slavery and its legacy. A digital 
								copy of the expanded online database will be stored at York University, under the 
								direction of Advisory Board member Paul Lovejoy.</div>
								
							</div>
		
						</li>
						<li>		

							<img class="sponsor-logo" src="logos/ufrj.png"
							width="150" height="100" alt="Universidade Federal do Rio de Janeiro" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.ufrj.br">Universidade Federal do Rio de Janeiro</a></div>
			
								<div class="sponsor-desc">The largest federal university in Brazil, the Federal University 
								of Rio de Janeiro boasts an international reputation for its work in slavery and slave trade 
								research. It hosts the largest concentration of slave-trade specialists in Brazil, as well 
								as a journal that has become a major Portuguese language outlet for research on slavery and 
								the slave trade in the Atlantic world. A digital copy of the Voyages Database will be stored 
								at the Federal University of Rio de Janeiro, under the direction of Steering Committee member 
								Manolo Florentino.</div>
								
							</div>
		
						</li>
						<li>		

							<img class="sponsor-logo" src="logos/victoria.png"
							width="150" height="75" alt="Victoria University of Wellington" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.vuw.ac.nz">Victoria University of Wellington</a></div>
			
								<div class="sponsor-desc">Victoria University of Wellington, New Zealand, founded in 1897, enjoys an 
								international reputation for humanities research. It is home to the Treaty of Waitangi Research Unit 
								(TOWRU) at the Stout Research Centre for New Zealand Studies, which contributes to the resolution of 
								Maori claims under the Treaty of Waitangi (1840), to equitable and harmonious ethnic relationships 
								within Aotearoa/New Zealand, and to international comparative scholarship on the historical (especially 
								coercive) relationships between states and indigenous peoples. The Victoria University Library holds the 
								largest collection of slave-trade materials in Australasia. A digital copy of the Voyages Database will 
								be stored at Victoria University of Wellington, under the direction of Steering Committee member 
								Stephen D. Behrendt. </div>
								
							</div>
					
						</li>			
						<li>		

							<img class="sponsor-logo" src="logos/cambridge-press.png"
							width="150" height="45" alt="Cambridge University Press" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.cambridge.org/americas">Cambridge University Press</a></div>
			
								<div class="sponsor-desc">Cambridge University Press of New York, the publishers of the 1999 CD-ROM, 
								The Trans-Atlantic Slave Trade: A Database on CD-ROM, provided the Voyages project with full access 
								to the programming code and allowed us to draw on sections of the Introduction that appeared in this 
								original publication.</div>
								
							</div>
					
						</li>		
						<li>

							<img class="sponsor-logo" src="logos/yale-press.png"
							width="150" height="70" alt="Yale University Press" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://yalepress.yale.edu">Yale University Press</a></div>
			
								<div class="sponsor-desc">By publishing serious works that contribute to a global
								understanding of human affairs, Yale University Press aids in the
								discovery and dissemination of light and truth, lux et veritas,
								which is a central purpose of Yale University. The publications of
								the Press are books and other materials that further scholarly
								investigation, advance interdisciplinary inquiry, stimulate public
								debate, educate both within and outside the classroom, and enhance
								cultural life.</div>
								
							</div>
		
						</li>
						<li>		

							<img class="sponsor-logo" src="logos/gilder-lehrman.png"
							width="150" height="45" alt="The Gilder Lehrman Institute of American History" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.gilderlehrman.org">The Gilder Lehrman Institute of American History</a></div>
			
								<div class="sponsor-desc">The Gilder Lehrman Institute promotes and sponsors
								events, projects, and publications that celebrate and teach
								American history. Through its online site educators, researchers,
								and the general public may discover a range of primary and
								secondary materials, lectures and workshops, and a host of
								opportunities for re-discovering America's past.</div>
							
							</div>

						</li>
						<li>		

							<img class="sponsor-logo" src="logos/mapping-specialists.png"
							width="150" height="30" alt="Mapping Specialists" border="0" />

							<div class="sponsor-text">

								<div class="sponsor-name"><a href="http://www.mappingspecialists.com">Mapping Specialists</a></div>
			
								<div class="sponsor-desc">Established in 1984, Mapping Specialists, Ltd. has
								gained an industry-wide reputation as a leader in accuracy,
								quality, and service by offering a broad range of graphic services.
								Located in Madison, Wisconsin, a highly regarded center of
								cartographic resources, it draws on a well-educated, skilled
								workforce and extensive local research facilities to produce
								cartographic resources.</div>
								
							</div>
							
						</li>
						
					</ul>
					
					<h2>SPECIAL THANKS</h2>

					<p>Many people contributed to the successful completion of this
					project – enough that we fear in our attempts to acknowledge them,
					we may only call attention to having forgotten someone and their
					worthy assistance in our efforts. With apologies for any
					regrettable omissions and our sincerest thanks to everyone, named
					or anonymous, who graciously gave their time and expertise, we
					would like to especially acknowledge the following individuals.</p>

					<p>Many individuals at Emory University’s Robert W. Woodruff Library 
					took time outside of their already busy schedules and routine work in 
					order to answer our questions, assist with various project tasks, and 
					generally support the life of the project. Rob Riley’s early advice on 
					and examples of database systems sparked ideas for our own database. 
					Naomi Nelson and Teresa Burk of MARBL (<a href="http://marbl.library.emory.edu">Manuscript, Archives, and Rare 
					Book Library</a>) helped locate and prepare rare materials for digitizing 
					as well as for various presentations on the project. Anne Frellsen, of 
					the <a href="http://web.library.emory.edu/preservation">Preservation Office at Emory University Libraries</a>, assisted also with 
					digitizing efforts and met with members of our team to discuss best practices 
					for photographing and digitizing historical artifacts. Lisa Macklin’s advice 
					on copyright helped guide the collection of images for the site. At the 
					insistence of Lea McLees, the library’s Director of Communications, we 
					documented our work on the project – a task greatly aided by Francine Thurston, 
					Matt Miller, and Sarah Toton, who all gave generously of their time and abilities 
					in photography, videography, and video editing, respectively. Jim Kruse, 
					Shannon O’Daniel, and other members of both <a href="http://cet.emory.edu/ecit/">Emory’s Center for Interactive 
					Teaching (ECIT)</a> and <a href="http://it.emory.edu/showdoc.cfm?docid=8129">University Technology Services (UTS)</a> regularly and 
					graciously gave their time, tools and resources, and expertise to help 
					ensure that all team members – both on site and off – could fully participate 
					in project meetings. Finally, Ginger Cain’s enthusiasm and energy for sharing 
					information about the project continues to put us in touch with new people 
					interested in the project and new possibilities for continuing this work.</p>		

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