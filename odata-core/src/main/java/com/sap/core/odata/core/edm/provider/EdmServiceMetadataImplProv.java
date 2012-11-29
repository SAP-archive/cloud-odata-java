package com.sap.core.odata.core.edm.provider;

import java.io.StringWriter;
import java.io.Writer;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.DataServices;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.serialization.ODataSerializationException;

public class EdmServiceMetadataImplProv implements EdmServiceMetadata {

  protected EdmProvider edmProvider;
  private double dataServiceVersion = 2.0;

  public EdmServiceMetadataImplProv(EdmProvider edmProvider) {
    this.edmProvider = edmProvider;
  }

  @Override
  public String getMetadata() throws EdmException {

    Writer writer = new StringWriter();

    //TODO Exception Handling
    try {
      DataServices metadata = new DataServices().setSchemas(edmProvider.getSchemas()).setDataServiceVersion(String.valueOf(dataServiceVersion));
      EdmMetadata.writeMetadata(metadata, writer);
    } catch (ODataSerializationException e) {
      throw new EdmException(EdmException.COMMON, e);
    } catch (ODataException e) {
      throw new EdmException(EdmException.COMMON, e);
    }

    return writer.toString();
  }

  @Override
  public String getDataServiceVersion() throws EdmException {
    return "" + this.dataServiceVersion;
  }
}