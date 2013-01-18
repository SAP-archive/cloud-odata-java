package com.sap.core.odata.processor.ref.util;


import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.EntityManager;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.SQLCall;
import org.eclipse.persistence.sessions.Session;

/**
 * This is a utility class for generating and cleaning data. The generated data would be used by the application.
 * 
 * @author SAP AG
 *
 */
public class DataGenerator {

	private EntityManager entityManager;
	
	/**
	 * This is configuration property to hold comma separated names of Insert Files
	 */
	private static final String SQL_INSERT_CONFIG = "SQLInsertConfig";
	
	/**
	 * This is key which will be used to fetch file names from SQL Insert Config File.
	 */
	private static final String SQL_INSERT_FILE_NAMES_KEY = "insert_file_names";
	
	private static final String SQL_DELETE_CONFIG = "DataDeleteSQLs";
	private static final String SQL_DELETE_STATEMENTS_KEY = "delete_queries";

	public DataGenerator(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * This method generates data to be used in the application. It does so by
	 * reading properties file. Currently it iterates through comma separated
	 * file names in file SQLInsertConfig and gets the insert statements from
	 * those files in the order provided in the file.
	 */
	public void generate() {
		String[] resourceSQLPropFileNames = getSQLInsertFileNames();
		if(resourceSQLPropFileNames.length > 0 ){ //If configuration is proper with at least one file
			Session session = ((EntityManagerImpl) entityManager).getActiveSession();
			ResourceBundle[] resourceBundleArr = new ResourceBundle[resourceSQLPropFileNames.length];
			this.entityManager.getTransaction().begin();
			
			for (int i = 0; i < resourceSQLPropFileNames.length; i++) { //For each Entity SQL property file, 
				System.out.println("Reading from File - "+resourceSQLPropFileNames[i]);
				resourceBundleArr[i] = ResourceBundle.getBundle(resourceSQLPropFileNames[i]);//Get SQL statements as properties
				
				Set<String> keySet = resourceBundleArr[i].keySet();
				
				for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
					String currentSQL = (String) iterator.next();
					String sqlQuery = resourceBundleArr[i].getString(currentSQL);
					System.out.println("Executing Query - "+sqlQuery);
					SQLCall sqlCall = new SQLCall(sqlQuery);
					
					DataModifyQuery query = new DataModifyQuery();
					query.setCall(sqlCall);
					session.executeQuery(query);
				}
			}
			this.entityManager.getTransaction().commit();
		}
			
	}
	

	private String[] getSQLInsertFileNames() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(SQL_INSERT_CONFIG);// File names from properties
		String namesStr = resourceBundle.getString(SQL_INSERT_FILE_NAMES_KEY);
		return namesStr.split(",");
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
