package com.sap.core.odata.processor.ref.util;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.sap.core.odata.processor.ref.jpa.Address;
import com.sap.core.odata.processor.ref.jpa.SalesOrderItem;
import com.sap.core.odata.processor.ref.jpa.SalesOrderHeader;

public class DataGenerator {

	private static final int MAX_SALESORDER = 10;
	private static final int MAX_SALESORDERITEM_PER_SALESORDER = 3;

	private EntityManager entityManager;

	public DataGenerator(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void generate() {
		this.entityManager.getTransaction().begin();
		int count = 0;
		for (int i = 0; i < DataGenerator.MAX_SALESORDER; i++) {
			Address ba = new Address((short) i, "Street_" + i,
					"City_" + i, "Country_" + i);
			SalesOrderHeader salesOrder = new SalesOrderHeader(i, "Test_Buyer_" + i,
					ba, "Cur_Code_" + i, (double) i, ((i % 2) == 0) ? true
							: false);
			for (int j = 0; j < DataGenerator.MAX_SALESORDERITEM_PER_SALESORDER; j++) {
				SalesOrderItem salesOrderItem = new SalesOrderItem("SalesOrderItem_" + j,j,(double) ((j*200)/4));
				salesOrder.getSalesOrderItem().add(salesOrderItem);
				this.entityManager.persist(salesOrderItem); 
			}
			this.entityManager.persist(salesOrder);
			
			count++;
			
			if((i==7)&&(count==8))
			{ i--;}
		}
		this.entityManager.getTransaction().commit();
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
