package com.sap.core.odata.processor.ref;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.sap.core.odata.processor.ref.jpa.Address;
import com.sap.core.odata.processor.ref.jpa.SalesOrderItem;
import com.sap.core.odata.processor.ref.jpa.SalesOrderHeader;

public class DataGenerator {

	private static final int MAX_SALES_ORDER = 10;
	private static final int MAX_LINE_ITEMS_PER_SALES_ORDER = 3;

	private EntityManager entityManager;

	public DataGenerator(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void generate() {
		this.entityManager.getTransaction().begin();
		for (int i = 0; i < DataGenerator.MAX_SALES_ORDER; i++) {
			Address ba = new Address((short) i, "Street_" + i,
					"City_" + i, "Country_" + i);
			SalesOrderHeader salesOrder = new SalesOrderHeader(i, "Test_Buyer_" + i,
					ba, "Cur_Code_" + i, (double) i, ((i % 2) == 0) ? true
							: false);
			for (int j = 0; j < DataGenerator.MAX_LINE_ITEMS_PER_SALES_ORDER; j++) {
				SalesOrderItem lineItem = new SalesOrderItem("Line_Item_" + j);
				salesOrder.getLineItems().add(lineItem);
				this.entityManager.persist(lineItem); 
			}
			this.entityManager.persist(salesOrder);
		}
		this.entityManager.getTransaction().commit();
	}

	public void clean() {
		this.entityManager.getTransaction().begin();

		TypedQuery<SalesOrderItem> queryLineItems = this.entityManager.createQuery(
				"SELECT m FROM SalesOrderItem m", SalesOrderItem.class);
		for (SalesOrderItem part : queryLineItems.getResultList()) {
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
