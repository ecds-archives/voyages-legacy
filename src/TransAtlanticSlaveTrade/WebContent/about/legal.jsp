<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Legal</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/about.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-info.css" rel="stylesheet" type="text/css">
	<link href="../styles/about-team.css" rel="stylesheet" type="text/css">

	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	
	<style type="text/css">
	
		table.persmissions {
			border-collapse: collapse; }
		
		table.persmissions th, table.persmissions td {
			vertical-align: top;
			padding: 0.5em 0.75em;
			border: 1px solid #98A1A9; }
	
		table.persmissions th {
			background-color: #DFE1E2;
			text-align: left; }
			
		table.persmissions td.restrictions ul {
			margin: 0px;
			padding-left: 1.5em; }

		table.persmissions td.restrictions ul li {
			padding: 0px;
			margin: 0px; }
			
		table.persmissions td.restrictions ul li.not-first {
			margin-top: 0.2em; }

		table.persmissions td.restrictions ul li .asset {
			font-weight: bold; }

		table.persmissions td.citations ul {
			margin: 0px;
			padding-left: 1.5em; }

		table.persmissions td.citations ul li {
			margin: 0px;
			padding: 0px; }
			
		table.persmissions td.citations ul li .asset {
			font-weight: bold; }

		table.persmissions td.citations ul li ul li {
			margin-top: 0.2em;
			list-style: square; }

		table.persmissions td.citations ul li ul li .format {
			font-style: italic; }

		table.persmissions td.citations ul li ul li .example {
			font-style: italic; }

	</style>

</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>

	<s:siteHeader activeSectionId="about">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="./index.faces"><h:outputText value="About the Project" /></h:outputLink>
		<h:outputText value="Legal" />
	</s:siteHeader>
	
	<div id="content">
	
		<table border="0" cellspacing="0" cellpadding="0" id="about-layout">
		<tr>
			<td id="about-left-column">
				<%@ include file="about-menu.jsp" %>
			</td>
			<td id="about-right-column">
				<s:simpleBox>
			
					<h1>Legal</h1>

					<p>Items on the <em>Voyages</em> website may be protected by the U.S.
					Copyright Law (Title 17, U.S.C.). Transmission or reproduction of
					copyrighted items beyond that allowed by fair use requires the
					written permission of the copyright owners. Some items may also be
					subject to additional restrictions imposed by the copyright owner
					and/or by Emory University. While information provided below seeks
					to assist with identifying permissions and use restrictions for
					various items, it is the researcher's obligation to determine and
					satisfy copyright or other use restrictions when publishing or
					otherwise distributing materials from this website, including an
					assessment of fair use rights in light of intended use. Please
					contact the <em>Voyages</em> website administrator with any questions about
					the copyright status and any restrictions on the use of particular
					images or texts.</p>

					<h2>Conditions of Use</h2>

					<p>The <em>Voyages</em> website is Open Access, which means the contents of
					this website are made freely available for individual private
					study, scholarship and research, and educational purposes;
					institutions. This also means a signed license agreement is not
					required for access to or use of the database, and a link to
					<em>Voyages</em> can be made from websites and library catalogs with no
					permission required. However, any link to the <em>Voyages</em> website
					should be presented in a manner that does not give the impression
					that Emory University and the <em>Voyages</em> website are making an express
					or implied endorsement of any good or service provided on the
					external site and that the link is presented in a manner that
					clearly indicates that the user is leaving one site and accessing
					another.</p>

					<p>Emory University and the <em>Voyages</em> project team have posted
					the material on this website in an effort to make meaningful
					information widely available, free of charge for personal or
					research purposes, in a manner that they believe is consistent with
					the intentions of the individuals and people who came together to
					create these works. The University and the <em>Voyages</em> project team
					have made diligent efforts to locate and obtain permission to post
					this material from any individual or group that may own rights
					therein. Please contact the <em>Voyages</em> website administrator if you
					have an interest in these materials and have any objection or
					concern with respect to the posting of them.</p>

					<p>An Attribution-Non-Commercial Creative Commons license
					(described below) provides for non-commercial use and
					re-distribution of items and information on the <em>Voyages</em> website
					that may be considered the intellectual property of Emory
					University. Any commercial use is prohibited without the prior
					written permission of Emory University as well as any owners of
					rights in the materials, if applicable. Regarding such permission,
					please contact the <em>Voyages</em> website administrator for additional
					information.</p>

					<h2>Permissions to Reproduce</h2>

					<p>The materials on this site come from a variety of
					repositories and private collections. The low-resolution images
					available on the website are suitable for immediate printing or
					downloading to provide good-quality reference copies for a wide
					range of educational, creative, and research purposes.
					High-resolution copies of images from the collections of Emory
					Libraries are available for licensing for personal use and for
					professional reproduction; permission to reproduce these images may
					be sought from Emory Libraries’ Manuscripts, Archives, and Rare
					Book Library. In many cases, copies of images from external
					repositories and collections are also available, but must be
					obtained from those repositories and collections directly.</p>

					<p>The following chart offers a limited guide to the types of
					intellectual content found on this site and whose permission may
					need to be sought in order to reuse or republish these items in
					another context.</p>

					<table border="0" cellspacing="0" cellpadding="0" class="persmissions">
						<tr>
							<th>TYPE OF CONTENT</th>
							<th>USE RESTRICTIONS</th>
							<th>NOTES</th>
							<th>CITATION EXAMPLE</th>
						</tr>
						<tr>
							<td class="content">CODE</td>
							<td class="restrictions">
								<ul>
									<li class="first"><span class="asset">Open Source:</span> <a href="http://www.gnu.org/licenses/gpl.html">GNU General Public License, v. 3</a> or later</li>
								</ul>
							</td>
							<td class="notes">Code for this site may be found at
							<a href="http://sourceforge.net">SourceForge</a> and
							<a href="http://code.google.com/opensource">Google Code</a>,
							and is made available to support open
							source programming efforts. Visit the GNU website for more
							information on the limits of this license.</td>
							<td class="citations">Not applicable.</td>
						</tr>
						<tr>
							<td class="content">DATA</td>
							<td class="restrictions">
								<ul>
									<li class="first"><span class="asset">Historical data:</span> Public domain (use restrictions do not apply).</li>
									<li class="not-first"><span class="asset">Imputed data:</span> <a href="http://creativecommons.org/licenses/by-nc/3.0/us">Creative Commons Attribution-Noncommercial 3.0 United States License</a>.</li>
								</ul>
							</td>
							<td class="notes">Data drawn from historical documents is
							public domain and therefore not restricted. Imputed data
							(indicated with an asterisk, *, in the Voyages Database, and all
							data in the Estimates Database) is licensed through a Creative
							Commons license. Visit the Creative Commons website for more
							information on the limits of this license.</td>
							<td class="citations">
								<ul>
									<li>
										<div class="asset">Database:</div>
										<ul>
											<li>
												<span class="format">Format:</span>
												[name of specific database used].
												[latest publication year of data, indicated on the Downloads page of the site].
												Voyages: The Trans-Atlantic Slave Trade Database.
												[website URL or permanent link created from data search]
												(accessed Month, Day, Year)
											</li>
											<li>								
												<span class="example">Example 1:</span>
												Voyages Database.
												2009.
												Voyages: The Trans-Atlantic Slave Trade Database.
												<a href="http://www.slavevoyages.org">http://www.slavevoyages.org</a>
												(accessed January 1, 2010).
											</li>
											<li>
												<span class="example">Example 2:</span>
												Estimates Database.
												2009.
												Voyages: The Trans-Atlantic Slave Trade Database.
												<a href="http://slavevoyages.org/tast/assessment/estimates.faces?yearFrom=1501&amp;yearTo=1866&amp;flag=2">http://slavevoyages.org/tast/assessment/estimates.faces?yearFrom=1501&amp;yearTo=1866&amp;flag=2</a>
												(accessed January 1, 2010).
											</li>								
										</ul>
									</li>
								</ul>
							</td>
						</tr>
						<tr>
							<td class="content">IMAGES</td>
							<td class="restrictions">
								<ul>
									<li class="first"><span class="asset">Digitized objects:</span> For permission questions or requests, contact the institution that provided the digital copy and/or holds the original.</li>
									<li class="not-first"><span class="asset">Introductory maps:</span> Contact Yale University Press.</li>
								</ul>
							</td>
							<td class="notes">Contact <a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">the
							<em>Voyages</em> website administrator</a> with questions or requests
							about re-using images contained within the Voyages Database or
							the Estimates Database (graphs, maps, timeline). The <em>Voyages</em>
							logo is only available for approved publicity for the <em>Voyages</em>
							site.</td>
							<td class="citations">
								<ul>
									<li>
									<div class="asset">Image from Images section:</div>
									<ul>
										<li>
											<span class="format">Format:</span>
											[title provided for digitized image in the Images section].
											JPEG.
											Voyages: The Trans-Atlantic Slave Trade Database.
											[URL for Voyages Images Database]
											(accessed Month, Day, Year).
										</li>
										<li>
											<span class="example">Example:</span>
											West and West-Central Africa, c. 1660.
											JPEG.
											Voyages: The Trans-Atlantic Slave Trade Database.
											<a href="http://www.slavevoyages.org/tast/resources/images.faces">http://www.slavevoyages.org/tast/resources/images.faces</a>
											(accessed January 1, 2010).
										</li>									
									</ul>
									</li>
									<li>
										<div class="asset">Introductory Map:</div>
										<ul>
											<li>
												<span class="format">Format:</span>
												[title of map].
												JPEG.
												Voyages: The Trans-Atlantic Slave Trade Database.
												[URL for Introductory Maps section of Voyages website]
												(accessed Month, Day, Year).
											</li>
											<li>
												<span class="example">Example:</span>
												Map 4: Wind and ocean currents of the Atlantic basins.
												JPEG.
												Voyages: The Trans-Atlantic Slave Trade Database.
												<a href="http://www.slavevoyages.org/tast/assessment/intro-maps.faces">http://www.slavevoyages.org/tast/assessment/intro-maps.faces</a>
												(accessed January 1, 2010).
											</li>
										</ul>
									</li>
								</ul>
							</td>
						</tr>
						<tr>
							<td class="content">TEXT</td>
							<td class="restrictions">
							<ul>
								<li class="first"><span class="asset">Essays:</span> Contact individual author for permission to republish.</li>
								<li class="not-first"><span class="asset">Lesson plans:</span> Curriculum and instruction only. Not for commercial republication. Contact individual authors for additional permissions.</li>
								<li class="not-first"><span class="asset">Other text:</span> PDF documents, glossary, FAQs, site text, and other text on the site not attributed to an individual is covered by a <a href="http://creativecommons.org/licenses/by-nc/3.0/us">Creative Commons Attribution-Noncommercial 3.0 United States License</a>.</li>
							</ul>
							</td>
							<td>As with any information or object reused or republished
							in another context, original author or copyright should be
							appropriately credited.</td>
							<td class="citations">
								<ul>
									<li>
										<div class="asset">Essay:</div>
										<ul>
											<li>		
												<span class="format">Format:</span>								
												[author of essay].
												[publication date of essay].
												[title of essay].
												Voyages: The Trans-Atlantic Slave Trade Database.
												[URL to first page of essay in Essays section]
												(accessed Month, Day, Year).
											</li>
											<li>		
												<span class="example">Example 1:</span>
												Behrendt, Stephen D.
												2008.
												Seasonality in the trans-Atlantic slave trade.
												Voyages: The Trans-Atlantic Slave Trade Database.
												<a href="http://www.slavevoyages.org/tast/assessment/essays-seasonality-01.faces">http://www.slavevoyages.org/tast/assessment/essays-seasonality-01.faces</a>
												(accessed January 1, 2010).
											</li>
											<li>		
												<span class="example">Example 2:</span>
												Eltis, David, and Paul F. Lachance.
												2009.
												Estimates of the size and direction of the trans-Atlantic slave trade.
												Voyages: The Trans-Atlantic Slave Trade Database.
												<a href="http://www.slavevoyages.org/downloads/EstimatesMethod.pdf">http://www.slavevoyages.org/downloads/EstimatesMethod.pdf</a>
												(accessed January 1, 2010).
											</li>
										</ul>
									</li>
									<li>
										<div class="asset">Lesson plan:</div>
										<ul>
											<li>
												<span class="format">Format:</span>
												[author of lesson plan].
												[title of lesson plan].
												Voyages: The Trans-Atlantic Slave Trade Database.
												[URL to PDF version of lesson plan]
												(accessed Month, Day, Year).
											</li>
										</ul>
									</li>
								</ul>
							</td>
						</tr>
						<tr>
							<td class="content">VIDEO</td>
							<td class="notes"><a href="http://creativecommons.org/licenses/by-nc/3.0/us">Creative Commons Attribution-Noncommercial 3.0 United States License</a></td>
							<td>&nbsp;</td>
							<td class="citations">
								<ul>
									<li>
										<div class="asset">Demo:</div>
										<ul>
											<li>
												<span class="format">Format:</span>
												Demos:
												[title of demonstration video].
												QuickTime movie.
												Voyages: The Trans-Atlantic Slave Trade Database.
												<a href="http://www.slavevoyages.org">http://www.slavevoyages.org</a>
												(accessed Month, Day, Year).
											</li>
											<li>
												<span class="example">Example:</span>
												Demos:
												Voyages site overview.
												QuickTime movie.
												Voyages: The Trans-Atlantic Slave Trade Database.
												<a href="http://www.slavevoyages.org">http://www.slavevoyages.org</a>
												(accessed January, 1, 2010).
											</li>
										</ul>
									</li>
								</ul>
							</td>
						</tr>
						<tr>
							<td class="content">[Link to Website]</td>
							<td class="notes">Open Access</td>
							<td><em>Voyages</em> is an open access website. No permission or
							license agreement is needed to create a link to this site.</td>
							<td class="citations">Not applicable.</td>
						</tr>
					</table>

					<h2>Licenses</h2>

					<p><em>Voyages: The Trans-Atlantic Slave Trade Database</em> by
					Emory University is licensed under a
					<a href="http://creativecommons.org/licenses/by-nc/3.0/us/">Creative Commons Attribution-Noncommercial 3.0 United States License</a>.
					Forpermissions beyond the scope of this license, please contact
					<a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">the Voyages website administrator</a>.
					Please note that this license only applies to intellectual content
					for which Voyages is the rights holder. See "Permissions to Reproduce" above or contact
					<a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">the Voyages website administrator</a>
					for more information on the types of content on the Voyages site and permissions required for re-use.</p>

					<p><em>Voyages: The Trans-Atlantic Slave Trade Database</em>
					was constructed using open source software components. In honor of
					the open source movement which helps make projects such as this
					possible, and in obligation to its own commitments to its federal
					sponsor, this site licenses its code through a
					<a href="http://www.gnu.org/licenses/gpl.html">GNU General Public License (GPL), version 3</a>
					or any later version. Open source software components of the Voyages website are freely available through
					<a href="http://sourceforge.net">SourceForge</a> and
					<a href="http://code.google.com/opensource">Google Code</a>.</p>

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