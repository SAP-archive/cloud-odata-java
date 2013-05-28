package com.sap.core.odata.core.edm.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.testutil.mock.EdmTestProvider;

public class EdmxProviderTest {

  @Test
  public void testEntityType() throws EntityProviderException, ODataException, XMLStreamException {
    Edm edm = createEdm();

    assertNotNull(edm);
    FullQualifiedName fqNameEmployee = new FullQualifiedName("RefScenario", "Employee");
    EdmProvider testProvider = new EdmTestProvider();
    EdmImplProv edmImpl = (EdmImplProv) edm;
    EntityType employee = edmImpl.getEdmProvider().getEntityType(fqNameEmployee);
    EntityType testEmployee = testProvider.getEntityType(fqNameEmployee);
    assertEquals(testEmployee.getName(), employee.getName());
    assertEquals(testEmployee.isHasStream(), employee.isHasStream());
    assertEquals(testEmployee.getProperties().size(), employee.getProperties().size());
    assertEquals(testEmployee.getNavigationProperties().size(), employee.getNavigationProperties().size());

  }

  @Test
  public void tesAssociation() throws EntityProviderException, ODataException, XMLStreamException {
    Edm edm = createEdm();
    assertNotNull(edm);

    FullQualifiedName fqNameAssociation = new FullQualifiedName("RefScenario", "BuildingRooms");
    EdmProvider testProvider = new EdmTestProvider();
    EdmImplProv edmImpl = (EdmImplProv) edm;
    Association association = edmImpl.getEdmProvider().getAssociation(fqNameAssociation);
    Association testAssociation = testProvider.getAssociation(fqNameAssociation);
    assertEquals(testAssociation.getName(), association.getName());
    assertEquals(testAssociation.getEnd1().getMultiplicity(), association.getEnd1().getMultiplicity());
    assertEquals(testAssociation.getEnd2().getRole(), association.getEnd2().getRole());
    assertEquals(testAssociation.getEnd1().getType(), association.getEnd1().getType());

  }

  @Test
  public void testAssociationSet() throws EntityProviderException, ODataException, XMLStreamException {
    EdmProvider testProvider = new EdmTestProvider();
    Edm edm = createEdm();
    assertNotNull(edm);

    FullQualifiedName fqNameAssociation = new FullQualifiedName("RefScenario", "ManagerEmployees");
    EdmImplProv edmImpl = (EdmImplProv) edm;
    AssociationSet associationSet = edmImpl.getEdmProvider().getAssociationSet("Container1", fqNameAssociation, "Managers", "r_Manager");
    AssociationSet testAssociationSet = testProvider.getAssociationSet("Container1", fqNameAssociation, "Managers", "r_Manager");
    assertEquals(testAssociationSet.getName(), associationSet.getName());
    assertEquals(testAssociationSet.getEnd1().getEntitySet(), associationSet.getEnd1().getEntitySet());
    assertEquals(testAssociationSet.getEnd2().getEntitySet(), associationSet.getEnd2().getEntitySet());
    assertEquals(testAssociationSet.getEnd2().getRole(), associationSet.getEnd2().getRole());

  }

  @Test
  public void testSchema() throws EntityProviderException, ODataException, XMLStreamException {
    EdmProvider testProvider = new EdmTestProvider();
    Edm edm = createEdm();
    assertNotNull(edm);

    EdmImplProv edmImpl = (EdmImplProv) edm;
    List<Schema> schemas = edmImpl.getEdmProvider().getSchemas();
    List<Schema> testSchemas = testProvider.getSchemas();
    assertEquals(testSchemas.size(), schemas.size());

    if (!schemas.isEmpty() && !testSchemas.isEmpty()) {
      Schema schema = schemas.get(0);
      Schema testSchema = testSchemas.get(0);
      assertEquals(testSchema.getEntityContainers().size(), schema.getEntityContainers().size());
      assertEquals(testSchema.getEntityTypes().size(), schema.getEntityTypes().size());
      assertEquals(testSchema.getComplexTypes().size(), schema.getComplexTypes().size());
    }
  }

  @Test
  public void testContainer() throws EntityProviderException, ODataException, XMLStreamException {
    EdmProvider testProvider = new EdmTestProvider();
    Edm edm = createEdm();
    assertNotNull(edm);

    EdmImplProv edmImpl = (EdmImplProv) edm;
    EntityContainerInfo container = edmImpl.getEdmProvider().getEntityContainerInfo("Container2");
    EntityContainerInfo testContainer = testProvider.getEntityContainerInfo("Container2");
    assertEquals(testContainer.getName(), container.getName());
    assertEquals(testContainer.isDefaultEntityContainer(), container.isDefaultEntityContainer());

    container = edmImpl.getEdmProvider().getEntityContainerInfo(null);
    testContainer = testProvider.getEntityContainerInfo(null);
    assertNotNull(container);
    assertEquals(testContainer.getName(), container.getName());
    assertEquals(testContainer.isDefaultEntityContainer(), container.isDefaultEntityContainer());
  }

  private Edm createEdm() throws EntityProviderException, ODataException {
    EdmProvider testProvider = new EdmTestProvider();
    ODataResponse response = EntityProvider.writeMetadata(testProvider.getSchemas(), null);
    InputStream in = (InputStream) response.getEntity();
    return EntityProvider.readMetadata(in, true);

  }

}
