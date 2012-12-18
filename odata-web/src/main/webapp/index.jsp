<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
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
	<ul>
		<li><a href="" target="_blank">index page</a></li>
		<li><a href="ReferenceScenario.svc?_wadl" target="_blank">wadl</a></li>
		<li><a href="ReferenceScenario.svc/" target="_blank">service document</a></li>
		<li><a href="ReferenceScenario.svc/$metadata" target="_blank">metadata</a></li>
	</ul>
</body>
</html>
