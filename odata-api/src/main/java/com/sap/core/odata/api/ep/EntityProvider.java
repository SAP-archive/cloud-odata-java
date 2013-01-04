package com.sap.core.odata.api.ep;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;

/**
 * Abstract provider for writing output
 * @author SAP AG
 */
public abstract class EntityProvider {

  protected EntityProvider() throws EntityProviderException {}

  public static EntityProvider create(String contentType) throws EntityProviderException {
    return RuntimeDelegate.createSerializer(contentType);
  }

  public abstract ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws EntityProviderException;

  public abstract ODataResponse writeFeed(EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException;

  public abstract ODataResponse writeEntry(EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException;

  public abstract ODataResponse writeProperty(EdmProperty edmProperty, Object value) throws EntityProviderException;

  public abstract ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws EntityProviderException;

  public abstract ODataResponse writeText(String value) throws EntityProviderException;

  public abstract ODataResponse writeBinary(String mimeType, byte[] data) throws EntityProviderException;

  public abstract ODataResponse writeLink(EdmEntitySet entitySet, Map<String, Object> data, EntityProviderProperties properties) throws EntityProviderException;

  public abstract ODataResponse writeLinks(EdmEntitySet entitySet, List<Map<String, Object>> data, EntityProviderProperties properties) throws EntityProviderException;

  public abstract ODataResponse writeFunctionImport(EdmFunctionImport functionImport, Object data, EntityProviderProperties properties) throws EntityProviderException;
}
