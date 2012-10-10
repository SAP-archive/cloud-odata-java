package org.odata4j.test.unit.producer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.odata4j.producer.ErrorResponseExtensions.ODATA4J_DEBUG;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.odata4j.producer.ErrorResponseExtensions;

public class ErrorResponseExtensionTest {

  private static final String CUSTOM_KEY = "custom.key";

  @Test
  public void alwaysReturnInnerErrors() throws Exception {
    assertThat(ErrorResponseExtensions.returnInnerErrors().returnInnerError(null, null, null), is(true));
  }

  @Test
  public void returnInnerErrorsIfDefaultSystemPropertyIsSetToTrue() throws Exception {
    callReturnInnerErrorWithSystemPropertyAndVerifyResult("true", true);
  }

  @Test
  public void doNotReturnInnerErrorsIfDefaultSystemPropertyIsSetToOtherValue() throws Exception {
    callReturnInnerErrorWithSystemPropertyAndVerifyResult("test", false);
  }

  @Test
  public void returnInnerErrorsIfCustomSystemPropertyIsSetToTrue() throws Exception {
    callReturnInnerErrorWithSystemPropertyAndVerifyResult(CUSTOM_KEY, "true", true);
  }

  @Test
  public void doNotReturnInnerErrorsIfCustomSystemPropertyIsSetToOtherValue() throws Exception {
    callReturnInnerErrorWithSystemPropertyAndVerifyResult(CUSTOM_KEY, "test", false);
  }

  @Test
  public void returnInnerErrorIfDefaultQueryParameterIsSetToTrue() throws Exception {
    callReturnInnerErrorWithQueryParameterAndVerifyResult("true", true);
  }

  @Test
  public void doNotReturnInnerErrorIfDefaultQueryParameterIsSetToOtherValue() throws Exception {
    callReturnInnerErrorWithQueryParameterAndVerifyResult("test", false);
  }

  @Test
  public void returnInnerErrorIfCustomQueryParameterIsSetToTrue() throws Exception {
    callReturnInnerErrorWithQueryParameterAndVerifyResult(CUSTOM_KEY, "true", true);
  }

  @Test
  public void doNotReturnInnerErrorIfCustomQueryParameterIsSetToOtherValue() throws Exception {
    callReturnInnerErrorWithQueryParameterAndVerifyResult(CUSTOM_KEY, "test", false);
  }

  private void callReturnInnerErrorWithSystemPropertyAndVerifyResult(String propertyValue, boolean expectedResult) {
    String oldDebugProperty = System.setProperty(ODATA4J_DEBUG, propertyValue);
    assertThat(ErrorResponseExtensions.returnInnerErrorsBasedOnDefaultSystemProperty().returnInnerError(null, null, null), is(expectedResult));
    restoreProperty(ODATA4J_DEBUG, oldDebugProperty);
  }

  private void callReturnInnerErrorWithSystemPropertyAndVerifyResult(String customProperty, String propertyValue, boolean expectedResult) {
    String oldCustomProperty = System.setProperty(customProperty, propertyValue);
    assertThat(ErrorResponseExtensions.returnInnerErrorsBasedOnSystemProperty(customProperty).returnInnerError(null, null, null), is(expectedResult));
    restoreProperty(customProperty, oldCustomProperty);
  }

  private void callReturnInnerErrorWithQueryParameterAndVerifyResult(String parameterValue, boolean expectedResult) {
    UriInfo uriInfoMock = mockUriInfo(ODATA4J_DEBUG, parameterValue);
    assertThat(ErrorResponseExtensions.returnInnerErrorsBasedOnDefaultQueryParameter().returnInnerError(null, uriInfoMock, null), is(expectedResult));
  }

  private void callReturnInnerErrorWithQueryParameterAndVerifyResult(String customParameter, String parameterValue, boolean expectedResult) {
    UriInfo uriInfoMock = mockUriInfo(customParameter, parameterValue);
    assertThat(ErrorResponseExtensions.returnInnerErrorsBasedOnQueryParameter(customParameter).returnInnerError(null, uriInfoMock, null), is(expectedResult));
  }

  private void restoreProperty(String propertyKey, String propertyValue) {
    if (propertyValue != null)
      System.setProperty(propertyKey, propertyValue);
    else
      System.clearProperty(propertyKey);
  }

  @SuppressWarnings("unchecked")
  private UriInfo mockUriInfo(String propertyKey, String propertyValue) {
    MultivaluedMap<String, String> queryParametersMock = mock(MultivaluedMap.class);
    when(queryParametersMock.getFirst(propertyKey)).thenReturn(propertyValue);
    UriInfo uriInfoMock = mock(UriInfo.class);
    when(uriInfoMock.getQueryParameters()).thenReturn(queryParametersMock);
    return uriInfoMock;
  }
}
