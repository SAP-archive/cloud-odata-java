package com.sap.core.odata.core.serialization.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.core.serializer.AtomEntrySerializer;
import com.sap.core.odata.testutils.helper.XMLUnitHelper;

public abstract class AbstractSerializerTest {

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  protected static final URI BASE_URI;
  static {
    try {
      BASE_URI = new URI("http://host:port/särvice/");
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  protected Map<String, Object> employeeData;

  {
    this.employeeData = new HashMap<String, Object>();

    Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    date.clear();
    date.set(1999, 0, 1);

    this.employeeData.put("EmployeeId", "1");
    this.employeeData.put("ImmageUrl", null);
    this.employeeData.put("ManagerId", "1");
    this.employeeData.put("Age", new Integer(52));
    this.employeeData.put("RoomId", "1");
    this.employeeData.put("EntryDate", date);
    this.employeeData.put("TeamId", "42");
    this.employeeData.put("EmployeeName", "Walter Winter");

    Map<String, Object> locationData = new HashMap<String, Object>();
    Map<String, Object> cityData = new HashMap<String, Object>();
    cityData.put("PostalCode", "33470");
    cityData.put("CityName", "Duckburg");
    locationData.put("City", cityData);
    locationData.put("Country", "Calisota");

    this.employeeData.put("Location", locationData);

  }

  @Before
  public void before() throws EdmException {
    Map<String, String> ns = new HashMap<String, String>();
    ns.put("d", AtomEntrySerializer.NS_DATASERVICES);
    ns.put("m", AtomEntrySerializer.NS_DATASERVICES_METADATA);
    ns.put("a", AtomEntrySerializer.NS_ATOM);
    XMLUnitHelper.registerXmlNs(ns);
  }

  protected ODataContext createContextMock() throws ODataException {
    ODataUriInfo uriInfo = mock(ODataUriInfo.class);
    when(uriInfo.getBaseUri()).thenReturn(BASE_URI);
    ODataContext ctx = mock(ODataContext.class);
    when(ctx.getUriInfo()).thenReturn(uriInfo);
    return ctx;
  }

  protected ODataSerializer createAtomSerializer() throws ODataException, EdmException, ODataSerializationException {
    ODataSerializer ser = ODataSerializer.create(Format.ATOM, createContextMock());
    return ser;
  }

}
