package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataNotFoundException;

public class UriNotMatchingException extends ODataNotFoundException {

  public static final MessageReference MATCHPROBLEM = createMessageReference(UriNotMatchingException.class, "MATCHPROBLEM");
  public static final MessageReference PROPERTYNOTFOUND = createMessageReference(UriNotMatchingException.class, "PROPERTYNOTFOUND");
  public static final MessageReference NOTFOUND = createMessageReference(UriNotMatchingException.class, "NOTFOUND");
  public static final MessageReference ENTITYNOTFOUND = createMessageReference(UriNotMatchingException.class, "ENTITYNOTFOUND");
  public static final MessageReference CONTAINERNOTFOUND = createMessageReference(UriNotMatchingException.class, "CONTAINERNOTFOUND");
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * {@inheritDoc}
   */
  public UriNotMatchingException(MessageReference messageReference) {
    super(messageReference);
  }

  /**
   * {@inheritDoc}
   */
  public UriNotMatchingException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
