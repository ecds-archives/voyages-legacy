<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://tas.library.emory.edu" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>Methodology</title>

	<link href="../styles/main.css" rel="stylesheet" type="text/css">
	<link href="../styles/main-menu.css" rel="stylesheet" type="text/css">
	<link href="../styles/database-info.css" rel="stylesheet" type="text/css">
	
	<script src="../scripts/main-menu.js" language="javascript" type="text/javascript"></script>
	<script src="../scripts/tooltip.js" type="text/javascript" language="javascript"></script>

</head>
<body>
<f:view>
<h:form id="main">

	<s:siteHeader activeSectionId="database">
		<h:outputLink value="../index.faces"><h:outputText value="Home"/></h:outputLink>
		<h:outputLink value="search.faces"><h:outputText value="Voyages Database" /></h:outputLink>
		<h:outputText value="Understanding the database" />
		<h:outputText value="Methodology" />
	</s:siteHeader>
	
	<div id="content">
		<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td valign="top" id="left-column">
				<%@ include file="guide-menu.jsp" %>
			</td>			
			<td valign="top" id="main-content">
				<s:simpleBox>
				
					<h1>Construction of the Trans-Atlantic Slave Trade Database: Sources and Methods</h1>
					
                    <div class="method-info">
						<span class="method-author"><b>David Eltis</b></span>
						<span class="method-location">(Emory University)</span>,
						<span class="method-date">2007</span>
					</div>
					
					<h2 style="text-align:center;">Appendix</h2>
					<p style="text-align: center;">Derivation of Estimated number of Captives Carried on Vessels in the Voyages
					Database for which such Information cannot be obtained from the Sources  
		<% /*			
							
							
					<h:dataTable var="m" value="#{MethodAppendixBean.datalist}"  style="border: 1px solid #CCCCCC;">
						<h:column>
							<f:facet name="header">
								<h:outputText value="Voyage groupings for estimating imputed slaves"/>
							</f:facet>
 							<h:outputText value="#{m.group}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Average number of captives embarked"/>
							</f:facet>
 							<h:outputText value="#{m.ave_em}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="number"/>
							</f:facet>
							<h:outputText value="#{m.number_em}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Sd"/>
							</f:facet>
							<h:outputText value="#{m.sd_em}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Average number of captives disembarked"/>
							</f:facet>
							<h:outputText value="#{m.ave_disem}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="number"/>
							</f:facet>
							<h:outputText value="#{m.number_disem}"/>		
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText value="Sd"/>
							</f:facet>
							<h:outputText value="#{m.sd_disem}"/>		
						</h:column>						
					</h:dataTable>
		*/%>			





