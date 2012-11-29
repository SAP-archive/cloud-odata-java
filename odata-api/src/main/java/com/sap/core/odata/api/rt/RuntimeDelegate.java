package com.sap.core.odata.api.rt;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.expression.FilterParser;

/**
 * Abstract class to get odata core implementations of api interfaces
 * @author SAP AG
 */
public abstract class RuntimeDelegate {

  private static final String IMPLEMENTATION = "com.sap.core.odata.core.rt.RuntimeDelegateImpl";

  /**
   * Get a RuntimeDelegate Instance through reflection
   * @return {@link RuntimeDelegate} object
   */
  private static RuntimeDelegate getInstance() {
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
  protected abstract ODataResponseBuilder createODataResponseBuilder__();

  /**
   * Gets an {@link EdmSimpleType} implementation for a given {@link EdmSimpleTypeKind}
   * @param edmSimpleTypeKind
   * @return {@link EdmSimpleType}
   */
  protected abstract EdmSimpleType getEdmSimpleType__(EdmSimpleTypeKind edmSimpleTypeKind);

  /**
   * Get a UriParser object
   * @return {@link UriParser} object
   */
  protected abstract UriParser getUriParser__(Edm edm);

  /**
   * Gets an {@link EdmSimpleType} implementation for a given simple-type name.
   * @param edmSimpleType  name of the simple type
   * @return {@link EdmSimpleType}
   */
  protected abstract EdmSimpleType getInternalEdmSimpleTypeByString__(String edmSimpleType);

  /**
   * Gets an implementation of the EDM simple-type facade.
   * @return {@link EdmSimpleTypeFacade}
   */
  protected abstract EdmSimpleTypeFacade getSimpleTypeFacade__();

  /**
   * Creates an entity data model.
   * @param provider A {@link EdmProvider} instance
   * @return {@link Edm} implementation object
   */
  protected abstract Edm createEdm__(EdmProvider provider);

  protected abstract FilterParser getFilterParser__(Edm edm, EdmType edmType);

  /**
   * @param format serializer format
   * @return a OData serializer
   * @throws ODataException 
   */
  protected abstract ODataSerializer createSerializer__(Format format, ODataContext ctx) throws ODataSerializationException;

  public static ODataSerializer createSerializer(Format atom, ODataContext ctx) throws ODataSerializationException {
    return RuntimeDelegate.getInstance().createSerializer__(atom, ctx);
  }

  public static EdmSimpleType getEdmSimpleType(EdmSimpleTypeKind edmSimpleType) {
    return RuntimeDelegate.getInstance().getEdmSimpleType__(edmSimpleType);
  }

  public static EdmSimpleTypeFacade getSimpleTypeFacade() {
    return RuntimeDelegate.getInstance().getSimpleTypeFacade__();
  }

  public static ODataResponseBuilder createODataResponseBuilder() {
    return RuntimeDelegate.getInstance().createODataResponseBuilder__();
  }

  public static Edm createEdm(EdmProvider provider) {
    return RuntimeDelegate.getInstance().createEdm__(provider);
  }

  public static UriParser getUriParser(Edm edm) {
    return RuntimeDelegate.getInstance().getUriParser__(edm);
  }

  public static EdmSimpleType getInternalEdmSimpleTypeByString(String edmSimpleType) {
    return RuntimeDelegate.getInstance().getInternalEdmSimpleTypeByString__(edmSimpleType);
  }

  public static FilterParser getFilterParser(Edm edm, EdmType edmType) {
    return RuntimeDelegate.getInstance().getFilterParser__(edm, edmType);
  }

}
