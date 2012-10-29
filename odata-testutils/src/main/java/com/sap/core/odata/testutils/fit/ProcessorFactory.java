package com.sap.core.odata.testutils.fit;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataProcessorFactory;

public class ProcessorFactory implements ODataProcessorFactory {

  private static ODataProcessor processor;

  @Override
  public ODataProcessor create() throws ODataError {

    if (ProcessorFactory.processor == null) {
      throw new NullPointerException();
    }

    return ProcessorFactory.processor;
  }

  public static void setProcessor(ODataProcessor processor) {
    ProcessorFactory.processor = processor;
  }

}
