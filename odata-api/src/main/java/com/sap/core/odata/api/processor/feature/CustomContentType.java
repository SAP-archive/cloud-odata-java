package com.sap.core.odata.api.processor.feature;

import java.util.List;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;

public interface CustomContentType extends ODataProcessorFeature {
  public List<String> getCustomContentTypes(Class<? extends ODataProcessor> processorFeature) throws ODataException;
}
