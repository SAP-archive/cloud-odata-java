/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent the data service. They contain all schemas of the EDM as well as the dataServiceVersion
 * @author SAP AG
 */
public class DataServices {

  private List<Schema> schemas;
  private String dataServiceVersion;

  /**
   * Sets the schemas for this {@link DataServices}
   * @param schemas
   * @return {@link DataServices} for method chaining
   */
  public DataServices setSchemas(final List<Schema> schemas) {
    this.schemas = schemas;
    return this;
  }

  /**
   * Sets the data service version for this {@link DataServices}
   * @param dataServiceVersion
   * @return {@link DataServices} for method chaining
   */
  public DataServices setDataServiceVersion(final String dataServiceVersion) {
    this.dataServiceVersion = dataServiceVersion;
    return this;
  }

  /**
   * @return List<{@link Schema}>
   */
  public List<Schema> getSchemas() {
    return schemas;
  }

  /**
   * @return <b>String</b> data service version
   */
  public String getDataServiceVersion() {
    return dataServiceVersion;
  }
}