/**
 * (c) 2013 by SAP AG
 */
/**
 * Exception Classes used in the OData library as well as the implementing application
 * <p>APPLICATION DEVELOPERS: Please use {@link com.sap.core.odata.api.exception.ODataApplicationException} for custom exceptions.
 * 
 * <p><b>Exception handling:</b>
 * <br>Inside the OData library an ExceptionMapper exists which can transform any exception into an OData error format. 
 * The ExceptionMapper behaves after the following algorithm:
 * <br>1. The cause of the exception will be determined by looking into the stack trace.
 * <br>1.1. If the cause is an ODataApplicationException meaning that somewhere in the stack an ODataApplicationException is found the 
 * ExceptionMapper will take the following information from the ApplicationException and transform it into an OData error: 
 * message text, Locale, Inner Error and Error Code. There will be no altering of information for the ODataApplicationException.
 * <br>1.2. If no ODataApplicationException is found in the stack the cause can be three different types of exceptions: ODataHttpException, ODataMessageException or an uncaught RuntimeException.
 * <br>The ExceptionMapper will process them in the following order: 1. ODataHttpException, 2. ODataMessageException, 3 Other Exceptions.
 * <br>1.2.1. ODataHttpExceptions will be transformed as follows: If an error code is set it will be displayed. The HTTP status code will be derived from the ODataHttpException. The message text and its language depend on the AcceptLanguageHeaders. 
 * The first supported language which is found in the Headers will result in the language of the message and the response. 
 * <br>1.2.1. ODataMessageException will be transformed as follows: If an error code is set it will be displayed. The HTTP status code will be 500.
 * The message text and its language depend on the AcceptLanguageHeaders. The first supported language which is found in the Headers will result in the language of the message and the response. 
 * <br>1.2.1 Runtime Exceptions will be transformed as follows: No error code will be set. HTTP status will be 500. Message text will be taken from the exception and the language for the response will be English as default. 
 * <p><b>Exception Hierarchy</b>
 * <br> {@link com.sap.core.odata.api.exception.ODataException}
 * <br> *{@link com.sap.core.odata.api.exception.ODataApplicationException}
 * <br> *{@link com.sap.core.odata.api.exception.ODataMessageException}
 * <br> ** {@link com.sap.core.odata.api.edm.EdmException}
 * <br> ** {@link com.sap.core.odata.api.ep.EntityProviderException}
 * <br> ** {@link com.sap.core.odata.api.uri.expression.ExceptionVisitExpression}
 * <br> ** {@link com.sap.core.odata.api.exception.ODataHttpException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataConflictException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataForbiddenException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataMethodNotAllowedException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataNotAcceptableException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataNotImplementedException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataPreconditionFailedException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataPreconditionRequiredException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataServiceUnavailableException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataNotFoundException}
 * <br> **** {@link com.sap.core.odata.api.uri.UriNotMatchingException}
 * <br> *** {@link com.sap.core.odata.api.exception.ODataBadRequestException}
 * <br> **** {@link com.sap.core.odata.api.uri.expression.ExpressionParserException}
 * <br> **** {@link com.sap.core.odata.api.uri.UriSyntaxException}
 */
package com.sap.core.odata.api.exception;