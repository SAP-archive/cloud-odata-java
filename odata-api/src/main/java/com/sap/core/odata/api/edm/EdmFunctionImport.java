package com.sap.core.odata.api.edm;

import java.util.Collection;

public interface EdmFunctionImport extends EdmNamed {

  EdmParameter getParameter(String name) throws EdmException;

  Collection<String> getParameterNames() throws EdmException;

  EdmEntitySet getEntitySet() throws EdmException;

  String getHttpMethod() throws EdmException;

  EdmTyped getReturnType() throws EdmException;

  EdmEntityContainer getEntityContainer() throws EdmException;
}
