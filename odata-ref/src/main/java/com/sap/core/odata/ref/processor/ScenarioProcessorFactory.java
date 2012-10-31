package com.sap.core.odata.ref.processor;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataProcessorFactory;
import com.sap.core.odata.ref.model.DataContainer;

/**
 * @author SAP AG
 */
public class ScenarioProcessorFactory implements ODataProcessorFactory {

  private static DataContainer dataContainer;

  @Override
  public ODataProcessor create() throws ODataException {
    if (dataContainer == null) {
      dataContainer = new DataContainer();
      dataContainer.init();
    }
    return new CollectionsProcessor(new ScenarioDataSource(dataContainer));
  }

}
