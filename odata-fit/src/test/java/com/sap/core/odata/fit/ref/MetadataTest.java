package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertFalse;

import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;

/**
 * Tests employing the reference scenario reading the metadata document in XML format
 * @author SAP AG
 */
public class MetadataTest extends AbstractRefXmlTest {

  private static String payload;

  @Before
  public void prepare() throws Exception {
    payload = getBody(callUri("$metadata"));
  }

  @Test
  public void metadataDocument() throws Exception {
    final HttpResponse response = callUri("$metadata");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertFalse(getBody(response).isEmpty());

    notFound("$invalid");
    badRequest("$metadata?$format=atom");
  }

  @Test
  public void testGeneral() throws Exception {
    assertXpathExists("/edmx:Edmx[@Version='1.0']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices[@m:DataServiceVersion='2.0']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']", payload);
  }

  @Test
  public void testEntityTypes() throws Exception {
    //Employee
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Key/edm:PropertyRef[@Name='EmployeeId']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='EmployeeId' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='EmployeeName' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationTitle']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='ManagerId' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='RoomId' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='TeamId' and @Type='Edm.String' and @MaxLength='2']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='Location' and @Type='RefScenario.c_Location']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='Age' and @Type='Edm.Int16']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='EntryDate' and @Type='Edm.DateTime' and @Nullable='true' and @m:FC_TargetPath='SyndicationUpdated']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:Property[@Name='ImageUrl' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:NavigationProperty[@Name='ne_Manager' and @Relationship='RefScenario.ManagerEmployees' and @FromRole='r_Employees' and @ToRole='r_Manager']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:NavigationProperty[@Name='ne_Team' and @Relationship='RefScenario.TeamEmployees' and @FromRole='r_Employees' and @ToRole='r_Team']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Employee' and @m:HasStream='true']/edm:NavigationProperty[@Name='ne_Room' and @Relationship='RefScenario.RoomEmployees' and @FromRole='r_Employees' and @ToRole='r_Room']", payload);

