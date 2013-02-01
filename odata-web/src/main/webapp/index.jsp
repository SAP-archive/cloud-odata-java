<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>SAP OData library</title>
</head>
<body>
	<h1>SAP OData library</h1>
	<hr />
	<%
	  String version = "gen/version.html";
	%>
	<%
	  try {
	%>
	<jsp:include page='<%=version%>' />
	<%
	  } catch (Exception e) {
	%>
	<p>IDE Build</p>
	<%
	  }
	%>
	<hr />
	<h2>Reference Scenario</h2>
	<h3>Service Document & Metadata</h3>
	<ul>
		<li><a href="ReferenceScenario.svc?_wadl" target="_blank">wadl</a></li>
		<li><a href="ReferenceScenario.svc/" target="_blank">service
				document</a></li>
		<li><a href="ReferenceScenario.svc/$metadata" target="_blank">metadata</a></li>
	</ul>
	<h3>EntitySets</h3>
	<ul>
		<li><a href="ReferenceScenario.svc/Employees" target="_blank">Employees</a></li>
		<li><a href="ReferenceScenario.svc/Managers" target="_blank">Managers</a></li>
		<li><a href="ReferenceScenario.svc/Buildings" target="_blank">Buildings</a></li>
		<li><a href="ReferenceScenario.svc/Rooms" target="_blank">Rooms</a></li>
		<li><a href="ReferenceScenario.svc/Container2.Photos" target="_blank">Container2.Photos</a></li>
	</ul>
	<h3>Entities</h3>
	<ul>
		<li><a href="ReferenceScenario.svc/Employees('1')" target="_blank">Employees('1')</a></li>
		<li><a href="ReferenceScenario.svc/Managers('1')" target="_blank">Managers('1')</a></li>
		<li><a href="ReferenceScenario.svc/Buildings('1')" target="_blank">Buildings('1')</a></li>
		<li><a href="ReferenceScenario.svc/Rooms('1')" target="_blank">Rooms('1')</a></li>
		<li><a href="ReferenceScenario.svc/Container2.Photos(Id=1,Type='image/png')" target="_blank">Container2.Photos(Id=1,Type='image/png')</a></li>
	</ul>
</body>
</html>
