package com.sap.core.odata.processor.ref.util;


import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.SQLCall;
import org.eclipse.persistence.sessions.Session;

import com.sap.core.odata.processor.ref.jpa.SalesOrderHeader;
import com.sap.core.odata.processor.ref.jpa.SalesOrderItem;

public class DataGenerator {

	private EntityManager entityManager;

	public DataGenerator(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void generate() {
		try {
			Session session = ((EntityManagerImpl) entityManager).getActiveSession();
			String[] resourceSQLPropFileNames = new String[]{"SalesOrderHeaderSQLs","SalesOrderItemSQLs","NoteSQLs","MaterialSQLs"};
			ResourceBundle[] resourceBundleArr = new ResourceBundle[resourceSQLPropFileNames.length];
			this.entityManager.getTransaction().begin();
			
			for (int i = 0; i < resourceSQLPropFileNames.length; i++) { //For each Entity SQL property file, 
				resourceBundleArr[i] = ResourceBundle.getBundle(resourceSQLPropFileNames[i]);//Get SQL statements as properties
				
				Set<String> keySet = resourceBundleArr[i].keySet();
				
				for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
					//this.entityManager.getTransaction().begin();
					String currentSQL = (String) iterator.next();
					String sqlQuery = resourceBundleArr[i].getString(currentSQL);
					System.out.println("Currently executing Query - "+sqlQuery);
					SQLCall sqlCall = new SQLCall(sqlQuery);
					
					DataModifyQuery query = new DataModifyQuery();
					query.setCall(sqlCall);
					session.executeQuery(query);
					//this.entityManager.getTransaction().commit();
				}
			}
			this.entityManager.getTransaction().commit();
			
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	

	public void clean() {
		this.entityManager.getTransaction().begin();

		/*TypedQuery<SalesOrderItem> querySalesOrderItem = this.entityManager.createQuery(
				"SELECT m FROM SalesOrderItem m", SalesOrderItem.class);
		for (SalesOrderItem part : querySalesOrderItem.getResultList()) {
			this.entityManager.remove(part);
		}
		TypedQuery<SalesOrderHeader> querySalesOrder = this.entityManager
				.createQuery("SELECT m FROM SalesOrderHeader m",
						SalesOrderHeader.class);
		for (SalesOrderHeader whole : querySalesOrder.getResultList()) {
			this.entityManager.remove(whole);
		}*/
		
		
		
		// Delete using SQLs
		Session session = ((EntityManagerImpl) entityManager).getActiveSession();
		ResourceBundle resourceBundle = ResourceBundle.getBundle("DataDeleteSQLs");//Get SQL statements as properties
		
		Set<String> keySet = resourceBundle.keySet();
		//session.
		
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String currentSQL = (String) iterator.next();
			String sqlQuery = resourceBundle.getString(currentSQL);
			System.out.println("In Clean(), executing Query - "+sqlQuery);
			SQLCall sqlCall = new SQLCall(sqlQuery);
			
			DataModifyQuery query = new DataModifyQuery();
			query.setCall(sqlCall);
			session.executeQuery(query);
		}
		
		
		
		this.entityManager.getTransaction().commit();
	}

}
