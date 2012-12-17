package com.sap.core.odata.api.uri.expression;
/*TODO all*/
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataBadRequestException;

/**
 * @author SAP AG
 */
public class OrderByParserException extends ODataBadRequestException
{
  private static final long serialVersionUID = 1L;

  /*TODO add error texts to resource file*/

  //VON ABAP "An exception occurred"
 
  public static final MessageReference COMMON_ERROR = createMessageReference(OrderByParserException.class, "COMMON");

  /*instance attributes*/
  private CommonExpression filterTree;

  /*--Constructors--*/
  public OrderByParserException() {
    super(COMMON_ERROR);
  }

  public OrderByParserException(MessageReference messageReference) {
    super(messageReference);
  }

  public OrderByParserException(MessageReference messageReference, Throwable cause) {
    super(messageReference);
    this.initCause(cause);
  }

  /**
   * Get erroneous filter for debug information
   * @return Erroneous filter tree 
   */
  public CommonExpression getFilterTree() {
    return filterTree;
  }

  
  
  /**
   * See erroneous filter tree for debug information
   * @param filterTree
   */
  public OrderByParserException setFilterTree(CommonExpression filterTree)
  {
    this.filterTree = filterTree;
    return this;
  }
  
  public OrderByParserException setCause(Throwable tokenizerException) {
    this.initCause(tokenizerException);
    return this;
  }


  


}
