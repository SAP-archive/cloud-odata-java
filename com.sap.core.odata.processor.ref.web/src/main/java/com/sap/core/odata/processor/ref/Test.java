package com.sap.core.odata.processor.ref;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Test {
	
	public static void main(String args[]) {
		EntityManagerFactory entityMangerFactory = Persistence.createEntityManagerFactory("salesorderprocessing");
		EntityManager entityManager = entityMangerFactory.createEntityManager();
		DataGenerator dataGenerator = new DataGenerator(entityManager);

		// Check if data already exists
				
		Query q = entityManager.createQuery("SELECT COUNT(x) FROM SalesOrderHeader x");

		Number result = (Number) q.getSingleResult();

		if (result.intValue() == 0) { // Generate only if no data!

			System.err.println("****** only generate");

			dataGenerator.generate();

		} else {

			System.err.println("****** both clean and generate");

			dataGenerator.clean();

			dataGenerator.generate();

		}
		entityManager.close();

	}

}
