package com.sap.core.odata.core.edm.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

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
  public InputStream getMetadata() throws EdmException {
    OutputStreamWriter writer = null;

    //TODO Exception Handling
    try {
      DataServices metadata = new DataServices().setSchemas(edmProvider.getSchemas()).setDataServiceVersion(String.valueOf(dataServiceVersion));
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      writer = new OutputStreamWriter(outputStream, "UTF-8");
      EdmMetadata.writeMetadata(metadata, writer);
      return new ByteArrayInputStream(outputStream.toByteArray());
    } catch (ODataSerializationException e) {
      throw new EdmException(EdmException.COMMON, e);
    } catch (ODataException e) {
      throw new EdmException(EdmException.COMMON, e);
    } catch (UnsupportedEncodingException e) {
      throw new EdmException(EdmException.COMMON, e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          throw new EdmException(EdmException.COMMON, e);
        }
      }
    }
  }

  @Override
  public String getDataServiceVersion() throws EdmException {
    return "" + this.dataServiceVersion;
  }
}