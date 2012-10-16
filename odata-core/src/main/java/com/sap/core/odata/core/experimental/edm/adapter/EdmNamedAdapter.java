package com.sap.core.odata.core.experimental.edm.adapter;

import com.sap.core.odata.core.edm.EdmNamed;

public class EdmNamedAdapter implements EdmNamed {

  private String name;

  public EdmNamedAdapter(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

}
