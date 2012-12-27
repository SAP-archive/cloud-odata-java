package com.sap.core.odata.processor.ref;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.sap.core.odata.processor.ref.jpa.BuyerAddress;
import com.sap.core.odata.processor.ref.jpa.LineItems;
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
			BuyerAddress ba = new BuyerAddress((short) i, "Street_" + i,
					"City_" + i, "Country_" + i);
			SalesOrderHeader order = new SalesOrderHeader(i, "Test_Buyer_" + i,
					ba, "Currency_Code_" + i, (double) i, ((i % 2) == 0) ? true
							: false);
			for (int j = 0; j < DataGenerator.MAX_LINE_ITEMS_PER_SALES_ORDER; j++) {
				LineItems lineItem = new LineItems("Line_Item_" + j);
				order.getLineItems().add(lineItem);
				this.entityManager.persist(lineItem);
			}
			this.entityManager.persist(order);
		}
		this.entityManager.getTransaction().commit();
	}

	public void clean() {
		this.entityManager.getTransaction().begin();

		TypedQuery<LineItems> queryPart = this.entityManager.createQuery(
				"SELECT m FROM LineItems m", LineItems.class);
		for (LineItems part : queryPart.getResultList()) {
			this.entityManager.remove(part);
		}
		TypedQuery<SalesOrderHeader> queryWhole = this.entityManager
				.createQuery("SELECT m FROM SalesOrderHeader m",
						SalesOrderHeader.class);
		for (SalesOrderHeader whole : queryWhole.getResultList()) {
			this.entityManager.remove(whole);
		}
		this.entityManager.getTransaction().commit();
	}

}
