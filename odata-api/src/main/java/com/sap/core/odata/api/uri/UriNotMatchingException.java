package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataNotFoundException;

/**
 * URI-parsing exception resulting in a 404 Not Found response.
 * @author SAP AG
 */
public class UriNotMatchingException extends ODataNotFoundException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference MATCHPROBLEM = createMessageReference(UriNotMatchingException.class, "MATCHPROBLEM");
  public static final MessageReference NOTFOUND = createMessageReference(UriNotMatchingException.class, "NOTFOUND");
  public static final MessageReference CONTAINERNOTFOUND = createMessageReference(UriNotMatchingException.class, "CONTAINERNOTFOUND");
  public static final MessageReference ENTITYNOTFOUND = createMessageReference(UriNotMatchingException.class, "ENTITYNOTFOUND");
  public static final MessageReference PROPERTYNOTFOUND = createMessageReference(UriNotMatchingException.class, "PROPERTYNOTFOUND");

  public UriNotMatchingException(final MessageReference messageReference) {
    super(messageReference);
  }

  public UriNotMatchingException(final MessageReference messageReference, final Throwable cause) {
    super(messageReference, cause);
  }

  public UriNotMatchingException(final MessageReference messageReference, final String errorCode) {
    super(messageReference, errorCode);
  }

  public UriNotMatchingException(final MessageReference messageReference, final Throwable cause, final String errorCode) {
    super(messageReference, cause, errorCode);
  }
}
