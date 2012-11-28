package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class UriParserException extends ODataMessageException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference URISYNTAX = createMessageReference(UriParserException.class, "URISYNTAX");
  public static final MessageReference MATCHPROBLEM = createMessageReference(UriParserException.class, "MATCHPROBLEM");
  public static final MessageReference ENTITYSETINSTEADOFENTITY = createMessageReference(UriParserException.class, "ENTITYSETINSTEADOFENTITY");

  public static final MessageReference NOTEXT = createMessageReference(UriParserException.class, "NOTEXT");
  public static final MessageReference NOMEDIARESOURCE = createMessageReference(UriParserException.class, "NOMEDIARESOURCE");
  public static final MessageReference NONAVIGATIONPROPERTY = createMessageReference(UriParserException.class, "NONAVIGATIONPROPERTY");
  public static final MessageReference MISSINGPARAMETER = createMessageReference(UriParserException.class, "MISSINGPARAMETER");

  public static final MessageReference MISSINGKEYPREDICATENAME = createMessageReference(UriParserException.class, "MISSINGKEYPREDICATENAME");
  public static final MessageReference DUPLICATEKEYNAMES = createMessageReference(UriParserException.class, "DUPLICATEKEYNAMES");

  public static final MessageReference EMPTYSEGMENT = createMessageReference(UriParserException.class, "EMPTYSEGMENT");
  public static final MessageReference NOTLASTSEGMENT = createMessageReference(UriParserException.class, "NOTLASTSEGMENT");
  public static final MessageReference MUSTBELASTSEGMENT = createMessageReference(UriParserException.class, "MUSTBELASTSEGMENT");
  public static final MessageReference INVALIDSEGMENT = createMessageReference(UriParserException.class, "INVALIDSEGMENT");

  public static final MessageReference INVALIDVALUE = createMessageReference(UriParserException.class, "INVALIDVALUE");
  public static final MessageReference INVALIDNULLVALUE = createMessageReference(UriParserException.class, "INVALIDNULLVALUE");
  public static final MessageReference INVALIDNEGATIVEVALUE = createMessageReference(UriParserException.class, "INVALIDNEGATIVEVALUE");
  public static final MessageReference INVALIDRETURNTYPE = createMessageReference(UriParserException.class, "INVALIDRETURNTYPE");
  public static final MessageReference INVALIDPROPERTYTYPE = createMessageReference(UriParserException.class, "INVALIDPROPERTYTYPE");
  public static final MessageReference INVALIDKEYPREDICATE = createMessageReference(UriParserException.class, "INVALIDKEYPREDICATE");
  public static final MessageReference INVALIDSYSTEMQUERYOPTION = createMessageReference(UriParserException.class, "INVALIDSYSTEMQUERYOPTION");

  public static final MessageReference LITERALFORMAT = createMessageReference(UriParserException.class, "LITERALFORMAT");
  public static final MessageReference UNKNOWNLITERAL = createMessageReference(UriParserException.class, "UNKNOWNLITERAL");

  public static final MessageReference INCOMPATIBLELITERAL = createMessageReference(UriParserException.class, "INCOMPATIBLELITERAL");
  public static final MessageReference INCOMPATIBLESYSTEMQUERYOPTION = createMessageReference(UriParserException.class, "INCOMPATIBLESYSTEMQUERYOPTION");

  public static final MessageReference NOTFOUND = createMessageReference(UriParserException.class, "NOTFOUND");
  public static final MessageReference PROPERTYNOTFOUND = createMessageReference(UriParserException.class, "PROPERTYNOTFOUND");
  public static final MessageReference ENTITYNOTFOUND = createMessageReference(UriParserException.class, "ENTITYNOTFOUND");
  public static final MessageReference CONTAINERNOTFOUND = createMessageReference(UriParserException.class, "CONTAINERNOTFOUND");

  
  /**
   * {@inheritDoc}
   */
  public UriParserException(MessageReference MessageReference) {
    super(MessageReference);
  }
  
  /**
   * {@inheritDoc}
   */
  public UriParserException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
