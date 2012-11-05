package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.exception.Context;
import com.sap.core.odata.api.exception.ODataMessageException;

public class UriParserException extends ODataMessageException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final Context EDM = createContext(UriParserException.class, "EDM");
  public static final Context URISYNTAX = createContext(UriParserException.class, "URISYNTAX");
  public static final Context MATCHPROBLEM = createContext(UriParserException.class, "MATCHPROBLEM");
  public static final Context ENTITYSETINSTEADOFENTITY = createContext(UriParserException.class, "ENTITYSETINSTEADOFENTITY");

  public static final Context NOTEXT = createContext(UriParserException.class, "NOTEXT");
  public static final Context NOMEDIARESOURCE = createContext(UriParserException.class, "NOMEDIARESOURCE");
  public static final Context NONAVIGATIONPROPERTY = createContext(UriParserException.class, "NONAVIGATIONPROPERTY");
  public static final Context MISSINGPARAMETER = createContext(UriParserException.class, "MISSINGPARAMETER");

  public static final Context MISSINGKEYPREDICATENAME = createContext(UriParserException.class, "MISSINGKEYPREDICATENAME");
  public static final Context DUPLICATEKEYNAMES = createContext(UriParserException.class, "DUPLICATEKEYNAMES");

  public static final Context EMPTYSEGMENT = createContext(UriParserException.class, "EMPTYSEGMENT");
  public static final Context NOTLASTSEGMENT = createContext(UriParserException.class, "NOTLASTSEGMENT");
  public static final Context INVALIDSEGMENT = createContext(UriParserException.class, "INVALIDSEGMENT");

  public static final Context INVALIDVALUE = createContext(UriParserException.class, "INVALIDVALUE");
  public static final Context INVALIDNULLVALUE = createContext(UriParserException.class, "INVALIDNULLVALUE");
  public static final Context INVALIDNEGATIVEVALUE = createContext(UriParserException.class, "INVALIDNEGATIVEVALUE");
  public static final Context INVALIDRETURNTYPE = createContext(UriParserException.class, "INVALIDRETURNTYPE");
  public static final Context INVALIDPROPERTYTYPE = createContext(UriParserException.class, "INVALIDPROPERTYTYPE");
  public static final Context INVALIDKEYPREDICATE = createContext(UriParserException.class, "INVALIDKEYPREDICATE");
  public static final Context INVALIDSYSTEMQUERYOPTION = createContext(UriParserException.class, "INVALIDSYSTEMQUERYOPTION");

  public static final Context LITERALFORMAT = createContext(UriParserException.class, "LITERALFORMAT");
  public static final Context UNKNOWNLITERAL = createContext(UriParserException.class, "UNKNOWNLITERAL");

  public static final Context INCOMPATIBLELITERAL = createContext(UriParserException.class, "INCOMPATIBLELITERAL");
  public static final Context INCOMPATIBLESYSTEMQUERYOPTION = createContext(UriParserException.class, "INCOMPATIBLESYSTEMQUERYOPTION");

  public UriParserException(Context context) {
    super(context);
  }
}
