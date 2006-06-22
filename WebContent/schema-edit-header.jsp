<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s" %>

<div class="main-title">Trans-Atlantic Slave Trade</div>

<div class="tabs-container">
	<s:tabBar id="bar" binding="#{Switcher.moduleTabs}" onTabChanged="#{Switcher.moduleChanged}" selectedTabId="#{Switcher.selectedModuleId}">
		<s:tab text="Voyages - groups" tabId="voyages-groups" />
		<s:tab text="Voyages - compound atrributes" tabId="voyages-compound-attributes" />
		<s:tab text="Voyages - atrributes" tabId="voyages-attributes" />
		<s:tab text="Slaves - groups" tabId="slaves-groups" />
		<s:tab text="Slaves - compound atrributes" tabId="slaves-compound-attributes" />
		<s:tab text="Slaves - atrributes" tabId="slaves-attributes" />
	</s:tabBar>
</div>

