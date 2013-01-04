package com.sap.core.odata.api.processor.feature;

import java.util.List;

import com.sap.core.odata.api.exception.ODataException;

public interface ContentTypeSupport {
  public List<String> getSupportedContentTypes(ProcessorFeature processorAspect) throws ODataException;
}
