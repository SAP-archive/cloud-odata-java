package com.sap.core.odata.processor.api.jpa.access;

import java.util.HashMap;
import java.util.List;

public class JPAProcessorExtension {

  private JPAProcessor jpaProcessor;
  private HashMap<JPAProcessorOperation, List<String>> operationEntityMap;

  public JPAProcessor getJpaProcessor() {
    return jpaProcessor;
  }

  public void setJpaProcessor(final JPAProcessor jpaProcessor) {
    this.jpaProcessor = jpaProcessor;
  }

  public HashMap<JPAProcessorOperation, List<String>> getOperationEntityMap() {
    return operationEntityMap;
  }

  public void setOperationEntityMap(final HashMap<JPAProcessorOperation, List<String>> operationEntityMap) {
    this.operationEntityMap = operationEntityMap;
  }

}
