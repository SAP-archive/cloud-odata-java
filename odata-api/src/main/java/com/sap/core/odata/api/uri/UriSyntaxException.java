package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;

/**
 * This exception results in a 400 Bad Request
 * @author SAP AG
 */
public class UriSyntaxException extends ODataBadRequestException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference URISYNTAX = createMessageReference(UriSyntaxException.class, "URISYNTAX");
  public static final MessageReference ENTITYSETINSTEADOFENTITY = createMessageReference(UriSyntaxException.class, "ENTITYSETINSTEADOFENTITY");

  public static final MessageReference NOTEXT = createMessageReference(UriSyntaxException.class, "NOTEXT");
  public static final MessageReference NOMEDIARESOURCE = createMessageReference(UriSyntaxException.class, "NOMEDIARESOURCE");
  public static final MessageReference NONAVIGATIONPROPERTY = createMessageReference(UriSyntaxException.class, "NONAVIGATIONPROPERTY");
  public static final MessageReference MISSINGPARAMETER = createMessageReference(UriSyntaxException.class, "MISSINGPARAMETER");

  public static final MessageReference MISSINGKEYPREDICATENAME = createMessageReference(UriSyntaxException.class, "MISSINGKEYPREDICATENAME");
  public static final MessageReference DUPLICATEKEYNAMES = createMessageReference(UriSyntaxException.class, "DUPLICATEKEYNAMES");

  public static final MessageReference EMPTYSEGMENT = createMessageReference(UriSyntaxException.class, "EMPTYSEGMENT");
  public static final MessageReference MUSTNOTBELASTSEGMENT = createMessageReference(UriSyntaxException.class, "NOTLASTSEGMENT");
  public static final MessageReference MUSTBELASTSEGMENT = createMessageReference(UriSyntaxException.class, "MUSTBELASTSEGMENT");
  public static final MessageReference INVALIDSEGMENT = createMessageReference(UriSyntaxException.class, "INVALIDSEGMENT");

  public static final MessageReference INVALIDVALUE = createMessageReference(UriSyntaxException.class, "INVALIDVALUE");
  public static final MessageReference INVALIDNULLVALUE = createMessageReference(UriSyntaxException.class, "INVALIDNULLVALUE");
  public static final MessageReference INVALIDNEGATIVEVALUE = createMessageReference(UriSyntaxException.class, "INVALIDNEGATIVEVALUE");
  public static final MessageReference INVALIDRETURNTYPE = createMessageReference(UriSyntaxException.class, "INVALIDRETURNTYPE");
  public static final MessageReference INVALIDPROPERTYTYPE = createMessageReference(UriSyntaxException.class, "INVALIDPROPERTYTYPE");
  public static final MessageReference INVALIDKEYPREDICATE = createMessageReference(UriSyntaxException.class, "INVALIDKEYPREDICATE");
  public static final MessageReference INVALIDSYSTEMQUERYOPTION = createMessageReference(UriSyntaxException.class, "INVALIDSYSTEMQUERYOPTION");

  public static final MessageReference INVALIDFILTEREXPRESSION = createMessageReference(UriSyntaxException.class, "INVALIDFILTEREXPRESSION");
  public static final MessageReference INVALIDORDERBYEXPRESSION = createMessageReference(UriSyntaxException.class, "INVALIDORDERBYEXPRESSION");

  public static final MessageReference LITERALFORMAT = createMessageReference(UriSyntaxException.class, "LITERALFORMAT");
  public static final MessageReference UNKNOWNLITERAL = createMessageReference(UriSyntaxException.class, "UNKNOWNLITERAL");

  public static final MessageReference INCOMPATIBLELITERAL = createMessageReference(UriSyntaxException.class, "INCOMPATIBLELITERAL");
  public static final MessageReference INCOMPATIBLESYSTEMQUERYOPTION = createMessageReference(UriSyntaxException.class, "INCOMPATIBLESYSTEMQUERYOPTION");
  
  /**
   * {@inheritDoc}
   */
  public UriSyntaxException(MessageReference MessageReference) {
    super(MessageReference);
  }
  
  /**
   * {@inheritDoc}
   */
  public UriSyntaxException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause);
  }
}
