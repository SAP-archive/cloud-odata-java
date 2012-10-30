package com.sap.core.odata.ref.processor;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataProcessorFactory;
import com.sap.core.odata.ref.model.DataContainer;

public class ScenarioProcessorFactory implements ODataProcessorFactory {

  private static DataContainer dataContainer;

  @Override
  public ODataProcessor create() throws ODataError {
    if (dataContainer == null) {
      dataContainer = new DataContainer();
      dataContainer.init();
    }
    return new CollectionsProcessor(new ScenarioDataSource(dataContainer));
  }

}
