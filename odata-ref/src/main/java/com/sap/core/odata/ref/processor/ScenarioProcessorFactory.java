package com.sap.core.odata.ref.processor;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataProcessorFactory;

public class ScenarioProcessorFactory implements ODataProcessorFactory {

  @Override
  public ODataProcessor create() throws ODataError {
    return new ScenarioProcessor();
  }

}
