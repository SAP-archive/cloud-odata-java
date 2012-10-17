package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmFunctionParameter;

import com.sap.core.odata.core.edm.EdmException;
import com.sap.core.odata.core.edm.EdmFacets;
import com.sap.core.odata.core.edm.EdmMapping;
import com.sap.core.odata.core.edm.EdmMultiplicity;
import com.sap.core.odata.core.edm.EdmParameter;
import com.sap.core.odata.core.edm.EdmType;

public class EdmParameterAdapter extends EdmNamedAdapter implements EdmParameter {

  private EdmFunctionParameter edmFunctionParameter;

  public EdmParameterAdapter(EdmFunctionParameter edmFunctionParameter) {
    super(edmFunctionParameter.getName());
    this.edmFunctionParameter = edmFunctionParameter;
  }

  @Override
  public EdmFacets getFacets() {
    return new EdmFacetsAdapter(
        this.edmFunctionParameter.isNullable(),
        null,
        this.edmFunctionParameter.getMaxLength(),
        false,
        this.edmFunctionParameter.getPrecision(),
        this.edmFunctionParameter.getScale(),
        false,
        null,
        null);
  }

  @Override
  public EdmType getType() {
    String fullQualifiedName = this.edmFunctionParameter.getType().getFullyQualifiedTypeName();
    return new EdmSimpleTypeAdapter(this.edmFunctionParameter.getType().getSimple(fullQualifiedName)).getType();
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    return EdmMultiplicity.ONE;
  }

  @Override
  public EdmMapping getMapping() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }
}
