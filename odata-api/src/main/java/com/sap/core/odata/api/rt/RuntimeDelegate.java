package com.sap.core.odata.api.rt;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.uri.UriParser;

/**
 * @author SAP AG
 * Abstract class to get odata core implementations of api interfaces
 */
public abstract class RuntimeDelegate {

  private static final String IMPLEMENTATION = "com.sap.core.odata.core.rest.RuntimeDelegateImpl";

  /**
   * Get a RuntimeDelegate Instance through reflection
   * @return {@link RuntimeDelegate} object
   */
  public static RuntimeDelegate getInstance() {
    RuntimeDelegate delegate;

    try {
      Class<?> clazz = Class.forName(RuntimeDelegate.IMPLEMENTATION);

      Object object = clazz.newInstance();
      delegate = (RuntimeDelegate) object;

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return delegate;
  }

  /**
   * Get a OData response builder
   * @return {@link ODataResponseBuilder} object
   */
  public abstract ODataResponseBuilder createODataResponseBuilder();


  /**
   * Get an EdmSimpleType implementation for a given {@link EdmSimpleTypeKind}
   * @param edmSimpleTypeKind
   * @return {@link EdmSimpleType}
   */
  public abstract EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleTypeKind);

  /**
   * Get a UriParser object
   * @return {@link UriParser} object
   */
  public abstract UriParser getUriParser(Edm edm);

  public abstract EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType);
  
  public abstract EdmSimpleTypeFacade getSimpleTypeFacade();

}
