<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<t:div styleClass="left-menu-box">

	<s:simpleBox>
	
		<t:div styleClass="left-menu-title">
			<h:outputText value="EDUCATIONAL MATERIALS" />
		</t:div>
		
		<s:secondaryMenu>
			<s:secondaryMenuItem label="Lesson plans" menuId="lesson-plans" href="lesson-plans.faces" />
			<s:secondaryMenuItem label="Other resources" menuId="others" href="others.faces">			
				<s:secondaryMenuItem label="Links" menuId="links" href="links.faces" />
				<s:secondaryMenuItem label="Further Reading" menuId="further-reading" href="further-reading.faces" />				
			</s:secondaryMenuItem>
		</s:secondaryMenu>
		
	</s:simpleBox>

</t:div>