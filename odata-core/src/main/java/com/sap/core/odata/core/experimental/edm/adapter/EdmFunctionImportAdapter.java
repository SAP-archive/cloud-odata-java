package com.sap.core.odata.core.experimental.edm.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmTyped;

public class EdmFunctionImportAdapter extends EdmNamedAdapter implements EdmFunctionImport {

  private org.odata4j.edm.EdmFunctionImport edmFunctionImport;
  private org.odata4j.edm.EdmEntityContainer edmEntityContainer;

  public EdmFunctionImportAdapter(org.odata4j.edm.EdmFunctionImport edmFunctionImport, org.odata4j.edm.EdmEntityContainer edmEntityContainer) {
    super(edmFunctionImport.getName());
    this.edmFunctionImport = edmFunctionImport;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmParameter getParameter(String name) {
    for (org.odata4j.edm.EdmFunctionParameter edmFunctionParameter : this.edmFunctionImport.getParameters()) {
      if (edmFunctionParameter.getName().equals(name)) {
        return new EdmParameterAdapter(edmFunctionParameter);
      }
    }
    return null;
  }

  @Override
  public List<String> getParameterNames() {
    List<String> edmParameterNames = new ArrayList<String>();
    for (org.odata4j.edm.EdmFunctionParameter edmFunctionParameter : this.edmFunctionImport.getParameters()) {
      edmParameterNames.add(edmFunctionParameter.getName());
    }
    return edmParameterNames;
  }

  @Override
  public EdmEntitySet getEntitySet() {
    org.odata4j.edm.EdmEntitySet edmEntitySet = this.edmFunctionImport.getEntitySet();
    if (edmEntitySet != null) {
      return new EdmEntitySetAdapter(edmEntitySet, this.edmEntityContainer);
    }
    return null;
  }

  @Override
  public String getHttpMethod() {
    return this.edmFunctionImport.getHttpMethod();
  }

  @Override
  public EdmTyped getReturnType() {
    org.odata4j.edm.EdmType edmType = this.edmFunctionImport.getReturnType();
    if (edmType != null) {
      return new EdmReturnTypeAdapter(edmType);
    }
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() {
    return new EdmEntityContainerAdapter(this.edmEntityContainer);
  }

}
