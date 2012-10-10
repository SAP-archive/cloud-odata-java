package org.odata4j.test.integration.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.odata4j.edm.EdmDataServices;
import org.odata4j.format.xml.EdmxFormatParser;
import org.odata4j.stax2.XMLInputFactory2;
import org.odata4j.stax2.staximpl.StaxXMLFactoryProvider2;

public class MetadataUtil {

  public static final String TEST_FUNCTION_RETURN_STRING_DELETE = "TestFunctionReturnStringDelete";
  public static final String TEST_FUNCTION_RETURN_STRING_MERGE = "TestFunctionReturnStringMerge";
  public static final String TEST_FUNCTION_RETURN_STRING_PATCH = "TestFunctionReturnStringPatch";
  public static final String TEST_FUNCTION_RETURN_STRING_PUT = "TestFunctionReturnStringPut";
  public static final String TEST_FUNCTION_RETURN_STRING_GET = "TestFunctionReturnStringGet";
  public static final String TEST_FUNCTION_RETURN_STRING_POST = "TestFunctionReturnStringPost";
  public static final String TEST_FUNCTION_RETURN_STRING = "TestFunctionReturnString";
  public static final String TEST_FUNCTION_RETURN_BOOLEAN = "TestFunctionReturnBoolean";
  public static final String TEST_FUNCTION_RETURN_ENTITY = "TestFunctionReturnEmployee";
  public static final String TEST_FUNCTION_RETURN_COMPLEX_TYPE = "TestFunctionReturnComplexType";
  public static final String TEST_FUNCTION_RETURN_INT16 = "TestFunctionReturnInt16";
  public static final String TEST_FUNCTION_RETURN_COLLECTION_STRING = "TestFunctionReturnCollectionString";
  public static final String TEST_FUNCTION_RETURN_COLLECTION_DOUBLE = "TestFunctionReturnCollectionDouble";
  public static final String TEST_FUNCTION_RETURN_COLLECTION_COMPLEX_TYPE = "TestFunctionReturnCollectionComplexType";
  public static final String TEST_FUNCTION_RETURN_COLLECTION_ENTITY = "TestFunctionReturnCollectionEmployees";
  public static final String TEST_FUNCTION_RETURN_ENTITYSET = "TestFunctionReturnEmployees";

  private static final String REF_SCENARIO_EDMX = "/META-INF/FunctionImportScenario.edmx.xml";

  public static EdmDataServices readMetadataServiceFromFile() {
    InputStream inputStream = FunctionImportProducerMock.class.getResourceAsStream(MetadataUtil.REF_SCENARIO_EDMX);
    Reader reader = new InputStreamReader(inputStream);

    XMLInputFactory2 inputFactory = StaxXMLFactoryProvider2.getInstance().newXMLInputFactory2();
    EdmxFormatParser parser = new EdmxFormatParser();
    EdmDataServices edmDataService = parser.parseMetadata(inputFactory.createXMLEventReader(reader));

    return edmDataService;
  }

  public static String readMetadataFromFile() {
    try {
      InputStream inputStream = MetadataUtil.class.getResourceAsStream(MetadataUtil.REF_SCENARIO_EDMX);

      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line = null;
      StringBuilder stringBuilder = new StringBuilder();
      String ls = System.getProperty("line.separator");
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line);
        stringBuilder.append(ls);
      }
      return stringBuilder.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
