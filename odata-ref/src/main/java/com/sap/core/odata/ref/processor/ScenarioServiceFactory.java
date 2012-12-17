package com.sap.core.odata.ref.processor;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.service.ODataServiceFactory;
import com.sap.core.odata.api.service.ODataSingleProcessorService;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;

/**
 * @author SAP AG
 */
public class ScenarioServiceFactory implements ODataServiceFactory {

  @Override
  public ODataService createService() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    ODataService service = new ODataSingleProcessorService(new ScenarioEdmProvider(), new ListsProcessor(new ScenarioDataSource(dataContainer))) {};
    return service;
  }

}
