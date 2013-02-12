/**
 * Exception Classes used in the OData library as well as the implementing application
 * <p>APPLICATION DEVELOPERS: Please use {@link com.sap.core.odata.api.exception.ODataApplicationException} for custom exceptions. 
 * <br>Language handling: Application exceptions contain a message and the corresponding locale to the message text. Both have to be set via the constructor. If an exception is serialized the application will always use the locale at the exception to determine the language. 
 * <br>Error code handling:If set an error code will be shown in the response to a client.
 * <br>Internationalization: The library uses messaged exceptions to determine the language of the exception message text. It can be influenced via the accept-language header.
 * <p>Exception Hierarchy
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