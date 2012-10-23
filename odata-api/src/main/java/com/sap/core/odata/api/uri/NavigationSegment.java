package com.sap.core.odata.api.uri;

import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

public interface NavigationSegment {

  public List<KeyPredicate> getKeyPredicates();

  public EdmNavigationProperty getNavigationProperty();

  public EdmEntitySet getEntitySet();

}
