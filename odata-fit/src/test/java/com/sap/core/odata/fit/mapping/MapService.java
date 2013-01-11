package com.sap.core.odata.fit.mapping;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;

public class MapService extends ODataSingleProcessorService {

  public MapService(EdmProvider provider, ODataSingleProcessor processor) {
    super(provider, processor);
  }

}
