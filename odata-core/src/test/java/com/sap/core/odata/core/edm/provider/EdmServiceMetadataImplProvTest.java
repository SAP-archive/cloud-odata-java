package com.sap.core.odata.core.edm.provider;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.EdmTestProvider;

/**
 * @author SAP AG
 */
public class EdmServiceMetadataImplProvTest extends BaseTest {

  private static String metadata;

  @BeforeClass
  public static void setup() throws Exception {
    EdmImplProv edmImplProv = new EdmImplProv(new EdmTestProvider());
    EdmServiceMetadata serviceMetadata = edmImplProv.getServiceMetadata();
    metadata = StringHelper.inputStreamToString(serviceMetadata.getMetadata());
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_EDM_2008_09);
    prefixMap.put("edmx", Edm.NAMESPACE_EDMX_2007_06);
    prefixMap.put("m", Edm.NAMESPACE_M_2007_08);
    prefixMap.put("annoPrefix", "http://annoNamespace");

    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Test
  public void getEntitySetInfosForEmptyEdmProvider() throws Exception {
    EdmProvider edmProvider = mock(EdmProvider.class);
    EdmServiceMetadata serviceMetadata = new EdmServiceMetadataImplProv(edmProvider);

    List<EdmEntitySetInfo> infos = serviceMetadata.getEntitySetInfos();
    assertNotNull(infos);
    assertEquals(Collections.emptyList(), infos);
  }

  @Test
  public void getEntitySetInfosForEmptyEdmProviderSchemas() throws Exception {
    List<Schema> schemas = new ArrayList<Schema>();

    EdmProvider edmProvider = mock(EdmProvider.class);
    when(edmProvider.getSchemas()).thenReturn(schemas);

    EdmServiceMetadata serviceMetadata = new EdmServiceMetadataImplProv(edmProvider);

    List<EdmEntitySetInfo> infos = serviceMetadata.getEntitySetInfos();
    assertNotNull(infos);
    assertEquals(Collections.emptyList(), infos);
  }

  @Test
  public void oneEntitySetOneContainerForInfo() throws Exception {
    String entitySetUriString = new URI("Employees").toASCIIString();

    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    EntitySet entitySet = new EntitySet().setName("Employees");
    entitySets.add(entitySet);

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    EntityContainer container = new EntityContainer().setDefaultEntityContainer(true).setName("Container").setEntitySets(entitySets);
    entityContainers.add(container);

    List<Schema> schemas = new ArrayList<Schema>();
    schemas.add(new Schema().setEntityContainers(entityContainers));

    EdmProvider edmProvider = mock(EdmProvider.class);
    when(edmProvider.getSchemas()).thenReturn(schemas);

    EdmServiceMetadata serviceMetadata = new EdmServiceMetadataImplProv(edmProvider);

    List<EdmEntitySetInfo> infos = serviceMetadata.getEntitySetInfos();
    assertNotNull(infos);
    assertEquals(1, infos.size());

    assertEquals(infos.get(0).getEntitySetName(), "Employees");
    assertEquals(infos.get(0).getEntityContainerName(), "Container");
    assertEquals(infos.get(0).getEntitySetUri().toASCIIString(), entitySetUriString);
    assertTrue(infos.get(0).isDefaultEntityContainer());
  }

  @Test
  public void twoEntitySetsOneContainerForInfo() throws Exception {
    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    EntitySet entitySet = new EntitySet().setName("Employees");
    entitySets.add(entitySet);
    entitySets.add(entitySet);

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    EntityContainer container = new EntityContainer().setDefaultEntityContainer(true).setName("Container").setEntitySets(entitySets);
    entityContainers.add(container);

    List<Schema> schemas = new ArrayList<Schema>();
    schemas.add(new Schema().setEntityContainers(entityContainers));

    EdmProvider edmProvider = mock(EdmProvider.class);
    when(edmProvider.getSchemas()).thenReturn(schemas);

    EdmServiceMetadata serviceMetadata = new EdmServiceMetadataImplProv(edmProvider);

    List<EdmEntitySetInfo> infos = serviceMetadata.getEntitySetInfos();
    assertNotNull(infos);
    assertEquals(2, infos.size());
  }

