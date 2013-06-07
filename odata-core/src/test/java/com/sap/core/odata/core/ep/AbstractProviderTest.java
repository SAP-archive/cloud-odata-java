package com.sap.core.odata.core.ep;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProvider.EntityProviderInterface;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.TombstoneCallback;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * @author SAP AG
*/
public abstract class AbstractProviderTest extends AbstractXmlProducerTestHelper {

  public AbstractProviderTest(final StreamWriterImplType type) {
    super(type);
  }

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  protected static final URI BASE_URI;

  static {
    try {
      BASE_URI = new URI("http://host:80/service/");
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
  protected static final EntityProviderWriteProperties DEFAULT_PROPERTIES = EntityProviderWriteProperties.serviceRoot(BASE_URI).build();

  protected Map<String, Object> employeeData;

  protected ArrayList<Map<String, Object>> employeesData;

  protected Map<String, Object> photoData;

  protected Map<String, Object> roomData;

  protected Map<String, Object> buildingData;

  protected ArrayList<Map<String, Object>> roomsData;

  {
    employeeData = new HashMap<String, Object>();

    Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    date.clear();
    date.set(1999, 0, 1);

    employeeData.put("EmployeeId", "1");
    employeeData.put("ImmageUrl", null);
    employeeData.put("ManagerId", "1");
    employeeData.put("Age", new Integer(52));
    employeeData.put("RoomId", "1");
    employeeData.put("EntryDate", date);
    employeeData.put("TeamId", "42");
    employeeData.put("EmployeeName", "Walter Winter");

    Map<String, Object> locationData = new HashMap<String, Object>();
    Map<String, Object> cityData = new HashMap<String, Object>();
    cityData.put("PostalCode", "33470");
    cityData.put("CityName", "Duckburg");
    locationData.put("City", cityData);
    locationData.put("Country", "Calisota");

    employeeData.put("Location", locationData);

    Map<String, Object> employeeData2 = new HashMap<String, Object>();
    employeeData2.put("EmployeeId", "1");
    employeeData2.put("ImmageUrl", null);
    employeeData2.put("ManagerId", "1");
    employeeData2.put("Age", new Integer(52));
    employeeData2.put("RoomId", "1");
    employeeData2.put("EntryDate", date);
    employeeData2.put("TeamId", "42");
    employeeData2.put("EmployeeName", "Walter Winter");

    Map<String, Object> locationData2 = new HashMap<String, Object>();
    Map<String, Object> cityData2 = new HashMap<String, Object>();
    cityData2.put("PostalCode", "33470");
    cityData2.put("CityName", "Duckburg");
    locationData2.put("City", cityData2);
    locationData2.put("Country", "Calisota");

    employeeData2.put("Location", locationData2);

    employeesData = new ArrayList<Map<String, Object>>();
    employeesData.add(employeeData);
    employeesData.add(employeeData2);

    photoData = new HashMap<String, Object>();
    photoData.put("Id", Integer.valueOf(1));
    photoData.put("Name", "Mona Lisa");
    photoData.put("Type", "image/png");
    photoData.put("ImageUrl", "http://www.mopo.de/image/view/2012/6/4/16548086,13385561,medRes,maxh,234,maxw,234,Parodia_Mona_Lisa_Lego_Hamburger_Morgenpost.jpg");
    Map<String, Object> imageData = new HashMap<String, Object>();
    imageData.put("Image", new byte[] { 1, 2, 3, 4 });
    imageData.put("getImageType", "image/png");
    photoData.put("Image", imageData);
    photoData.put("BinaryData", new byte[] { -1, -2, -3, -4 });
    photoData.put("Содержание", "В лесу шумит водопад. Если он не торопится просп воды");

    roomData = new HashMap<String, Object>();
    roomData.put("Id", "1");
    roomData.put("Name", "Neu Schwanstein");
    roomData.put("Seats", new Integer(20));
    roomData.put("Version", new Integer(3));

    buildingData = new HashMap<String, Object>();
    buildingData.put("Id", "1");
    buildingData.put("Name", "WDF03");
    buildingData.put("Image", "image");
  }

  protected void initializeRoomData(final int count) {
    roomsData = new ArrayList<Map<String, Object>>();
    for (int i = 1; i <= count; i++) {
      HashMap<String, Object> tmp = new HashMap<String, Object>();
      tmp.put("Id", "" + i);
      tmp.put("Name", "Neu Schwanstein" + i);
      tmp.put("Seats", new Integer(20));
      tmp.put("Version", new Integer(3));
      roomsData.add(tmp);
    }
  }

  @Before
  public void setXmlNamespacePrefixes() throws Exception {
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_ATOM_2005);
    prefixMap.put("d", Edm.NAMESPACE_D_2007_08);
    prefixMap.put("m", Edm.NAMESPACE_M_2007_08);
    prefixMap.put("xml", Edm.NAMESPACE_XML_1998);
    prefixMap.put("ру", "http://localhost");
    prefixMap.put("custom", "http://localhost");
    prefixMap.put("at", TombstoneCallback.NAMESPACE_TOMBSTONE);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  protected ODataContext createContextMock() throws ODataException {
    PathInfo pathInfo = mock(PathInfo.class);
    when(pathInfo.getServiceRoot()).thenReturn(BASE_URI);
    ODataContext ctx = mock(ODataContext.class);
    when(ctx.getPathInfo()).thenReturn(pathInfo);
    return ctx;
  }

  protected EntityProviderInterface createEntityProvider() throws ODataException, EdmException, EntityProviderException {
    return new ProviderFacadeImpl();
  }

  protected AtomEntityProvider createAtomEntityProvider() throws EntityProviderException {
    return new AtomEntityProvider();
  }

  public Map<String, Object> getEmployeeData() {
    return employeeData;
  }

  public List<Map<String, Object>> getEmployeesData() {
    return employeesData;
  }

  public Map<String, Object> getRoomData() {
    return roomData;
  }

  public ArrayList<Map<String, Object>> getRoomsData() {
    return roomsData;
  }

}
