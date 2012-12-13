package com.sap.core.odata.api.ep;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;

public abstract class ODataEntityProvider {

  private final ODataContext context;

  protected ODataEntityProvider(ODataContext ctx) throws ODataEntityProviderException {
    this.context = ctx;
  }

  public static ODataEntityProvider create(Format format, ODataContext ctx) throws ODataEntityProviderException {
    return RuntimeDelegate.createSerializer(format, ctx);
  }

  protected ODataContext getContext() {
    return this.context;
  }

  public abstract ODataEntityContent writeServiceDocument(Edm edm, String serviceRoot) throws ODataEntityProviderException;

  public ODataEntityContent writeFeed(GetEntitySetView entitySetView, List<Map<String, Object>> data, String mediaResourceMimeType) throws ODataEntityProviderException{
    ODataEpOptionalProperties properties = ODataEpOptionalProperties.builder()
        .mediaResourceMimeType(mediaResourceMimeType)
        .inlineCount(-1)
        .build();
    return writeFeed(entitySetView, data, properties);
  }

  public ODataEntityContent writeFeed(GetEntitySetView entitySetView, List<Map<String, Object>> data, ODataEpOptionalProperties optionalProperties) throws ODataEntityProviderException
  {
    return writeFeed(entitySetView, data, optionalProperties.getMediaResourceMimeType(), optionalProperties.getInlineCount(), optionalProperties.getNextSkipToken());
  }

  public abstract ODataEntityContent writeFeed(GetEntitySetView entitySetView, List<Map<String, Object>> data, String mediaResourceMimeType, int inlinecount, String nextSkiptoken) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeEntry(EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeProperty(EdmProperty edmProperty, Object value) throws ODataEntityProviderException;

  public abstract ODataEntityContent writeText(EdmProperty edmProperty, Object value) throws ODataEntityProviderException;
  
  public abstract ODataEntityContent writeMediaResource(String mimeType, byte [] data) throws ODataEntityProviderException;

  /**
   * 
   * @param entitySet
   * @param data
   * @return
   * @throws ODataEntityProviderException
   */
  public ODataEntityContent writeEntry(EdmEntitySet entitySet, Map<String, Object> data) throws ODataEntityProviderException {
    return this.writeEntry(entitySet, data, null);
  }
}
