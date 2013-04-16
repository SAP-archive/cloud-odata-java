package com.sap.core.odata.fit.mapping;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;

/**
 * @author SAP AG
 */
public class MapFactory extends ODataServiceFactory {

  @Override
  public ODataService createService(final ODataContext ctx) throws ODataException {
    final MapProvider provider = new MapProvider();
    final MapProcessor processor = new MapProcessor();

    return createODataSingleProcessorService(provider, processor);
  }

  public static ODataService create() throws ODataException {
    return new MapFactory().createService(null);
  }

}
