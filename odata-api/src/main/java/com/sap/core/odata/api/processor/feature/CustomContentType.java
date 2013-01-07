package com.sap.core.odata.api.processor.feature;

import java.util.List;

import com.sap.core.odata.api.exception.ODataException;

public interface CustomContentType extends ProcessorFeature {
  public List<String> getCustomContentTypes(Class<? extends ProcessorFeature> processorFeature) throws ODataException;
}
