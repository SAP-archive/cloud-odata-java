package com.sap.core.odata.core.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.provider.Schema;

public class Metadata {

  private Collection<Schema> schemas;
  private double dataServiceVersion;

  public Collection<Schema> getSchemas() {
    return schemas;
  }

  public void setSchemas(Collection<Schema> schemas) {
    this.schemas = schemas;
  }

  public double getDataServiceVersion() {
    return dataServiceVersion;
  }

  public void setDataServiceVersion(double dataServiceVersion) {
    this.dataServiceVersion = dataServiceVersion;
  }

  @Override
  public String toString() {
    return "" + dataServiceVersion + schemas;
  }

}
