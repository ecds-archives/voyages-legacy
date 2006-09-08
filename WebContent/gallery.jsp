<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="main.css" rel="stylesheet" type="text/css">
	<link href="gallery.css" rel="stylesheet" type="text/css">
	<script src="gallery.js" language="javascript" type="text/javascript"></script>
	<title>Images gallery</title>
</head>

<body>
<f:view>


	<div class="header">
		<img src="header-text.png" width="600" height="40" border="0" alt="TAST">
	</div>
	<div class="header-path">
		<a href="search.faces">Database search</a> |
		<a href="reports.faces">Precompiled reports</a> |
		<a href="gallery.faces">Images database</a>
	</div>

	<h:form id="form">

		<div align="center" id="gallery-div-lists">
		
		<t:htmlTag rendered="#{PicturesBean.galleryList}" value="table" style="border-collapse: collapse;">
			<t:htmlTag value="tr">
				<t:htmlTag value="td" style="margin-right: 30px;">
					
					<% /** Available groups of pictures **/ %>
					<h:outputText value="Galleries:" id="gal-main-text"/>
					<t:htmlTag value="br" id="esc-1"/>
					<h:outputLink value="gallery.faces?gal=people" id="gal-people">
						<h:outputText value="People"/>
					</h:outputLink>
					<t:htmlTag value="br" id="esc-2"/>
					<h:outputLink value="gallery.faces?gal=ports" id="gal-ports">
						<h:outputText value="Ports"/>
					</h:outputLink>
					<t:htmlTag value="br" id="esc-3"/>
					<h:outputLink value="gallery.faces?gal=regions" id="gal-regions">
						<h:outputText value="Regions"/>
					</h:outputLink>
					<t:htmlTag value="br" id="esc-4"/>
					<h:outputLink value="gallery.faces?gal=ships" id="gal-ships">
						<h:outputText value="Ships"/>
					</h:outputLink>
				</t:htmlTag>
				
				<t:htmlTag value="td">
				
					<% /** Available entities in chosen group **/ %>
					<t:div rendered="#{PicturesBean.galleryUserName != null}">
						<h:outputText value="Images of #{PicturesBean.galleryUserName}" />
						<t:htmlTag value="br"/>
						<h:outputText value="Quick search: " />
						<h:inputText id="gallery-quick-search" onkeyup="filterItems('form:gallery-list-objects', this); return false;"/>
						<t:htmlTag value="br" id="gallery-list-br1"/>
						<h:selectOneListbox size="10" style="width: 300px;"
									value="#{PicturesBean.visibleImage}" id="gallery-list-objects">
							<f:selectItems value="#{PicturesBean.currentObjects}"/>
						</h:selectOneListbox>
						<t:htmlTag value="br"/>
						<h:commandButton value="Show images" 
							onclick="var id = document.forms['form'].elements['form:gallery-list-objects'].value; if (id == null || id == '') {return false;};window.location='gallery.faces?obj=#{PicturesBean.gallery}&id=' + id; return false;"/>
					</t:div>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
		</div>

		<t:htmlTag rendered="#{!PicturesBean.galleryList}" value="div">
			<t:div id="gallsery-div-back-to-list" style="text-align: center;">
				<h:outputLink value="gallery.faces?gal=#{PicturesBean.lastGalleryName}">
					<h:outputText value="Back to gallery list"/>
				</h:outputLink>
			</t:div>
			<s:picture-gallery pictures="#{PicturesBean.pictureGalery}" rows="6" columns="1"
				thumbnailWidth="100" thumbnailHeight="100" action="#{PicturesBean.showinfo}" 
			 	searchCondition="#{PicturesBean.searchCondition}" />
		 </t:htmlTag>

	
	</h:form>
	
</f:view>
</body>
</html>
