
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Sources for submission</title>
	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/submission.css" rel="stylesheet" type="text/css">
	<link href="../admin/main.css" rel="stylesheet" type="text/css">
</head>
<body style="margin: 15px;">
<f:view>
<h:form id="main">

	<table cellpadding="0" cellspacing="0">
		<tr>
			<td class="step-indicator-left">Type of contribution</td>
			<td class="step-indicator-middle">Your voyage information</td>
			<td class="step-indicator-active-middle">Your sources</td>
			<td class="step-indicator-middle">Verify contribution</td>
			<td class="step-indicator-right">Finish</td>
		</tr>
	</table>
	
	<br>
	
	<h1>Specify sources for your submission</h1>
	
	<br>
	
	<table>
	<h:panelGroup rendered="#{SourcesBean.newSourceType==-1}">
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:commandButton style="width: 180px;" value="No new source added" action="done"/>
		</t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:commandButton style="width: 180px;" value="Primary source" action="#{SourcesBean.primarySource}"/>
		</t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:commandButton style="width: 180px;" value="Article" action="#{SourcesBean.articleSource}"/>
		</t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="tr">
		<t:htmlTag value="td">	
			<h:commandButton style="width: 180px;" value="Book" action="#{SourcesBean.bookSource}"/>
		</t:htmlTag>
	</t:htmlTag>
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<h:commandButton style="width: 180px;" value="Add other type of source" action="#{SourcesBean.otherSource}"/>
		</t:htmlTag>
	</t:htmlTag>
	</h:panelGroup>
	<t:htmlTag value="tr">
		<t:htmlTag value="td">
			<t:htmlTag value="div" rendered="#{SourcesBean.newSourceType==1}" style="margin-top: 10px; padding: 5px; border: 1px solid #CCCCCC;">
				<h:outputText value="Book source" style="font-weight: bold;"/>
				<t:htmlTag value="table">
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Author(s)"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newBook.authors}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Book title"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newBook.title}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Publisher"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newBook.publisher}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Place of publication"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newBook.placeOfPublication}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Year"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 80px;" value="#{SourcesBean.newBook.year}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Page numbers"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 30px;" value="#{SourcesBean.newBook.pageFrom}"/>
											  <h:outputText value="&nbsp;-&nbsp;" escape="false"/>
											  <h:inputText style="width: 30px;" value="#{SourcesBean.newBook.pageTo}"/>
						</t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Other information"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputTextarea style="width: 350px;" value="#{SourcesBean.newBook.otherInfo}" /></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr" rendered="#{SourcesBean.errorMessage != null}">
						<t:htmlTag value="td"></t:htmlTag>
						<t:htmlTag value="td">
							<h:outputText style="font-weight: bold; color: red;" value="#{SourcesBean.errorMessage}"/>
						</t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"></t:htmlTag>
						<t:htmlTag value="td">
							<h:commandButton value="#{SourcesBean.applyLabel}" action="#{SourcesBean.addSource}"/>
							<h:commandButton value="Delete" action="#{SourcesBean.deleteSource}" rendered="#{SourcesBean.mode==true}"/>
							<h:commandButton value="Cancel" action="#{SourcesBean.cancelAddSource}"/>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
			
			<t:htmlTag value="div" rendered="#{SourcesBean.newSourceType==2}" style="margin-top: 10px; padding: 5px; border: 1px solid #CCCCCC;">
				<h:outputText value="Article source" style="font-weight: bold;"/>
				<t:htmlTag value="table">
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Author(s)"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputTextarea style="width: 350px;" value="#{SourcesBean.newArticle.authors}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Article title"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputTextarea style="width: 350px;" value="#{SourcesBean.newArticle.title}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Journal"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputTextarea style="width: 350px;" value="#{SourcesBean.newArticle.journal}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Volume number"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 30px;" value="#{SourcesBean.newArticle.volume}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Year"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 80px;" value="#{SourcesBean.newArticle.year}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Page numbers"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 30px;" value="#{SourcesBean.newArticle.pageFrom}"/>
											  <h:outputText value="&nbsp;-&nbsp;" escape="false"/>
											  <h:inputText style="width: 30px;" value="#{SourcesBean.newArticle.pageTo}"/>
						</t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Other information"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputTextarea style="width: 350px;" value="#{SourcesBean.newArticle.otherInfo}" /></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr" rendered="#{SourcesBean.errorMessage != null}">
						<t:htmlTag value="td"></t:htmlTag>
						<t:htmlTag value="td">
							<h:outputText style="font-weight: bold; color: red;" value="#{SourcesBean.errorMessage}"/>
						</t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"></t:htmlTag>
						<t:htmlTag value="td">
							<h:commandButton value="#{SourcesBean.applyLabel}" action="#{SourcesBean.addSource}"/>
							<h:commandButton value="Delete" action="#{SourcesBean.deleteSource}" rendered="#{SourcesBean.mode==true}"/>
							<h:commandButton value="Cancel" action="#{SourcesBean.cancelAddSource}"/>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
			
			<t:htmlTag value="div" rendered="#{SourcesBean.newSourceType==3}" style="margin-top: 10px; padding: 5px; border: 1px solid #CCCCCC;">
				<h:outputText value="Other source" style="font-weight: bold;"/>
				<t:htmlTag value="table">
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Title"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newOther.title}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Location"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newOther.location}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Page"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 30px;" value="#{SourcesBean.newOther.pageOrFolio}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Information about source"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputTextarea style="width: 350px;" value="#{SourcesBean.newOther.note}" /></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"></t:htmlTag>
						<t:htmlTag value="td">
							<h:commandButton value="#{SourcesBean.applyLabel}" action="#{SourcesBean.addSource}"/>
							<h:commandButton value="Delete" action="#{SourcesBean.deleteSource}" rendered="#{SourcesBean.mode==true}"/>
							<h:commandButton value="Cancel" action="#{SourcesBean.cancelAddSource}"/>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
			
			<t:htmlTag value="div" rendered="#{SourcesBean.newSourceType==4}" style="margin-top: 10px; padding: 5px; border: 1px solid #CCCCCC;">
				<h:outputText value="Primary source" style="font-weight: bold;"/>
				<t:htmlTag value="table">
				<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Name of library of archive"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newPrimary.name}"/></t:htmlTag>
				</t:htmlTag>
				<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Location of library/archive"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newPrimary.location}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Series or collection"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newPrimary.series}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Volume or box or bundle"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newPrimary.volume}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"  style="width: 350px;"><h:outputText value="Document detail (page or folio number, and/or date of document)"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputText style="width: 350px;" value="#{SourcesBean.newPrimary.details}"/></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"><h:outputText value="Information about source"/></t:htmlTag>
						<t:htmlTag value="td"><h:inputTextarea style="width: 350px;" value="#{SourcesBean.newPrimary.note}" /></t:htmlTag>
					</t:htmlTag>
					<t:htmlTag value="tr">
						<t:htmlTag value="td"></t:htmlTag>
						<t:htmlTag value="td">
							<h:commandButton value="#{SourcesBean.applyLabel}" action="#{SourcesBean.addSource}"/>
							<h:commandButton value="Delete" action="#{SourcesBean.deleteSource}" rendered="#{SourcesBean.mode==true}"/>
							<h:commandButton value="Cancel" action="#{SourcesBean.cancelAddSource}"/>
						</t:htmlTag>
					</t:htmlTag>
				</t:htmlTag>
			</t:htmlTag>
		</t:htmlTag>
	</t:htmlTag>
	
	<t:htmlTag value="div" rendered="#{SourcesBean.newSourceType==-1}">
		<f:verbatim>
		
				<h2>Contributed sources</h2>
				</f:verbatim>
				<s:grid id="voyges" 
					columns="#{SourcesBean.columns}"
					rows="#{SourcesBean.rows}" 
					onOpenRow="#{SourcesBean.openRow}" />
				<f:verbatim>
		</f:verbatim>
	</t:htmlTag>
	</table>
	
	<t:htmlTag value="div" rendered="#{SourcesBean.newSourceType==-1}">
		<h:commandButton value="< Previous" action="back"/>
		<h:commandButton value="Save & logout" action="#{SubmissionBean.saveStateSources}"/>
		<h:commandButton value="Next > " action="done"/>
	</t:htmlTag>
</h:form>
</f:view>
</body>
</html>