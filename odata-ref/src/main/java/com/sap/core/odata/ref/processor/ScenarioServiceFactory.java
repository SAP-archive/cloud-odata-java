package com.sap.core.odata.ref.processor;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.service.ODataServiceFactory;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;

/**
 * @author SAP AG
 */
public class ScenarioServiceFactory implements ODataServiceFactory {

  @Override
  public ODataProcessor createProcessor() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.init();
    return new ListsProcessor(new ScenarioDataSource(dataContainer));
  }

  @Override
  public EdmProvider createProvider() throws ODataException {
    return new ScenarioEdmProvider();
  }

}
