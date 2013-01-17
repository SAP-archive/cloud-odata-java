package com.sap.core.odata.processor.ref.util;

//import java.util.Date;

import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.queries.SQLCall;
import org.eclipse.persistence.sessions.Session;

//import com.sap.core.odata.processor.ref.jpa.Address;
//import com.sap.core.odata.processor.ref.jpa.Note;
import com.sap.core.odata.processor.ref.jpa.SalesOrderItem;
import com.sap.core.odata.processor.ref.jpa.SalesOrderHeader;
//import com.sap.core.odata.processor.ref.jpa.SalesOrderItemKey;

public class DataGenerator {

	//private static final int MAX_SALESORDER = 10;
	//private static final int MAX_SALESORDERITEM_PER_SALESORDER = 3;

	private EntityManager entityManager;

	public DataGenerator(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void generate() {
		this.entityManager.getTransaction().begin();
		/*int count = 0;
		for (int i = 0; i < DataGenerator.MAX_SALESORDER; i++) {
			Address ba = new Address((short) i, "Street_" + i,
					"City_" + i, "Country_" + i);
			SalesOrderHeader salesOrder = new SalesOrderHeader(i, "Test_Buyer_" + i,
					ba, "Cur_Code_" + i, (double) i, ((i % 2) == 0) ? true
							: false);
			for (int j = 0; j < DataGenerator.MAX_SALESORDERITEM_PER_SALESORDER; j++) {
				SalesOrderItem salesOrderItem = new SalesOrderItem("SalesOrderItem_" + j,j,(double) ((j*200)/4));
				salesOrderItem.setSalesOrderItemKey(new SalesOrderItemKey(j*100));
				salesOrder.getSalesOrderItem().add(salesOrderItem);
				this.entityManager.persist(salesOrderItem); 
			}
			
			Note note = new Note("Created_By_" + i, new Date(), "Text_" + i);
			salesOrder.setNote(note);
			this.entityManager.persist(note);
			
			this.entityManager.persist(salesOrder);
			
			count++;
			
			if((i==7)&&(count==8))
			{ i--;}
		}*/
		generatedDataFromFile();
		this.entityManager.getTransaction().commit();
	}
	
	private void generatedDataFromFile() {
		try {
			Session session = ((EntityManagerImpl) entityManager).getActiveSession();
			ResourceBundle resourceBundle = ResourceBundle.getBundle("SQLStatements");
			Set<String> keySet = resourceBundle.keySet();
			//int i = 0;
			for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
				this.entityManager.getTransaction().begin();
				String currentSQL = (String) iterator.next();
				String sqlQuery = resourceBundle.getString(currentSQL);
				System.out.println("5 Currently executing Query - "+sqlQuery);
				SQLCall sqlCall = new SQLCall(sqlQuery);
//				if(i==0){
//					// Create table
//					org.eclipse.persistence.queries.
//				}
				
				DataModifyQuery query = new DataModifyQuery();
				query.setCall(sqlCall);
				session.executeQuery(query);
				this.entityManager.getTransaction().commit();
			}
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void clean() {
		this.entityManager.getTransaction().begin();

		TypedQuery<SalesOrderItem> querySalesOrderItem = this.entityManager.createQuery(
				"SELECT m FROM SalesOrderItem m", SalesOrderItem.class);
		for (SalesOrderItem part : querySalesOrderItem.getResultList()) {
			this.entityManager.remove(part);
		}
		TypedQuery<SalesOrderHeader> querySalesOrder = this.entityManager
				.createQuery("SELECT m FROM SalesOrderHeader m",
						SalesOrderHeader.class);
		for (SalesOrderHeader whole : querySalesOrder.getResultList()) {
			this.entityManager.remove(whole);
		}
		this.entityManager.getTransaction().commit();
	}

}
