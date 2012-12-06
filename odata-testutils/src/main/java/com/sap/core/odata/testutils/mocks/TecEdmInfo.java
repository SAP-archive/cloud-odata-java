package com.sap.core.odata.testutils.mocks;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.ComplexProperty;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EdmProviderDefault;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.edm.provider.SimpleProperty;
import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Provider for the entity data model used in the reference scenario
 * @author SAP AG
 */
public class TecEdmInfo
{
  private Edm edm;

  public TecEdmInfo(Edm edm)
  {
    this.edm = edm;
  }
  
  public EdmEntityType getEtAllTypes()
  {
    try {
      return edm
          .getEntityContainer(TechnicalScenarioEdmProvider.ENTITY_CONTAINER_1)
          .getEntitySet(TechnicalScenarioEdmProvider.ES_ALL_TYPES)
          .getEntityType();
    } catch (EdmException e) {
      fail("Error in test setup" + e.getLocalizedMessage());
    }
    return null;
  }
}
