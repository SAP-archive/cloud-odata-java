package com.sap.core.odata.api.batch;

import java.util.List;

import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
* A BatchResponsePart
* <p> BatchResponsePart represents a distinct part of a Batch Response body. It can be a ChangeSet response or a response to a retrieve request
* @author SAP AG
*/
public abstract class BatchResponsePart {

  /**
   * Get responses. If a BatchResponsePart is a response to a retrieve request, the list consists of one response.
   * @return a list of {@link ODataResponse}
   */
  public abstract List<ODataResponse> getResponses();

  /**
   * Get the info if a BatchResponsePart is a ChangeSet response
   * @return true or false
   */
  public abstract boolean isChangeSet();

  /**
   * 
   * @param responses a list of {@link ODataResponse}
   * @return a builder object
   */
  public static BatchResponsePartBuilder responses(final List<ODataResponse> responses) {
    return newBuilder().responses(responses);
  }

  /**
   * @param isChangeSet true if a BatchResponsePart is a ChangeSet response
   * @return a builder object
   */
  public static BatchResponsePartBuilder changeSet(final boolean isChangeSet) {
    return newBuilder().changeSet(isChangeSet);
  }

  /**
   * @return returns a new builder object
   */
  public static BatchResponsePartBuilder newBuilder() {
    return BatchResponsePartBuilder.newInstance();
  }

  /**
   * Implementation of the builder pattern to create instances of this type of object. 
   * @author SAP AG
   */
  public static abstract class BatchResponsePartBuilder {
    public abstract BatchResponsePart build();

    private static BatchResponsePartBuilder newInstance() {
      return RuntimeDelegate.createBatchResponsePartBuilder();
    }

    public abstract BatchResponsePartBuilder responses(List<ODataResponse> responses);

    public abstract BatchResponsePartBuilder changeSet(boolean isChangeSet);
  }
}
