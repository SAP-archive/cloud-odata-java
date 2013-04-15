package com.sap.core.odata.processor.fit;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import com.sap.core.odata.processor.api.jpa.ODataJPAContext;
import com.sap.core.odata.processor.api.jpa.ODataJPAServiceFactory;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;

public class JPAReferenceServiceFactory extends ODataJPAServiceFactory{
	
	private static final String PUNIT_NAME = "salesorderprocessing";
		
	@Override
	public ODataJPAContext initializeJPAContext() throws ODataJPARuntimeException  {
		DataSource ds = null;
		try {
			InitialContext ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		Map<String, DataSource> properties = new HashMap<String, DataSource>();
		properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
		//properties.put("eclipselink.target-database", "com.sap.persistence.platform.database.HDBPlatform");
		ODataJPAContext oDataJPAContext= this.getODataJPAContext();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PUNIT_NAME,properties);
		oDataJPAContext.setEntityManagerFactory(emf);
		oDataJPAContext.setPersistenceUnitName(PUNIT_NAME); 
		
		return oDataJPAContext;
	}

}
