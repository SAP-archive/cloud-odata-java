package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 */
public class EdmParameterImplProv extends EdmElementImplProv implements EdmParameter {

  public EdmParameterImplProv(EdmImplProv edm, String name, FullQualifiedName typeName, EdmFacets edmFacets, EdmMapping edmMapping) throws EdmException {
    super(edm, name, typeName, edmFacets, edmMapping);
  }
}