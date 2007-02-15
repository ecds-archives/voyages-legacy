<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<s:pictures images="#{NewImagesBean.queryResponse}" 
				columnsCount="5"
				thumbnailHeight="100" 
				thumbnailWidth="100"
				action="#{NewImagesBean.detailRequested}" 
				selectedImageId="#{NewImagesBean.imageId}" />