package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

public class DataServices {

  private Collection<Schema> schemas;
  private String dataServiceVersion;

  public DataServices setSchemas(Collection<Schema> schemas) {
    this.schemas = schemas;
    return this;
  }

  public DataServices setDataServiceVersion(String dataServiceVersion) {
    this.dataServiceVersion = dataServiceVersion;
    return this;
  }

  public Collection<Schema> getSchemas() {
    return schemas;
  }

  public String getDataServiceVersion() {
    return dataServiceVersion;
  }
}