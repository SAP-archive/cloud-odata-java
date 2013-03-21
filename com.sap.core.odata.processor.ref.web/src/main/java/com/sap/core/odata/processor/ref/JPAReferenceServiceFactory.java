package com.sap.core.odata.processor.ref;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.ODataJPAServiceFactory;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.api.jpa.model.JPAEdmExtension;

public class JPAReferenceServiceFactory extends ODataJPAServiceFactory {

	private static final String PUNIT_NAME = "salesorderprocessing";
	private static final String MAPPING_MODEL = "SalesOrderProcessingMappingModel.xml";

	@Override
	public ODataJPAContext initializeODataJPAContext()
			throws ODataJPARuntimeException {
		ODataJPAContext oDataJPAContext = this.getODataJPAContext();

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory(PUNIT_NAME);

		oDataJPAContext.setEntityManagerFactory(emf);
		oDataJPAContext.setPersistenceUnitName(PUNIT_NAME);
		oDataJPAContext.setJPAEdmMappingModel(MAPPING_MODEL);
		oDataJPAContext
				.setJPAEdmExtension((JPAEdmExtension) new SalesOrderProcessingExtension());

		return oDataJPAContext;
	}

}
