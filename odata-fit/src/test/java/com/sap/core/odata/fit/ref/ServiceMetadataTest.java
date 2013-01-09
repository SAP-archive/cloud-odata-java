package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

public class ServiceMetadataTest extends AbstractRefTest {

  private static String payload;

  @Before
  public void prepare() throws Exception {
    HttpResponse response = callUri("/$metadata");
    payload = getBody(response);

    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put(null, "http://schemas.microsoft.com/ado/2008/09/edm");
    prefixMap.put("edmx", "http://schemas.microsoft.com/ado/2007/06/edmx");
    prefixMap.put("m", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    prefixMap.put("a", "http://schemas.microsoft.com/ado/2008/09/edm");
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Test
  public void testGeneral() throws Exception {
    assertXpathExists("/edmx:Edmx[@Version='1.0']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices[@m:DataServiceVersion='2.0']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']", payload);
  }

  @Test
  public void testEntityTypes() throws Exception {
    //Employee
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Key/a:PropertyRef[@Name='EmployeeId']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='EmployeeId' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='EmployeeName' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationTitle']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='ManagerId' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='RoomId' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='TeamId' and @Type='Edm.String' and @MaxLength='2']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='Location' and @Type='RefScenario.c_Location']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='Age' and @Type='Edm.Int16']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='EntryDate' and @Type='Edm.DateTime' and @Nullable='true' and @m:FC_TargetPath='SyndicationUpdated']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:Property[@Name='ImageUrl' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:NavigationProperty[@Name='ne_Manager' and @Relationship='RefScenario.ManagerEmployees' and @FromRole='r_Employees' and @ToRole='r_Manager']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:NavigationProperty[@Name='ne_Team' and @Relationship='RefScenario.TeamEmployees' and @FromRole='r_Employees' and @ToRole='r_Team']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Employee' and @m:HasStream='true']/a:NavigationProperty[@Name='ne_Room' and @Relationship='RefScenario.RoomEmployees' and @FromRole='r_Employees' and @ToRole='r_Room']", payload);

    //Team
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Team' and @BaseType='RefScenario.Base']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Team' and @BaseType='RefScenario.Base']/a:Property[@Name='isScrumTeam' and @Type='Edm.Boolean' and @Nullable='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Team' and @BaseType='RefScenario.Base']/a:NavigationProperty[@Name='nt_Employees' and @Relationship='RefScenario.TeamEmployees' and @FromRole='r_Team' and @ToRole='r_Employees']", payload);

    //Room
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Room' and @BaseType='RefScenario.Base']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/a:Property[@Name='Seats' and @Type='Edm.Int16']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/a:Property[@Name='Version' and @Type='Edm.Int16' and @ConcurrencyMode='Fixed']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/a:NavigationProperty[@Name='nr_Employees' and @Relationship='RefScenario.RoomEmployees' and @FromRole='r_Room' and @ToRole='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/a:NavigationProperty[@Name='nr_Building' and @Relationship='RefScenario.BuildingRooms' and @FromRole='r_Room' and @ToRole='r_Building']", payload);

    //Manager
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Manager' and @BaseType='RefScenario.Employee' and @m:HasStream='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Manager' and @BaseType='RefScenario.Employee' and @m:HasStream='true']/a:NavigationProperty[@Name='nm_Employees' and @Relationship='RefScenario.ManagerEmployees' and @FromRole='r_Manager' and @ToRole='r_Employees']", payload);

    //Building
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']/a:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']/a:Key/a:PropertyRef[@Name='Id']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']/a:Property[@Name='Id' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']/a:Property[@Name='Name' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationAuthorName']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']/a:Property[@Name='Image' and @Type='Edm.Binary']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Building']/a:NavigationProperty[@Name='nb_Rooms' and @Relationship='RefScenario.BuildingRooms' and @FromRole='r_Building' and @ToRole='r_Room']", payload);

    //Base
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Base' and @Abstract='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Base' and @Abstract='true']/a:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Base' and @Abstract='true']/a:Key/a:PropertyRef[@Name='Id']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Base' and @Abstract='true']/a:Property[@Name='Id' and @Type='Edm.String' and @Nullable='false' and @DefaultValue='1']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityType[@Name='Base' and @Abstract='true']/a:Property[@Name='Name' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationTitle']", payload);
  }

  @Test
  public void testComplexTypes() throws Exception {
    //Location
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType[@Name='c_Location']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType[@Name='c_Location']/a:Property[@Name='City' and @Type='RefScenario.c_City']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType[@Name='c_Location']/a:Property[@Name='Country' and @Type='Edm.String']", payload);

    //Location
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType[@Name='c_City']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType[@Name='c_City']/a:Property[@Name='PostalCode' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:ComplexType[@Name='c_City']/a:Property[@Name='CityName' and @Type='Edm.String']", payload);
  }

  @Test
  public void testAssociation() throws Exception {
    //ManagerEmployees
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='ManagerEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='ManagerEmployees']/a:End[@Type='RefScenario.Employee' and @Multiplicity='*' and @Role='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='ManagerEmployees']/a:End[@Type='RefScenario.Manager' and @Multiplicity='1' and @Role='r_Manager']", payload);

    //TeamEmployees
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='TeamEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='TeamEmployees']/a:End[@Type='RefScenario.Employee' and @Multiplicity='*' and @Role='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='TeamEmployees']/a:End[@Type='RefScenario.Team' and @Multiplicity='1' and @Role='r_Team']", payload);

    //RoomEmployees
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='RoomEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='RoomEmployees']/a:End[@Type='RefScenario.Employee' and @Multiplicity='*' and @Role='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='RoomEmployees']/a:End[@Type='RefScenario.Room' and @Multiplicity='1' and @Role='r_Room']", payload);

    //BuildingRooms
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='BuildingRooms']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='BuildingRooms']/a:End[@Type='RefScenario.Building' and @Multiplicity='1' and @Role='r_Building']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:Association[@Name='BuildingRooms']/a:End[@Type='RefScenario.Room' and @Multiplicity='*' and @Role='r_Room']", payload);
  }

  @Test
  public void testEntityContainer() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']", payload);

    //EntitySets
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:EntitySet[@Name='Employees' and @EntityType='RefScenario.Employee']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:EntitySet[@Name='Teams' and @EntityType='RefScenario.Team']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:EntitySet[@Name='Rooms' and @EntityType='RefScenario.Room']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:EntitySet[@Name='Managers' and @EntityType='RefScenario.Manager']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:EntitySet[@Name='Buildings' and @EntityType='RefScenario.Building']", payload);

    //AssociationSets
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='ManagerEmployees' and @Association='RefScenario.ManagerEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='ManagerEmployees' and @Association='RefScenario.ManagerEmployees']/a:End[@EntitySet='Managers' and @Role='r_Manager']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='ManagerEmployees' and @Association='RefScenario.ManagerEmployees']/a:End[@EntitySet='Employees' and @Role='r_Employees']", payload);

    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='TeamEmployees' and @Association='RefScenario.TeamEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='TeamEmployees' and @Association='RefScenario.TeamEmployees']/a:End[@EntitySet='Teams' and @Role='r_Team']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='TeamEmployees' and @Association='RefScenario.TeamEmployees']/a:End[@EntitySet='Employees' and @Role='r_Employees']", payload);

    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='RoomEmployees' and @Association='RefScenario.RoomEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='RoomEmployees' and @Association='RefScenario.RoomEmployees']/a:End[@EntitySet='Rooms' and @Role='r_Room']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='RoomEmployees' and @Association='RefScenario.RoomEmployees']/a:End[@EntitySet='Employees' and @Role='r_Employees']", payload);

    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='BuildingRooms' and @Association='RefScenario.BuildingRooms']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='BuildingRooms' and @Association='RefScenario.BuildingRooms']/a:End[@EntitySet='Buildings' and @Role='r_Building']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:AssociationSet[@Name='BuildingRooms' and @Association='RefScenario.BuildingRooms']/a:End[@EntitySet='Rooms' and @Role='r_Room']", payload);

    //FunctionImports
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='EmployeeSearch' and @ReturnType='Collection(RefScenario.Employee)' and @m:HttpMethod='GET' and @EntitySet='Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='EmployeeSearch' and @ReturnType='Collection(RefScenario.Employee)' and @m:HttpMethod='GET' and @EntitySet='Employees']/a:Parameter[@Name='q' and @Type='Edm.String' and @Nullable='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='AllLocations' and @ReturnType='Collection(RefScenario.c_Location)' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='AllUsedRoomIds' and @ReturnType='Collection(Edm.String)' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='MaximalAge' and @ReturnType='Edm.Int16' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='MostCommonLocation' and @ReturnType='RefScenario.c_Location' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='ManagerPhoto' and @ReturnType='Edm.Binary' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='ManagerPhoto' and @ReturnType='Edm.Binary' and @m:HttpMethod='GET']/a:Parameter[@Name='Id' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema/a:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/a:FunctionImport[@Name='OldestEmployee' and @ReturnType='RefScenario.Employee' and @m:HttpMethod='GET' and @EntitySet='Employees']", payload);
  }

  @Test
  public void testSchema2() throws Exception {

    //EntityType   
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Key/a:PropertyRef[@Name='Id']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Key/a:PropertyRef[@Name='Type']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Property[@Name='Id' and @Type='Edm.Int32' and @Nullable='false' and @ConcurrencyMode='Fixed']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Property[@Name='Name' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationTitle']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Property[@Name='Type' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Property[@Name='ImageUrl' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationAuthorUri']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Property[@Name='Image' and @Type='Edm.Binary']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Property[@Name='BinaryData' and @Type='Edm.Binary' and @Nullable='true' and @m:MimeType='image/jpeg']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityType[@Name='Photo' and @m:HasStream='true']/a:Property[@Name='Содержание' and @Type='Edm.String' and @Nullable='true' and @m:FC_KeepInContent='false' and @m:FC_NsPrefix='ру' and @m:FC_NsUri='http://localhost' and @m:FC_TargetPath='Содержание']", payload);

    //EntityContainer
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityContainer[@Name='Container2']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/a:Schema[@Namespace='RefScenario2']/a:EntityContainer[@Name='Container2']/a:EntitySet[@Name='Photos' and @EntityType='RefScenario2.Photo']", payload);
  }

}