    //Team
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Team' and @BaseType='RefScenario.Base']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Team' and @BaseType='RefScenario.Base']/edm:Property[@Name='isScrumTeam' and @Type='Edm.Boolean' and @Nullable='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Team' and @BaseType='RefScenario.Base']/edm:NavigationProperty[@Name='nt_Employees' and @Relationship='RefScenario.TeamEmployees' and @FromRole='r_Team' and @ToRole='r_Employees']", payload);

    //Room
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Room' and @BaseType='RefScenario.Base']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/edm:Property[@Name='Seats' and @Type='Edm.Int16']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/edm:Property[@Name='Version' and @Type='Edm.Int16' and @ConcurrencyMode='Fixed']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/edm:NavigationProperty[@Name='nr_Employees' and @Relationship='RefScenario.RoomEmployees' and @FromRole='r_Room' and @ToRole='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Room' and @BaseType='RefScenario.Base']/edm:NavigationProperty[@Name='nr_Building' and @Relationship='RefScenario.BuildingRooms' and @FromRole='r_Room' and @ToRole='r_Building']", payload);

    //Manager
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Manager' and @BaseType='RefScenario.Employee' and @m:HasStream='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Manager' and @BaseType='RefScenario.Employee' and @m:HasStream='true']/edm:NavigationProperty[@Name='nm_Employees' and @Relationship='RefScenario.ManagerEmployees' and @FromRole='r_Manager' and @ToRole='r_Employees']", payload);

    //Building
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Building']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Building']/edm:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Building']/edm:Key/edm:PropertyRef[@Name='Id']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Building']/edm:Property[@Name='Id' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Building']/edm:Property[@Name='Name' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationAuthorName']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Building']/edm:Property[@Name='Image' and @Type='Edm.Binary']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Building']/edm:NavigationProperty[@Name='nb_Rooms' and @Relationship='RefScenario.BuildingRooms' and @FromRole='r_Building' and @ToRole='r_Room']", payload);

    //Base
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Base' and @Abstract='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Base' and @Abstract='true']/edm:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Base' and @Abstract='true']/edm:Key/edm:PropertyRef[@Name='Id']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Base' and @Abstract='true']/edm:Property[@Name='Id' and @Type='Edm.String' and @Nullable='false' and @DefaultValue='1']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityType[@Name='Base' and @Abstract='true']/edm:Property[@Name='Name' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationTitle']", payload);
  }

  @Test
  public void testComplexTypes() throws Exception {
    //Location
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:ComplexType[@Name='c_Location']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:ComplexType[@Name='c_Location']/edm:Property[@Name='City' and @Type='RefScenario.c_City']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:ComplexType[@Name='c_Location']/edm:Property[@Name='Country' and @Type='Edm.String']", payload);

    //Location
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:ComplexType[@Name='c_City']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:ComplexType[@Name='c_City']/edm:Property[@Name='PostalCode' and @Type='Edm.String']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:ComplexType[@Name='c_City']/edm:Property[@Name='CityName' and @Type='Edm.String']", payload);
  }

  @Test
  public void testAssociation() throws Exception {
    //ManagerEmployees
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='ManagerEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='ManagerEmployees']/edm:End[@Type='RefScenario.Employee' and @Multiplicity='*' and @Role='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='ManagerEmployees']/edm:End[@Type='RefScenario.Manager' and @Multiplicity='1' and @Role='r_Manager']", payload);

    //TeamEmployees
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='TeamEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='TeamEmployees']/edm:End[@Type='RefScenario.Employee' and @Multiplicity='*' and @Role='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='TeamEmployees']/edm:End[@Type='RefScenario.Team' and @Multiplicity='1' and @Role='r_Team']", payload);

    //RoomEmployees
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='RoomEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='RoomEmployees']/edm:End[@Type='RefScenario.Employee' and @Multiplicity='*' and @Role='r_Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='RoomEmployees']/edm:End[@Type='RefScenario.Room' and @Multiplicity='1' and @Role='r_Room']", payload);

    //BuildingRooms
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='BuildingRooms']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='BuildingRooms']/edm:End[@Type='RefScenario.Building' and @Multiplicity='1' and @Role='r_Building']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:Association[@Name='BuildingRooms']/edm:End[@Type='RefScenario.Room' and @Multiplicity='*' and @Role='r_Room']", payload);
  }

  @Test
  public void testEntityContainer() throws Exception {
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']", payload);

    //EntitySets
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:EntitySet[@Name='Employees' and @EntityType='RefScenario.Employee']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:EntitySet[@Name='Teams' and @EntityType='RefScenario.Team']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:EntitySet[@Name='Rooms' and @EntityType='RefScenario.Room']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:EntitySet[@Name='Managers' and @EntityType='RefScenario.Manager']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:EntitySet[@Name='Buildings' and @EntityType='RefScenario.Building']", payload);

    //AssociationSets
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='ManagerEmployees' and @Association='RefScenario.ManagerEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='ManagerEmployees' and @Association='RefScenario.ManagerEmployees']/edm:End[@EntitySet='Managers' and @Role='r_Manager']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='ManagerEmployees' and @Association='RefScenario.ManagerEmployees']/edm:End[@EntitySet='Employees' and @Role='r_Employees']", payload);

    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='TeamEmployees' and @Association='RefScenario.TeamEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='TeamEmployees' and @Association='RefScenario.TeamEmployees']/edm:End[@EntitySet='Teams' and @Role='r_Team']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='TeamEmployees' and @Association='RefScenario.TeamEmployees']/edm:End[@EntitySet='Employees' and @Role='r_Employees']", payload);

    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='RoomEmployees' and @Association='RefScenario.RoomEmployees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='RoomEmployees' and @Association='RefScenario.RoomEmployees']/edm:End[@EntitySet='Rooms' and @Role='r_Room']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='RoomEmployees' and @Association='RefScenario.RoomEmployees']/edm:End[@EntitySet='Employees' and @Role='r_Employees']", payload);

    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='BuildingRooms' and @Association='RefScenario.BuildingRooms']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='BuildingRooms' and @Association='RefScenario.BuildingRooms']/edm:End[@EntitySet='Buildings' and @Role='r_Building']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:AssociationSet[@Name='BuildingRooms' and @Association='RefScenario.BuildingRooms']/edm:End[@EntitySet='Rooms' and @Role='r_Room']", payload);

    //FunctionImports
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='EmployeeSearch' and @ReturnType='Collection(RefScenario.Employee)' and @m:HttpMethod='GET' and @EntitySet='Employees']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='EmployeeSearch' and @ReturnType='Collection(RefScenario.Employee)' and @m:HttpMethod='GET' and @EntitySet='Employees']/edm:Parameter[@Name='q' and @Type='Edm.String' and @Nullable='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='AllLocations' and @ReturnType='Collection(RefScenario.c_Location)' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='AllUsedRoomIds' and @ReturnType='Collection(Edm.String)' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='MaximalAge' and @ReturnType='Edm.Int16' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='MostCommonLocation' and @ReturnType='RefScenario.c_Location' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='ManagerPhoto' and @ReturnType='Edm.Binary' and @m:HttpMethod='GET']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='ManagerPhoto' and @ReturnType='Edm.Binary' and @m:HttpMethod='GET']/edm:Parameter[@Name='Id' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema/edm:EntityContainer[@Name='Container1' and @m:IsDefaultEntityContainer='true']/edm:FunctionImport[@Name='OldestEmployee' and @ReturnType='RefScenario.Employee' and @m:HttpMethod='GET' and @EntitySet='Employees']", payload);
  }

  @Test
  public void testSchema2() throws Exception {

    //EntityType   
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Key", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Key/edm:PropertyRef[@Name='Id']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Key/edm:PropertyRef[@Name='Type']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Property[@Name='Id' and @Type='Edm.Int32' and @Nullable='false' and @ConcurrencyMode='Fixed']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Property[@Name='Name' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationTitle']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Property[@Name='Type' and @Type='Edm.String' and @Nullable='false']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Property[@Name='ImageUrl' and @Type='Edm.String' and @m:FC_TargetPath='SyndicationAuthorUri']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Property[@Name='Image' and @Type='Edm.Binary']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Property[@Name='BinaryData' and @Type='Edm.Binary' and @Nullable='true' and @m:MimeType='image/jpeg']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityType[@Name='Photo' and @m:HasStream='true']/edm:Property[@Name='Содержание' and @Type='Edm.String' and @Nullable='true' and @m:FC_KeepInContent='false' and @m:FC_NsPrefix='ру' and @m:FC_NsUri='http://localhost' and @m:FC_TargetPath='Содержание']", payload);

    //EntityContainer
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityContainer[@Name='Container2']", payload);
    assertXpathExists("/edmx:Edmx/edmx:DataServices/edm:Schema[@Namespace='RefScenario2']/edm:EntityContainer[@Name='Container2']/edm:EntitySet[@Name='Photos' and @EntityType='RefScenario2.Photo']", payload);
  }

}
