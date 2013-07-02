package com.sap.core.odata.testutil.tool.core;


public class BasicTestClient extends AbstractTestClient {

  BasicTestClient(final CallerConfig config) {
    super(config);
  }

  public static BasicTestClient create(final CallerConfig config) {
    return new BasicTestClient(config);
  }
}