  @Test
  public void twoContainersWithOneEntitySetEachForInfo() throws Exception {
    String entitySetUriString = new URI("Employees").toASCIIString();
    String entitySetUriString2 = new URI("Container2.Employees").toASCIIString();

    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    EntitySet entitySet = new EntitySet().setName("Employees");
    entitySets.add(entitySet);

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    EntityContainer container = new EntityContainer().setDefaultEntityContainer(true).setName("Container").setEntitySets(entitySets);
    entityContainers.add(container);

    EntityContainer container2 = new EntityContainer().setDefaultEntityContainer(false).setName("Container2").setEntitySets(entitySets);
    entityContainers.add(container2);

    List<Schema> schemas = new ArrayList<Schema>();
    schemas.add(new Schema().setEntityContainers(entityContainers));

    EdmProvider edmProvider = mock(EdmProvider.class);
    when(edmProvider.getSchemas()).thenReturn(schemas);

    EdmServiceMetadata serviceMetadata = new EdmServiceMetadataImplProv(edmProvider);

    List<EdmEntitySetInfo> infos = serviceMetadata.getEntitySetInfos();
    assertNotNull(infos);
    assertEquals(2, infos.size());

    assertEquals(infos.get(0).getEntitySetName(), "Employees");
    assertEquals(infos.get(0).getEntityContainerName(), "Container");
    assertEquals(infos.get(0).getEntitySetUri().toASCIIString(), entitySetUriString);
    assertTrue(infos.get(0).isDefaultEntityContainer());

    assertEquals(infos.get(1).getEntitySetName(), "Employees");
    assertEquals(infos.get(1).getEntityContainerName(), "Container2");
    assertEquals(infos.get(1).getEntitySetUri().toASCIIString(), entitySetUriString2);
    assertFalse(infos.get(1).isDefaultEntityContainer());
  }
  
  @Test
  public void oneEntitySetsOneContainerTwoSchemadForInfo() throws Exception {
    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    EntitySet entitySet = new EntitySet().setName("Employees");
    entitySets.add(entitySet);

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    EntityContainer container = new EntityContainer().setDefaultEntityContainer(true).setName("Container").setEntitySets(entitySets);
    entityContainers.add(container);

    List<Schema> schemas = new ArrayList<Schema>();
    schemas.add(new Schema().setEntityContainers(entityContainers));
    schemas.add(new Schema().setEntityContainers(entityContainers));
    
    EdmProvider edmProvider = mock(EdmProvider.class);
    when(edmProvider.getSchemas()).thenReturn(schemas);

    EdmServiceMetadata serviceMetadata = new EdmServiceMetadataImplProv(edmProvider);

    List<EdmEntitySetInfo> infos = serviceMetadata.getEntitySetInfos();
    assertNotNull(infos);
    assertEquals(2, infos.size());
  }

  @Test
  public void dataServiceVersion() throws Exception {
    EdmProvider edmProvider = mock(EdmProvider.class);
    EdmImplProv edmImplProv = new EdmImplProv(edmProvider);

    EdmServiceMetadata serviceMetadata = edmImplProv.getServiceMetadata();
    assertEquals("1.0", serviceMetadata.getDataServiceVersion());
  }

  @Test
  public void testSchemaStructure() throws Exception {
    assertXpathExists("/edmx:Edmx", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer", metadata);
  }

  @Test
  public void testEntityTypeStructure() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name and @m:HasStream]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name and @BaseType and @m:HasStream]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType/a:Key", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType/a:Key/a:PropertyRef[@Name]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType/a:Property[@Name and @Type]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType/a:Property[@Name and @Type and @Nullable and @m:FC_TargetPath]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType/a:NavigationProperty[@Name and @Relationship and @FromRole and @ToRole]", metadata);
  }

  @Test
  public void testAnnotations() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType/a:Property[@Name and @Type and @Nullable and @annoPrefix:annoName]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType/a:Property[@Name and @Type and @m:FC_TargetPath and @annoPrefix:annoName]", metadata);
  }

  @Test
  public void testComplexTypeStructure() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType[@Name]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType/a:Property[@Name and @Type]", metadata);
  }

  @Test
  public void testEntityContainerStructure() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer/a:EntitySet[@Name and @EntityType]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer/a:AssociationSet[@Name and @Association]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer//a:AssociationSet/a:End[@EntitySet and @Role]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer/a:FunctionImport[@Name and @ReturnType and @EntitySet and @m:HttpMethod]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer/a:FunctionImport/a:Parameter[@Name and @Type]", metadata);
  }

  @Test
  public void testAssociationStructure() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association/a:End[@Type and @Multiplicity and @Role]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association/a:ReferentialConstraint/a:Principal[@Role]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association/a:ReferentialConstraint/a:Principal[@Role]/a:PropertyRef[@Name]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association/a:ReferentialConstraint/a:Dependent[@Role]", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association/a:ReferentialConstraint/a:Dependent[@Role]/a:PropertyRef[@Name]", metadata);
  }

  @Test
  public void testRefScenarioContent() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee']", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Base']", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Team']", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Room']", metadata);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']", metadata);
  }
}
