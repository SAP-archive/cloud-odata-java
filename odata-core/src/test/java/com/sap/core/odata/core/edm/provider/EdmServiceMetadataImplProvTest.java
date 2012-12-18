package com.sap.core.odata.core.edm.provider;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.EdmProviderMock;

public class EdmServiceMetadataImplProvTest {

  private static String metadata;

  @BeforeClass
  public static void setup() throws Exception {
    EdmImplProv edmImplProv = new EdmImplProv(new EdmProviderMock());
    EdmServiceMetadata serviceMetadata = edmImplProv.getServiceMetadata();
    metadata = StringHelper.inputStreamToString(serviceMetadata.getMetadata());
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put(null, "http://schemas.microsoft.com/ado/2008/09/edm");
    prefixMap.put("edmx", "http://schemas.microsoft.com/ado/2007/06/edmx");
    prefixMap.put("m", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    prefixMap.put("a", "http://schemas.microsoft.com/ado/2008/09/edm");

    NamespaceContext ctx = new SimpleNamespaceContext(prefixMap);
    XMLUnit.setXpathNamespaceContext(ctx);
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
