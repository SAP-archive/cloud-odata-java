package com.sap.core.odata.processor.ref;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.sap.core.odata.processor.jpa.ODataJPAServiceFactory;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;

public class JPAReferenceServiceFactory extends ODataJPAServiceFactory{
	
	private static final String PUNIT_NAME = "salesorderprocessing";
		
	@Override
	public ODataJPAContext initializeJPAContext() {
		ODataJPAContext oDataJPAContext= this.getODataJPAContext();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNIT_NAME);
		
		oDataJPAContext.setEntityManagerFactory(emf);
		oDataJPAContext.setPersistenceUnitName(PUNIT_NAME);
		
		EntityManager entityManager = emf.createEntityManager();
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
		return oDataJPAContext;
	}

}
