package com.sap.core.odata.core.edm.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
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
    final Map<String, FunctionImportParameter> parameters = functionImport.getParameters();
    if (parameters == null)
      return Collections.emptySet();
    else
      return parameters.keySet();
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
    return new EdmTyped() {

      @Override
      public String getName() throws EdmException {
        return returnType.getQualifiedName().getName();
      }

      @Override
      public EdmType getType() throws EdmException {
        final FullQualifiedName qualifiedName = returnType.getQualifiedName();
        final String namespace = qualifiedName.getNamespace();
        if (EdmSimpleTypeFacade.edmNamespace.equals(namespace)) {
          return EdmSimpleTypeFacade.getInstance(EdmSimpleTypeKind.valueOf(qualifiedName.getName()));
        } else {
          EdmType edmType = null;
          try {
             edmType = edm.getEntityType(namespace, qualifiedName.getName());
          } catch (EdmException e) {};
          if (edmType == null)
            edmType = edm.getComplexType(namespace, qualifiedName.getName());
          return edmType;
        }
      }

      @Override
      public EdmMultiplicity getMultiplicity() throws EdmException {
        return returnType.getMultiplicity();
      }
    };
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return edmEntityContainer;
  }

}
