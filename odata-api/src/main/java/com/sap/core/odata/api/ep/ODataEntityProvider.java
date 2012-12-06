package com.sap.core.odata.api.ep;

import java.io.InputStream;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.rt.RuntimeDelegate;

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

  public abstract InputStream writeServiceDocument(Edm edm, String serviceRoot) throws ODataEntityProviderException;

  public abstract InputStream writeFeed(EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType) throws ODataEntityProviderException;

  public abstract InputStream writeEntry(EdmEntitySet entitySet, Map<String, Object> data, String mediaResourceMimeType) throws ODataEntityProviderException;

  public abstract InputStream writeProperty(EdmProperty edmProperty, Object value) throws ODataEntityProviderException;

  /**
   * 
   * @param entitySet
   * @param data
   * @return
   * @throws ODataEntityProviderException
   */
  public InputStream writeEntry(EdmEntitySet entitySet, Map<String, Object> data) throws ODataEntityProviderException {
    return this.writeEntry(entitySet, data, null);
  }
}
