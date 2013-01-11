package com.sap.core.odata.fit.mapping;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;

public class MapFactory implements ODataServiceFactory {

  @Override
  public ODataSingleProcessorService createService(ODataContext ctx) throws ODataException {
    MapProvider provider = new MapProvider();
    MapProcessor processor = new MapProcessor();

    return new MapService(provider, processor);
  }

  public static ODataSingleProcessorService create() throws ODataException {
    return new MapFactory().createService(null);
  }

}
