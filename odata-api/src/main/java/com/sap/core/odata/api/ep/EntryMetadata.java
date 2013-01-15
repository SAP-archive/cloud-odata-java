package com.sap.core.odata.api.ep;

import java.util.Map;

public interface EntryMetadata {

  public abstract String getUri();

  public abstract Map<String, String> getAssociationUris();

  public abstract String getEtag();

  public abstract String getId();

}
