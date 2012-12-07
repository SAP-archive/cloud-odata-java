package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * Objects of this class represent the data service. They contain all schemas of the EDM as well as the dataServiceVersion
 * @author SAP AG
 */
public class DataServices {

  private Collection<Schema> schemas;
  private String dataServiceVersion;

  /**
   * Sets the schemas for this {@link DataServices}
   * @param schemas
   * @return {@link DataServices} for method chaining
   */
  public DataServices setSchemas(Collection<Schema> schemas) {
    this.schemas = schemas;
    return this;
  }

  /**
   * Sets the data service version for this {@link DataServices}
   * @param dataServiceVersion
   * @return {@link DataServices} for method chaining
   */
  public DataServices setDataServiceVersion(String dataServiceVersion) {
    this.dataServiceVersion = dataServiceVersion;
    return this;
  }

  /**
   * @return Collection<{@link Schema}>
   */
  public Collection<Schema> getSchemas() {
    return schemas;
  }

  /**
   * @return <b>String</b> data service version
   */
  public String getDataServiceVersion() {
    return dataServiceVersion;
  }
}