<center>
<table border="1" cellpadding="2" class="methodborder">
 <tr>
  <td>Voyage groupings for estimating imputed slaves</td>
  <td>Average number of captives embarked</td>
  <td>number</td>
  <td>Sd</td>
  <td>Average number of captives disembarked</td>
  <td>number</td>
  <td>sd</td>
 </tr>
 <tr>
  <td >Spanish, America, pre-1626</td>
  <td class="number">200.1</td>
  <td class="number">202</td>
  <td class="number">91</td>
  <td class="number">166.7</td>
  <td class="number">202</td>
  <td class="number">91</td>
 </tr>
 <tr>
  <td>Spanish, America, 1626-41</td>
  <td class="number">294</td>
  <td class="number">6</td>
  <td class="number">148.6</td>
  <td class="number">152.9</td>
  <td class="number">148</td>
  <td class="number">98.4</td>
 </tr>
 <tr>
  <td>Steamer</td>
  <td class="number">897.3</td>
  <td class="number">6</td>
  <td class="number">259.8</td>
  <td class="number">1004.5</td>
  <td class="number">17</td>
  <td class="number">399.9</td>
 </tr>
 <tr>
  <td>pre1716 French</td>
  <td class="number">337.7</td>
  <td class="number">77</td>
  <td class="number">166.6</td>
  <td class="number">241.5</td>
  <td class="number">103</td>
  <td class="number">134</td>
 </tr>
 <tr>
  <td>Sumaca, pre-1751</td>
  <td class="number">211.5</td>
  <td class="number">14</td>
  <td class="number">68.1</td>
  <td class="number">229.6</td>
  <td class="number">25</td>
  <td class="number">170.4</td>
 </tr>
 <tr>
  <td>Sumaca, 1751-76</td>
  <td class="number">290.2</td>
  <td class="number">73</td>
  <td class="number">82.4</td>
  <td class="number">223.8</td>
  <td class="number">60</td>
  <td class="number">89.7</td>
 </tr>
 <tr>
  <td>Sumaca, 1776-1800</td>
  <td class="number">342.9</td>
  <td class="number">33</td>
  <td class="number">134.6</td>
  <td class="number">256.5</td>
  <td class="number">93</td>
  <td class="number">133</td>
 </tr>
 <tr>
  <td>Sumaca, 1800-25</td>
  <td class="number">226.2</td>
  <td class="number">50</td>
  <td class="number">100.4</td>
  <td class="number">217.6</td>
  <td class="number">75</td>
  <td class="number">94.6</td>
 </tr>
 <tr>
  <td>Sumaca,1826-66</td>
  <td class="number">317.4</td>
  <td class="number">10</td>
  <td class="number">102.2</td>
  <td class="number">241.5</td>
  <td class="number">13</td>
  <td class="number">107.4</td>
 </tr>
 <tr>
  <td>pre-1650 Dutch</td>
  <td class="number">293.6</td>
  <td class="number">94</td>
  <td class="number">172.7</td>
  <td class="number">227.8</td>
  <td class="number">25</td>
  <td class="number">142.3</td>
 </tr>
 <tr>
  <td>1650-73 Dutch</td>
  <td class="number">369.8</td>
  <td class="number">14</td>
  <td class="number">325.5</td>
  <td class="number">273.8</td>
  <td class="number">22</td>
  <td class="number">95.1</td>
 </tr>
 <tr>
  <td>1674-1730Dutch</td>
  <td class="number">506.5</td>
  <td class="number">45</td>
  <td class="number">150.9</td>
  <td class="number">447.6</td>
  <td class="number">140</td>
  <td class="number">148.2</td>
 </tr>
 <tr>
  <td>post1730Dutch</td>
  <td class="number">427.8</td>
  <td class="number">24</td>
  <td class="number">180.5</td>
  <td class="number">284.3</td>
  <td class="number">442</td>
  <td class="number">105.7</td>
 </tr>
 <tr>
  <td>pre-1801 Bahia</td>
  <td class="number">488.9</td>
  <td class="number">10</td>
  <td class="number">280</td>
  <td class="number">469.4</td>
  <td class="number">9</td>
  <td class="number">268.7</td>
 </tr>
 <tr>
  <td>1801-25 Bahia</td>
  <td class="number">246</td>
  <td class="number">4</td>
  <td class="number">187.1</td>
  <td class="number">245.3</td>
  <td class="number">13</td>
  <td class="number">165.7</td>
 </tr>
 <tr>
  <td>post-1825
  Bahia</td>
  <td class="number">367.3</td>
  <td class="number">35</td>
  <td class="number">162.6</td>
  <td class="number">330.1</td>
  <td class="number">47</td>
  <td class="number">156.1</td>
 </tr>
 <tr>
  <td>Balandra</td>
  <td class="number">223.3</td>
  <td class="number">4</td>
  <td class="number">124.6</td>
  <td class="number">129.3</td>
  <td class="number">36</td>
  <td class="number">89.5</td>
 </tr>
 <tr>
  <td>Barque, pre-1826</td>
  <td class="number">121.3</td>
  <td class="number">4</td>
  <td class="number">89.4</td>
  <td class="number">174.7</td>
  <td class="number">6</td>
  <td class="number">43.3</td>
 </tr>
 <tr>
  <td>Barque, 1826-50</td>
  <td class="number">614.3</td>
  <td class="number">8</td>
  <td class="number">371.3</td>
  <td class="number">638.3</td>
  <td class="number">63</td>
  <td class="number">237.1</td>
 </tr>
 <tr>
  <td>Barque, 1851-75</td>
  <td class="number">821.9</td>
  <td class="number">9</td>
  <td class="number">300.8</td>
  <td class="number">655.5</td>
  <td class="number">23</td>
  <td class="number">212.5</td>
 </tr>
 <tr>
  <td>Barqua</td>
  <td class="number">572.4</td>
  <td class="number">16</td>
  <td class="number">143.7</td>
  <td class="number">516.5</td>
  <td class="number">20</td>
  <td class="number">127</td>
 </tr>
 <tr>
  <td>B. of Benin, pre-1700</td>
  <td class="number">418.5</td>
  <td class="number">86</td>
  <td class="number">153.1</td>
  <td class="number">308.3</td>
  <td class="number">131</td>
  <td class="number">137.3</td>
 </tr>
 <tr>
  <td>B. of Benin, 1700-1800</td>
  <td class="number">356.3</td>
  <td class="number">513</td>
  <td class="number">130.7</td>
  <td class="number">313.1</td>
  <td class="number">533</td>
  <td class="number">129.1</td>
 </tr>
 <tr>
  <td>B. of Benin, post-1800</td>
  <td class="number">344.5</td>
  <td class="number">10</td>
  <td class="number">165</td>
  <td class="number">439</td>
  <td class="number">9</td>
  <td class="number">183.9</td>
 </tr>
 <tr>
  <td>Bergantine pre-1776</td>
  <td class="number">246.8</td>
  <td class="number">19</td>
  <td class="number">97.6</td>
  <td class="number">193.7</td>
  <td class="number">39</td>
  <td class="number">137.7</td>
 </tr>
 <tr>
  <td>Bergantine, 1776-1800</td>
  <td class="number">314.7</td>
  <td class="number">46</td>
  <td class="number">157.2</td>
  <td class="number">249.7</td>
  <td class="number">91</td>
  <td class="number">134.8</td>
 </tr>
 <tr>
  <td>Bergantine,1801-25</td>
  <td class="number">417.5</td>
  <td class="number">562</td>
  <td class="number">147.4</td>
  <td class="number">352.8</td>
  <td class="number">783</td>
  <td class="number">135.7</td>
 </tr>
 <tr>
  <td>Bergantine, 1826-75</td>
  <td class="number">454.9</td>
  <td class="number">134</td>
  <td class="number">129.1</td>
  <td class="number">419.6</td>
  <td class="number">147</td>
  <td class="number">132.2</td>
 </tr>
 <tr>
  <td>B. of Biafra, pre-1700</td>
  <td class="number">229.4</td>
  <td class="number">44</td>
  <td class="number">90.4</td>
  <td class="number">175</td>
  <td class="number">109</td>
  <td class="number">68.6</td>
 </tr>
 <tr>
  <td>B. of Biafra, 1700-1800</td>
  <td class="number">263.9</td>
  <td class="number">140</td>
  <td class="number">122.4</td>
  <td class="number">208.8</td>
  <td class="number">326</td>
  <td class="number">102.2</td>
 </tr>
 <tr>
  <td>B. of Biafra, post-1800</td>
  <td class="number">311.5</td>
  <td class="number">52</td>
  <td class="number">166.6</td>
  <td class="number">261.8</td>
  <td class="number">29</td>
  <td class="number">142.3</td>
 </tr>
 <tr>
  <td>Brig, pre-1751</td>
  <td class="number">150</td>
  <td class="number">1</td>
  <td class="number">.</td>
  <td class="number">151.7</td>
  <td class="number">49</td>
  <td class="number">54.2</td>
 </tr>
 <tr>
  <td>Brig, 1751-75</td>
  <td class="number">141.6</td>
  <td class="number">26</td>
  <td class="number">74.3</td>
  <td class="number">150.1</td>
  <td class="number">112</td>
  <td class="number">64.3</td>
 </tr>
 <tr>
  <td>Brig, 1776-1800</td>
  <td class="number">201.1</td>
  <td class="number">86</td>
  <td class="number">75.5</td>
  <td class="number">198.1</td>
  <td class="number">243</td>
  <td class="number">76</td>
 </tr>
 <tr>
  <td>Brig,1801-25</td>
  <td class="number">332.9</td>
  <td class="number">140</td>
  <td class="number">139.8</td>
  <td class="number">265.2</td>
  <td class="number">293</td>
  <td class="number">138.5</td>
 </tr>
 <tr>
  <td>Brig, 1826-50</td>
  <td class="number">455.5</td>
  <td class="number">263</td>
  <td class="number">169.1</td>
  <td class="number">441.5</td>
  <td class="number">538</td>
  <td class="number">161.4</td>
 </tr>
 <tr>
  <td>Brig, post-1850</td>
  <td class="number">489.1</td>
  <td class="number">7</td>
  <td class="number">210.2</td>
  <td class="number">461.6</td>
  <td class="number">36</td>
  <td class="number">140.3</td>
 </tr>
 <tr>
  <td>Brigantine, pre-1776</td>
  <td class="number">142.7</td>
  <td class="number">44</td>
  <td class="number">66.6</td>
  <td class="number">131.3</td>
  <td class="number">307</td>
  <td class="number">58.2</td>
 </tr>
 <tr>
  <td>Brigantine, 1776-1800</td>
  <td class="number">111</td>
  <td class="number">1</td>
  <td class="number">.</td>
  <td class="number">216.6</td>
  <td class="number">11</td>
  <td class="number">65.9</td>
 </tr>
 <tr>
  <td>Brigantine,1801-25</td>
  <td class="number">206.1</td>
  <td class="number">7</td>
  <td class="number">83.6</td>
  <td class="number">255.9</td>
  <td class="number">121</td>
  <td class="number">118.4</td>
 </tr>
 <tr>
  <td>Brigantine, 1825-50</td>
  <td class="number">372</td>
  <td class="number">79</td>
  <td class="number">182.7</td>
  <td class="number">361.1</td>
  <td class="number">159</td>
  <td class="number">163.5</td>
 </tr>
 <tr>
  <td>Brigantine, post-1850</td>
  <td class="number">471.3</td>
  <td class="number">7</td>
  <td class="number">286.5</td>
  <td class="number">480.1</td>
  <td class="number">28</td>
  <td class="number">199.8</td>
 </tr>
 <tr>
  <td>Buque, pre-1726</td>
  <td class="number">406.2</td>
  <td class="number">6</td>
  <td class="number">102.1</td>
  <td class="number">319.7</td>
  <td class="number">30</td>
  <td class="number">122.5</td>
 </tr>
 <tr>
  <td>Buque, 1726-1825</td>
  <td class="number">409</td>
  <td class="number">1</td>
  <td class="number">.</td>
  <td class="number">228.5</td>
  <td class="number">11</td>
  <td class="number">113</td>
 </tr>
 <tr>
  <td>Chalupa</td>
  <td class="number">235</td>
  <td class="number">4</td>
  <td class="number">72.4</td>
  <td class="number">152.9</td>
  <td class="number">13</td>
  <td class="number">78.4</td>
 </tr>
 <tr>
  <td>Charrua</td>
  <td class="number">329</td>
  <td class="number">5</td>
  <td class="number">131.1</td>
  <td class="number">319.3</td>
  <td class="number">7</td>
  <td class="number">133</td>
 </tr>
 <tr>
  <td>Curveta, pre-1751</td>
  <td class="number">335.5</td>
  <td class="number">75</td>
  <td class="number">83.5</td>
  <td class="number">272.2</td>
  <td class="number">50</td>
  <td class="number">95</td>
 </tr>
 <tr>
  <td>Curveta, 1751-75</td>
  <td class="number">348.9</td>
  <td class="number">217</td>
  <td class="number">106.7</td>
  <td class="number">277.3</td>
  <td class="number">199</td>
  <td class="number">112.1</td>
 </tr>
 <tr>
  <td>Curveta, 1776-1800</td>
  <td class="number">371.7</td>
  <td class="number">210</td>
  <td class="number">162.9</td>
  <td class="number">323.5</td>
  <td class="number">343</td>
  <td class="number">145.8</td>
 </tr>
 <tr>
  <td>Curveta,
  post-1800</td>
  <td class="number">496.7</td>
  <td class="number">172</td>
  <td class="number">125.9</td>
  <td class="number">435.7</td>
  <td class="number">195</td>
  <td class="number">120.9</td>
 </tr>
 <tr>
  <td>Danish,
  pre-1794</td>
  <td class="number">228.9</td>
  <td class="number">8</td>
  <td class="number">73.7</td>
  <td class="number">&nbsp;</td>
  <td class="number">0</td>
  <td class="number">0</td>
 </tr>
 <tr>
  <td>Danish,
  1794-1806</td>
  <td class="number">248.8</td>
  <td class="number">4</td>
  <td class="number">134.6</td>
  <td class="number">104.3</td>
  <td class="number">34</td>
  <td class="number">65.3</td>
 </tr>
 <tr>
  <td>Felucca</td>
  <td class="number">246.8</td>
  <td class="number">5</td>
  <td class="number">207.2</td>
  <td class="number">340.2</td>
  <td class="number">26</td>
  <td class="number">148.3</td>
 </tr>
 <tr>
  <td>Fluit</td>
  <td class="number">440.3</td>
  <td class="number">19</td>
  <td class="number">139</td>
  <td class="number">391</td>
  <td class="number">29</td>
  <td class="number">145.1</td>
 </tr>
 <tr>
  <td>Fregata,
  pre-1726</td>
  <td class="number">349.8</td>
  <td class="number">34</td>
  <td class="number">134.3</td>
  <td class="number">299</td>
  <td class="number">56</td>
  <td class="number">155.2</td>
 </tr>
 <tr>
  <td>Fregata,
  1726-75</td>
  <td class="number">348.5</td>
  <td class="number">88</td>
  <td class="number">103.6</td>
  <td class="number">310.4</td>
  <td class="number">123</td>
  <td class="number">126.4</td>
 </tr>
 <tr>
  <td>Fregata,
  1776-1800</td>
  <td class="number">303.5</td>
  <td class="number">21</td>
  <td class="number">191.4</td>
  <td class="number">259.2</td>
  <td class="number">64</td>
  <td class="number">102.3</td>
 </tr>
 <tr>
  <td>Fregata,
  1801-75</td>
  <td class="number">229.9</td>
  <td class="number">21</td>
  <td class="number">181</td>
  <td class="number">265.7</td>
  <td class="number">88</td>
  <td class="number">155.2</td>
 </tr>
 <tr>
  <td>Galera
  1751-76</td>
  <td class="number">400.7</td>
  <td class="number">69</td>
  <td class="number">190.1</td>
  <td class="number">285.6</td>
  <td class="number">90</td>
  <td class="number">183.5</td>
 </tr>
 <tr>
  <td>Galera
  1776-1800</td>
  <td class="number">371.7</td>
  <td class="number">67</td>
  <td class="number">205</td>
  <td class="number">335.7</td>
  <td class="number">79</td>
  <td class="number">194.4</td>
 </tr>
 <tr>
  <td>Galera
  1801-25</td>
  <td class="number">548.2</td>
  <td class="number">206</td>
  <td class="number">150</td>
  <td class="number">472.3</td>
  <td class="number">224</td>
  <td class="number">148.2</td>
 </tr>
 <tr>
  <td>Galera
  1851-76</td>
  <td class="number">600.5</td>
  <td class="number">62</td>
  <td class="number">164.2</td>
  <td class="number">536.8</td>
  <td class="number">70</td>
  <td class="number">150.5</td>
 </tr>
 <tr>
  <td>Galera
  pre-1751</td>
  <td class="number">444</td>
  <td class="number">114</td>
  <td class="number">129.6</td>
  <td class="number">403.3</td>
  <td class="number">157</td>
  <td class="number">141.5</td>
 </tr>
 <tr>
  <td>Galeta1826-50</td>
  <td class="number">321.5</td>
  <td class="number">11</td>
  <td class="number">160.4</td>
  <td class="number">267.8</td>
  <td class="number">27</td>
  <td class="number">145.3</td>
 </tr>
 <tr>
  <td>Galeta1851-75</td>
  <td class="number">423.5</td>
  <td class="number">2</td>
  <td class="number">470.2</td>
  <td class="number">345.3</td>
  <td class="number">7</td>
  <td class="number">273</td>
 </tr>
 <tr>
  <td>Galeta
  pre-1826</td>
  <td class="number">161.8</td>
  <td class="number">6</td>
  <td class="number">94.2</td>
  <td class="number">209</td>
  <td class="number">250</td>
  <td class="number">99.4</td>
 </tr>
 <tr>
  <td>Gold
  Coast 1700</td>
  <td class="number">262.4</td>
  <td class="number">276</td>
  <td class="number">127.8</td>
  <td class="number">236.7</td>
  <td class="number">400</td>
  <td class="number">100.4</td>
 </tr>
 <tr>
  <td>Gold
  Coast pre-1700</td>
  <td class="number">316.9</td>
  <td class="number">36</td>
  <td class="number">208.1</td>
  <td class="number">242.2</td>
  <td class="number">52</td>
  <td class="number">141.5</td>
 </tr>
 <tr>
  <td>Iate1826-75</td>
  <td class="number">421.6</td>
  <td class="number">7</td>
  <td class="number">155.2</td>
  <td class="number">282</td>
  <td class="number">48</td>
  <td class="number">138.8</td>
 </tr>
 <tr>
  <td>Iatepre1826</td>
  <td class="number">296.5</td>
  <td class="number">57</td>
  <td class="number">136.4</td>
  <td class="number">245.4</td>
  <td class="number">52</td>
  <td class="number">147.7</td>
 </tr>
 <tr>
  <td>Navio
  mercante</td>
  <td class="number">454.7</td>
  <td class="number">80</td>
  <td class="number">130.6</td>
  <td class="number">410.6</td>
  <td class="number">44</td>
  <td class="number">139.6</td>
 </tr>
 <tr>
  <td>No
  rig1651-75</td>
  <td class="number">306</td>
  <td class="number">5</td>
  <td class="number">135.1</td>
  <td class="number">261.3</td>
  <td class="number">32</td>
  <td class="number">194.4</td>
 </tr>
 <tr>
  <td>No
  rig1676-1700</td>
  <td class="number">418.4</td>
  <td class="number">8</td>
  <td class="number">230.3</td>
  <td class="number">196.9</td>
  <td class="number">134</td>
  <td class="number">126.9</td>
 </tr>
 <tr>
  <td>No
  rig1701-25</td>
  <td class="number">259.8</td>
  <td class="number">15</td>
  <td class="number">159.1</td>
  <td class="number">191.2</td>
  <td class="number">451</td>
  <td class="number">96.7</td>
 </tr>
 <tr>
  <td>No
  rig1726-50</td>
  <td class="number">252.8</td>
  <td class="number">32</td>
  <td class="number">142.1</td>
  <td class="number">219.4</td>
  <td class="number">206</td>
  <td class="number">103</td>
 </tr>
 <tr>
  <td>No
  rig1751-75</td>
  <td class="number">303.9</td>
  <td class="number">38</td>
  <td class="number">162.8</td>
  <td class="number">208.8</td>
  <td class="number">137</td>
  <td class="number">124</td>
 </tr>
 <tr>
  <td>No
  rig1776-1800</td>
  <td class="number">331.9</td>
  <td class="number">34</td>
  <td class="number">138.8</td>
  <td class="number">262.3</td>
  <td class="number">133</td>
  <td class="number">137</td>
 </tr>
 <tr>
  <td>No
  rig1801-25</td>
  <td class="number">287.9</td>
  <td class="number">16</td>
  <td class="number">137.7</td>
  <td class="number">195.8</td>
  <td class="number">37</td>
  <td class="number">128.1</td>
 </tr>
 <tr>
  <td>No
  rig1826-50</td>
  <td class="number">387</td>
  <td class="number">6</td>
  <td class="number">421.7</td>
  <td class="number">444.3</td>
  <td class="number">109</td>
  <td class="number">236.2</td>
 </tr>
 <tr>
  <td>No
  rig1851-75</td>
  <td class="number">658.3</td>
  <td class="number">4</td>
  <td class="number">192.7</td>
  <td class="number">493.5</td>
  <td class="number">84</td>
  <td class="number">194.7</td>
 </tr>
 <tr>
  <td>North
  American pre-1851</td>
  <td class="number">151.6</td>
  <td class="number">49</td>
  <td class="number">110</td>
  <td class="number">129.4</td>
  <td class="number">98</td>
  <td class="number">121.7</td>
 </tr>
 <tr>
  <td>Paquete</td>
  <td class="number">302</td>
  <td class="number">4</td>
  <td class="number">86.4</td>
  <td class="number">211</td>
  <td class="number">7</td>
  <td class="number">118</td>
 </tr>
 <tr>
  <td>Patacho</td>
  <td class="number">306.4</td>
  <td class="number">63</td>
  <td class="number">100.7</td>
  <td class="number">335.3</td>
  <td class="number">164</td>
  <td class="number">105.4</td>
 </tr>
 <tr>
  <td>Penque</td>
  <td class="number">363.5</td>
  <td class="number">11</td>
  <td class="number">116.8</td>
  <td class="number">259.8</td>
  <td class="number">8</td>
  <td class="number">111.5</td>
 </tr>
 <tr>
  <td>Pilot
  boat</td>
  <td class="number">273.5</td>
  <td class="number">2</td>
  <td class="number">37.5</td>
  <td class="number">270.8</td>
  <td class="number">13</td>
  <td class="number">133.8</td>
 </tr>
 <tr>
  <td>Polacca</td>
  <td class="number">738.7</td>
  <td class="number">3</td>
  <td class="number">300</td>
  <td class="number">428.9</td>
  <td class="number">27</td>
  <td class="number">231.8</td>
 </tr>
 <tr>
  <td>Schooner
  1776-1800</td>
  <td class="number">107.1</td>
  <td class="number">31</td>
  <td class="number">55.5</td>
  <td class="number">104</td>
  <td class="number">104</td>
  <td class="number">50.2</td>
 </tr>
 <tr>
  <td>Schooner,
  1801-1825</td>
  <td class="number">207.1</td>
  <td class="number">106</td>
  <td class="number">145.4</td>
  <td class="number">167.5</td>
  <td class="number">220</td>
  <td class="number">111.5</td>
 </tr>
 <tr>
  <td>Schooner
  1826-1850</td>
  <td class="number">264.2</td>
  <td class="number">249</td>
  <td class="number">117.9</td>
  <td class="number">278.7</td>
  <td class="number">488</td>
  <td class="number">137.5</td>
 </tr>
 <tr>
  <td>Schooner
  1851-1875</td>
  <td class="number">327.7</td>
  <td class="number">13</td>
  <td class="number">243</td>
  <td class="number">322.2</td>
  <td class="number">26</td>
  <td class="number">171.1</td>
 </tr>
 <tr>
  <td>Schooner
  brig</td>
  <td class="number">322</td>
  <td class="number">35</td>
  <td class="number">97.4</td>
  <td class="number">335.8</td>
  <td class="number">86</td>
  <td class="number">111.3</td>
 </tr>
 <tr>
  <td>Schooner
  pre-1776</td>
  <td class="number">67.9</td>
  <td class="number">10</td>
  <td class="number">34.8</td>
  <td class="number">100.6</td>
  <td class="number">50</td>
  <td class="number">85.9</td>
 </tr>
 <tr>
  <td>Southeast
  Africa 1700-1800</td>
  <td class="number">425.1</td>
  <td class="number">14</td>
  <td class="number">210.9</td>
  <td class="number">266.6</td>
  <td class="number">72</td>
  <td class="number">121.6</td>
 </tr>
 <tr>
  <td>Southeast
  Africa post-1800</td>
  <td class="number">451.3</td>
  <td class="number">27</td>
  <td class="number">190</td>
  <td class="number">403.9</td>
  <td class="number">28</td>
  <td class="number">178.8</td>
 </tr>
 <tr>
  <td>Southeast
  Africa pre-1700</td>
  <td class="number">129.5</td>
  <td class="number">4</td>
  <td class="number">57</td>
  <td class="number">192.3</td>
  <td class="number">18</td>
  <td class="number">77.3</td>
 </tr>
 <tr>
  <td>Senegambia
  1700-1800</td>
  <td class="number">183.3</td>
  <td class="number">160</td>
  <td class="number">133.3</td>
  <td class="number">149.6</td>
  <td class="number">262</td>
  <td class="number">103.1</td>
 </tr>
 <tr>
  <td>Senegambia
  post-1800</td>
  <td class="number">123.6</td>
  <td class="number">13</td>
  <td class="number">99.9</td>
  <td class="number">159</td>
  <td class="number">16</td>
  <td class="number">94.2</td>
 </tr>
 <tr>
  <td>Senegambia
  pre1700</td>
  <td class="number">218.5</td>
  <td class="number">43</td>
  <td class="number">129.7</td>
  <td class="number">155.3</td>
  <td class="number">43</td>
  <td class="number">70.4</td>
 </tr>
 <tr>
  <td>Ship
  1726-1750</td>
  <td class="number">335</td>
  <td class="number">15</td>
  <td class="number">124.5</td>
  <td class="number">297.6</td>
  <td class="number">289</td>
  <td class="number">126.7</td>
 </tr>
 <tr>
  <td>Ship
  1751-1775</td>
  <td class="number">351.3</td>
  <td class="number">160</td>
  <td class="number">127</td>
  <td class="number">287.3</td>
  <td class="number">658</td>
  <td class="number">105.6</td>
 </tr>
 <tr>
  <td>Ship
  1776-1800</td>
  <td class="number">355</td>
  <td class="number">503</td>
  <td class="number">123.9</td>
  <td class="number">329.2</td>
  <td class="number">1367</td>
  <td class="number">118.7</td>
 </tr>
 <tr>
  <td>Ship
  1801-1825</td>
  <td class="number">400.1</td>
  <td class="number">32</td>
  <td class="number">167.6</td>
  <td class="number">278.8</td>
  <td class="number">648</td>
  <td class="number">79.1</td>
 </tr>
 <tr>
  <td>Ship
  1826-1850</td>
  <td class="number">597</td>
  <td class="number">21</td>
  <td class="number">263</td>
  <td class="number">532.7</td>
  <td class="number">29</td>
  <td class="number">199.8</td>
 </tr>
 <tr>
  <td>Ship
  1851-75</td>
  <td class="number">915.3</td>
  <td class="number">3</td>
  <td class="number">257.1</td>
  <td class="number">768.7</td>
  <td class="number">9</td>
  <td class="number">154.9</td>
 </tr>
 <tr>
  <td>Ship
  pre-1726</td>
  <td class="number">291.7</td>
  <td class="number">39</td>
  <td class="number">148.6</td>
  <td class="number">213.8</td>
  <td class="number">132</td>
  <td class="number">107</td>
 </tr>
 <tr>
  <td>Sierra
  Leone 1700-1800</td>
  <td class="number">204.9</td>
  <td class="number">59</td>
  <td class="number">156</td>
  <td class="number">205.7</td>
  <td class="number">115</td>
  <td class="number">136.9</td>
 </tr>
 <tr>
  <td>Sierra
  Leone post-1800</td>
  <td class="number">246.7</td>
  <td class="number">23</td>
  <td class="number">163</td>
  <td class="number">245.5</td>
  <td class="number">16</td>
  <td class="number">180.2</td>
 </tr>
 <tr>
  <td>Sloop
  1751-76</td>
  <td class="number">82.9</td>
  <td class="number">22</td>
  <td class="number">31.8</td>
  <td class="number">66.6</td>
  <td class="number">71</td>
  <td class="number">36.2</td>
 </tr>
 <tr>
  <td>Sloop
  1776-1800</td>
  <td class="number">94.4</td>
  <td class="number">8</td>
  <td class="number">80.5</td>
  <td class="number">92.9</td>
  <td class="number">36</td>
  <td class="number">41.5</td>
 </tr>
 <tr>
  <td>Sloop
  1801-76</td>
  <td class="number">128</td>
  <td class="number">4</td>
  <td class="number">59.2</td>
  <td class="number">101.4</td>
  <td class="number">10</td>
  <td class="number">54.9</td>
 </tr>
 <tr>
  <td>Sloop
  pre1751</td>
  <td class="number">123.4</td>
  <td class="number">15</td>
  <td class="number">69.4</td>
  <td class="number">108.2</td>
  <td class="number">92</td>
  <td class="number">38.8</td>
 </tr>
 <tr>
  <td>Sierra
  Leone pre-1700</td>
  <td class="number">126.9</td>
  <td class="number">12</td>
  <td class="number">26.2</td>
  <td class="number">92.5</td>
  <td class="number">13</td>
  <td class="number">39.5</td>
 </tr>
 <tr>
  <td>Smack</td>
  <td class="number">314</td>
  <td class="number">7</td>
  <td class="number">128.3</td>
  <td class="number">257.3</td>
  <td class="number">57</td>
  <td class="number">112.3</td>
 </tr>
 <tr>
  <td>Small craft</td>
  <td class="number">111.7</td>
  <td class="number">12</td>
  <td class="number">68.2</td>
  <td class="number">99.6</td>
  <td class="number">35</td>
  <td class="number">55.6</td>
 </tr>
 <tr>
  <td>Snow 1726-50</td>
  <td class="number">195.9</td>
  <td class="number">9</td>
  <td class="number">26.7</td>
  <td class="number">194.8</td>
  <td class="number">185</td>
  <td class="number">70.2</td>
 </tr>
 <tr>
  <td>Snow 1751-75</td>
  <td class="number">220.2</td>
  <td class="number">102</td>
  <td class="number">92.5</td>
  <td class="number">201.1</td>
  <td class="number">557</td>
  <td class="number">70.9</td>
 </tr>
 <tr>
  <td>Snow 1776-1800</td>
  <td class="number">258.3</td>
  <td class="number">28</td>
  <td class="number">78.5</td>
  <td class="number">210.7</td>
  <td class="number">59</td>
  <td class="number">85.3</td>
 </tr>
 <tr>
  <td>Snow 1801-76</td>
  <td class="number">192</td>
  <td class="number">2</td>
  <td class="number">18.4</td>
  <td class="number">167.6</td>
  <td class="number">13</td>
  <td class="number">47.2</td>
 </tr>
 <tr>
  <td>Snow pre-1726</td>
  <td class="number">165.7</td>
  <td class="number">3</td>
  <td class="number">51.1</td>
  <td class="number">119.8</td>
  <td class="number">30</td>
  <td class="number">45.2</td>
 </tr>
 <tr>
  <td>Spanish 1642-1662</td>
  <td class="number">314.6</td>
  <td class="number">9</td>
  <td class="number">321.6</td>
  <td class="number">250.9</td>
  <td class="number">17</td>
  <td class="number">219</td>
 </tr>
 <tr>
  <td>US barque 1826-75</td>
  <td class="number">743</td>
  <td class="number">13</td>
  <td class="number">215</td>
  <td class="number">569.7</td>
  <td class="number">28</td>
  <td class="number">177.7</td>
 </tr>
 <tr>
  <td>US brig 1776-1800</td>
  <td class="number">108</td>
  <td class="number">55</td>
  <td class="number">26.6</td>
  <td class="number">107.4</td>
  <td class="number">44</td>
  <td class="number">55.9</td>
 </tr>
 <tr>
  <td>US brig 1801-25</td>
  <td class="number">127.9</td>
  <td class="number">79</td>
  <td class="number">46.1</td>
  <td class="number">129.4</td>
  <td class="number">100</td>
  <td class="number">56</td>
 </tr>
 <tr>
  <td>US brigantine 1776-1825</td>
  <td class="number">83.3</td>
  <td class="number">3</td>
  <td class="number">33.3</td>
  <td class="number">125.6</td>
  <td class="number">26</td>
  <td class="number">46.9</td>
 </tr>
 <tr>
  <td>US brigantine post1825</td>
  <td class="number">420.3</td>
  <td class="number">4</td>
  <td class="number">136.8</td>
  <td class="number">436.5</td>
  <td class="number">12</td>
  <td class="number">158.1</td>
 </tr>
 <tr>
  <td>US brigantine pre-1776</td>
  <td class="number">99.1</td>
  <td class="number">9</td>
  <td class="number">51.2</td>
  <td class="number">100.1</td>
  <td class="number">22</td>
  <td class="number">40.6</td>
 </tr>
 <tr>
  <td>US brig post-1825</td>
  <td class="number">595.5</td>
  <td class="number">11</td>
  <td class="number">130.6</td>
  <td class="number">557.6</td>
  <td class="number">30</td>
  <td class="number">145.4</td>
 </tr>
 <tr>
  <td>US brig pre-1776</td>
  <td class="number">127.3</td>
  <td class="number">54</td>
  <td class="number">46.3</td>
  <td class="number">103.5</td>
  <td class="number">69</td>
  <td class="number">44.9</td>
 </tr>
 <tr>
  <td>US schooner, 1776-1800</td>
  <td class="number">80.6</td>
  <td class="number">48</td>
  <td class="number">38.6</td>
  <td class="number">76.1</td>
  <td class="number">43</td>
  <td class="number">36.6</td>
 </tr>
 <tr>
  <td>US schooner, 1801-1825</td>
  <td class="number">90</td>
  <td class="number">29</td>
  <td class="number">18.2</td>
  <td class="number">88</td>
  <td class="number">80</td>
  <td class="number">28.9</td>
 </tr>
 <tr>
  <td>US schooner, 1826-75</td>
  <td class="number">405.4</td>
  <td class="number">7</td>
  <td class="number">201.2</td>
  <td class="number">398.2</td>
  <td class="number">13</td>
  <td class="number">169.3</td>
 </tr>
 <tr>
  <td>US schooner, pre-1776</td>
  <td class="number">75.3</td>
  <td class="number">18</td>
  <td class="number">35</td>
  <td class="number">74</td>
  <td class="number">24</td>
  <td class="number">35.7</td>
 </tr>
 <tr>
  <td>US ship1776-1800</td>
  <td class="number">165.9</td>
  <td class="number">31</td>
  <td class="number">81.4</td>
  <td class="number">166.5</td>
  <td class="number">30</td>
  <td class="number">93.8</td>
 </tr>
 <tr>
  <td>US ship1801-1825</td>
  <td class="number">214.7</td>
  <td class="number">47</td>
  <td class="number">91</td>
  <td class="number">197.1</td>
  <td class="number">54</td>
  <td class="number">95.2</td>
 </tr>
 <tr>
  <td>US ship post-1825</td>
  <td class="number">1126.8</td>
  <td class="number">6</td>
  <td class="number">210.1</td>
  <td class="number">1003</td>
  <td class="number">10</td>
  <td class="number">331.4</td>
 </tr>
 <tr>
  <td>US ship pre-1776</td>
  <td class="number">188.3</td>
  <td class="number">22</td>
  <td class="number">82.4</td>
  <td class="number">155.2</td>
  <td class="number">36</td>
  <td class="number">72.7</td>
 </tr>
 <tr>
  <td>US sloop</td>
  <td class="number">80.1</td>
  <td class="number">141</td>
  <td class="number">31.2</td>
  <td class="number">70.1</td>
  <td class="number">141</td>
  <td class="number">31.8</td>
 </tr>
 <tr>
  <td>US snow</td>
  <td class="number">115.9</td>
  <td class="number">53</td>
  <td class="number">38.7</td>
  <td class="number">119.8</td>
  <td class="number">41</td>
  <td class="number">66.7</td>
 </tr>
 <tr>
  <td>W Central Africa 1700-1800</td>
  <td class="number">382.7</td>
  <td class="number">477</td>
  <td class="number">148.7</td>
  <td class="number">351.9</td>
  <td class="number">859</td>
  <td class="number">139.9</td>
 </tr>
 <tr>
  <td>W Central Africa post-1800</td>
  <td class="number">419.1</td>
  <td class="number">63</td>
  <td class="number">162.9</td>
  <td class="number">372</td>
  <td class="number">76</td>
  <td class="number">161.7</td>
 </tr>
 <tr>
  <td>W Central Africa pre-1700</td>
  <td class="number">369.2</td>
  <td class="number">19</td>
  <td class="number">175.2</td>
  <td class="number">331</td>
  <td class="number">54</td>
  <td class="number">135.6</td>
 </tr>
 <tr>
  <td>Windward C. 1700-1800</td>
  <td class="number">219.5</td>
  <td class="number">23</td>
  <td class="number">112.5</td>
  <td class="number">177.8</td>
  <td class="number">51</td>
  <td class="number">102.2</td>
 </tr>
</table>
</center>











			
					<br>
					
					<table border="0" cellspacing="0" cellpadding="0" class="method-prev-next">
					<tr>
						<td class="method-prev">
							<a href="methodology-19.faces">Resistance and Price of Slaves</a>
						</td>
						<td class="method-next">
							<a href="methodology-21.faces">Notes</a>
						</td>
					</tr>
					</table>
				</s:simpleBox>
			</td>
		</tr>
		</table>
		
	</div>
</h:form>
</f:view>
</body>
</html>