package com.sap.core.odata.processor.fit.util;


import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.SQLCall;
import org.eclipse.persistence.sessions.Session;

import com.sap.core.odata.processor.fit.jpa.Address;
import com.sap.core.odata.processor.fit.jpa.SalesOrderHeader;
import com.sap.core.odata.processor.fit.jpa.SalesOrderItem;
import com.sap.core.odata.processor.fit.jpa.SalesOrderItemKey;


/**
 * This is a utility class for generating and cleaning data. The generated data would be used by the application.
 * 
 * @author SAP AG
 *
 */
public class DataGenerator {

	private EntityManager entityManager;
	
	private static final String SQL_DELETE_CONFIG = "DataDeleteSQLs";
	private static final String SQL_DELETE_STATEMENTS_KEY = "delete_queries";

	public DataGenerator(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * This method generates data to be used in the application.
	 */
	public void generate() {
		this.entityManager.getTransaction().begin();
		System.out.println("Generating data  .");
		for (int i = 1; i <= 500; i++) {
			Address ba = new Address((short) i, "Street_" + i,
					"City_" + i, "Country_" + i);
			SalesOrderHeader salesOrder = new SalesOrderHeader(new Date(),i, "Test_Buyer_" + i,
					ba, "Cur_Cd_" + i, (double) i, ((i % 2) == 0) ? true
							: false);
			salesOrder.setSoId(i);
			salesOrder.setSalesOrderItem(new ArrayList<SalesOrderItem>());
			
			// Generate only for last 4 headers
			if(i>=496){
			//Insert 50 items for all SalesOrderHeader
			for (int j = 1; j <= 50; j++) {
				SalesOrderItem salesOrderItem = new SalesOrderItem();
				salesOrderItem.setQuantity(j);
				salesOrderItem.setAmount(j);
				salesOrderItem.setDiscount(((j*200)/4));
				
				SalesOrderItemKey salesOrderItemKey = new SalesOrderItemKey();
				salesOrderItemKey.setSoId(i);
				salesOrderItemKey.setLiId(j);
				salesOrderItem.setSalesOrderItemKey(salesOrderItemKey);
				
				salesOrder.getSalesOrderItem().add(salesOrderItem);
				//this.entityManager.persist(salesOrderItem); 
			}
			}
			this.entityManager.persist(salesOrder);
		}
		this.entityManager.getTransaction().commit();
	}
	

	private String[] getSQLDeleteStatements() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(SQL_DELETE_CONFIG);// File names from properties
		String deleteStatements = resourceBundle.getString(SQL_DELETE_STATEMENTS_KEY);
		return deleteStatements.split(",");
	}

	/**
	 * This method deletes data from JPA tables created. This method reads comma
	 * separated SQL delete statements from DataDeleteSQLs properties files and
	 * executes them in order.
	 */
	public void clean() {
		// Delete using SQLs
		String[] deleteStatements = getSQLDeleteStatements();
		if(deleteStatements.length > 0 ){ //If configuration is proper with at least one delete Statements
			Session session = ((EntityManagerImpl) entityManager).getActiveSession();
			this.entityManager.getTransaction().begin();
			for (int i = 0; i < deleteStatements.length; i++) {
				System.out.println("Cleaning - "+deleteStatements[i]);
				SQLCall sqlCall = new SQLCall(deleteStatements[i]);
				
				DataModifyQuery query = new DataModifyQuery();
				query.setCall(sqlCall);
				session.executeQuery(query);
			}
			this.entityManager.getTransaction().commit();
		}else{
			System.err.println("Delete configuration file doesn't have any delete statements.");
		}
	}

}
