package com.sap.core.odata.core.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.provider.FunctionImport;

public class EdmFunctionImportImplProv extends EdmNamedImplProv implements EdmFunctionImport {

  private FunctionImport functionImport;
  private EdmEntityContainer edmEntityContainer;
  
  public EdmFunctionImportImplProv(EdmImplProv edm, FunctionImport functionImport, EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, functionImport.getName());
    this.functionImport = functionImport;
    this.edmEntityContainer = edmEntityContainer;
    // TODO Auto-generated constructor stub
  }

  @Override
  public EdmParameter getParameter(String name) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<String> getParameterNames() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntitySet getEntitySet() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getHttpMethod() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmTyped getReturnType() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

}
