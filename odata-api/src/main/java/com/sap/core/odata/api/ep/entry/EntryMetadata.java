package com.sap.core.odata.api.ep.entry;

import java.util.Map;

/**
 * @author SAP AG
 */
public interface EntryMetadata {

  public abstract String getUri();

  public abstract Map<String, String> getAssociationUris();

  public abstract String getEtag();

  public abstract String getId();

}
