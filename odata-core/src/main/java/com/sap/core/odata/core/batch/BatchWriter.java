package com.sap.core.odata.core.batch;

import java.util.UUID;

public abstract class BatchWriter {
  protected String generateBoundary(final String value) {
    return value + "_" + UUID.randomUUID().toString();
  }

}
