package com.sap.core.odata.processor.jpa;

import java.util.Locale;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.service.ODataServiceFactory;
import com.sap.core.odata.core.exception.MessageService.Message;
import com.sap.core.odata.processor.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.jpa.api.ODataJPAContext;
import com.sap.core.odata.processor.jpa.edm.ODataJPAEdmProvider;

/**
 * <p>
 * JPAService Factory creates an OData Service out of a JPA Persistent Unit
 * </p>
 *
 * @author SAP AG
 */

public abstract class ODataJPAServiceFactory implements ODataServiceFactory {

	private ODataJPAContext oDataJPAContext;

	public final ODataService createService(ODataContext ctx) throws ODataException {

		//Initialize OData JPA Context
		oDataJPAContext = initializeJPAContext();

		validatePreConditions( );

		// OData JPA Processor
		ODataJPAProcessor odataJPAProcessor = new ODataJPAProcessor();
		odataJPAProcessor.setOdataJPAContext(oDataJPAContext);

		// OData Entity Data Model Provider based on JPA
		ODataJPAEdmProvider edmProvider = new ODataJPAEdmProvider(oDataJPAContext);
		edmProvider.setODataJPAContext(oDataJPAContext);

		return new ODataJPAProcessorService(edmProvider, odataJPAProcessor);
	}

	private void validatePreConditions() throws ODataJPARuntimeException{

		if ( oDataJPAContext.getEntityManagerFactory() == null ){
			Message msg = new Message(Locale.ENGLISH, "EntityManagerFactory not initialized");
			throw new ODataJPARuntimeException(msg.getText());
		}


	}

	public abstract ODataJPAContext initializeJPAContext();

	public final ODataJPAContext getODataJPAContext( )
	{
		if ( oDataJPAContext == null )
				oDataJPAContext = new ODataJPAContextImpl( );
		return oDataJPAContext;

	}
}
