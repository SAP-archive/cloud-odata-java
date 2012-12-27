package com.sap.core.odata.api.ep;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;

/**
 * Abstract provider for writing output
 * @author SAP AG
 */
public abstract class ODataEntityProvider {

  protected ODataEntityProvider() throws ODataEntityProviderException {}

  public static ODataEntityProvider create(ContentType contentType) throws ODataEntityProviderException {
    return RuntimeDelegate.createSerializer(contentType);
  }

  public abstract ODataEntityContent writeServiceDocument(Edm edm, String serviceRoot) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeFeed(GetEntitySetView entitySetView, List<Map<String, Object>> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeEntry(EdmEntitySet entitySet, Map<String, Object> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeProperty(EdmProperty edmProperty, Object value) throws ODataEntityProviderException;

  public abstract ODataEntityContent writePropertyValue(EdmProperty edmProperty, Object value) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeText(String value) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeBinary(String mimeType, byte[] data) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeLink(EdmEntitySet entitySet, Map<String, Object> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeLinks(EdmEntitySet entitySet, List<Map<String, Object>> data, ODataEntityProviderProperties properties) throws ODataEntityProviderException;
}
