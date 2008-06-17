<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div styleClass="left-menu-box">

	<s:simpleBox>
	
		<t:div styleClass="left-menu-title">
			<h:outputText value="ABOUT THE PROJECT" />
		</t:div>
		
		<s:secondaryMenu>
			<s:secondaryMenuItem label="History" menuId="history" href="history.faces" />
			<s:secondaryMenuItem label="Project Team" menuId="team" href="team.faces" />
			<s:secondaryMenuItem label="Contributors of data" menuId="data" href="data.faces" />
			<s:secondaryMenuItem label="Acknowledgements" menuId="acknowledgements" href="acknowledgements.faces" />
			<s:secondaryMenuItem label="Contact Us" menuId="contact" href="contact.faces" />
			<s:secondaryMenuItem label="Legal" menuId="legal" href="legal.faces" />
		</s:secondaryMenu>		

	</s:simpleBox>
</t:div>