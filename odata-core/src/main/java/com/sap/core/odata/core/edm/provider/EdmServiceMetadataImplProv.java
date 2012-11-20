package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;

public class EdmServiceMetadataImplProv implements EdmServiceMetadata {

  protected EdmProvider edmProvider;
  private double dataServiceVersion;
  
  public EdmServiceMetadataImplProv(EdmProvider edmProvider) {
    this.edmProvider = edmProvider;
  }

  @Override
  public String getMetadata() throws EdmException {
    Metadata metadata = new Metadata();
    try {
      metadata.setSchemas(edmProvider.getSchemas());
    } catch (ODataException e) {
      throw new EdmException(EdmException.COMMON, e);
    }
    metadata.setDataServiceVersion(this.dataServiceVersion);
    return metadata.serialize();

  }

  @Override
  public String getDataServiceVersion() throws EdmException {

    return "" + this.dataServiceVersion;
  }

}
