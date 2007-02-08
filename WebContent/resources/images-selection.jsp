<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<s:expandableBox text="Sample queries">
	<f:verbatim escape="false">
		<ul class="box">
			<li>How did the atlantic slave trade effect the economy of Europe, Africa, and America?</li>
			<li>Where were they taken to?</li>
			<li>Which European country was the first to engage in the slave trade in Central Africa?</li>
			<li>Has there ever been evidence of a slave earning his freedom and returning to Africa in their lifetime?</li>
			<li>Why were the slaves taken from Africa?</li>
			<li>show me more queries...</li>
		</ul>
	</f:verbatim>
</s:expandableBox>

<br>

<s:expandableBox text="Search for images">
	<f:verbatim escape="false">
		There will be a search options. User will be able to choose year range for image, author, place etc.
		<br> <br>
	</f:verbatim>
	<h:commandButton value="Search images"/>
</s:expandableBox>