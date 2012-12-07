package com.sap.core.odata.core.edm.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmAnnotatable;
import com.sap.core.odata.api.edm.EdmAnnotations;
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
public class EdmFunctionImportImplProv extends EdmNamedImplProv implements EdmFunctionImport, EdmAnnotatable {

  private FunctionImport functionImport;
  private EdmEntityContainer edmEntityContainer;
  private Map<String, EdmParameter> edmParameters;
  private Map<String, FunctionImportParameter> parameters;

  public EdmFunctionImportImplProv(EdmImplProv edm, FunctionImport functionImport, EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, functionImport.getName());
    this.functionImport = functionImport;
    this.edmEntityContainer = edmEntityContainer;

    buildFunctionImportParametersInternal();

    edmParameters = new HashMap<String, EdmParameter>();
  }

  private void buildFunctionImportParametersInternal() {
    this.parameters = new HashMap<String, FunctionImportParameter>();

    Collection<FunctionImportParameter> parameters = functionImport.getParameters();
    if (parameters != null) {
      FunctionImportParameter functionImportParameter;
      for (Iterator<FunctionImportParameter> iterator = parameters.iterator(); iterator.hasNext();) {
        functionImportParameter = iterator.next();
        this.parameters.put(functionImportParameter.getName(), functionImportParameter);
      }
    }
  }

  @Override
  public EdmParameter getParameter(String name) throws EdmException {
    EdmParameter parameter = null;
    if (edmParameters.containsKey(name)) {
      parameter = edmParameters.get(name);
    } else {
      parameter = createParameter(name);
    }

    return parameter;
  }

  private EdmParameter createParameter(String name) throws EdmException {
    EdmParameter edmParameter = null;
    if (parameters.containsKey(name)) {
      FunctionImportParameter parameter = parameters.get(name);
      edmParameter = new EdmParameterImplProv(edm, parameter);
      edmParameters.put(name, edmParameter);
    }
    return edmParameter;
  }

  @Override
  public Collection<String> getParameterNames() throws EdmException {
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
    return new EdmTypedImplProv(edm, functionImport.getName(), returnType.getTypeName(), returnType.getMultiplicity());
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return edmEntityContainer;
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(functionImport.getAnnotationAttributes(), functionImport.getAnnotationElements());
  }
}