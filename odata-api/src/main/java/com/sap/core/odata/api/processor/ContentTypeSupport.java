package com.sap.core.odata.api.processor;

import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.aspect.ProcessorAspect;

public interface ContentTypeSupport {
  public List<String> getSupportedContentTypes(ProcessorAspect processorAspect) throws ODataException;
}
