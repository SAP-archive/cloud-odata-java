package com.sap.core.odata.core.edm;

import java.util.Collection;

public interface EdmFunctionImport extends EdmNamed {

  EdmParameter getParameter(String name);

  Collection<String> getParameterNames();

  EdmEntitySet getEntitySet();

  String getHttpMethod();

  EdmTyped getReturnType();

  EdmEntityContainer getEntityContainer();
}
