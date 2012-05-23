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
	<link href="../styles/help-main.css" rel="stylesheet" type="text/css">
	<link href="../styles/help-legal.css" rel="stylesheet" type="text/css">
	
</head>
<body>
<f:view>
<h:form id="form">

	<f:loadBundle basename="resources" var="res"/>
	
	<f:param value="legal" binding="#{HelpMenuBean.activeSectionParam}" />
	<%@ include file="top-bar.jsp" %>
	
	<div id="help-section-title"><img src="../images/help-legal-title.png" width="240" height="50" border="0" alt="Legal"></div>
	
	<div class="content">

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
		study, scholarship and research, and educational purposes. This also
		means a signed license agreement is not
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
	
		<p>An <a href="http://creativecommons.org/licenses/by-nc/3.0/" target="_blank">Attribution-Non-Commercial
		Creative Commons license</a> provides for non-commercial use and
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
		be sought from Emory Libraries’ 
		<a href="http://marbl.library.emory.edu/reproduction-options.html" target="_blank">Manuscripts, Archives, and Rare Book Library</a>.
		In many cases, copies of images from external
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
			<th>CITATION EXAMPLE</th>
			<th>NOTES</th>
		</tr>
		<tr>
			<td class="content">CODE</td>
			<td class="restrictions"><a href="http://www.gnu.org/licenses/gpl.html" target="_blank">GNU General Public License, v. 3</a> or later</td>
			<td class="citations">Not applicable.</td>
			<td class="notes">Code for this site may be found at
			<a href="https://github.com/emory-libraries/voyages" target="_blank">GitHub</a>,
			and is made available to support open
			source programming efforts. Visit the GNU website for more
			information on the limits of this license.</td>
		</tr>
		<tr>
			<td class="content" rowspan="2">DATA</td>
			<td class="restrictions">
				<div class="asset">Historical data</div>
				Public domain (use restrictions do not apply).
			</td>
			<td class="citations">
				<div class="format">
					<span class="format">Format:</span>
					[name of specific database used].
					[latest publication year of data, indicated on the Downloads page of the site].
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					[website URL or permanent link created from data search]
					(accessed Month, Day, Year).
				</div>
				<div class="example">
					<span class="example">Example:</span>
					Voyages Database.
					2009.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://www.slavevoyages.org" target="_blank">http://www.slavevoyages.org</a>
					(accessed January 1, 2010).
				</div>
			</td>
			<td class="notes" rowspan="2">Data drawn from historical documents is
			public domain and therefore not restricted. Imputed data
			(indicated with an asterisk, *, in the Voyages Database, and all
			data in the Estimates Database) is licensed through a Creative
			Commons license. Visit the Creative Commons website for more
			information on the limits of this license.</td>
		</tr>
		<tr>
			<td class="restrictions">
				<div class="asset">Imputed data</div>
				<a href="http://creativecommons.org/licenses/by-nc/3.0/us" target="_blank">Creative Commons Attribution-Noncommercial 3.0 United States License</a>.
			</td>
			<td class="citations">
				<div class="format">
					<span class="format">Format:</span>
					[name of specific database used].
					[latest publication year of data, indicated on the Downloads page of the site].
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					[website URL or permanent link created from data search]
					(accessed Month, Day, Year).
				</div>
				<div class="example">
					<span class="example">Example:</span>
					Estimates Database.
					2009.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://slavevoyages.org/tast/assessment/estimates.faces?yearFrom=1501&amp;yearTo=1866&amp;flag=2" target="_blank">http://www.slavevoyages.org/tast/assessment/estimates.faces?yearFrom=1501&amp;yearTo=1866&amp;flag=2</a>
					(accessed January 1, 2010).
				</div>								
			</td>
			
		</tr>
		<tr>
			<td class="content" rowspan="2">IMAGES</td>
			<td class="restrictions">
				<div class="asset">Digitized objects</div>
				For permission questions or requests, contact the institution that provided the digital copy and/or holds the original.
			</td>
			<td class="citations">
				<div class="format">
					<span class="format">Format:</span>
					[title provided for digitized image in the Images section].
					JPEG.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					[URL for Voyages Images Database]
					(accessed Month, Day, Year).
				</div>
				<div class="example">
					<span class="example">Example:</span>
					West and West-Central Africa, c. 1660.
					JPEG.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://www.slavevoyages.org/tast/resources/images.faces" target="_blank">http://www.slavevoyages.org/tast/resources/images.faces</a>
					(accessed January 1, 2010).
				</div>									
			</td>
			<td class="notes" rowspan="2">Contact <a href="mailto:&#118;&#111;&#121;a&#103;&#101;s&#64;&#101;&#109;o&#114;&#121;&#46;&#101;d&#117;">the
			<em>Voyages</em> website administrator</a> with questions or requests
			about re-using images contained within the Voyages Database or
			the Estimates Database (graphs, maps, timeline). The <em>Voyages</em>
			logo is only available for approved publicity for the <em>Voyages</em>
			site.</td>
		</tr>
		<tr>
			<td class="restrictions">
				<div class="asset">Introductory Maps</div>
				Contact Yale University Press.
			</td>
			<td class="citations">
				<div class="format">
					<span class="format">Format:</span>
					[title of map].
					JPEG.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					[URL for Introductory Maps section of Voyages website]
					(accessed Month, Day, Year).
				</div>
				<div class="example">
					<span class="example">Example:</span>
					Map 4: Wind and ocean currents of the Atlantic basins.
					JPEG.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://www.slavevoyages.org/tast/assessment/intro-maps.faces" target="_blank">http://www.slavevoyages.org/tast/assessment/intro-maps.faces</a>
					(accessed January 1, 2010).
				</div>
			</td>
		</tr>
		<tr>
			<td class="content" rowspan="3">TEXT</td>
			<td class="restrictions">
				<div class="asset">Essays</div>
				Contact individual author for permission to republish.
			</td>
			<td class="citations">
				<div class="format">
					<span class="format">Format:</span>								
					[author of essay].
					[publication date of essay].
					[title of essay].
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					[URL to first page of essay in Essays section]
					(accessed Month, Day, Year).
				</div>
				<div class="example">		
					<span class="example">Example 1:</span>
					Behrendt, Stephen D.
					2008.
					Seasonality in the trans-Atlantic slave trade.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://www.slavevoyages.org/tast/assessment/essays-seasonality-01.faces" target="_blank">http://www.slavevoyages.org/tast/assessment/essays-seasonality-01.faces</a>
					(accessed January 1, 2010).
				</div>
				<div class="example">
					<span class="example">Example 2:</span>
					Eltis, David, and Paul F. Lachance.
					2009.
					Estimates of the size and direction of the trans-Atlantic slave trade.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://www.slavevoyages.org/downloads/estimates-method.pdf" target="_blank">hhttp://www.slavevoyages.org/downloads/estimates-method.pdf</a>
					(accessed January 1, 2010).
				</div>
			</td>
			<td rowspan="3">As with any information or object reused or republished
			in another context, original author or copyright should be
			appropriately credited.</td>
		</tr>
		<tr>
			<td class="restrictions">
				<div class="asset">Lesson plans</div>
				Curriculum and instruction only. Not for commercial republication. Contact individual authors for additional permissions.
			</td>
			<td class="citations">
				<div class="format">
					<span class="format">Format:</span>
					[author of lesson plan].
					[title of lesson plan].
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					[URL to PDF version of lesson plan]
					(accessed Month, Day, Year).
				</div>
			</td>
		</tr>
		<tr>
			<td class="restrictions">
				<div class="asset">Other text</div>
				PDF documents, glossary, FAQs, site text, and other text on the site not attributed to an individual is covered by a <a href="http://creativecommons.org/licenses/by-nc/3.0/us" target="_blank">Creative Commons Attribution-Noncommercial 3.0 United States License</a>.
			</td>
			<td></td>
		</tr>
		<tr>
			<td class="content">VIDEO</td>
			<td class="notes"><a href="http://creativecommons.org/licenses/by-nc/3.0/us" target="_blank">Creative Commons Attribution-Noncommercial 3.0 United States License</a></td>
			<td class="citations">
				<div class="format">
					<span class="format">Format:</span>
					Demos:
					[title of demonstration video].
					QuickTime movie.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://www.slavevoyages.org" target="_blank">http://www.slavevoyages.org</a>
					(accessed Month, Day, Year).
				</div>
				<div class="example">
					<span class="example">Example:</span>
					Demos:
					Voyages site overview.
					QuickTime movie.
					<i>Voyages: The Trans-Atlantic Slave Trade Database.</i>
					<a href="http://www.slavevoyages.org" target="_blank">http://www.slavevoyages.org</a>
					(accessed January, 1, 2010).
				</div>
			</td>
			<td></td>
		</tr>
		<tr>
			<td class="content">[Link to Website]</td>
			<td class="notes">Open Access</td>
			<td class="citations">Not applicable.</td>
			<td><em>Voyages</em> is an open access website. No permission or
			license agreement is needed to create a link to this site.</td>
		</tr>
		</table>
	
		<h2>Licenses</h2>
	
		<p><em>Voyages: The Trans-Atlantic Slave Trade Database</em> by
		Emory University is licensed under a
		<a href="http://creativecommons.org/licenses/by-nc/3.0/us/" target="_blank">Creative Commons Attribution-Noncommercial 3.0 United States License</a>.
		For permissions beyond the scope of this license, please contact
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
		<a href="http://www.gnu.org/licenses/gpl.html" target="_blank">GNU General Public License (GPL), version 3</a>
		or any later version. Open source software components of the Voyages website are freely available through
		<a href="https://github.com/emory-libraries/voyages" target="_blank">GitHub</a>.</p>
	
	</div>

</h:form>
	
</f:view>

<%@ include file="../google-analytics.jsp" %>

</body>
</html>