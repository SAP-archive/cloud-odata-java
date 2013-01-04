package com.sap.core.odata.api.ep;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;

/**
 * Abstract provider for writing output
 * @author SAP AG
 */
public abstract class ODataEntityProvider {

  protected ODataEntityProvider() throws ODataEntityProviderException {}

  public static ODataEntityProvider create(String contentType) throws ODataEntityProviderException {
    return RuntimeDelegate.createSerializer(contentType);
  }

  public abstract ODataResponse writeServiceDocument(Edm edm, String serviceRoot) throws ODataEntityProviderException;

  public abstract ODataResponse writeFeed(GetEntitySetView entitySetView, List<Map<String, Object>> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;

  public abstract ODataResponse writeEntry(EdmEntitySet entitySet, Map<String, Object> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;

  public abstract ODataResponse writeProperty(EdmProperty edmProperty, Object value) throws ODataEntityProviderException;

  public abstract ODataResponse writePropertyValue(EdmProperty edmProperty, Object value) throws ODataEntityProviderException;

  public abstract ODataResponse writeText(String value) throws ODataEntityProviderException;

  public abstract ODataResponse writeBinary(String mimeType, byte[] data) throws ODataEntityProviderException;

  public abstract ODataResponse writeLink(EdmEntitySet entitySet, Map<String, Object> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;

  public abstract ODataResponse writeLinks(EdmEntitySet entitySet, List<Map<String, Object>> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;

  public abstract ODataResponse writeFunctionImport(EdmFunctionImport functionImport, Object data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;
}
