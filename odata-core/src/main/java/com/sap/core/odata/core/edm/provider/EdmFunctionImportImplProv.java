package com.sap.core.odata.core.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.api.edm.provider.ReturnType;

/**
 * @author SAP AG
 */
public class EdmFunctionImportImplProv extends EdmNamedImplProv implements EdmFunctionImport {

  private FunctionImport functionImport;
  private EdmEntityContainer edmEntityContainer;
  
  public EdmFunctionImportImplProv(EdmImplProv edm, FunctionImport functionImport, EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, functionImport.getName());
    this.functionImport = functionImport;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmParameter getParameter(String name) throws EdmException {
    final FunctionImportParameter parameter = functionImport.getParameters().get(name);
    return null;
  }

  @Override
  public Collection<String> getParameterNames() throws EdmException {
    return functionImport.getParameters().keySet();
  }

  @Override
  public EdmEntitySet getEntitySet() throws EdmException {
    return edmEntityContainer.getEntitySet(functionImport.getEntitySet());
  }

  @Override
  public String getHttpMethod() throws EdmException {
    return functionImport.getHttpMethod();
  }

  @Override
  public EdmTyped getReturnType() throws EdmException {
    final ReturnType returnType = functionImport.getReturnType();
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return edmEntityContainer;
  }

}
