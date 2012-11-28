package com.sap.core.odata.core.serializer;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializerProperties;

public class BasicSerializerProperties extends ODataSerializerProperties {

  @Override
  public void verify() throws ODataSerializationException {
    if(data == null) {
      throw new ODataSerializationException(ODataSerializationException.MISSING_PROPERTY.addContent("Data"));
    }
    if(ctx == null) {
      throw new ODataSerializationException(ODataSerializationException.MISSING_PROPERTY.addContent(ODataContext.class.getSimpleName()));      
    }
    if(edmEdmEntitySet == null) {
      throw new ODataSerializationException(ODataSerializationException.MISSING_PROPERTY.addContent(EdmEntitySet.class.getSimpleName()));            
    }
  }

}
