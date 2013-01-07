<%@page import="com.sap.core.odata.processor.jpa.api.ODataJPAContext"%>
<%@page import="java.util.List"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.Query"%>
<%@page	import="com.sap.core.odata.processor.ref.JPAReferenceServiceFactory"%>
<%@page import="com.sap.core.odata.processor.ref.util.DataGenerator"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Data Generator</title>
</head>
<body>

	<%
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("salesorderprocessing");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		DataGenerator dataGenerator = new DataGenerator(entityManager);

		request.getSession().setAttribute("entityManager", entityManager);
		request.getSession().setAttribute("entityManagerFactory", entityManagerFactory);
		request.getSession().setAttribute("dataGenerator", dataGenerator); 
	%>

	<h2>Data Generator and OData Explorer UI</h2>
	<br>
	<br>

	<a href="DataGenerator.jsp"><button type=submit>Generate Data</button></a>
	<a href="DataCleaner.jsp"><button type=submit>Clean Data</button></a>

	<br>

</body>
</html>