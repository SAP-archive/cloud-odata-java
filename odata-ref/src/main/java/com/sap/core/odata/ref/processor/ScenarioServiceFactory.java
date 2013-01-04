package com.sap.core.odata.ref.processor;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;

/**
 * @author SAP AG
 */
public class ScenarioServiceFactory implements ODataServiceFactory {

  @Override
  public ODataService createService(final ODataContext context) throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    return new ODataSingleProcessorService(
        new ScenarioEdmProvider(),
        new ListsProcessor(new ScenarioDataSource(dataContainer)));
  }
}
