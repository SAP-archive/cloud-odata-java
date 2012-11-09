package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;

public class UriParserException extends ODataMessageException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final MessageReference EDM = createContext(UriParserException.class, "EDM");
  public static final MessageReference URISYNTAX = createContext(UriParserException.class, "URISYNTAX");
  public static final MessageReference MATCHPROBLEM = createContext(UriParserException.class, "MATCHPROBLEM");
  public static final MessageReference ENTITYSETINSTEADOFENTITY = createContext(UriParserException.class, "ENTITYSETINSTEADOFENTITY");

  public static final MessageReference NOTEXT = createContext(UriParserException.class, "NOTEXT");
  public static final MessageReference NOMEDIARESOURCE = createContext(UriParserException.class, "NOMEDIARESOURCE");
  public static final MessageReference NONAVIGATIONPROPERTY = createContext(UriParserException.class, "NONAVIGATIONPROPERTY");
  public static final MessageReference MISSINGPARAMETER = createContext(UriParserException.class, "MISSINGPARAMETER");

  public static final MessageReference MISSINGKEYPREDICATENAME = createContext(UriParserException.class, "MISSINGKEYPREDICATENAME");
  public static final MessageReference DUPLICATEKEYNAMES = createContext(UriParserException.class, "DUPLICATEKEYNAMES");

  public static final MessageReference EMPTYSEGMENT = createContext(UriParserException.class, "EMPTYSEGMENT");
  public static final MessageReference NOTLASTSEGMENT = createContext(UriParserException.class, "NOTLASTSEGMENT");
  public static final MessageReference INVALIDSEGMENT = createContext(UriParserException.class, "INVALIDSEGMENT");

  public static final MessageReference INVALIDVALUE = createContext(UriParserException.class, "INVALIDVALUE");
  public static final MessageReference INVALIDNULLVALUE = createContext(UriParserException.class, "INVALIDNULLVALUE");
  public static final MessageReference INVALIDNEGATIVEVALUE = createContext(UriParserException.class, "INVALIDNEGATIVEVALUE");
  public static final MessageReference INVALIDRETURNTYPE = createContext(UriParserException.class, "INVALIDRETURNTYPE");
  public static final MessageReference INVALIDPROPERTYTYPE = createContext(UriParserException.class, "INVALIDPROPERTYTYPE");
  public static final MessageReference INVALIDKEYPREDICATE = createContext(UriParserException.class, "INVALIDKEYPREDICATE");
  public static final MessageReference INVALIDSYSTEMQUERYOPTION = createContext(UriParserException.class, "INVALIDSYSTEMQUERYOPTION");

  public static final MessageReference LITERALFORMAT = createContext(UriParserException.class, "LITERALFORMAT");
  public static final MessageReference UNKNOWNLITERAL = createContext(UriParserException.class, "UNKNOWNLITERAL");

  public static final MessageReference INCOMPATIBLELITERAL = createContext(UriParserException.class, "INCOMPATIBLELITERAL");
  public static final MessageReference INCOMPATIBLESYSTEMQUERYOPTION = createContext(UriParserException.class, "INCOMPATIBLESYSTEMQUERYOPTION");

  public UriParserException(MessageReference context) {
    super(context);
  }
}
