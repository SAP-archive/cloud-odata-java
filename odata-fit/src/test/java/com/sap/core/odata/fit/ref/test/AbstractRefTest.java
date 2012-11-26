package com.sap.core.odata.fit.ref.test;

import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.ref.edm.ScenarioEdmProvider;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutils.fit.AbstractFitTest;

public class AbstractRefTest extends AbstractFitTest {

  @Override
  protected EdmProvider createEdmProviderMock() {
    
    return new ScenarioEdmProvider();
  }

  @Override
  protected ODataSingleProcessor createProcessorMock() throws ODataException {
    DataContainer dataContainer = new DataContainer();
    dataContainer.reset();
    return new ListsProcessor(new ScenarioDataSource(dataContainer));
  }

}
