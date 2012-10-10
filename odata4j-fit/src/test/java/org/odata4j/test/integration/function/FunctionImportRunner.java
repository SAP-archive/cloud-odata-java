package org.odata4j.test.integration.function;

import org.odata4j.examples.JaxRsImplementation;
import org.odata4j.examples.ODataServerFactory;
import org.odata4j.producer.resources.DefaultODataProducerProvider;

public class FunctionImportRunner {

  private final static String endpointUri = "http://localhost:8811/FunctionImportScenario.svc/";

  public static void main(String[] args) {
    DefaultODataProducerProvider.setInstance(new FunctionImportProducerMock());
    new ODataServerFactory(JaxRsImplementation.CXF).hostODataServer(FunctionImportRunner.endpointUri);
  }

}
