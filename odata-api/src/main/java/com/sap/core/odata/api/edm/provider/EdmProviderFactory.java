package com.sap.core.odata.api.edm.provider;

import java.io.InputStream;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * EDM Provider Factory which can be used to create an edm provider (e.g. from a metadata document)
 * @author SAP AG
 *
 */
public class EdmProviderFactory {

  /**
   * Creates and returns an edm provider. 
   * @param metadataXml a metadata xml input stream (means the metadata document)
   * @param validate true if semantic checks for metadata document input stream shall be done
   * @return an instance of EdmProvider
   */
  public static EdmProvider getEdmProvider(final InputStream metadataXml, final boolean validate) throws EntityProviderException {
    return RuntimeDelegate.createEdmProvider(metadataXml, validate);
  }
}