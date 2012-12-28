<%@page import="com.sap.core.odata.processor.jpa.api.ODataJPAContext"%>
<%@page import="java.util.List"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.EntityManagerFactory"%>
<%@page import="javax.persistence.Persistence"%>
<%@page import="javax.persistence.Query"%>
<%@page
	import="com.sap.core.odata.processor.ref.JPAReferenceServiceFactory"%>
<%@page import="com.sap.core.odata.processor.ref.DataGenerator"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Data Cleaner</title>
</head>
<body>

	<%
		EntityManagerFactory entityMangerFactory = (EntityManagerFactory) request.getSession().getAttribute("entityManagerFactory");
			EntityManager entityManager = (EntityManager) request.getSession().getAttribute("entityManager");
			DataGenerator dataGenerator = (DataGenerator) request.getSession().getAttribute("dataGenerator");
			
			// Check if data already exists
			Query q = entityManager.createQuery("SELECT COUNT(x) FROM SalesOrderHeader x"); 

			Number result = (Number) q.getSingleResult();

		if (result.intValue() > 0) { // Generate only if no data!
			dataGenerator.clean();
			/* if (entityManager != null) {
				entityManager.close();
			}
			if (entityManager != null) {
				entityMangerFactory.close();
			} */
	%>
		<h2>Data has been cleaned.... Totally <%=result%> items deleted</h2>
	<%
		} else {
	%>
	<h2>No data existing to clean</h2>
	<%
		}
	%>

</body>
</html>